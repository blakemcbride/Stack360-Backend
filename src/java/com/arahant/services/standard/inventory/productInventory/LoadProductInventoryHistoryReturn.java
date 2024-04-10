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

import com.arahant.business.BItem;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BItemH;


/**
 * 
 *
 *
 */
public class LoadProductInventoryHistoryReturn extends TransmitReturnBase {

	void setData(BItemH bc)
	{
		
		id=bc.getId();
		productDescription=bc.getProductDescription();
		parentProductDescription=bc.getParentProductDescription();
		lotNumber=bc.getLotNumber();
		serialNumber=bc.getSerialNumber();
		inventoryDescription=bc.getInventoryDescription();
		originalQuantity=bc.getOriginalQuantity();
		dateReceived=bc.getDateReceived();
		productCost=bc.getProductCost();

	}

	void setData(BItem bc)
	{

		id=bc.getId();
		productDescription=bc.getProductDescription();
		parentProductDescription=bc.getParentProductDescription();
		lotNumber=bc.getLotNumber();
		serialNumber=bc.getSerialNumber();
		inventoryDescription=bc.getInventoryDescription();
		originalQuantity=bc.getOriginalQuantity();
		dateReceived=bc.getDateReceived();
		productCost=bc.getProductCost();

	}
	
	private String id;
	private String productDescription;
	private String parentProductDescription;
	private String lotNumber;
	private String serialNumber;
	private String inventoryDescription;
	private int originalQuantity;
	private int dateReceived;
	private double productCost;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}
	public String getParentProductDescription()
	{
		return parentProductDescription;
	}
	public void setParentProductDescription(String parentProductDescription)
	{
		this.parentProductDescription=parentProductDescription;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public String getInventoryDescription()
	{
		return inventoryDescription;
	}
	public void setInventoryDescription(String inventoryDescription)
	{
		this.inventoryDescription=inventoryDescription;
	}
	public int getOriginalQuantity()
	{
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity)
	{
		this.originalQuantity=originalQuantity;
	}
	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public double getProductCost()
	{
		return productCost;
	}
	public void setProductCost(double productCost)
	{
		this.productCost=productCost;
	}

}

	
