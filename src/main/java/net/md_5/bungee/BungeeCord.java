package net.md_5.bungee;

import com.github.velocity.bridge.BridgeProxyServer;
import com.github.velocity.bridge.BungeeVelocityBridgePlugin;

public class BungeeCord extends BridgeProxyServer {
    public BungeeCord(BungeeVelocityBridgePlugin bungeeVelocityBridgePlugin) {
        super(bungeeVelocityBridgePlugin);
    }

    public static BungeeCord getInstance() {
        return (BungeeCord) BridgeProxyServer.getInstance();
    }
}
