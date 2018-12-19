package com.common.core.utils

import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.widget.EditText

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author liulun
 * @version V1.0
 * @Description: 系统工具
 * @date 2016/12/2 10:05
 */
object SystemToolUtils {
    fun isPhoneNumber(str: String): Boolean {
        if (TextUtils.isEmpty(str)) return false
        return if (str.length != 11) false else true
    }

    fun isChinese(str: String): Boolean {
        return Pattern.matches("^[\u4E00-\u9FA5]+$", str)
    }

    fun isDouble(str: String): Boolean {
        try {
            java.lang.Double.valueOf(str)
            return true
        } catch (e: Exception) {
            return false
        }

    }

    fun getFloat(str: String): Float {
        try {
            return java.lang.Float.valueOf(str)!!
        } catch (e: Exception) {
            return 0f
        }

    }

    fun getOneFloat(num: Float): Float {
        return (num * 10).toInt().toFloat() / 10
    }

    fun <T> getValue(clazz: Class<T>, value: String): T {
        return if (clazz == String::class) {
            value.toString() as T
        } else if (clazz == Byte::class|| clazz == Byte::class) {
            java.lang.Byte.valueOf(value) as T
        } else if (clazz == Short::class|| clazz == Short::class) {
            java.lang.Short.valueOf(value) as T
        } else if (clazz == Int::class|| clazz == Int::class) {
            Integer.valueOf(value) as T
        } else if (clazz == Long::class|| clazz == Long::class) {
            java.lang.Long.valueOf(value) as T
        } else if (clazz == Float::class|| clazz == Float::class) {
            java.lang.Float.valueOf(value) as T
        } else if (clazz == Double::class|| clazz == Double::class) {
            java.lang.Double.valueOf(value) as T
        } else if (clazz == Boolean::class|| clazz == Boolean::class) {
            java.lang.Boolean.valueOf(value) as T
        } else {
            value as T
        }
    }

    /**
     * 是否金额
     *
     * @return
     */
    fun isDecimalMoney(str: String): Boolean {
        return Pattern.matches("^\\d+(\\.\\d{1,2})?$", str)
    }

    fun isInteger(str: String): Boolean {
        return Pattern.matches("^\\d+$", str)
    }

    fun isEqual(text1: String?, text2: String?): Boolean {
        return if (text1 == null) {
            text2 == null
        } else {
            text1 == text2
        }
    }
}
