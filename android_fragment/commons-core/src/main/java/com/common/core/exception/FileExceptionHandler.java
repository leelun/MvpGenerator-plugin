package com.common.core.exception;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.common.core.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

/**
 * 对未捕获异常进行处理
 * @author 刘伦
 * @version 1.0
 * @date 2015年11月9日
 */
public class FileExceptionHandler extends BaseExceptionHandler{

	public FileExceptionHandler(Context context) {
		super(context);
	}

	@Override
	protected boolean handleException(Throwable ex) {
		Log.i("sdfsdf","sdfsdfsdf");
		if(ex==null)return false;
		ex.printStackTrace();
		//-----------业务逻辑进行处理-------------
		Log.i("ssssssssssss", ex.getMessage());
		saveCrashInfo2InternetOrFile(ex);
		return true;
	}
	/**
	 * 保存错误信息到网络或文件中
	 * @param ex
	 */
	private void saveCrashInfo2InternetOrFile(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		final String exStr = sb.toString();
	}
	/**
	 * @Title: save2File
	 * @Description: TODO 保存为文件
	 * @param ex 异常信息
	 * @return 文件名
	 */
	private String save2File(String ex) {
		try {
			long timestamp = System.currentTimeMillis();
			String time = System.currentTimeMillis()+"";
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				String path = "";
				String root = "";
				File file = null;
				List<String> list = FileUtils.getExtSDCardPath();
				if (list.size() == 0) {
					root = FileUtils.getInnerSDCardPath();
				} else {
					root = list.get(0);
				}
				path = root + "/fyz/crash/";
				file = new File(path);
				if (!file.exists()) {
					file.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(path + fileName);
				fos.write(ex.getBytes());
				fos.close();
				return fileName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
