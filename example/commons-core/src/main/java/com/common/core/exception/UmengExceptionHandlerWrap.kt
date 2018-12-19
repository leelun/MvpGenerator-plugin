package com.common.core.exception

import android.os.Environment
import android.text.TextUtils
import com.common.core.utils.CmLog
import com.common.core.utils.DateUtils
import com.common.core.utils.FileUtils
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter

/**
 * @author liulun
 * @version V1.0
 * @Description: 友盟错误处理器
 * @date 2016/11/17 18:03
 */
class UmengExceptionHandlerWrap : Thread.UncaughtExceptionHandler {
    private var mUncaughtExceptionHandler: Thread.UncaughtExceptionHandler? = null
    private var mPackageName: String?=null

    constructor(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler
    }

    constructor(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler, packagename: String) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler
        mPackageName = packagename
    }

    fun setUncaughtExceptionHandler(uncaughtExceptionHandler: Thread.UncaughtExceptionHandler) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler
    }

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        handleException(ex)
        if (mUncaughtExceptionHandler != null) {
            CmLog.i("umeng", mUncaughtExceptionHandler!!.toString() + "---")
            mUncaughtExceptionHandler!!.uncaughtException(thread, ex)
        }
    }

    /**
     * 未捕获异常处理
     *
     * @param ex
     * @author LiuLun
     */
    protected fun handleException(ex: Throwable?) {
        if (ex != null) {
            val bos = ByteArrayOutputStream()
            FileUtils.writeStr(Environment.getExternalStorageDirectory().path + (if (TextUtils.isEmpty(mPackageName)) "" else "/" + mPackageName) + "/applicationerror.txt", DateUtils.formatNow() + ":\r\n")
            try {
                val writer = PrintWriter(OutputStreamWriter(bos, "utf-8"))
                ex.printStackTrace(writer)
                writer.flush()
                FileUtils.writeStr(Environment.getExternalStorageDirectory().path + (if (TextUtils.isEmpty(mPackageName)) "" else "/" + mPackageName) + "/applicationerror.txt", bos.toByteArray())
                writer.close()
                bos.close()
            } catch (e: Exception) {
                FileUtils.writeStr(Environment.getExternalStorageDirectory().path + (if (TextUtils.isEmpty(mPackageName)) "" else "/" + mPackageName) + "/applicationerror.txt", ex.message!!)
            }

            FileUtils.writeStr(Environment.getExternalStorageDirectory().path + (if (TextUtils.isEmpty(mPackageName)) "" else "/" + mPackageName) + "/applicationerror.txt", "\r\n\n")
        }
    }
}
