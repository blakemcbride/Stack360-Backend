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
package com.arahant.services.standard.project.orgGroupProjectListClient;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class SearchAssignablePersonsForProjectReturnItem {
	
	public SearchAssignablePersonsForProjectReturnItem()
	{
		;
	}

	SearchAssignablePersonsForProjectReturnItem (BPerson bc)
	{
		
		personId=bc.getPersonId();
		firstName=bc.getFirstName();
		middleName=bc.getMiddleName();
		lastName=bc.getLastName();
		priority=10000;

	}
	
	private String personId;
private String firstName;
private String middleName;
private String lastName;
private int priority;
;

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String firstName)
	{
		this.firstName=firstName;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(String middleName)
	{
		this.middleName=middleName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String lastName)
	{
		this.lastName=lastName;
	}
	public int getPriority()
	{
		return priority;
	}
	public void setPriority(int priority)
	{
		this.priority=priority;
	}

}

	
