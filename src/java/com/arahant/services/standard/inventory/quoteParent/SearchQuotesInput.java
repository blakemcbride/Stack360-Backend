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


public class SearchQuotesInput extends TransmitInputBase {

	@Validation (table = "quote_table",column = "quote_name",required = false)
	private String name;
	@Validation (min = 2,max = 5,required = false)
	private int nameSearchType;
	@Validation (table = "quote_table",column = "quote_description",required = false)
	private String description;
	@Validation (min = 2,max = 5,required = false)
	private int descriptionSearchType;
	@Validation (type = "date",required = false)
	private int createdFromDate;
	@Validation (type = "date",required = false)
	private int createdToDate;
	@Validation (type = "date",required = false)
	private int finalizedFromDate;
	@Validation (type = "date",required = false)
	private int finalizedToDate;
	@Validation (table = "quote_table",column = "client_id",required = false)
	private String clientId;
	@Validation (table = "quote_table",column = "location_cost_id",required = false)
	private String locationId;
	

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
	public String getDescription()
	{
		return modifyForSearch(description,descriptionSearchType);
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getDescriptionSearchType()
	{
		return descriptionSearchType;
	}
	public void setDescriptionSearchType(int descriptionSearchType)
	{
		this.descriptionSearchType = descriptionSearchType;
	}
	public int getCreatedFromDate()
	{
		return createdFromDate;
	}
	public void setCreatedFromDate(int createdFromDate)
	{
		this.createdFromDate = createdFromDate;
	}
	public int getCreatedToDate()
	{
		return createdToDate;
	}
	public void setCreatedToDate(int createdToDate)
	{
		this.createdToDate = createdToDate;
	}
	public int getFinalizedFromDate()
	{
		return finalizedFromDate;
	}
	public void setFinalizedFromDate(int finalizedFromDate)
	{
		this.finalizedFromDate = finalizedFromDate;
	}
	public int getFinalizedToDate()
	{
		return finalizedToDate;
	}
	public void setFinalizedToDate(int finalizedToDate)
	{
		this.finalizedToDate = finalizedToDate;
	}
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getLocationId()
	{
		return locationId;
	}
	public void setLocationId(String locationId)
	{
		this.locationId = locationId;
	}


}

	
