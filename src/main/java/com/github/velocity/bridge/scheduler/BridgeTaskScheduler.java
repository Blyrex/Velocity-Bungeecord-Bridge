package com.github.velocity.bridge.scheduler;

import com.velocitypowered.api.proxy.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BridgeTaskScheduler implements TaskScheduler {

    private final ProxyServer velocityProxyServer;
    private final Map<Integer, com.velocitypowered.api.scheduler.ScheduledTask> scheduledTaskMap;

    public BridgeTaskScheduler(ProxyServer velocityProxyServer) {
        this.velocityProxyServer = velocityProxyServer;
        this.scheduledTaskMap = new HashMap<>();
    }

    @Override
    public void cancel(int id) {
        com.velocitypowered.api.scheduler.ScheduledTask scheduledTask = this.scheduledTaskMap.get(id);
        if(scheduledTask == null) {
            return;
        }
        scheduledTask.cancel();
        this.scheduledTaskMap.remove(id);
    }

    @Override
    public void cancel(ScheduledTask task) {
        this.cancel(task.getId());
    }

    @Override
    public int cancel(Plugin plugin) {
        return 0;
    }

    @Override
    public ScheduledTask runAsync(Plugin owner, Runnable task) {
        return null;
    }

    @Override
    public ScheduledTask schedule(Plugin owner, Runnable task, long delay, TimeUnit unit) {
        return null;
    }

    @Override
    public ScheduledTask schedule(Plugin owner, Runnable task, long delay, long period, TimeUnit unit) {
        return null;
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }
}
