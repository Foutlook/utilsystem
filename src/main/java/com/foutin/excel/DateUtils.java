package com.foutin.excel;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @description: 时间日期类
 * @author: z0803
 * @create: 2021/11/05
 **/
public final class DateUtils {

    public static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    private DateUtils() {
        // do nothing
    }

    public static String getDateYmdStr(Long timeStamp) {
        if (null == timeStamp) {
            return null;
        }
        LocalDateTime localDateTime = asLocalDateTime(timeStamp);
        return localDateTime.format(DateTimeFormatter.ofPattern(FORMAT_YMD_HMS));
    }

    /**
     * 毫秒时间戳转为日期
     *
     * @param timestamp 毫秒时间戳
     * @return 日期
     */
    public static Date timestampToDate(long timestamp) {
        return asDate(asLocalDateTime(timestamp));
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String timestampToString(Long timestamp) {
        return getDateYmdStr(timestamp);
    }

    public static LocalDate asLocalDate(long days) {
        return Instant.ofEpochMilli(days * 24 * 60 * 60 * 1000).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    public static String formatDateToString(Date time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static Date formatStrToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_YMD_HMS);
        try {
            return sdf.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("格式化日期失败", e);
        }
    }

}
