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
package com.arahant.services.standard.site.screenGroup;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CopyScreenGroupInput extends TransmitInputBase {

@Validation (required=false)
	private String screenGroupId;
@Validation (required=false)
	private String screenGroupName;
@Validation (required=false)
	private String parentScreenGroupId;
@Validation (required=false)
	private boolean associate;

	public boolean getShallowCopy() {
		return shallowCopy;
	}

	public void setShallowCopy(boolean shallowCopy) {
		this.shallowCopy = shallowCopy;
	}
@Validation (required=false)
	private boolean shallowCopy;

	public String getScreenGroupId()
	{
		return screenGroupId;
	}
	public void setScreenGroupId(String screenGroupId)
	{
		this.screenGroupId=screenGroupId;
	}
	public String getScreenGroupName()
	{
		return screenGroupName;
	}
	public void setScreenGroupName(String screenGroupName)
	{
		this.screenGroupName=screenGroupName;
	}
	public String getParentScreenGroupId()
	{
		return parentScreenGroupId;
	}
	public void setParentScreenGroupId(String parentScreenGroupId)
	{
		this.parentScreenGroupId=parentScreenGroupId;
	}
	public boolean getAssociate()
	{
		return associate;
	}
	public void setAssociate(boolean associate)
	{
		this.associate=associate;
	}


}

	
