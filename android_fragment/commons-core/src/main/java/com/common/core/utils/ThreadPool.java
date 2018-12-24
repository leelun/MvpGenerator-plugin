package com.common.core.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author liulun
 * @version V1.0
 * @Description: 线程池
 * @date 2016/12/12 16:30
 */
public class ThreadPool {
    private static Executor executor = Executors.newCachedThreadPool();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    private ConcurrentHashMap<Runnable, ScheduledFuture> executorRunnables = new ConcurrentHashMap<>();

    public static void submit(Runnable runnable) {
        executor.execute(runnable);
    }

    public static ScheduledFuture postDelayed(Runnable runnable, long delay) {
        return scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    public static ScheduledFuture scheduleAtFixedRate(Runnable runnable, long initialDelay, long delay) {
        return scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }
}
