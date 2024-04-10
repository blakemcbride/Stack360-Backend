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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BItem;
import com.arahant.business.BItemInspection;


/**
 * 
 *
 *
 */
public class LoadItemReturn extends TransmitReturnBase {

	void setData(BItem bc)
	{
		
		dateReceived=bc.getDateReceived();
		itemCost=bc.getItemCost();
		itemDescription=bc.getItemDescription();
		quantity=bc.getQuantity();
		serialNumber=bc.getSerialNumber();
		hasParentItem=bc.getHasParentItem();
		orgGroupName=bc.getOrgGroupName();

		BItemInspection []details=bc.getInspections();

		inspection=new LoadItemReturnItem[details.length];

		for (int loop=0;loop<inspection.length;loop++)
			inspection[loop]=new LoadItemReturnItem(details[loop]);

	}
	
	private int dateReceived;
	private LoadItemReturnItem [] inspection;
	private double itemCost;
	private String itemDescription;
	private int quantity;
	private String serialNumber;
	private String orgGroupName;
	private boolean hasParentItem;

	public boolean getHasParentItem() {
		return hasParentItem;
	}

	public void setHasParentItem(boolean hasParentItem) {
		this.hasParentItem = hasParentItem;
	}

	public String getOrgGroupName() {
		return orgGroupName;
	}

	public void setOrgGroupName(String orgGroupName) {
		this.orgGroupName = orgGroupName;
	}



	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public LoadItemReturnItem [] getInspection()
	{
		if (inspection==null)
			inspection= new LoadItemReturnItem [0];
		return inspection;
	}
	public void setInspection(LoadItemReturnItem [] inspection)
	{
		this.inspection=inspection;
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

}

	
