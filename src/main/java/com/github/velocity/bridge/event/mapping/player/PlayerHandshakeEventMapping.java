package com.github.velocity.bridge.event.mapping.player;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.connection.BridgeHandshake;
import com.github.velocity.bridge.connection.BridgeSimplePendingConnection;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.ConnectionHandshakeEvent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PlayerHandshakeEvent;

public class PlayerHandshakeEventMapping extends EventMapping<ConnectionHandshakeEvent, PlayerHandshakeEvent> {

    public PlayerHandshakeEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ConnectionHandshakeEvent.class, PlayerHandshakeEvent.class, PostOrder.FIRST);
    }

    @Override
    protected PlayerHandshakeEvent preparation(ConnectionHandshakeEvent connectionHandshakeEvent) {
        PendingConnection pendingConnection = new BridgeSimplePendingConnection(connectionHandshakeEvent.getConnection(), super.proxyServer);
        BridgeHandshake handshake = new BridgeHandshake(connectionHandshakeEvent.getConnection());
        return new PlayerHandshakeEvent(pendingConnection, handshake);
    }
}
