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
import com.arahant.annotation.Validation;

import com.arahant.business.BProject;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProjectInput extends TransmitInputBase {

	void setData(final BProject bc)
	{
		bc.setDescription(summary);
		
		bc.setCurrentPersonId(assignedEmployeeId);
		bc.setDetailDesc(detail);
		bc.setDateCompleted(dateCompleted);
		bc.setTimeCompleted(timeCompleted);
		String oldType=bc.getProjectTypeId();
		bc.setProjectTypeId(projectTypeId);

		if (statusId==null || "".equals(statusId) || !oldType.equals(projectTypeId))
			bc.calculateRouteAndStatus();
		else
			bc.setProjectStatusId(statusId);
	}
	
	@Validation (table="project",column="project_id",required=false)
	private String projectId;
	@Validation (table="project",column="description",required=true)
	private String summary;
	@Validation (table="project",column="project_status_id",required=false)
	private String statusId;
	@Validation (required=true)
	private String assignedEmployeeId;
	@Validation (min=0,max=40000,table="project",column="detail_desc",required=false)
	private String detail;;
	@Validation (table="project",column="completed_date",type="date",required=false)
	private int dateCompleted;
	@Validation (table="project",column="completed_time",type="time",required=false)
	private int timeCompleted;
	@Validation (table="project",column="project_type_id",required=true)
	private String projectTypeId;

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
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(final String projectId)
	{
		this.projectId=projectId;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(final String summary)
	{
		this.summary=summary;
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
	 * @return Returns the timeCompleted.
	 */
	public int getTimeCompleted() {
		return timeCompleted;
	}
	/**
	 * @param timeCompleted The timeCompleted to set.
	 */
	public void setTimeCompleted(final int timeCompleted) {
		this.timeCompleted = timeCompleted;
	}



}

	
