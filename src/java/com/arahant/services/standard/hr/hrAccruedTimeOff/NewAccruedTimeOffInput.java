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
import com.arahant.annotation.Validation;

import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
public class NewAccruedTimeOffInput extends TransmitInputBase {

	public NewAccruedTimeOffInput() {
		super();
	}
	
	@Validation (required=true)
	private String employeeId;
	@Validation (required=true)
	private String accrualAccountId;
	@Validation (type="date",required=true)
	private int date;
	@Validation (required=true)
	private double hours;
	@Validation (table="hr_accrual",column="description",required=true)
	private String description;
	/**
	 * @return Returns the accrualAccountId.
	 */
	public String getAccrualAccountId() {
		return accrualAccountId;
	}
	/**
	 * @param accrualAccountId The accrualAccountId to set.
	 */
	public void setAccrualAccountId(final String accrualAccountId) {
		this.accrualAccountId = accrualAccountId;
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
	 * @return Returns the employeeId.
	 */
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
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
	/**
	 * @param x
	 */
	void setData(final BHRAccruedTimeOff x) {
		x.setAccrualDate(date);
		x.setAccrualHours(hours);
		x.setDescription(description);
		x.setEmployeeId(employeeId);
		x.setAccrualAccountId(accrualAccountId);
	}
}

	
