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
package com.arahant.services.standard.inventory.item;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchProductsInput extends TransmitInputBase {

	@Validation (table="product_type",column="description",required=false)
	private String description;
	@Validation (min=2,max=5,required=false)
	private int descriptionSearchType;
	@Validation (required=false)
	private String selectFromLotId;
	@Validation (table="product",column="sku",required=false)
	private String sku;
	@Validation (required=false)
	private String selectFromProductId;
	@Validation (required=false)
	private String selectFromItemId;

	public String getSelectFromItemId() {
		return selectFromItemId;
	}

	public void setSelectFromItemId(String selectFromItemId) {
		this.selectFromItemId = selectFromItemId;
	}

	public String getSelectFromProductId() {
		return selectFromProductId;
	}

	public void setSelectFromProductId(String selectFromProductId) {
		this.selectFromProductId = selectFromProductId;
	}

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
	public String getSelectFromLotId()
	{
		return selectFromLotId;
	}
	public void setSelectFromLotId(String selectFromLotId)
	{
		this.selectFromLotId=selectFromLotId;
	}
	public String getSku()
	{
		return sku;
	}
	public void setSku(String sku)
	{
		this.sku=sku;
	}


}

	
