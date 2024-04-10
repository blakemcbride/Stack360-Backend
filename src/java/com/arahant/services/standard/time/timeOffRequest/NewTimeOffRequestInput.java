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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BTimeOffRequest;
import com.arahant.annotation.Validation;
import com.arahant.beans.TimeOffRequest;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewTimeOffRequestInput extends TransmitInputBase {

	void setData(BTimeOffRequest bc)
	{
		
		bc.setPersonId(personId);
		bc.setFirstDateOff(firstDateOff);
		bc.setFirstTimeOff(firstTimeOff);
		bc.setLastDateOff(lastDateOff);
		bc.setLastTimeOff(lastTimeOff);
		bc.setRequestingComment(requestingComment);
		bc.setStatus(TimeOffRequest.ORIGINATED);
		bc.setProjectId(projectId);
	}
	
	@Validation (required=true)
	private String projectId;
	@Validation (required=true)
	private String personId;
	@Validation (required=true, type="date")
	private int firstDateOff;
	@Validation (required=false, type="time")
	private int firstTimeOff;
	@Validation (required=true, type="date")
	private int lastDateOff;
	@Validation (required=false, type="time")
	private int lastTimeOff;
	@Validation (table="time_off_request",column="requesting_comment",required=false)
	private String requestingComment;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
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
	public String getRequestingComment()
	{
		return requestingComment;
	}
	public void setRequestingComment(String requestingComment)
	{
		this.requestingComment=requestingComment;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

}

	
