package com.common.core.utils

import android.util.Log

/**
 * @author liulun
 * @version V1.0
 * @Description: log日志
 * @date 2016/12/21 13:32
 */
object CmLog {
    private var isDebug = true

    fun setDebug(debug: Boolean) {
        CmLog.isDebug = debug
    }

    fun v(tag: String, msg: String) {
        if (isDebug) {
            Log.v(tag, msg)
        }
    }

    fun v(tag: String, msg: String, tr: Throwable) {
        if (isDebug) {
            Log.v(tag, msg, tr)
        }
    }

    fun d(tag: String, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    fun d(tag: String, msg: String, tr: Throwable) {
        if (isDebug) {
            Log.d(tag, msg, tr)
        }
    }

    fun i(tag: String, msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }

    fun i(tag: String, msg: String, tr: Throwable) {
        if (isDebug) {
            Log.i(tag, msg, tr)
        }
    }

    fun w(tag: String, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    fun w(tag: String, msg: String, tr: Throwable) {
        if (isDebug) {
            Log.w(tag, msg, tr)
        }
    }

    fun w(tag: String, tr: Throwable) {
        if (isDebug) {
            Log.w(tag, tr)
        }
    }

    fun e(tag: String, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    fun printStackTrace(tr: Throwable) {
        if (isDebug) {
            tr.printStackTrace()
        }
    }

    fun printStackTrace(errorMsg: String) {
        if (isDebug) {
            try {
                throw Exception(errorMsg)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }
}
