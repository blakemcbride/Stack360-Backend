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
 *
 */
package com.arahant.services.standard.misc.documentSearch;
import com.arahant.business.BFormType;

public class ListDocumentTypesReturnItem {
	
	public ListDocumentTypesReturnItem()
	{
		
	}

	ListDocumentTypesReturnItem (BFormType bc)
	{
		typeName=bc.getCode() + " - " + bc.getDescription();
		typeId=bc.getId();
	}
	
	private String typeName;
	private String typeId;
	

	public String getTypeName()
	{
		return typeName;
	}
	public void setTypeName(String typeName)
	{
		this.typeName=typeName;
	}
	public String getTypeId()
	{
		return typeId;
	}
	public void setTypeId(String typeId)
	{
		this.typeId=typeId;
	}

}

	
