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
package com.arahant.services.standard.inventory.assemblyTemplate;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListTemplateDetailsInput extends TransmitInputBase {

	@Validation (required=false)
	private String parentId;
	@Validation (required=true)
	private String templateParentId;
	

	public String getParentId()
	{
		return parentId;
	}
	public void setParentId(String parentId)
	{
		this.parentId=parentId;
	}
	public String getTemplateParentId()
	{
		return templateParentId;
	}
	public void setTemplateParentId(String templateParentId)
	{
		this.templateParentId=templateParentId;
	}


}

	
