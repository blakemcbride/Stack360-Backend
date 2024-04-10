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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BHrEmergencyContact;
import com.arahant.annotation.Validation;
import com.arahant.beans.HrEmergencyContact;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;

public class NewEmergencyContactInput extends TransmitInputBase {

	void setData(BHrEmergencyContact bc)
	{
		bc.setPerson(new BPerson(personId).getPerson());
		bc.setName(contactName);
		bc.setRelationship(relationship);
		bc.setAddress(address);
		int seq = ArahantSession.getHSU().createCriteria(HrEmergencyContact.class).eq(HrEmergencyContact.PERSON, bc.getPerson()).count();
		bc.setSeqno(seq);
		bc.setHomePhone(homePhone);
		bc.setCellPhone(cellPhone);
		bc.setWorkPhone(workPhone);
	}
	
	@Validation (required=true)
	private String personId;
	@Validation (required=true)
	private String contactName;
	@Validation (required=false)
	private String relationship;
	@Validation (required=false)
	private String address;
	@Validation (required=false)
	private String homePhone;
	@Validation (required=false)
	private String cellPhone;
	@Validation (required=false)
	private String workPhone;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getContactName()
	{
		return contactName;
	}
	public void setContactName(String contactName)
	{
		this.contactName=contactName;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(String relationship)
	{
		this.relationship=relationship;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address=address;
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

	
