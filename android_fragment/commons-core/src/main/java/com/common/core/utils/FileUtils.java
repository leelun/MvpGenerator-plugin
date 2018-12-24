package com.common.core.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.common.core.exception.BaseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作工具类
 */
public class FileUtils {
    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return 缓存文件夹
     * @author LiuLun
     * @Time 2015年11月9日下午4:28:09
     */
    public static String getDiskCacheDir(Context context) {
        String dir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.isExternalStorageEmulated()) {
            dir = context.getExternalCacheDir().getAbsolutePath();
        } else {
            dir = context.getCacheDir().getAbsolutePath();
        }
        return dir;

    }

    public static String getEnvironmentOrCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.isExternalStorageEmulated()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return getDiskCacheDir(context);
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
    public static String createFileOnSDisk(String path) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Environment.isExternalStorageEmulated()) {
            throw new BaseException("SD卡不可写");
        }
        File file = new File(path);
        if (file.exists())
            throw new BaseException("该文件已经存在了");
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(e);
        }
        return path;
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
    public static boolean writeStr(String path, String content) {
        try {
            Log.e("filepath", path);
            File file = new File(path);
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            raf.write(content.getBytes(Charset.forName("utf-8")));
            raf.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean writeStr(String path, byte[] bytes) {
        try {
            Log.e("filepath", path);
            File file = new File(path);
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            raf.write(bytes);
            raf.close();
            return true;
        } catch (IOException e) {
            return false;
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
    public static boolean writeStr(String path, String content, boolean append) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(path, append);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
    public static String readStr(String path) {
        try {
            File file = new File(path);
            if (!file.exists())
                return null;
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, "gbk"));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();
            fis.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFile(f);
            }
        } else {
            file.delete();
        }
    }

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public static String getInnerSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public static List<String> getExtSDCardPath() {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("extSdCard")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    /**
     * @return 若不存在外置SD卡 则返回内置SD卡根路径
     * @Title: getRootPath
     * @Description: TODO 获取SD卡根路径
     */
    public static String getRootPath() {
        List<String> path = getExtSDCardPath();
        if (path != null && path.size() > 0) {
            return getExtSDCardPath().get(0);
        } else {
            return getInnerSDCardPath();
        }
    }

    public static File getUriFile(Context context, Uri uri) {
        String path = getUriPath(context, uri);
        if (path == null) {
            return null;
        }
        return new File(path);
    }

    public static String getUriPath(Context context, Uri uri) {
        if (uri == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    public static String getFileSizeStr(long size) {
        double fileSize = (double) size / 1024;
        if (fileSize < 1024) {
            return String.format("%.1fK", fileSize);
        }
        fileSize /= 1024;
        if (fileSize < 1024) {
            return String.format("%.1fM", fileSize);
        }
        fileSize /= 1024;
        return String.format("%.1fG", fileSize);
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}
