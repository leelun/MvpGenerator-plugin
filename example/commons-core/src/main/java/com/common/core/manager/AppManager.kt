package com.common.core.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.Messenger
import com.common.core.utils.CmLog
import java.util.*

class AppManager private constructor() {
    private val activityStack = Stack<Activity>()
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        var activity: Activity? = null
        try {
            if (activityStack.size > 0) {
                activity = activityStack.lastElement()
            }
        } catch (e: Exception) {
            CmLog.printStackTrace(e)
        }

        return activity
    }

    fun isActivityLauncher(clazz: Class<*>): Boolean {
        for (activity in activityStack) {
            if (activity::class == clazz) {
                return true
            }
        }
        return false
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity::class == cls) {
                finishActivity(activity)
                break
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        for (i in activityStack) {
            if (null != i) {
                i.finish()
            }
        }
        activityStack.clear()
    }

    /**
     * 结束所有Activity
     */
    fun finishOtherAllActivity(activity: Class<*>) {
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i] && activityStack[i]::class != activity) {
                activityStack[i].finish()
            }
            i++
        }
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
            //			ThreadPool.getInstance().destroy();
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun runOnUiThread(runnable: Runnable) {
        val activity = currentActivity()
        activity?.runOnUiThread(runnable) ?: Handler(Looper.getMainLooper()).post(runnable)
    }


    /**
     * 申请权限
     *
     * @param context
     * @param permissions
     * @param requestCode
     */
    fun requestPermission(context: Context, permissions: Array<String>, requestCode: Int, callBack: PermissionCallback?) {
        val handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                callBack?.onPermissionCallback()
            }
        }
        val messenger = Messenger(handler)
//        val intent = Intent(context, IntentActivity::class.java)
//        intent.putExtra(IntentActivity.EXTRA_MESSENGER, messenger)
//        intent.putExtra(IntentActivity.EXTRA_REQUESTCODE, requestCode)
//        intent.putExtra(IntentActivity.EXTRA_PERMISSIONS, permissions)
//        context.startActivity(intent)
    }

    interface ActivityCallBack {
        fun onActivityForResult(requestCode: Int, resultCode: Int, data: Intent)
    }

    interface PermissionCallback {
        fun onPermissionCallback()
    }

    companion object {

        private var instance: AppManager? = null

        /**
         * 单一实例
         */
        fun getAppManager(): AppManager {
            if (instance == null) {
                instance = AppManager()
            }
            return instance as AppManager
        }
    }
}
