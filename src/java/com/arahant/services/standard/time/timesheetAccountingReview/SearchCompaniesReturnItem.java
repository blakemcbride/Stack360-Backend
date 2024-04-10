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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetAccountingReview;
import com.arahant.business.BCompanyBase;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
public class SearchCompaniesReturnItem  {


	private String orgGroupId;
	private String name;
	private String type; 
	

	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type The type to set.
	 */
	public void setType(final String type) {
		this.type = type;
	}


	public SearchCompaniesReturnItem() {

	}


	/**
	 * @param company
	 */
	SearchCompaniesReturnItem(final BCompanyBase company) {
		super();
		 orgGroupId=company.getOrgGroupId();
		 name=company.getName();
		 type=company.getOrgType();
		
	}


	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}


	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}


	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}


	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}


	
}

	
