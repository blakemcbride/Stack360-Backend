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
 * Created on Feb 12, 2007
 * 
 */
package com.arahant.services.standard.project.projectCategory;
import com.arahant.business.BProjectCategory;


/**
 * 
 *
 * Created on Feb 12, 2007
 *
 */
public class ListProjectCategoriesReturnItem {


	public ListProjectCategoriesReturnItem() {
		super();
	}

	private String projectCategoryId;
	private String code;
	private String description;
	private String scope;
	private String scopeFormatted;
	private int lastActiveDate;
	private boolean allCompanies;

	
	/**
	 * @param category
	 */
	ListProjectCategoriesReturnItem(final BProjectCategory category) {
		super();
		projectCategoryId=category.getProjectCategoryId();
		code=category.getCode();
		description=category.getDescription();
		scope=category.getScope();
		scopeFormatted=category.getScopeFormatted();
		lastActiveDate=category.getLastActiveDate();
		allCompanies=category.getCompany()==null;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
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

	public String getScopeFormatted() {
		return scopeFormatted;
	}

	public void setScopeFormatted(String scopeFormatted) {
		this.scopeFormatted = scopeFormatted;
	}

	
}

	
