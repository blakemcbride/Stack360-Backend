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
package com.arahant.services.standard.hr.garnishmentType;
import com.arahant.business.BGarnishmentType;


/**
 * 
 *
 *
 */
public class ListTypesReturnItem {
	
	public ListTypesReturnItem()
	{
		
	}

	ListTypesReturnItem (BGarnishmentType bc)
	{
		id=bc.getId();
		wageTypeId=bc.getWageTypeId();
		description=bc.getDescription();
		lastActiveDate=bc.getLastActiveDate();
		wageType=bc.getWageName();
	}
	
	private String id;
	private String wageTypeId;
	private String description;
	private int lastActiveDate;
	private String wageType;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

	public String getWageTypeId() {
		return wageTypeId;
	}

	public void setWageTypeId(String wageTypeId) {
		this.wageTypeId = wageTypeId;
	}
	
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getWageType() {
		return wageType;
	}

	public void setWageType(String wageType) {
		this.wageType = wageType;
	}
}

	
