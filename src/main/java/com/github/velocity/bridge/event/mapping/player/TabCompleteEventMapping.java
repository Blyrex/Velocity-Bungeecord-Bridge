package com.github.velocity.bridge.event.mapping.player;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class TabCompleteEventMapping extends EventMapping<TabCompleteEvent, net.md_5.bungee.api.event.TabCompleteEvent> {

    public TabCompleteEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, TabCompleteEvent.class, net.md_5.bungee.api.event.TabCompleteEvent.class, PostOrder.FIRST);
    }

    @Override
    protected net.md_5.bungee.api.event.TabCompleteEvent preparation(TabCompleteEvent tabCompleteEvent) {
        ProxiedPlayer proxiedPlayer = BridgeProxiedPlayer.fromVelocity(super.proxyServer, tabCompleteEvent.getPlayer());
        return new net.md_5.bungee.api.event.TabCompleteEvent(
                proxiedPlayer.getServer(),
                proxiedPlayer,
                tabCompleteEvent.getPartialMessage(),
                tabCompleteEvent.getSuggestions()
        );
    }

    @Override
    protected void done(TabCompleteEvent tabCompleteEvent, net.md_5.bungee.api.event.TabCompleteEvent bungeeTabCompleteEvent) {
        if(bungeeTabCompleteEvent.isCancelled()) {
            tabCompleteEvent.getSuggestions().clear();
        }
    }
}
