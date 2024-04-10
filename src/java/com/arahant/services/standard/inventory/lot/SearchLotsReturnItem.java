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
import com.arahant.business.BLot;


/**
 * 
 *
 *
 */
public class SearchLotsReturnItem {
	
	public SearchLotsReturnItem()
	{
	
	}

	SearchLotsReturnItem (BLot bc)
	{
		
		dateReceived=bc.getDateReceived();
		originalQuantity=bc.getOriginalQuantity();
		lotCost=bc.getLotCost();
		lotNumber=bc.getLotNumber();
		orgGroupName=bc.getOrgGroupName();
		productDescription=bc.getProductDescription();
		id=bc.getId();

	}
	
	private int dateReceived;
	private int originalQuantity;
	private double lotCost;
	private String lotNumber;
	private String orgGroupName;
	private String productDescription;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public int getOriginalQuantity()
	{
		return originalQuantity;
	}
	public void setOriginalQuantity(int originalQuantity)
	{
		this.originalQuantity=originalQuantity;
	}
	public double getLotCost()
	{
		return lotCost;
	}
	public void setLotCost(double lotCost)
	{
		this.lotCost=lotCost;
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
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}

}

	
