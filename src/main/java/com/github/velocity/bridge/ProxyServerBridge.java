package com.github.velocity.bridge;

import com.github.velocity.bridge.chat.BridgeProxyTitle;
import com.github.velocity.bridge.console.BridgeConsoleSender;
import com.github.velocity.bridge.player.BridgeProxiedPlayer;
import com.github.velocity.bridge.scheduler.BridgeTaskScheduler;
import com.github.velocity.bridge.server.BridgeServerInfo;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.Getter;
import lombok.SneakyThrows;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
public final class ProxyServerBridge extends ProxyServer {
    private final BungeeVelocityBridgePlugin bungeeVelocityBridgePlugin;
    private final com.velocitypowered.api.proxy.ProxyServer velocityProxyServer;
    private final CommandSender consoleCommandSender;
    private final TaskScheduler taskScheduler;
    private final Path pluginsFolder;
    private final PluginManager pluginManager;

    public ProxyServerBridge(BungeeVelocityBridgePlugin bungeeVelocityBridgePlugin) {
        // Set ProxyServer instance
        ProxyServer.setInstance(this);
        this.bungeeVelocityBridgePlugin = bungeeVelocityBridgePlugin;
        this.velocityProxyServer = this.bungeeVelocityBridgePlugin.getServer();
        this.consoleCommandSender = new BridgeConsoleSender(this.velocityProxyServer);
        this.taskScheduler = new BridgeTaskScheduler(this.velocityProxyServer);
        this.pluginsFolder = this.setupPluginsFolder();
        this.pluginManager = new PluginManager(this, this.bungeeVelocityBridgePlugin);
        this.loadPlugins();
    }

    private void loadPlugins() {
        this.pluginManager.detectPlugins(this.pluginsFolder.toFile());
        this.pluginManager.loadPlugins();
        this.pluginManager.enablePlugins();
    }

    @SneakyThrows
    private Path setupPluginsFolder() {
        Path pluginsFolder = Paths.get(
                this.bungeeVelocityBridgePlugin.getDataDirectory().toString(),
                "plugins"
        );
        if (!Files.exists(pluginsFolder)) {
            Files.createDirectories(pluginsFolder);
        }
        return pluginsFolder;
    }

    @Getter
    public final Configuration configuration = new Configuration();

    @Override
    public String getName() {
        return "BungeeVelocityBridge";
    }

    @Override
    public String getVersion() {
        return BungeeVelocityBridgePlugin.class.getPackage().getImplementationVersion() == null
                ? "unknown"
                : BungeeVelocityBridgePlugin.class.getPackage().getImplementationVersion();
    }

    @Override
    public String getTranslation(String name, Object... args) {
        return "";
    }

    @Override
    public Logger getLogger() {
        return this.bungeeVelocityBridgePlugin.getLogger();
    }

    @Override
    public Collection<ProxiedPlayer> getPlayers() {
        return this.velocityProxyServer.getAllPlayers()
                .stream()
                .map(player -> BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, player))
                .collect(Collectors.toList());
    }

    @Override
    public ProxiedPlayer getPlayer(String name) {
        return this.velocityProxyServer.getPlayer(name)
                .map(player -> BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, player))
                .orElse(null);
    }

    @Override
    public ProxiedPlayer getPlayer(UUID uuid) {
        return this.velocityProxyServer.getPlayer(uuid)
                .map(player -> BridgeProxiedPlayer.fromVelocity(this.velocityProxyServer, player))
                .orElse(null);
    }

    @Override
    public Map<String, ServerInfo> getServers() {
        return this.velocityProxyServer.getAllServers().stream()
                .collect(Collectors.toMap(
                        registeredServer -> registeredServer.getServerInfo().getName(),
                        registeredServer -> new BridgeServerInfo(this.velocityProxyServer, registeredServer)
                ));
    }

    @Override
    public ServerInfo getServerInfo(String name) {
        return this.getServers().get(name);
    }

    @Override
    public ConfigurationAdapter getConfigurationAdapter() {
        return null;
    }

    @Override
    public void setConfigurationAdapter(ConfigurationAdapter adapter) {

    }

    @Override
    public ReconnectHandler getReconnectHandler() {
        return null;
    }

    @Override
    public void setReconnectHandler(ReconnectHandler handler) {

    }

    @Override
    public void stop() {
        this.velocityProxyServer.shutdown();
    }

    @Override
    public void stop(String reason) {
        this.velocityProxyServer.shutdown(Component.text(reason));
    }

    @Override
    public void registerChannel(String channel) {
        this.velocityProxyServer.getChannelRegistrar().register(MinecraftChannelIdentifier.forDefaultNamespace(channel));
    }

    @Override
    public void unregisterChannel(String channel) {
        this.velocityProxyServer.getChannelRegistrar().unregister(MinecraftChannelIdentifier.forDefaultNamespace(channel));
    }

    @Override
    public Collection<String> getChannels() {
        return Collections.emptyList();
    }

    @Override
    public String getGameVersion() {
        return "";
    }

    @Override
    public int getProtocolVersion() {
        return 0;
    }

    @Override
    public ServerInfo constructServerInfo(String name, InetSocketAddress address, String motd, boolean restricted) {
        return null;
    }

    @Override
    public ServerInfo constructServerInfo(String name, SocketAddress address, String motd, boolean restricted) {
        return null;
    }

    @Override
    public CommandSender getConsole() {
        return this.consoleCommandSender;
    }

    @Override
    public File getPluginsFolder() {
        return this.pluginsFolder.toFile();
    }

    @Override
    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }

    @Override
    public int getOnlineCount() {
        return this.velocityProxyServer.getPlayerCount();
    }

    @Override
    public void broadcast(String message) {
        this.broadcast(TextComponent.fromLegacyText(message));
    }

    @Override
    public void broadcast(BaseComponent... message) {
        this.velocityProxyServer.sendMessage(Identity.nil(), BungeeComponentSerializer.legacy().deserialize(message));
    }

    @Override
    public void broadcast(BaseComponent message) {
        this.broadcast(new BaseComponent[]{message});
    }

    @Override
    public Collection<String> getDisabledCommands() {
        return Collections.emptyList();
    }

    @Override
    public ProxyConfig getConfig() {
        return null;
    }

    @Override
    public Collection<ProxiedPlayer> matchPlayer(String match) {
        return Objects.requireNonNull(this.getPlayers()).stream()
                .filter(proxiedPlayer -> proxiedPlayer.getName().equalsIgnoreCase(match))
                .collect(Collectors.toList());
    }

    @Override
    public Title createTitle() {
        return new BridgeProxyTitle(this.velocityProxyServer);
    }
}
