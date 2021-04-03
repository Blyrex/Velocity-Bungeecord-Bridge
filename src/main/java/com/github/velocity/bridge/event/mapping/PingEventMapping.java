package com.github.velocity.bridge.event.mapping;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.connection.BridgeSimplePendingConnection;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.util.Favicon;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PingEventMapping extends EventMapping<ProxyPingEvent, net.md_5.bungee.api.event.ProxyPingEvent> {

    public PingEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ProxyPingEvent.class, net.md_5.bungee.api.event.ProxyPingEvent.class, PostOrder.FIRST);
    }

    @Override
    protected net.md_5.bungee.api.event.ProxyPingEvent preparation(ProxyPingEvent proxyPingEvent) {
        com.velocitypowered.api.proxy.server.ServerPing.Version velocityProtocol = proxyPingEvent.getPing().getVersion();
        ServerPing.Protocol protocol = new ServerPing.Protocol(velocityProtocol.getName(), velocityProtocol.getProtocol());

        List<Player> allPlayers = new ArrayList<>(super.proxyServer.getAllPlayers());
        ServerPing.PlayerInfo[] playerInfos = new ServerPing.PlayerInfo[allPlayers.size()];
        for (int i = 0; i < allPlayers.size(); i++) {
            Player player = allPlayers.get(i);
            playerInfos[i] = new ServerPing.PlayerInfo(player.getUsername(), player.getUniqueId());
        }

        ServerPing.Players players = new ServerPing.Players(
                this.proxyServer.getConfiguration().getShowMaxPlayers(),
                this.proxyServer.getPlayerCount(),
                playerInfos
        );

        String description = LegacyComponentSerializer.legacySection().serialize(proxyPingEvent.getPing().getDescriptionComponent());
        Favicon velocityIcon = proxyPingEvent.getPing().getFavicon().orElse(null);

        ServerPing serverPing = new ServerPing(
                protocol,
                players,
                TextComponent.fromLegacyText(description)[0],
                velocityIcon == null ? null : net.md_5.bungee.api.Favicon.create(velocityIcon.getBase64Url()));

        PendingConnection pendingConnection = new BridgeSimplePendingConnection(proxyPingEvent.getConnection(), super.proxyServer);

        return new net.md_5.bungee.api.event.ProxyPingEvent(pendingConnection, serverPing, (result, error) -> {
        });
    }

    @Override
    protected void done(ProxyPingEvent proxyPingEvent, net.md_5.bungee.api.event.ProxyPingEvent bungeeProxyPingEvent) {
        ServerPing bungeeResponse = bungeeProxyPingEvent.getResponse();

        com.velocitypowered.api.proxy.server.ServerPing.Builder serverPingBuilder = com.velocitypowered.api.proxy.server.ServerPing
                .builder()
                .description(BungeeComponentSerializer.legacy().deserialize(new BaseComponent[]{bungeeResponse.getDescriptionComponent()}))
                .maximumPlayers(bungeeResponse.getPlayers().getMax())
                .onlinePlayers(bungeeResponse.getPlayers().getOnline())
                .version(new com.velocitypowered.api.proxy.server.ServerPing.Version(
                        bungeeResponse.getVersion().getProtocol(),
                        bungeeResponse.getVersion().getName()
                ));

        if (bungeeResponse.getFaviconObject() != null) {
            serverPingBuilder.favicon(new Favicon(bungeeResponse.getFaviconObject().getEncoded()));
        }
        com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer[] players = Arrays.stream(bungeeResponse.getPlayers().getSample())
                .map(playerInfo -> new com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer(playerInfo.getName(), playerInfo.getUniqueId()))
                .toArray(com.velocitypowered.api.proxy.server.ServerPing.SamplePlayer[]::new);
        serverPingBuilder.samplePlayers(players);

        proxyPingEvent.setPing(serverPingBuilder.build());
    }
}
