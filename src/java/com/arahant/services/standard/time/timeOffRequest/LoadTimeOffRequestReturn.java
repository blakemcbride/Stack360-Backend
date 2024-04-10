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
package com.arahant.services.standard.time.timeOffRequest;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BTimeOffRequest;


/**
 * 
 *
 *
 */
public class LoadTimeOffRequestReturn extends TransmitReturnBase {

	void setData(BTimeOffRequest bc)
	{
		
		requestingComment=bc.getRequestingComment();
		requestDateFormatted=bc.getRequestDateFormatted();
		approvingPersonFormatted=bc.getApprovingPersonFormatted();
		firstDateOff=bc.getFirstDateOff();
		firstTimeOff=bc.getFirstTimeOff();
		lastDateOff=bc.getLastDateOff();
		lastTimeOff=bc.getLastTimeOff();
		projectId=bc.getProjectId();

	}
	
	private String requestingComment;
	private String requestDateFormatted;
	private String approvingPersonFormatted;
	private int firstDateOff;
	private int firstTimeOff;
	private int lastDateOff;
	private int lastTimeOff;
	private String projectId;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	

	public String getRequestingComment()
	{
		return requestingComment;
	}
	public void setRequestingComment(String requestingComment)
	{
		this.requestingComment=requestingComment;
	}
	public String getRequestDateFormatted()
	{
		return requestDateFormatted;
	}
	public void setRequestDateFormatted(String requestDateFormatted)
	{
		this.requestDateFormatted=requestDateFormatted;
	}
	public String getApprovingPersonFormatted()
	{
		return approvingPersonFormatted;
	}
	public void setApprovingPersonFormatted(String approvingPersonFormatted)
	{
		this.approvingPersonFormatted=approvingPersonFormatted;
	}
	public int getFirstDateOff()
	{
		return firstDateOff;
	}
	public void setFirstDateOff(int firstDateOff)
	{
		this.firstDateOff=firstDateOff;
	}
	public int getFirstTimeOff()
	{
		return firstTimeOff;
	}
	public void setFirstTimeOff(int firstTimeOff)
	{
		this.firstTimeOff=firstTimeOff;
	}
	public int getLastDateOff()
	{
		return lastDateOff;
	}
	public void setLastDateOff(int lastDateOff)
	{
		this.lastDateOff=lastDateOff;
	}
	public int getLastTimeOff()
	{
		return lastTimeOff;
	}
	public void setLastTimeOff(int lastTimeOff)
	{
		this.lastTimeOff=lastTimeOff;
	}

}

	
