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

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BScreenGroup;
import com.arahant.annotation.Validation;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ReorderScreensAndScreenGroupsInput extends TransmitInputBase {

	
	@Validation (required=true)
	private String parentScreenGroupId;
	@Validation (required=true,min=1,type="array")
	private ReorderScreensAndScreenGroupsInputItem []item;
	
	public String getParentScreenGroupId()
	{
		return parentScreenGroupId;
	}
	public void setParentScreenGroupId(String parentScreenGroupId)
	{
		this.parentScreenGroupId=parentScreenGroupId;
	}

	public ReorderScreensAndScreenGroupsInputItem[] getItem() {
		if (item==null)
			item=new ReorderScreensAndScreenGroupsInputItem[0];
		return item;
	}

	public void setItem(ReorderScreensAndScreenGroupsInputItem[] item) {
		this.item = item;
	}

}

	
