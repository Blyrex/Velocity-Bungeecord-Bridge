package com.github.velocity.bridge.event.login;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.LoginEvent;
import net.md_5.bungee.api.event.PostLoginEvent;

public final class PostLoginEventMapping extends EventMapping<LoginEvent, PostLoginEvent> {
    public PostLoginEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, LoginEvent.class, PostLoginEvent.class, PostOrder.LAST);
    }

    @Override
    protected PostLoginEvent preparation(LoginEvent event) {
        return new PostLoginEvent(BridgeProxiedPlayer.fromVelocity(super.plugin.getServer(), event.getPlayer()));
    }
}
