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
package com.arahant.services.standard.project.subProjectParent;
import com.arahant.business.BProjectType;


/**
 * 
 *
 * Created on Jan 1, 2008
 *
 */
public class SearchProjectTypesReturnItem {
	private String projectTypeId;
	private String code;
	private String description;

	public SearchProjectTypesReturnItem() {
		super();
	}

	SearchProjectTypesReturnItem(final BProjectType pt) {
		super();
		projectTypeId=pt.getProjectTypeId();
		code=pt.getCode();
		description=pt.getDescription();
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(final String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}
}

	
