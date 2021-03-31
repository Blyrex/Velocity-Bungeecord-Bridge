package com.github.velocity.bridge.connection;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.UUID;

public class BridgePendingConnection extends BridgeSimplePendingConnection implements PendingConnection {

    private final Player player;

    public BridgePendingConnection(Player player, ProxyServer proxyServer) {
        super(player, proxyServer);

        this.player = player;
    }

    @Override
    public String getName() {
        return this.player.getUsername();
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
    public boolean isOnlineMode() {
        return this.player.isOnlineMode();
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

}
