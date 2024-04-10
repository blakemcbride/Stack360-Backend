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
package com.arahant.services.standard.hr.personHistory;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class SearchPersonHistoryInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;
	
	@Validation (type="date", required=false)
	private int toDate;

	@Validation (type="date", required=false)
	private int fromDate;

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}

	public int getFromDate() {
		if (fromDate==0)
			fromDate=19000101;
		return fromDate;
	}

	public void setFromDate(int fromDate) {
		this.fromDate = fromDate;
	}

	public int getToDate() {
		if (toDate==0)
			toDate=20990101;
		return toDate;
	}

	public void setToDate(int toDate) {
		this.toDate = toDate;
	}

	

}

	
