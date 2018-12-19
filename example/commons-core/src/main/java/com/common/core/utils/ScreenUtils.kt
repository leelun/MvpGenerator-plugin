package com.common.core.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager

@SuppressLint("NewApi")
object ScreenUtils {
    fun getShotScreenWithStatusBar(activity: Activity): Bitmap {
        val height = getScreenHeight(activity)
        val width = getScreenWidth(activity)
        val outRect = Rect()
        activity.window.decorView
                .getWindowVisibleDisplayFrame(outRect)
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        val bitmap = view.drawingCache
        val screenBitmap = Bitmap.createBitmap(bitmap, 0, outRect.top,
                width, height)
        bitmap.recycle()
        view.isDrawingCacheEnabled = false
        return screenBitmap
    }

    fun getShopScreenWithOutStatusBar(activity: Activity): Bitmap {
        val height = getScreenHeight(activity)
        val width = getScreenWidth(activity)
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        val bitmap = view.drawingCache
        val screenBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
        bitmap.recycle()
        view.isDrawingCacheEnabled = false
        return screenBitmap
    }

    /**
     * 获得屏幕高度
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    fun getScreenWidth(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕宽度
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    fun getScreenHeight(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val realSize = Point()
        display.getRealSize(realSize)
        return realSize.y
    }

    /**
     * 获取显示高度
     * @param context
     * @return
     */
    fun getWindowHeight(context: Context): Int {
        val wm = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.heightPixels
    }

    /**
     * 获得状态栏的高度
     *
     * @param context 上下文
     * @return 状态栏高度
     */
    fun getStatusHeight(context: Context): Int {

        var statusHeight = -1
        if (context is Activity) {
            val frame = Rect()
            context.window.decorView
                    .getWindowVisibleDisplayFrame(frame)
            statusHeight = frame.top
            if (statusHeight > 0) return statusHeight
        }
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(`object`).toString())
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusHeight
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return 图片
     */
    fun snapShotWithStatusBar(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap? = null
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height)
        view.destroyDrawingCache()
        return bp

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return 屏幕截图
     */
    fun snapShotWithoutStatusBar(activity: Activity): Bitmap? {
        val view = activity.window.decorView
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bmp = view.drawingCache
        val frame = Rect()
        activity.window.decorView.getWindowVisibleDisplayFrame(frame)
        val statusBarHeight = frame.top

        val width = getScreenWidth(activity)
        val height = getScreenHeight(activity)
        var bp: Bitmap? = null
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight)
        view.destroyDrawingCache()
        return bp

    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取底部导航高度
     *
     * @param context
     * @author LiuLun
     * @Time 2015年7月1日下午2:43:04
     */
    fun getNavigationBarHeight(context: Context): Int {
        if (!isNavigationBarShow(context)) {
            return 0
        }
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height",
                "dimen", "android")
        //获取NavigationBar的高度
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 是否存在底部菜单
     *
     * @param context
     * @return
     */
    fun isNavigationBarShow(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val wm = context
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            val size = Point()
            val realSize = Point()
            display.getSize(size)
            display.getRealSize(realSize)
            return realSize.y != size.y
        } else {
            val menu = ViewConfiguration.get(context).hasPermanentMenuKey()
            val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            return if (menu || back) {
                false
            } else {
                true
            }
        }
    }

    /**
     * 检查navigationBar是否存在
     *
     * @param context
     * @author LiuLun
     * @Time 2015年11月18日上午11:13:41
     * 请查看 [.isNavigationBarShow]
     */
    @Deprecated("")
    fun checkNavigationBar(context: Context): Boolean {
        //是否存在菜单键(手机屏幕之外的按键)
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        //是否存在返回键
        val hasBackkey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        return if (!hasMenuKey && !hasBackkey) {
            true
        } else false
    }
}
