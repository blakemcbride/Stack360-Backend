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
package com.arahant.services.standard.hr.hrEvaluation;
import com.arahant.business.BHREmployeeEval;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListEmployeeEvalsItem {


	public ListEmployeeEvalsItem() {
		super();
		
		
	}
	
	/**
	 * @param eval
	 */
	ListEmployeeEvalsItem(final BHREmployeeEval eval) {
		super();
		statusFormatted=eval.getStatus();
		employeeEvalId=eval.getEmployeeEvalId();
		employeeId=eval.getEmployeeId();
		evalDate=eval.getEvalDate();
		supervisorId=eval.getSupervisorId();
		nextEvalDate=eval.getNextEvalDate();
		description=eval.getDescription();
		comments=eval.getComments();
		this.evalDateFormatted = DateUtils.getDateFormatted(this.getEvalDate());
		this.nextEvalDateFormatted = DateUtils.getDateFormatted(this.getNextEvalDate());
		status=eval.getState();
	}

	private String evalDateFormatted;
	private String nextEvalDateFormatted;
	private String statusFormatted;
	private String employeeEvalId;
	private String employeeId;
	private int evalDate;
	private String supervisorId;
	private int nextEvalDate;
	private String description;
	private String comments;
	private String status;

	/**
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatus(final String status) {
		this.status = status;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(final String comments) {
		this.comments = comments;
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
	 * @return Returns the evalDateFormatted.
	 */
	public String getEvalDateFormatted() {
		return evalDateFormatted;
	}
	/**
	 * @param evalDateFormatted The evalDateFormatted to set.
	 */
	public void setEvalDateFormatted(final String evalDateFormatted) {
		this.evalDateFormatted = evalDateFormatted;
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
	 * @return Returns the nextEvalDateFormatted.
	 */
	public String getNextEvalDateFormatted() {
		return nextEvalDateFormatted;
	}
	/**
	 * @param nextEvalDateFormatted The nextEvalDateFormatted to set.
	 */
	public void setNextEvalDateFormatted(final String nextEvalDateFormatted) {
		this.nextEvalDateFormatted = nextEvalDateFormatted;
	}
	/**
	 * @return Returns the supervisorFName.
	 *
	public String getSupervisorFName() {
		return supervisorFName;
	}
	/**
	 * @param supervisorFName The supervisorFName to set.
	 *
	public void setSupervisorFName(final String supervisorFName) {
		this.supervisorFName = supervisorFName;
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
	/**
	 * @return Returns the supervisorLName.
	 *
	public String getSupervisorLName() {
		return supervisorLName;
	}
	/**
	 * @param supervisorLName The supervisorLName to set.
	 *
	public void setSupervisorLName(final String supervisorLName) {
		this.supervisorLName = supervisorLName;
	}
	*/

	/**
	 * @return Returns the status.
	 */
	public String getStatusFormatted() {
		return statusFormatted;
	}

	/**
	 * @param status The status to set.
	 */
	public void setStatusFormatted(final String status) {
		this.statusFormatted = status;
	}
}

	
