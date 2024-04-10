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
import com.arahant.business.BProjectChecklistDetail;


/**
 * 
 *
 *
 */
public class ListCheckListDetailsForRouteStopReturnItem {
	
	public ListCheckListDetailsForRouteStopReturnItem()
	{
		;
	}

	ListCheckListDetailsForRouteStopReturnItem (BProjectChecklistDetail bc)
	{
		
		description=bc.getDescription();
		checkListId=bc.getCheckListId();
		checkListDetailId=(bc.getCheckListDetailId()!=null)?bc.getCheckListDetailId():"";
		completedDate=bc.getCompletedDate();
		required=bc.getRequired();

	}
	
	private String description;
	private String checkListId;
	private String checkListDetailId;
	private int completedDate;
	private String required;


	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getCheckListId()
	{
		return checkListId;
	}
	public void setCheckListId(String checkListId)
	{
		this.checkListId=checkListId;
	}
	public String getCheckListDetailId()
	{
		return checkListDetailId;
	}
	public void setCheckListDetailId(String checkListDetailId)
	{
		this.checkListDetailId=checkListDetailId;
	}
	public int getCompletedDate()
	{
		return completedDate;
	}
	public void setCompletedDate(int completedDate)
	{
		this.completedDate=completedDate;
	}
	public String getRequired()
	{
		return required;
	}
	public void setRequired(String required)
	{
		this.required=required;
	}

}

	
