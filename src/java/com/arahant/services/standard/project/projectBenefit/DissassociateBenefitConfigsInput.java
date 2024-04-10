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
package com.arahant.services.standard.project.projectBenefit;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Dec 4, 2007
 *
 */
public class DissassociateBenefitConfigsInput extends TransmitInputBase {
	@Validation (required=false)
	private String[] configIds;
	@Validation (required=false)
	private String projectId;

	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	/**
	 * @return Returns the configIds.
	 */
	public String[] getConfigIds() {
            if (configIds==null)
                return new String[0];
		return configIds;
	}
	/**
	 * @param configIds The configIds to set.
	 */
	public void setConfigIds(final String[] configIds) {
		this.configIds = configIds;
	}
}

	
