package com.common.core.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

@SuppressLint("NewApi")
public class ScreenUtils {
	public static Bitmap getShotScreenWithStatusBar(Activity activity) {
		int height = getScreenHeight(activity);
		int width = getScreenWidth(activity);
		Rect outRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(outRect);
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		Bitmap screenBitmap = Bitmap.createBitmap(bitmap, 0, outRect.top,
				width, height);
		bitmap.recycle();
		view.setDrawingCacheEnabled(false);
		return screenBitmap;
	}

	public static Bitmap getShopScreenWithOutStatusBar(Activity activity) {
		int height = getScreenHeight(activity);
		int width = getScreenWidth(activity);
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		Bitmap bitmap = view.getDrawingCache();
		Bitmap screenBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
		bitmap.recycle();
		view.setDrawingCacheEnabled(false);
		return screenBitmap;
	}

	/**
	 * 获得屏幕高度
	 * 
	 * @param context
	 *            上下文
	 * @return 屏幕高度
	 */
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * 获得屏幕宽度
	 * 
	 * @param context
	 *            上下文
	 * @return 屏幕宽度
	 */
	public static int getScreenHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}

	/**
	 * 获得状态栏的高度
	 * 
	 * @param context
	 *            上下文
	 * @return 状态栏高度
	 */
	public static int getStatusHeight(Context context) {

		int statusHeight = -1;
		if (context instanceof Activity) {
			Rect frame = new Rect();
			((Activity) context).getWindow().getDecorView()
					.getWindowVisibleDisplayFrame(frame);
			statusHeight = frame.top;
			if(statusHeight>0)return statusHeight;
		}
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	/**
	 * 获取当前屏幕截图，包含状态栏
	 * 
	 * @param activity
	 * @return 图片
	 */
	public static Bitmap snapShotWithStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 获取当前屏幕截图，不包含状态栏
	 * 
	 * @param activity
	 * @return 屏幕截图
	 */
	public static Bitmap snapShotWithoutStatusBar(Activity activity) {
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap bmp = view.getDrawingCache();
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		
		int width = getScreenWidth(activity);
		int height = getScreenHeight(activity);
		Bitmap bp = null;
		bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
				- statusBarHeight);
		view.destroyDrawingCache();
		return bp;

	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取底部导航高度
	 * @param context
	 * @author LiuLun
	 * @Time 2015年7月1日下午2:43:04
	 */
	public static int getNavigationBarHeight(Context context) {
		Resources resources = context.getResources();
		int rid = resources.getIdentifier("config_showNavigationBar", "bool", "android");
		if (rid > 0 && resources.getBoolean(rid)) {
			int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
			if (resourceId > 0) {
				int NavigationBarHeight = resources.getDimensionPixelSize(resourceId); // 获取高度;
				return NavigationBarHeight;
			}
		}
		return 0;
	}
	/**
	 * 检查navigationBar是否存在
	 * @param context
	 * @author LiuLun
	 * @Time 2015年11月18日上午11:13:41
	 */
	public static boolean checkNavigationBar(Context context){
		//是否存在菜单键(手机屏幕之外的按键)
		boolean hasMenuKey=ViewConfiguration.get(context).hasPermanentMenuKey();
		//是否存在返回键
		boolean hasBackkey=KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
		if(!hasMenuKey&&!hasBackkey){
			return true;
		}
		return false;
	}
}
