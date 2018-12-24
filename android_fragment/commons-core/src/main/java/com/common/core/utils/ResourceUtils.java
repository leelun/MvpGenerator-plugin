package com.common.core.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 * @author liulun
 * @version V1.0
 * @Description: 资源获取工具
 * @date 2016/11/18 15:02
 */
public class ResourceUtils {
    public static int getColor(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(resId);
        } else {
            return context.getResources().getColor(resId);
        }
    }

    public static Drawable getDrawable(Context context, int resId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(resId);
        } else {
            drawable = context.getResources().getDrawable(resId);
        }
        if (drawable.getIntrinsicHeight() > 0 && drawable.getIntrinsicWidth() > 0) {
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        }
        return drawable;
    }

    public static int dipToPx(Context context, float dip) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
        if (px == 0) {
            return (int) (dip * metrics.density + 0.5);
        }
        return px;
    }
    public static int pxToDip(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (px/metrics.density);
    }
    public static int spToPx(Context context, float dip) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics);
        if (px == 0) {
            return (int) (dip * metrics.scaledDensity + 0.5);
        }
        return px;
    }
    public static int pxToSp(Context context, float px) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (px/metrics.scaledDensity);
    }
}
