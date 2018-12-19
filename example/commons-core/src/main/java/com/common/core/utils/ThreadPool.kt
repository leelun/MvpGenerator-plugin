package com.common.core.utils

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * @author liulun
 * @version V1.0
 * @Description: 线程池
 * @date 2016/12/12 16:30
 */
class ThreadPool {
    private val executorRunnables = ConcurrentHashMap<Runnable, ScheduledFuture<*>>()

    companion object {
        private val executor = Executors.newCachedThreadPool()
        private val scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors())

        fun submit(runnable: Runnable) {
            executor.execute(runnable)
        }

        fun postDelayed(runnable: Runnable, delay: Long): ScheduledFuture<*> {
            return scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS)
        }

        fun scheduleAtFixedRate(runnable: Runnable, initialDelay: Long, delay: Long): ScheduledFuture<*> {
            return scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, delay, TimeUnit.MILLISECONDS)
        }
    }
}
