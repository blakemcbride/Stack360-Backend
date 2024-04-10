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
import com.arahant.business.BSearchMetaInput;
import com.arahant.services.SearchMetaInput;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchItemsInput extends TransmitInputBase {

	@Validation (required=false)
	private String lotId;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (min=2,max=5,required=false)
	private int serialNumberSearchType;
	@Validation (required=false)
	private String productId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private SearchMetaInput searchMeta;
	

	public String getLotId()
	{
		return lotId;
	}
	public void setLotId(String lotId)
	{
		this.lotId=lotId;
	}
	public String getSerialNumber()
	{
		return modifyForSearch(serialNumber,serialNumberSearchType);
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public int getSerialNumberSearchType()
	{
		return serialNumberSearchType;
	}
	public void setSerialNumberSearchType(int serialNumberSearchType)
	{
		this.serialNumberSearchType=serialNumberSearchType;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}

	public SearchMetaInput getSearchMeta() {
		return searchMeta;
	}

	public void setSearchMeta(SearchMetaInput searchMeta) {
		this.searchMeta = searchMeta;
	}

	BSearchMetaInput getSearchMetaInput() {
		if (searchMeta == null) {
			return new BSearchMetaInput(0, true, false, 0);
		} else {
			return new BSearchMetaInput(searchMeta,new String[]{"serialNumber", "dateReceived", "hasParentItem", "lotNumber", "itemCost",
				"orgGroupName", "productDescription", "quantity"});

		}
	}

}

	
