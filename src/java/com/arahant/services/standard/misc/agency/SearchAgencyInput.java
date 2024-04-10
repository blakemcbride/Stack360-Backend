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
package com.arahant.services.standard.misc.agency;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Oct 9, 2009
 *
 */
public class SearchAgencyInput extends TransmitInputBase {

	@Validation (required=false)
	private String identifier;
	@Validation (min=2,max=5,required=false)
	private int identifierSearchType;
	@Validation (required=false)
	private String name;
	@Validation (min=2,max=5,required=false)
	private int nameSearchType;
	@Validation (required=false)
	private String agentFirstName;
	@Validation (min=2,max=5,required=false)
	private int agentFirstNameSearchType;
	@Validation (required=false)
	private String agentLastName;
	@Validation (min=2,max=5,required=false)
	private int agentLastNameSearchType;
	

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
	public String getAgentFirstName()
	{
		return modifyForSearch(agentFirstName,agentFirstNameSearchType);
	}
	public void setAgentFirstName(String agentFirstName)
	{
		this.agentFirstName=agentFirstName;
	}
	public int getAgentFirstNameSearchType()
	{
		return agentFirstNameSearchType;
	}
	public void setAgentFirstNameSearchType(int agentFirstNameSearchType)
	{
		this.agentFirstNameSearchType=agentFirstNameSearchType;
	}
	public String getAgentLastName()
	{
		return modifyForSearch(agentLastName,agentLastNameSearchType);
	}
	public void setAgentLastName(String agentLastName)
	{
		this.agentLastName=agentLastName;
	}
	public int getAgentLastNameSearchType()
	{
		return agentLastNameSearchType;
	}
	public void setAgentLastNameSearchType(int agentLastNameSearchType)
	{
		this.agentLastNameSearchType=agentLastNameSearchType;
	}


}

	
