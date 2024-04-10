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
 * Created on Jul 12, 2007
 * 
 */
package com.arahant.services.standard.hr.benefitCategory;
import com.arahant.business.BHRBenefitCategory.CategoryType;


/**
 * 
 *
 * Created on Jul 12, 2007
 *
 */
public class ListTypesReturnItem {

	private int id;
	private String description;
	/**
	 * @param type
	 */
	ListTypesReturnItem(final CategoryType type) {
		id=type.getId();
		description=type.getName();
	}
	
	public ListTypesReturnItem()
	{
		
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}
	/**
	 * @return Returns the id.
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(final int id) {
		this.id = id;
	}
}

	
