package com.github.velocity.bridge.event.mapping.server;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.server.BridgeServer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;

public class ServerSwitchMapping extends EventMapping<ServerPreConnectEvent, ServerSwitchEvent> {

    private final ProxyServer proxyServer;

    public ServerSwitchMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ServerPreConnectEvent.class, ServerSwitchEvent.class, PostOrder.NORMAL);

        this.proxyServer = plugin.getServer();
    }

    @Override
    protected ServerSwitchEvent preparation(ServerPreConnectEvent serverPreConnectEvent) {
        Player velocityPlayer = serverPreConnectEvent.getPlayer();

        ProxiedPlayer bridgePlayer = BridgeProxiedPlayer.fromVelocity(this.proxyServer, velocityPlayer);
        ServerConnection serverConnection = velocityPlayer.getCurrentServer().orElse(null);
        RegisteredServer registeredServer = serverPreConnectEvent.getOriginalServer();
        if(serverConnection != null) {
            registeredServer = serverConnection.getServer();
        }
        Server bridgeServer = new BridgeServer(this.proxyServer, registeredServer);
        return new net.md_5.bungee.api.event.ServerSwitchEvent(bridgePlayer, bridgeServer.getInfo());
    }
}
