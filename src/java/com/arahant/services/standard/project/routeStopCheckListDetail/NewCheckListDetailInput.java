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
package com.arahant.services.standard.project.routeStopCheckListDetail;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProjectChecklistDetail;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewCheckListDetailInput extends TransmitInputBase {

	void setData(BProjectChecklistDetail bc)
	{
		
		bc.setCheckListId(checkListId);
		bc.setProjectId(projectId);
	
		bc.setCompletedName(completedName);
		bc.setCompletedDate(completedDate);
		bc.setComments(comments);

	}
	
	@Validation (required=true)
	private String checkListId;
	@Validation (required=true)
private String projectId;

	@Validation (required=true)
private String completedName;
	@Validation (type="date",required=true)
private int completedDate;
	@Validation (required=false)
private String comments;


	public String getCheckListId()
	{
		return checkListId;
	}
	public void setCheckListId(String checkListId)
	{
		this.checkListId=checkListId;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}

	public String getCompletedName()
	{
		return completedName;
	}
	public void setCompletedName(String completedName)
	{
		this.completedName=completedName;
	}
	public int getCompletedDate()
	{
		return completedDate;
	}
	public void setCompletedDate(int completedDate)
	{
		this.completedDate=completedDate;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments=comments;
	}

}

	
