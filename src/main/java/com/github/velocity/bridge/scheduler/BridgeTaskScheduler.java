package com.github.velocity.bridge.scheduler;

import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@RequiredArgsConstructor
public class BridgeTaskScheduler implements TaskScheduler {

    private final ProxyServer velocityProxyServer;
    private final Map<Integer, ScheduledTask> scheduledTaskMap = new HashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Integer.MAX_VALUE);
    private final Random random = new Random();
    private final Unsafe unsafe = plugin -> this.executorService;

    @Override
    public void cancel(int id) {
        ScheduledTask scheduledTask = this.scheduledTaskMap.get(id);
        if (scheduledTask == null) {
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
        new HashMap<>(this.scheduledTaskMap).forEach((id, task) -> {
            if (task.getOwner().equals(plugin)) {
                task.cancel();
                this.scheduledTaskMap.remove(id);
            }
        });
        return 0;
    }

    @Override
    public ScheduledTask runAsync(Plugin owner, Runnable task) {
        return this.schedule(owner, task, 0L, TimeUnit.MILLISECONDS);
    }

    @Override
    public ScheduledTask schedule(Plugin owner, Runnable task, long delay, TimeUnit unit) {
        return new BridgeScheduledTask(
                this.random.nextInt(10000),
                owner,
                task,
                this.scheduledExecutorService.schedule(task, delay, unit)
        );
    }

    @Override
    public ScheduledTask schedule(Plugin owner, Runnable task, long delay, long period, TimeUnit unit) {
        return new BridgeScheduledTask(
                this.random.nextInt(10000),
                owner,
                task,
                this.scheduledExecutorService.scheduleAtFixedRate(task, delay, period, unit)
        );
    }

    @Override
    public Unsafe unsafe() {
        return this.unsafe;
    }
}
