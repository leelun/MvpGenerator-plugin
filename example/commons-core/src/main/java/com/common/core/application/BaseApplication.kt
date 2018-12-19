package com.common.core.application

import android.content.Context
import android.content.res.Resources
import android.support.multidex.MultiDexApplication
import android.os.StrictMode
import android.os.Build
import com.common.core.exception.FileExceptionHandler
import com.common.core.manager.AppManager
import com.common.core.utils.ScreenUtils
import java.lang.Thread.getDefaultUncaughtExceptionHandler


/**
 * @author leellun
 * @version V1.0
 * @Description:
 * @date 2018/10/10 18:24
 */
open abstract class BaseApplication : MultiDexApplication() {
    private var mScreenHeightDp: Int = 0
    private var mScreenWidthDp: Int = 0

    companion object {
        private var mApplication: Context? = null
        fun getInstance(): Context {
            return mApplication!!;
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication=this
        if (getDefaultUncaughtExceptionHandler() == null) {
            Thread.setDefaultUncaughtExceptionHandler(FileExceptionHandler(applicationContext))
        } else {
            Thread.setDefaultUncaughtExceptionHandler(getDefaultUncaughtExceptionHandler())
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
        mScreenHeightDp = ScreenUtils.getScreenHeight(this)
        mScreenWidthDp = ScreenUtils.getScreenWidth(this)
    }

    /**
     * 返回默认未捕获异常处理器
     *
     * @return
     * @throws
     * @author LiuLun
     * @Time 2015年11月9日下午4:43:49
     */
    fun getDefaultUncaughtExceptionHandler(): Thread.UncaughtExceptionHandler?{
        return null
    }

    override fun getResources(): Resources {
        val res = super.getResources()
        val config = res.configuration
        config.setToDefaults()
        config.screenHeightDp = mScreenHeightDp
        config.screenWidthDp = mScreenWidthDp
        res.updateConfiguration(config, super.getResources().displayMetrics)
        return res
    }
}