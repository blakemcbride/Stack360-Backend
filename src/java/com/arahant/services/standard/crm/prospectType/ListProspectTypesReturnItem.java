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
package com.arahant.services.standard.crm.prospectType;

import com.arahant.business.BProspectType;


public class ListProspectTypesReturnItem {
	private String typeCode;
	private String description;
	private String prospectTypeId;
	private String companyId;
	private int lastActiveDate;

	public ListProspectTypesReturnItem() {}

	ListProspectTypesReturnItem (BProspectType bt) {
		typeCode = bt.getTypeCode();
		description = bt.getDescription();
		prospectTypeId = bt.getProspectTypeId();
		companyId = bt.getCompanyId();
		lastActiveDate = bt.getLastActiveDate();
	}


	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public String getTypeCode()
	{
		return typeCode;
	}
	public void setTypeCode(String typeCode)
	{
		this.typeCode = typeCode;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getProspectTypeId()
	{
		return prospectTypeId;
	}
	public void setProspectTypeId(String projectTypeId)
	{
		this.prospectTypeId = projectTypeId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


}

	
