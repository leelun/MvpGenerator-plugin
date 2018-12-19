package com.common.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.Signature
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.DisplayMetrics
import java.io.File
import java.security.MessageDigest
import java.util.*

/**
 * 版本号工具类
 */
object ApkUtils {
    /**
     * 获取本地路径的apk版本号
     *
     * @param context
     * @param path
     * @author LiuLun
     * @Time 2015年11月17日下午2:24:22
     */
    fun getDiskApkPackageInfo(context: Context, path: String): PackageInfo? {
        val packageManager = context.packageManager
        return packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
    }

    /**
     * @param context
     * @return
     * @throws NameNotFoundException
     * @Title: getApplicationPackageInfo
     * @Description: TODO 获取当前程序版本信息
     */
    @Throws(NameNotFoundException::class)
    fun getApplicationPackageInfo(context: Context): PackageInfo {
        return context.packageManager.getPackageInfo(context.packageName, 0)
    }

    /**
     * @param context
     * @param path
     * @return
     * @Title: isSameApp
     * @Description: TODO 是否相同APP
     */
    fun isSameApp(context: Context, path: String): Boolean {
        try {
            val packageInfo = getDiskApkPackageInfo(context, path)
            return if (packageInfo == null)
                false
            else
                getDiskApkPackageInfo(context, path)!!.versionCode == getApplicationPackageInfo(context).versionCode
        } catch (e: NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            return false
        }

    }

    /**
     * @param context
     * @param path
     * @return
     * @Title: installApk
     * @Description: TODO 安装APK
     */
    fun installApk(context: Context, path: String): Boolean {
        if (TextUtils.isEmpty(path))
            return false
        val file = File(path)
        if (!file.exists())
            return false
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
        context.startActivity(intent)
        return true
    }

    /**
     * 判断是否安装制定包
     *
     * @param context
     * @param packageName
     * @return
     */
    fun isInstallApk(context: Context, packageName: String): Boolean {
        //获取packagemanager
        val packageManager = context.packageManager
        //获取所有已安装程序的包信息
        val packageInfos = packageManager.getInstalledPackages(0)
        //用于存储所有已安装程序的包名
        val packageNames = ArrayList<String>()
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (i in packageInfos.indices) {
                val packName = packageInfos[i].packageName
                packageNames.add(packName)
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName)
    }

    @SuppressLint("WrongConstant")
            /**
     * 检测 响应某个意图的Activity 是否存在
     *
     * @param context
     * @param intent
     * @return
     */
    fun isIntentAvailable(context: Context, intent: Intent): Boolean {
        val packageManager = context.packageManager
        val list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES)
        return list.size > 0
    }

    /**
     * 获取sha1
     *
     * @param context
     * @return
     */
    fun getSha1(context: Context): String? {
        try {
            val info = context.packageManager.getPackageInfo(
                    context.packageName, PackageManager.GET_SIGNATURES)
            val cert = info.signatures[0].toByteArray()
            val md = MessageDigest.getInstance("SHA1")
            val publicKey = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                        .toUpperCase(Locale.US)
                if (appendString.length == 1)
                    hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Get the Signature of apk file which is not installed.
     *
     * @param apkPath
     * @return
     */
    fun getApkSignature(apkPath: String): String? {
        val PATH_PackageParser = "android.content.pm.PackageParser"
        try {
            val pkgParserCls = Class.forName(PATH_PackageParser)
            var typeArgs = arrayOfNulls<Class<*>>(1)
            typeArgs[0] = String::class.java
            var valueArgs = arrayOfNulls<Any>(1)
            valueArgs[0] = apkPath
            val pkgParser: Any
            if (Build.VERSION.SDK_INT > 19) {
                pkgParser = pkgParserCls.newInstance()
            } else {
                val constructor = pkgParserCls.getConstructor(*typeArgs)
                pkgParser = constructor.newInstance(*valueArgs)
            }

            val metrics = DisplayMetrics()
            metrics.setToDefaults()

            var pkgParserPkg: Any? = null
            if (Build.VERSION.SDK_INT > 19) {
                typeArgs = arrayOfNulls(2)
                typeArgs[0] = File::class.java
                typeArgs[1] = Int::class.javaPrimitiveType

                val pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
                        "parsePackage", *typeArgs)
                pkgParser_parsePackageMtd.isAccessible = true

                valueArgs = arrayOfNulls(2)
                valueArgs[0] = File(apkPath)
                valueArgs[1] = PackageManager.GET_SIGNATURES
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
                        *valueArgs)
            } else {
                typeArgs = arrayOfNulls(4)
                typeArgs[0] = File::class.java
                typeArgs[1] = String::class.java
                typeArgs[2] = DisplayMetrics::class.java
                typeArgs[3] = Int::class.javaPrimitiveType

                val pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
                        "parsePackage", *typeArgs)
                pkgParser_parsePackageMtd.isAccessible = true

                valueArgs = arrayOfNulls(4)
                valueArgs[0] = File(apkPath)
                valueArgs[1] = apkPath
                valueArgs[2] = metrics
                valueArgs[3] = PackageManager.GET_SIGNATURES
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
                        *valueArgs)
            }


            typeArgs = arrayOfNulls(2)
            typeArgs[0] = pkgParserPkg!!.javaClass
            typeArgs[1] = Int::class.javaPrimitiveType
            val pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates", *typeArgs)
            valueArgs = arrayOfNulls(2)
            valueArgs[0] = pkgParserPkg
            valueArgs[1] = PackageManager.GET_SIGNATURES
            pkgParser_collectCertificatesMtd.invoke(pkgParser, *valueArgs)
            val packageInfoFld = pkgParserPkg.javaClass.getDeclaredField(
                    "mSignatures")
            val info = packageInfoFld.get(pkgParserPkg) as Array<Signature>
            return info[0].toCharsString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * Get the Signature of the apk file which package name is @param packageName
     *
     * @param context
     * @param packageName
     * @return packageSignatrue
     */
    fun getInstallPackageSignature(context: Context,
                                   packageName: String): String? {
        val pm = context.packageManager
        val apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES)

        val iter = apps.iterator()
        while (iter.hasNext()) {
            val packageinfo = iter.next()
            val thisName = packageinfo.packageName
            if (thisName == packageName) {
                return packageinfo.signatures[0].toCharsString()
            }
        }

        return null
    }

    fun getCacheApkFilePath(ctx: Context, fileUrl: String): String {
        return ctx.externalCacheDir.toString() + "/" + StringUtils.md5(fileUrl) + ".apk"
    }

    /**
     * Compare  the patch apk to the apk which package name is ctx.getPackageName()
     *
     * @param ctx
     * @param apkFile the patch apk
     * @return the result of compare
     */
    fun isSignEqual(ctx: Context, apkFile: String): Boolean {
        val packageSign = getInstallPackageSignature(ctx, ctx.packageName)
        val apkFileSign = getApkSignature(apkFile)
        return TextUtils.equals(packageSign, apkFileSign)
    }
}
