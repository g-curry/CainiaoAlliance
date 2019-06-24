package com.cainiaoalliance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * @author g_curry   2019/5/27 16:17
 */
public class DateUtil {

	// 把 unix time --> 16:17

	public static String getFormattedTime(long timeStamp) { //时间戳

		// 时间格式化，传入unix时间戳，毫秒，转化成HH:mm格式
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
		return simpleDateFormat.format(new Date(timeStamp * 1000));
	}


	public static String getFormattedDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}


	public static String getFormattedDateA(long timeStamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date(timeStamp * 1000));
	}

	// 得到系统日期
	private static Date strToDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Date();    // 今天
	}

	// 2019-5-27 星期几
	public static String getWeekDay(String date) {
		String[] weekdays = {"星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(date));
		int index = calendar.get(Calendar.DAY_OF_WEEK) - 1;    // 返回的是1-7，所以要-1
		return weekdays[index];
	}

	// 2019-5-27 月份
	public static String getDateTitle(String date) {
		String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(strToDate(date));
		int monthIndex = calendar.get(Calendar.MONTH);    // 返回的是1-7，所以要-1
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return months[monthIndex] + " " + String.valueOf(day); // 2019-5-27 --> May 11
	}

}












