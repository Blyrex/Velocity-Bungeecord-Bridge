package com.github.velocity.bridge.velocity;

import com.github.velocity.bridge.connection.BridgePendingConnection;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Map;

@RequiredArgsConstructor
public class BridgeEventListener {

    private final ProxyServer proxyServer;
    private final com.velocitypowered.api.proxy.ProxyServer velocityProxyServer;

    @Subscribe
    public void handlePlayerChat(PlayerChatEvent event) {
        if(event.getMessage().charAt(0) != '/') {
            return;
        }

        ProxiedPlayer proxiedPlayer = BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, event.getPlayer());
        ChatEvent chatEvent = new ChatEvent(proxiedPlayer, proxiedPlayer, event.getMessage());
        this.proxyServer.getPluginManager().callEvent(chatEvent);

        if(chatEvent.isCancelled()) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            return;
        }

        String[] args = event.getMessage().split(" ");
        for (Map.Entry<String, Command> commandEntry : this.proxyServer.getPluginManager().getCommands()) {
            if(!args[0].equalsIgnoreCase(commandEntry.getKey())) {
               continue;
            }
            commandEntry.getValue().execute(BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, event.getPlayer()), Arrays.copyOfRange(args, 1, args.length));
            break;
        }
    }

    @Subscribe
    public void handlePlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        PreLoginEvent preLoginEvent = new PreLoginEvent(new BridgePendingConnection(event.getPlayer()), new Callback<PreLoginEvent>() {
            @Override
            public void done(PreLoginEvent result, Throwable error) {
                if(result.isCancelled()) {
                    player.disconnect(BungeeComponentSerializer.legacy().deserialize(result.getCancelReasonComponents()));
                }
            }
        });
    }
}
