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
import com.arahant.business.BLot;
import com.arahant.annotation.Validation;
import com.arahant.business.BItem;
import com.arahant.business.BItemInspection;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewLotInput extends TransmitInputBase {

	void setData(BLot bc)
	{
		
		bc.setDateReceived(dateReceived);
		bc.setLotCost(lotCost);
		bc.setLotDescription(lotDescription);
		bc.setLotNumber(lotNumber);
		if (originalQuantity==0)
			originalQuantity=getSerialNumber().length;
		bc.setOriginalQuantity(originalQuantity);

	}

	void setData(BItem bc)
	{
		bc.setProductId(productId);
		bc.setLocation(orgGroupId);
		bc.setQuantity(1);

		for (NewLotInputItem i : getInspection())
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
	private double lotCost;
	@Validation (table="item",column="item_particulars",required=false)
	private String lotDescription;
	@Validation (table="lot",column="lot_number",required=true)
	private String lotNumber;
	@Validation (min=0,required=false)
	private int originalQuantity;
	@Validation (required=true)
	private String productId;
	@Validation (required=false)
	private NewLotInputSerial [] serialNumber;
	@Validation (required=false)
	private NewLotInputItem [] inspection;
	@Validation (required=true)
	private String orgGroupId;

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	

	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public double getLotCost()
	{
		return lotCost;
	}
	public void setLotCost(double lotCost)
	{
		this.lotCost=lotCost;
	}
	public String getLotDescription()
	{
		return lotDescription;
	}
	public void setLotDescription(String lotDescription)
	{
		this.lotDescription=lotDescription;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public int getOriginalQuantity()
	{
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity)
	{
		this.originalQuantity=originalQuantity;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}
	public NewLotInputSerial [] getSerialNumber()
	{
		if (serialNumber==null)
			serialNumber= new NewLotInputSerial [0];
		return serialNumber;
	}
	public void setSerialNumber(NewLotInputSerial [] serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public NewLotInputItem [] getInspection()
	{
		if (inspection==null)
			inspection= new NewLotInputItem [0];
		return inspection;
	}
	public void setInspection(NewLotInputItem [] inspection)
	{
		this.inspection=inspection;
	}

}

	
