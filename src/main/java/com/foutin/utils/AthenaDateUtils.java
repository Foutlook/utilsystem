package com.foutin.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.time.FastTimeZone;

import java.text.ParseException;
import java.time.Month;
import java.util.*;

/**
 * @ClassName AthenaDateUtils
 * @Description 日期辅助类
 * @Author 王培
 * @Date 2019-08-05 10:52
 **/
public class AthenaDateUtils {

    public static final FastDateFormat FULL_DATE_TIME_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");

    public static final FastDateFormat FULL_DATE_TIME_FORMAT_WITHOUT_SEC = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public static final FastDateFormat FULL_DATE_TIME_FORMAT_WITH_MSEC = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    public static final FastDateFormat FULL_DATE = FastDateFormat.getInstance("yyyy-MM-dd");

    public static final FastDateFormat FULL_MONTH = FastDateFormat.getInstance("yyyy-MM");

    public static final FastDateFormat SHORT_DATE_FORMAT = FastDateFormat.getInstance("yyyyMMdd");

    public static final FastDateFormat FULL_DATE_WITH_HOUR_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");

    public static final FastDateFormat FULL_TIME_FORMAT = FastDateFormat.getInstance("HH:mm:ss");

    public static final TimeZone UTC_TIME_ZONE = FastTimeZone.getGmtTimeZone();

    /** 星座分隔时间日 */
    private static final int[] DAY_ARR = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };

    /** 星座 */
    private static final String[] ZODIACS = new String[] { "摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座" };

    private static final String[] CHINESE_ZODIACS = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪" };


    /**
     * 解析日期，如果str不是规范的日期格式，例如2019-01-35 会解析城2019-02-04，不会跑出异常
     *
     * @param str 日期字符串
     * @param parsePatterns 解析格式
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseDate(final String str, final String parsePatterns) throws ParseException {
        return DateUtils.parseDate(str, null, new String[]{parsePatterns});
    }

    /**
     * 解析日期，如果str不是规范的日期格式，例如2019-01-35 会解析城2019-02-04，不会跑出异常
     *
     * @param str 日期字符串
     * @param parsePatterns 解析格式
     * @param locale 本地化
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseDate(final String str, final Locale locale, final String parsePatterns) throws ParseException {
        return DateUtils.parseDate(str, locale, parsePatterns);
    }

    /**
     * 解析日期，如果str不是规范的日期格式，例如2019-01-35 会抛出异常
     *
     * @param str 日期字符串
     * @param parsePatterns 解析格式
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseDateStrictly(final String str, final String parsePatterns) throws ParseException {
        return DateUtils.parseDateStrictly(str, null, new String[]{parsePatterns});
    }

    /**
     * 解析日期，如果str不是规范的日期格式，例如2019-01-35 会抛出异常
     *
     * @param str 日期字符串
     * @param parsePatterns 解析格式
     * @param locale 本地化
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseDateStrictly(final String str, final Locale locale, final String parsePatterns) throws ParseException {
        return DateUtils.parseDateStrictly(str, locale, parsePatterns);
    }

    /**
     * 解析日期,"yyyy-MM-dd"格式
     *
     * @param str 日期字符串
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseDate(final String str) throws ParseException {
        return DateUtils.parseDate(str, null, new String[]{FULL_DATE.getPattern()});
    }

    /**
     * 解析日期,"yyyy-MM-dd HH:mm:ss"格式
     *
     * @param str 日期字符串
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseFullDateTime(final String str) throws ParseException {
        return DateUtils.parseDate(str, null, new String[]{FULL_DATE_TIME_FORMAT.getPattern()});
    }

    /**
     * 解析日期,"yyyy-MM-dd HH:mm"格式
     *
     * @param str 日期字符串
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseFullDateTimeWithoutSec(final String str) throws ParseException {
        return DateUtils.parseDate(str, null, new String[]{FULL_DATE_TIME_FORMAT_WITHOUT_SEC.getPattern()});
    }

    /**
     * 解析日期,"yyyy-MM-dd HH:mm:ss.SSS"格式
     *
     * @param str 日期字符串
     *
     * @return 按照格式解析出的字符串
     *
     * @throws ParseException
     */
    public static Date parseFullDateTimeWithMsec(final String str) throws ParseException {
        return DateUtils.parseDate(str, null, new String[]{FULL_DATE_TIME_FORMAT_WITH_MSEC.getPattern()});
    }


    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final long millis, final String pattern) {
        return DateFormatUtils.format(new Date(millis), pattern, UTC_TIME_ZONE, null);
    }

    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final Date date, final String pattern) {
        return DateFormatUtils.format(date, pattern, UTC_TIME_ZONE, null);
    }

    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final Calendar calendar, final String pattern) {
        return DateFormatUtils.format(calendar, pattern, UTC_TIME_ZONE, null);
    }



    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final long millis, final String pattern,final Locale locale) {
        return DateFormatUtils.format(new Date(millis), pattern, UTC_TIME_ZONE, locale);
    }

    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final Date date, final String pattern,final Locale locale) {
        return DateFormatUtils.format(date, pattern, UTC_TIME_ZONE, locale);
    }

    /**
     * GMT TimeZone 获取格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String formatUTC(final Calendar calendar, final String pattern,final Locale locale) {
        return DateFormatUtils.format(calendar, pattern, UTC_TIME_ZONE, locale);
    }

    /**
     * 格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String format(final long millis, final String pattern) {
        return DateFormatUtils.format(new Date(millis), pattern, null, null);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String format(final Date date, final String pattern) {
        return DateFormatUtils.format(date, pattern, null, null);
    }

    /**
     * 格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     *
     * @return 格式化后的日期
     */
    public static String format(final Calendar calendar, final String pattern) {
        return DateFormatUtils.format(calendar, pattern, null, null);
    }

    /**
     * 格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     * @param timeZone 时区
     *
     * @return 格式化后的日期
     */
    public static String format(final long millis, final String pattern, final TimeZone timeZone) {
        return DateFormatUtils.format(new Date(millis), pattern, timeZone, null);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @param timeZone 时区
     *
     * @return 格式化后的日期
     */
    public static String format(final Date date, final String pattern, final TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone, null);
    }

    /**
     * 格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     * @param timeZone 时区
     *
     * @return 格式化后的日期
     */
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone) {
        return DateFormatUtils.format(calendar, pattern, timeZone, null);
    }

    /**
     * 格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final long millis, final String pattern, final Locale locale) {
        return DateFormatUtils.format(new Date(millis), pattern, null, locale);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final Date date, final String pattern, final Locale locale) {
        return DateFormatUtils.format(date, pattern, null, locale);
    }

    /**
     * 格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final Calendar calendar, final String pattern, final Locale locale) {
        return DateFormatUtils.format(calendar, pattern, null, locale);
    }

    /**
     * 格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     * @param timeZone 时区
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final long millis, final String pattern, final TimeZone timeZone, final Locale locale) {
        return DateFormatUtils.format(new Date(millis), pattern, timeZone, locale);
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @param timeZone 时区
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final Date date, final String pattern, final TimeZone timeZone, final Locale locale) {
        return DateFormatUtils.format(date, pattern, timeZone, locale);
    }

    /**
     * 格式化日期
     *
     * @param calendar 日历
     * @param pattern 格式
     * @param timeZone 时区
     * @param locale 本地化
     *
     * @return 格式化后的日期
     */
    public static String format(final Calendar calendar, final String pattern, final TimeZone timeZone, final Locale locale) {
        return DateFormatUtils.format(calendar, pattern, timeZone, locale);
    }

    /**
     * 2个时间是不是同一天
     *
     * @param date1 时间1
     * @param date2 时间2
     *
     * @return 是否同一天
     */
    public static boolean isSameDay(final Date date1, final Date date2){
        return DateUtils.isSameDay(date1, date2);
    }


    /**
     * 2个时间是不是同一天
     *
     * @param cal1 时间1
     * @param cal2 时间2
     *
     * @return 是否同一天
     */
    public static boolean isSameDay(final Calendar cal1, final Calendar cal2){
        return DateUtils.isSameDay(cal1, cal2);
    }

    /**
     * 2个时间是不是同一天
     *
     * @param cal1 时间1
     * @param cal2 时间2
     *
     * @return 是否同一天
     */
    public static boolean isSameDay(final String cal1, final String cal2)throws ParseException{
        return DateUtils.isSameDay(parseDate(cal1), parseDate((cal2)));
    }

    /**
     * 2个时间是不是一样，比较毫秒数
     *
     * @param date1 时间1
     * @param date2 时间2
     *
     * @return 是否同一天
     */
    public static boolean isSameTime(final Date date1, final Date date2){
        return DateUtils.isSameInstant(date1, date2);
    }


    /**
     * 2个时间是不是一样，比较毫秒数
     *
     * @param cal1 时间1
     * @param cal2 时间2
     *
     * @return 是否同一天
     */
    public static boolean isSameInstant(final Calendar cal1, final Calendar cal2){
        return DateUtils.isSameInstant(cal1, cal2);
    }

    /**
     * 增加年数
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addYears(final Date date, final int amount) {
        return DateUtils.addYears(date, amount);
    }

    /**
     * 增加月数
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addMonths(final Date date, final int amount) {
        return DateUtils.addMonths(date,  amount);
    }

    /**
     * 增加周
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addWeeks(final Date date, final int amount) {
        return DateUtils.addWeeks(date, amount);
    }

    /**
     * 增加日期
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addDays(final Date date, final int amount) {
        return DateUtils.addDays(date, amount);
    }


    /**
     * 增加小时
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addHours(final Date date, final int amount) {
        return DateUtils.addHours(date , amount);
    }

    /**
     * 增加分钟
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addMinutes(final Date date, final int amount) {
        return DateUtils.addMinutes(date , amount);
    }

    /**
     * 增加秒
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addSeconds(final Date date, final int amount) {
        return DateUtils.addSeconds(date , amount);
    }

    /**
     * 增加毫秒
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date addMilliseconds(final Date date, final int amount) {
        return DateUtils.addMilliseconds(date, amount);
    }

    /**
     * 设置年数
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setYears(final Date date, final int amount) {
        return DateUtils.setYears(date, amount);
    }

    /**
     * 设置月数
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setMonths(final Date date, final int amount) {
        return DateUtils.setMonths(date,  amount);
    }

    /**
     * 设置日期
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setDays(final Date date, final int amount) {
        return DateUtils.setDays(date, amount);
    }


    /**
     * 设置小时
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setHours(final Date date, final int amount) {
        return DateUtils.setHours(date , amount);
    }

    /**
     * 设置分钟
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setMinutes(final Date date, final int amount) {
        return DateUtils.setMinutes(date , amount);
    }

    /**
     * 设置秒
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的日期
     */
    public static Date setSeconds(final Date date, final int amount) {
        return DateUtils.setSeconds(date , amount);
    }

    /**
     * 设置毫秒
     *
     * @param date 当前时间
     *
     * @param amount 数量
     *
     * @return 返回新的时间
     */
    public static Date setMilliseconds(final Date date, final int amount) {
        return DateUtils.setMilliseconds(date, amount);
    }

    /**
     * 日期转换成日历格式
     *
     * @param date 时间
     *
     * @return 日历
     */
    public static Calendar toCalendar(final Date date) {
        return DateUtils.toCalendar(date);
    }

    /**
     * 日期转换成日历格式
     *
     * @param date 时间
     * @param tz 时区
     *
     * @return 日历
     */
    public static Calendar toCalendar(final Date date, final TimeZone tz) {
        return DateUtils.toCalendar(date, tz);
    }

    /**
     * 将指定的日期指定的部分四舍五入
     *
     * @param date 时间
     * @param field 时间字段 Calendar.YEAR.......
     *
     * @return 日历
     */
    public static Date round(final Object date, final int field){
        return DateUtils.round(date, field);
    }

    /**
     * 将指定的日期按照指定的部分截取
     *
     * @param date 时间
     * @param field 时间字段 Calendar.YEAR.......
     *
     * @return 日历
     */
    public static Date truncate(final Object date, final int field) {
        return DateUtils.truncate(date, field);
    }

    /**
     * 将指定的日期按照指定的部分向上舍入
     *
     * @param date 时间 Calendar或Date类型
     * @param field 时间字段 Calendar.YEAR.......
     *
     * @return 日历
     */
    public static Date ceiling(final Object date, final int field) {
        return DateUtils.ceiling(date, field);
    }

    /**
      * 根据指定的时间focus和范围类型rangeStyle构建一个时间范围迭代器 。
      *
      * 如传入的时间是Thursday, July 4, 2002，范围类型是RANGE_MONTH_SUNDAY，
      * 则返回迭代器的范围是从Sunday, June 30, 2002 到 Saturday, August 3, 2002
      *
      * @param focus  指定的时间
      * @param rangeStyle  范围类型，值必须是如下之一：
      * DateUtils.RANGE_MONTH_SUNDAY， 
      * DateUtils.RANGE_MONTH_MONDAY，
      * DateUtils.RANGE_WEEK_SUNDAY，
      * DateUtils.RANGE_WEEK_MONDAY，
      * DateUtils.RANGE_WEEK_RELATIVE，
      * DateUtils.RANGE_WEEK_CENTER

      * @return 时间的迭代器
      */
    public static Iterator<Calendar> iterator(final Date focus, final int rangeStyle){
        return DateUtils.iterator(focus, rangeStyle);
    }

    /**
      * 根据指定的时间focus和范围类型rangeStyle构建一个时间范围迭代器 。
      *
      * 如传入的时间是Thursday, July 4, 2002，范围类型是RANGE_MONTH_SUNDAY，
      * 则返回迭代器的范围是从Sunday, June 30, 2002 到 Saturday, August 3, 2002
      *
      * @param focus  指定的时间
      * @param rangeStyle  范围类型，值必须是如下之一：
      * DateUtils.RANGE_MONTH_SUNDAY， 
      * DateUtils.RANGE_MONTH_MONDAY，
      * DateUtils.RANGE_WEEK_SUNDAY，
      * DateUtils.RANGE_WEEK_MONDAY，
      * DateUtils.RANGE_WEEK_RELATIVE，
      * DateUtils.RANGE_WEEK_CENTER
      */
    public static Iterator<Calendar> iterator(final Calendar focus, final int rangeStyle){
        return DateUtils.iterator(focus, rangeStyle);
    }


    /**
      * 返回指定分段内的毫秒数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期毫秒，将返回当前秒的毫秒数 (返回一个数字在0和999之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于MILLISECOND，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
      *  January 6, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
      *  January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10538
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      *   (a millisecond cannot be split in milliseconds)
      * 
      * @param date 获取值得时间对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInMilliseconds(final Date date, final int fragment) {
        return DateUtils.getFragmentInMilliseconds(date, fragment);
    }

    /**
      * 返回指定分段内的秒数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期秒，将返回当前的分钟的秒数 (返回一个数字在0和59之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于SECOND，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10
      *  January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10
      *  January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 26110
      *   (7*3600 + 15*60 + 10)</li>
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param date 获取值得时间对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInSeconds(final Date date, final int fragment) {
        return DateUtils.getFragmentInSeconds(date, fragment);
    }


    /**
      * 返回指定分段内的分钟数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期分钟，将返回当前的小时的分钟数 (返回一个数字在0和59之间)
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于MINUTE，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
      *  January 6, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
      *  January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 15
      *  January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 435 (7*60 + 15)
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param date 获取值得时间对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInMinutes(final Date date, final int fragment) {
        return DateUtils.getFragmentInMinutes(date, fragment);
    }

    /**
      * 返回指定分段内的小时数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期小时，将返回当前的天的小时数 (返回一个数字在0和23之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于HOUR_OF_DAY，将返回0 。
      *  
      *  January 1, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
      *  January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
      *  January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 7
      *  January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 127 (5*24 + 7)
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      *  
      * @param date 获取值得时间对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInHours(final Date date, final int fragment) {
        return DateUtils.getFragmentInHours(date, fragment);
    }

    /**
      * 返回指定分段内的天数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期天数，将返回当前的月的天数 (返回一个数字在1和31之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于DATE，将返回0 。
      * 
      *  January 28, 2008 with Calendar.MONTH as fragment will return 28
      *  February 28, 2008 with Calendar.MONTH as fragment will return 28
      *  January 28, 2008 with Calendar.YEAR as fragment will return 28
      *  February 28, 2008 with Calendar.YEAR as fragment will return 59
      *  January 28, 2008 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param date 获取值得时间对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInDays(final Date date, final int fragment) {
        return DateUtils.getFragmentInDays(date, fragment);
    }

    /**
      * 返回指定分段内的毫秒数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期毫秒，将返回当前秒的毫秒数 (返回一个数字在0和999之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于MILLISECOND，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
      *  January 6, 2008 7:15:10.538 with Calendar.SECOND as fragment will return 538
      *  January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10538
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      *   (a millisecond cannot be split in milliseconds)
      * 
      * @param calendar 获取值得日历对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInMilliseconds(final Calendar calendar, final int fragment) {
        return DateUtils.getFragmentInMilliseconds(calendar, fragment);
    }

    /**
      * 返回指定分段内的秒数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期秒，将返回当前的分钟的秒数 (返回一个数字在0和59之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于SECOND，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10
      *  January 6, 2008 7:15:10.538 with Calendar.MINUTE as fragment will return 10
      *  January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 26110
      *   (7*3600 + 15*60 + 10)</li>
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param calendar 获取值得日历对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInSeconds(final Calendar calendar, final int fragment) {
        return DateUtils.getFragmentInSeconds(calendar, fragment);
    }


    /**
      * 返回指定分段内的分钟数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期分钟，将返回当前的小时的分钟数 (返回一个数字在0和59之间)
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于MINUTE，将返回0 。
      * 
      *  January 1, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
      *  January 6, 2008 7:15:10.538 with Calendar.HOUR_OF_DAY as fragment will return 15
      *  January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 15
      *  January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 435 (7*60 + 15)
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param calendar 获取值得日历对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInMinutes(final Calendar calendar, final int fragment) {
        return DateUtils.getFragmentInMinutes(calendar, fragment);
    }

    /**
      * 返回指定分段内的小时数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期小时，将返回当前的天的小时数 (返回一个数字在0和23之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于HOUR_OF_DAY，将返回0 。
      *  
      *  January 1, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
      *  January 6, 2008 7:15:10.538 with Calendar.DAY_OF_YEAR as fragment will return 7
      *  January 1, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 7
      *  January 6, 2008 7:15:10.538 with Calendar.MONTH as fragment will return 127 (5*24 + 7)
      *  January 16, 2008 7:15:10.538 with Calendar.MILLISECOND as fragment will return 0
      *  
      * @param calendar 获取值得日历对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInHours(final Calendar calendar, final int fragment) {
        return DateUtils.getFragmentInHours(calendar, fragment);
    }

    /**
      * 返回指定分段内的天数 。 所有大于分段的DateFields将被忽略 。
      *
      * 请求任何日期天数，将返回当前的月的天数 (返回一个数字在1和31之间) 。
      * 有效的分段值是： Calendar.YEAR、Calendar.MONTH、Calendar.DAY_OF_YEAR、
      * Calendar.DATE、Calendar.HOUR_OF_DAY、Calendar.MINUTE、
      * Calendar.SECOND 和 Calendar.MILLISECOND
      * 分段值小于或等于DATE，将返回0 。
      * 
      *  January 28, 2008 with Calendar.MONTH as fragment will return 28
      *  February 28, 2008 with Calendar.MONTH as fragment will return 28
      *  January 28, 2008 with Calendar.YEAR as fragment will return 28
      *  February 28, 2008 with Calendar.YEAR as fragment will return 59
      *  January 28, 2008 with Calendar.MILLISECOND as fragment will return 0
      * 
      * @param calendar 获取值得日历对象，非null
      * @param fragment 分段值
      */
    public static long getFragmentInDays(final Calendar calendar, final int fragment) {
        return DateUtils.getFragmentInDays(calendar, fragment);
    }

    /**
      * 截取比较两个日历对象的field处的值是否相同 。
      * 
      * @param cal1 第一个日历对象，非null
      * @param cal2 第二个日历对象，非null
      * @param field Calendar中的阈值
      */
    public static boolean truncatedEquals(final Calendar cal1, final Calendar cal2, final int field) {
        return DateUtils.truncatedEquals(cal1, cal2, field);
    }

    /**
      * 截取比较两个日历对象的field处的值是否相同 。
      * 
      * @param date1 第一个日期对象，非null
      * @param date2 第二个日期对象，非null
      * @param field Calendar中的阈值
      */
    public static boolean truncatedEquals(final Date date1, final Date date2, final int field) {
        return DateUtils.truncatedEquals(date1, date2, field);
    }

    /**
      * 截取比较两个日历对象的field处的值 。
      * 如果第一个日历小于、等于、大于第二个，则对应返回负整数、0、正整数
      * 
      * @param cal1 第一个日历对象，非null
      * @param cal2 第二个日历对象，非null
      * @param field Calendar中的阈值
      */
    public static int truncatedCompareTo(final Calendar cal1, final Calendar cal2, final int field){
        return DateUtils.truncatedCompareTo(cal1, cal2, field);
    }

    /**
      * 截取比较断两个日期对象的field处的值 。
      * 如果第一个日期小于、等于、大于第二个，则对应返回负整数、0、正整数
      * 
      * @param date1 第一个日期对象，非null
      * @param date2 第二个日期对象，非null
      * @param field Calendar中的阈值
      */
    public static int truncatedCompareTo(final Date date1, final Date date2, final int field){
        return DateUtils.truncatedCompareTo(date1, date2, field);
    }

    /**
     * 获取字符串类型的当前日期
     *
     * @return 当前日期
     *
     */
    public static String getDate() {
        return FULL_DATE.format(new Date());
    }

    /**
     * 根据传入的日期格式化成yyyy-MM-dd日期
     *
     * @param date 传入的日期
     *
     * @return 格式化后的日期
     */
    public static String getDate(final Date date) {
        return FULL_DATE.format(date);
    }


    /**
     * 根据传入的日期格式化成yyyy-MM日期
     *
     * @param date 传入的日期
     *
     * @return 格式化后的日期
     */
    public static String getMonthDate(final Date date) {
        return FULL_MONTH.format(date);
    }

    /**
     * 根据传入的时间格式化成yyyy-MM-dd HH:mm:ss日期
     *
     * @param date 传入的日期
     *
     * @return 格式化后的日期
     */
    public static String getDateTime(final Date date) {
        return FULL_DATE_TIME_FORMAT.format(date);
    }

    /**
     * 根据传入的时间格式化成yyyy-MM-dd HH:mm日期
     *
     * @param date 传入的日期
     *
     * @return 格式化后的日期
     */
    public static String getDateTimeWithoutSec(final Date date) {
        return FULL_DATE_TIME_FORMAT_WITHOUT_SEC.format(date);
    }

    /**
     * 根据传入的日历格式化成yyyy-MM-dd日期
     *
     * @param calendar 传入的日历
     *
     * @return 格式化后的日期
     */
    public static String getDate(final Calendar calendar) {
        return FULL_DATE.format(calendar.getTime());
    }

    /**
     * 根据传入的日历格式化成yyyy-MM-dd HH:mm:ss日期
     *
     * @param calendar 传入的日历
     *
     * @return 格式化后的日期
     */
    public static String getDateTime(final Calendar calendar) {
        return FULL_DATE_TIME_FORMAT.format(calendar.getTime());
    }

    /**
     * 根据传入的日历格式化成yyyy-MM-dd HH:mm日期
     *
     * @param calendar 传入的日历
     *
     * @return 格式化后的日期
     */
    public static String getDateTimeWithoutSec(final Calendar calendar) {
        return FULL_DATE_TIME_FORMAT_WITHOUT_SEC.format(calendar.getTime());
    }

    /**
     * 根据传入的日期格式化成yyyyMMdd的日期
     *
     * @param date 传入的日期
     *
     * @return 格式化后的日期
     */
    public static String getShortDate(final Date date) {
        return SHORT_DATE_FORMAT.format(date);
    }


    /**
     * 获取年
     * @param date 日期
     *
     * @return 返回年
     */
    public static int getYear(final Date date) {
        return getDateFieldValue(date,Calendar.YEAR);
    }

    /**
     * 获取季度
     * @param date 日期
     *
     * @return 返回年
     */
    public static int getQuarter(final Date date) {
        return getMonth(date)/3+1;
    }


    /**
     * 获取月
     * @param date 日期
     *
     * @return 返回年
     */
    public static int getMonth(final Date date) {
        return getDateFieldValue(date,Calendar.MONTH);
    }

    /**
     * 获取日
     * @param date 日期
     *
     * @return 返回年
     */
    public static int getDay(final Date date) {
        return getDateFieldValue(date,Calendar.MONTH);
    }

    /**
     * 获得指定日期是所在年份的第几周
     *
     * @param date 日期
     *
     * @return 周
     */
    public static int weekOfYear(Date date) {
        return getDateFieldValue(date,Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获得指定日期是所在月份的第几周
     *
     * @param date 日期
     *
     * @return 周
     */
    public static int weekOfMonth(Date date) {
        return  getDateFieldValue(date,Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获得指定日期是所在年的第几天
     *
     * @param date 日期
     *
     * @return 周
     */
    public static int dayOfYear(Date date) {
        return  getDateFieldValue(date,Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得指定日期是所在月的第几天
     *
     * @param date 日期
     *
     * @return 周
     */
    public static int dayOfMonth(Date date) {
        return  getDateFieldValue(date,Calendar.DAY_OF_MONTH);
    }


    /**
     * 获得指定日期是所在周的第几天，也就是星期几，符合中国人习惯，获取到的值减去1
     *
     * @param date 日期
     *
     * @return 周
     */
    public static int dayOfWeek(Date date) {
        return getDateFieldValue(date,Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * 获得指定日期的小时数部分
     *
     * @param date 日期
     * @param is24HourClock 是否24小时制
     *
     * @return 小时数
     */
    public static int hour(Date date, boolean is24HourClock) {
        return is24HourClock?getDateFieldValue(date,Calendar.HOUR_OF_DAY):getDateFieldValue(date,Calendar.HOUR);
    }


    /**
     * 获得指定日期的分钟部分
     *
     * @param date 日期
     * @return 秒数
     */
    public static int minute(Date date) {
        return getDateFieldValue(date,Calendar.MINUTE);
    }

    /**
     * 获得指定日期的秒数部分
     *
     * @param date 日期
     * @return 秒数
     */
    public static int second(Date date) {
        return getDateFieldValue(date,Calendar.SECOND);
    }

    /**
     * 获得指定日期的毫秒数部分
     *
     * @param date 日期
     *
     * @return 毫秒数
     */
    public static int millsecond(Date date) {
        return getDateFieldValue(date,Calendar.MILLISECOND);
    }


    /**
     * 是否为上午
     *
     * @param date 日期
     *
     * @return 是否为上午
     */
    public static boolean isAM(Date date) {
        return Calendar.AM == getDateFieldValue(date,Calendar.AM_PM);
    }

    /**
     * 是否为上午
     *
     * @param calendar {@link Calendar}
     *
     * @return 是否为上午
     *
     */
    public static boolean isAM(Calendar calendar) {
        return Calendar.AM == calendar.get(Calendar.AM_PM);
    }

    /**
     * 是否为下午
     *
     * @param date 日期
     *
     * @return 是否为下午
     */
    public static boolean isPM(Date date) {
        return Calendar.PM == getDateFieldValue(date,Calendar.AM_PM);
    }

    /**
     * 是否为下午
     *
     * @param calendar {@link Calendar}
     *
     * @return 是否为下午
     *
     */
    public static boolean isPM(Calendar calendar) {
        return Calendar.PM == calendar.get(Calendar.AM_PM);
    }


    /**
     * @return 今年
     */
    public static int thisYear() {
        return getYear(new Date());
    }

    /**
     * @return 当前月份
     */
    public static int thisMonth() {
        return getMonth(new Date());
    }


    /**
     * @return 当前日期所在年份的第几周
     */
    public static int thisWeekOfYear() {
        return weekOfYear(new Date());
    }

    /**
     * @return 当前日期所在年份的第几周
     */
    public static int thisWeekOfMonth() {
        return weekOfMonth(new Date());
    }

    /**
     * @return 当前日期是这个日期所在月份的第几天
     */
    public static int thisDayOfMonth() {
        return dayOfMonth(new Date());
    }

    /**
     * @return 当前日期是星期几
     */
    public static int thisDayOfWeek() {
        return dayOfWeek(new Date());
    }

    /**
     * @param is24HourClock 是否24小时制
     *
     * @return 当前日期的小时数部分<br>
     */
    public static int thisHour(boolean is24HourClock) {
        return hour(new Date(), is24HourClock);
    }

    /**
     * @return 当前日期的分钟数部分<br>
     */
    public static int thisMinute() {
        return minute(new Date());
    }

    /**
     * @return 当前日期的秒数部分<br>
     */
    public static int thisSecond() {
        return second(new Date());
    }

    /**
     * @return 当前日期的毫秒数部分<br>
     */
    public static int thisMillsecond() {
        return millsecond(new Date());
    }

    /**
     * 获取当天的开始时间
     *
     * @return 每天的开始时间
     *
     */
    public static Date beginOfDay() {
        return beginOfDay(new Date());
    }

    /**
     * 获取某天的开始时间
     *
     * @param date 时间
     *
     * @return 每天的开始时间
     *
     */
    public static Date beginOfDay(Date date) {
        return truncate(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某天的结束时间
     *
     * @return 每天的结束时间 其实就是第二天的开始时间
     *
     */
    public static Date endOfDay() {
        return endOfDay(new Date());
    }


    /**
     * 获取某天的结束时间
     *
     * @param date 时间
     *
     * @return 每天的结束时间
     *
     */
    public static Date endOfDay(Date date) {
        return ceiling(date, Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取某周的开始时间，周一为第一天
     *
     * @param calendar 日历
     *
     * @return 开始时间
     */
    public static Date beginOfWeek(Calendar calendar) {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return truncate(calendar, Calendar.WEEK_OF_MONTH);
    }
    /**
     * 获取某周的开始时间，周一为第一天
     *
     * @param date 时间
     *
     * @return 开始时间
     */
    public static Date beginOfWeek(Date date) {
        return beginOfWeek(toCalendar(date));
    }

    /**
     * 获取某周的结束时间
     *
     * @param calendar 日历
     *
     * @return 结束日期
     */
    public static Date endOfWeek(Calendar calendar) {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        return ceiling(calendar.getTime(), Calendar.WEEK_OF_MONTH);
    }

    /**
     * 获取某周的结束时间
     *
     * @param date 日期
     *
     * @return 结束日期
     */
    public static Date endOfWeek(Date date) {
        return endOfWeek(toCalendar(date));
    }

    /**
     * 获取某月的开始时间
     *
     * @param date 日期
     *
     * @return 开始日期
     */
    public static Date beginOfMonth(Date date) {
        return beginOfMonth(toCalendar(date));
    }

    /**
     * 获取某月的开始时间
     *
     * @param calendar 日历
     *
     * @return 开始日期
     */
    public static Date beginOfMonth(Calendar calendar) {
        return truncate(calendar, Calendar.MONTH);
    }

    /**
     * 获取某月的开始时间
     *
     * @param date 日期
     *
     * @return 开始日期
     */
    public static String beginOfMonth(String date) throws Exception{
        return getMonthDate(beginOfMonth(parseDate(date)));
    }


    /**
     * 获取某月的结束时间
     *
     * @param calendar 日历
     *
     * @return 结束日期
     */
    public static Date endOfMonth(Calendar calendar) {
        return ceiling(calendar, Calendar.MONTH);
    }

    /**
     * 获取某月的结束时间
     *
     * @param date 日期
     *
     * @return 结束日期
     */
    public static Date endOfMonth(Date date) {
        return endOfMonth(toCalendar(date));
    }

    /**
     * 获取某月的结束时间
     *
     * @param date 日期
     *
     * @return 结束时间
     */
    public static String endOfMonth(String date) throws Exception{
        return getMonthDate(endOfMonth(parseDate(date)));
    }



    /**
     * 获取某季度的开始时间
     *
     * @param calendar 日历
     *
     * @return 开始日期
     */
    public static Date beginOfQuarter(Calendar calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) / 3 * 3);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return beginOfDay(calendar.getTime());
    }

    /**
     * 获取某季度的开始时间
     *
     * @param date 日期
     *
     * @return 开始时间
     */
    public static Date beginOfQuarter(Date date) {
        return beginOfQuarter(toCalendar(date));
    }

    /**
     * 获取某季度的结束时间
     *
     * @param calendar 日历
     *
     * @return 结束时间
     */
    public static Date endOfQuarter(Calendar calendar) {
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) / 3 * 3 + 2);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endOfDay(calendar.getTime());
    }

    /**
     * 获取某季度的结束时间
     *
     * @param date 日期
     *
     * @return 结束时间
     */
    public static Date endOfQuarter(Date date) {
        return endOfQuarter(toCalendar(date));
    }


    /**
     * 获取某年的开始时间
     *
     * @param calendar 日历
     *
     * @return 开始时间
     */
    public static Date beginOfYear(Calendar calendar) {
        return truncate(calendar, Calendar.YEAR);
    }

    /**
     * 获取某年的开始时间
     *
     * @param date 日期
     *
     * @return 开始时间
     */
    public static Date beginOfYear(Date date) {
        return beginOfYear(toCalendar(date));
    }

    /**
     * 获取某年的结束时间
     *
     * @param calendar 日历
     *
     * @return 结束时间
     */
    public static Date endOfYear(Calendar calendar) {
        return ceiling(calendar, Calendar.YEAR);
    }

    /**
     * 获取某年的结束时间
     *
     * @param date 日期
     *
     * @return 结束时间
     */
    public static Date endOfYear(Date date) {
        return endOfYear(toCalendar(date));
    }

    /**
     * 昨天
     *
     * @return 昨天
     *
     */
    public static Date yesterday() {
        return offsetDay(new Date(), -1);
    }

    /**
     * 明天
     *
     * @return 明天
     *
     */
    public static Date tomorrow() {
        return offsetDay(new Date(), 1);
    }

    /**
     * 上周
     *
     * @return 上周
     *
     */
    public static Date lastWeek() {
        return offsetWeek(new Date(), -1);
    }

    /**
     * 下周
     *
     * @return 下周
     *
     */
    public static Date nextWeek() {
        return offsetWeek(new Date(), 1);
    }

    /**
     * 上个月
     *
     * @return 上个月
     *
     */
    public static Date lastMonth() {
        return offsetMonth(new Date(), -1);
    }

    /**
     * 下个月
     *
     * @return 下个月
     *
     */
    public static Date nextMonth() {
        return offsetMonth(new Date(), 1);
    }



    /**
     * 偏移毫秒数
     *
     * @param date 日期
     * @param offset 偏移毫秒数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetMillisecond(Date date, int offset) {
        return offset(date, Calendar.MILLISECOND, offset);
    }

    /**
     * 偏移秒数
     *
     * @param date 日期
     * @param offset 偏移秒数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetSecond(Date date, int offset) {
        return offset(date, Calendar.SECOND, offset);
    }

    /**
     * 偏移分钟
     *
     * @param date 日期
     * @param offset 偏移分钟数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetMinute(Date date, int offset) {
        return offset(date, Calendar.MINUTE, offset);
    }

    /**
     * 偏移小时
     *
     * @param date 日期
     * @param offset 偏移小时数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetHour(Date date, int offset) {
        return offset(date, Calendar.HOUR_OF_DAY, offset);
    }

    /**
     * 偏移天
     *
     * @param date 日期
     * @param offset 偏移天数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetDay(Date date, int offset) {
        return offset(date, Calendar.DAY_OF_YEAR, offset);
    }

    /**
     * 偏移周
     *
     * @param date 日期
     * @param offset 偏移周数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetWeek(Date date, int offset) {
        return offset(date, Calendar.WEEK_OF_YEAR, offset);
    }

    /**
     * 偏移月
     *
     * @param date 日期
     * @param offset 偏移月数，正数向未来偏移，负数向历史偏移
     *
     * @return 偏移后的日期
     */
    public static Date offsetMonth(Date date, int offset) {
        return offset(date, Calendar.MONTH, offset);
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date 基准日期
     * @param dateField 偏移的粒度大小（小时、天、月等）
     * @param offset 偏移量，正数为向后偏移，负数为向前偏移
     *
     * @return 偏移后的日期
     */
    public static Date offset(final Date date, final int dateField, final int offset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(dateField, offset);
        return cal.getTime();
    }

    /**
     * 判断两个日期相差的毫秒数<br>
     *
     *
     * @param beginDate 起始时间
     * @param endDate 结束时间

     * @return 毫秒差
     */
    public static long betweenMSecond(Date beginDate, Date endDate) {
        return between(beginDate, endDate, DateUnit.MS);
    }

    /**
     * 判断两个日期相差的秒数<br>
     *
     *
     * @param beginDate 起始时间
     * @param endDate 结束时间

     * @return 秒差
     */
    public static long betweenSecond(Date beginDate, Date endDate) {
        return between(beginDate, endDate, DateUnit.SECOND);
    }

    /**
     * 判断两个日期相差的分钟数<br>
     *
     *
     * @param beginDate 起始时间
     * @param endDate 结束时间

     * @return 分钟差
     */
    public static long betweenMinute(Date beginDate, Date endDate) {
        return between(beginDate, endDate, DateUnit.MINUTE);
    }


    /**
     * 判断两个日期相差的小时数<br>
     *
     *
     * @param beginDate 起始时间
     * @param endDate 结束时间

     * @return 小时差
     */
    public static long betweenHour(Date beginDate, Date endDate) {
        return between(beginDate, endDate, DateUnit.HOUR);
    }


    /**
     * 判断两个日期相差的天数<br>
     *
     * <pre>
     * 有时候我们计算相差天数的时候需要忽略时分秒。
     * 比如：2016-02-01 23:59:59和2016-02-02 00:00:00相差一秒
     * 如果isReset为<code>false</code>相差天数为0。
     * 如果isReset为<code>true</code>相差天数将被计算为1
     * </pre>
     *
     * @param beginDate 起始日期
     * @param endDate 结束日期
     * @param isReset 是否重置时间为起始时间
     *
     * @return 日期差
     */
    public static long betweenDay(Date beginDate, Date endDate, boolean isReset) {
        if (isReset) {
            beginDate = beginOfDay(beginDate);
            endDate = beginOfDay(endDate);
        }
        return between(beginDate, endDate, DateUnit.DAY);
    }


    /**
     * 计算两个日期相差月数<br>
     * 在非重置情况下，如果起始日期的天小于结束日期的天，月数要少算1（不足1个月）
     *
     * @param begin 开始时间
     * @param end 结束时间
     * @param isReset 是否重置时间为起始时间（重置月天时分秒）
     *
     * @return 相差月数
     *
     */
    public static long betweenMonth(final Date begin,final Date end,final boolean isReset) {
        final Calendar beginCal = toCalendar(begin);
        final Calendar endCal = toCalendar(end);

        final int betweenYear = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
        final int betweenMonthOfYear = endCal.get(Calendar.MONTH) - beginCal.get(Calendar.MONTH);

        int result = betweenYear * 12 + betweenMonthOfYear;
        if (!isReset) {
            endCal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR));
            endCal.set(Calendar.MONTH, beginCal.get(Calendar.MONTH));
            long between = endCal.getTimeInMillis() - beginCal.getTimeInMillis();
            if (between < 0) {
                return result - 1;
            }
        }
        return result;
    }

    /**
     * 计算两个日期相差年数<br>
     * 在非重置情况下，如果起始日期的月小于结束日期的月，年数要少算1（不足1年）
     *
     * @param begin 开始时间
     * @param end 结束时间
     * @param isReset 是否重置时间为起始时间（重置月天时分秒）
     *
     * @return 相差年数
     *
     */
    public static long betweenYear(final Date begin,final Date end,final boolean isReset) {
        final Calendar beginCal = toCalendar(begin);
        final Calendar endCal = toCalendar(end);

        int result = endCal.get(Calendar.YEAR) - beginCal.get(Calendar.YEAR);
        if (!isReset) {
            endCal.set(Calendar.YEAR, beginCal.get(Calendar.YEAR));
            long between = endCal.getTimeInMillis() - beginCal.getTimeInMillis();
            if (between < 0) {
                return result - 1;
            }
        }
        return result;
    }


    /**
     * 当前日期是否在日期指定范围内<br>
     *
     * @param date 被检查的日期
     * @param beginDate 起始日期
     * @param endDate 结束日期
     *
     * @return 是否在范围内
     *
     */
    public static boolean isIn(Date date, Date beginDate, Date endDate) {
        long beginMills = beginDate.getTime();
        long endMills = endDate.getTime();
        long thisMills = date.getTime();

        return thisMills >= Math.min(beginMills, endMills) && thisMills <= Math.max(beginMills, endMills);
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：纳秒
     *
     * @param preTime 之前记录的时间
     *
     * @return 时间差，纳秒
     */
    public static long spendNt(long preTime) {
        return System.nanoTime() - preTime;
    }

    /**
     * 计时，常用于记录某段代码的执行时间，单位：毫秒
     *
     * @param preTime 之前记录的时间
     *
     * @return 时间差，毫秒
     */
    public static long spendMs(long preTime) {
        return System.currentTimeMillis() - preTime;
    }

    /**
     * 计算指定指定时间区间内的周数
     *
     * @param start 开始时间
     * @param end 结束时间
     *
     * @return 周数
     */
    public static int weekCount(Date start, Date end) {
        final Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        final Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);

        final int startWeekofYear = startCalendar.get(Calendar.WEEK_OF_YEAR);
        final int endWeekofYear = endCalendar.get(Calendar.WEEK_OF_YEAR);

        int count = endWeekofYear - startWeekofYear + 1;

        if (Calendar.SUNDAY != startCalendar.get(Calendar.DAY_OF_WEEK)) {
            count--;
        }

        return count;
    }


    /**
     * 生日转为年龄，计算法定年龄
     *
     * @param birthDay 生日，标准日期字符串
     * @return 年龄
     */
    public static int ageOfNow(String birthDay) throws ParseException{
        return ageOfNow(parseDate(birthDay));
    }

    /**
     * 生日转为年龄，计算法定年龄
     *
     * @param birthDay 生日
     * @return 年龄
     */
    public static int ageOfNow(Date birthDay) {
        return age(birthDay, new Date());
    }

    /**
     * 是否闰年
     *
     * @param year 年
     * @return 是否闰年
     */
    public static boolean isLeapYear(int year) {
        return new GregorianCalendar().isLeapYear(year);
    }


    /**
     * 通过生日计算星座
     *
     * @param date 出生日期
     * @return 星座名
     */
    public static String getZodiac(Date date) {
        return getZodiac(toCalendar(date));
    }

    /**
     * 通过生日计算星座
     *
     * @param calendar 出生日期
     * @return 星座名
     */
    public static String getZodiac(Calendar calendar) {
        if (null == calendar) {
            return null;
        }
        return getZodiac(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 通过生日计算星座
     *
     * @param month 月，从0开始计数
     * @param day 天
     * @return 星座名
     * @since 4.5.0
     */
    public static String getZodiac(Month month, int day) {
        return getZodiac(month.getValue(), day);
    }


    /**
     * 通过生日计算生肖，只计算1900年后出生的人
     *
     * @param date 出生日期（年需农历）
     * @return 星座名
     */
    public static String getChineseZodiac(Date date) {
        return getChineseZodiac(toCalendar(date));
    }

    /**
     * 通过生日计算生肖，只计算1900年后出生的人
     *
     * @param calendar 出生日期（年需农历）
     * @return 星座名
     */
    public static String getChineseZodiac(Calendar calendar) {
        if (null == calendar) {
            return null;
        }
        return getChineseZodiac(calendar.get(Calendar.YEAR));
    }

    /**
     * 计算生肖，只计算1900年后出生的人
     *
     * @param year 农历年
     *
     * @return 生肖名
     */
    public static String getChineseZodiac(int year) {
        if (year < 1900) {
            return null;
        }
        return CHINESE_ZODIACS[(year - 1900) % CHINESE_ZODIACS.length];
    }

    /**
     * 取得月天数
     *
     * @param date 日期
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return getDayOfMonth(c);
    }

    /**
     * 取得月天数
     *
     * @param calendar 日历
     * @return
     */
    public static int getDayOfMonth(Calendar calendar) {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    /**
     * 获取社交时间，比如几分钟前，几小时前等
     *
     * @param date 社交时间
     *
     * @return
     */
    public static String getInteractionTime(Date date){
        //当天
        if (isSameDay(date,new Date())) {
            long hourDiff = betweenHour( date,new Date());
            if(hourDiff>0L){
                return hourDiff+"小时前";
            }else{
                long minuteDiff = betweenMinute( date,new Date());
                if(minuteDiff<1){
                    return "刚刚";
                }else{
                    return minuteDiff+"分钟前";
                }
            }
        }else{
            long datDiff = betweenDay( date,new Date(),false);
            if(datDiff<= 5L){
                return (datDiff==0?1:datDiff)+"天前";
            }else{
                return FULL_DATE_TIME_FORMAT.format(date);
            }
        }
    }


    /*****************仅在此类中用的私有方法**********************/

    /**
     * 判断两个日期相差的时长<br>
     * 返回 给定单位的时长差
     *
     * @param unit 相差的单位：相差 天、小时 等
     *
     * @return 时长差
     */
    private static long between(final Date begin,final Date end,final DateUnit unit) {
        long diff = end.getTime() - begin.getTime();
        return diff / unit.getMillis();
    }

    /**
     * 获取日期某字段的值
     *
     * @param date 日期
     * @param field 最后的字段
     *
     * @return 字段的值
     */
    private static Integer getDateFieldValue(final Date date,final int field) {
        GregorianCalendar ca = new GregorianCalendar();
        ca.setTime(date);
        return ca.get(field);
    }


    /**
     * 获得指定日期年份和季节<br>
     * 格式：2019年第二季度
     *
     * @param date 日期
     */
    private static String yearAndQuarter(Date date) {
        return getYear(date)+"年"+ QuarterEnum.getQuarterEnum(getQuarter(date));
    }

    /**
     * 计算相对于dateToCompare的年龄，长用于计算指定生日在某年的年龄
     *
     * @param birthDay 生日
     * @param dateToCompare 需要对比的日期
     *
     * @return 年龄
     */
    private static int age(Date birthDay, Date dateToCompare) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCompare);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthDay);
        int age = year - cal.get(Calendar.YEAR);

        int monthBirth = cal.get(Calendar.MONTH);
        if (month == monthBirth) {
            int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
            if (dayOfMonth < dayOfMonthBirth) {
                // 如果生日在当月，但是未达到生日当天的日期，年龄减一
                age--;
            }
        } else if (month < monthBirth) {
            // 如果当前月份未达到生日的月份，年龄计算减一
            age--;
        }

        return age;
    }

    /**
     * 通过生日计算星座
     *
     * @param month 月，从0开始计数，见{@link Month#getValue()}
     * @param day 天
     *
     * @return 星座名
     */
    private static String getZodiac(int month, int day) {
        // 在分隔日前为前一个星座，否则为后一个星座
        return day < DAY_ARR[month] ? ZODIACS[month] : ZODIACS[month + 1];
    }

    /**
     * 判断输入日期是一个星期中的第几天(星期天为一个星期第一天),减去一天,老外的星期是从星期日开始的，老外的日历里面星期天是0
     *
     * @param date 日期
     *
     * @return
     */
    public static int getWeekIndex(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK)-1==0?7:calendar.get(Calendar.DAY_OF_WEEK)-1;
    }

    /**
     * 获取上周几的日期
     *
     * @param date 日期
     *
     * @return
     */
    public static Date getLastWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        return calendar.getTime();
    }

    /**
     * 获取上周几的日期
     *
     * @param date 日期
     *
     * @return
     */
    public static String getLastWeekDayStr(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        return getDate(calendar);
    }




    /**
     * 季度翻译
     */
    private enum QuarterEnum {

        /**
         * 第一季度
         */
        FIRST_QUARTER(1, "第一季度"),

        /**
         * 第二季度
         */
        SECOND_QUARTER(2, "第二季度"),

        /**
         * 第三季度
         */
        THIRD_QUARTER(3, "第三季度"),

        /**
         * 第四季度
         */
        FORTH_QUARTER(4, "第四季度"),
        ;

        private int code;

        private String desc;

        QuarterEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static QuarterEnum getQuarterEnum(int code) {
            for (QuarterEnum result : values()) {
                if (result.getCode() == code) {
                    return result;
                }
            }
            return null;
        }

        public int getCode() {
            return this.code;
        }

        public String getDesc() {
            return desc;
        }

    }

    /**
     * 日期时间单位，每个单位都是以毫秒为基数
     *
     */
    public enum DateUnit {
        /** 一毫秒 */
        MS(1),

        /** 一秒的毫秒数 */
        SECOND(1000),

        /**一分钟的毫秒数 */
        MINUTE(SECOND.getMillis() * 60),

        /**一小时的毫秒数 */
        HOUR(MINUTE.getMillis() * 60),

        /**一天的毫秒数 */
        DAY(HOUR.getMillis() * 24),

        /**一周的毫秒数 */
        WEEK(DAY.getMillis() * 7);

        private long millis;
        DateUnit(long millis){
            this.millis = millis;
        }

        public long getMillis(){
            return this.millis;
        }
    }


}
