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
package com.arahant.services.standard.project.orgGroupProjectList;

import com.arahant.utils.StandardProperty;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProjectCategory;
import com.arahant.business.BProperty;


/**
 * 
 *
 *
 */
public class SearchProjectCategoriesReturn extends TransmitReturnBase {
	private	SearchProjectCategoriesReturnItem item[];
	private SearchProjectCategoriesReturnItem selectedItem;

	public SearchProjectCategoriesReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchProjectCategoriesReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchProjectCategoriesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchProjectCategoriesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BProjectCategory[] a) {
		item=new SearchProjectCategoriesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchProjectCategoriesReturnItem(a[loop]);
	}
}

	
