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
package com.arahant.services.standard.site.properties;
import com.arahant.business.BProperty;


/**
 * 
 *
 *
 */
public class ListPropertiesReturnItem {
	
	public ListPropertiesReturnItem()
	{
		
	}

	ListPropertiesReturnItem (BProperty bc)
	{
		
		id=bc.getName();
		name=bc.getName();
		value=bc.getValue();
		description=bc.getDescription();

	}
	
	private String id;
        private String name;
        private String value;
        private String description;


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
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
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

	
