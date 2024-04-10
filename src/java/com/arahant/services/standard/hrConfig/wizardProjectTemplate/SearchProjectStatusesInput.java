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


package com.arahant.services.standard.hrConfig.wizardProjectTemplate;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 *
 */
public class SearchProjectStatusesInput extends TransmitInputBase {

	@Validation (table="project_status",column="code",required=false)
	private String code; //
	@Validation (table="project_status",column="description",required=false)
	private String description;//
	@Validation (min=2,max=5,required=false)
	private int codeSearchType;//
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;//
	@Validation (required=true)
	private boolean excludeAlreadyUsed;
	@Validation (required=false)
	private String projectTemplateId;
	@Validation (required=false)
	private String projectCategoryId;
	@Validation (required=false)
	private String projectTypeId;

	public String getProjectCategoryId() {
		return projectCategoryId;
	}

	public void setProjectCategoryId(String projectCategoryId) {
		this.projectCategoryId = projectCategoryId;
	}

	public String getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(String projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	@Validation (required=false)
	private String projectStatusId;

	public String getProjectStatusId() {
		return projectStatusId;
	}

	public void setProjectStatusId(String projectStatusId) {
		this.projectStatusId = projectStatusId;
	}


	public String getProjectTemplateId() {
		return projectTemplateId;
	}

	public void setProjectTemplateId(String projectTemplateId) {
		this.projectTemplateId = projectTemplateId;
	}
	
	public boolean getExcludeAlreadyUsed() {
		return excludeAlreadyUsed;
	}

	public void setExcludeAlreadyUsed(boolean excludeAlreadyUsed) {
		this.excludeAlreadyUsed = excludeAlreadyUsed;
	}

	public String getCode()
	{
		return modifyForSearch(code, codeSearchType);
	}
	public void setCode(final String code)
	{
		this.code=code;
	}
	public String getDescription()
	{
		return modifyForSearch(description,descriptionSearchType);
	}
	public void setDescription(final String description)
	{
		this.description=description;
	}
	/**
	 * @return Returns the codeSearchType.
	 */
	public int getCodeSearchType() {
		return codeSearchType;
	}
	/**
	 * @param codeSearchType The codeSearchType to set.
	 */
	public void setCodeSearchType(final int codeSearchType) {
		this.codeSearchType = codeSearchType;
	}
	/**
	 * @return Returns the descriptionSearchType.
	 */
	public int getDescriptionSearchType() {
		return descriptionSearchType;
	}
	/**
	 * @param descriptionSearchType The descriptionSearchType to set.
	 */
	public void setDescriptionSearchType(final int descriptionSearchType) {
		this.descriptionSearchType = descriptionSearchType;
	}
	


}

	
