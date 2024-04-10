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
package com.arahant.services.standard.project.projectListReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProjectStatusesInput extends TransmitInputBase {

	@Validation (required=false)
	private String code;
	@Validation (required=false)
private String description;
	@Validation (required=false)
private String [] excludeIds;
	@Validation (min=2,max=5,required=false)
private int codeSearchType;
	@Validation (min=2,max=5,required=false)
private int descriptionSearchType;
;

	public String getCode()
	{
		return modifyForSearch(code,codeSearchType);
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getDescription()
	{
		return modifyForSearch(description,descriptionSearchType);
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public String [] getExcludeIds()
	{
		if (excludeIds==null)
			excludeIds= new String [0];
		return excludeIds;
	}
	public void setExcludeIds(String [] excludeIds)
	{
		this.excludeIds=excludeIds;
	}
	public int getCodeSearchType()
	{
		return codeSearchType;
	}
	public void setCodeSearchType(int codeSearchType)
	{
		this.codeSearchType=codeSearchType;
	}
	public int getDescriptionSearchType()
	{
		return descriptionSearchType;
	}
	public void setDescriptionSearchType(int descriptionSearchType)
	{
		this.descriptionSearchType=descriptionSearchType;
	}


}

	
