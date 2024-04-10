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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPersonalReference;

public class LoadReferenceReturn extends TransmitReturnBase {

	void setData(BPersonalReference bc)
	{
		referenceName=bc.getReferenceName();
		relationship=bc.getRelationshipType()=='O'?bc.getRelationshipOther():bc.getRelationshipType()+"";
		companyName=bc.getCompany();
		phone=bc.getPhone();
		address=bc.getAddress();
		yearsKnown=bc.getYearsKnown();
	}
	
	private String referenceName;
	private String relationship;
	private String companyName;
	private String phone;
	private String address;
	private int yearsKnown;
	

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
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}
	public int getYearsKnown()
	{
		return yearsKnown;
	}
	public void setYearsKnown(int yearsKnown)
	{
		this.yearsKnown=yearsKnown;
	}

}

	
