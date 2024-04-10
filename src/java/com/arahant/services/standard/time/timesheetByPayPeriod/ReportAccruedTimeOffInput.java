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
package com.arahant.services.standard.time.timesheetByPayPeriod;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
public class ReportAccruedTimeOffInput extends TransmitInputBase {

	public ReportAccruedTimeOffInput() {
		super();
	}
	
	@Validation (required=true)
	private String employeeId;
	@Validation (required=true)
	private String accrualAccountId;
	@Validation (type="date",required=false)
	private int startDate;
	@Validation (type="date",required=false)
	private int endDate;
	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
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
}

	
