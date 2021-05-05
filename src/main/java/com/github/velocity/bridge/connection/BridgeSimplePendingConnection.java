package com.github.velocity.bridge.connection;

import com.github.velocity.bridge.BridgeProxyServer;
import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

@RequiredArgsConstructor
public class BridgeSimplePendingConnection implements PendingConnection {

    private final InboundConnection connection;
    private final ProxyServer proxyServer;

    @Override
    public String getName() {
        throw new UnsupportedOperationException(BridgeProxyServer.NOT_SUPPORTED);
    }

    @Override
    public int getVersion() {
        return this.connection.getProtocolVersion().getProtocol();
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        return this.connection.getVirtualHost().orElse(null);
    }

    @Override
    public ListenerInfo getListener() {
        return BridgeListenerInfo.constructListenerInfo(this.connection, this.proxyServer);
    }

    @Override
    public String getUUID() {
        throw new UnsupportedOperationException(BridgeProxyServer.NOT_SUPPORTED);
    }

    @Override
    public UUID getUniqueId() {
        throw new UnsupportedOperationException(BridgeProxyServer.NOT_SUPPORTED);
    }

    @Override
    public void setUniqueId(UUID uuid) {}

    @Override
    public boolean isOnlineMode() {
        return true;
    }

    @Override
    public void setOnlineMode(boolean onlineMode) {}

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.connection.getRemoteAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return this.connection.getRemoteAddress();
    }

    @Override
    public void disconnect(String reason) {}

    @Override
    public void disconnect(BaseComponent... reason) {}

    @Override
    public void disconnect(BaseComponent reason) {}

    @Override
    public boolean isConnected() {
        return this.connection.isActive();
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException(BridgeProxyServer.NOT_SUPPORTED);
    }
}
