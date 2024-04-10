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
package com.arahant.services.standard.hr.projectCheckList;

import com.arahant.business.BRouteStopChecklist;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProjectChecklistDetail;


/**
 * 
 *
 *
 */
public class LoadCheckListDetailReturn extends TransmitReturnBase {

	void setData(BProjectChecklistDetail bc)
	{
		
		completedName=bc.getCompletedName();
		entryNameFormatted=bc.getEntryNameFormatted();
		entryDateTimeFormatted=bc.getEntryDateTimeFormatted();
		comments=bc.getComments();
		detail=bc.getDetail();

	}
	void setData(BRouteStopChecklist b) {
		detail=b.getDetail();
	}
	
	private String completedName;
	private String entryNameFormatted;
	private String entryDateTimeFormatted;
	private String comments;
	private String detail;


	public String getCompletedName()
	{
		return completedName;
	}
	public void setCompletedName(String completedName)
	{
		this.completedName=completedName;
	}
	public String getEntryNameFormatted()
	{
		return entryNameFormatted;
	}
	public void setEntryNameFormatted(String entryNameFormatted)
	{
		this.entryNameFormatted=entryNameFormatted;
	}
	public String getEntryDateTimeFormatted()
	{
		return entryDateTimeFormatted;
	}
	public void setEntryDateTimeFormatted(String entryDateTimeFormatted)
	{
		this.entryDateTimeFormatted=entryDateTimeFormatted;
	}
	public String getComments()
	{
		return comments;
	}
	public void setComments(String comments)
	{
		this.comments=comments;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail=detail;
	}

	

}

	
