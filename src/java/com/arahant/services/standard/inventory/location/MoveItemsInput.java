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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class MoveItemsInput extends TransmitInputBase {

	@Validation (required=false)
	private String fromProductId;
	@Validation (required=false)
	private String fromOrgGroupId;
	@Validation (required=false)
	private int moveQuantity;
	@Validation (required=false)
	private String moveItemId;
	@Validation (required=false)
	private String toOrgGroupId;
	@Validation (required=false)
	private String toItemId;
	

	public String getFromProductId()
	{
		return fromProductId;
	}
	public void setFromProductId(String fromProductId)
	{
		this.fromProductId=fromProductId;
	}
	public String getFromOrgGroupId()
	{
		return fromOrgGroupId;
	}
	public void setFromOrgGroupId(String fromOrgGroupId)
	{
		this.fromOrgGroupId=fromOrgGroupId;
	}
	public int getMoveQuantity()
	{
		return moveQuantity;
	}
	public void setMoveQuantity(int moveQuantity)
	{
		this.moveQuantity=moveQuantity;
	}
	public String getMoveItemId()
	{
		return moveItemId;
	}
	public void setMoveItemId(String moveItemId)
	{
		this.moveItemId=moveItemId;
	}
	public String getToOrgGroupId()
	{
		return toOrgGroupId;
	}
	public void setToOrgGroupId(String toOrgGroupId)
	{
		this.toOrgGroupId=toOrgGroupId;
	}
	public String getToItemId()
	{
		return toItemId;
	}
	public void setToItemId(String toItemId)
	{
		this.toItemId=toItemId;
	}


}

	
