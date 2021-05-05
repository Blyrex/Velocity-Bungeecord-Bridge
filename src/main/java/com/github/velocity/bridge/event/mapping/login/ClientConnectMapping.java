package com.github.velocity.bridge.event.mapping.login;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.connection.BridgeListenerInfo;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.event.ClientConnectEvent;

public class ClientConnectMapping extends EventMapping<PreLoginEvent, ClientConnectEvent> {

    public ClientConnectMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, PreLoginEvent.class, ClientConnectEvent.class, PostOrder.EARLY);
    }

    @Override
    protected ClientConnectEvent preparation(PreLoginEvent preLoginEvent) {
        ListenerInfo listenerInfo = BridgeListenerInfo
                .constructListenerInfo(preLoginEvent.getConnection(), super.proxyServer);
        return new ClientConnectEvent(preLoginEvent.getConnection().getRemoteAddress(), listenerInfo);
    }

    @Override
    protected void done(PreLoginEvent preLoginEvent, ClientConnectEvent clientConnectEvent) {
        if (clientConnectEvent.isCancelled()) {
            preLoginEvent.setResult(PreLoginEvent.PreLoginComponentResult.denied(Component.empty()));
        }
    }
}
