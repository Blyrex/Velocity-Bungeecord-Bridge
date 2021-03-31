package com.github.velocity.bridge.server;

import com.github.velocity.bridge.BridgeProxyServer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

public class BridgeServer extends BridgeServerInfo implements Server {

    private final ProxyServer proxyServer;

    public BridgeServer(ProxyServer proxyServer, RegisteredServer registeredServer) {
        super(proxyServer, registeredServer);

        this.proxyServer = proxyServer;
    }

    @Override
    public ServerInfo getInfo() {
        return this;
    }

    @Override
    public void disconnect(String reason) {}

    @Override
    public void disconnect(BaseComponent... reason) {}

    @Override
    public void disconnect(BaseComponent reason) {}

    @Override
    public boolean isConnected() {
        return this.proxyServer.getServer(this.getName()).isPresent();
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException(BridgeProxyServer.NOT_SUPPORTED);
    }
}
