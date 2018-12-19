package com.common.core.utils

/**
 * @author leellun
 * @version V1.0
 * @Description: 同步计数器
 * @date 2017/7/25
 */

class SynchronizedCounter {
    private var mCounter = 0

    @Synchronized
    fun incrementAndGet(): Int {
        return ++mCounter
    }

    @Synchronized
    fun decrementAndGet(): Int {
        CmLog.e("aaaaaaaaaaaaaa===", mCounter.toString() + "")
        return --mCounter
    }

    fun get(): Int {
        return mCounter
    }
}
