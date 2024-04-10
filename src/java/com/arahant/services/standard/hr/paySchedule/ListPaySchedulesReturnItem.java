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
package com.arahant.services.standard.hr.paySchedule;
import com.arahant.business.BPaySchedule;


/**
 * 
 *
 *
 */
public class ListPaySchedulesReturnItem {
	
	public ListPaySchedulesReturnItem()
	{
		
	}

	ListPaySchedulesReturnItem (BPaySchedule bc)
	{
		
		name=bc.getName();
		lastPayPeriodDate=bc.getLastPayPeriodDate();
		description=bc.getDescription();
		id=bc.getId();
	}
	
	private String name;
	private int lastPayPeriodDate;
	private String description;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getLastPayPeriodDate()
	{
		return lastPayPeriodDate;
	}
	public void setLastPayPeriodDate(int lastPayPeriodDate)
	{
		this.lastPayPeriodDate=lastPayPeriodDate;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}

}

	
