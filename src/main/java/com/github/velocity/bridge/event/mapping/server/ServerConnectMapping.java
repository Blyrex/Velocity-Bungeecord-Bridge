package com.github.velocity.bridge.event.mapping.server;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.server.BridgeServer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;

public class ServerConnectMapping extends EventMapping<ServerPreConnectEvent, ServerConnectEvent> {

    private final ProxyServer proxyServer;

    public ServerConnectMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ServerPreConnectEvent.class, net.md_5.bungee.api.event.ServerConnectEvent.class, PostOrder.EARLY);

        this.proxyServer = plugin.getServer();
    }

    @Override
    protected ServerConnectEvent preparation(ServerPreConnectEvent serverConnectEvent) {
        ProxiedPlayer bridgePlayer = BridgeProxiedPlayer.fromVelocity(this.proxyServer, serverConnectEvent.getPlayer());
        Server bridgeServer = new BridgeServer(this.proxyServer, serverConnectEvent.getResult().getServer().orElse(null));
        return new ServerConnectEvent(bridgePlayer, bridgeServer.getInfo(), ServerConnectEvent.Reason.UNKNOWN, null);
    }

    @Override
    protected void done(ServerPreConnectEvent serverPreConnectEvent, ServerConnectEvent serverConnectEvent) {
        if(serverConnectEvent.isCancelled()) {
            serverPreConnectEvent.setResult(ServerPreConnectEvent.ServerResult.denied());
        }
    }
}
