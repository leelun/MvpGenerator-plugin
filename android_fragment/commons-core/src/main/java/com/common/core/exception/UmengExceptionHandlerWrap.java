package com.common.core.exception;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.common.core.utils.DateUtils;
import com.common.core.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author liulun
 * @version V1.0
 * @Description: 友盟错误处理器
 * @date 2016/11/17 18:03
 */
public class UmengExceptionHandlerWrap implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;
    private String mPackageName;

    public UmengExceptionHandlerWrap(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    public UmengExceptionHandlerWrap(Thread.UncaughtExceptionHandler uncaughtExceptionHandler, String packagename) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler;
        mPackageName = packagename;
    }

    public void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        mUncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        handleException(ex);
        if (mUncaughtExceptionHandler != null) {
            Log.i("umeng", mUncaughtExceptionHandler + "---");
            mUncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 未捕获异常处理
     *
     * @param ex
     * @author LiuLun
     */
    protected void handleException(Throwable ex) {
        if (ex != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            FileUtils.writeStr(Environment.getExternalStorageDirectory().getPath() + (TextUtils.isEmpty(mPackageName) ? "" : ("/" + mPackageName)) + "/applicationerror.txt", DateUtils.formatNow() + ":\r\n");
            try {
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, "utf-8"));
                ex.printStackTrace(writer);
                writer.flush();
                FileUtils.writeStr(Environment.getExternalStorageDirectory().getPath() + (TextUtils.isEmpty(mPackageName) ? "" : ("/" + mPackageName)) + "/applicationerror.txt", bos.toByteArray());
                writer.close();
                bos.close();
            } catch (Exception e) {
                FileUtils.writeStr(Environment.getExternalStorageDirectory().getPath() + (TextUtils.isEmpty(mPackageName) ? "" : ("/" + mPackageName)) + "/applicationerror.txt", ex.getMessage());
            }
            FileUtils.writeStr(Environment.getExternalStorageDirectory().getPath() + (TextUtils.isEmpty(mPackageName) ? "" : ("/" + mPackageName)) + "/applicationerror.txt", "\r\n\n");
        }
    }

    ;
}
