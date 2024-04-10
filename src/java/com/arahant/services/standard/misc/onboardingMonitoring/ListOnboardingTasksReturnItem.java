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
package com.arahant.services.standard.misc.onboardingMonitoring;
import com.arahant.beans.HrEmplStatusHistory;
import com.arahant.business.BHRCheckListDetail;
import com.arahant.business.BHRCheckListItem;
import com.arahant.business.BHREmplStatusHistory;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Date;

public class ListOnboardingTasksReturnItem {
	
	public ListOnboardingTasksReturnItem()
	{
		
	}

	ListOnboardingTasksReturnItem (BHRCheckListDetail bc, String empId)
	{
		taskName=bc.getName();

		BHREmplStatusHistory bes = new BHREmplStatusHistory(ArahantSession.getHSU()
				.createCriteria(HrEmplStatusHistory.class)
				.eq(HrEmplStatusHistory.STATUS_ID, new BHRCheckListItem(bc.getChecklistItemId()).getEmployeeStatusId())
				.eq(HrEmplStatusHistory.EMPLOYEE_ID, empId).first());

		if (bc.getDateCompleted() > 0)
		{
			status="Completed";		//Completed or In Progress;
			date = DateUtils.getDate(bc.getDateCompleted(), bc.getTimeCompleted());
			statusDate=DateUtils.getDateAndTimeFormatted(date);
		}
		else
		{
			//If 0 use status history date
			status="In Progress";
			date = bes.getRecordChangeDate();
			statusDate=DateUtils.getDateAndTimeFormatted(bes.getRecordChangeDate());
		}
		
		personId=bes.getEmployee().getPersonId();
		taskId=bc.getChecklistDetailId();
	}
	
	private String taskName;
	private String status;
	private String statusDate;
	private String personId;
	private String taskId;
	private Date date;

	
	public String getTaskName()
	{
		return taskName;
	}
	public void setTaskName(String taskName)
	{
		this.taskName=taskName;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getStatusDate() {
		return statusDate;
	}
	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId=taskId;
	}

	public Date Date() {
		return date;
	}

	public void Date(Date date) {
		this.date = date;
	}

}

	
