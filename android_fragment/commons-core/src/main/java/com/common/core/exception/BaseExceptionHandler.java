package com.common.core.exception;

import android.content.Context;
import android.os.Looper;

import com.common.core.manager.AppManager;
import com.common.core.utils.FileUtils;
import com.common.core.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
/**
 * 未捕获异常处理基类
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月9日
 */
public abstract class BaseExceptionHandler implements UncaughtExceptionHandler{
	protected Context mContext;
	public BaseExceptionHandler(Context context){
		mContext=context;
	}
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if(!handleException(ex)){
			//对于未捕获异常进行相应处理
			// 使用Toast来显示异常信息
			AppManager.getAppManager().exit(mContext);
			new Thread(new Runnable() {
				@Override
				public void run() {
					Looper.prepare();
					ToastUtils.show(mContext, "很抱歉,程序出现异常");
					Looper.loop();
				}
			}).start();
			// 保存日志文件
			saveCrashInfo2InternetOrFile(ex);
		}
	}
	/**
	 * 保存错误信息到网络或文件中
	 * @param ex
	 */
	private void saveCrashInfo2InternetOrFile(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		save2File(writer.toString());
		printWriter.close();
	}
	/**
	 * @Title: save2File
	 * @Description: TODO 保存为文件
	 * @param ex 异常信息
	 * @return 文件名
	 */
	private String save2File(String ex) {
		try {
			String fileName ="log/"+ System.currentTimeMillis()+ ".log";
			String path= FileUtils.getDiskCacheDir(mContext);
			FileOutputStream fos=new FileOutputStream(path+File.separator+fileName);
			fos.write(ex.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 未捕获异常处理
	 * @param ex
	 * @author LiuLun
	 * @Time 2015年11月9日下午4:18:41
	 */
	protected abstract boolean handleException(Throwable ex);
}
