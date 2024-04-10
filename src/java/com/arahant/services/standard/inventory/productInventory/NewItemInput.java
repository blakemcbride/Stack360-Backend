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

import com.arahant.business.BLot;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BItem;
import com.arahant.annotation.Validation;
import com.arahant.business.BItemInspection;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewItemInput extends TransmitInputBase {

	void setData(BItem bc)
	{
		
		bc.setProductId(productId);
	//	bc.setProductCost(productCost);
	//	bc.setDateReceived(dateReceived);
		bc.setSerialNumber(serialNumber);
		bc.setLotNumberId(lotNumberId);
		bc.setLocation(orgGroupId);
		bc.setParentItemId(parentItemId);
		bc.setDescription(description);
		bc.setQuantity(quantity);

		for (NewItemInputItem i : getInspectionItem())
		{
			BItemInspection ii=new BItemInspection();
			ii.create();
			ii.setItem(bc);
			i.setData(ii);

			bc.addPendingInsert(ii);
		}

	}


	void setData(BLot l) {
		l.setDateReceived(dateReceived);
		l.setLotCost(productCost);
		l.setOriginalQuantity(quantity);
		l.setDescription(description);
	}

	@Validation (required=true)
	private String productId;
	@Validation (required=false)
	private double productCost;
	@Validation (type="date",required=true)
	private int dateReceived;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (table="item",column="item_id",required=false)
	private String lotNumberId;
	@Validation (table="item",column="item_id",required=false)
	private String orgGroupId;
	@Validation (required=false)
	private NewItemInputItem []inspectionItem;
	@Validation (required=false,table="lot",column="lot_particulars")
	private String description;
	@Validation (required=false)
	private String parentItemId;
	@Validation (required=false, min=0)
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public String getParentItemId() {
		return parentItemId;
	}

	public void setParentItemId(String parentItemId) {
		this.parentItemId = parentItemId;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public NewItemInputItem[] getInspectionItem() {
		if (inspectionItem==null)
			inspectionItem=new NewItemInputItem[0];
		return inspectionItem;
	}

	public void setInspectionItem(NewItemInputItem[] inspectionItem) {
		this.inspectionItem = inspectionItem;
	}

	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public double getProductCost()
	{
		return productCost;
	}
	public void setProductCost(double productCost)
	{
		this.productCost=productCost;
	}
	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public String getLotNumberId()
	{
		return lotNumberId;
	}
	public void setLotNumberId(String lotNumberId)
	{
		this.lotNumberId=lotNumberId;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}




}

	
