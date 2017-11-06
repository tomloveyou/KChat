package com.mfc.appframe.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ParseException;
import android.text.TextUtils;
import android.text.format.DateUtils;

/***
 * 日期工具类
 * 
 * @author Liruijie
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public static String getRefreshTime(Context _mContext) {
		return DateUtils.formatDateTime(_mContext, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

	}

	/**
	 * 返回指定格式的当前日期
	 * 
	 * @param _pattern
	 *            指定的日期格式 ,null默认用（yyyy-MM-dd）
	 * @return
	 */
	public static String getCurrDateTime(String _pattern) {
		SimpleDateFormat mDateFormat;
		if (TextUtils.isEmpty(_pattern)) {
			mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			mDateFormat = new SimpleDateFormat(_pattern);
		}
		return mDateFormat.format(new Date(System.currentTimeMillis()));
	}

	/**
	 * @param timeMillis
	 *            要格式化的毫秒值
	 * @param _pattern
	 *            指定的日期格式
	 * @return
	 */
	public static String getGivenDateTime(long timeMillis, String _pattern) {
		SimpleDateFormat mDateFormat;
		if (TextUtils.isEmpty(_pattern)) {
			mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		} else {
			mDateFormat = new SimpleDateFormat(_pattern);
		}
		return mDateFormat.format(new Date(timeMillis));
	}


	/***
	 * 判断给定毫秒是多少天
	 * 
	 * @param time
	 * @return
	 */
	public static int getDays(long time) {
		int leftHour = (int) (time / 1000 / 60 / 60);
		int days = leftHour / 24;
		return days;
	}

	/***
	 * 判断是否是同一天
	 * 
	 * @param _time1
	 *            时间毫秒值
	 * @param _time2
	 * @return
	 */
	public static boolean isInSameDay(long _time1, long _time2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(_time1);
		int year1 = calendar1.get(Calendar.YEAR);
		int month1 = calendar1.get(Calendar.MONTH);
		int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(_time2);
		int year2 = calendar2.get(Calendar.YEAR);
		int month2 = calendar2.get(Calendar.MONTH);
		int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
		return year1 == year2 && month1 == month2 && day1 == day2;
	}

	/***
	 * liruijie 该方法返回距离今天+/-distance天的日期
	 * 
	 * @param distance距离今天的天数
	 *            正数表示往后推几天 负数表示往前推算几天 0表示今天
	 * @return 返回yyyy-MM-dd格式的日期字符串
	 */
	public static String getDateOfDistanceToday(int distance) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = "";
		cal.set(Calendar.DATE, cal.get(Calendar.DAY_OF_MONTH) + distance);
		date = sdf.format(new Date(cal.getTimeInMillis()));
		return date;
	}

	/***
	 * liruijie 该方法返回距离给定年月日distance天的日期字符串
	 * 
	 * @param year
	 *            给定年
	 * @param month
	 *            给定月
	 * @param day
	 *            给定日
	 * @param distance
	 *            距离给定日期的天数 可为负数 表示距离给定日期之前多少天的日期
	 * @return
	 */
	public static String getDateOfDistanceGivenDay(int year, int month,
			int day, int distance) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = "";
		cal.set(Calendar.DATE, cal.get(Calendar.DAY_OF_MONTH) + distance);
		date = sdf.format(new Date(cal.getTimeInMillis()));
		return date;
	}

}
