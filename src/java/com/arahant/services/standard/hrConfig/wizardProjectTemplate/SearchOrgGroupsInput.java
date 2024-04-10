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
package com.arahant.services.standard.hrConfig.wizardProjectTemplate;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;



/**
 *        
 * 
 * Created on Feb 8, 2007
 *
 */
public class SearchOrgGroupsInput extends TransmitInputBase {

	@Validation (table="org_group",column="name",required=false)
	private String name;
	@Validation (min=2, max=5, required=false)
	private int nameSearchType;
	@Validation (required=false)
	private String projectTemplateId;
	@Validation (required=false)
	private String projectTemplateType;
	@Validation (required=false)
	private String orgGroupId;

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String og) {
		this.orgGroupId = og;
	}

	public String getProjectTemplateId() {
		return projectTemplateId;
	}

	public void setProjectTemplateId(String projectTemplateId) {
		this.projectTemplateId = projectTemplateId;
	}

	public String getProjectTemplateType() {
		return projectTemplateType;
	}

	public void setProjectTemplateType(String projectTemplateType) {
		this.projectTemplateType = projectTemplateType;
	}
	public String getName()
	{
		return modifyForSearch(name,nameSearchType);
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public int getNameSearchType()
	{
		return nameSearchType;
	}
	public void setNameSearchType(int nameSearchType)
	{
		this.nameSearchType=nameSearchType;
	}


}

	
