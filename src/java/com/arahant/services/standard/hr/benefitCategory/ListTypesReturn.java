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
 * Created on Jul 12, 2007
 * 
 */
package com.arahant.services.standard.hr.benefitCategory;
import java.util.List;

import com.arahant.business.BHRBenefitCategory.CategoryType;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Jul 12, 2007
 *
 */
public class ListTypesReturn extends TransmitReturnBase {

	private ListTypesReturnItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListTypesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListTypesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param name
	 */
	void setItem(final List<CategoryType> l) {
		
		item=new ListTypesReturnItem[l.size()];
		for (int loop=0;loop<item.length;loop++)
			item[loop]=new ListTypesReturnItem(l.get(loop));
	}
}

	
