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
package com.arahant.services.standard.inventory.quoteParent;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class SearchLocationsInput extends TransmitInputBase {

	@Validation (table = "location_cost",column = "location_cost_id",required = false)
	private String locationId;
	@Validation (table = "location_cost",column = "description",required = false)
	private String name;
	@Validation (min = 2,max = 5,required = false)
	private int nameSearchType;
	

	public String getLocationId()
	{
		return locationId;
	}
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}
	public String getName()
	{
		return modifyForSearch(name,nameSearchType);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getNameSearchType()
	{
		return nameSearchType;
	}
	public void setNameSearchType(int nameSearchType)
	{
		this.nameSearchType = nameSearchType;
	}


}

	
