package com.github.velocity.bridge.connection;

import com.velocitypowered.api.proxy.InboundConnection;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.protocol.packet.Handshake;

@RequiredArgsConstructor
public class BridgeHandshake extends Handshake {

    private final InboundConnection connection;

    @Override
    public int getProtocolVersion() {
        return this.connection.getProtocolVersion().getProtocol();
    }

    @Override
    public String getHost() {
        return this.connection.getRemoteAddress().getHostName();
    }

    @Override
    public int getPort() {
        return this.connection.getRemoteAddress().getPort();
    }

    @Override
    public int getRequestedProtocol() {
        return this.connection.getProtocolVersion().getProtocol();
    }
}
