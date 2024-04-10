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
import com.arahant.business.BItemH;


/**
 * 
 *
 *
 */
public class SearchProductInventoryHistoryReturnItem {
	
	public SearchProductInventoryHistoryReturnItem()
	{
		
	}

	SearchProductInventoryHistoryReturnItem (BItemH bc)
	{
		
		id=bc.getId();
		dateTimeFormatted=bc.getDateTimeFormatted();
		changeType=bc.getChangeType();
		orgGroupName=bc.getOrgGroupName();
		remainingQuantity=bc.getQuantity();
		changeMadeBy=bc.getChangeMadeBy();
		hasParent=bc.hasParent();
	}

	SearchProductInventoryHistoryReturnItem (BItem bc)
	{

		id=bc.getId();
		dateTimeFormatted=bc.getDateTimeFormatted();
		changeType=bc.getChangeType();
		orgGroupName=bc.getOrgGroupName();
		remainingQuantity=bc.getQuantity();
		changeMadeBy=bc.getChangeMadeBy();
		hasParent=bc.hasParent();

	}
	
	private String id;
	private String dateTimeFormatted;
	private String changeType;
	private String orgGroupName;
	private int remainingQuantity;
	private String changeMadeBy;
	private boolean hasParent;

	public boolean getHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getDateTimeFormatted()
	{
		return dateTimeFormatted;
	}
	public void setDateTimeFormatted(String dateTimeFormatted)
	{
		this.dateTimeFormatted=dateTimeFormatted;
	}
	public String getChangeType()
	{
		return changeType;
	}
	public void setChangeType(String changeType)
	{
		this.changeType=changeType;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public int getRemainingQuantity()
	{
		return remainingQuantity;
	}
	public void setRemainingQuantity(int remainingQuantity)
	{
		this.remainingQuantity=remainingQuantity;
	}
	public String getChangeMadeBy()
	{
		return changeMadeBy;
	}
	public void setChangeMadeBy(String changeMadeBy)
	{
		this.changeMadeBy=changeMadeBy;
	}

}

	
