package com.mfc.appframe.utils.datetimpick.Utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hefuyi on 16/8/4.
 */
public class DateUtils {

    public static boolean isToday(String date) {
        String today = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Calendar.getInstance().getTime());
        return today.equals(date);
    }
    /**
     * 获取系统时间：xxxx-xx-xx xx:xx
     */
    public static String getStartTimeandHHMM() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year-2 + "-" + month + "-" + day+ " " + hour + ":" + minute;

    }

	/**
	 * 将时间转为标准的时间格式
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeStyle(String time) {
		String lastheadresult = null;
		String headresult = time.substring(0, 2);
		String endresult = time.substring(2, time.length());
		lastheadresult = headresult + ":" + endresult;
		return lastheadresult;
	}
	   /**
		 * 当地时间 ---> UTC时间
		 * 
		 * @return
		 */
		@SuppressLint("SimpleDateFormat")
		public static String getHHMMTime(long time) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			String gmtTime = sdf.format(time);
			return gmtTime;
		}
    /**
	 * 当地时间 ---> UTC时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String Local2UTC(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		sdf.setTimeZone(TimeZone.getTimeZone("gmt"));
		String gmtTime = sdf.format(getDateSS(true,time));
		return gmtTime;
	}
/**
 * 将时间字符串转Date类型
 * @param haveHHmm 是否包含时分
 * @param time String类型的时间格式
 * @return Date类型的时间格式
 */
	public static Date getDateSS(Boolean haveHHmm,String time) {
		SimpleDateFormat df = new SimpleDateFormat(haveHHmm?"yyyy-MM-dd HH:mm":"yyyy-MM-dd");
		Date date = null;
		try {
			date = df.parse(time);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
	 /**
     * 当天稍后2小时
     *
     * @return
     */
    public static String getNowTwoLaterDate() {
        String mYear;
        String mMonth;
        String mDay;
        String hour;
        String minute;
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY) + 2);
        minute = String.valueOf(c.get(Calendar.MINUTE));
        return mYear + "-" + mMonth + "-" + mDay + " " + hour + ":" + minute;
    }
/**
 * 时间比较
 * @param d1 时间1
 * @param d2 时间2
 * @return true 时间1在时间2之后 false 时间1在时间2之前
 */
    public static boolean compareDate(Date d1, Date d2) {
        if (d1.getTime() > d2.getTime()) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 可选最晚日期
     */
    public static String getEndTimeandHHMM() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return year+2+ "-" + month + "-" + day + " " + hour + ":" + minute;

    }
    public static String getMainPageDate(String date) {
        String mDate = "";
        try {
            Date tmpDate = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(date);
            mDate = DateFormat.getDateInstance(DateFormat.FULL).format(tmpDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String thisYear = Calendar.getInstance().get(Calendar.YEAR) + "年";
        if (mDate.startsWith(thisYear)) {
            return mDate.replace(thisYear, "");
        }
        return mDate;
    }


    /**
     * 系统单年月日
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPresentSystime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 设置日期格式
        String a = df.format(new Date());
        return a;
    }

    /**
     * 获取系统时间：xxxx-xx-xx xx:xx
     */
    public static String getStartTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year - 10 + "-" + month + "-" + day + " " + hour + ":" + minute;
    }

    /**
     * 获取系统时间：xxxx-xx-xx xx:xx
     */
    public static int getHourTime() {
        Calendar calendar = Calendar.getInstance();
        return  calendar.get(Calendar.HOUR_OF_DAY);
    }
    /**
     * 获取系统时间：xxxx-xx-xx xx:xx
     */
    public static int getMinuteTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 可选最晚日期
     */
    public static String getEndTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute;

    }
}
