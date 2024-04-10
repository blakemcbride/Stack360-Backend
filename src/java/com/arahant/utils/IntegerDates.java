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

import com.arahant.beans.OrgGroup;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import java.util.LinkedList;
import java.util.Set;

/**
 * Routines dealing with dates in the form YYYYMMDD
 *
 * @author Blake McBride
 */
public class IntegerDates {
	
	public static final char PeriodType_Daily = 'D';
	public static final char PeriodType_Weekly = 'W';
	public static final char PeriodType_BiWeekly = 'B';
	public static final char PeriodType_SemiMonthly = 'S';
	public static final char PeriodType_Monthly = 'M';
		
	/**
	 * Calculates the date of the beginning of the period that timesheetDate is in
	 * 
	 * @param periodType
	 * @param periodStartDate  start date of any of the valid periods
	 * @param timesheetDate a date inside the period to be found
	 * @return 
	 */
	public static int beginningPeriodDate(final PeriodInfo pi, final int timesheetDate) {
		int fromDate = timesheetDate;
		int day;
		
		if (pi.periodType == PeriodType_Daily) {
			// default to timesheetDate
		} else if (pi.periodType == PeriodType_Weekly) {
			fromDate = calcBeginningPeriod(7, pi.periodStartDate, timesheetDate);
		} else if (pi.periodType == PeriodType_BiWeekly) {
			fromDate = calcBeginningPeriod(14, pi.periodStartDate, timesheetDate);
		} else if (pi.periodType == PeriodType_SemiMonthly) {
			day = DateUtils.getDay(timesheetDate);
			if (day <= 15)
				fromDate = DateUtils.setDay(timesheetDate, 1);
			else
				fromDate = DateUtils.setDay(timesheetDate, 16);
		} else if (pi.periodType == PeriodType_Monthly) {
			fromDate = DateUtils.setDay(timesheetDate, 1);
		}
		return fromDate;
	}
	
	/**
	 * Calculates the date of the ending of the period that timesheetDate is in
	 * 
	 * @param periodType
	 * @param periodStartDate  start date of any of the valid periods
	 * @param timesheetDate a date inside the period to be found
	 * @return 
	 */
	public static int endingPeriodDate(final PeriodInfo pi, final int timesheetDate) {
		int toDate = timesheetDate;
		int day;
		
		if (pi.periodType == PeriodType_Daily) {
			// default to timesheetDate
		} else if (pi.periodType == PeriodType_Weekly) {
			toDate = calcEndingPeriod(7, pi.periodStartDate, timesheetDate);
		} else if (pi.periodType == PeriodType_BiWeekly) {
			toDate = calcEndingPeriod(14, pi.periodStartDate, timesheetDate);
		} else if (pi.periodType == PeriodType_SemiMonthly) {
			day = DateUtils.getDay(timesheetDate);
			if (day <= 15)
				toDate = DateUtils.setDay(timesheetDate, 15);
			else {
				toDate = DateUtils.addMonths(timesheetDate, 1);
				toDate = DateUtils.setDay(toDate, 1);
				toDate = DateUtils.addDays(toDate, -1);
			}
		} else if (pi.periodType == PeriodType_Monthly) {
			toDate = DateUtils.addMonths(timesheetDate, 1);
			toDate = DateUtils.setDay(toDate, 1);
			toDate = DateUtils.addDays(toDate, -1);
		}
		return toDate;
	}
	
	/**
	 * Returns the date of the beginning of the period following the one 'date' is in.
	 * 
	 * @param periodStartDate start date of any of the valid periods
	 * @param date
	 * @return 
	 */
	public static int nextPeriodStartDate(final PeriodInfo pi, int date) {
		int startDate = DateUtils.addDays(date, 1);
		int day = DateUtils.getDay(date);

		if (pi.periodType == PeriodType_Daily) {
			// default to next date
		} else if (pi.periodType == PeriodType_Weekly) {
			startDate = DateUtils.addDays(calcBeginningPeriod(7, pi.periodStartDate, date), 7);
		} else if (pi.periodType == PeriodType_BiWeekly) {
			startDate = DateUtils.addDays(calcBeginningPeriod(14, pi.periodStartDate, date), 14);
		} else if (pi.periodType == PeriodType_SemiMonthly) {
			if (day <= 15) {
				startDate = DateUtils.setDay(date, 16);
			} else {
				startDate = DateUtils.addMonths(date, 1);
				startDate = DateUtils.setDay(startDate, 1);
			}
		} else if (pi.periodType == PeriodType_Monthly) {
			startDate = DateUtils.addMonths(date, 1);
			startDate = DateUtils.setDay(startDate, 1);
		}
		return startDate;
	}
	
	/**
	 * Returns the date of the beginning of the period preceding the one 'date' is in.
	 * 
	 * @param periodStartDate start date of any of the valid periods
	 * @param date
	 * @return 
	 */
	public static int prevPeriodStartDate(final PeriodInfo pi, int date) {
		int startDate = DateUtils.addDays(date, -1);
		int day = DateUtils.getDay(date);

		if (pi.periodType == PeriodType_Daily) {
			// default to previous date
		} else if (pi.periodType == PeriodType_Weekly) {
			startDate = DateUtils.addDays(calcBeginningPeriod(7, pi.periodStartDate, date), -7);
		} else if (pi.periodType == PeriodType_BiWeekly) {
			startDate = DateUtils.addDays(calcBeginningPeriod(14, pi.periodStartDate, date), -14);
		} else if (pi.periodType == PeriodType_SemiMonthly) {
			if (day >= 16) {
				startDate = DateUtils.setDay(date, 1);
			} else {
				startDate = DateUtils.addMonths(date, -1);
				startDate = DateUtils.setDay(startDate, 16);				
			}
		} else if (pi.periodType == PeriodType_Monthly) {
			startDate = DateUtils.addMonths(date, -1);
			startDate = DateUtils.setDay(startDate, 1);				
		}
		return startDate;
	}
	
	/**
	 * Calculates the beginning date of the period inputDate is part of.
	 * 
	 * @param daysInPeriod 7=weekly, 14=biweekly
	 * @param periodStartDate start date of any of the valid periods
	 * @param inputDate date in question
	 * @return 
	 */
	private static int calcBeginningPeriod(int daysInPeriod, int periodStartDate, int inputDate) {
		int startDays = toDays(periodStartDate);
		int inputDays = toDays(inputDate);
		return DateUtils.addDays(inputDate, -((inputDays - startDays) % daysInPeriod));	
	}
	
	/**
	 * Calculates the ending date of the period inputDate is part of.
	 * 
	 * @param daysInPeriod 7=weekly, 14=biweekly
	 * @param periodStartDate start date of any of the valid periods
	 * @param inputDate date in question
	 * @return 
	 */
	private static int calcEndingPeriod(int daysInPeriod, int periodStartDate, int inputDate) {
		int startDays = toDays(periodStartDate);
		int inputDays = toDays(inputDate);
		return DateUtils.addDays(inputDate, daysInPeriod - 1 -((inputDays - startDays) % daysInPeriod));	
	}
	
	/**
	 * Returns a sort of Julian date but instead of starting BC it starts as of the startDate.
	 * This is good enough for must purposes (determining a number of days between dates, etc.
	 * 
	 * @param date YYYYMMDD
	 * @return number of days since a fixed start date
	 */
	private static int toDays(int date) {
		final int startDate = 19000101;  //  arbitrary but fixed start date
		return (int) DateUtils.getDaysBetween(date, startDate);
	}
	
	private static final char DefaultPeriodType = 'W';
	private static final int DefaultPeriodStartDate = 20000103;  // Monday
	private static final char DefaultShowBillable = 'Y';
	
	public static class PeriodInfo {
		private char periodType;
		private int periodStartDate;
		private char showBillable;
		
		public PeriodInfo(BPerson p) {
			periodType = 'I';
			periodStartDate = DefaultPeriodStartDate;
			showBillable = DefaultShowBillable;
			Set<BOrgGroup> bogs = p.getOrgGroups();
			for (BOrgGroup bog : bogs) {
				periodType = bog.getTimesheetPeriodType();
				if (periodType != 'I') {
					periodStartDate = bog.getTimesheetPeriodStartDate();
					showBillable = bog.getTimesheetShowBillable();
					break;
				}
			}
			if (periodType == 'I')
				for (BOrgGroup bog : bogs) {
					LinkedList<OrgGroup> ogl = bog.getOrgGroupHierarchyBreadthFirst();
					ogl.remove();  //  remove the one we already tested
					for (OrgGroup og : ogl) {
						periodType = og.getTimesheetPeriodType();
						if (periodType != 'I') {
							periodStartDate = og.getTimesheetPeriodStartDate();
							showBillable = og.getTimesheetShowBillable();
							break;
						}
					}
					if (periodType != 'I')
						break;
				}
			if (periodType == 'I')
				periodType = DefaultPeriodType;			
		}
		
		public boolean showBillable() {
			return showBillable == 'Y';
		}
	}

}
