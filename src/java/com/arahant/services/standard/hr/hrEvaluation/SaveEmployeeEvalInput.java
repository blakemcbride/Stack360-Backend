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
 * Created on Feb 26, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluation;
import com.arahant.annotation.Validation;
import com.arahant.business.BHREmployeeEval;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 26, 2007
 *
 */
public class SaveEmployeeEvalInput extends TransmitInputBase {


	/**
	 * @return Returns the employeeEvalId.
	 */
	public String getEmployeeEvalId() {
		return employeeEvalId;
	}

	/**
	 * @param employeeEvalId The employeeEvalId to set.
	 */
	public void setEmployeeEvalId(final String employeeEvalId) {
		this.employeeEvalId = employeeEvalId;
	}

	public SaveEmployeeEvalInput() {
		super();
	}
	
	/**
	 * @param ev
	 */
	void setData(final BHREmployeeEval ev) {
	//	ev.setEmployeeId(employeeId);
		ev.setEvalDate(evalDate);
		ev.setSupervisorId(supervisorId);
		ev.setNextEvalDate(nextEvalDate);
		ev.setDescription(description);
		ev.setComments(supervisorComments);

		ev.setInternalSupervisorComments(internalSupervisorComments);
	}
	
	@Validation (type="date",required=true)
	private int evalDate;
	@Validation (required=true)
	private String supervisorId;
	@Validation (type="date",required=false)
	private int nextEvalDate;
	@Validation (table="hr_employee_eval",column="description",required=true)
	private String description;
	@Validation (table="hr_empl_eval_detail",column="comments",required=false)
	private String supervisorComments;
	@Validation (required=true)
	private String employeeEvalId;

	@Validation (table="hr_empl_eval_detail",column="p_notes",required=false)
	private String internalSupervisorComments;

	

	/**
	 * @return Returns the internalSupervisorComments.
	 */
	public String getInternalSupervisorComments() {
		return internalSupervisorComments;
	}

	/**
	 * @param internalSupervisorComments The internalSupervisorComments to set.
	 */
	public void setInternalSupervisorComments(final String internalSupervisorComments) {
		this.internalSupervisorComments = internalSupervisorComments;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getSupervisorComments() {
		return supervisorComments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setSupervisorComments(final String comments) {
		this.supervisorComments = comments;
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
	 *
	public String getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId The employeeId to set.
	 *
	public void setEmployeeId(final String employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return Returns the evalDate.
	 */
	public int getEvalDate() {
		return evalDate;
	}
	/**
	 * @param evalDate The evalDate to set.
	 */
	public void setEvalDate(final int evalDate) {
		this.evalDate = evalDate;
	}
	/**
	 * @return Returns the nextEvalDate.
	 */
	public int getNextEvalDate() {
		return nextEvalDate;
	}
	/**
	 * @param nextEvalDate The nextEvalDate to set.
	 */
	public void setNextEvalDate(final int nextEvalDate) {
		this.nextEvalDate = nextEvalDate;
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
	
}

	
