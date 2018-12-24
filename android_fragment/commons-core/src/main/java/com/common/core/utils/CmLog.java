package com.common.core.utils;

import android.util.Log;

/**
 * @author liulun
 * @version V1.0
 * @Description: log日志
 * @date 2016/12/21 13:32
 */
public class CmLog {
    private static boolean isDebug = true;

    public static void setDebug(boolean debug) {
        CmLog.isDebug = debug;
    }

    public static void v(String tag, String msg) {
        if (isDebug) {
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.v(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (isDebug) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.d(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (isDebug) {
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.i(tag, msg, tr);
        }
    }

    public static void w(String tag, String msg) {
        if (isDebug) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.w(tag, msg, tr);
        }
    }

    public static void w(String tag, Throwable tr) {
        if (isDebug) {
            Log.w(tag, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (isDebug) {
            Log.e(tag, msg);
        }
    }

    public static void printStackTrace(Throwable tr) {
        if (isDebug) {
            tr.printStackTrace();
        }
    }

    public static void printStackTrace(String errorMsg) {
        if (isDebug) {
            try {
                throw new Exception(errorMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
