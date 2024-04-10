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

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProjectViewJoin;


/**
 * 
 *
 *
 */
public class ListProjectViewsReturn extends TransmitReturnBase {
	ListProjectViewsReturnItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListProjectViewsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListProjectViewsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BProjectViewJoin[] a) {
		item=new ListProjectViewsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListProjectViewsReturnItem(a[loop]);
	}
}

	