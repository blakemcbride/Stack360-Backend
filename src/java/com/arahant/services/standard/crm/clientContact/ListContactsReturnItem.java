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
package com.arahant.services.standard.crm.clientContact;
import com.arahant.business.BPerson;


/**
 * 
 *
 *
 */
public class ListContactsReturnItem {
	
	public ListContactsReturnItem()
	{
		;
	}

	ListContactsReturnItem (BPerson bc, String orgGroupId)
	{
		
		id=bc.getPersonId();
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
		jobTitle=bc.getJobTitle();
		workPhone=bc.getWorkPhoneNumber();
		
		primary=bc.isPrimary(orgGroupId)?"Yes":"No";

	}
	
	private String id;
	private String firstName;
	private String lastName;
	private String jobTitle;
	private String workPhone;
	private String primary;


	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
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

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getWorkPhone()
	{
		return workPhone;
	}
	public void setWorkPhone(String workPhone)
	{
		this.workPhone=workPhone;
	}
	public String getPrimary()
	{
		return primary;
	}
	public void setPrimary(String primary)
	{
		this.primary=primary;
	}

}

	
