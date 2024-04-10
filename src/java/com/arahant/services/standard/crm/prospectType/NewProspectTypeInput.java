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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class NewProspectTypeInput extends TransmitInputBase {

	@Validation (required = true, table = "prospect_type", column = "type_code")
	private String typeCode;
	@Validation (required = false, table = "prospect_type", column = "description")
	private String description;
	@Validation (required = false)
	private boolean allCompanies;
	@Validation (required = false, table = "prospect_type", column = "company_id")
	private String companyId;
	@Validation(table = "prospect_type", column = "last_active_date", required = false, type = "date")
	private int lastActiveDate;

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
	
	public boolean getAllCompanies() {
		return allCompanies;
	}
	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}


}

	
