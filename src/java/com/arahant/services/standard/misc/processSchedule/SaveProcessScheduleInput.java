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


/**
 * 
 */
package com.arahant.services.standard.misc.processSchedule;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProcessSchedule;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProcessScheduleInput extends TransmitInputBase {

	void setData(BProcessSchedule bc)
	{
	/*	bc.setProcessId(processId);
		bc.setType(type);
		bc.setDailyStartTime(dailyStartTime);
		bc.setDailyDays(dailyDays);
		bc.setDailyPerformMissingRuns(dailyPerformMissingRuns);
		bc.setWeeklyStartTime(weeklyStartTime);
		bc.setWeeklyWeeks(weeklyWeeks);
		bc.setWeeklyMonday(weeklyMonday);
		bc.setWeeklyTuesday(weeklyTuesday);
		bc.setWeeklyWednesday(weeklyWednesday);
		bc.setWeeklyThursday(weeklyThursday);
		bc.setWeeklyFriday(weeklyFriday);
		bc.setWeeklySaturday(weeklySaturday);
		bc.setWeeklySunday(weeklySunday);
		bc.setWeeklyPerformMissingRuns(weeklyPerformMissingRuns);
		bc.setMonthlyStartTime(monthlyStartTime);
		bc.setMonthlyType(monthlyType);
		bc.setMonthlySpecificDay(monthlySpecificDay);
		bc.setMonthlyApproximateDay(monthlyApproximateDay);
		bc.setMonthlyApproximateWeekday(monthlyApproximateWeekday);
		bc.setMonthlyJanuary(monthlyJanuary);
		bc.setMonthlyFebruary(monthlyFebruary);
		bc.setMonthlyMarch(monthlyMarch);
		bc.setMonthlyApril(monthlyApril);
		bc.setMonthlyMay(monthlyMay);
		bc.setMonthlyJune(monthlyJune);
		bc.setMonthlyJuly(monthlyJuly);
		bc.setMonthlyAugust(monthlyAugust);
		bc.setMonthlySeptember(monthlySeptember);
		bc.setMonthlyOctober(monthlyOctober);
		bc.setMonthlyNovember(monthlyNovember);
		bc.setMonthlyDecember(monthlyDecember);
		bc.setMonthlyPerformMissingRuns(monthlyPerformMissingRuns);
		bc.setOnceStartTime(onceStartTime);
		bc.setOnceRunOn(onceRunOn);
		bc.setOncePerformMissingRuns(oncePerformMissingRuns);
*/
	}
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=true)
	private String processId;
	@Validation (min=1,max=5,required=true)
	private int type;
	@Validation (type="time",required=false)
	private int dailyStartTime;
	@Validation (min=0,required=false)
	private int dailyDays;
	@Validation (required=false)
	private boolean dailyPerformMissingRuns;
	@Validation (type="time",required=false)
	private int weeklyStartTime;
	@Validation (min=0,max=52,required=false)
	private int weeklyWeeks;
	@Validation (required=false)
	private boolean weeklyMonday;
	@Validation (required=false)
	private boolean weeklyTuesday;
	@Validation (required=false)
	private boolean weeklyWednesday;
	@Validation (required=false)
	private boolean weeklyThursday;
	@Validation (required=false)
	private boolean weeklyFriday;
	@Validation (required=false)
	private boolean weeklySaturday;
	@Validation (required=false)
	private boolean weeklySunday;
	@Validation (required=false)
	private boolean weeklyPerformMissingRuns;
	@Validation (type="time",required=false)
	private int monthlyStartTime;
	@Validation (min=0,max=2,required=false)
	private int monthlyType;
	@Validation (min=0,max=31,required=false)
	private int monthlySpecificDay;
	@Validation (min=1,max=5,required=false)
	private int monthlyApproximateDay;
	@Validation (min=1,max=7,required=false)
	private int monthlyApproximateWeekday;
	@Validation (required=false)
	private boolean monthlyJanuary;
	@Validation (required=false)
	private boolean monthlyFebruary;
	@Validation (required=false)
	private boolean monthlyMarch;
	@Validation (required=false)
	private boolean monthlyApril;
	@Validation (required=false)
	private boolean monthlyMay;
	@Validation (required=false)
	private boolean monthlyJune;
	@Validation (required=false)
	private boolean monthlyJuly;
	@Validation (required=false)
	private boolean monthlyAugust;
	@Validation (required=false)
	private boolean monthlySeptember;
	@Validation (required=false)
	private boolean monthlyOctober;
	@Validation (required=false)
	private boolean monthlyNovember;
	@Validation (required=false)
	private boolean monthlyDecember;
	@Validation (required=false)
	private boolean monthlyPerformMissingRuns;
	@Validation (type="time",required=false)
	private int onceStartTime;
	@Validation (type="date",required=false)
	private int onceRunOn;
	@Validation (required=false)
	private boolean oncePerformMissingRuns;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getProcessId()
	{
		return processId;
	}
	public void setProcessId(String processId)
	{
		this.processId=processId;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public int getDailyStartTime()
	{
		return dailyStartTime;
	}
	public void setDailyStartTime(int dailyStartTime)
	{
		this.dailyStartTime=dailyStartTime;
	}
	public int getDailyDays()
	{
		return dailyDays;
	}
	public void setDailyDays(int dailyDays)
	{
		this.dailyDays=dailyDays;
	}
	public boolean getDailyPerformMissingRuns()
	{
		return dailyPerformMissingRuns;
	}
	public void setDailyPerformMissingRuns(boolean dailyPerformMissingRuns)
	{
		this.dailyPerformMissingRuns=dailyPerformMissingRuns;
	}
	public int getWeeklyStartTime()
	{
		return weeklyStartTime;
	}
	public void setWeeklyStartTime(int weeklyStartTime)
	{
		this.weeklyStartTime=weeklyStartTime;
	}
	public int getWeeklyWeeks()
	{
		return weeklyWeeks;
	}
	public void setWeeklyWeeks(int weeklyWeeks)
	{
		this.weeklyWeeks=weeklyWeeks;
	}
	public boolean getWeeklyMonday()
	{
		return weeklyMonday;
	}
	public void setWeeklyMonday(boolean weeklyMonday)
	{
		this.weeklyMonday=weeklyMonday;
	}
	public boolean getWeeklyTuesday()
	{
		return weeklyTuesday;
	}
	public void setWeeklyTuesday(boolean weeklyTuesday)
	{
		this.weeklyTuesday=weeklyTuesday;
	}
	public boolean getWeeklyWednesday()
	{
		return weeklyWednesday;
	}
	public void setWeeklyWednesday(boolean weeklyWednesday)
	{
		this.weeklyWednesday=weeklyWednesday;
	}
	public boolean getWeeklyThursday()
	{
		return weeklyThursday;
	}
	public void setWeeklyThursday(boolean weeklyThursday)
	{
		this.weeklyThursday=weeklyThursday;
	}
	public boolean getWeeklyFriday()
	{
		return weeklyFriday;
	}
	public void setWeeklyFriday(boolean weeklyFriday)
	{
		this.weeklyFriday=weeklyFriday;
	}
	public boolean getWeeklySaturday()
	{
		return weeklySaturday;
	}
	public void setWeeklySaturday(boolean weeklySaturday)
	{
		this.weeklySaturday=weeklySaturday;
	}
	public boolean getWeeklySunday()
	{
		return weeklySunday;
	}
	public void setWeeklySunday(boolean weeklySunday)
	{
		this.weeklySunday=weeklySunday;
	}
	public boolean getWeeklyPerformMissingRuns()
	{
		return weeklyPerformMissingRuns;
	}
	public void setWeeklyPerformMissingRuns(boolean weeklyPerformMissingRuns)
	{
		this.weeklyPerformMissingRuns=weeklyPerformMissingRuns;
	}
	public int getMonthlyStartTime()
	{
		return monthlyStartTime;
	}
	public void setMonthlyStartTime(int monthlyStartTime)
	{
		this.monthlyStartTime=monthlyStartTime;
	}
	public int getMonthlyType()
	{
		return monthlyType;
	}
	public void setMonthlyType(int monthlyType)
	{
		this.monthlyType=monthlyType;
	}
	public int getMonthlySpecificDay()
	{
		return monthlySpecificDay;
	}
	public void setMonthlySpecificDay(int monthlySpecificDay)
	{
		this.monthlySpecificDay=monthlySpecificDay;
	}
	public int getMonthlyApproximateDay()
	{
		return monthlyApproximateDay;
	}
	public void setMonthlyApproximateDay(int monthlyApproximateDay)
	{
		this.monthlyApproximateDay=monthlyApproximateDay;
	}
	public int getMonthlyApproximateWeekday()
	{
		return monthlyApproximateWeekday;
	}
	public void setMonthlyApproximateWeekday(int monthlyApproximateWeekday)
	{
		this.monthlyApproximateWeekday=monthlyApproximateWeekday;
	}
	public boolean getMonthlyJanuary()
	{
		return monthlyJanuary;
	}
	public void setMonthlyJanuary(boolean monthlyJanuary)
	{
		this.monthlyJanuary=monthlyJanuary;
	}
	public boolean getMonthlyFebruary()
	{
		return monthlyFebruary;
	}
	public void setMonthlyFebruary(boolean monthlyFebruary)
	{
		this.monthlyFebruary=monthlyFebruary;
	}
	public boolean getMonthlyMarch()
	{
		return monthlyMarch;
	}
	public void setMonthlyMarch(boolean monthlyMarch)
	{
		this.monthlyMarch=monthlyMarch;
	}
	public boolean getMonthlyApril()
	{
		return monthlyApril;
	}
	public void setMonthlyApril(boolean monthlyApril)
	{
		this.monthlyApril=monthlyApril;
	}
	public boolean getMonthlyMay()
	{
		return monthlyMay;
	}
	public void setMonthlyMay(boolean monthlyMay)
	{
		this.monthlyMay=monthlyMay;
	}
	public boolean getMonthlyJune()
	{
		return monthlyJune;
	}
	public void setMonthlyJune(boolean monthlyJune)
	{
		this.monthlyJune=monthlyJune;
	}
	public boolean getMonthlyJuly()
	{
		return monthlyJuly;
	}
	public void setMonthlyJuly(boolean monthlyJuly)
	{
		this.monthlyJuly=monthlyJuly;
	}
	public boolean getMonthlyAugust()
	{
		return monthlyAugust;
	}
	public void setMonthlyAugust(boolean monthlyAugust)
	{
		this.monthlyAugust=monthlyAugust;
	}
	public boolean getMonthlySeptember()
	{
		return monthlySeptember;
	}
	public void setMonthlySeptember(boolean monthlySeptember)
	{
		this.monthlySeptember=monthlySeptember;
	}
	public boolean getMonthlyOctober()
	{
		return monthlyOctober;
	}
	public void setMonthlyOctober(boolean monthlyOctober)
	{
		this.monthlyOctober=monthlyOctober;
	}
	public boolean getMonthlyNovember()
	{
		return monthlyNovember;
	}
	public void setMonthlyNovember(boolean monthlyNovember)
	{
		this.monthlyNovember=monthlyNovember;
	}
	public boolean getMonthlyDecember()
	{
		return monthlyDecember;
	}
	public void setMonthlyDecember(boolean monthlyDecember)
	{
		this.monthlyDecember=monthlyDecember;
	}
	public boolean getMonthlyPerformMissingRuns()
	{
		return monthlyPerformMissingRuns;
	}
	public void setMonthlyPerformMissingRuns(boolean monthlyPerformMissingRuns)
	{
		this.monthlyPerformMissingRuns=monthlyPerformMissingRuns;
	}
	public int getOnceStartTime()
	{
		return onceStartTime;
	}
	public void setOnceStartTime(int onceStartTime)
	{
		this.onceStartTime=onceStartTime;
	}
	public int getOnceRunOn()
	{
		return onceRunOn;
	}
	public void setOnceRunOn(int onceRunOn)
	{
		this.onceRunOn=onceRunOn;
	}
	public boolean getOncePerformMissingRuns()
	{
		return oncePerformMissingRuns;
	}
	public void setOncePerformMissingRuns(boolean oncePerformMissingRuns)
	{
		this.oncePerformMissingRuns=oncePerformMissingRuns;
	}

}

	
