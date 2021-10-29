package com.bog.demo.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date trim(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear(); // as per BalusC comment.
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date addOneDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear(); // as per BalusC comment.
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, +1);
		return cal.getTime();
	}

	public static Date setDayMinTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear(); // as per BalusC comment.
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date setDayMaxTime(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.clear(); // as per BalusC comment.
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}
}
