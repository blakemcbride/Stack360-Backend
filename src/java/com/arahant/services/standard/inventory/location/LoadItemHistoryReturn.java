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
package com.arahant.services.standard.inventory.location;

import com.arahant.business.BItem;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BItemH;


/**
 * 
 *
 *
 */
public class LoadItemHistoryReturn extends TransmitReturnBase {

	void setData(BItemH bc)
	{
		
		itemDescription=bc.getItemDescription();
		quantity=bc.getQuantity();
		serialNumber=bc.getSerialNumber();
		productDescription=bc.getProductDescription();
		parentItemDescription=bc.getParentItemDescription();
		lotNumber=bc.getLotNumber();
		dateReceived=bc.getDateReceived();
		cost=bc.getProductCost();

	}

	void setData(BItem bc)
	{

		itemDescription=bc.getDescription();
		quantity=bc.getQuantity();
		serialNumber=bc.getSerialNumber();
		productDescription=bc.getProductDescription();
		parentItemDescription=bc.getParentDescription();
		lotNumber=bc.getLotNumber();

	}

	
	private String itemDescription;
	private int quantity;
	private String serialNumber;
	private String productDescription;
	private String parentItemDescription;
	private String lotNumber;
	private int dateReceived;
	private double cost;

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public int getDateReceived() {
		return dateReceived;
	}

	public void setDateReceived(int dateReceived) {
		this.dateReceived = dateReceived;
	}



	public String getItemDescription()
	{
		return itemDescription;
	}
	public void setItemDescription(String itemDescription)
	{
		this.itemDescription=itemDescription;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
	}
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}
	public String getParentItemDescription()
	{
		return parentItemDescription;
	}
	public void setParentItemDescription(String parentItemDescription)
	{
		this.parentItemDescription=parentItemDescription;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}

}

	
