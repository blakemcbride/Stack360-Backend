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
package com.arahant.services.standard.misc.onboardingTasks;
import com.arahant.business.BOnboardingTask;


/**
 * 
 *
 *
 */
public class ListTasksForConfigReturnItem {
	
	public ListTasksForConfigReturnItem()
	{
		
	}

	ListTasksForConfigReturnItem (BOnboardingTask bc)
	{		
		taskName=bc.getOnboardingTaskName();
		description=bc.getDescription();
		taskId=bc.getOnboardingTaskId();
		completeByDays=bc.getCompletedByDays();
		screenId=bc.getScreen().getScreenId();
	}
	
	private String taskName;
	private String description;
	private String taskId;
	private int completeByDays;
	private String screenId;
	

	public String getTaskName()
	{
		return taskName;
	}
	public void setTaskName(String taskName)
	{
		this.taskName=taskName;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getTaskId()
	{
		return taskId;
	}
	public void setTaskId(String taskId)
	{
		this.taskId=taskId;
	}
	public int getCompleteByDays()
	{
		return completeByDays;
	}
	public void setCompleteByDays(int completeByDays)
	{
		this.completeByDays=completeByDays;
	}
	public String getScreenId()
	{
		return screenId;
	}
	public void setScreenId(String screenId)
	{
		this.screenId=screenId;
	}

}

	
