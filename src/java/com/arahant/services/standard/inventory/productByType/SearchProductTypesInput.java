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
package com.arahant.services.standard.inventory.productByType;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProductTypesInput extends TransmitInputBase {

	@Validation (required=false)
	private String description;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	@Validation (required=false)
	private String selectProductTypeId;
	@Validation (required=false)
	private String excludeProductTypeId;
	@Validation (required=false)
	private String selectParentProductTypeId;
	

	public String getDescription()
	{
		return modifyForSearch(description,descriptionSearchType);
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public int getDescriptionSearchType()
	{
		return descriptionSearchType;
	}
	public void setDescriptionSearchType(int descriptionSearchType)
	{
		this.descriptionSearchType=descriptionSearchType;
	}
	public String getSelectProductTypeId()
	{
		return selectProductTypeId;
	}
	public void setSelectProductTypeId(String selectProductTypeId)
	{
		this.selectProductTypeId=selectProductTypeId;
	}
	public String getExcludeProductTypeId()
	{
		return excludeProductTypeId;
	}
	public void setExcludeProductTypeId(String excludeProductTypeId)
	{
		this.excludeProductTypeId=excludeProductTypeId;
	}
	public String getSelectParentProductTypeId()
	{
		return selectParentProductTypeId;
	}
	public void setSelectParentProductTypeId(String selectParentProductTypeId)
	{
		this.selectParentProductTypeId=selectParentProductTypeId;
	}


}

	
