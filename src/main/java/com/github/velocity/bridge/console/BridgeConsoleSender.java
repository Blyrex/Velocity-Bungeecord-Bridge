package com.github.velocity.bridge.console;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;

import java.util.Collection;
import java.util.Collections;

@Getter
@RequiredArgsConstructor
public final class BridgeConsoleSender implements CommandSender {
    private final ProxyServer velocityProxyServer;

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public void sendMessage(String message) {
        this.velocityProxyServer.getConsoleCommandSource()
                .sendMessage(Component.text(message));
    }

    @Override
    public void sendMessages(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        this.velocityProxyServer
                .getConsoleCommandSource()
                .sendMessage(BungeeComponentSerializer.legacy().deserialize(message));
    }

    @Override
    public void sendMessage(BaseComponent message) {
        this.sendMessage(new BaseComponent[]{message});
    }

    @Override
    public Collection<String> getGroups() {
        // Ignore for Console
        return Collections.emptyList();
    }

    @Override
    public void addGroups(String... groups) {
        // Ignore for Console
    }

    @Override
    public void removeGroups(String... groups) {
        // Ignore for Console
    }

    @Override
    public boolean hasPermission(String permission) {
        return true;
    }

    @Override
    public void setPermission(String permission, boolean value) {
        // Ignore for Console
    }

    @Override
    public Collection<String> getPermissions() {
        return Collections.emptyList();
    }
}
