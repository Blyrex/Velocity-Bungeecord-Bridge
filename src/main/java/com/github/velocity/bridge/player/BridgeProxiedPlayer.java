package com.github.velocity.bridge.player;

import com.github.velocity.bridge.connection.BridgePendingConnection;
import com.github.velocity.bridge.server.BridgeServer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@Getter
@Setter
public final class BridgeProxiedPlayer extends BridgePendingConnection implements ProxiedPlayer {

    private static final BaseComponent[] EMPTY_COMPONENT = new BaseComponent[]{new TextComponent()};

    private final ProxyServer velocityProxyServer;
    private final Player player;
    private String displayName;
    private final List<String> permissions = new ArrayList<>();
    private final List<String> groups = new ArrayList<>();

    private BridgeProxiedPlayer(ProxyServer velocityProxyServer, Player player) {
        super(player, velocityProxyServer);
        this.velocityProxyServer = velocityProxyServer;
        this.player = player;
        this.displayName = this.player.getUsername();
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent... message) {
        if (position.equals(ChatMessageType.CHAT)) {
            this.player.sendMessage(BungeeComponentSerializer.legacy().deserialize(message));
        } else if (position.equals(ChatMessageType.ACTION_BAR)) {
            this.player.sendActionBar(BungeeComponentSerializer.legacy().deserialize(message));
        }
    }

    @Override
    public void sendMessage(ChatMessageType position, BaseComponent message) {
        this.sendMessage(position, new BaseComponent[]{message});
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent... message) {
        this.sendMessage(ChatMessageType.CHAT, message);
    }

    @Override
    public void sendMessage(UUID sender, BaseComponent message) {
        this.sendMessage(ChatMessageType.CHAT, message);
    }

    @Override
    public void connect(ServerInfo target) {
        this.velocityProxyServer.getServer(target.getName())
                .ifPresent(
                        registeredServer -> this.player.createConnectionRequest(registeredServer).fireAndForget()
                );
    }

    @Override
    public void connect(ServerInfo target, ServerConnectEvent.Reason reason) {
        this.connect(target);
    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback) {
        RegisteredServer server = this.velocityProxyServer.getServer(target.getName()).orElse(null);
        if(server == null) {
            return;
        }
        CompletableFuture<Boolean> resultCompletableFuture = this.player.createConnectionRequest(server).connectWithIndication();
        resultCompletableFuture.thenAccept(result -> callback.done(result, null));

    }

    @Override
    public void connect(ServerInfo target, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        this.connect(target, callback);
    }

    @Override
    public void connect(ServerConnectRequest request) {
        this.connect(request.getTarget());
    }

    @Override
    public Server getServer() {
        ServerConnection serverConnection = this.player.getCurrentServer().orElse(null);
        if(serverConnection == null) {
            return null;
        }
        return new BridgeServer(this.velocityProxyServer, serverConnection.getServer());
    }

    @Override
    public int getPing() {
        return (int) this.player.getPing();
    }

    @Override
    public void sendData(String channel, byte[] data) {
        this.player.sendPluginMessage(MinecraftChannelIdentifier.forDefaultNamespace(channel), data);
    }

    @Override
    public PendingConnection getPendingConnection() {
        return new BridgePendingConnection(this.player, this.velocityProxyServer);
    }

    @Override
    public void chat(String message) {
        this.player.spoofChatInput(message);
    }

    @Override
    public ServerInfo getReconnectServer() {
        return null;
    }

    @Override
    public void setReconnectServer(ServerInfo server) {
    }

    @Override
    public String getUUID() {
        return this.player.getUniqueId().toString();
    }

    @Override
    public UUID getUniqueId() {
        return this.player.getUniqueId();
    }

    @Override
    public Locale getLocale() {
        return this.player.getPlayerSettings().getLocale();
    }

    @Override
    public byte getViewDistance() {
        return this.player.getPlayerSettings().getViewDistance();
    }

    @Override
    public ChatMode getChatMode() {
        switch (this.player.getPlayerSettings().getChatMode()) {
            case HIDDEN:
                return ChatMode.HIDDEN;
            case COMMANDS_ONLY:
                return ChatMode.COMMANDS_ONLY;
            case SHOWN:
            default:
                return ChatMode.SHOWN;
        }
    }

    @Override
    public boolean hasChatColors() {
        return this.player.getPlayerSettings().hasChatColors();
    }

    @Override
    public SkinConfiguration getSkinParts() {
        return new BridgeSkinConfiguration(this.player.getPlayerSettings().getSkinParts());
    }

    @Override
    public MainHand getMainHand() {
        switch (this.player.getPlayerSettings().getMainHand()) {
            case LEFT:
                return MainHand.LEFT;
            default:
            case RIGHT:
                return MainHand.RIGHT;
        }
    }

    @Override
    public void setTabHeader(BaseComponent header, BaseComponent footer) {
        this.setTabHeader(new BaseComponent[]{header}, new BaseComponent[]{footer});
    }

    @Override
    public void setTabHeader(BaseComponent[] header, BaseComponent[] footer) {
        this.player.sendPlayerListHeaderAndFooter(
                BungeeComponentSerializer.legacy().deserialize(header),
                BungeeComponentSerializer.legacy().deserialize(footer)
        );
    }

    @Override
    public void resetTabHeader() {
        this.setTabHeader(EMPTY_COMPONENT, EMPTY_COMPONENT);
    }

    @Override
    public void sendTitle(Title title) {
        title.send(this);
    }

    @Override
    public boolean isForgeUser() {
        return this.player.getModInfo().isPresent();
    }

    @Override
    public Map<String, String> getModList() {
        return new HashMap<>();
    }

    @Override
    public Scoreboard getScoreboard() {
        throw new UnsupportedOperationException("This feature is not supported by velocity, so the BungeeVelocityBridge cannot support it either");
    }

    @Override
    public String getName() {
        return this.player.getUsername();
    }

    @Override
    public void sendMessage(String message) {
        this.sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(message));
    }

    @Override
    public void sendMessages(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(BaseComponent... message) {
        this.sendMessage(ChatMessageType.CHAT, message);
    }

    @Override
    public void sendMessage(BaseComponent message) {
        this.sendMessage(ChatMessageType.CHAT, message);
    }

    @Override
    public Collection<String> getGroups() {
        return this.groups;
    }

    @Override
    public void addGroups(String... groups) {
        this.groups.addAll(Arrays.asList(groups));
    }

    @Override
    public void removeGroups(String... groups) {
        this.groups.removeAll(Arrays.asList(groups));
    }

    @Override
    public boolean hasPermission(String permission) {
        return this.player.hasPermission(permission) || this.permissions.contains(permission);
    }

    @Override
    public void setPermission(String permission, boolean value) {
        if (value) {
            this.permissions.add(permission);
        } else {
            this.permissions.remove(permission);
        }
    }

    @Override
    public Collection<String> getPermissions() {
        return this.permissions;
    }

    @Override
    public Unsafe unsafe() {
        throw new UnsupportedOperationException("This feature is not supported by velocity, so the BungeeVelocityBridge cannot support it either");
    }

    public static ProxiedPlayer fromVelocity(ProxyServer velocityProxyServer, Player player) {
        return new BridgeProxiedPlayer(velocityProxyServer, player);
    }
}
