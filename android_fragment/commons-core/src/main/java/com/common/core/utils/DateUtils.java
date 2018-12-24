package com.common.core.utils;

import android.annotation.SuppressLint;
import android.os.SystemClock;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
    private static SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat simpleFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat MMddSimpleFormat = new SimpleDateFormat("MM月dd日");
    private static long internetTime;
    private static long uptimeMillis;

    /**
     * 时间保持和服务器时间同步
     *
     * @param dateStr
     */
    public static void setHeaderDate(String dateStr) {
        SimpleDateFormat greenwichDate = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        Calendar cal = Calendar.getInstance();
        // 时区设为格林尼治
        greenwichDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date date = greenwichDate.parse(dateStr);
            setInternetTime(date.getTime());
            CmLog.e("DateUtils", formatDate(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setInternetTime(long internetTime) {
        DateUtils.internetTime = internetTime;
        DateUtils.uptimeMillis = SystemClock.uptimeMillis();
    }

    /**
     * 取网络时间
     *
     * @return
     */
    public static long getInternetTime() {
        if (internetTime <= 0) {
            long time = System.currentTimeMillis();
            try {
                URL url = new URL("http://www.baidu.com");
                URLConnection uc = url.openConnection();
                uc.connect();
                time = uc.getDate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return time;
        } else {
            return internetTime + (SystemClock.uptimeMillis() - uptimeMillis);
        }
    }

    /**
     * 获取网络时间
     *
     * @return
     */
    public static Date getNetWorkDate() {
        return getNetWorkDate(false);
    }

    /**
     * @param isMust 是否必须获取
     * @return 事件
     * @Description: 获取网络时间
     */
    public static Date getNetWorkDate(boolean isMust) {
        Date date = new Date(getInternetTime());
        return date;
    }

    /**
     * 获取网络事件
     *
     * @return
     */
    public static Calendar getNetWorkCalendar() {
        Date date = DateUtils.getNetWorkDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static String formatYMD(long timestamp) {
        long time = timestamp * 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String formatTime(long millisecond) {
        Date date = new Date(millisecond);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    public static String formatMD(Date date) {
        return MMddSimpleFormat.format(date);
    }

    public static String formatHHMM(long timestamp) {
        long time = timestamp * 1000;
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String formatDate(Date date) {
        return simpleFormat.format(date);
    }

    public static Date parseDate(String dateStr) {
        try {
            return simpleFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseDate2(String dateStr) {
        try {
            return simpleFormat2.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String formatDate(long timemillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timemillis);
        return simpleFormat.format(calendar.getTime());
    }

    public static String formatNow() {
        return formatDate(new Date());
    }

    public static String getYYYYMM(Calendar calendar) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMM");
        return simpleFormat.format(calendar.getTime());
    }

    public static String getYyyyMM(Date date) {
        SimpleDateFormat yyyyMMSimpleFormat = new SimpleDateFormat("yyyy月MM月");
        return yyyyMMSimpleFormat.format(date);
    }

    /**
     * 返回时间
     *
     * @param datetime
     */
    public static Calendar getCalendar(String datetime) {
        try {
            Date date = simpleFormat.parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取天数
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getDayNumber(int year, int month) {
        int dayNumber;
        switch (month + 1) {
            case 4:
            case 6:
            case 9:
            case 11:
                dayNumber = 30;
                break;
            case 2:
                if ((year % 100 == 0 && year % 400 == 0) || year % 100 != 0 && year % 4 == 0) {
                    dayNumber = 29;
                } else {
                    dayNumber = 28;
                }
                break;
            default:
                dayNumber = 31;
                break;
        }
        return dayNumber;
    }

    public static int getYearNumber(int year) {
        if ((year % 100 == 0 && year % 400 == 0) || year % 100 != 0 && year % 4 == 0) {
            return 366;
        } else {
            return 365;
        }
    }

    /**
     * 时间大于时间2多少天
     *
     * @param year
     * @param month
     * @param day
     * @param year2
     * @param month2
     * @param day2
     * @return
     */
    public static int after(int year, int month, int day, int year2, int month2, int day2) {
        if (year < year2) return -1;
        if (year == year2) {
            if (month < month2) return -1;
            if (month == month2) {
                if (day < day2) return -1;
                return day - day2;
            } else {
                int number = getDayNumber(year2, month2) - day2;
                for (int i = month2 + 1; i < month; i++) {
                    number += getDayNumber(year, i);
                }
                number += day;
                return number;
            }
        } else {
            int number = getDayNumber(year2, month2) - day2;
            for (int m = month2 + 1; m < 12; m++) {
                number += getDayNumber(year2, m);
            }
            for (int y = year2 + 1; y < year; y++) {
                number += getYearNumber(y);
            }
            for (int m = 0; m < month; m++) {
                number += getDayNumber(year, m);
            }
            number += day;
            return number;
        }
    }

    public static boolean afterDay(Calendar calendar, Calendar calendar2) {
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        if (year > year2 || (year == year2 && day > day2)) return true;
        return false;
    }
}
