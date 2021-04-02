package com.github.velocity.bridge.console;

import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public final class BridgeCommandForward {

    private final ProxyServer proxyServer;
    private final com.velocitypowered.api.proxy.ProxyServer velocityProxyServer;

    @Subscribe(order = PostOrder.LATE)
    public void playerCommandExecution(PlayerChatEvent event) {
        if (event.getMessage().charAt(0) != '/' || !event.getResult().isAllowed()) {
            return;
        }
        String[] args = event.getMessage().split(" ");
        for (Map.Entry<String, Command> commandEntry : this.proxyServer.getPluginManager().getCommands()) {
            if (!args[0].equalsIgnoreCase(commandEntry.getKey())) {
                continue;
            }
            commandEntry
                    .getValue()
                    .execute(BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, event.getPlayer()), Arrays.copyOfRange(args, 1, args.length));
            break;
        }
    }
}