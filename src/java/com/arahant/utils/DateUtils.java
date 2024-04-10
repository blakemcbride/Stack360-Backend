/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
 */
package com.arahant.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Blake McBride (and utterly molested by others; corrections are ongoing)
 */
public class DateUtils {

	private static final String DATE_FORMAT_MM_DD_YY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/(\\d\\d)";
	private static final String DATE_FORMAT_MM_DD_YYYY = "(0?[1-9]|1[012])/(0?[1-9]|[12][0-9]|3[01])/(\\d\\d\\d\\d)";
	private static final String DATE_FORMAT_YYYY_MM_DD = "(\\d\\d\\d\\d)-(\\d?\\d)-(\\d?\\d)";
	private static final String TIME_FORMAT_12 = "[01 ]?[0-9]:[0-5][0-9] *[aApP][mM]";
	private static final String TIME_FORMAT_24 = "[012 ]?[0-9]:[0-5][0-9]";
	private static final String TIME_SEC_FORMAT_12 = "[01 ]?[0-9]:[0-5][0-9]:[0-5][0-9] *[aApP][mM]";
	private static final String TIME_SEC_FORMAT_24 = "[012 ]?[0-9]:[0-5][0-9]:[0-5][0-9]";
	
	
	private static int guessYear(int y) {
		if (y >= 100)
			return y;
		int currentYear = year(today());
		if (y + 2000 > currentYear + 10)
			return 1900 + y;
		else
			return 2000 + y;
	}
	
	/**
	 * Creates an int date from year, month, day components.
	 * 
	 * @param y 4 digit year - will guess if 2 digit year
	 * @param m
	 * @param d
	 * @return an int representing a date in the format YYYYMMDD
	 */
	public static int create(int y, int m, int d) {
		y = guessYear(y);	
		return y * 10000 + m * 100 + d;
	}
	
	/**
	 * Returns integer representation of day of week.
	 * 1=Sun, 2=Mon, 3=Tue, 4=Wed, 5=Thu, 6=Fri, 7=Sat
	 * 
	 * @param date
	 * @return integer 1-7 representing the day of the week
	 */
	public static int getDayOfWeek(int date) {
		Calendar cal = getCalendar(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns three character day-of-week.
	 * 
	 * 
	 * @param date YYYYMMDD format
	 * @return 
	 */
	public static String getDayOfWeekAbbreviated(int date) {
		switch (getDayOfWeek(date)) {
			case 1:
				return "Sun";
			case 2:
				return "Mon";
			case 3:
				return "Tue";
			case 4:
				return "Wed";
			case 5:
				return "Thu";
			case 6:
				return "Fri";
			case 7:
				return "Sat";
			default:
				return "Err";
		}
	}

	private static String normalizeDate(String date) {
		int length = date.length();
		int year = guessYear(Integer.parseInt(date.substring(length - 2)));
		return date.substring(0, length - 2) + year;
	}

	/**
	 * Parses a string containing a date formatted as follows into an integer representation of that date.
	 *     MM/DD/YY
	 *     MM/DD/YYYY
	 *     MM.DD.YYYY
	 *     MM-DD-YYYY
	 * 
	 * @param date input date
	 * @return int date or 0 on error
	 */
	public static int getDate(String date) {
		try {
			date = date.replaceAll("-", "/");
			date = date.replaceAll("\\.", "/");

			if (date.matches(DATE_FORMAT_MM_DD_YYYY))
				return DateUtils.getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));
			else if (date.matches(DATE_FORMAT_MM_DD_YY))
				return DateUtils.getDate(new SimpleDateFormat("MM/dd/yyyy").parse(normalizeDate(date)));
		} catch (Exception ex) {
			return 0;
		}
		return 0;
	}
	
	/**
	 * Parses a string containing a time formatted as:
	 * hh:mm AM/PM
	 * HH:mm
	 * hh:mm:ss AM/PM
	 * HH:mm:ss
	 * 
	 * @param time
	 * @return time as an int or -1 on error
	 */
	public static int getTime(String time) {
		try {
			time = time.replaceAll("\\.", ":");
			if (time.matches(TIME_FORMAT_12)) {
				time = time.replaceAll(" ", "");  // ignore spaces brfore AM/PM
				return getTime(new SimpleDateFormat("h:mma").parse(time));
			} else if (time.matches(TIME_FORMAT_24))
				return getTime(new SimpleDateFormat("H:m").parse(time));
			else if (time.matches(TIME_SEC_FORMAT_12)) {
				time = time.replaceAll(" ", "");  // ignore spaces brfore AM/PM
				return getTime(new SimpleDateFormat("h:mm:ssa").parse(time));
			} else if (time.matches(TIME_SEC_FORMAT_24))
				return getTime(new SimpleDateFormat("H:m:ss").parse(time));
		} catch (Exception ex) {
			return -1;
		}
		return -1;		
	}

	/**
	 * returns date as yyyymmdd
	 * 
	 * @param dt YYYYMMDD
	 * @return YYYYMMDD as string
	 */
	public static String getDateCCYYMMDD(int dt) {
		return dateFormat("yyyyMMdd", dt);
	}

	/**
	 * Returns year portion of a date.
	 * 
	 * @param dt YYYYMMDD
	 * @return  YYYY
	 */
	public static int year(int dt) {
		return dt / 10000;
	}

	/**
	 * Returns month portion of a date.
	 * 
	 * @param dt YYYYMMDD
	 * @return  MM
	 */
	public static int month(int dt) {
		return (dt % 10000) / 100;
	}

	/**
	 * Returns day portion of a date.
	 * 
	 * @param dt YYYYMMDD
	 * @return  DD
	 */
	public static int day(int dt) {
		return dt % 100;
	}

	public static int getYear(int dt) {
		return dt / 10000;
	}

	public static int getMonth(int dt) {
		return (dt % 10000) / 100;
	}

	public static int getDay(int dt) {
		return dt % 100;
	}

	/**
	 * Returns the current date in the form YYYYMMDD. This is the same as now().
	 *
	 * @return
	 */
	public static int today() {
		Calendar c = Calendar.getInstance();
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);
		return y * 10000 + m * 100 + d;
	}

	/**
	 * Converts an integer date to Java Date object
	 * 
	 * @param datint  YYYYMMDD
	 * @return 
	 */
	public static Date getDate(final int datint) {
		if (datint <= 0)
			return new java.sql.Date(new Date().getTime());

		final int day = datint % 100;
		final int month = (datint % 10000 - day) / 100;

		final int year = datint / 10000;

		final Calendar c = Calendar.getInstance();
		c.clear();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1);
		c.set(Calendar.DAY_OF_MONTH, day);

		return new java.sql.Date(c.getTime().getTime());
	}

	/**
	 * Changes date data type from Date to int.
	 * 
	 * @param dat YYYYMMDD
	 * @return 
	 */
	public static int getDate(final Date dat) {
		if (dat == null)
			return 0;

		final Calendar cal = Calendar.getInstance();
		cal.setTime(dat);

		return cal.get(Calendar.DAY_OF_MONTH) + ((cal.get(Calendar.MONTH) + 1) * 100) + ((cal.get(Calendar.YEAR)) * 10000);
	}

	/**
	 * Returns the minutes portion of the time unrelated to the hours.
	 * 
	 * @param time HHMMSSmmm
	 * @return 
	 */
	public static int getMinutes(int time) {
		if (time <= 0)
			return 0;

		//discard millis
		time /= 1000;

		//discard seconds
		time /= 100;

		//get minutes
		return time % 100;
	}
	
	/**
	 * Return total minutes since midnight taking into account the hours.
	 * 
	 * @param time HHMMSSmmm
	 * @return 
	 */
	public static int getTotalMinutes(int time) {
		if (time <= 0)
			return 0;

		//discard millis
		time /= 1000;

		//discard seconds
		time /= 100;

		//get minutes
		int minutes = time % 100;
		int hours = time / 100;
		return hours * 60 + minutes;
	}

	/**
	 * Returns the number of hours in an integer time.
	 * 
	 * 
	 * @param time  HHMMSSmmm
	 * @return 
	 */
	public static int getHour(int time) {
		if (time == -1 || time < 0)
			return 0;

		if (time == 0)
			return 0;

		//discard millis
		time /= 1000;

		//discard seconds
		time /= 100;

		//get hours
		return time / 100;
	}
	
	/**
	 * Calculate difference in hours.
	 * 
	 * @param date1 beginning date YYYYMMDD
	 * @param time1 beginning time HHMMSSmmm
	 * @param date2 ending date YYYYMMDD
	 * @param time2 ending time HHMMSSmmm
	 * @return 
	 */
	public static double differenceInHours(int date1, int time1, int date2, int time2) {
		Date d1 = getDate(date1, time1);
		Date d2 = getDate(date2, time2);
		final double MILLI_TO_HOUR = 1000 * 60 * 60;
		return ((double)d2.getTime() - (double)d1.getTime()) / MILLI_TO_HOUR;
	}

	/**
	 * Returns a Date build from the integer date and time
	 * 
	 * @param date YYYYMMDD
	 * @param time HHMMSSmmm
	 * @return 
	 */
	public static Date getDate(int date, int time) {
		if (date == 0)
			return null;

		Date d = getDate(date);

		Calendar cal = getCalendar(date);

		cal.set(Calendar.MILLISECOND, time % 10000);
		time /= 1000;

		cal.set(Calendar.SECOND, time % 100);

		//discard seconds
		time /= 100;

		//get minutes
		cal.set(Calendar.MINUTE, time % 100);

		time /= 100;

		cal.set(Calendar.HOUR_OF_DAY, time);

		return cal.getTime();
	}

	/**
	 * 
	 * @param date
	 * @return as an int HHMMSSmmm
	 */
	public static int getTime(Date date) {
		if (date == null)
			return -1;

		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return getTime(cal);
	}

	/**
	 * @param time  HHMMSSMMM
	 * @return
	 */
	public static String getTimeFormatted(int time) {
		String ret = "";
		try {
			if (time < 0)
				return "";

			//discard millis
			time /= 1000;

			//discard seconds
			time /= 100;

			//get minutes
			final int min = time % 100;

			String meridian = time < 2400 && time > 1159 ? "pm" : "am";

			//get hours
			int hours = time / 100;

			if (hours == 0)
				hours = 12;
			else if (hours > 12)
				hours -= 12;

			if (hours < 10)
				ret += " "; //formatting to line up columns

			String minutes = (min < 10 ? "0" : "") + min;

			ret += hours + ":" + minutes + " " + meridian;

		} catch (final RuntimeException e) {
			ret = "Invalid Time";
		}

		return ret;
	}

	public static String dateFormat(String fmt, Date date) {
		if (date == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat(fmt);
		return df.format(date);
	}

	public static String dateFormat(String fmt, int date) {
		if (date < 1)
			return "";
		return dateFormat(fmt, getDate(date));
	}

	public static String dateFormat(String fmt, Calendar date) {
		if (date == null)
			return "";
		return dateFormat(fmt, date.getTime());
	}

	/**
	 * Returns date formatted as mm/dd/yyyy
	 * 
	 * @param workDate
	 * @return
	 */
	public static String getDateFormatted(final int workDate) {
		return dateFormat("MM/dd/yyyy", workDate);
	}

	/**
	 * Return date/time formatted as yyyy-mm-ss hh:mm:ss
	 * 
	 * @param workDate
	 * @return
	 */
	public static String getDateTimeFormatted(final Date workDate) {
		return dateFormat("yyyy-MM-dd HH:mm:ss", workDate);
	}

	/**
	 * Returns date formateed as mm/dd/yyyy h:mm AM/PM"
	 * 
	 * @param workDate
	 * @return 
	 */
	public static String getDateAndTimeFormatted(final Date workDate) {
		return dateFormat("MM/dd/yyyy h:mm a", workDate);
	}

	public static Date getSixtyDaysAgo() {
		final Date sixtyDaysBack = new Date();
		sixtyDaysBack.setTime(sixtyDaysBack.getTime() - (60 * 24 * 60 * 60 * 1000));
		return sixtyDaysBack;
	}

	public static Date getYesterday() {
		final Date sixtyDaysBack = new Date();
		sixtyDaysBack.setTime(sixtyDaysBack.getTime() - (1 * 24 * 60 * 60 * 1000));
		return sixtyDaysBack;
	}

	/**
	 * @param dat
	 * @return
	 */
	public static long getDays(final int dat) {
		return DateUtils.getDate(dat).getTime() / 1000 / 60 / 60 / 24 + 1;
	}

	public static String daysToYearMonth(final long days) {
		//all I have to go on are days
		//so I'll make a "generic" month
		final double monthLength = 365.25 / 12;

		long months = Math.round(days / monthLength);

		final long years = months / 12;
		months -= 12 * years;

		String m = months + "";

		while (m.length() < 2)
			m = "0" + m;

		return years + "/" + m;
	}

	public static int getDaysFromNow(int days) {
		return addDays(now(), days);
	}

	/**
	 * Returns the current date in the form YYYYMMDD. This is the same as
	 * today().
	 *
	 * @return
	 */
	public static int now() {
		return getDate(new Date());
	}

	/**
	 * @param eventDate
	 * @return
	 */
	public static Calendar getCalendar(final int eventDate) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(getDate(eventDate));
		return cal;
	}

	/**
	 * @return current date/time as a Calendar object
	 */
	public static Calendar getNow() {
		return Calendar.getInstance();
	}

	/**
	 * @param date
	 * @return
	 */
	public static int getDate(final Calendar date) {
		return getDate(date.getTime());
	}

	/**
	 * @param dateReported
	 * @param timeReported
	 * @return
	 */
	public static String getDateTimeFormatted(final int dateReported, final int timeReported) {
		return getDateFormatted(dateReported) + " " + getTimeFormatted(timeReported);
	}

	/**
	 * @param date
	 * @param i
	 * @return
	 */
	public static int addDays(final int date, final int i) {
		if (date == 0)
			return date;
		final Calendar cal = getCalendar(date);
		cal.add(Calendar.DAY_OF_YEAR, i);
		return getDate(cal);
	}

	public static int addMonths(final int date, final int i) {
		if (date == 0)
			return date;
		final Calendar cal = getCalendar(date);
		cal.add(Calendar.MONTH, i);
		return getDate(cal);
	}

	public static int addYears(final int date, final int i) {
		if (date == 0)
			return date;
		final Calendar cal = getCalendar(date);
		cal.add(Calendar.YEAR, i);
		return getDate(cal);
	}

	public static int getStartOfMonthAfter(final int startDate, final int i) {
		int date = addDays(startDate, i);

		final Calendar cal = getCalendar(date);

		if (cal.get(Calendar.DAY_OF_MONTH) != 1) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
		}

		return getDate(cal);
	}

	public static int endOfMonth(final int date) {
		final Calendar cal = getCalendar(date);
		if (cal.get(Calendar.DAY_OF_MONTH) != 1) {
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		return getDate(cal);
	}

	/**
	 * returns date formatted like: January 3, 2014
	 * 
	 * @param i
	 * @return
	 */
	public static String getDateSpelledOut(final int i) {
		return dateFormat("MMMMM d, yyyy", i);
	}

	/**
	 * Returns current time as HHMMSSmmm
	 */
	public static int nowTime() {
		return getTime(getNow());
	}

	public static int nowTimeToMinute() {
		return (getTime(getNow()) / 100000) * 100000;
	}

	public static int nowTimeToMinuteWithTimeZone(int machineTimeZone) {
		return (getTimeWithTimeZone(getNow(), machineTimeZone) / 100000) * 100000;
	}

	/**
	 * @param date
	 * @param i
	 * @return
	 */
	public static int add(final int date, final int i) {
		final Calendar cal = getCalendar(date);
		cal.add(Calendar.DAY_OF_YEAR, i);
		return getDate(cal);
	}

	/**
	 * returns date formatted as mm/dd/yyyy
	 * 
	 * @param createDate
	 * @return
	 */
	public static String getDateFormatted(Date createDate) {
		return dateFormat("MM/dd/yyyy", createDate);
	}

	/**
	 * Returns HHMMSSmmm
	 * 
	 * @param cal
	 * @return
	 */
	public static int getTime(Calendar cal) {
		final int hour = cal.get(Calendar.HOUR_OF_DAY);
		final int min = cal.get(Calendar.MINUTE);
		final int sec = cal.get(Calendar.SECOND);
		final int millis = cal.get(Calendar.MILLISECOND);

		return (((((hour * 100) + min) * 100) + sec) * 1000) + millis;
	}

	public static int getTimeWithTimeZone(Calendar cal, int machineTimeZone) {
		//machineTimeZone - serverTimeZone (add difference in minutes to time)
		if (machineTimeZone != 2000)
			cal.add(Calendar.MINUTE, machineTimeZone - getTimeZoneOffset()); //			System.out.println("Adjusting time by " + (machineTimeZone - getTimeZoneOffset() + " minutes."));
		final int hour = cal.get(Calendar.HOUR_OF_DAY);
		final int min = cal.get(Calendar.MINUTE);
		final int sec = cal.get(Calendar.SECOND);
		final int millis = cal.get(Calendar.MILLISECOND);

		return (((((hour * 100) + min) * 100) + sec) * 1000) + millis;
	}

	//public static void main(String args[]) {
	//System.out.println(nowTimeToMinute());
	//   System.out.println(getDate(20081101, 105351000));
	//System.out.println(DateUtils.getDateCCYYMMDD(addDays(20060501, 1)));

	/*
	 Calendar n=getNow();
	 System.out.println(n.getTimeZone());
	 System.out.println(getTime(n));
	 System.out.println(getTimeFormatted(getTime(n)));
	 n.add(Calendar.HOUR_OF_DAY, 12);
	 System.out.println(getTime(n));
	 System.out.println(getTimeFormatted(getTime(n)));
	 */
	//}
	/**
	 * @param dat
	 * @return
	 */
	public static long getDaysSince(int dat) {
		return julian(today()) - julian(dat);
	}

	public static long getDaysBetween(int dat1, int dat2) {
		return julian(dat1) - julian(dat2);
	}

	/*
	 * Can't just divide getDaysBetween because of leap years.
	 *
	 * Note: this returns full years!
	 */
	public static long getYearsBetween(int dat1, int dat2) {
		if (dat2 < dat1) {
			int s = dat2;
			dat2 = dat1;
			dat1 = s;
		}

		Calendar cal1 = getCalendar(dat1);
		Calendar cal2 = getCalendar(dat2);

		int ret = 0;

		do {
			cal1.add(Calendar.YEAR, 1);
			if (cal1.before(cal2))
				ret++;
		} while (cal1.before(cal2));

		return ret;
	}

	/*
	 * Can't just divide getDaysBetween because of leap years.
	 *
	 * Note: this returns full months!
	 */
	public static long getMonthsBetween(int dat1, int dat2) {

		if (dat2 < dat1) {
			int s = dat2;
			dat2 = dat1;
			dat1 = s;
		}

		Calendar cal1 = getCalendar(dat1);
		Calendar cal2 = getCalendar(dat2);

		int ret = 0;

		do {
			cal1.add(Calendar.MONTH, 1);
			if (cal1.before(cal2))
				ret++;
		} while (cal1.before(cal2));

		return ret;
	}
	
	/**
	 * Returns the number of minutes between two dates and times.
	 * If both begDate and begTime are zero only the times will be used.
	 * 
	 * @param begDate YYYYMMDD or 0
	 * @param begTime HHMMSSmmm
	 * @param endDate YYYYMMDD or 0
	 * @param endTime HHMMSSmmm
	 * @return 
	 */
	public static int getMinutesBetween(int begDate, int begTime, int endDate, int endTime) {
		if (endDate < begDate  ||  endDate == begDate  &&  endTime < begTime)
			return 0;
		if (endDate == begDate)
			return getTotalMinutes(endTime) - getTotalMinutes(begTime);
		int days = (int) getDaysBetween(endDate, begDate);
		int res = (days-1) * (24 * 60);
		res += getTotalMinutes(240000000) - getTotalMinutes(begTime);
		res += getTotalMinutes(endTime);
		return res;
	}

	public static double getHours(double amount, int unit) {
		switch (unit) {
			case 1: // hours
				return amount;
			case 2: // days
				return amount * 8;
			case 3: // weeks
				return amount * 40;
			case 4: //months
				return amount * 160;
			case 5: //years
				return amount * 2080;
			default:
				return amount;
		}
	}

	public static double getMeasuredValue(double amount, int unit) {
		switch (unit) {
			case 1: // hours
				return amount;
			case 2: // days
				return amount / 8;
			case 3: // weeks
				return amount / 40;
			case 4: //months
				return amount / 160;
			case 5: //years
				return amount / 2080;
			default:
				return amount;
		}
	}

	public static int getMeasurement(double hours) {
		if (hours < 4)
			return 1;
		if (hours >= 4 && hours < 20)
			return 2;
		if (hours >= 20 && hours < 160)
			return 3;
		if (hours >= 160 && hours < 2080)
			return 4;
		if (hours >= 2080)
			return 5;
		return 0;
	}

	public static double getMeasuredValue(double hours) {
		if (hours < 4)
			return hours;
		if (hours >= 4 && hours < 20)
			return hours / 8;
		if (hours >= 20 && hours < 160)
			return hours / 40;
		if (hours >= 160 && hours < 2080)
			return hours / 160;
		if (hours >= 2080)
			return hours / 2080;
		return 0;
	}

	public static int getTimeZoneOffset() {
		Calendar c = Calendar.getInstance();
		TimeZone tz = c.getTimeZone();

		//if we are currently in daylight savings time
		int ret = tz.getRawOffset();
		if (tz.inDaylightTime(new Date()))
			//add in the DTS factor
			ret += tz.getDSTSavings();
		ret = ret / 60000;
		//System.out.println("TimeZone: " + tz.getDisplayName());
		//System.out.println(ret);

		return ret;
	}

	public static int getWorkDaysBetween(int start, int end) {
		int workDays = 0;
		Calendar startCal = getCalendar(start);
		Calendar endCal = getCalendar(end);

		while (startCal.before(endCal) || startCal.equals(endCal)) {
			int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
			if (dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY)
				workDays++;
			startCal.add(Calendar.DAY_OF_YEAR, 1);
		}

		return workDays;
	}
	
	public static int setDay(int date, int day) {
		int y = year(date);
		int m = month(date);
		return create(y, m, day);
	}
	
	public static int setMonth(int date, int month) {
		int y = year(date);
		int d = day(date);
		return create(y, month, d);
	}
	
	public static int setYear(int date, int year) {
		int m = month(date);
		int d = day(date);
		return create(year, m, d);
	}

	/**
	 * Parses a string containing a date formatted as follows into an integer representation of that date.
	 * <br><br>
	 * <code>
	 *     MM/DD/YY
	 *     MM/DD/YYYY
	 *     MM.DD.YYYY
	 *     YYYY-MM-DD
	 * </code>
	 * @param date input date
	 * @return int date or 0 on error
	 */
	public static int parse(String date) {
        if (date == null  ||  date.trim().isEmpty())
            return 0;
        try {
			date = date.replaceAll("\\.", "/");

			if (date.matches(DATE_FORMAT_YYYY_MM_DD))
				return getDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			else if (date.matches(DATE_FORMAT_MM_DD_YYYY))
				return getDate(new SimpleDateFormat("MM/dd/yyyy").parse(date));
			else if (date.matches(DATE_FORMAT_MM_DD_YY))
				return getDate(new SimpleDateFormat("MM/dd/yyyy").parse(normalizeDate(date)));
		} catch (Exception ex) {
			return 0;
		}
		return 0;
	}

    /**
     * Returns true if the dates overlap.
     * Each input also takes a zero if unknown.
     *
     * @param sa beginning date 1
     * @param ea ending date 1
     * @param sb beginning date 2
     * @param eb ending date 2
     * @return
     */
	public static boolean overlap(int sa, int ea, int sb, int eb) {
	    int a=0, b=0;
	    if (sa == 0 && ea == 0 && sb == 0 && eb == 0)
	        return false;
        if (sa == 0  &&  ea != 0  ||  sa != 0 && ea == 0)
            a = sa + ea;
        if (sb == 0  &&  eb != 0  ||  sb != 0 && eb == 0)
            b = sb + eb;
        if (a != 0  &&  b != 0)
            return a == b;
        if (a == 0  &&  b != 0)
            return b >= sa  &&  b <= ea;
        if (a != 0 )
            return a >= sb  &&  a <= eb;
        return sa <= eb && ea >= sb;
	}

	/**
	 * Convert a date into the number of days since a certain date (julian date)
	 *
	 * @param dt YYYYMMDD
	 * @return number of days since some early start date
	 */
	public static long julian(int dt) {
		/* This can't be done some of the more obvious ways because of changes in daylight savings time.  */
		long d, y, m;

		if (dt <= 0)
			return dt;
		y = dt / 10000L;
		m = (dt % 10000L) / 100L;
		d = dt % 100L;
		d += (long) (.5 + (m - 1L) * 30.57);
		if (m >	2L) {
			d--;
			if (0L != y % 400L && (0L != y % 4L || 0L == y % 100L))
				d--;
		}
		d += (long) (365.25 * --y);
		d += y / 400L;
		d -= y / 100L;
		return d;
	}

	/**
	 * Convert a julian date back into a standard date.
	 *
	 * @param d
	 * @return YYYYMMDD
	 */
	public static long calendar(long d)
	{
		long	y, m, t;

		if (d <= 0L)
			return d;
		y = (long)(1.0 + d / 365.2425);
		t = y -	1L;
		d -= (long) (t * 365.25);
		d -= t / 400L;
		d += t / 100L;
		if (d >	59L  &&	 0L != y % 400L	 &&  (0L != y %	4  ||  0L == y % 100L))
			d++;
		if (d >	60L)
			d++;
		m = (long)((d + 30L) / 30.57);
		d -= (long) Math.floor(.5 + (m - 1L) * 30.57);
		if (m == 13)  {
			m = 1;
			++y;
		}  else  if (m == 0L)  {
			m = 12;
			--y;
		}
		return 10000L * y + m * 100L + d;
	}

	public static void main(String[] args) {

		System.out.println(julian(20210227));
		System.out.println(julian(20210228));
		System.out.println(julian(20210301));
		System.out.println(julian(20210302));

		/*
		System.out.println(getDaysSince(20190316));
		System.out.println(getDaysSince(20190315));
		System.out.println(getDaysSince(20190314));
		System.out.println(getDaysSince(20190313));
*/
		/*
		System.out.println(getTimeFormatted(0));
		System.out.println(getTimeFormatted(  3600000));
		System.out.println(getTimeFormatted(120000000));
		System.out.println(getTimeFormatted(145600000));
		System.out.println(getTimeFormatted( 20300000));
		*/
		//int d = getDate("6/8/59");
		//System.out.println(differenceInHours(19590608, 2300 * 100000, 19590609, 115 * 100000));
	}
//	  public static void main(String[] args) {
//	  Date date = new Date();
//	  int x = date.getTimezoneOffset();
//	  DateFormat firstFormat = new SimpleDateFormat();
//	  DateFormat secondFormat = new SimpleDateFormat();
//	  TimeZone firstTime = TimeZone.getTimeZone(new Date().toString());
//	  TimeZone secondTime = TimeZone.getTimeZone(new Date().toString());
//	  firstFormat.setTimeZone(firstTime);
//	  secondFormat.setTimeZone(secondTime);
//	  System.out.println("-->"+new Date().toString()+": " + firstFormat.format(date) + " (" + x + ")");
//	  System.out.println("-->"+new Date().toString()+": " + secondFormat.format(date) + " (" + x + ")");
//	}
}
