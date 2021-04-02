package com.github.velocity.bridge;

import com.github.velocity.bridge.event.mapping.ChatEventMapping;
import com.github.velocity.bridge.event.mapping.PingEventMapping;
import com.github.velocity.bridge.event.mapping.PluginMessageEventMapping;
import com.github.velocity.bridge.event.mapping.login.LoginEventMapping;
import com.github.velocity.bridge.event.mapping.login.PostLoginEventMapping;
import com.github.velocity.bridge.event.mapping.login.PreLoginEventMapping;
import com.github.velocity.bridge.event.mapping.player.PlayerDisconnectEventMapping;
import com.github.velocity.bridge.event.mapping.player.PlayerHandshakeEventMapping;
import com.github.velocity.bridge.event.mapping.player.PlayerSettingsEventMapping;
import com.github.velocity.bridge.event.mapping.server.*;
import com.github.velocity.bridge.velocity.command.BridgeCommandForward;
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
        this.bungeeProxyServer = new BridgeProxyServer(this);
        this.forwardRegistration();
        this.loadEventMappings();
    }

    private void loadEventMappings() {
        new ChatEventMapping(this);

        new PluginMessageEventMapping(this);
        new PingEventMapping(this);

        new PreLoginEventMapping(this);
        new LoginEventMapping(this);
        new PostLoginEventMapping(this);

        new PlayerDisconnectEventMapping(this);
        new PlayerHandshakeEventMapping(this);
        new PlayerSettingsEventMapping(this);
      
        new ServerConnectedMapping(this);
        new ServerConnectMapping(this);
        new ServerDisconnectMapping(this);
        new ServerSwitchMapping(this);
        new ServerKickMapping(this);
    }

    private void forwardRegistration() {
        this.server.getEventManager()
                .register(this, new BridgeCommandForward(this.bungeeProxyServer, this.server));
    }
}
