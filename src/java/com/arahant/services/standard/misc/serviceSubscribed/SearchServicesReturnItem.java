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
package com.arahant.services.standard.misc.serviceSubscribed;
import com.arahant.business.BServiceSubscribed;

public class SearchServicesReturnItem {
	
	public SearchServicesReturnItem()
	{
		
	}

	SearchServicesReturnItem (BServiceSubscribed bs)
	{
		
		name=bs.getServiceName();
		firstActiveDate=bs.getFirstActiveDate();
		lastActiveDate=bs.getLastActiveDate();
		descriptionPreview=bs.getDescription();
		id=bs.getServiceId();
	}
	
	private String name;
	private int firstActiveDate;
	private int lastActiveDate;
	private String descriptionPreview;
	private String id;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getFirstActiveDate()
	{
		return firstActiveDate;
	}
	public void setFirstActiveDate(int firstActiveDate)
	{
		this.firstActiveDate=firstActiveDate;
	}
	public int getLastActiveDate()
	{
		return lastActiveDate;
	}
	public void setLastActiveDate(int lastActiveDate)
	{
		this.lastActiveDate=lastActiveDate;
	}
	public String getDescriptionPreview()
	{
		return descriptionPreview;
	}
	public void setDescriptionPreview(String descriptionPreview)
	{
		this.descriptionPreview=descriptionPreview;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
