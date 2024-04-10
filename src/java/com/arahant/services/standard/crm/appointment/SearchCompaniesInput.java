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
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchCompaniesInput extends TransmitInputBase {

	@Validation (table="org_group",column="org_group",required=false)
	private String name;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (table="company_base",column="company_base",required=false)
	private String identifier;
	@Validation (min=2,max=5,required=false)
	private int identifierSearchType;
	@Validation (min=0,max=2,required=false)
	private int type;
	

	public String getName()
	{
		return modifyForSearch(name,nameSearchType);
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getNameSearchType()
	{
		return nameSearchType;
	}
	public void setNameSearchType(int nameSearchType)
	{
		this.nameSearchType=nameSearchType;
	}
	public String getIdentifier()
	{
		return modifyForSearch(identifier,identifierSearchType);
	}
	public void setIdentifier(String identifier)
	{
		this.identifier=identifier;
	}
	public int getIdentifierSearchType()
	{
		return identifierSearchType;
	}
	public void setIdentifierSearchType(int identifierSearchType)
	{
		this.identifierSearchType=identifierSearchType;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}


}

	
