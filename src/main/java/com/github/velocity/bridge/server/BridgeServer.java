package com.github.velocity.bridge.server;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.Server;

public class BridgeServer extends BridgeServerInfo implements Server {

    public BridgeServer(ProxyServer proxyServer, RegisteredServer registeredServer) {
        super(proxyServer, registeredServer);
    }

    @Override
    public ServerInfo getInfo() {
        return this;
    }

    @Override
    public void disconnect(String reason) {

    }

    @Override
    public void disconnect(BaseComponent... reason) {

    }

    @Override
    public void disconnect(BaseComponent reason) {

    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException("This feature is not supported by velocity, so the BungeeVelocityBridge cannot support it either");
    }
}
