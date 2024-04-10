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
 *
 */
package com.arahant.services.standard.crm.salesTasks;

public class SearchProspectLogsByTaskReturnItem {
	
	public SearchProspectLogsByTaskReturnItem()
	{
		
	}

	SearchProspectLogsByTaskReturnItem (ProspectLogResults plr)
	{
		prospectName=plr.getOrgGroup().getName();
		prospectId=plr.getOrgGroup().getOrgGroupId();
		taskName=plr.getTaskName();
		dateCompleted=plr.getDateCompleted();
		logId=plr.getProspectLogId();
		resultId=plr.getSalesActivityResult().getSalesActivityResultId();
		activityId=plr.getSalesActivity().getSalesActivityId();
	}
	
	private String prospectName;
	private String prospectId;
	private String taskName;
	private int dateCompleted;
	private String logId;
	private String resultId;
	private String activityId;
	

	public String getProspectName()
	{
		return prospectName;
	}
	public void setProspectName(String prospectName)
	{
		this.prospectName=prospectName;
	}
	public String getProspectId()
	{
		return prospectId;
	}
	public void setProspectId(String prospectId)
	{
		this.prospectId=prospectId;
	}
	public String getTaskName()
	{
		return taskName;
	}
	public void setTaskName(String taskName)
	{
		this.taskName=taskName;
	}
	public int getDateCompleted()
	{
		return dateCompleted;
	}
	public void setDateCompleted(int dateCompleted)
	{
		this.dateCompleted=dateCompleted;
	}
	public String getLogId()
	{
		return logId;
	}
	public void setLogId(String logId)
	{
		this.logId=logId;
	}
	public String getResultId()
	{
		return resultId;
	}
	public void setResultId(String resultId)
	{
		this.resultId=resultId;
	}
	public String getActivityId()
	{
		return activityId;
	}
	public void setActivityId(String activityId)
	{
		this.activityId=activityId;
	}

}

	
