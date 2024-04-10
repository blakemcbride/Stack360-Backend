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
package com.arahant.services.standard.crm.prospectParent;

import com.arahant.business.BProspectStatus;


/**
 * 
 *
 *
 */
public class SearchProspectStatusesReturnItem {
	
	public SearchProspectStatusesReturnItem()
	{
	
	}

	SearchProspectStatusesReturnItem (BProspectStatus ps)
	{
		
		id=ps.getId();
		code=ps.getCode();
		description=ps.getDescription();
		status=ps.getActive().equals("Y")?"Active":"Inactive";

	}
	
	private String id;
	private String code;
	private String description;
	private String status;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}

}

	
