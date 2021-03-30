package com.github.velocity.bridge.connection;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.config.ProxyConfig;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class BridgePendingConnection implements PendingConnection {
    private final Player player;
    private final ProxyServer proxyServer;

    @Override
    public String getName() {
        return this.player.getUsername();
    }

    @Override
    public int getVersion() {
        return this.player.getProtocolVersion().getProtocol();
    }

    @Override
    public InetSocketAddress getVirtualHost() {
        return this.player.getVirtualHost().orElse(null);
    }

    @Override
    public ListenerInfo getListener() {
        ProxyConfig proxyConfig = this.proxyServer.getConfiguration();

        Map<String, String> forcedHosts = new HashMap<>();

        proxyConfig.getForcedHosts().forEach((key, value) -> {
            forcedHosts.put(key, value.get(0));
        });

        return new ListenerInfo(
                this.getSocketAddress(),
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

    @Override
    public String getUUID() {
        return this.getUniqueId().toString();
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public void setUniqueId(UUID uuid) {
    }

    @Override
    public boolean isOnlineMode() {
        return this.player.isOnlineMode();
    }

    @Override
    public void setOnlineMode(boolean onlineMode) {
    }

    @Override
    public boolean isLegacy() {
        return false;
    }

    @Override
    public InetSocketAddress getAddress() {
        return this.player.getRemoteAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return this.player.getRemoteAddress();
    }

    @Override
    public void disconnect(String reason) {
        this.disconnect(TextComponent.fromLegacyText(reason));
    }

    @Override
    public void disconnect(BaseComponent... reason) {
        this.player.disconnect(BungeeComponentSerializer.legacy().deserialize(reason));
    }

    @Override
    public void disconnect(BaseComponent reason) {
        this.disconnect(new BaseComponent[]{reason});
    }

    @Override
    public boolean isConnected() {
        return this.player.isActive();
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }
}
