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
package com.arahant.services.standard.inventory.productAttribute;
import com.arahant.business.BProductAttribute;

public class ListAttributesReturnItem {
	
	public ListAttributesReturnItem()
	{
		
	}

	ListAttributesReturnItem (BProductAttribute ba)
	{
		id=ba.getId();
		name=ba.getAttribute();
		dataType=ba.getChoiceType();
		inactiveDate=ba.getInactiveDate();

	}

        private String id;
	private String name;
	private String dataType;
	private int inactiveDate;

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
	public String getDataType()
	{
		return dataType;
	}
	public void setDataType(String dataType)
	{
		this.dataType=dataType;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}

}

	
