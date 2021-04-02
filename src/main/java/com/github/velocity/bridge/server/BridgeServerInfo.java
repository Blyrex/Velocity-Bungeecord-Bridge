package com.github.velocity.bridge.server;

import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.SneakyThrows;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.stream.Collectors;

public class BridgeServerInfo implements ServerInfo {

    public static String EMPTY_STRING = "";

    private final ProxyServer proxyServer;
    private final RegisteredServer registeredServer;

    public BridgeServerInfo(ProxyServer proxyServer, RegisteredServer registeredServer) {
        this.proxyServer = proxyServer;
        this.registeredServer = registeredServer;
    }

    @Override
    public String getName() {
        return this.registeredServer.getServerInfo().getName();
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.registeredServer.getServerInfo().getAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return this.registeredServer.getServerInfo().getAddress();
    }

    @Override
    public Collection<ProxiedPlayer> getPlayers() {
        return this.registeredServer.getPlayersConnected()
                .stream()
                .map(player -> BridgeProxiedPlayer.fromVelocity(this.proxyServer, player))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public String getMotd() {
        return LegacyComponentSerializer.legacySection().serialize(this.registeredServer.ping().get().getDescriptionComponent());
    }

    @Override
    public boolean isRestricted() {
        return false;
    }

    @Override
    public String getPermission() {
        return EMPTY_STRING;
    }

    @Override
    public boolean canAccess(CommandSender sender) {
        return true;
    }

    @Override
    public void sendData(String channel, byte[] data) {
        this.registeredServer.sendPluginMessage(MinecraftChannelIdentifier.forDefaultNamespace(channel), data);
    }

    @Override
    public boolean sendData(String channel, byte[] data, boolean queue) {
        this.sendData(channel, data);
        return true;
    }

    @Override
    public void ping(Callback<ServerPing> callback) {
        this.registeredServer.ping().thenAccept(serverPing -> callback.done(
                new ServerPing(
                        new ServerPing.Protocol(
                                serverPing.getVersion().getName(), serverPing.getVersion().getProtocol()
                        ),
                        new ServerPing.Players(1, 1, null),
                        BungeeComponentSerializer.legacy().serialize(serverPing.getDescriptionComponent())[0],
                        Favicon.create(serverPing.getFavicon().get().getBase64Url())
                ),
                null
        )).exceptionally(throwable -> {
            callback.done(null, throwable);
            return null;
        });
    }
}
