package fun.bookish.blueberry.sip.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 *
 * @author LIUXINDONG
 */
public class DateUtils {

    private DateUtils() {
    }

    /**
     * 获取当前时间的字符串格式
     *
     * @return
     */
    public static String getNowDateTimeStr() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 转换日期类
     *
     * @return
     */
    public static LocalDate convertDate(Date date, String pattern) {
        String dateStr = new SimpleDateFormat(pattern).format(date);
        return parseStrToLocalDate(dateStr, pattern);
    }

    /**
     * 转换日期类
     *
     * @return
     */
    public static LocalDateTime convertDateTime(Date date, String pattern) {
        String dateStr = new SimpleDateFormat(pattern).format(date);
        return parseDateStr(dateStr, pattern);
    }

    /**
     * 获取当前时间的字符串格式
     *
     * @param pattern
     * @return
     */
    public static String getNowDateStr(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间戳(秒)
     *
     * @return
     */
    public static Long getSecondTimestamp() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
    }

    /**
     * 获取当前时间戳(毫秒)
     *
     * @return
     */
    public static Long getMilliSecondTimestamp() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当前时间戳(纳秒)
     *
     * @return
     */
    public static Long getNanoTimestamp() {
        return System.nanoTime();
    }

    /**
     * 格式化指定的时间
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String formatDate(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化指定的时间
     *
     * @param time
     * @return
     */
    public static String formatDate(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 格式化指定的日期
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 格式化指定的日期
     *
     * @param date
     * @return
     */
    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 格式化指定的时间
     *
     * @param time
     * @param pattern
     * @return
     */
    public static String formatDate(Date time, String pattern) {
        return new SimpleDateFormat(pattern).format(time);
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDateTime parseDateStr(String dateStr, String pattern) {
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析日期字符串
     *
     * @param dateStr
     * @param pattern
     * @return
     */
    public static LocalDate parseStrToLocalDate(String dateStr, String pattern) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 日期之前间隔天数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long intervalDaysOfLocalDate(String startDate, String endDate, String pattern) {
        LocalDate localStartDate = parseStrToLocalDate(startDate, pattern);
        LocalDate localEndDate = parseStrToLocalDate(endDate, pattern);

        return localStartDate.until(localEndDate, ChronoUnit.DAYS);
    }

    /**
     * 日期之间间隔天数集合
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> intervalDayList(String startDate, String endDate) {
        List<String> days = new ArrayList<>();
        DateFormat dateFormat = DateFormat.getDateInstance();

        Date parseStartDate;
        Date parseEndDate;
        try {
            parseStartDate = dateFormat.parse(startDate);
            parseEndDate = dateFormat.parse(endDate);

            Calendar start = Calendar.getInstance();
            start.setTime(parseStartDate);
            Calendar end = Calendar.getInstance();
            end.setTime(parseEndDate);

            while (start.before(end) || start.equals(end)) {
                String format = dateFormat.format(start.getTime());
                String[] split = format.split("-");

                if (Integer.valueOf(split[1]) < 10) {
                    split[1] = "0" + split[1];
                }
                if (Integer.valueOf(split[2]) < 10) {
                    split[2] = "0" + split[2];
                }
                days.add(split[0] + "-" + split[1] + "-" + split[2]);
                start.add(Calendar.DAY_OF_YEAR, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }

    /**
     * 时间之前间隔天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long intervalDaysOfLocalDateTime(String startTime, String endTime, String pattern) {
        LocalDateTime localStartTime = parseDateStr(startTime, pattern);
        LocalDateTime localEndTime = parseDateStr(endTime, pattern);

        return localStartTime.until(localEndTime, ChronoUnit.DAYS);
    }

    /**
     * 时间之前间隔小时数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static Long intervalHoursOfLocalDateTime(String startTime, String endTime, String pattern) {
        LocalDateTime localStartTime = parseDateStr(startTime, pattern);
        LocalDateTime localEndTime = parseDateStr(endTime, pattern);

        return localStartTime.until(localEndTime, ChronoUnit.HOURS);
    }
}
