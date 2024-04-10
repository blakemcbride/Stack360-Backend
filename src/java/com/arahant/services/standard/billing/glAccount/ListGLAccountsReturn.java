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
package com.arahant.services.standard.billing.glAccount;

import com.arahant.beans.GlAccount;
import com.arahant.business.BGlAccount;
import com.arahant.services.TransmitReturnBase;

import java.util.List;


/**
 * 
 *
 *
 */
public class ListGLAccountsReturn extends TransmitReturnBase {
	private ListGLAccountsItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListGLAccountsItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListGLAccountsItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BGlAccount[] accounts) {
		item=new ListGLAccountsItem[accounts.length];
		for (int loop=0;loop<item.length;loop++)
			item[loop]=new ListGLAccountsItem(accounts[loop]);
	}
	
}

	
