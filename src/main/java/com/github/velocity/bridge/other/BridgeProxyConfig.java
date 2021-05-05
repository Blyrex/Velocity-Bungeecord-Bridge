package com.github.velocity.bridge.other;

import com.github.velocity.bridge.connection.BridgeListenerInfo;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.Favicon;
import net.md_5.bungee.api.ProxyConfig;
import net.md_5.bungee.api.config.ListenerInfo;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BridgeProxyConfig implements ProxyConfig {

    private final ProxyServer proxyServer;
    private final com.velocitypowered.api.proxy.config.ProxyConfig proxyConfig;

    @Override
    public int getTimeout() {
        return this.proxyConfig.getConnectTimeout();
    }

    @Override
    public String getUuid() {
        return null;
    }

    @Override
    public Collection<ListenerInfo> getListeners() {
        return this.proxyServer.getAllServers().stream().map(server ->
                BridgeListenerInfo.constructListenerInfo(server.getServerInfo().getAddress(), this.proxyServer))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, ServerInfo> getServers() {
        return net.md_5.bungee.api.ProxyServer.getInstance().getServers();
    }

    @Override
    public boolean isOnlineMode() {
        return this.proxyConfig.isOnlineMode();
    }

    @Override
    public boolean isLogCommands() {
        return true;
    }

    @Override
    public int getRemotePingCache() {
        return 0;
    }

    @Override
    public int getPlayerLimit() {
        return this.proxyConfig.getShowMaxPlayers();
    }

    @Override
    public Collection<String> getDisabledCommands() {
        return Collections.emptyList();
    }

    @Override
    public int getServerConnectTimeout() {
        return this.proxyConfig.getConnectTimeout();
    }

    @Override
    public int getRemotePingTimeout() {
        return this.proxyConfig.getConnectTimeout();
    }

    @Override
    public int getThrottle() {
        return 0;
    }

    @Override
    public boolean isIpForward() {
        return false;
    }

    @Override
    public String getFavicon() {
        return this.getFaviconObject().getEncoded();
    }

    @Override
    public Favicon getFaviconObject() {
        com.velocitypowered.api.util.Favicon favicon = this.proxyConfig.getFavicon().orElse(null);
        if (favicon == null) {
            return null;
        }
        return Favicon.create(favicon.getBase64Url());
    }
}
