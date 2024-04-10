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
package com.arahant.services.standard.hr.emergencyContact;
import com.arahant.business.BHrEmergencyContact;

public class ListEmergencyContactsReturnItem {
	
	public ListEmergencyContactsReturnItem()
	{
		
	}

	ListEmergencyContactsReturnItem (BHrEmergencyContact bc)
	{
		contactName=bc.getName();
		contactId=bc.getContactId();
		relationship=bc.getRelationship();
		homePhone=bc.getHomePhone();
		cellPhone=bc.getCellPhone();
		workPhone=bc.getWorkPhone();
	}
	
	private String contactName;
	private String contactId;
	private String relationship;
	private String homePhone;
	private String cellPhone;
	private String workPhone;
	

	public String getContactName()
	{
		return contactName;
	}
	public void setContactName(String contactName)
	{
		this.contactName=contactName;
	}
	public String getContactId()
	{
		return contactId;
	}
	public void setContactId(String contactId)
	{
		this.contactId=contactId;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(String relationship)
	{
		this.relationship=relationship;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getWorkPhone() {
		return workPhone;
	}

	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
}

	
