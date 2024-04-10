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
 * Created on Jun 6, 2007
 * 
 */
package com.arahant.services.standard.security.securityToken;
import com.arahant.business.BRight;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Jun 6, 2007
 *
 */
public class ListSecurityTokensReturn extends TransmitReturnBase {

	private ListSecurityTokensItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListSecurityTokensItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListSecurityTokensItem[] item) {
		this.item = item;
	}

	/**
	 * @param rights
	 */
	void setItem(final BRight[] rights) {
		item=new ListSecurityTokensItem[rights.length];
		for (int loop=0;loop<rights.length;loop++)
			item[loop]=new ListSecurityTokensItem(rights[loop]);
	}

}

	
