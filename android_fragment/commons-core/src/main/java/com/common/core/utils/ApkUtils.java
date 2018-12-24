package com.common.core.utils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

/**
 * 版本号工具类
 */
public class ApkUtils {
    /**
     * 获取本地路径的apk版本号
     *
     * @param context
     * @param path
     * @author LiuLun
     * @Time 2015年11月17日下午2:24:22
     */
    public static PackageInfo getDiskApkPackageInfo(Context context, String path) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }

    /**
     * @param context
     * @return
     * @throws NameNotFoundException
     * @Title: getApplicationPackageInfo
     * @Description: TODO 获取当前程序版本信息
     */
    public static PackageInfo getApplicationPackageInfo(Context context) throws NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
    }

    /**
     * @param context
     * @param path
     * @return
     * @Title: isSameApp
     * @Description: TODO 是否相同APP
     */
    public static boolean isSameApp(Context context, String path) {
        try {
            PackageInfo packageInfo = getDiskApkPackageInfo(context, path);
            return packageInfo == null ? false
                    : getDiskApkPackageInfo(context, path).versionCode == getApplicationPackageInfo(context).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param context
     * @param path
     * @return
     * @Title: installApk
     * @Description: TODO 安装APK
     */
    public static boolean installApk(Context context, String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        if (!file.exists())
            return false;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
        return true;
    }

    /**
     * 判断是否安装制定包
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstallApk(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }

    /**
     * 检测 响应某个意图的Activity 是否存在
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }

    /**
     * 获取sha1
     *
     * @param context
     * @return
     */
    public static String getSha1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the Signature of apk file which is not installed.
     *
     * @param apkPath
     * @return
     */
    public static String getApkSignature(String apkPath) {
        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
            Class<?>[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser;
            if (Build.VERSION.SDK_INT > 19) {
                pkgParser = pkgParserCls.newInstance();
            } else {
                Constructor constructor = pkgParserCls.getConstructor(typeArgs);
                pkgParser = constructor.newInstance(valueArgs);
            }

            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();

            Object pkgParserPkg = null;
            if (Build.VERSION.SDK_INT > 19) {
                typeArgs = new Class[2];
                typeArgs[0] = File.class;
                typeArgs[1] = int.class;

                Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
                        "parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[2];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
                        valueArgs);
            } else {
                typeArgs = new Class[4];
                typeArgs[0] = File.class;
                typeArgs[1] = String.class;
                typeArgs[2] = DisplayMetrics.class;
                typeArgs[3] = int.class;

                Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
                        "parsePackage", typeArgs);
                pkgParser_parsePackageMtd.setAccessible(true);

                valueArgs = new Object[4];
                valueArgs[0] = new File(apkPath);
                valueArgs[1] = apkPath;
                valueArgs[2] = metrics;
                valueArgs[3] = PackageManager.GET_SIGNATURES;
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
                        valueArgs);
            }


            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = int.class;
            Method pkgParser_collectCertificatesMtd = pkgParserCls.getDeclaredMethod("collectCertificates", typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField(
                    "mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            return info[0].toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get the Signature of the apk file which package name is @param packageName
     *
     * @param context
     * @param packageName
     * @return packageSignatrue
     */
    public static String getInstallPackageSignature(Context context,
                                                    String packageName) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);

        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String thisName = packageinfo.packageName;
            if (thisName.equals(packageName)) {
                return packageinfo.signatures[0].toCharsString();
            }
        }

        return null;
    }

    public static String getCacheApkFilePath(Context ctx, String fileUrl) {
        return ctx.getExternalCacheDir() + "/" + StringUtils.md5(fileUrl) + ".apk";
    }

    /**
     * Compare  the patch apk to the apk which package name is ctx.getPackageName()
     *
     * @param ctx
     * @param apkFile the patch apk
     * @return the result of compare
     **/
    public static boolean isSignEqual(Context ctx, String apkFile) {
        String packageSign = getInstallPackageSignature(ctx, ctx.getPackageName());
        String apkFileSign = getApkSignature(apkFile);
        return TextUtils.equals(packageSign, apkFileSign);
    }
}
