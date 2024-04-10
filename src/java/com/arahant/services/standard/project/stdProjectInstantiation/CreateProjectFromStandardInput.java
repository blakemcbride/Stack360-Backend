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
package com.arahant.services.standard.project.stdProjectInstantiation;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class CreateProjectFromStandardInput extends TransmitInputBase {

	@Validation (required=true)
	private String companyId;
	@Validation (required=true)
	private String [] projectId;
	public CreateProjectFromStandardInput() {
		super();
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
	 * @return Returns the projectId.
	 */
	public String[] getProjectId() {
            if (projectId==null)
                return new String[0];
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String[] projectId) {
		this.projectId = projectId;
	}
}

	
