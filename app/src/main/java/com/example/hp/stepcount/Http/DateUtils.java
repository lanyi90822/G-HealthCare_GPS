package com.example.hp.stepcount.Http;

/**
 * Created by bin on 10/02/2017.
 */
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtils {


    public static final SimpleDateFormat SDF_HH_mm = new SimpleDateFormat("HH:mm");

    public static final SimpleDateFormat SDF_HH_mm_ss = new SimpleDateFormat("HH:mm:ss");

    public static final SimpleDateFormat SDF_YYYY_MM = new SimpleDateFormat("yyyy-MM");

    public static final SimpleDateFormat SDF_yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat SDF_yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static final SimpleDateFormat SDF_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat SDF_yyyy_MM_dd_HH_mm_ss_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static final SimpleDateFormat SDF_ISO0861 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+0800");

    /**
     * 距当前多长时间
     * @return
     */
    public static String getDelay(Date date){
        if(date == null){
            return null;
        }
//		long time = date.getTime();
        long currentTime = System.currentTimeMillis();
        Calendar currentC = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        currentC.setTimeInMillis(currentTime);
        c.setTime(date);
        int delayYear = currentC.get(Calendar.YEAR) - c.get(Calendar.YEAR);
        int delayMoth = currentC.get(Calendar.MONTH) - c.get(Calendar.MONTH);
        int delayDate = currentC.get(Calendar.DATE) - c.get(Calendar.DATE);
        int delayHour = currentC.get(Calendar.HOUR_OF_DAY) - c.get(Calendar.HOUR_OF_DAY);
        int delayMinute = currentC.get(Calendar.MINUTE) - c.get(Calendar.MINUTE);
        int delaySecond = currentC.get(Calendar.SECOND) - c.get(Calendar.SECOND);
        int delayMillistSecond = currentC.get(Calendar.MILLISECOND) - c.get(Calendar.MILLISECOND);
        //long delay = currentTime  - time;
        String result = "";
        if(delayYear > 1){
            result = formatDate(date, SDF_yyyy_MM_dd_HH_mm);
            return result;
        } else if(delayYear == 1){
            if(delayMoth < 0){
                result = "1年前";
            } else if(delayMoth == 0){
                if(delayDate < 0){
                    result = "1年前";
                } else if(delayDate == 0){
                    if(delayHour < 0){
                        result = "1年前";
                    } else if(delayHour == 0){
                        if(delayMinute < 0){
                            result = "1年前";
                        } else if(delayMinute == 0){
                            if(delaySecond < 0){
                                result = "1年前";
                            } else if(delaySecond == 0){
                                if(delayMillistSecond < 0){
                                    result = "1年前";
                                } else if(delayMillistSecond == 0){
                                    result = "1年前";
                                } else {
                                    result = "11个月前";
                                }
                            } else {
                                result = "11个月前";
                            }
                        } else {
                            result = "11个月前";
                        }
                    } else {
                        result = "11个月前";
                    }
                } else {
                    result = "11个月前";
                }
            } else {
                result = (12 - delayMoth) + "个月前";
            }
        } else if(delayYear == 0){
            if(delayMoth < 0){
//				result = (-delayMoth) + "个月后";
                result = "刚刚";
            } else if(delayMoth == 0){
                if(delayDate < 0){
//					result = (-delayDate) +"天后";
                    result = "刚刚";
                } else if(delayDate == 0){
                    if(delayHour < 0){
//						result = (-delayHour) +"小时后";
                        result = "刚刚";
                    } else if(delayHour == 0){
                        if(delayMinute < 0){
//							result = (-delayMinute) +"分钟后";
                            result = "刚刚";
                        } else if(delayMinute == 0){
                            if(delaySecond < 0){
//								result = (-delaySecond) +"秒后";
                                result = "刚刚";
                            } else if(delaySecond == 0){
                                result = "刚刚";
                            } else {
                                result = (delayMinute) +"秒前";
                            }
                        } else {
                            result = (delayMinute) +"分钟前";
                        }
                    } else {
                        result = (delayHour) +"小时前";
                    }
                } else {
                    result = (delayDate) +"天前";
                }
            } else {
                if(delayDate < 0){
                    if(delayMoth > 1){
                        result = (delayMoth - 1) + "个月前";
                    } else {
                        if(delayHour < 0){
                            int d = currentC.get(Calendar.DATE) + c.getActualMaximum(Calendar.DATE) - c.get(Calendar.DATE);
                            result = (d - 1) + "天前";
                        } else {
                            int d = currentC.get(Calendar.DATE) + c.getActualMaximum(Calendar.DATE) - c.get(Calendar.DATE);
                            result = d + "天前";
                        }
                    }
                } else if(delayDate == 0){
                    if(delayHour < 0){
                        result = delayMoth + "个月前";
                    } else if(delayHour == 0){
                        if(delayMinute < 0){
                            result = delayMoth + "个月前";
                        } else if(delayMinute == 0){
                            if(delaySecond < 0){
                                result = delayMoth + "个月前";
                            } else if(delaySecond == 0){
                                result = delayMoth + "个月前";
                            } else {
                                if(delayMoth > 1){
                                    result = (delayMoth - 1) + "个月前";
                                } else {
                                    int d = currentC.get(Calendar.DATE) + c.getActualMaximum(Calendar.DATE) - c.get(Calendar.DATE);
                                    result = d + "天前";
                                }
                            }
                        } else {
                            if(delayMoth > 1){
                                result = (delayMoth - 1) + "个月前";
                            } else {
                                int d = currentC.get(Calendar.DATE) + c.getActualMaximum(Calendar.DATE) - c.get(Calendar.DATE);
                                result = d + "天前";
                            }
                        }
                    } else {
                        result = delayMoth + "个月前";
                    }
                } else {
                    if(delayMoth > 1){
                        result = (delayMoth - 1) + "个月前";
                    } else {
                        int d = currentC.get(Calendar.DATE) + c.getActualMaximum(Calendar.DATE) - c.get(Calendar.DATE);
                        result = d + "天前";
                    }
                }
            }
        } else {
            result = formatDate(date, SDF_yyyy_MM_dd_HH_mm);
            return result;
        }
        return result;
        //return formatDate(date, SDF_yyyy_MM_dd_HH_mm_ss_SSS) + ":" + result;
    }
    /**
     *
     * 把日期转换为yyyy-MM-dd格式字符串
     *
     * @param d 日期
     * @author wuliuhua
     * @throws ParseException
     */
    public static String formatDate(Date d, DateFormat format) {
        return d != null ? format.format(d) : "";
    }

    /**
     *
     * 根据str长度来解析日期
     *
     * @param str
     * @author wuliuhua
     * @return date
     * @throws ParseException
     */
    public static Date parseDate(String str, DateFormat format) throws ParseException{
        if (str == null){
            return null;
        }
        if (format == null){
            return null;
        }
        return format.parse(str);
    }

    /**
     *
     * 根据str长度来解析日期
     *
     * @param str
     * @author wuliuhua
     * @return date
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {
        if (str == null){
            return null;
        }

        if (str.length() == 5){
            return SDF_HH_mm.parse(str);
        }

        if (str.length() == 7){
            return SDF_YYYY_MM.parse(str);
        }

        if (str.length() == 8){
            return SDF_HH_mm_ss.parse(str);
        }

        if (str.length() == 10){
            return SDF_yyyy_MM_dd.parse(str);
        }

        if (str.length() == 16){
            return SDF_yyyy_MM_dd_HH_mm.parse(str);
        }
        if (str.length() == 19){
            return SDF_yyyy_MM_dd_HH_mm_ss.parse(str);
        }
        if (str.length() == 23){
            return SDF_yyyy_MM_dd_HH_mm_ss_SSS.parse(str);
        }
        return null;
    }

    public static String formatDateISO0861(Date d){
        String result = SDF_ISO0861.format(d);
        return result;
    }


    public static boolean isToday(Date date){
        Calendar todayCalendar = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();

        todayCalendar.setTimeInMillis(System.currentTimeMillis());
        c1.setTime(date);

        if(todayCalendar.get(Calendar.YEAR) != c1.get(Calendar.YEAR)){
            return false;
        } else if(todayCalendar.get(Calendar.MONTH) != c1.get(Calendar.MONTH)){
            return false;
        } else if(todayCalendar.get(Calendar.DAY_OF_MONTH) != c1.get(Calendar.DAY_OF_MONTH)){
            return false;
        }
        return true;
    }

    /**
     * 调试工具,显示一段代码执行耗费的时间
     *
     * 使用方法:
     *   在代码开始处showTime("code"); 在代码结束处showTime("code");
     *   如:
     *   showTime("code", this);
     *
     *   var sum : int = 0;
     *   for(var i:int=0;i<100;i++){
     *     sum += i;
     * 	 }
     *
     *   showTime("code", this);
     *
     * 注意:
     *   1、开始和结束处调用showTime的参数须一致.
     *   2、请在debug模式下使用该方法,
     *   3、结果将打印在控制台上,格式如:code:521ms
     *   4、showFlag启用开关，true:打开,false:关闭
     *   5、参数path一般用this,标识代码段所在的文件
     *
     * @author wuliuhua
     * 2013-07-15
     **/
    private static final Boolean showFlag = true;//showTime启用开关，true:打开,false:关闭
    private static final Map<String, Date> timeArray = new HashMap<String, Date>();//存放时间
    public static void showTime(String flag, Object obj){
        if(!showFlag){
            return;
        }
        String temp = flag;
        if(obj != null){
            temp = obj.getClass().getName() + " " + flag;
        }
        if(timeArray.containsKey(temp) && timeArray.get(temp) != null){
            System.out.println("showTime " + temp + " : " + (new Date().getTime() - timeArray.get(temp).getTime())+"ms");
            timeArray.remove(temp);
        } else{
            timeArray.put(temp, new Date());
        }
    }

    /**
     * 查询时把开始时间格式化成 yyyy-MM-dd 00:00:00
     * @author wangxiaobing 2013-06-05
     * @param date
     * @return
     */
    public static Date getBeginDate(Date beginDate){
        if(beginDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(beginDate)+" 00:00:00";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                //System.out.println("开始时间："+s);
                return sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 查询时把开始时间格式化成 yyyy-MM-01 00:00:00
     * @author wangxiaobing 2014-03-05
     * @param date
     * @return
     */
    public static Date getBeginMonthDate(Date beginDate){
        if(beginDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
            String s = sdf.format(beginDate)+" 00:00:00";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                //System.out.println("开始时间："+s);
                return sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 查询时把开始时间格式化成 yyyy-MM-dd 23:59:59
     * @author wangxiaobing 2013-06-05
     * @param endDate
     * @return
     */
    public static Date getEndDate(Date endDate){
        if(endDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String s = sdf.format(endDate)+" 23:59:59";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                //System.out.println("结束时间："+s);
                return sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 查询时把开始时间格式化成 yyyy-MM-01 23:59:59
     * @author wangxiaobing 2013-06-05
     * @param endDate
     * @return
     */
    public static Date getEndMonthDate(Date endDate){
        if(endDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-01");
            String s = sdf.format(endDate)+" 23:59:59";
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                //System.out.println("结束时间："+s);
                return sdf.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 查询时把开始时间格式化成 yyyy-MM-dd 00:00:00
     * @author wangxiaobing 2013-06-05
     * @param date
     * @return
     */
    public static String getBeginDateString(Date beginDate){
        if(beginDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(beginDate)+" 00:00:00";
        }
        return null;
    }
    /**
     * 查询时把开始时间格式化成 yyyy-MM-dd 23:59:59
     * @author wangxiaobing 2013-06-05
     * @param endDate
     * @return
     */
    public static String getEndDateString(Date endDate){
        if(endDate!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(endDate)+" 23:59:59";
        }
        return null;
    }


    /**
     * 计算周岁
     * @param birthDay
     * @return
     * @throws Exception
     */
    public static int getAge(Date birthDay) throws Exception {
        if(birthDay == null){
            return 0;
        }
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "出生时间大于当前时间!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH)+1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow < monthBirth) {
            age--;
        } else if(monthNow == monthBirth){
            if (dayOfMonthNow < dayOfMonthBirth) {
                age--;
            }
        }
        return age;
    }
}