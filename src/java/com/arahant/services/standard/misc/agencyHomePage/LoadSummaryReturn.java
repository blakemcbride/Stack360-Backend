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
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.business.BProject;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadSummaryReturn extends TransmitReturnBase {

	void setData(final BProject bc)
	{
		
		summary=bc.getDescription();
		
		personSSN=bc.getWorkDoneForEmployeeSSN();
		statusId=bc.getProjectStatusId();
		assignedEmployeeId=bc.getCurrentPersonId();
		detail=bc.getDetailDesc();
		dateRequested=bc.getDateReported();
		timeRequested=bc.getTimeReported();
		dateCompleted=bc.getDateCompleted();
		timeCompleted=bc.getTimeCompleted();
		projectTypeId=bc.getProjectTypeId();
		personEmail=bc.getWorkDoneForEmployeeEmail();
		personId=bc.getWorkDoneForEmployeeId();

		personFirstName=bc.getWorkDoneForEmployeeFirstName();
		personLastName=bc.getWorkDoneForEmployeeLastName();
		personType=bc.getWorkDoneForEmployeePersonType();
		hrScreenGroupId=BProperty.get("HR Screen Group ID");
	}
	
	private String summary;
	private String personFirstName;
	private String personLastName;
	private String personType;
	private String hrScreenGroupId;

	private String personSSN;
	private String statusId;
	private String assignedEmployeeId;
	private String detail;
	private int dateRequested;
	private int timeRequested;
	private int dateCompleted;
	private int timeCompleted;
	private String projectTypeId, personEmail;
	private String personId;

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	/**
	 * @return Returns the projectTypeId.
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}
	/**
	 * @param projectTypeId The projectTypeId to set.
	 */
	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(final String summary)
	{
		this.summary=summary;
	}

	public String getHrScreenGroupId() {
		return hrScreenGroupId;
	}

	public void setHrScreenGroupId(String hrScreenGroupId) {
		this.hrScreenGroupId = hrScreenGroupId;
	}

	public String getPersonFirstName() {
		return personFirstName;
	}

	public void setPersonFirstName(String personFirstName) {
		this.personFirstName = personFirstName;
	}

	public String getPersonLastName() {
		return personLastName;
	}

	public void setPersonLastName(String personLastName) {
		this.personLastName = personLastName;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}
	
	public String getPersonSSN()
	{
		return personSSN;
	}
	public void setPersonSSN(final String employeeSSN)
	{
		this.personSSN=employeeSSN;
	}
	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(final String statusId)
	{
		this.statusId=statusId;
	}
	public String getAssignedEmployeeId()
	{
		return assignedEmployeeId;
	}
	public void setAssignedEmployeeId(final String assignedEmployeeId)
	{
		this.assignedEmployeeId=assignedEmployeeId;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(final String detail)
	{
		this.detail=detail;
	}
	public int getDateRequested()
	{
		return dateRequested;
	}
	public void setDateRequested(final int dateRequested)
	{
		this.dateRequested=dateRequested;
	}
	public int getTimeRequested()
	{
		return timeRequested;
	}
	public void setTimeRequested(final int timeRequested)
	{
		this.timeRequested=timeRequested;
	}
	public int getDateCompleted()
	{
		return dateCompleted;
	}
	public void setDateCompleted(final int dateCompleted)
	{
		this.dateCompleted=dateCompleted;
	}
	public int getTimeCompleted()
	{
		return timeCompleted;
	}
	public void setTimeCompleted(final int timeCompleted)
	{
		this.timeCompleted=timeCompleted;
	}
	/**
	 * @return Returns the employeeEmail.
	 */
	public String getPersonEmail() {
		return personEmail;
	}
	/**
	 * @param employeeEmail The employeeEmail to set.
	 */
	public void setPersonEmail(final String employeeEmail) {
		this.personEmail = employeeEmail;
	}

}

	
