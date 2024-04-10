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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrCheckList;
import com.arahant.business.BHRCheckListItem;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListChecklistItemsReturn extends TransmitReturnBase {


	public ListChecklistItemsReturn() {
		super();
	}
	
	private ListChecklistItemsItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListChecklistItemsItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListChecklistItemsItem[] item) {
		this.item = item;
	}

	/**
	 * @param items
	 */
	void setItem(final BHRCheckListItem[] i) {
		item=new ListChecklistItemsItem[i.length];
		for (int loop=0;loop<i.length;loop++)
			item[loop]=new ListChecklistItemsItem(i[loop]);
	}
}

	
