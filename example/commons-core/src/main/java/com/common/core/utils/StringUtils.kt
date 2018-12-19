package com.common.core.utils

import android.text.TextUtils

import java.io.UnsupportedEncodingException
import java.math.BigDecimal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.and

/**
 * @author liulun
 * @version V1.0
 * @Description: 字符串验证
 * @date 2016/12/8 10:56
 */
object StringUtils {
    /**
     * @Description: 判断是否是null 如果非空则返回null 否则返回提示
     * @version 1.0
     * @time 2016/12/8 10:59
     * @author leellun
     */
    fun isEmpty(strs: Array<Array<String>>): String? {
        for (i in strs.indices) {
            if (TextUtils.isEmpty(strs[i][0])) {
                return strs[i][1]
            }
        }
        return null
    }

    fun formatMoney(money: Double): String {
        return String.format("%.2f", money)
    }

    fun formatNumber(number: Double): String {
        val format = DecimalFormat("#.##")
        return format.format(number)
    }

    fun formatNumber(number: Double, precision: Int): String {
        return String.format("%." + precision + "f", number)
    }

    fun getDefaultText(str: String?): String {
        return str ?: ""
    }

    fun getDefaultText(str: String?, defualtStr: String): String {
        return if (str == null || str == "") defualtStr else str
    }

    /**
     * 获取text中的一个double数字
     *
     * @param text
     * @return
     */
    fun getDouble(text: String): Double? {
        val pattern = Pattern.compile("\\d+(\\.\\d)*")
        val matcher = pattern.matcher(text)
        return if (matcher.find()) {
            java.lang.Double.parseDouble(matcher.group(0))
        } else {
            null
        }
    }

    /**
     * 获取浮点型数字
     * @param text
     * @return
     */
    fun getDecimalNumber(text: String): Double {
        try {
            return java.lang.Double.parseDouble(text)
        } catch (e: Exception) {
            return 0.0
        }

    }

    fun trim(str: String): String {
        return str.replace("\\s*(\\S*)\\s*".toRegex(), "$1")
    }

    fun removeEmptyChar(str: String): String {
        return str.replace("\\s+".toRegex(), "")
    }

    /**
     * md5
     *
     * @param string
     * @return
     */
    fun md5(string: String): String {
        val hash: ByteArray
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.toByteArray(charset("UTF-8")))
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Huh, MD5 should be supported?", e)
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("Huh, UTF-8 should be supported?", e)
        }

        val hex = StringBuilder(hash.size * 2)
        for (b in hash) {
            if ((b and 0xFF.toByte()) < 0x10) hex.append("0");
            hex.append(Integer.toHexString((b and 0xFF.toByte()).toInt()))
        }
        return hex.toString()
    }
}
