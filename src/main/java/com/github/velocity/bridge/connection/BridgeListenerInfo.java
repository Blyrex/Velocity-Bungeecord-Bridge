package com.github.velocity.bridge.connection;

import com.velocitypowered.api.proxy.InboundConnection;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.config.ListenerInfo;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BridgeListenerInfo {

    public static ListenerInfo constructListenerInfo(InboundConnection connection, ProxyServer proxyServer) {
        return constructListenerInfo(connection.getRemoteAddress(), proxyServer);
    }

    public static ListenerInfo constructListenerInfo(SocketAddress socketAddress, ProxyServer proxyServer) {
        ProxyConfig proxyConfig = proxyServer.getConfiguration();

        Map<String, String> forcedHosts = new HashMap<>();

        proxyConfig.getForcedHosts().forEach((key, value) -> forcedHosts.put(key, value.get(0)));

        return new ListenerInfo(
                socketAddress,
                LegacyComponentSerializer.legacySection().serialize(proxyConfig.getMotd()),
                proxyConfig.getShowMaxPlayers(),
                proxyConfig.getShowMaxPlayers(),
                new ArrayList<>(proxyConfig.getServers().values()),
                false,
                forcedHosts,
                "GLOBAL_PING",
                true,
                false,
                25577,
                false,
                false
        );
    }

}
