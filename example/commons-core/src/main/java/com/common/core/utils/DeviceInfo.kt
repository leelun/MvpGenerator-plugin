package com.common.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.app.PendingIntent.CanceledException
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.net.NetworkInterface
import java.net.SocketException

/**
 * 设备检测类
 */
object DeviceInfo {
    /**
     * 未知网络
     */
    val NETWORK_UNKNOWN = -1
    /**
     * 没有网络
     */
    val NETWORKTYPE_INVALID = 0
    /**
     * wap网络
     */
    val NETWORKTYPE_WAP = 1
    /**
     * 2G网络
     */
    val NETWORKTYPE_2G = 2
    /**
     * 3G网络
     */
    val NETWORKTYPE_3G = 3
    /**
     * 4G和4G以上网络，或统称为快速网络
     */
    val NETWORKTYPE_4G = 4
    /**
     * wifi网络
     */
    val NETWORKTYPE_WIFI = 5

    /**
     * 获取厂商
     *
     * @return
     */
    val manufacturer: String
        get() = android.os.Build.MANUFACTURER

    /**
     * 获取SDK版本号
     *
     * @return
     */
    val intSDK: Int
        get() = android.os.Build.VERSION.SDK_INT

    /**
     * 获取SDK版本号
     *
     * @return
     */
    val stringSDK: String
        get() = android.os.Build.VERSION.SDK

    /**
     * 版本号加手机型号
     *
     * @author LiuLun
     * @Time 2015年6月1日下午3:04:44
     */
    val operationSystemMsg: String
        get() = android.os.Build.VERSION.RELEASE + " " + android.os.Build.MODEL

    @SuppressLint("MissingPermission")
            /**
     * 是否有可用网络（WIFI和移动网络）
     *
     * @param context
     * @return true 网络可用
     */
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo?.isAvailable ?: false
    }

    @SuppressLint("MissingPermission")
    fun getIp(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobileNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifiState = wifiNetworkInfo?.state
        val mobileState = mobileNetworkInfo?.state
        if (wifiState != null && mobileState != null
                && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED == mobileState) {
            // 手机网络连接成功
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
                            return inetAddress.hostAddress.toString()
                        }
                    }
                }
            } catch (ex: SocketException) {
            }

        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            // 无线网络连接成功
            //获取wifi服务
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            //判断wifi是否开启
            if (wifiManager.isWifiEnabled) {
                val wifiInfo = wifiManager.connectionInfo
                val ipAddress = wifiInfo.ipAddress
                return (ipAddress and 0xFF).toString() + "." +
                        (ipAddress shr 8 and 0xFF) + "." +
                        (ipAddress shr 16 and 0xFF) + "." +
                        (ipAddress shr 24 and 0xFF)
            }
        }
        return ""
    }

    /**
     * 检测GPRS是否打开
     *
     * @param context
     * @return true 打开
     */
    fun gprsIsOpenMethod(context: Context): Boolean {
        val mCM = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val cmClass = mCM.javaClass
        var isOpen: Boolean? = false
        try {
            val method = cmClass.getMethod("getMobileDataEnabled")
            isOpen = method.invoke(mCM) as Boolean?
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return isOpen!!
    }

    /**
     * 打开移动数据
     *
     * @param context
     */
    fun openMobileData(context: Context): Int {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var i = 0
        var conMgrClass: Class<*>? = null // ConnectivityManager类
        var iConMgrField: Field? = null // ConnectivityManager类中的字段
        var iConMgr: Any? = null // IConnectivityManager类的引用
        var iConMgrClass: Class<*>? = null // IConnectivityManager类
        var setMobileDataEnabledMethod: Method? = null // setMobileDataEnabled方法
        try {
            // 取得ConnectivityManager类
            conMgrClass = Class.forName(conMgr.javaClass.getName())
            // 取得ConnectivityManager类中的对象mService
            iConMgrField = conMgrClass!!.getDeclaredField("mService")
            // 设置mService可访问
            iConMgrField!!.isAccessible = true
            // 取得mService的实例化类IConnectivityManager
            iConMgr = iConMgrField.get(conMgr)
            // 取得IConnectivityManager类
            iConMgrClass = Class.forName(iConMgr!!.javaClass.getName())
            // 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
            setMobileDataEnabledMethod = iConMgrClass!!.getDeclaredMethod("setMobileDataEnabled", java.lang.Boolean.TYPE)
            // 设置setMobileDataEnabled方法可访问
            setMobileDataEnabledMethod!!.isAccessible = true
            // 调用setMobileDataEnabled方法
            setMobileDataEnabledMethod.invoke(iConMgr, true)
            i = 1
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return i
    }

    /**
     * 定位是否开启
     *
     * @param context
     * @return true 开启
     */
    fun isLocationEnable(context: Context): Boolean {
        var result = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)// 网络定位
            val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)// GPS定位
            if (network || gps) {
                result = true
            }
        }
        return result
    }

    /**
     * GPS定位是否开启
     *
     * @param context
     * @return true 开启
     */
    fun isGpsEnable(context: Context): Boolean {
        var result = false
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager != null) {
            result = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)// GPS定位
        }
        return result
    }

    /**
     * 打开或关闭GPS
     *
     * @param context
     */
    fun openGPS(context: Context) {
        val GPSIntent = Intent()
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider")
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
        GPSIntent.data = Uri.parse("custom:3")
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
        } catch (e: CanceledException) {
            e.printStackTrace()
        }

    }

    fun settingGPS(context: Context) {
        val alm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            context.startActivity(intent) // 此为设置完成后返回到获取界面
        }
    }

    /**
     * WIFI是否可用
     *
     * @param context
     * @return true 可用
     */
    fun isWifiEnable(context: Context): Boolean {
        var result = false
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager != null) {
            result = wifiManager.isWifiEnabled
        }
        return result
    }

    @SuppressLint("MissingPermission")
            /**
     * 打开WIFI
     *
     * @param context
     */
    fun openWifi(context: Context) {
        if (!isWifiEnable(context)) {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if (wifiManager != null) {
                wifiManager.isWifiEnabled = true
            }
        }
    }

    @SuppressLint("MissingPermission")
            /**
     * 获取网络类型
     *
     * @param context
     * @return -1（获取错误），0（无网），1（WAP网络），2（2G），3（3G及以上），4（WIFI）
     */
    fun getNetWorkType(context: Context): Int {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            val type = networkInfo.type
            return if (type == ConnectivityManager.TYPE_WIFI) {
                NETWORKTYPE_WIFI
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                when (type) {
                    TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> NETWORKTYPE_2G
                    TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> NETWORKTYPE_3G
                    TelephonyManager.NETWORK_TYPE_LTE -> NETWORKTYPE_4G
                    else -> NETWORK_UNKNOWN
                }
            } else {
                NETWORK_UNKNOWN
            }
        } else {
            return NETWORKTYPE_INVALID
        }
    }

    /**
     * 判断是否为快速，网络（3G及以上）
     *
     * @param context
     * @return true 为快速
     */
    private fun isFastMobileNetwork(context: Context): Boolean {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        when (telephonyManager.networkType) {
            TelephonyManager.NETWORK_TYPE_1xRTT -> return false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_CDMA -> return false // ~ 14-64 kbps
            TelephonyManager.NETWORK_TYPE_EDGE -> return false // ~ 50-100 kbps
            TelephonyManager.NETWORK_TYPE_EVDO_0 -> return true // ~ 400-1000 kbps
            TelephonyManager.NETWORK_TYPE_EVDO_A -> return true // ~ 600-1400 kbps
            TelephonyManager.NETWORK_TYPE_GPRS -> return false // ~ 100 kbps
            TelephonyManager.NETWORK_TYPE_HSDPA -> return true // ~ 2-14 Mbps
            TelephonyManager.NETWORK_TYPE_HSPA -> return true // ~ 700-1700 kbps
            TelephonyManager.NETWORK_TYPE_HSUPA -> return true // ~ 1-23 Mbps
            TelephonyManager.NETWORK_TYPE_UMTS -> return true // ~ 400-7000 kbps
            TelephonyManager.NETWORK_TYPE_EHRPD -> return true // ~ 1-2 Mbps
            TelephonyManager.NETWORK_TYPE_EVDO_B -> return true // ~ 5 Mbps
            TelephonyManager.NETWORK_TYPE_HSPAP -> return true // ~ 10-20 Mbps
            TelephonyManager.NETWORK_TYPE_IDEN -> return false // ~25 kbps
            TelephonyManager.NETWORK_TYPE_LTE -> return true // ~ 10+ Mbps
            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return false
            else -> return false
        }
    }

    @SuppressLint("MissingPermission")
            /**
     * 得到用户手机号
     *
     * @param context
     * @return
     */
    fun getPhoneNumber(context: Context): String? {
        var result = ""
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        result = telephonyManager.line1Number
        if (!TextUtils.isEmpty(result)) {
            result = result.replace("+86", "")
        }
        return result
    }

    @SuppressLint("MissingPermission")
            /**
     * 得到用户手机串号
     *
     * @param context
     * @return
     */
    fun getPhoneDeviceId(context: Context): String {
        var result = ""
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        try {
            result = telephonyManager.deviceId
        } catch (e: Exception) {
            CmLog.printStackTrace(e)
        }

        return result
    }

    /**
     * 获取媒体音量
     *
     * @param context
     * @param type    音量类型
     * @return
     */
    fun getCurrentVolume(context: Context, type: Int): Int {
        val mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return mAudioManager.getStreamVolume(type)
    }

    fun getVersion(context: Context): String {
        try {
            val info = context.packageManager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            return ""
        }

    }

    fun getProcessName(context: Context): String {
        val pid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return ""
    }

    /**
     * 进程是否在运行
     *
     * @param context
     * @param processName
     * @return
     */
    fun isProcessRunning(context: Context, processName: String): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (processName == appProcess.processName) {
                return true
            }
        }
        return false
    }

    /**
     * activity是否存在
     *
     * @param context
     * @return
     */
    fun isActivityActive(context: Context, className: String): Boolean {
        try {
            val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val runningTaskInfos = manager.getRunningTasks(Integer.MAX_VALUE) ?: return false
            for (info in runningTaskInfos) {
                if (info.topActivity.className == className) return true
            }
            return false
        } catch (e: Exception) {
            return false
        }

    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    fun isApplicationBroughtToBackground(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            val topActivity = tasks[0].topActivity
            if (topActivity.packageName != context.packageName) {
                return true
            }
        }
        return false
    }

    /**
     * 是否在后台
     *
     * @param context
     * @return
     */
    fun isBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    CmLog.i("后台", appProcess.processName)
                    return true
                } else {
                    CmLog.i("前台", appProcess.processName)
                    return false
                }
            }
        }
        return false
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.applicationContext
                .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val packageName = context.applicationContext.packageName
        /**
         * 获取Android设备中所有正在运行的App
         */
        /**
         * 获取Android设备中所有正在运行的App
         */
        val appProcesses = activityManager
                .runningAppProcesses ?: return false

        for (appProcess in appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName == packageName && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }

        return false
    }

    /**
     * 获取当前手机状态
     *
     * @param context
     * @return
     */
    fun getDeviceStatus(context: Context): Int {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isScreenOn = pm.isScreenOn
        val mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val flag = mKeyguardManager.inKeyguardRestrictedInputMode()
        return if (isScreenOn) {
            if (!flag) {
                1//未锁屏
            } else {
                2//锁屏
            }
        } else {
            //黑屏
            3
        }
    }

    /**
     * 点亮屏幕
     *
     * @param context
     */
    fun accquireScreenOn(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取电源管理器对象
        val wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK, "bright")
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        wl.acquire()
        //点亮屏幕


        //重新启用自动加锁
        wl.release()
    }

    @SuppressLint("MissingPermission")
            /**
     * 解锁
     *
     * @param context
     */
    fun accquireKeyguardLock(context: Context) {
        //得到键盘锁管理器对象
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        //参数是LogCat里用的Tag
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //自动锁
        kl.reenableKeyguard()
    }

    /**
     * 显示输入法
     */
    fun showKeyBord(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    /**
     * 开关输入法
     */
    fun hideKeyBord(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (!activity.isFinishing && imm != null && activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }
}
