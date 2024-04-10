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
package com.arahant.services.standard.hr.projectParent;
import com.arahant.annotation.Validation;

import com.arahant.business.BProject;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectInput extends TransmitInputBase {

	void setData(final BProject bc)
	{
		bc.setBillable('N');
		bc.setDoneForPersonId(personId);
		bc.setDescription(summary);
		bc.setCurrentPersonId(assignedEmployeeId);
		bc.setDetailDesc(detail);
//		bc.setDateReported(dateRequested);    These two are (and should be) set internally
//		bc.setTimeReported(timeRequested);
		bc.setProjectTypeId(projectTypeId);
		bc.setTimeCompleted(-1);
		
		if (statusId!=null && !"".equals(statusId.trim()))
		{
			bc.setProjectStatusId(statusId);
			bc.setDateCompleted(dateCompleted);
			bc.setTimeCompleted(timeCompleted);
		}
		
	}
	
	@Validation (table="project",column="subject_person",required=true)
	private String personId;
	@Validation (table="project",column="description",required=true)
	private String summary;
	@Validation (required=true)
	private String assignedEmployeeId;
	@Validation (min=0,max=40000,table="project",column="detail_desc",required=false)
	private String detail;
	@Validation (table="project",column="date_reported",type="date",required=true)
	private int dateRequested;
	@Validation (type="time",required=false)
	private int timeRequested;
	@Validation (table="project",column="project_type_id",required=true)
	private String projectTypeId;
	@Validation (required=false)
	private String statusId;
	@Validation (type="date",required=false)
	private int dateCompleted;
	@Validation (type="time",required=false)
	private int timeCompleted;

	public int getDateCompleted() {
		return dateCompleted;
	}

	public void setDateCompleted(int dateCompleted) {
		this.dateCompleted = dateCompleted;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public int getTimeCompleted() {
		return timeCompleted;
	}

	public void setTimeCompleted(int timeCompleted) {
		this.timeCompleted = timeCompleted;
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
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(final String employeeId)
	{
		this.personId=employeeId;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(final String summary)
	{
		this.summary=summary;
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

	public int getTimeRequested() {
		return timeRequested;
	}

	public void setTimeRequested(int timeRequested) {
		this.timeRequested = timeRequested;
	}

	/**
	 * @return Returns the dateRequested.
	 */
	public int getDateRequested() {
		return dateRequested;
	}
	/**
	 * @param dateRequested The dateRequested to set.
	 */
	public void setDateRequested(final int dateRequested) {
		this.dateRequested = dateRequested;
	}

}

	
