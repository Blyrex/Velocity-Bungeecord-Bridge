package com.github.velocity.bridge.velocity.event;

import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public final class BridgeChatActionForward {
    private final ProxyServer proxyServer;
    private final com.velocitypowered.api.proxy.ProxyServer velocityProxyServer;

    @Subscribe(order = PostOrder.EARLY)
    public void playerChat(PlayerChatEvent event) {
        ProxiedPlayer proxiedPlayer = BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, event.getPlayer());
        ChatEvent chatEvent = new ChatEvent(proxiedPlayer, proxiedPlayer, event.getMessage());
        this.proxyServer.getPluginManager().callEvent(chatEvent);
        if (chatEvent.isCancelled()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
        }
    }

    @Subscribe(order = PostOrder.NORMAL)
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
