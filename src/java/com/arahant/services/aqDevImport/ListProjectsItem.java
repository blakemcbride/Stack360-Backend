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
 * Created on Mar 7, 2007
 * 
 */
package com.arahant.services.aqDevImport;
import com.arahant.business.BProject;


/**
 * 
 *
 * Created on Mar 7, 2007
 *
 */
public class ListProjectsItem {

	public ListProjectsItem()
	{
		;
	}
	/**
	 * @param project
	 */
	ListProjectsItem(final BProject project) {
		super();
		projectId=project.getProjectId();
		reference=project.getReference();
	}
	private String projectId;
	private String reference;
	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return Returns the reference.
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * @param reference The reference to set.
	 */
	public void setReference(final String reference) {
		this.reference = reference;
	}
}

	
