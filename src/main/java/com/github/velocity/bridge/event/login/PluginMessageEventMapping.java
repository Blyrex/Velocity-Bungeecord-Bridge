package com.github.velocity.bridge.event.login;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.connection.PluginMessageEvent;

public final class PluginMessageEventMapping extends EventMapping<PluginMessageEvent, net.md_5.bungee.api.event.PluginMessageEvent> {

    public PluginMessageEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, PluginMessageEvent.class, net.md_5.bungee.api.event.PluginMessageEvent.class, PostOrder.NORMAL);
    }

    @Override
    protected net.md_5.bungee.api.event.PluginMessageEvent preparation(PluginMessageEvent pluginMessageEvent) {
        return new net.md_5.bungee.api.event.PluginMessageEvent(
                null,
                null,
                pluginMessageEvent.getIdentifier().getId(),
                pluginMessageEvent.getData()
        );
    }
}
