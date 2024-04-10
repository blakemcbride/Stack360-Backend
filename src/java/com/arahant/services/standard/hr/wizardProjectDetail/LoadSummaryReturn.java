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
package com.arahant.services.standard.hr.wizardProjectDetail;

import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.business.BProject;
import com.arahant.business.BProperty;
import com.arahant.business.BWizardProject;
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

		personId=bc.getWorkDoneForEmployeeId();
		personSSN=bc.getWorkDoneForEmployeeSSN();
		statusId=bc.getProjectStatusId();
		assignedEmployeeId=bc.getCurrentPersonId();
		detail= formatDetailDesc(bc.getDetailDesc());
		dateRequested=bc.getDateReported();
		timeRequested=bc.getTimeReported();
		dateCompleted=bc.getDateCompleted();
		timeCompleted=bc.getTimeCompleted();
		projectTypeId=bc.getProjectTypeId();
		personEmail=bc.getWorkDoneForEmployeeEmail();

		personFirstName=bc.getWorkDoneForEmployeeFirstName();
		personLastName=bc.getWorkDoneForEmployeeLastName();
		personType=bc.getWorkDoneForEmployeePersonType();
		hrScreenGroupId=BProperty.get("HR Screen Group ID");

		setRequests(bc.getWizardProjects(false));
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
	private LoadSummaryReturnItem[] requests;

	public LoadSummaryReturnItem[] getRequests() {
		return requests;
	}

	public void setRequests(LoadSummaryReturnItem[] requests) {
		this.requests = requests;
	}

	public void setRequests(BWizardProject[] arr)
	{
		requests = new LoadSummaryReturnItem[arr.length];
		for(int x = 0; x < arr.length; x++)
		{
			requests[x] = new LoadSummaryReturnItem(arr[x]);
		}
	}

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

	private String formatDetailDesc(String description) {
		//System.out.println(description);
		//System.out.println("Group Critical Illness index: " + description.indexOf("Group Critical Illness"));
		int index = description.indexOf("Group Critical Illness");
		if(index != -1)
		{
			String detailDescBegin = description.substring(0, index + ("Group Critical Illness").length());
			String detailDescEnd = description.substring(index + + ("Group Critical Illness").length());
			BPerson person = new BPerson(personId);
			detailDescBegin += "\n\tEmployee Answers";
			detailDescBegin += "\n\t\tActively at work: " + person.getActivelyAtWork();
			detailDescBegin += "\n\t\tUses tobacco products: " + person.getTabaccoUse();
			detailDescBegin += "\n\t\tDiagnosed with Aids: " + person.getHasAids();
			detailDescBegin += "\n\t\tDiagnosed with Cancer: " + person.getHasCancer();
			detailDescBegin += "\n\t\tHas Heart Condition: " + person.getHasCancer();
			if(person.getBEmployee().hasSpouse())
			{
				BHREmplDependent spouse = new BHREmplDependent(person.getBEmployee().getSpouse());
				detailDescBegin += "\n\n\tSpouse Answers:";
				detailDescBegin += "\n\t\tUnable to Perform normal duties: " + spouse.getUnableToPerform();
				detailDescBegin += "\n\t\tUses tobacco products: " + spouse.getTabaccoUse();
				detailDescBegin += "\n\t\tDiagnosed with Aids: " + spouse.getHasAids();
				detailDescBegin += "\n\t\tDiagnosed with Cancer: " + spouse.getHasCancer();
				detailDescBegin += "\n\t\tHas Heart Condition: " + spouse.getHasCancer();
			}
			else
			{
				detailDescBegin += "\n\n\t(No Spouse)";
			}
			return detailDescBegin + detailDescEnd;
		}
		return description;
		
	}

}

	
