package com.github.velocity.bridge;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class ProxyServerBridge extends ProxyServer {

    public String getName() {
        return null;
    }

    public String getVersion() {
        return null;
    }

    public String getTranslation(String name, Object... args) {
        return null;
    }

    public Logger getLogger() {
        return null;
    }

    public Collection<ProxiedPlayer> getPlayers() {
        return null;
    }

    public ProxiedPlayer getPlayer(String name) {
        return null;
    }

    public ProxiedPlayer getPlayer(UUID uuid) {
        return null;
    }

    public Map<String, ServerInfo> getServers() {
        return null;
    }

    public ServerInfo getServerInfo(String name) {
        return null;
    }

    public PluginManager getPluginManager() {
        return null;
    }

    public ConfigurationAdapter getConfigurationAdapter() {
        return null;
    }

    public void setConfigurationAdapter(ConfigurationAdapter adapter) {

    }

    public ReconnectHandler getReconnectHandler() {
        return null;
    }

    public void setReconnectHandler(ReconnectHandler handler) {

    }

    public void stop() {

    }

    public void stop(String reason) {

    }

    public void registerChannel(String channel) {

    }

    public void unregisterChannel(String channel) {

    }

    public Collection<String> getChannels() {
        return null;
    }

    public String getGameVersion() {
        return null;
    }

    public int getProtocolVersion() {
        return 0;
    }

    public ServerInfo constructServerInfo(String name, InetSocketAddress address, String motd, boolean restricted) {
        return null;
    }

    public ServerInfo constructServerInfo(String name, SocketAddress address, String motd, boolean restricted) {
        return null;
    }

    public CommandSender getConsole() {
        return null;
    }

    public File getPluginsFolder() {
        return null;
    }

    public TaskScheduler getScheduler() {
        return null;
    }

    public int getOnlineCount() {
        return 0;
    }

    public void broadcast(String message) {

    }

    public void broadcast(BaseComponent... message) {

    }

    public void broadcast(BaseComponent message) {

    }

    public Collection<String> getDisabledCommands() {
        return null;
    }

    public ProxyConfig getConfig() {
        return null;
    }

    public Collection<ProxiedPlayer> matchPlayer(String match) {
        return null;
    }

    public Title createTitle() {
        return null;
    }
}
