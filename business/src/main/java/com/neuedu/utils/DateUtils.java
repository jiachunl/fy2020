package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    public static final String STANDARD="yyyy-MM-dd mm:HH:ss";
    /**
     * 将时间DATE类型的时间转成字符串
     */

    public static String date2Str(Date date){
        if (date == null){
            return "";
        }
        DateTime dateTime = new DateTime();
        return dateTime.toString(STANDARD);

    }

    public static String date2Str(Date date,String formate){
        if (date == null){
            return "";
        }
        DateTime dateTime = new DateTime();
        return dateTime.toString(formate);

    }
    /**
     * 将字符串类型的时间转成DATE
     */
    public static Date str2Date(String strDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD);
        DateTime date = dateTimeFormatter.parseDateTime(strDate);
        return date.toDate();
    }
    public static Date str2Date(String strDate,String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime date = dateTimeFormatter.parseDateTime(strDate);
        return date.toDate();
    }
}
