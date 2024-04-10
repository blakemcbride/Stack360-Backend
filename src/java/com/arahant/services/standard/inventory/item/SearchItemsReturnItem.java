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
import com.arahant.business.BItem;


/**
 * 
 *
 *
 */
public class SearchItemsReturnItem {
	
	public SearchItemsReturnItem()
	{
	
	}

	SearchItemsReturnItem (BItem bc)
	{
		
		dateReceived=bc.getDateReceived();
		hasParentItem=bc.getHasParentItem();
		id=bc.getId();
		itemCost=bc.getItemCost();
		lotNumber=bc.getLotNumber();
		orgGroupName=bc.getOrgGroupName();
		serialNumber=bc.getSerialNumber();
		productDescription=bc.getProductDescription();
		quantity=bc.getQuantity();

	}
	
	private int dateReceived;
	private boolean hasParentItem;
	private String id;
	private double itemCost;
	private String lotNumber;
	private String orgGroupName;
	private String serialNumber;
	private String productDescription;
	private int quantity;
	

	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public boolean getHasParentItem()
	{
		return hasParentItem;
	}
	public void setHasParentItem(boolean hasParentItem)
	{
		this.hasParentItem=hasParentItem;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public double getItemCost()
	{
		return itemCost;
	}
	public void setItemCost(double itemCost)
	{
		this.itemCost=itemCost;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
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
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity=quantity;
	}

}

	
