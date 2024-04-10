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

import java.util.Calendar;
import java.util.Date;

/**
 * A set of convienient functions for using Java Dates. 
 * Java has two primary classes that are used when dealing with Date/Time values, (@link java.util.Date} and {@link java.util.Calendar} (three if you include java.sql.Date).
 * In many cases, you have a {@link java.util.Date} object, but you need to use certain functions in the {@link java.util.Calendar Calendar} class to operate on that object. This results
 * in code similar to the following for setting the year of a date object ...
 * 
 * <pre>
 * 		Date dt = getSomeDate();
 * 		Calendar cal = Calendar.getInstance();
 * 		cal.setTime(dt);
 * 		cal.set(Calendar.YEAR, year);
 * </pre>
 * 
 * Many of the static methods in this class are designed to replace this kind of code using methods like this instead....
 * 
 * <pre>
 * 		Date dt = getSomeDate();
 * 		Dates.setYear(dt, 1964);
 * </pre>
 * 
 * In addition to being terse, it also means that the programmer does not have to wade their way through the intricate API of the {@link java.util.Calendar Calendar} class 
 * or the seemingly useful but sadly deprecated API of the {@link java.util.Date Date} class.
 * 
 * @see java.util.Date
 * @see java.util.Calendar
 * 
 * Arahant
 */
public class Dates
{
	// useful constants for Javascript
	public static final int JANUARY = Calendar.JANUARY;
	public static final int FEBRUARY = Calendar.FEBRUARY;
	public static final int MARCH = Calendar.MARCH;
	public static final int APRIL = Calendar.APRIL;
	public static final int MAY = Calendar.MAY;
	public static final int JUNE = Calendar.JUNE;
	public static final int JULY = Calendar.JULY;
	public static final int AUGUST = Calendar.AUGUST;
	public static final int SEPTEMBER = Calendar.SEPTEMBER;
	public static final int OCTOBER = Calendar.OCTOBER;
	public static final int NOVEMBER = Calendar.NOVEMBER;
	public static final int DECEMBER = Calendar.DECEMBER;
	
	public static final int SUNDAY = Calendar.SUNDAY;
	public static final int MONDAY = Calendar.MONDAY;
	public static final int TUESDAY = Calendar.TUESDAY;
	public static final int WEDNESDAY = Calendar.WEDNESDAY;
	public static final int THURSDAY = Calendar.THURSDAY;
	public static final int FRIDAY = Calendar.FRIDAY;
	public static final int SATURDAY = Calendar.SATURDAY;

	private static final int MILLISECONDS_IN_SECOND = 1000;
	private static final int MILLISECONDS_IN_MINUTE = 60 * MILLISECONDS_IN_SECOND;
	private static final int MILLISECONDS_IN_HOUR = MILLISECONDS_IN_MINUTE * 60;
	private static final int MILLISECONDS_IN_DAY = MILLISECONDS_IN_HOUR * 24;
	
	/**
	 * Creates a {@link java.util.Date Date} object
	 * 
	 * @param year The year
	 * @param month The month, 0 based
	 * @param day The day of the month
	 * @return the new {@link java.util.Date Date} object
	 */
	public static Date createDate(final int year, final int month, final int day)
	{
		final Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		return cal.getTime();
	}
	
	/**
	 * Creates a {@link java.util.Date Date} object
	 * 
	 * @param year The year
	 * @param month The month, 0 based
	 * @param day The day of the month
	 * @param hours The hour of the day, 0-23
	 * @param minutes The minute of the hour, 0-59
	 * @param seconds The second of the minute, 0-59
	 * @return the new {@link java.util.Date Date} object.
	 */
	public static Date createDate(final int year, final int month, final int day, final int hours, final int minutes, final int seconds)
	{
		final Calendar cal = Calendar.getInstance();
		cal.set(year, month, day, hours, minutes, seconds);
		return cal.getTime();
	}
	
	/**
	 * Adds a number of years to a {@link java.util.Date Date} object.
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of years to add
	 * @return a new {@link java.util.Date Date} object with the number of years added.
	 */
	public static Date addYears(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.YEAR);
	}
	
	/**
	 * Adds a number of months to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of months to add. This value may cause the year to increment many times. For example, you may pass 36 to this method instead of passing 3 to (@link #addYears(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of months added.
	 */
	public static Date addMonths(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.MONTH);
	}
	
	/**
	 * Adds a number of weeks to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of weeks to add. This value may cause the month or year to increment many times. For example, you may pass 52 to this method instead of passing 1 to (@link #addYears(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of weeks added.
	 */
	public static Date addWeeks(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * Adds a number of days to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of days to add. This value may cause the month or year to increment many times. For example, you may pass 365 to this method instead of passing 1 to (@link #addYears(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of days added.
	 */
	public static Date addDays(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.DAY_OF_YEAR);
	}
	
	/**
	 * Adds a number of hours to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of hours to add. This value may cause the days, month or year to increment many times. For example, you may pass 24 to this method instead of passing 1 to (@link #addDays(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of hours added.
	 */
	public static Date addHours(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * Adds a number of minutes to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of minutes to add. This value may cause the hours, days, month or year to increment many times. For example, you may pass 1440 to this method instead of passing 1 to (@link #addDays(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of minutes added.
	 */
	public static Date addMinutes(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.MINUTE);
	}
	
	/**
	 * Adds a number of seconds to a {@link java.util.Date Date} object. 
	 * @param dt the {@link java.util.Date Date} object
	 * @param num The number of seconds to add. This value may cause the minutes, hours, days, month or year to increment many times. For example, you may pass 86400 to this method instead of passing 1 to (@link #addDays(Date, int)}
	 * @return a new {@link java.util.Date Date} object with the number of seconds added.
	 */
	public static Date addSeconds(final Date dt, final int num)
	{
		return addTime(dt, num, Calendar.SECOND);
	}
	
	private static Date addTime(Date dt, final int numUnit, final int unit)
	{
		if (dt == null)
			dt = new Date();
		
		final Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(unit, numUnit);
		return c.getTime();
	}

	/**
	 * This method determines the difference between two {@link java.util.Date Date} objects in days. A positive value means that dt1 is greater than (after) dt2. 
	 * A negative value means that dt1 is less than (before) dt2. If the difference between the two {@link java.util.Date Dates} is not an exact interval, the result is 
	 * rounded down to the nearest integer.
	 * 
	 * @param dt1 The first {@link java.util.Date Date}
	 * @param dt2 The second {@link java.util.Date Date}
	 * @return The number of days difference between the two. 
	 */
	public static double daysDiff(final Date dt1, final Date dt2)
	{
		final double days1 = dt1.getTime() / MILLISECONDS_IN_DAY;
		final double days2 = dt2.getTime() / MILLISECONDS_IN_DAY;
		
		return days1 - days2;
	}
	
	/**
	 * This method determines the difference between two {@link java.util.Date Date} objects in hours. A positive value means that dt1 is greater than (after) dt2. 
	 * A negative value means that dt1 is less than (before) dt2. If the difference between the two {@link java.util.Date Dates} is not an exact interval, the result is 
	 * rounded down to the nearest integer.
	 * 
	 * @param dt1 The first {@link java.util.Date Date}
	 * @param dt2 The second {@link java.util.Date Date}
	 * @return The number of hours difference between the two. 
	 */
	public static double hoursDiff(final Date dt1, final Date dt2)
	{
		final double days1 = dt1.getTime() / MILLISECONDS_IN_HOUR;
		final double days2 = dt2.getTime() / MILLISECONDS_IN_HOUR;
		
		return days1 - days2;
	}
	
	/**
	 * This method determines the difference between two {@link java.util.Date Date} objects in minutes. A positive value means that dt1 is greater than (after) dt2. 
	 * A negative value means that dt1 is less than (before) dt2. If the difference between the two {@link java.util.Date Dates} is not an exact interval, the result is 
	 * rounded down to the nearest integer.
	 * 
	 * @param dt1 The first {@link java.util.Date Date}
	 * @param dt2 The second {@link java.util.Date Date}
	 * @return The number of minutes difference between the two. 
	 */
	public static double minutesDiff(final Date dt1, final Date dt2)
	{
		final double days1 = dt1.getTime() / MILLISECONDS_IN_MINUTE;
		final double days2 = dt2.getTime() / MILLISECONDS_IN_MINUTE;
		
		return days1 - days2;
	}
	
	/**
	 * This method determines the difference between two {@link java.util.Date Date} objects in seconds. A positive value means that dt1 is greater than (after) dt2. 
	 * A negative value means that dt1 is less than (before) dt2. If the difference between the two {@link java.util.Date Dates} is not an exact interval, the result is 
	 * rounded down to the nearest integer.
	 * 
	 * @param dt1 The first {@link java.util.Date Date}
	 * @param dt2 The second {@link java.util.Date Date}
	 * @return The number of seconds difference between the two. 
	 */
	public static double secondsDiff(final Date dt1, final Date dt2)
	{
		final double days1 = dt1.getTime() / MILLISECONDS_IN_SECOND;
		final double days2 = dt2.getTime() / MILLISECONDS_IN_SECOND;
		
		return days1 - days2;
	}

	/**
	 * This method returns the year value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The year value of the target {@link java.util.Date Date}
	 */
	public static int getYear(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.YEAR);
	}

	/**
	 * This method sets the year of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param year the year to set.
	 */
	public static void setYear(final Date dt, final int year)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.YEAR, year);
	}
	
	/**
	 * This method returns the month value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The month value of the target {@link java.util.Date Date} as a zero based value, zero being equal to January to 11 for December.
	 */
	public static int getMonth(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.MONTH);
	}
	
	/**
	 * This method sets the month of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param month the month to set. Must be between 0 and 11 inclusive.
	 */
	public static void setMonth(final Date dt, final int month)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.MONTH, month);
	}
	
	/**
	 * This method returns the day of the month value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The day of the month value of the target {@link java.util.Date Date}. The return value will be between 1 and 31 inclusive.
	 */
	public static int getDayOfMonth(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * This method sets the day of the month of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param day the day of the month to set. Must be between 0 and 31 inclusive.
	 */
	public static void setDayOfMonth(final Date dt, final int day)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.DAY_OF_MONTH, day);
	}
	
	/**
	 * This method returns the day of the week value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The day of the week value of the target {@link java.util.Date Date}. The return value will be between 1 (Sunday) and 7 (Saturday) inclusive.
	 */
	public static int getDayOfWeek(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	/**
	 * This method returns the hour of the day value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The hour of the day value of the target {@link java.util.Date Date}. The return value will be between 0 and 23 inclusive.
	 */
	public static int getHour(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	/**
	 * This method sets the hour of the day of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param hour the hour of the day to set. Must be between 0 and 23 inclusive.
	 */
	public static void setHour(final Date dt, final int hour)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.HOUR_OF_DAY, hour);
	}

	/**
	 * This method returns the minute of the hour value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The minute of the hour value of the target {@link java.util.Date Date}. The return value will be between 0 and 59 inclusive.
	 */
	public static int getMinute(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.MINUTE);
	}
	
	/**
	 * This method sets the minute of the hour of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param minute the minute of the hour to set. Must be between 0 and 59 inclusive.
	 */
	public static void setMinute(final Date dt, final int minute)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.MINUTE, minute);
	}

	/**
	 * This method returns the second of the minute value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The second of the minute value of the target {@link java.util.Date Date}. The return value will be between 0 and 59 inclusive.
	 */
	public static int getSecond(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.SECOND);
	}
	
	/**
	 * This method sets the second of the minute of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param second the second of the minute to set. Must be between 0 and 59 inclusive.
	 */
	public static void setSecond(final Date dt, final int second)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.SECOND, second);
	}

	/**
	 * This method returns the millisecond value of the dt parameter. 
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @return The millisecond value of the target {@link java.util.Date Date}. The return value will be between 0 and 999 inclusive.
	 */
	public static int getMillisecond(Date dt)
	{
		final Calendar cal = Calendar.getInstance();
		if (dt == null)
			dt = new Date();
		
		cal.setTime(dt);
		return cal.get(Calendar.MILLISECOND);
	}
	
	/**
	 * This method sets the millisecond value of the target {@link java.util.Date Date}
	 * @param dt the target {@link java.util.Date Date} or, if null, the target date becomes the current date.
	 * @param milli the millisecond value to set. Must be between 0 and 999 inclusive.
	 */
	public static void setMillisecond(final Date dt, final int milli)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		cal.set(Calendar.MILLISECOND, milli);
	}

	/**
	 * Creates a new {@link java.util.Date Date} object with the current time. This method is identical to simply creating a new {@link java.util.Date Date} using the 
	 * default constructor
	 * 
	 * <pre>
	 * 		new Date();
	 * </pre>
	 * 
	 * However, this method is useful for Javascript where the default {@link java.util.Date Date()} constructor is not available. 
	 * @return a new {@link java.util.Date Date} object with the current time.
	 */
	public static Date now()
	{
		return new Date();
	}

	/**
	 * Gets the current time in milliseconds.
	 * 
	 * @return The current time.
	 */
	public static long getTime()
	{
		return now().getTime();
	}
	
	/**
	 * Tests to see if the target {@link java.util.Date Date} is today.
	 * 
	 * @param dt The target {@link java.util.Date Date}
	 * @return true if the target {@link java.util.Date Date} is the current day.
	 */
	public static boolean isToday(final Date dt)
	{
		return Dates.compareDates(dt, now()) == 0;
	}

	/**
	 * Compares only the date fields of the Date objects.
	 * 
	 * @param dt1 The first date
	 * @param dt2 The second date
	 * @return -1 if dt1 is before dt2, 0 if dt1 equals dt2, 1 if dt1 is after dt2
	 */
	public static int compareDates(final Date dt1, final Date dt2)
	{
		int ret = Comparisons.compare(Dates.getYear(dt1), Dates.getYear(dt2));
		if (ret != 0)
			return ret;
		
		ret = Comparisons.compare(Dates.getMonth(dt1), Dates.getMonth(dt2));
		if (ret != 0)
			return ret;

		return Comparisons.compare(Dates.getDayOfMonth(dt1), Dates.getDayOfMonth(dt2));
	}
	/**
	 * Tests to see if the target {@link java.util.Date Date} is null or within 1 second of midnight January 1st, 1970. 
	 * @param dt the target {@link java.util.Date Date}
	 * @return true if the target {@link java.util.Date Date} is null as defined by this method.
	 */
	public static boolean isNull(final Date dt)
	{
		return dt == null || dt.getTime() <= 1;
	}
	
}
