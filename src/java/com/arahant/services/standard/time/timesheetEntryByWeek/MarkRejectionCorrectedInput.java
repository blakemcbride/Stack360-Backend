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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetEntryByWeek;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class MarkRejectionCorrectedInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;
	@Validation (type="date",required=true)
	private int date;
	
	
	public MarkRejectionCorrectedInput() {
		super();
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
}

	
