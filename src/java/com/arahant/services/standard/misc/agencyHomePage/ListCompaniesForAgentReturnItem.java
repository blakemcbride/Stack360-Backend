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
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.business.BCompany;

public class ListCompaniesForAgentReturnItem {
	
	public ListCompaniesForAgentReturnItem()
	{
		
	}

	ListCompaniesForAgentReturnItem (BCompany bc)
	{
		mainContactLastName=bc.getMainContactLname();
		mainContactFirstName=bc.getMainContactFname();
		mainContactId=bc.getMainContactPersonId();
		companyId=bc.getCompanyId();
		companyName=bc.getName();
		companyExternalRef=bc.getIdentifier();
	}
	
	private String mainContactLastName;
	private String mainContactFirstName;
	private String companyId;
	private String companyName;
	private String companyExternalRef;
	private String mainContactId;

	public String getMainContactLastName()
	{
		return mainContactLastName;
	}
	public void setMainContactLastName(String mainContactLastName)
	{
		this.mainContactLastName=mainContactLastName;
	}
	public String getMainContactFirstName()
	{
		return mainContactFirstName;
	}
	public void setMainContactFirstName(String mainContactFirstName)
	{
		this.mainContactFirstName=mainContactFirstName;
	}

	public String getCompanyExternalRef() {
		return companyExternalRef;
	}

	public void setCompanyExternalRef(String companyExternalRef) {
		this.companyExternalRef = companyExternalRef;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getMainContactId() {
		return mainContactId;
	}

	public void setMainContactId(String mainContactId) {
		this.mainContactId = mainContactId;
	}

}

	
