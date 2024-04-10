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
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BProperty;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SavePropertyInput extends TransmitInputBase {

	void setData(BProperty bc)
	{
		
		bc.setName(name);
		bc.setDescription(description);
		bc.setValue(value);

	}
	
	@Validation (table="property",column="prop_name",required=true)
	private String name;
	@Validation (required=false)
private String description;
	@Validation (table="property",column="prop_name",required=false)
private String id;
	@Validation (table="property",column="prop_value",required=false)
private String value;
;

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}

}

	
