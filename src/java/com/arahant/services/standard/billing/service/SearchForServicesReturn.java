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
package com.arahant.services.standard.billing.service;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BService;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class SearchForServicesReturn extends TransmitReturnBase {
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private SearchForServicesItem [] item;

	/**
	 * @return Returns the item.
	 */
	public SearchForServicesItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchForServicesItem[] item) {
		this.item = item;
	}

	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}

	/**
	 * @param products
	 */
	void setItem(final BService[] p) {
		item=new SearchForServicesItem[p.length];
		for (int loop=0;loop<p.length;loop++)
			item[loop]=new SearchForServicesItem(p[loop]);
	}
}

	
