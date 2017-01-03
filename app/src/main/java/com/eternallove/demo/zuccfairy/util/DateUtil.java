package com.eternallove.demo.zuccfairy.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @description:
 * @author: eternallove
 * @date: 2017/1/2 20:40
 */

public class DateUtil {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public DateUtil(){}
    public static String getReportDate(long Timestamp){
        Date date = new Date(Timestamp);
        String reportDate = df.format(date);
        return reportDate;
    }
    public static long getStartTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY,0);
        return todayStart.getTime().getTime();

    }
    public static long getEndTime(){
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY,23);
        return todayStart.getTime().getTime();
    }


}
