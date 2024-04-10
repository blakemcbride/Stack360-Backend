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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.business.BHREEORace;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEEORaceItem {

	private String EEORaceId;
	private String name;
	
	public ListEEORaceItem()
	{
		
	}
	
	/**
	 * @return Returns the EEORaceId.
	 */
	public String getEEORaceId() {
		return EEORaceId;
	}

	/**
	 * @param EEORaceId The EEORaceId to set.
	 */
	public void setEEORaceId(final String EEORaceId) {
		this.EEORaceId = EEORaceId;
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
	ListEEORaceItem(final BHREEORace account) {
		EEORaceId=account.getEEORaceId();
		name=account.getName();
	}
}

	
