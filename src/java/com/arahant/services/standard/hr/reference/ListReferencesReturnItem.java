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
import com.arahant.business.BPersonalReference;

public class ListReferencesReturnItem {
	
	public ListReferencesReturnItem()
	{
		
	}

	ListReferencesReturnItem (BPersonalReference bc)
	{
		referenceName=bc.getReferenceName();
		relationship=bc.getRelationshipType() == 'O'?bc.getRelationshipOther():bc.getRelationshipType() + "";
		phone=bc.getPhone();
		companyName=bc.getCompany();
		referenceId=bc.getReferenceId();
	}
	
	private String referenceName;
	private String relationship;
	private String phone;
	private String companyName;
	private String referenceId;
	

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
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getCompanyName()
	{
		return companyName;
	}
	public void setCompanyName(String companyName)
	{
		this.companyName=companyName;
	}
	public String getReferenceId()
	{
		return referenceId;
	}
	public void setReferenceId(String referenceId)
	{
		this.referenceId=referenceId;
	}

}

	
