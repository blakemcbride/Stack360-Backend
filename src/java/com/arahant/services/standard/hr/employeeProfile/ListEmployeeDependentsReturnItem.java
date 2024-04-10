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
package com.arahant.services.standard.hr.employeeProfile;
import com.arahant.business.BHREmplDependent;

public class ListEmployeeDependentsReturnItem {
	
	public ListEmployeeDependentsReturnItem()
	{
		
	}

	ListEmployeeDependentsReturnItem (BHREmplDependent bc)
	{
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		ssn=bc.getSsn();
		id=bc.getDependentId();
		currentStatus=bc.getStatus();
		relationship=bc.getRelationshipType();
	}
	
	private String firstName;
	private String lastName;
	private String ssn;
	private String id;
	private String currentStatus;
	private String relationship;
	

	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(String ssn)
	{
		this.ssn=ssn;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getCurrentStatus()
	{
		return currentStatus;
	}
	public void setCurrentStatus(String currentStatus)
	{
		this.currentStatus=currentStatus;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(String relationship)
	{
		this.relationship=relationship;
	}

}

	
