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
package com.arahant.services.standard.misc.companyOrgGroup;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListW4StatusesItem {

	private String W4StatusId;
	private String name;
	
	
	public ListW4StatusesItem()
	{
		
	}
	/**
	 * @return Returns the W4StatusId.
	 */
	public String getW4StatusId() {
		return W4StatusId;
	}

	/**
	 * @param W4StatusId The W4StatusId to set.
	 */
	public void setW4StatusId(final String W4StatusId) {
		this.W4StatusId = W4StatusId;
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

}

	
