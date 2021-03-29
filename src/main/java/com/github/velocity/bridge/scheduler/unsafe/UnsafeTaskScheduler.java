package com.github.velocity.bridge.scheduler.unsafe;

import com.github.velocity.bridge.scheduler.BridgeTaskScheduler;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
public final class UnsafeTaskScheduler implements TaskScheduler.Unsafe {

    private final BridgeTaskScheduler bridgeTaskScheduler;

    @Override
    public ExecutorService getExecutorService(Plugin plugin) {
        return this.bridgeTaskScheduler.getExecutorService();
    }
}
