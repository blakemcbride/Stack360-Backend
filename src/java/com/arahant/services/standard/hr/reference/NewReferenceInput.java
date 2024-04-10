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
 *
 */
package com.arahant.services.standard.hr.reference;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BPersonalReference;
import com.arahant.annotation.Validation;
import com.arahant.business.BPerson;

public class NewReferenceInput extends TransmitInputBase {

	void setData(BPersonalReference bc)
	{
		bc.setPerson(new BPerson(personId).getPerson());
		bc.setCompany(companyName);
		bc.setPhone(phone);
		bc.setReferenceName(referenceName);

		if (relationship.length() > 1)
		{
			bc.setRelationshipOther(relationship);
			bc.setRelationshipType('O');
		}
		else
			bc.setRelationshipType(relationship.charAt(0));

		bc.setYearsKnown((short)yearsKnown);
		bc.setAddress(address);
	}
	
	@Validation (table="personal_reference", column="person_id", required=true)
	private String personId;
	@Validation (table="personal_reference", column="company", required=false)
	private String companyName;
	@Validation (table="personal_reference", column="phone", required=false)
	private String phone;
	@Validation (table="personal_reference", column="reference_name", required=true)
	private String referenceName;
	@Validation (required=true)
	private String relationship;
	@Validation (table="personal_reference", column="years_known", required=false)
	private int yearsKnown;
	@Validation (table="personal_reference", column="address", required=false)
	private String address;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName=companyName;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getReferenceName()
	{
		return referenceName;
	}
	public void setReferenceName(String referenceName)
	{
		this.referenceName=referenceName;
	}
	public String getRelationship()
	{
		return relationship;
	}
	public void setRelationship(String relationship)
	{
		this.relationship=relationship;
	}
	public int getYearsKnown()
	{
		return yearsKnown;
	}
	public void setYearsKnown(int yearsKnown)
	{
		this.yearsKnown=yearsKnown;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}

}

	
