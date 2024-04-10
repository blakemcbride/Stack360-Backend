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
package com.arahant.services.standard.crm.salesActivity;
import com.arahant.business.BSalesActivity;


/**
 * 
 *
 *
 */
public class ListActivitiesReturnItem {
	
	public ListActivitiesReturnItem()
	{

	}

	ListActivitiesReturnItem (BSalesActivity bsa)
	{
		id = bsa.getSalesActivityId();
		code = bsa.getActivityCode();
		description = bsa.getDescription();
		lastActiveDate = bsa.getLastActiveDate();
		points = bsa.getSalesPoints();
		allCompanies=(bsa.getCompanyId() == null || bsa.getCompanyId().equals(""));
	}

	private int lastActiveDate;
	private short points;
	private String description;
	private String code;
	private String id;
	private boolean allCompanies;


	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}
	public short getPoints()
	{
		return points;
	}
	public void setPoints(short points)
	{
		this.points=points;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}
}

	
