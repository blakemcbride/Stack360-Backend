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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrNote;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class GetNotesReportInput extends TransmitInputBase {

	@Validation (required=true)
	private String personId;
	@Validation (required=false)
	private boolean showAsDependent;

	/**
	 * @return Returns the employeeId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param employeeId The employeeId to set.
	 */
	public void setPersonId(final String employeeId) {
		this.personId = employeeId;
	}

	/**
	 * @return
	 */
	public boolean getShowAsDependent() {
		return showAsDependent;
	}
	
	/**
	 * 
	 * @param showAsDependent
	 */
	public void setShowAsDependent(boolean showAsDependent) {
		this.showAsDependent = showAsDependent;
	}
}

	
