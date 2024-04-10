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
 * Created on Jan 1, 2008
 * 
 */
package com.arahant.services.standard.project.orgGroupProjectList;
import com.arahant.business.BCompanyBase;


/**
 * 
 *
 * Created on Jan 1, 2008
 *
 */
public class SearchCompanyByTypeReturnItem 
{

	public SearchCompanyByTypeReturnItem() {
		super();
	}

		
	// Fields    

	private String orgGroupId;
	private String name;
	private String orgGroupTypeName;



	/**
	 * @param base
	 */
	SearchCompanyByTypeReturnItem(final BCompanyBase b) {
		super();
		if (b==null)
			return;
		 orgGroupId=b.getOrgGroupId();
		 name=b.getName();
		 orgGroupTypeName=b.getOrgType();

	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getOrgGroupTypeName() {
		return orgGroupTypeName;
	}

	public void setOrgGroupTypeName(final String orgGroupTypeName) {
		this.orgGroupTypeName = orgGroupTypeName;
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

	
