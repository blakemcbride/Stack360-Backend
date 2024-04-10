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
package com.arahant.services.standard.hr.hrTrainingCategory;
import com.arahant.business.BHRTrainingCategory;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 22, 2007
 *
 */
public class ListTrainingCategoriesReturn extends TransmitReturnBase {

	ListTrainingCategoryItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListTrainingCategoryItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListTrainingCategoryItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BHRTrainingCategory[] a) {
		item=new ListTrainingCategoryItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListTrainingCategoryItem(a[loop]);
	}
}

	
