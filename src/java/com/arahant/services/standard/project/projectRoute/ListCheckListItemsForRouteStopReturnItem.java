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
package com.arahant.services.standard.project.projectRoute;
import com.arahant.business.BRouteStopChecklist;


/**
 * 
 *
 *
 */
public class ListCheckListItemsForRouteStopReturnItem {
	
	public ListCheckListItemsForRouteStopReturnItem()
	{
		
	}

	ListCheckListItemsForRouteStopReturnItem (BRouteStopChecklist bc)
	{
		
		id=bc.getId();
		description=bc.getDescription();
		detail=bc.getDetail();
		required=bc.getRequired();
		activeDate=bc.getActiveDate();
		inactiveDate=bc.getInactiveDate();
		requiredFormatted=bc.getRequiredFormatted();
		activeFormatted=bc.getActiveFormatted();

	}
	
	private String id;
	private String description;
	private String detail;
	private boolean required;
	private int activeDate;
	private int inactiveDate;
	private String requiredFormatted;
	private String activeFormatted;

	public String getActiveFormatted() {
		return activeFormatted;
	}

	public void setActiveFormatted(String activeFormatted) {
		this.activeFormatted = activeFormatted;
	}
	
	

	public String getRequiredFormatted() {
		return requiredFormatted;
	}

	public void setRequiredFormatted(String requiredFormatted) {
		this.requiredFormatted = requiredFormatted;
	}

	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail=detail;
	}
	public boolean getRequired()
	{
		return required;
	}
	public void setRequired(boolean required)
	{
		this.required=required;
	}
	public int getActiveDate()
	{
		return activeDate;
	}
	public void setActiveDate(int activeDate)
	{
		this.activeDate=activeDate;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}

}

	
