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
 * Created on Feb 22, 2007
 * 
 */
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.business.BHREEOCategory;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEEOCategoryItem {

	private String eeoCategoryId;
	private String name;
	
	
	public ListEEOCategoryItem()
	{
		
	}
	/**
	 * @return Returns the accrualAccountId.
	 */
	public String getEeoCategoryId() {
		return eeoCategoryId;
	}

	public void setEeoCategoryId(final String eeoCategoryId) {
		this.eeoCategoryId = eeoCategoryId;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param account
	 */
	ListEEOCategoryItem(final BHREEOCategory account) {
		eeoCategoryId=account.getEeoCategoryId();
		name=account.getName();
	}
}

	
