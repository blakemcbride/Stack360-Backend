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
public class SaveItemInput extends TransmitInputBase {

	void setData(BItem bc)
	{
/*
 *             - if lotId is empty where it wasn't before, auto create the one-to-one lot record and fill in date received, cost, and original qty set to 1
            - if lotId is set where it was empty before, delete the existing one-to-one lot record and make it point to the specified lot id record
 */

		

		if (isEmpty(lotId) && !isEmpty(bc.getLotId()))
		{
			BLot blot=new BLot();
			lotId=blot.create();
			blot.setDateReceived(dateReceived);
			blot.setDescription(itemDescription);
			blot.setOriginalQuantity(1);
			blot.insert();
		}
		else
			if (!isEmpty(lotId) && isEmpty(bc.getLotNumber()))
			{
				new BLot(bc.getLotId()).delete();
			}
			else
				if (isEmpty(lotId) &&isEmpty(bc.getLotNumber()))
				{
					lotId=bc.getLotId();
				}

		bc.setLotId(lotId);

		bc.setDescription(itemDescription);
		bc.setSerialNumber(serialNumber);
		bc.setProductId(productId);
		bc.setQuantity(quantity);


		bc.deleteInspectionItems();

		for (SaveItemInputItem i : getInspection())
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
	@Validation (min=1,max=16,required=true)
	private String id;
	@Validation (required=false)
	private SaveItemInputItem []inspection;

	public SaveItemInputItem [] getInspection()
	{
		if (inspection==null)
			inspection= new SaveItemInputItem [0];
		return inspection;
	}
	public void setInspection(SaveItemInputItem [] inspection)
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
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}

}

	
