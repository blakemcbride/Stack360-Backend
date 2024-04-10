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
package com.arahant.services.standard.time.timesheetApproval;


/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
public class ListEmployeeTimeOffItem {


	public ListEmployeeTimeOffItem() {
		super();
	}
	
	private String accrualAccountName;
	private double hours;
	/**
	 * @return Returns the accrualAccountName.
	 */
	public String getAccrualAccountName() {
		return accrualAccountName;
	}
	/**
	 * @param accrualAccountName The accrualAccountName to set.
	 */
	public void setAccrualAccountName(final String accrualAccountName) {
		this.accrualAccountName = accrualAccountName;
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

}

	
