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
package com.arahant.services.scanner;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BFormType;



/**
 * 
 *
 *
 */
public class ListPersonFormTypesReturn extends TransmitReturnBase {
	ListPersonFormTypesReturnItem item[];
	

	/**
	 * @return Returns the item.
	 */
	public ListPersonFormTypesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListPersonFormTypesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BFormType[] a) {
		item=new ListPersonFormTypesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListPersonFormTypesReturnItem(a[loop]);
	}
}

	
