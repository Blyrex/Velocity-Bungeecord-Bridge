package com.github.velocity.bridge.event.mapping.player;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.PlayerSettingsChangedEvent;
import net.md_5.bungee.api.event.SettingsChangedEvent;

public class PlayerSettingsEventMapping extends EventMapping<PlayerSettingsChangedEvent, SettingsChangedEvent> {

    public PlayerSettingsEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, PlayerSettingsChangedEvent.class, SettingsChangedEvent.class, PostOrder.NORMAL);
    }

    @Override
    protected SettingsChangedEvent preparation(PlayerSettingsChangedEvent playerSettingsChangedEvent) {
        return new SettingsChangedEvent(BridgeProxiedPlayer.fromVelocity(super.proxyServer, playerSettingsChangedEvent.getPlayer()));
    }
}
