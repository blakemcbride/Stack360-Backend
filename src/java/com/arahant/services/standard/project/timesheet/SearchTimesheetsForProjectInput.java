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
package com.arahant.services.standard.project.timesheet;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchTimesheetsForProjectInput extends TransmitInputBase {

	@Validation (required=true)
	private String projectId;
	private String shiftId;
	@Validation (required=false)
	private boolean approved;
	@Validation (required=false)
	private boolean invoiced;
	@Validation (required=false)
	private boolean notApproved;
	@Validation (type="date",required=false)
	private int startDate;
	@Validation (type="date",required=false)
	private int finalDate;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public boolean getApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public boolean getInvoiced() {
		return invoiced;
	}

	public void setInvoiced(boolean invoiced) {
		this.invoiced = invoiced;
	}

	public boolean getNotApproved() {
		return notApproved;
	}

	public void setNotApproved(boolean notApproved) {
		this.notApproved = notApproved;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}
}

	
