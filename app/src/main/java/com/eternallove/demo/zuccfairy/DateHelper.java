package com.eternallove.demo.zuccfairy;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by angelzouxin on 2016/12/4.
 */

public class DateHelper {

    private static SimpleDateFormat DATE_FORMAT_TILL_DAY_TILL_MINUTES = new SimpleDateFormat(
            "yyyy年MM月dd日 HH:mm");
    private static String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    public static String timeLogic(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.get(Calendar.DAY_OF_MONTH);
        long now = calendar.getTimeInMillis();
        Date date = strToDate(dateStr, DATE_FORMAT);
        calendar.setTime(date);
        long past = calendar.getTimeInMillis();

        // 相差的秒数
        long time = (now - past) / 1000;

        StringBuffer sb = new StringBuffer();
        if (time > 0 && time < 60) { // 1小时内
            return sb.append(time + "秒前").toString();
        } else if (time > 60 && time < 3600) {
            return sb.append(time / 60 + "分钟前").toString();
        } else if (time >= 3600 && time < 3600 * 24) {
            return sb.append(time / 3600 + "小时前").toString();
        } else if (time >= 3600 * 24 && time < 3600 * 48) {
            return sb.append("昨天").toString();
        } else if (time >= 3600 * 48 && time < 3600 * 72) {
            return sb.append("前天").toString();
        } else if (time >= 3600 * 72) {
            return dateToString(dateStr, DATE_FORMAT);
        }
        return dateToString(dateStr, DATE_FORMAT);
    }

    /**
     * 日期字符串转换为Date
     *
     * @param dateStr
     * @param format
     * @return
     */
    public static Date strToDate(String dateStr, String format) {
        Date date = null;

        if (!TextUtils.isEmpty(dateStr)) {
            DateFormat df = new SimpleDateFormat(format);
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 日期转换为字符串
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static String dateToString(String timeStr, String format) {
        Date date = DateHelper.strToDate(timeStr, format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return DATE_FORMAT_TILL_DAY_TILL_MINUTES.format(date);
    }

    public static String dateToString(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat (format);
        return formatter.format(date);
    }
}
