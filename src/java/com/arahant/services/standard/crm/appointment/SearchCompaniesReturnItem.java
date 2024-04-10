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
package com.arahant.services.standard.crm.appointment;
import com.arahant.business.BCompanyBase;


/**
 * 
 *
 *
 */
public class SearchCompaniesReturnItem {
	
	public SearchCompaniesReturnItem()
	{
		;
	}

	SearchCompaniesReturnItem (BCompanyBase bc)
	{
		
		companyId=bc.getCompanyId();
		name=bc.getName();
		identifier=bc.getIdentifier();
		typeFormatted=bc.getTypeFormatted();

	}
	
	private String companyId;
	private String name;
	private String identifier;
	private String typeFormatted;
	

	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
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
	public String getTypeFormatted()
	{
		return typeFormatted;
	}
	public void setTypeFormatted(String typeFormatted)
	{
		this.typeFormatted=typeFormatted;
	}

}

	
