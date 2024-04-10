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
public class SearchLotsInput extends TransmitInputBase {

	@Validation (table="lot",column="lot_number",required=false)
	private String lotNumber;
	@Validation (min=2,max=5,required=false)
	private int lotNumberSearchType;
	@Validation (required=false)
	private String productId;
	@Validation (required=false)
	private String selectFromItemId;
	

	public String getLotNumber()
	{
		return modifyForSearch(lotNumber,lotNumberSearchType);
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public int getLotNumberSearchType()
	{
		return lotNumberSearchType;
	}
	public void setLotNumberSearchType(int lotNumberSearchType)
	{
		this.lotNumberSearchType=lotNumberSearchType;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public String getSelectFromItemId()
	{
		return selectFromItemId;
	}
	public void setSelectFromItemId(String selectFromItemId)
	{
		this.selectFromItemId=selectFromItemId;
	}


}

	
