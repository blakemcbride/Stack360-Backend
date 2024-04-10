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
package com.arahant.services.standard.crm.clientParent;
import com.arahant.business.BProspectCompany;


/**
 * 
 *
 *
 */
public class SearchProspectsReturnItem {
	
	public SearchProspectsReturnItem()
	{
		;
	}

	SearchProspectsReturnItem (BProspectCompany bc)
	{
		
		id=bc.getId();
		name=bc.getName();
		identifier=bc.getIdentifier();
		source=bc.getSource().getCode();
		status=bc.getStatus().getCode();
		mainContactFirstName=bc.getMainContactFname();
		mainContactLastName=bc.getMainContactLname();

	}
	
	private String id;
	private String name;
	private String identifier;
	private String source;
	private String status;
	private String mainContactFirstName;
	private String mainContactLastName;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public void setIdentifier(String identifier)
	{
		this.identifier=identifier;
	}
	public String getSource()
	{
		return source;
	}
	public void setSource(String source)
	{
		this.source=source;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status=status;
	}
	public String getMainContactFirstName()
	{
		return mainContactFirstName;
	}
	public void setMainContactFirstName(String mainContactFirstName)
	{
		this.mainContactFirstName=mainContactFirstName;
	}
	public String getMainContactLastName()
	{
		return mainContactLastName;
	}
	public void setMainContactLastName(String mainContactLastName)
	{
		this.mainContactLastName=mainContactLastName;
	}

}

	
