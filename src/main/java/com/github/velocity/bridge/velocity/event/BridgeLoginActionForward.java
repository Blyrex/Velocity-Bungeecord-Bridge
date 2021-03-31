package com.github.velocity.bridge.velocity.event;

import com.github.velocity.bridge.connection.BridgePendingConnection;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.PreLoginEvent;

@RequiredArgsConstructor
public final class BridgeLoginActionForward {

    private final ProxyServer proxyServer;
    private final com.velocitypowered.api.proxy.ProxyServer velocityProxyServer;

    @Subscribe(order = PostOrder.EARLY)
    public void playerPreLogin(LoginEvent event) {
        Player player = event.getPlayer();
        PendingConnection pendingConnection = new BridgePendingConnection(event.getPlayer(), this.velocityProxyServer);
        PreLoginEvent preLoginEvent = new PreLoginEvent(pendingConnection, (result, error) -> {
            if (result.isCancelled()) {
                player.disconnect(BungeeComponentSerializer.legacy().deserialize(result.getCancelReasonComponents()));
            }
        });
        this.proxyServer.getPluginManager().callEvent(preLoginEvent);
    }

    @Subscribe(order = PostOrder.NORMAL)
    public void playerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        PendingConnection pendingConnection = new BridgePendingConnection(event.getPlayer(), this.velocityProxyServer);
        net.md_5.bungee.api.event.LoginEvent loginEvent = new net.md_5.bungee.api.event.LoginEvent(pendingConnection, (result, error) -> {
            if (result.isCancelled()) {
                player.disconnect(BungeeComponentSerializer.legacy().deserialize(result.getCancelReasonComponents()));
            }
        });
        this.proxyServer.getPluginManager().callEvent(loginEvent);
    }

    @Subscribe(order = PostOrder.LATE)
    public void playerPostLogin(PostLoginEvent event) {
        net.md_5.bungee.api.event.PostLoginEvent postLoginEvent = new net.md_5.bungee.api.event
                .PostLoginEvent(BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, event.getPlayer()));
        this.proxyServer.getPluginManager().callEvent(postLoginEvent);
    }
}
