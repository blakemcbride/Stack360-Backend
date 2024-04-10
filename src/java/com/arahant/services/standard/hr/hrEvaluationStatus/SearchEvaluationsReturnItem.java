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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.hr.hrEvaluationStatus;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeEval;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class SearchEvaluationsReturnItem  {

	public SearchEvaluationsReturnItem() {
		super();
	}

	
	/**
	 * @param employee
	 */
	SearchEvaluationsReturnItem(final BHREmployeeEval e) {
		super();

		employeeId = e.getEmployeeId();
		supervisorId = e.getSupervisorId();
		employeeName = new BEmployee(e.getEmployeeId()).getNameLFM();
		supervisorName = e.getSupervisorNameLFM();

		evalDate = e.getEvalDate();
		finalDate = e.getFinalDate();
		nextDate = e.getNextEvalDate();

		completed = e.getFinalDate() > 0;

		description = e.getDescription();
		comments = e.getComments();
		employeeComments = e.getEmployeeComments();
		privateComments = e.getInternalSupervisorComments();

		state = e.getState().equals("E") ? "Employee" : "Supervisor";

		evaluationId = e.getEmployeeEvalId();
	}

	private String employeeId;
	private String supervisorId;
	private String employeeName;
	private String supervisorName;
	private int evalDate;
	private int finalDate;
	private int nextDate;
	private String description;
	private boolean completed;
	private String comments;
	private String employeeComments;
	private String privateComments;
	private String state;
	private String evaluationId;

	public String getEvaluationId() {
		return evaluationId;
	}

	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public String getPrivateComments() {
		return privateComments;
	}

	public void setPrivateComments(String privateComments) {
		this.privateComments = privateComments;
	}

	public boolean getCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getEvalDate() {
		return evalDate;
	}

	public void setEvalDate(int evalDate) {
		this.evalDate = evalDate;
	}

	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}

	public int getNextDate() {
		return nextDate;
	}

	public void setNextDate(int nextDate) {
		this.nextDate = nextDate;
	}

	public String getSupervisorId() {
		return supervisorId;
	}

	public void setSupervisorId(String supervisorId) {
		this.supervisorId = supervisorId;
	}

	public String getSupervisorName() {
		return supervisorName;
	}

	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}


	

	
}

	
