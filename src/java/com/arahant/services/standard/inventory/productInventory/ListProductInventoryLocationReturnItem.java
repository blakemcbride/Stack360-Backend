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
import com.arahant.business.BOrgGroup;


/**
 * 
 *
 *
 */
public class ListProductInventoryLocationReturnItem {
	
	public ListProductInventoryLocationReturnItem()
	{
		;
	}

	ListProductInventoryLocationReturnItem (BItem bc)
	{
		if (bc==null)
			return;
		id=bc.getId();
		description=bc.getDescription();
		orgGroup=false;
		serialNumber=bc.getSerialNumber();
		lotNumber=bc.getLotNumber();

	}

	ListProductInventoryLocationReturnItem (BOrgGroup bc)
	{
		if (bc==null)
			return;
		id=bc.getId();
		description=bc.getName();
		orgGroup=true;
		serialNumber="";
		lotNumber="";

	}
	
	private String id;
	private String description;
	private boolean orgGroup;
	private String serialNumber;
	private String lotNumber;
	

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description=description;
	}
	public boolean getOrgGroup()
	{
		return orgGroup;
	}
	public void setOrgGroup(boolean orgGroup)
	{
		this.orgGroup=orgGroup;
	}
	public String getSerialNumber()
	{
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber)
	{
		this.serialNumber=serialNumber;
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

	
