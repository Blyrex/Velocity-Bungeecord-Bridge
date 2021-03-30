package com.github.velocity.bridge;

import com.github.velocity.bridge.velocity.BridgeEventListener;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "bungeevelocitybridge",
        name = "BungeeVelocityBridge",
        version = "1.0.0-SNAPSHOT",
        authors = {"Blyrex", "0utplay"}
)
@Getter
public class BungeeVelocityBridgePlugin {
    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private net.md_5.bungee.api.ProxyServer bungeeProxyServer;

    @Inject
    public BungeeVelocityBridgePlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void proxyInitialisation(ProxyInitializeEvent event) {
        this.bungeeProxyServer = new ProxyServerBridge(this);
        this.server.getEventManager().register(this, new BridgeEventListener(bungeeProxyServer, this.server));
    }
}
