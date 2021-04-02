package com.github.velocity.bridge.event.mapping;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.event.EventMapping;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.proxy.ProxyReloadEvent;

public class ProxyReloadEventMapping extends EventMapping<ProxyReloadEvent, net.md_5.bungee.api.event.ProxyReloadEvent> {

    public ProxyReloadEventMapping(BungeeVelocityBridgePlugin plugin) {
        super(plugin, ProxyReloadEvent.class, net.md_5.bungee.api.event.ProxyReloadEvent.class, PostOrder.NORMAL);
    }

    @Override
    protected net.md_5.bungee.api.event.ProxyReloadEvent preparation(ProxyReloadEvent proxyReloadEvent) {
        return new net.md_5.bungee.api.event.ProxyReloadEvent(super.plugin.getBungeeProxyServer().getConsole());
    }
}
