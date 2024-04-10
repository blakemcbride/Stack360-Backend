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
package com.arahant.services.standard.hr.hrAccruedTimeOff;
import com.arahant.business.BTimeOffRequest;


/**
 * 
 *
 *
 */
public class ListApprovedTimeOffRequestsReturnItem {
	
	public ListApprovedTimeOffRequestsReturnItem()
	{

	}

	ListApprovedTimeOffRequestsReturnItem (BTimeOffRequest bc)
	{
		
		id=bc.getId();
		firstDate=bc.getFirstDateOff();
		firstTime=bc.getFirstTimeOff();
		lastDate=bc.getLastDateOff();
		lastTime=bc.getLastTimeOff();
		requestingComments=bc.getRequestingComment();
		project=bc.getBenefitName();
	}
	
	private String id;
	private int firstDate;
	private int firstTime;
	private int lastDate;
	private int lastTime;
	private String project;
	private String requestingComments;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getFirstDate()
	{
		return firstDate;
	}
	public void setFirstDate(int firstDate)
	{
		this.firstDate=firstDate;
	}
	public int getFirstTime()
	{
		return firstTime;
	}
	public void setFirstTime(int firstTime)
	{
		this.firstTime=firstTime;
	}
	public int getLastDate()
	{
		return lastDate;
	}
	public void setLastDate(int lastDate)
	{
		this.lastDate=lastDate;
	}
	public int getLastTime()
	{
		return lastTime;
	}
	public void setLastTime(int lastTime)
	{
		this.lastTime=lastTime;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getRequestingComments() {
		return requestingComments;
	}

	public void setRequestingComments(String requestingComments) {
		this.requestingComments = requestingComments;
	}

}

	
