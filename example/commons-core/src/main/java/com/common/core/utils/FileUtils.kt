package com.common.core.utils

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils

import com.common.core.exception.BaseException

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.RandomAccessFile
import java.math.BigInteger
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.security.MessageDigest
import java.util.ArrayList

/**
 * 文件操作工具类
 */
object FileUtils {

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    val innerSDCardPath: String
        get() = Environment.getExternalStorageDirectory().path

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    val extSDCardPath: List<String>
        get() {
            val lResult = ArrayList<String>()
            try {
                val rt = Runtime.getRuntime()
                val proc = rt.exec("mount")
                val `is` = proc.inputStream
                val isr = InputStreamReader(`is`)
                val br = BufferedReader(isr)
                var line: String
                while (true) {
                    line = br.readLine()?:break
                    if (line.contains("extSdCard")) {
                        val arr = line.split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                        val path = arr[1]
                        val file = File(path)
                        if (file.isDirectory()) {
                            lResult.add(path)
                        }
                    }
                }
                isr.close()
            } catch (e: Exception) {
            }

            return lResult
        }

    /**
     * @return 若不存在外置SD卡 则返回内置SD卡根路径
     * @Title: getRootPath
     * @Description: TODO 获取SD卡根路径
     */
    val rootPath: String
        get() {
            val path = extSDCardPath
            return if (path != null && path.size > 0) {
                extSDCardPath[0]
            } else {
                innerSDCardPath
            }
        }

    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return 缓存文件夹
     * @author LiuLun
     * @Time 2015年11月9日下午4:28:09
     */
    fun getDiskCacheDir(context: Context): String {
        var dir: String? = null
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && Environment.isExternalStorageEmulated()) {
            dir = context.externalCacheDir!!.absolutePath
        } else {
            dir = context.cacheDir.absolutePath
        }
        return dir

    }

    fun getEnvironmentOrCacheDir(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()
                && Environment.isExternalStorageEmulated()) {
            Environment.getExternalStorageDirectory().absolutePath
        } else {
            getDiskCacheDir(context)
        }
    }

    /**
     * 创建sd卡文件
     *
     * @param path
     * @return sd卡文件
     * @author LiuLun
     * @Time 2015年11月9日下午4:37:15
     */
    fun createFileOnSDisk(path: String): String {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() && Environment.isExternalStorageEmulated()) {
            throw BaseException("SD卡不可写")
        }
        val file = File(path)
        if (file.exists())
            throw BaseException("该文件已经存在了")
        if (!file.parentFile.exists())
            file.parentFile.mkdirs()
        try {
            file.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
            throw BaseException(e)
        }

        return path
    }

    /**
     * 写文件(追加)
     *
     * @param path    文件路径
     * @param content 内容
     * @return 日志记录状态
     * @author LiuLun
     * @Time 2015年11月9日下午5:50:33
     */
    @Deprecated("")
    fun writeStr(path: String, content: String): Boolean {
        try {
            CmLog.e("filepath", path)
            val file = File(path)
            if (!file.exists()) {
                if (!file.parentFile.exists())
                    file.parentFile.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rw")
            raf.seek(raf.length())
            raf.write(content.toByteArray(Charset.forName("utf-8")))
            raf.close()
            return true
        } catch (e: IOException) {
            return false
        }

    }

    fun fileExist(path: String): Boolean {
        return if (!TextUtils.isEmpty(path)) File(path).exists() else false
    }

    @Deprecated("")
    fun writeStr(path: String, bytes: ByteArray): Boolean {
        try {
            CmLog.e("filepath", path)
            val file = File(path)
            if (!file.exists()) {
                if (!file.parentFile.exists())
                    file.parentFile.mkdirs()
                file.createNewFile()
            }
            val raf = RandomAccessFile(file, "rw")
            raf.seek(raf.length())
            raf.write(bytes)
            raf.close()
            return true
        } catch (e: IOException) {
            return false
        }

    }

    /**
     * 写文件
     *
     * @param path
     * @param content
     * @param append  是否追加
     * @return
     */
    fun writeStr(path: String, content: String, append: Boolean): Boolean {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            val writer = FileWriter(path, append)
            writer.write(content)
            writer.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

    }

    /**
     * 读取指定路径文本内容
     *
     * @param path
     * @return 文件内容
     * @author LiuLun
     * @Time 2015年11月9日下午5:58:05
     */
    fun readStr(path: String): String? {
        try {
            val file = File(path)
            if (!file.exists())
                return null
            val fis = FileInputStream(file)
            val br = BufferedReader(InputStreamReader(fis, "gbk"))
            val sb = StringBuilder()
            var s: String? = null
            while (true) {
                s = br.readLine()?:break
                sb.append(s)
            }
            br.close()
            fis.close()
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    /**
     * 删除文件
     */
    fun deleteFile(file: File) {
        if (!file.exists()) return
        if (file.isDirectory) {
            for (f in file.listFiles()) {
                deleteFile(f)
            }
        } else {
            file.delete()
        }
    }

    fun getUriFile(context: Context, uri: Uri): File? {
        val path = getUriPath(context, uri) ?: return null
        return File(path)
    }

    fun getUriPath(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if ("com.android.providers.downloads.documents" == uri.authority) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)!!)
                return getDataColumn(context, contentUri, null, null)
            } else if ("com.android.providers.media.documents" == uri.authority) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf<String>(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if ("com.google.android.apps.photos.content" == uri.authority) {
                uri.lastPathSegment
            } else getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun getFileSizeStr(size: Long): String {
        var fileSize = size.toDouble() / 1024
        if (fileSize < 1024) {
            return String.format("%.1fK", fileSize)
        }
        fileSize /= 1024.0
        if (fileSize < 1024) {
            return String.format("%.1fM", fileSize)
        }
        fileSize /= 1024.0
        return String.format("%.1fG", fileSize)
    }

    fun getMd5ByFile(file: File): String? {
        var value: String? = null
        var `in`: FileInputStream? = null
        try {
            `in` = FileInputStream(file)
            val byteBuffer = `in`.channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length())
            val md5 = MessageDigest.getInstance("MD5")
            md5.update(byteBuffer)
            val bi = BigInteger(1, md5.digest())
            value = bi.toString(16)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (null != `in`) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return value
    }
}
