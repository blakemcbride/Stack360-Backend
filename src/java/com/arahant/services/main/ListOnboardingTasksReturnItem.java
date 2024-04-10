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
package com.arahant.services.main;
import com.arahant.business.BAgency;

public class ListOnboardingTasksReturnItem {
	
	public ListOnboardingTasksReturnItem()
	{
		
	}

	ListOnboardingTasksReturnItem (BAgency bc)
	{
		
//		taskName=bc.getTaskName();
//		select=bc.getSelect();
//		taskDescription=bc.getTaskDescription();
//		status=bc.getStatus();
//		statusDate=bc.getStatusDate();

	}
	
	private String taskName;
	private boolean select;
	private String taskDescription;
	private String status;
	private String statusDate;
	private int index;
	private String fileUrl;
	private String taskId;

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	public String getTaskName()
	{
		return taskName;
	}
	public void setTaskName(String taskName)
	{
		this.taskName=taskName;
	}
	public boolean getSelect()
	{
		return select;
	}
	public void setSelect(boolean select)
	{
		this.select=select;
	}
	public String getTaskDescription()
	{
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription)
	{
		this.taskDescription=taskDescription;
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

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}

	
