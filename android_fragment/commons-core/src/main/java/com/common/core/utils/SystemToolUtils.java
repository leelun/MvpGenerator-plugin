package com.common.core.utils;

import android.net.Uri;
import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * @author liulun
 * @version V1.0
 * @Description: 系统工具
 * @date 2016/12/2 10:05
 */
public class SystemToolUtils {
    public static boolean isPhoneNumber(String str) {
        if (TextUtils.isEmpty(str)) return false;
        if (str.length() != 11) return false;
        return true;
    }

    public static boolean isChinese(String str) {
        return Pattern.matches("^[\u4E00-\u9FA5]+$", str);
    }

    public static boolean isDouble(String str) {
        try {
            Double.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static float getFloat(String str) {
        try {
            return Float.valueOf(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getOneFloat(float num) {
        return (float) ((int) (num * 10)) / 10;
    }

    public static <T> T getValue(Class<T> clazz, String value) {
        if (clazz == String.class) {
            return (T) String.valueOf(value);
        } else if (clazz == Byte.class || clazz == byte.class) {
            return (T) Byte.valueOf(value);
        } else if (clazz == Short.class || clazz == short.class) {
            return (T) Short.valueOf(value);
        } else if (clazz == Integer.class || clazz == int.class) {
            return (T) Integer.valueOf(value);
        } else if (clazz == Long.class || clazz == long.class) {
            return (T) Long.valueOf(value);
        } else if (clazz == Float.class || clazz == float.class) {
            return (T) Float.valueOf(value);
        } else if (clazz == Double.class || clazz == double.class) {
            return (T) Double.valueOf(value);
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return (T) Boolean.valueOf(value);
        } else {
            return (T) value;
        }
    }

    /**
     * 是否金额
     *
     * @return
     */
    public static boolean isDecimalMoney(String str) {
        return Pattern.matches("^\\d+(\\.\\d{1,2})?$", str);
    }

    public static boolean isInteger(String str) {
        return Pattern.matches("^\\d+$", str);
    }

    public static boolean isEqual(String text1, String text2) {
        if (text1 == null) {
            return text2 == null;
        } else {
            return text1.equals(text2);
        }
    }

    public static boolean isHttp(String str) {
        if (str == null) {
            return false;
        } else {
            return str.startsWith("http");
        }
    }

    public static boolean isFileUri(String str) {
        if (str == null) {
            return false;
        } else {
            return str.startsWith("file://");
        }
    }

    public static String getFilePathFromFileUri(String uriStr) {
        if (uriStr == null) {
            return null;
        } else {
            return uriStr.replaceFirst("file://", "");
        }
    }
}
