package com.github.velocity.bridge.event.mapping.server;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.server.BridgeServer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class ServerConnectedMapping extends EventMapping<ServerConnectedEvent, net.md_5.bungee.api.event.ServerConnectedEvent> {

    private final ProxyServer proxyServer;

    public ServerConnectedMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ServerConnectedEvent.class, net.md_5.bungee.api.event.ServerConnectedEvent.class, PostOrder.EARLY);

        this.proxyServer = plugin.getServer();
    }

    @Override
    protected net.md_5.bungee.api.event.ServerConnectedEvent preparation(ServerConnectedEvent serverConnectedEvent) {
        ProxiedPlayer bridgePlayer = BridgeProxiedPlayer.fromVelocity(this.proxyServer, serverConnectedEvent.getPlayer());
        Server bridgeServer = new BridgeServer(this.proxyServer, serverConnectedEvent.getServer());
        return new net.md_5.bungee.api.event.ServerConnectedEvent(bridgePlayer, bridgeServer);
    }
}
