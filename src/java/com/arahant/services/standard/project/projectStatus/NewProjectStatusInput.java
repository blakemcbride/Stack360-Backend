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
package com.arahant.services.standard.project.projectStatus;
import com.arahant.annotation.Validation;
import com.arahant.business.BProjectStatus;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectStatusInput extends TransmitInputBase {


	@Validation (table="project_status",column="code",required=true)
	private String code;

	@Validation (table="project_status",column="description",required=true)
	private String description;
	
	@Validation (table="project_status",column="active",required=false)
	private int active;
	

	@Validation (table="project_status",column="last_active_date",required=false, type="date")
	private int lastActiveDate;
	@Validation (required=false)
	private boolean allCompanies;

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
	 * @return Returns the active.
	 */
	public int getActive() {
		return active;
	}

	/**
	 * @param active The active to set.
	 */
	public void setActive(final int active) {
		this.active = active;
	}

	public NewProjectStatusInput() {
		super();
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

	void setData(BProjectStatus bp) {
		bp.setCode(getCode());
		bp.setDescription(getDescription());
		bp.setActive(getActive());
		bp.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bp.setCompany(null);
		else
			bp.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}

	
}

	
