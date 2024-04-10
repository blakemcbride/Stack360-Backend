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
import com.arahant.business.BLot;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewLotInput extends TransmitInputBase {

	void setData(BItem bc)
	{
		
		bc.setProductId(productId);
	//	bc.setProductCost(productCost);
	//	bc.setDateReceived(dateReceived);
		bc.setLocation(orgGroupId);
	//	bc.setOriginalQuantity(originalQuantity);
		bc.setDescription(description);
		bc.setQuantity(originalQuantity);

		for (NewLotInputItem i : getInspectionItem())
		{
			BItemInspection ii=new BItemInspection();
			ii.create();
			ii.setItem(bc);
			i.setData(ii);

			bc.addPendingInsert(ii);
		}

	}

	void setData(BLot lot)
	{
		lot.setLotCost(productCost);
		lot.setDateReceived(dateReceived);
		lot.setOriginalQuantity(originalQuantity);
		lot.setDescription(description);
		lot.setLotNumber(lotNumber);

		if (originalQuantity==0)
			lot.setOriginalQuantity(getInspectionItem().length);
	}


	@Validation (required=true)
	private String productId;
	@Validation (required=false)
	private double productCost;
	@Validation (type="date",required=false)
	private int dateReceived;
	@Validation (required=true)
	private String orgGroupId;
	@Validation (required=true, min=0)
	private int originalQuantity;
	@Validation (min=0,required=true)
	private String [] serialNumber;
	@Validation (required=true, table="lot", column="lot_number")
	private String lotNumber;
	@Validation (required=false)
	private NewLotInputItem []inspectionItem;
	@Validation (required=false,table="lot",column="lot_particulars")
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public NewLotInputItem[] getInspectionItem() {
		if (inspectionItem==null)
			inspectionItem=new NewLotInputItem[0];
		return inspectionItem;
	}

	public void setInspectionItem(NewLotInputItem[] inspectionItem) {
		this.inspectionItem = inspectionItem;
	}




	public String getLotNumber() {
		return lotNumber;
	}

	public void setLotNumber(String lotNumber) {
		this.lotNumber = lotNumber;
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
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public int getOriginalQuantity()
	{
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity)
	{
		this.originalQuantity=originalQuantity;
	}
	public String [] getSerialNumber()
	{
		if (serialNumber==null)
			serialNumber= new String [0];
		return serialNumber;
	}
	public void setSerialNumber(String [] serialNumber)
	{
		this.serialNumber=serialNumber;
	}

}

	
