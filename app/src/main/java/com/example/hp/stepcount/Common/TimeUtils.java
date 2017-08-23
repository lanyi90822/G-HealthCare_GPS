package com.example.hp.stepcount.Common;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by HP on 2016/9/22.
 */
public class TimeUtils {

    public static final long MILLIO_TIME = 1;
    public static final long SECOND_TIME = 1000 * MILLIO_TIME;
    public static final long MINUTE_TIME = 60 * SECOND_TIME;
    public static final long HORE_TIME = 60 * MINUTE_TIME;
    public static final long DAY_TIME = 24 * HORE_TIME;



    @SuppressLint("SimpleDateFormat")
    public static String getString(long m,String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date(m);
        return dateFormat.format(date);
    }


    public static String getMAS(long m){
        int s = (int) (m / 1000);
        int min = s / 60;
        int hour = min / 60;
        min %= 60;
        s %= 60;
        hour %= 24;
        String ms = min >= 10 ? String.valueOf(min) : "0" + min;
        String ss = s >= 10 ? String.valueOf(s) : "0" + s;
        String hh = hour > 0 ? hour >= 10 ? String.valueOf(hour) : "0" + hour : "";
        return ("".equals(hh) ? "" : hh + ":") + ms + ":" + ss;
    }
    public static String getHAMAS(long m){
        int s = (int) (m / 1000);
        int min = s / 60;
        int hour = min / 60;
        hour %= 24;
        min %= 60;
        s %= 60;
        String ms = min >= 10 ? String.valueOf(min) : "0" + min;
        String ss = s >= 10 ? String.valueOf(s) : "0" + s;
        String hs = hour >= 10 ? String.valueOf(hour) : "0" + hour;
        return hs + ":" + ms + ":" + ss;
    }

    public static int getDayByMonth(int year,int month){
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10
                || month == 12){
            return 31;
        }
        if(month == 4 || month == 6 || month == 9 || month == 11){
            return 30;
        }
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            return 29;
        }
        return 28;
    }

    /**
     * 获取指定类型的时间数据
     */
    public static int getDateAndType(long birth, int type){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(birth);
        return c.get(type);
    }

    public static boolean isToday(long time){
        return isToday(time, System.currentTimeMillis());
    }

    public static boolean isToday(long time,long time1){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(time1);
        int d = c1.get(Calendar.DAY_OF_YEAR);
        int d1 = c.get(Calendar.DAY_OF_YEAR);



        return c1.get(Calendar.YEAR) == c.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR);
    }


    public static long generateTimeByYMD(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day,c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),
                c.get(Calendar.SECOND));
        return c.getTimeInMillis();
    }

    public static long getZeroTime(long time){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c = Calendar.getInstance();
        c.set(year, month, day,0,0,0);
        return c.getTimeInMillis() / 1000 * 1000;
    }
    public static int getDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_YEAR);
    }














}
