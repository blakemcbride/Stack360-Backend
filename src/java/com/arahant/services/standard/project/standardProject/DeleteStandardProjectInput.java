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
package com.arahant.services.standard.project.standardProject;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class DeleteStandardProjectInput extends TransmitInputBase {


	@Validation (required=true)
	private String []projectIds;
	
	public DeleteStandardProjectInput() {
		super();
	}

	/**
	 * @return Returns the projectId.
	 */
	public String[] getProjectIds() {
            if (projectIds==null)
                return new String[0];
		return projectIds;
	}

	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectIds(final String[] projectId) {
		this.projectIds = projectId;
	}
}

	
