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
package com.arahant.services.standard.hr.hrParent;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class SearchDependentsReturnItem {
	
	public SearchDependentsReturnItem()
	{
		;
	}

	SearchDependentsReturnItem (final BPerson bc)
	{
		
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		middleName=bc.getMiddleName();
		ssn=bc.getSsn();
		dependentId=bc.getPersonId();

	}
	
	private String firstName;
	private String lastName;
	private String middleName;
	private String ssn;
	private String dependentId;

	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getMiddleName()
	{
		return middleName;
	}
	public void setMiddleName(final String middleName)
	{
		this.middleName=middleName;
	}
	public String getSsn()
	{
		return ssn;
	}
	public void setSsn(final String ssn)
	{
		this.ssn=ssn;
	}
	public String getDependentId()
	{
		return dependentId;
	}
	public void setDependentId(final String dependentId)
	{
		this.dependentId=dependentId;
	}

}

	
