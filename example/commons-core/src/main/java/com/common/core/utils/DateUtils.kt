package com.common.core.utils

import android.annotation.SuppressLint
import android.os.SystemClock
import com.common.core.utils.DateUtils.getInternetTime

import java.net.URL
import java.net.URLConnection
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 时间工具类
 */
@SuppressLint("SimpleDateFormat")
object DateUtils {
    private val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val simpleFormat2 = SimpleDateFormat("yyyy-MM-dd HH:mm")
    private val MMddSimpleFormat = SimpleDateFormat("MM月dd日")
    private var internetTime: Long = 0
    private var uptimeMillis: Long = 0

    /**
     * 获取网络时间
     *
     * @return
     */
    val netWorkDate: Date
        get() = getNetWorkDate(false)

    /**
     * 获取网络事件
     *
     * @return
     */
    val netWorkCalendar: Calendar
        get() {
            val date = DateUtils.netWorkDate
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }

    /**
     * 时间保持和服务器时间同步
     *
     * @param dateStr
     */
    fun setHeaderDate(dateStr: String) {
        val greenwichDate = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US)
        val cal = Calendar.getInstance()
        // 时区设为格林尼治
        greenwichDate.timeZone = TimeZone.getTimeZone("GMT")
        try {
            val date = greenwichDate.parse(dateStr)
            setInternetTime(date.time)
            CmLog.e("DateUtils", formatDate(date))
        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    fun setInternetTime(internetTime: Long) {
        DateUtils.internetTime = internetTime
        DateUtils.uptimeMillis = SystemClock.uptimeMillis()
    }

    /**
     * 取网络时间
     *
     * @return
     */
    fun getInternetTime(): Long {
        if (internetTime <= 0) {
            var time = System.currentTimeMillis()
            try {
                val url = URL("http://www.baidu.com")
                val uc = url.openConnection()
                uc.connect()
                time = uc.date
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return time
        } else {
            return internetTime + (SystemClock.uptimeMillis() - uptimeMillis)
        }
    }

    /**
     * @param isMust 是否必须获取
     * @return 事件
     * @Description: 获取网络时间
     */
    fun getNetWorkDate(isMust: Boolean): Date {
        return Date(getInternetTime())
    }

    fun formatYMD(timestamp: Long): String {
        val time = timestamp * 1000
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    fun formatTime(millisecond: Long): String {
        val date = Date(millisecond)
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

    fun formatMD(date: Date): String {
        return MMddSimpleFormat.format(date)
    }

    fun formatHHMM(timestamp: Long): String {
        val time = timestamp * 1000
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm")
        return format.format(date)
    }

    fun formatDate(date: Date): String {
        return simpleFormat.format(date)
    }

    fun parseDate(dateStr: String): Date? {
        try {
            return simpleFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

    fun parseDate2(dateStr: String): Date? {
        try {
            return simpleFormat2.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
            return null
        }

    }

    fun formatDate(timemillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timemillis
        return simpleFormat.format(calendar.time)
    }

    fun formatNow(): String {
        return formatDate(Date())
    }

    fun getYYYYMM(calendar: Calendar): String {
        val simpleFormat = SimpleDateFormat("yyyyMM")
        return simpleFormat.format(calendar.time)
    }

    fun getYyyyMM(date: Date): String {
        val yyyyMMSimpleFormat = SimpleDateFormat("yyyy月MM月")
        return yyyyMMSimpleFormat.format(date)
    }

    /**
     * 返回时间
     *
     * @param datetime
     */
    fun getCalendar(datetime: String): Calendar? {
        try {
            val date = simpleFormat.parse(datetime)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        } catch (e: Exception) {
            return null
        }

    }

    /**
     * 获取天数
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    fun getDayNumber(year: Int, month: Int): Int {
        val dayNumber: Int
        when (month + 1) {
            4, 6, 9, 11 -> dayNumber = 30
            2 -> if (year % 100 == 0 && year % 400 == 0 || year % 100 != 0 && year % 4 == 0) {
                dayNumber = 29
            } else {
                dayNumber = 28
            }
            else -> dayNumber = 31
        }
        return dayNumber
    }

    fun getYearNumber(year: Int): Int {
        return if (year % 100 == 0 && year % 400 == 0 || year % 100 != 0 && year % 4 == 0) {
            366
        } else {
            365
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
    fun after(year: Int, month: Int, day: Int, year2: Int, month2: Int, day2: Int): Int {
        if (year < year2) return -1
        if (year == year2) {
            if (month < month2) return -1
            if (month == month2) {
                return if (day < day2) -1 else day - day2
            } else {
                var number = getDayNumber(year2, month2) - day2
                for (i in month2 + 1 until month) {
                    number += getDayNumber(year, i)
                }
                number += day
                return number
            }
        } else {
            var number = getDayNumber(year2, month2) - day2
            for (m in month2 + 1..11) {
                number += getDayNumber(year2, m)
            }
            for (y in year2 + 1 until year) {
                number += getYearNumber(y)
            }
            for (m in 0 until month) {
                number += getDayNumber(year, m)
            }
            number += day
            return number
        }
    }

    fun afterDay(calendar: Calendar, calendar2: Calendar): Boolean {
        val year = calendar.get(Calendar.YEAR)
        val day = calendar.get(Calendar.DAY_OF_YEAR)
        val year2 = calendar2.get(Calendar.YEAR)
        val day2 = calendar2.get(Calendar.DAY_OF_YEAR)
        return if (year > year2 || year == year2 && day > day2) true else false
    }
}
