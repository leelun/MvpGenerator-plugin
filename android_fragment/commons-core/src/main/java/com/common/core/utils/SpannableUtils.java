package com.common.core.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @author liulun
 * @version V1.0
 * @Description: spannable工具
 * @date 2016/11/28 15:45
 */
public class SpannableUtils {
    public static SpannableString getSizeSpannable(String content,int start,int end,int size){
        SpannableString spannableString=new SpannableString(content);
        spannableString.setSpan(new AbsoluteSizeSpan(size),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
    public static SpannableString getColorSpannable(String content,int start,int end,int color){
        SpannableString spannableString=new SpannableString(content);
        spannableString.setSpan(new ForegroundColorSpan(color),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
