package com.common.core.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {

	private static Toast mToast;
	private static Handler mHandler = new Handler();
	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void show(Context context, String msg) {
		if (TextUtils.isEmpty(msg))
			return;
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(msg);
		else
			mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		mHandler.postDelayed(r, 2000);

		mToast.show();

	}

	public static void show(Context context, int msg) {
		show(context, context.getResources().getString(msg));
	}
}
