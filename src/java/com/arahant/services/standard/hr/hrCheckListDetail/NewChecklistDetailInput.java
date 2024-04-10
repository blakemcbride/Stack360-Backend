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
package com.arahant.services.standard.hr.hrCheckListDetail;
import com.arahant.annotation.Validation;
import com.arahant.business.BHRCheckListDetail;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class NewChecklistDetailInput extends TransmitInputBase {

	@Validation (required=false)
	private String employeeId;
	@Validation (required=false)
	private String supervisorId;
	@Validation (required=false)
	private String checklistItemId[];
	@Validation (type="date",required=false)
	private int dateCompleted;
	/**
	 * @param cli
	 */
	void setData(final BHRCheckListDetail cli) {
		cli.setEmployeeId(employeeId);
		cli.setSupervisorId(supervisorId);
		cli.setDateCompleted(dateCompleted);
	}

	/**
	 * @return Returns the checklistItemId.
	 */
	public String[] getChecklistItemId() {
            if (checklistItemId==null)
                return new String[0];
		return checklistItemId;
	}

	/**
	 * @param checklistItemId The checklistItemId to set.
	 */
	public void setChecklistItemId(final String checklistItemId[]) {
		this.checklistItemId = checklistItemId;
	}

	/**
	 * @return Returns the dateCompleted.
	 */
	public int getDateCompleted() {
		return dateCompleted;
	}

	/**
	 * @param dateCompleted The dateCompleted to set.
	 */
	public void setDateCompleted(final int dateCompleted) {
		this.dateCompleted = dateCompleted;
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
	 * @return Returns the supervisorId.
	 */
	public String getSupervisorId() {
		return supervisorId;
	}

	/**
	 * @param supervisorId The supervisorId to set.
	 */
	public void setSupervisorId(final String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public NewChecklistDetailInput() {
		super();
	}

	
}

	
