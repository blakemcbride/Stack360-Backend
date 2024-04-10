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
 * Created on Feb 9, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetEntryExceptions;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class ListEmployeesForTimesheetExceptionReportInput extends TransmitInputBase {

	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (type="date",required=false)
	private int toDate;
	boolean includeSelf;
	
	public ListEmployeesForTimesheetExceptionReportInput() {
		super();
	}

	/**
	 * @return Returns the fromDate.
	 */
	public int getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(final int fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the includeSelf.
	 */
	public boolean isIncludeSelf() {
		return includeSelf;
	}

	/**
	 * @param includeSelf The includeSelf to set.
	 */
	public void setIncludeSelf(final boolean includeSelf) {
		this.includeSelf = includeSelf;
	}

	/**
	 * @return Returns the toDate.
	 */
	public int getToDate() {
		return toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(final int toDate) {
		this.toDate = toDate;
	}
}

	
