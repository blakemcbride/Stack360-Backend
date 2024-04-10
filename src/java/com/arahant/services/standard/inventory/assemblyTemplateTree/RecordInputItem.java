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
package com.arahant.services.standard.inventory.assemblyTemplateTree;

import com.arahant.beans.Item;
import com.arahant.beans.Lot;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Product;
import com.arahant.business.BItem;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;

public class RecordInputItem {

    private int quantity;
    private String location;
    private String lot;
    String itemId;
	private String serialNumber;

    public void build(String parentId, String productId, String finalLocationId)
    {

        HibernateSessionUtil hsu=ArahantSession.getHSU();

		//find one with this serial number
        HibernateCriteriaUtil<Item> hcu=hsu.createCriteria(Item.class);
		hcu.eq(Item.SERIAL_NUMBER, serialNumber);
        hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID,productId);
        hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID,location);
        hcu.joinTo(Item.LOT).eq(Lot.LOT_ID,lot);

        Item i=hcu.first();

		if (i==null) //none with that serial number, so search in general
		{
			hcu=hsu.createCriteria(Item.class);
			hcu.isNull(Item.SERIAL_NUMBER);
			hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID,productId);
			hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID,location);
			hcu.joinTo(Item.LOT).eq(Lot.LOT_ID,lot);

			i = hcu.first();
		}

		if (i==null) //none with that serial number, so search in general
		{
			hcu=hsu.createCriteria(Item.class);
			hcu.eq(Item.SERIAL_NUMBER,"");
			hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID,productId);
			hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID,location);
			hcu.joinTo(Item.LOT).eq(Lot.LOT_ID,lot);

			i = hcu.first();
		}

        if (i==null)
            throw new ArahantException("Invalid serial number. Item " +productId+" not found in system");

		BItem bi=new BItem(i);

		if (parentId!=null)
			itemId=bi.splitTo(parentId, quantity, serialNumber);
		else
			itemId=bi.moveTo(finalLocationId, quantity, serialNumber);


    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}
