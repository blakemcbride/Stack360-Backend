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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.projectCategory;
import com.arahant.annotation.Validation;
import com.arahant.business.BProjectCategory;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectCategoryInput extends TransmitInputBase {


	@Validation (table="project_type",column="code",required=true)
	private String code;
	@Validation (min=1,max=1,required=true)
	private String scope;
	@Validation (table="project_type",column="description",required=true)
	private String description;


	@Validation (table="project_type",column="last_active_date",required=false, type="date")
	private int lastActiveDate;
	@Validation (required=false)
	private boolean allCompanies;
	
	public NewProjectCategoryInput() {
		super();
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code The code to set.
	 */
	public void setCode(final String code) {
		this.code = code;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	void setData(BProjectCategory bpc) {
		bpc.setCode(code);
		bpc.setDescription(description);
		bpc.setScope(scope);
		bpc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bpc.setCompany(null);
		else
			bpc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}
	
	

}

	
