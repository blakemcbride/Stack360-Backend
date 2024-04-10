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
 * Created on Jun 14, 2007
 * 
 */
package com.arahant.services.standard.project.projectRoute;


/**
 * 
 *
 * Created on Jun 14, 2007
 *
 */
public class SaveProjectRouteInputItem {
	private String projectCategoryId, projectTypeId ;
	

	/**
	 * @return Returns the projectCategoryId.
	 */
	public String getProjectCategoryId() {
		return projectCategoryId;
	}

	/**
	 * @param projectCategoryId The projectCategoryId to set.
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}

	/**
	 * @return Returns the projectTypeId.
	 */
	public String getProjectTypeId() {
		return projectTypeId;
	}

	/**
	 * @param projectTypeId The projectTypeId to set.
	 */
	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
}

	
