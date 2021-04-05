package com.github.velocity.bridge.console;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.command.CommandExecuteEvent;
import com.velocitypowered.api.proxy.Player;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

@RequiredArgsConstructor
public final class BridgeCommandForward {

    private final BungeeVelocityBridgePlugin plugin;

    @Subscribe(order = PostOrder.LATE)
    public void handleCommandExecution(CommandExecuteEvent event) {
        if (!event.getResult().isAllowed()) {
            return;
        }
        String username = "Console";
        Player player = null;
        if(event.getCommandSource() instanceof Player) {
            player = (Player) event.getCommandSource();

            username = player.getUsername();
        }
        String[] args = event.getCommand().split(" ");
        for (Map.Entry<String, Command> commandEntry : this.plugin.getBungeeProxyServer().getPluginManager().getCommands()) {
            if (!args[0].equalsIgnoreCase(commandEntry.getKey())) {
                continue;
            }

            this.plugin.getLogger().log(
                    Level.INFO,
                    "{0} executed the command {1} through the Bungee-Velocity-Bridge",
                    new String[]{username, event.getCommand()});

            CommandSender commandSender = this.plugin.getBungeeProxyServer().getConsole();
            if(!username.equalsIgnoreCase("Console")) {
                commandSender = BridgeProxiedPlayer.fromVelocity(this.plugin.getServer(), player);
            }

            commandEntry
                    .getValue()
                    .execute(commandSender, Arrays.copyOfRange(args, 1, args.length));
            event.setResult(CommandExecuteEvent.CommandResult.denied());
            break;
        }

    }
}
