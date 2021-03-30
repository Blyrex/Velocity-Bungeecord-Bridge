package com.github.velocity.bridge.connection;

import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.connection.PendingConnection;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.UUID;

@RequiredArgsConstructor
public class BridgePendingConnection implements PendingConnection {

    private final Player player;

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
        return null;
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
