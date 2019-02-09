package com.jd.smartcloudmobilesdk.demo.utils;

import android.text.TextUtils;

import com.jd.smartcloudmobilesdk.demo.R;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TimeUtils {
    public static long ONEDAYMILLIS = 24 * 60 * 60 * 1000;

    public static String getShowTimeString(Date time) {
        long currentTimeMillis = System.currentTimeMillis();
        Date now = new Date(currentTimeMillis);
        long timeDistance = currentTimeMillis - time.getTime();
        long todayStartMillis = currentTimeMillis
                - (now.getHours() * 3600 + now.getMinutes() * 60 + now
                .getSeconds()) * 1000;
        long yesterdayStartMillis = todayStartMillis - ONEDAYMILLIS;
        // if (timeDistance < 1000 * 5) {
        // return "刚刚";
        // } else if (timeDistance < 1000 * 60) {
        // return timeDistance / 1000 + "秒前";
        // } else
        if (timeDistance < 1000 * 60 * 60) {
            if (timeDistance / (1000 * 60) == 0) {
                return "刚刚";
            }
            return timeDistance / (1000 * 60) + "分钟前";
        } else if (timeDistance < 1000 * 60 * 60 * 3) {
            return timeDistance / (1000 * 60 * 60) + "小时前";
        } else if (time.getTime() > todayStartMillis) {
            return "今天 " + getSimpleDateFormat("HH:mm", time);
        } else if (time.getTime() > yesterdayStartMillis) {
            return "昨天 " + getSimpleDateFormat("HH:mm", time);
        } else {
            return getSimpleDateFormat("yyyy-MM-dd HH:mm", time);
        }
    }

    public static String getChatShowTime(String time) {
        try {
            return getShowTimeString(getDateByString("yyyy-MM-dd HH:mm:ss",
                    time));
        } catch (Exception ex) {
            return time;
        }
    }

    public static String getSimpleDateFormat(String pattern, Date time) {
        return new SimpleDateFormat(pattern).format(time);
    }

    public static Date getDateByString(String pattern, String dateStr) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);// 设置一个时间转换器
        Date d = null;
        if (dateStr != null && !"".equals(dateStr)) {
            try {
                d = sf.parse(dateStr);// 将字符串s通过转换器转换为date类型
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return d;
    }

    public static boolean isSameDay(long currentTime, long lastTime) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String last_time = sf.format(lastTime);
        String current_time = sf.format(currentTime);
        return last_time.equals(current_time);
    }

    public static int getTag(long duration_time) {
        int tag = (int) duration_time / 10000;
        if (duration_time / 1000 < 5) {
            tag = 5;
        } else if (duration_time % 10000 != 0) {
            tag = (tag + 1) * 10;
        } else {
            tag = tag * 10;
        }

        if (tag > 60) {
            tag = 70;
        }
        return tag;
    }

    public static Date afterDay(int num, String time) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(TimeUtils.getDateByString("yyyyMMddHHmmss", time));
        calendar.add(Calendar.DATE, num);
        Date createDate = calendar.getTime();
        return createDate;
    }

    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return返回短时间格式 yyyy-MM-dd
     */
    public static Date getNowDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyyMMdd HH:mm
     */
    public static String getSynchTimeString() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateShort() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getStringDateLong() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取时间 小时:分;秒 HH:mm:ss
     *
     * @return
     */
    public static String getTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     *
     * @param strDate
     * @return
     */
    public static Date strToDateLong(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss * * @param dateDate * @return
     */
    public static String dateToStrLong(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     */
    public static String dateToStr(Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }

    /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }

    /**
     * 得到现在时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 提取一个月中的最后一天
     *
     * @param day
     * @return
     */
    public static Date getLastDate(long day) {
        Date date = new Date();
        long date_3_hm = date.getTime() - 3600000 * 34 * day;
        Date date_3_hm_date = new Date(date_3_hm);
        return date_3_hm_date;
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 yyyyMMdd HHmmss
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到现在小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour;
        hour = dateString.substring(11, 13);
        return hour;
    }

    /**
     * 得到现在分钟
     *
     * @return
     */
    public static String getTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String min;
        min = dateString.substring(14, 16);
        return min;
    }

    /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     *
     * @param sformat yyyyMMddhhmmss
     * @return
     */
    public static String getUserDate(String sformat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(sformat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     */
    public static String getTwoHour(String st1, String st2) {
        String[] kk = null;
        String[] jj = null;
        kk = st1.split(":");
        jj = st2.split(":");
        if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
            return "0";
        else {
            double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1])
                    / 60;
            double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1])
                    / 60;
            if ((y - u) > 0)
                return y - u + "";
            else
                return "0";
        }
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            Date date = myFormatter.parse(sj1);
            Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     */
    public static String getPreTime(String sj1, String jj) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String mydate1 = "";
        try {
            Date date1 = format.parse(sj1);
            long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
            date1.setTime(Time * 1000);
            mydate1 = format.format(date1);
        } catch (Exception e) {
        }
        return mydate1;
    }

    /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */
    public static String getNextDay(String nowdate, String delay) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String mdate = "";
            Date d = strToDate(nowdate);
            long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24
                    * 60 * 60;
            d.setTime(myTime * 1000);
            mdate = format.format(d);
            return mdate;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 判断是否润年
     *
     * @param ddate
     * @return
     */
    public static boolean isLeapYear(String ddate) {

        /**
         * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
         * 3.能被4整除同时能被100整除则不是闰年
         */
        Date d = strToDate(ddate);
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(d);
        int year = gc.get(Calendar.YEAR);
        if ((year % 400) == 0)
            return true;
        else if ((year % 4) == 0) {
            return (year % 100) != 0;
        } else
            return false;
    }

    /**
     * 返回美国时间格式 26 Apr 2006
     *
     * @param str
     * @return
     */
    public static String getEDate(String str) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(str, pos);
        String j = strtodate.toString();
        String[] k = j.split(" ");
        return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
    }

    /**
     * 获取一个月的最后一天
     *
     * @param dat
     * @return
     */
    public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
        String str = dat.substring(0, 8);
        String month = dat.substring(5, 7);
        int mon = Integer.parseInt(month);
        if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
                || mon == 10 || mon == 12) {
            str += "31";
        } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
            str += "30";
        } else {
            if (isLeapYear(dat)) {
                str += "29";
            } else {
                str += "28";
            }
        }
        return str;
    }

    /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameWeekDates(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        if (0 == subYear) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     *
     * @return
     */
    public static String getSeqWeek() {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
        if (week.length() == 1)
            week = "0" + week;
        String year = Integer.toString(c.get(Calendar.YEAR));
        return year + week;
    }

    public static final String LOCAL_TIME_VALUE = "localTimeValue";
    public static final String LOCAL_SHOW_TIME = "localShowTime";
    public static final String LOCAL_SHOW_DATE = "localShowDate";
    public static final String LOCAL_SHOW_WEEK = "localShowWeek";
    public static final String LOCAL_TIME_SECOND = "localSecond";
    public static final String LOCAL_TIME_MINUTE = "localMinute";
    public static final String LOCAL_TIME_HOUR = "localHour";
    public static final String LOCAL_TIME_WEEK = "localWeeK";
    public static final String LOCAL_DATE_DAY = "localDay";
    public static final String LOCAL_DATE_MONTH = "localMonth";
    public static final String LOCAL_DATE_YEAR = "localYear";

    public static Map getShowTitleWithTimeValue(String timeValue) {
        Map<String, String> map = new HashMap<>();
        if (TextUtils.isEmpty(timeValue)) {
            return null;
        }

        /**
         * 时间表达式 e    | Z    | S | M | H | w | d | m | y | startcmd    | stopcmd
         使能 |时区 |秒| 分| 时| 周| 日| 月| 年| 开始时间指令| 结束时间指令
         */
        String[] array = timeValue.split("\\|");
        if (array.length < 11) {
            return null;
        }

        map.put(LOCAL_TIME_SECOND, array[2]);
        map.put(LOCAL_TIME_MINUTE, array[3]);
        map.put(LOCAL_TIME_HOUR, array[4]);
        map.put(LOCAL_TIME_WEEK, array[5]);
        map.put(LOCAL_DATE_DAY, array[6]);
        map.put(LOCAL_DATE_MONTH, array[7]);
        map.put(LOCAL_DATE_YEAR, array[8]);

        if (!TextUtils.isEmpty(array[5]) && !"*".equals(array[5])) {
            String weekShow = showWeekValueWithString(array[5]);
            map.put(LOCAL_SHOW_WEEK, weekShow);
        }

        if (!"*".equals(array[6]) && !"*".equals(array[7]) && !"*".equals(array[8])) {
            String dateShow = array[8] + "." + array[7] + "." + array[6];
            map.put(LOCAL_SHOW_DATE, dateShow);
        }

        if (!TextUtils.isEmpty(array[3]) && !TextUtils.isEmpty(array[4])) {
            String showTime = String.format("%s:%s", array[4], array[3]);
            map.put(LOCAL_SHOW_TIME, showTime);
        }

        return map;
    }

    public static HashMap generateLocalTimeWithTime(String timeDate, ArrayList<String> weekArray) {

        String showTime;
        String showWeek;
        String timeValue = "";
        HashMap<String, String> map = new HashMap<>();
        String[] timeArray = timeDate.split(":");
        String[] selectArray = getStringDateShort().split("-");
        int minuteSlt = Integer.parseInt(timeArray[1]);
        int hourSlt = Integer.parseInt(timeArray[0]);
        int daySlt = Integer.parseInt(selectArray[2]);
        int mouthSlt = Integer.parseInt(selectArray[1]);

        showTime = String.format("%02d:%02d", hourSlt, minuteSlt);
        map.put(LOCAL_SHOW_TIME, showTime);

        if (weekArray != null && weekArray.size() > 0) {
            String weekDay = weekArray.get(0);
            for (int i = 1; i < weekArray.size(); i++) {
                weekDay = weekDay + "," + weekArray.get(i);
            }
            showWeek = showWeekValueWithString(weekDay);
            map.put(LOCAL_SHOW_WEEK, showWeek);
            timeValue = String.format("1|+8|0|%d|%d|%s|*|*|*|*|*", minuteSlt, hourSlt, weekDay);
            map.put(LOCAL_TIME_VALUE, timeValue);
            return map;
        } else {

            //不含星期，不含日期，那么需要根据当前时间与所选时间对比，如果比当前时间大，则日期为当天的日期，如果比当前时间小，则是第二天的日期
            String[] currentArray = getStringDateLong().split("-");
            int minuteCur = Integer.parseInt(currentArray[4]);
            int hourCur = Integer.parseInt(currentArray[3]);
            int dayCur = Integer.parseInt(currentArray[2]);
            int mouthCur = Integer.parseInt(currentArray[1]);
            int yearCur = Integer.parseInt(currentArray[0]);

//            String weekDay;
            if (hourCur > hourSlt) {
//                weekDay = String.format("*|%d|%d", dayCur, mouthCur);
                timeValue = String.format("1|+8|0|%d|%d|*|%d|%d|%d|*|*", minuteSlt, hourSlt, dayCur, mouthCur, yearCur);
            } else if (hourSlt == hourCur) {
                //判断分钟
                //当时间小时数相等，则当所选的时间大于当前时间，显示日期为今天
                if (minuteCur < minuteSlt) {
                    timeValue = String.format("1|+8|0|%d|%d|*|%d|%d|%d|*|*", minuteSlt, hourSlt, daySlt, mouthSlt, yearCur);

                } else {

                    timeValue = String.format("1|+8|0|%d|%d|*|%d|%d|%d|*|*", minuteSlt, hourSlt, dayCur, mouthCur, yearCur);
                }

            } else if (hourCur < hourSlt) {
                timeValue = String.format("1|+8|0|%d|%d|*|%d|%d|%d|*|*", minuteSlt, hourSlt, daySlt, mouthSlt, yearCur);
            }

            map.put(LOCAL_TIME_VALUE, timeValue);
            return map;
        }
    }

    public static String showWeekValueWithString(String weekValue) {

        String[] weekArr = weekValue.split(",");
        ArrayList<String> weekMutArr = new ArrayList<>();
        for (int i = 0; i < weekArr.length; i++) {
            weekMutArr.add(weekArr[i]);
        }
        String showString = "";

        if (weekMutArr.contains("0") && weekMutArr.contains("1") && weekMutArr.contains("2") && weekMutArr.contains("3")
                && weekMutArr.contains("4") && weekMutArr.contains("5") && weekMutArr.contains("6")) {
            showString = R.mipmap.everyday + "";
            return showString;
        }

        if (weekMutArr.contains("0") && weekMutArr.contains("6")) {
            showString = R.mipmap.weekend + "";
            weekMutArr.remove("0");
            weekMutArr.remove("6");
        }

        if (weekMutArr.contains("1") && weekMutArr.contains("2") && weekMutArr.contains("3")
                && weekMutArr.contains("4") && weekMutArr.contains("5")) {
            showString = R.mipmap.weekday + "";
            weekMutArr.remove("1");
            weekMutArr.remove("2");
            weekMutArr.remove("3");
            weekMutArr.remove("4");
            weekMutArr.remove("5");
        }

        int[] weekArray = new int[]{R.mipmap.sunday, R.mipmap.monday, R.mipmap.tuesday, R.mipmap.wednesday,
                R.mipmap.thursday, R.mipmap.friday, R.mipmap.saturday};
        for (int i = 0; i < weekMutArr.size(); i++) {
            if (TextUtils.isEmpty(showString)) {
                showString = weekArray[Integer.parseInt(weekMutArr.get(i))] + "";
            } else {
                showString = showString + "," + weekArray[Integer.parseInt(weekMutArr.get(i))];
            }

        }

        return showString;
    }

}
