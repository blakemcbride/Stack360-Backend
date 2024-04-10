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
package com.arahant.services.standard.inventory.productInventory;

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
public class SearchProductInventoryInput extends TransmitInputBase {

	@Validation (required=false)
	private SearchMetaInput searchMeta;
	@Validation (required=false)
	private String productId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (table="item",column="lot_number",required=false)
	private String lotNumber;
	@Validation (required=false)
	private String [] productTypeIds;
	@Validation (required=false)
	private String [] vendorIds;
	@Validation (required=false, min=1, max=3)
	private int productAvailable;
	@Validation (required=false)
	private String selectParentOfId;
	@Validation (required=false)
	private String excludeId;
	@Validation (required=false)
	private String selectFromId;

	public String getExcludeId() {
		if (excludeId==null)
			excludeId="";
		return excludeId;
	}

	public void setExcludeId(String excludeId) {
		this.excludeId = excludeId;
	}

	public String getSelectFromId() {
		return selectFromId;
	}

	public void setSelectFromId(String selectFromId) {
		this.selectFromId = selectFromId;
	}

	public String getSelectParentOfId() {
		return selectParentOfId;
	}

	public void setSelectParentOfId(String selectParentOfId) {
		this.selectParentOfId = selectParentOfId;
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
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	
	public String [] getProductTypeIds()
	{
		if (productTypeIds==null)
			productTypeIds= new String [0];
		return productTypeIds;
	}
	public void setProductTypeIds(String [] productTypeIds)
	{
		this.productTypeIds=productTypeIds;
	}
	public String [] getVendorIds()
	{
		if (vendorIds==null)
			vendorIds= new String [0];
		return vendorIds;
	}
	public void setVendorIds(String [] vendorIds)
	{
		this.vendorIds=vendorIds;
	}

	public int getProductAvailable() {
		return productAvailable;
	}

	public void setProductAvailable(int productAvailable) {
		this.productAvailable = productAvailable;
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
			return new BSearchMetaInput(searchMeta,new String[]{"description", "orgGroupName", "totalRemainingQuantity", "reOrderLevel"});
			/*description - string - the associated product
                    - orgGroupName - string - the associated org group
                    - detailCount - number - number of associated inventory details
                    - reOrderLevel - number
			 * */
		}
	}

}

	
