package com.common.core.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * @author liulun
 * @version V1.0
 * @Description: 字符串验证
 * @date 2016/12/8 10:56
 */
public class StringUtils {
    /**
     * @Description: 判断是否是null 如果非空则返回null 否则返回提示
     * @version 1.0
     * @time 2016/12/8 10:59
     * @author leellun
     */
    public static String isEmpty(String[][] strs) {
        for (int i = 0; i < strs.length; i++) {
            if (TextUtils.isEmpty(strs[i][0])) {
                return strs[i][1];
            }
        }
        return null;
    }

    public static String formatMoney(double money) {
        return String.format("%.2f", money);
    }
    public static String formatNumber(double number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(number);
    }

    public static String formatNumber(double number, int precision) {
        return String.format("%." + precision + "f", number);
    }

    public static String getDefaultText(String str) {
        if (str == null) return "";
        return str;
    }

    public static String getDefaultText(String str, String defualtStr) {
        if (str == null || str.equals("")) return defualtStr;
        return str;
    }

    /**
     * md5
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}
