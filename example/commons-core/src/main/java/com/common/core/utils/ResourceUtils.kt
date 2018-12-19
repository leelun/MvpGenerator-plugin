package com.common.core.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue

import junit.runner.Version

/**
 * @author liulun
 * @version V1.0
 * @Description: 资源获取工具
 * @date 2016/11/18 15:02
 */
object ResourceUtils {
    fun getColor(context: Context, resId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(resId)
        } else {
            context.resources.getColor(resId)
        }
    }

    fun getDrawable(context: Context, resId: Int): Drawable {
        val drawable: Drawable?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getDrawable(resId)
        } else {
            drawable = context.resources.getDrawable(resId)
        }
        if (drawable!!.intrinsicHeight > 0 && drawable.intrinsicWidth > 0) {
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        }
        return drawable
    }

    fun dipToPx(context: Context, dip: Float): Int {
        val metrics = context.resources.displayMetrics
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics).toInt()
        return if (px == 0) {
            (dip * metrics.density + 0.5).toInt()
        } else px
    }

    fun pxToDip(context: Context, px: Float): Int {
        val metrics = context.resources.displayMetrics
        return (px / metrics.density).toInt()
    }

    fun spToPx(context: Context, dip: Float): Int {
        val metrics = context.resources.displayMetrics
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, metrics).toInt()
        return if (px == 0) {
            (dip * metrics.scaledDensity + 0.5).toInt()
        } else px
    }

    fun pxToSp(context: Context, px: Float): Int {
        val metrics = context.resources.displayMetrics
        return (px / metrics.scaledDensity).toInt()
    }
}
