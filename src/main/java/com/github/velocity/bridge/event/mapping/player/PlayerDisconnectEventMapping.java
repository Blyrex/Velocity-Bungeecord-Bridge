package com.github.velocity.bridge.event.mapping.player;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;

public class PlayerDisconnectEventMapping extends EventMapping<DisconnectEvent, PlayerDisconnectEvent> {


    public PlayerDisconnectEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, DisconnectEvent.class, PlayerDisconnectEvent.class, PostOrder.NORMAL);
    }

    @Override
    protected PlayerDisconnectEvent preparation(DisconnectEvent disconnectEvent) {
        return new PlayerDisconnectEvent(BridgeProxiedPlayer.fromVelocity(super.proxyServer, disconnectEvent.getPlayer()));
    }
}
