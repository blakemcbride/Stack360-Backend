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
package com.arahant.services.standard.project.viewParent;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectViewsInput extends TransmitInputBase {

	@Validation (min=0,max=2,required=false)
	private int locationType;
	@Validation (required=false)
	private String locationTypeRelativeToId;
	@Validation (required=false)
	private NewProjectViewsInputItem views[];
	@Validation (required=false)
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getLocationType()
	{
		return locationType;
	}
	public void setLocationType(int locationType)
	{
		this.locationType=locationType;
	}
	public String getLocationTypeRelativeToId()
	{
		return locationTypeRelativeToId;
	}
	public void setLocationTypeRelativeToId(String locationTypeRelativeToId)
	{
		this.locationTypeRelativeToId=locationTypeRelativeToId;
	}

	public NewProjectViewsInputItem[] getViews() {
		if (views==null)
			return new NewProjectViewsInputItem[0];
		return views;
	}

	public void setViews(NewProjectViewsInputItem[] views) {
		this.views = views;
	}


}

	
