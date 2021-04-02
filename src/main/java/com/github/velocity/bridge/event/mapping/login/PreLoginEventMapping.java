package com.github.velocity.bridge.event.mapping.login;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.connection.BridgePendingConnection;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;

public final class PreLoginEventMapping extends EventMapping<LoginEvent, PreLoginEvent> {

    public PreLoginEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, LoginEvent.class, PreLoginEvent.class, PostOrder.FIRST);
    }

    @Override
    public PreLoginEvent preparation(LoginEvent event) {
        Player player = event.getPlayer();
        PendingConnection pendingConnection = new BridgePendingConnection(event.getPlayer(), super.proxyServer);
        return new PreLoginEvent(pendingConnection, (result, error) -> {
            if (result.isCancelled()) {
                player.disconnect(BungeeComponentSerializer.legacy().deserialize(result.getCancelReasonComponents()));
            }
        });
    }
}
