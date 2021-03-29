package com.github.velocity.bridge;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;

import java.util.logging.Logger;

@Plugin(
        id = "bungeevelocitybridge",
        name = "BungeeVelocityBridge",
        version = "1.0.0-SNAPSHOT",
        authors = {"Blyrex", "0utplay"}
)
public class BungeeVelocityBridgePlugin {

    @Getter
    private final ProxyServer server;
    @Getter
    private final Logger logger;

    @Inject
    public BungeeVelocityBridgePlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }
}
