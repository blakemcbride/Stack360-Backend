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
import com.arahant.business.BLot;


/**
 * 
 *
 *
 */
public class SearchLotsReturnItem {
	
	public SearchLotsReturnItem()
	{
		;
	}

	SearchLotsReturnItem (BLot bc)
	{
		
		id=bc.getId();
		lotNumber=bc.getLotNumber();
		productDescription=bc.getProductDescription();
		dateReceived=bc.getDateReceived();
		if (bc.getProduct()!=null)
			productId=bc.getProduct().getId();

	}
	
	private String id;
	private String lotNumber;
	private String productDescription;
	private int dateReceived;
	private String productId;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getLotNumber()
	{
		return lotNumber;
	}
	public void setLotNumber(String lotNumber)
	{
		this.lotNumber=lotNumber;
	}
	public String getProductDescription()
	{
		return productDescription;
	}
	public void setProductDescription(String productDescription)
	{
		this.productDescription=productDescription;
	}
	public int getDateReceived()
	{
		return dateReceived;
	}
	public void setDateReceived(int dateReceived)
	{
		this.dateReceived=dateReceived;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId=productId;
	}

}

	
