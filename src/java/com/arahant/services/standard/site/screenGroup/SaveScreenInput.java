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
public class SaveScreenInput extends TransmitInputBase {
	
@Validation (required=false)
	private boolean defaultScreen;
@Validation (required=false)
	private String parentScreenGroupId;
@Validation (required=false)
	private String screenId;
@Validation (required=false)
	private String label;

	public boolean getDefaultScreen()
	{
		return defaultScreen;
	}
	public void setDefaultScreen(boolean defaultScreen)
	{
		this.defaultScreen=defaultScreen;
	}
	public String getParentScreenGroupId()
	{
		return parentScreenGroupId;
	}
	public void setParentScreenGroupId(String parentScreenGroupId)
	{
		this.parentScreenGroupId=parentScreenGroupId;
	}
	public String getScreenId()
	{
		return screenId;
	}
	public void setScreenId(String screenId)
	{
		this.screenId=screenId;
	}
	public String getLabel()
	{
		return label;
	}
	public void setLabel(String label)
	{
		this.label=label;
	}

}

	
