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
package com.arahant.services.standard.project.projectRoute;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectTypesInput extends TransmitInputBase {

	@Validation (table="project_type",column="code",required=false)
	private String code;
	@Validation (table="project_type",column="description",required=false)
	private String description;;
	@Validation (min=2,max=5,required=false)
	private int codeSearchType;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	@Validation (required=false)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		return modifyForSearch(description, descriptionSearchType);
	}
	public void setDescription(final String description)
	{
		this.description=description;
	}
	public int getCodeSearchType()
	{
		return codeSearchType;
	}
	public void setCodeSearchType(final int codeSearchType)
	{
		this.codeSearchType=codeSearchType;
	}
	public int getDescriptionSearchType()
	{
		return descriptionSearchType;
	}
	public void setDescriptionSearchType(final int descriptionSearchType)
	{
		this.descriptionSearchType=descriptionSearchType;
	}


}

	
