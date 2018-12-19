package com.common.core.utils

import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan

/**
 * @author liulun
 * @version V1.0
 * @Description: spannable工具
 * @date 2016/11/28 15:45
 */
object SpannableUtils {
    fun getSizeSpannable(content: String, start: Int, end: Int, size: Int): SpannableString {
        val spannableString = SpannableString(content)
        spannableString.setSpan(AbsoluteSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    fun getColorSpannable(content: String, start: Int, end: Int, color: Int): SpannableString {
        val spannableString = SpannableString(content)
        spannableString.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }
}
