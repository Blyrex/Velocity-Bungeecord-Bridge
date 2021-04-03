package com.github.velocity.bridge.console;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
public final class BridgeCommandForward {

    private final BungeeVelocityBridgePlugin plugin;

    @Subscribe(order = PostOrder.LATE)
    public void playerCommandExecution(PlayerChatEvent event) {
        if (event.getMessage().charAt(0) != '/' || !event.getResult().isAllowed()) {
            return;
        }
        String[] args = event.getMessage().split(" ");
        for (Map.Entry<String, Command> commandEntry : this.plugin.getBungeeProxyServer().getPluginManager().getCommands()) {
            if (!args[0].equalsIgnoreCase(commandEntry.getKey())) {
                continue;
            }
            this.plugin.getLogger().log(
                    Level.INFO,
                    "Player {0} executed the command {1} through the Bungee-Velocity-Bridge",
                    new String[]{event.getPlayer().getUsername(), event.getMessage()});
            commandEntry
                    .getValue()
                    .execute(BridgeProxiedPlayer.fromVelocity(this.plugin.getServer(), event.getPlayer()), Arrays.copyOfRange(args, 1, args.length));
            break;
        }
    }
}
