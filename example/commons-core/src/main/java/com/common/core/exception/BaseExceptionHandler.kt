package com.common.core.exception

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer
import java.lang.Thread.UncaughtExceptionHandler

import android.content.Context
import android.os.Looper

import com.common.core.manager.AppManager
import com.common.core.utils.FileUtils
import com.common.core.utils.ToastUtils

/**
 * 未捕获异常处理基类
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月9日
 */
abstract class BaseExceptionHandler(protected var mContext: Context) : UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex)) {
            //对于未捕获异常进行相应处理
            // 使用Toast来显示异常信息
            AppManager.getAppManager().AppExit(mContext)
            Thread(Runnable {
                Looper.prepare()
                ToastUtils.show("很抱歉,程序出现异常")
                Looper.loop()
            }).start()
            // 保存日志文件
            saveCrashInfo2InternetOrFile(ex)
        }
    }

    /**
     * 保存错误信息到网络或文件中
     * @param ex
     */
    private fun saveCrashInfo2InternetOrFile(ex: Throwable) {
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        save2File(writer.toString())
        printWriter.close()
    }

    /**
     * @Title: save2File
     * @Description: TODO 保存为文件
     * @param ex 异常信息
     * @return 文件名
     */
    private fun save2File(ex: String): String? {
        try {
            val fileName = "log/" + System.currentTimeMillis() + ".log"
            val path = FileUtils.getDiskCacheDir(mContext)
            val fos = FileOutputStream(path + File.separator + fileName)
            fos.write(ex.toByteArray())
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 未捕获异常处理
     * @param ex
     * @author LiuLun
     * @Time 2015年11月9日下午4:18:41
     */
    protected abstract fun handleException(ex: Throwable): Boolean
}
