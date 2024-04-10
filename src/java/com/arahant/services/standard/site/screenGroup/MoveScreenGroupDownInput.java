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
 * Created on Feb 8, 2007
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
public class MoveScreenGroupDownInput extends TransmitInputBase {

	@Validation (required=true)
	private String childScreenGroupId;
	@Validation (required=true)
	private String parentScreenGroupId;
	
	
	/**
	 * @return Returns the screenGroupId.
	 */
	public String getParentScreenGroupId() {
		return parentScreenGroupId;
	}


	/**
	 * @param screenGroupId The screenGroupId to set.
	 */
	public void setParentScreenGroupId(final String screenGroupId) {
		this.parentScreenGroupId = screenGroupId;
	}


	/**
	 * @return Returns the screenId.
	 */
	public String getChildScreenGroupId() {
		return childScreenGroupId;
	}


	/**
	 * @param screenId The screenId to set.
	 */
	public void setChildScreenGroupId(final String screenId) {
		this.childScreenGroupId = screenId;
	}


	public MoveScreenGroupDownInput() {
		super();
	}
}

	
