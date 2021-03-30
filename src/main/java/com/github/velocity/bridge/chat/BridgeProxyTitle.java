package com.github.velocity.bridge.chat;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import net.kyori.adventure.util.Ticks;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class BridgeProxyTitle implements Title {

    private net.kyori.adventure.title.Title title = net.kyori.adventure.title.Title.title(Component.empty(), Component.empty());
    private final ProxyServer proxyServer;

    public BridgeProxyTitle(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public Title title(BaseComponent text) {
        return this.title(new BaseComponent[]{text});
    }

    public Title title(BaseComponent... text) {
        Component textComponent = BungeeComponentSerializer.legacy().deserialize(text);
        this.title = net.kyori.adventure.title.Title.title(textComponent, this.title.subtitle());
        return this;
    }

    public Title subTitle(BaseComponent text) {
        return this.subTitle(new BaseComponent[]{text});
    }

    public Title subTitle(BaseComponent... text) {
        Component textComponent = BungeeComponentSerializer.legacy().deserialize(text);
        this.title = net.kyori.adventure.title.Title.title(this.title.title(), textComponent);
        return this;
    }

    public Title fadeIn(int ticks) {
        net.kyori.adventure.title.Title.Times times = this.title.times();
        if(times == null) {
            times = net.kyori.adventure.title.Title.DEFAULT_TIMES;
        }
        times = net.kyori.adventure.title.Title.Times.of(Ticks.duration(ticks), times.stay(), times.fadeOut());
        this.title = net.kyori.adventure.title.Title.title(this.title.title(), this.title.subtitle(), times);
        return this;
    }

    public Title stay(int ticks) {
        net.kyori.adventure.title.Title.Times times = this.title.times();
        if(times == null) {
            times = net.kyori.adventure.title.Title.DEFAULT_TIMES;
        }
        times = net.kyori.adventure.title.Title.Times.of(times.fadeIn(), Ticks.duration(ticks), times.fadeOut());
        this.title = net.kyori.adventure.title.Title.title(this.title.title(), this.title.subtitle(), times);
        return this;
    }

    public Title fadeOut(int ticks) {
        net.kyori.adventure.title.Title.Times times = this.title.times();
        if(times == null) {
            times = net.kyori.adventure.title.Title.DEFAULT_TIMES;
        }
        times = net.kyori.adventure.title.Title.Times.of(times.fadeIn(), times.stay(), Ticks.duration(ticks));
        this.title = net.kyori.adventure.title.Title.title(this.title.title(), this.title.subtitle(), times);
        return this;
    }

    public Title clear() {
        return this;
    }

    public Title reset() {
        return this;
    }

    public Title send(ProxiedPlayer player) {
        this.proxyServer.getPlayer(player.getUniqueId()).ifPresent(velocityPlayer -> velocityPlayer.showTitle(this.title));
        return this;
    }
}
