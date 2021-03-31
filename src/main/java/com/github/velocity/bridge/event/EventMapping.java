package com.github.velocity.bridge.event;

import com.github.velocity.bridge.BungeeVelocityBridgePlugin;
import com.velocitypowered.api.event.PostOrder;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Event;

@Getter
public abstract class EventMapping<S, T extends Event> {
    protected final BungeeVelocityBridgePlugin plugin;
    protected final Class<S> sourceEventClass;
    protected final Class<T> targetEventClass;
    protected final PostOrder postOrder;

    public EventMapping(BungeeVelocityBridgePlugin plugin, Class<S> sourceEventClass, Class<T> targetEventClass, PostOrder postOrder) {
        this.plugin = plugin;
        this.sourceEventClass = sourceEventClass;
        this.targetEventClass = targetEventClass;
        this.postOrder = postOrder;
        this.plugin.getServer().getEventManager().register(this.plugin, this.sourceEventClass, this.postOrder, event -> {
            T target = this.preparation(event);
            if (target != null) {
                this.plugin.getBungeeProxyServer().getPluginManager()
                        .callEvent(target);
                this.done(event, target);
            }
        });
    }

    protected abstract T preparation(S s);

    protected void done(S s, T t) {

    }
}
