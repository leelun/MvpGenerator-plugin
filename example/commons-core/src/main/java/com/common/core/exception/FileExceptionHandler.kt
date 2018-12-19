package com.common.core.exception

import android.content.Context
import android.os.Environment
import com.common.core.utils.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter

/**
 * 对未捕获异常进行处理
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月9日
 */
class FileExceptionHandler(context: Context) : BaseExceptionHandler(context) {
    override fun handleException(ex: Throwable): Boolean {
        if (ex == null) return false
        ex.printStackTrace()
        //-----------业务逻辑进行处理-------------
        saveCrashInfo2InternetOrFile(ex)
        return true
    }

    /**
     * 保存错误信息到网络或文件中
     * @param ex
     */
    private fun saveCrashInfo2InternetOrFile(ex: Throwable) {
        val sb = StringBuffer()
        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause: Throwable? = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        val exStr = sb.toString()
    }

    /**
     * @Title: save2File
     * @Description: TODO 保存为文件
     * @param ex 异常信息
     * @return 文件名
     */
    private fun save2File(ex: String): String? {
        try {
            val timestamp = System.currentTimeMillis()
            val time = System.currentTimeMillis().toString() + ""
            val fileName = "crash-$time-$timestamp.log"
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                var path = ""
                var root = ""
                var file: File? = null
                val list = FileUtils.extSDCardPath
                if (list.size == 0) {
                    root = FileUtils.innerSDCardPath
                } else {
                    root = list.get(0)
                }
                path = root + "/fyz/crash/"
                file = File(path)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val fos = FileOutputStream(path + fileName)
                fos.write(ex.toByteArray())
                fos.close()
                return fileName
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
