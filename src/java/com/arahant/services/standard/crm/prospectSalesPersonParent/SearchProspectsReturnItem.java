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
package com.arahant.services.standard.crm.prospectSalesPersonParent;
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
		statusCode=bc.getStatusCode();
		sourceCode=bc.getSourceCode();
		firstContactDate=bc.getFirstContactDate();
		lastName=bc.getMainContactLname();
		firstName=bc.getMainContactFname();
		lastLogDate=bc.getLastLogDate();
		lastLogTime=bc.getLastLogTime();

	}
	
	private String id;
	private String name;
	private String identifier;
	private String statusCode;
	private String sourceCode;
	private int firstContactDate;
	private String lastName;
	private String firstName;
	private int lastLogDate;
	private int lastLogTime;

	public int getLastLogDate() {
		return lastLogDate;
	}

	public void setLastLogDate(int lastLogDate) {
		this.lastLogDate = lastLogDate;
	}

	public int getLastLogTime() {
		return lastLogTime;
	}

	public void setLastLogTime(int lastLogTime) {
		this.lastLogTime = lastLogTime;
	}
	
	
	

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
	public String getStatusCode()
	{
		return statusCode;
	}
	public void setStatusCode(String statusCode)
	{
		this.statusCode=statusCode;
	}
	public String getSourceCode()
	{
		return sourceCode;
	}
	public void setSourceCode(String sourceCode)
	{
		this.sourceCode=sourceCode;
	}
	public int getFirstContactDate()
	{
		return firstContactDate;
	}
	public void setFirstContactDate(int firstContactDate)
	{
		this.firstContactDate=firstContactDate;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}

}

	
