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
import com.arahant.business.BItemH;


/**
 * 
 *
 *
 */
public class SearchItemHistoryReturnItem {
	
	public SearchItemHistoryReturnItem()
	{
		
	}

	SearchItemHistoryReturnItem (BItemH bc)
	{
		
		changeMadeBy=bc.getChangeMadeBy();
		changeType=bc.getChangeType();
		dateTimeFormatted=bc.getDateTimeFormatted();
		hasParentItem=bc.getHasParentItem();
		id=bc.getId();
		orgGroupName=bc.getOrgGroupName();
		quantity=bc.getQuantity();

	}
	
	private String changeMadeBy;
	private String changeType;
	private String dateTimeFormatted;
	private boolean hasParentItem;
	private String id;
	private String orgGroupName;
	private int quantity;
	

	public String getChangeMadeBy()
	{
		return changeMadeBy;
	}
	public void setChangeMadeBy(String changeMadeBy)
	{
		this.changeMadeBy=changeMadeBy;
	}
	public String getChangeType()
	{
		return changeType;
	}
	public void setChangeType(String changeType)
	{
		this.changeType=changeType;
	}
	public String getDateTimeFormatted()
	{
		return dateTimeFormatted;
	}
	public void setDateTimeFormatted(String dateTimeFormatted)
	{
		this.dateTimeFormatted=dateTimeFormatted;
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
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
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

	
