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

    public String getTranslation(String s, Object... objects) {
        return null;
    }

    public Logger getLogger() {
        return null;
    }

    public Collection<ProxiedPlayer> getPlayers() {
        return null;
    }

    public ProxiedPlayer getPlayer(String s) {
        return null;
    }

    public ProxiedPlayer getPlayer(UUID uuid) {
        return null;
    }

    public Map<String, ServerInfo> getServers() {
        return null;
    }

    public ServerInfo getServerInfo(String s) {
        return null;
    }

    public PluginManager getPluginManager() {
        return null;
    }

    public ConfigurationAdapter getConfigurationAdapter() {
        return null;
    }

    public void setConfigurationAdapter(ConfigurationAdapter configurationAdapter) {

    }

    public ReconnectHandler getReconnectHandler() {
        return null;
    }

    public void setReconnectHandler(ReconnectHandler reconnectHandler) {

    }

    public void stop() {

    }

    public void stop(String s) {

    }

    public void registerChannel(String s) {

    }

    public void unregisterChannel(String s) {

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

    public ServerInfo constructServerInfo(String s, InetSocketAddress inetSocketAddress, String s1, boolean b) {
        return null;
    }

    public ServerInfo constructServerInfo(String s, SocketAddress socketAddress, String s1, boolean b) {
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

    public void broadcast(String s) {

    }

    public void broadcast(BaseComponent... baseComponents) {

    }

    public void broadcast(BaseComponent baseComponent) {

    }

    public Collection<String> getDisabledCommands() {
        return null;
    }

    public ProxyConfig getConfig() {
        return null;
    }

    public Collection<ProxiedPlayer> matchPlayer(String s) {
        return null;
    }

    public Title createTitle() {
        return null;
    }
}
