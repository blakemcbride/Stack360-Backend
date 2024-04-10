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
 * Created on Mar 19, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvent;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Mar 19, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;
	@Validation (type="date",required=false)
	private int startDate;
	@Validation (type="date",required=false)
	private int endDate;
	@Validation (required=false)
	private boolean asc;

	/**
	 * @return Returns the asc.
	 */
	public boolean isAsc() {
		return asc;
	}

	/**
	 * @param asc The asc to set.
	 */
	public void setAsc(final boolean asc) {
		this.asc = asc;
	}

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
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}


}

	
