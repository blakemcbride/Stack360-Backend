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
import com.arahant.business.BItem;
import com.arahant.annotation.Validation;
import com.arahant.business.BItemInspection;
import com.arahant.business.BLot;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewItemInput extends TransmitInputBase {
/*
	void setData(BLot bc)
	{

		bc.setDateReceived(dateReceived);
		bc.setLotCost(itemCost);
		bc.setLotDescription(itemDescription);
		bc.setSerialNumber(serialNumber);
		bc.setLotId(lotId);
		bc.setProductId(productId);
		bc.setQuantity(quantity);

	}
	*/
	void setData(BItem bc)
	{
		if (isEmpty(lotId))
		{
			BLot blot=new BLot();
			lotId=blot.create();
			blot.setDateReceived(dateReceived);
			blot.setDescription(itemDescription);
			blot.setOriginalQuantity(quantity);
			blot.insert();
		}

		bc.setSerialNumber(serialNumber);
		bc.setLotId(lotId);
		bc.setProductId(productId);
		bc.setQuantity(quantity);
		bc.setDetailedDescription(itemDescription);
		bc.setLocation(orgGroupId);


		for (NewItemInputItem i : getInspection())
		{
			BItemInspection ii=new BItemInspection();
			ii.create();
			ii.setItem(bc);
			i.setData(ii);

			bc.addPendingInsert(ii);
		}

	}
	
	@Validation (type="date",required=true)
	private int dateReceived;
	@Validation (min=0,required=false)
	private double itemCost;
	@Validation (table="item",column="item_particulars",required=false)
	private String itemDescription;
	@Validation (table="item",column="serial_number",required=false)
	private String serialNumber;
	@Validation (required=false)
	private String lotId;
	@Validation (required=true)
	private String productId;
	@Validation (min=1,required=true)
	private int quantity;
	@Validation (required=false)
	private NewItemInputItem []inspection;
	@Validation (required=true)
	private String orgGroupId;

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	
	public NewItemInputItem [] getInspection()
	{
		if (inspection==null)
			inspection= new NewItemInputItem [0];
		return inspection;
	}
	public void setInspection(NewItemInputItem [] inspection)
	{
		this.inspection=inspection;
	}

	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public double getItemCost()
	{
		return itemCost;
	}
	public void setItemCost(double itemCost)
	{
		this.itemCost=itemCost;
	}
	public String getItemDescription()
	{
		return itemDescription;
	}
	public void setItemDescription(String itemDescription)
	{
		this.itemDescription=itemDescription;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public String getLotId()
	{
		return lotId;
	}
	public void setLotId(String lotId)
	{
		this.lotId=lotId;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}

}

	
