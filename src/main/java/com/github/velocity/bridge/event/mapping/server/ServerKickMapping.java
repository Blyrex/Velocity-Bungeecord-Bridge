package com.github.velocity.bridge.event.mapping.server;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.server.BridgeServerInfo;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.KickedFromServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerKickEvent;

public class ServerKickMapping extends EventMapping<KickedFromServerEvent, ServerKickEvent> {

    private final ProxyServer proxyServer;

    public ServerKickMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, KickedFromServerEvent.class, ServerKickEvent.class, PostOrder.FIRST);

        this.proxyServer = plugin.getServer();
    }

    @Override
    protected ServerKickEvent preparation(KickedFromServerEvent kickedFromServerEvent) {
        ProxiedPlayer proxiedPlayer = BridgeProxiedPlayer.fromVelocity(this.proxyServer, kickedFromServerEvent.getPlayer());

        Component kickReason = kickedFromServerEvent.getServerKickReason().orElse(null);
        BaseComponent[] bungeeKickReason;
        if (kickReason == null) {
            bungeeKickReason = new BaseComponent[]{};
        } else {
            bungeeKickReason = BungeeComponentSerializer.legacy().serialize(kickReason);
        }

        return new net.md_5.bungee.api.event.ServerKickEvent(
                proxiedPlayer,
                new BridgeServerInfo(this.proxyServer, kickedFromServerEvent.getServer()),
                bungeeKickReason,
                null,
                ServerKickEvent.State.UNKNOWN);
    }

    @Override
    protected void done(KickedFromServerEvent kickedFromServerEvent, ServerKickEvent serverKickEvent) {
        if (!serverKickEvent.isCancelled()) {
            return;
        }
        ServerInfo cancelServer = serverKickEvent.getCancelServer();
        if (cancelServer == null) {
            return;
        }
        this.proxyServer.getServer(cancelServer.getName())
                .ifPresent(registeredServer -> kickedFromServerEvent.setResult(
                        KickedFromServerEvent.RedirectPlayer.create(
                                registeredServer,
                                BungeeComponentSerializer.legacy().deserialize(serverKickEvent.getKickReasonComponent()
                                )
                        )));
    }
}
