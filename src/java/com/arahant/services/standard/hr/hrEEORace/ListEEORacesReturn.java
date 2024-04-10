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
package com.arahant.services.standard.hr.hrEEORace;
import com.arahant.business.BHREEORace;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListEEORacesReturn extends TransmitReturnBase {

	ListEEORaceItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListEEORaceItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListEEORaceItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BHREEORace[] a) {
		item=new ListEEORaceItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListEEORaceItem(a[loop]);
	}
}

	
