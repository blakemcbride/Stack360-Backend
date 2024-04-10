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
 * Created on Mar 13, 2007
 * 
 */
package com.arahant.services.standard.hr.hrAccruedTimeOff;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Mar 13, 2007
 *
 * date, hours, running total, description.
 */
public class ListAccruedTimeOffItem {

	private String accrualId;
	private String dateFormatted;
	private double hours;
	private String description;
	private int date;
	private double runningTotal;
	

	/**
	 * @return Returns the runningTotal.
	 */
	public double getRunningTotal() {
		return runningTotal;
	}

	/**
	 * @param runningTotal The runningTotal to set.
	 */
	public void setRunningTotal(final double runningTotal) {
		this.runningTotal = runningTotal;
	}

	/**
	 * @return Returns the date.
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(final int date) {
		this.date = date;
	}

	/**
	 * @return Returns the accrualId.
	 */
	public String getAccrualId() {
		return accrualId;
	}

	/**
	 * @param accrualId The accrualId to set.
	 */
	public void setAccrualId(final String accrualId) {
		this.accrualId = accrualId;
	}

	/**
	 * @return Returns the dateFormatted.
	 */
	public String getDateFormatted() {
		return dateFormatted;
	}

	/**
	 * @param dateFormatted The dateFormatted to set.
	 */
	public void setDateFormatted(final String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the hours.
	 */
	public double getHours() {
		return hours;
	}

	/**
	 * @param hours The hours to set.
	 */
	public void setHours(final double hours) {
		this.hours = hours;
	}

	public ListAccruedTimeOffItem() {
		super();
	}

	/**
	 * @param off
	 */
	ListAccruedTimeOffItem(final BHRAccruedTimeOff off) {
		super();
		accrualId=off.getAccrualId();
		dateFormatted=DateUtils.getDateFormatted(off.getAccrualDate());
		description=off.getDescription();
		hours=round(off.getAccrualHours());
		date=off.getAccrualDate();
		runningTotal=round(off.getRunningTotal());
	}
	
	private double round(double x)
	{
		return x;
		//eturn Math.round(x*100)/100;
	}
}

	
