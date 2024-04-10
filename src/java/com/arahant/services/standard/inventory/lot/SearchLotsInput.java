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
package com.arahant.services.standard.inventory.lot;

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
public class SearchLotsInput extends TransmitInputBase {

	@Validation (table="lot",column="lot_number",required=false)
	private String lotNumber;
	@Validation (min=2,max=5,required=false)
	private int lotNumberSearchType;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private String productId;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (required=false)
	private SearchMetaInput searchMeta;



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
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
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
			return new BSearchMetaInput(searchMeta,new String[]{"lotNumber","dateReceived", "lotCost",  "orgGroupName",
				"originalQuantity", "productDescription"});

		}
	}


}

	
