package com.github.velocity.bridge.velocity.event.login;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.connection.BridgePendingConnection;
import com.github.velocity.bridge.velocity.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.connection.PendingConnection;

public final class LoginEventMapping extends EventMapping<LoginEvent, net.md_5.bungee.api.event.LoginEvent> {
    public LoginEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, LoginEvent.class, net.md_5.bungee.api.event.LoginEvent.class, PostOrder.NORMAL);
    }

    @Override
    protected net.md_5.bungee.api.event.LoginEvent preparation(LoginEvent event) {
        Player player = event.getPlayer();
        PendingConnection pendingConnection = new BridgePendingConnection(event.getPlayer(), super.plugin.getServer());
        return new net.md_5.bungee.api.event.LoginEvent(pendingConnection, (result, error) -> {
            if (result.isCancelled()) {
                player.disconnect(BungeeComponentSerializer.legacy().deserialize(result.getCancelReasonComponents()));
            }
        });
    }
}
