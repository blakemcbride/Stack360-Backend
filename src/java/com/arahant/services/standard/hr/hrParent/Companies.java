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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.standard.hr.hrParent;
import com.arahant.business.BCompany;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class Companies   {
	
	private String companyId;
	private String companyName;

	
	public Companies()
	{
		;
	}


	/**
	 * @param company
	 */
	Companies(final BCompany company) {
		super();
		companyId=company.getOrgGroupId();
		companyName=company.getName();
		
	}


	/**
	 * @return Returns the companyId.
	 */
	public String getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId The companyId to set.
	 */
	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName The companyName to set.
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}
	


}

	
