package com.github.velocity.bridge.scheduler;

import lombok.Data;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.ScheduledFuture;

@Data
public final class BridgeScheduledTask implements ScheduledTask {

    private final int id;
    private final Plugin owner;
    private final Runnable task;
    private final ScheduledFuture<?> scheduledFuture;

    @Override
    public void cancel() {
        this.scheduledFuture.cancel(true);
    }
}
