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
import com.arahant.business.BItem;
import com.arahant.annotation.Validation;
import com.arahant.business.BItemInspection;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveItemInput extends TransmitInputBase {

	void setData(BItem bc)
	{

		bc.setDateReceived(dateReceived);
		bc.setDescription(descripiton);
		bc.setParentItemId(parentItemId);
		bc.setProductCost(productCost);
		bc.setProductId(productId);
		bc.setLocation(orgGroupId);
		bc.setSerialNumber(serialNumber);
		bc.setQuantity(quantity);
		bc.deleteInspectionItems();

		for (SaveItemInputItem i : getInspectionItem())
		{
			BItemInspection ii=new BItemInspection();
			ii.create();
			ii.setItem(bc);
			i.setData(ii);

			bc.addPendingInsert(ii);
		}

	}
	
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (type="date",required=false)
	private int dateReceived;
	@Validation (table="item",column="item_particulars",required=false)
	private String descripiton;
	@Validation (required=false)
	private String parentItemId;
	@Validation (required=false)
	private double productCost;
	@Validation (required=false)
	private String productId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (required=false)
	private SaveItemInputItem []inspectionItem;
	@Validation (required=false,min=0)
	private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	public SaveItemInputItem[] getInspectionItem() {
		if (inspectionItem==null)
			inspectionItem=new SaveItemInputItem[0];
		return inspectionItem;
	}

	public void setInspectionItem(SaveItemInputItem[] inspectionItem) {
		this.inspectionItem = inspectionItem;
	}



	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public String getDescripiton()
	{
		return descripiton;
	}
	public void setDescripiton(String descripiton)
	{
		this.descripiton=descripiton;
	}
	public String getParentItemId()
	{
		return parentItemId;
	}
	public void setParentItemId(String parentItemId)
	{
		this.parentItemId=parentItemId;
	}
	public double getProductCost()
	{
		return productCost;
	}
	public void setProductCost(double productCost)
	{
		this.productCost=productCost;
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

}

	
