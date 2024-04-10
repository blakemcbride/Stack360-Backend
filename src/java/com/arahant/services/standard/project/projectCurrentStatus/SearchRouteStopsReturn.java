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
package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.business.BRouteStop;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class SearchRouteStopsReturn extends TransmitReturnBase {
	private SearchRouteStopsReturnItem item[];
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private SearchRouteStopsReturnItem selectedItem;
	
	public SearchRouteStopsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchRouteStopsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	
	
	

	public int getHighCap() {
		return highCap;
	}

	public void setHighCap(int highCap) {
		this.highCap = highCap;
	}

	public int getLowCap() {
		return lowCap;
	}

	public void setLowCap(int lowCap) {
		this.lowCap = lowCap;
	}

	
	
	/**
	 * @return Returns the item.
	 */
	public SearchRouteStopsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchRouteStopsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BRouteStop[] a) {
		item=new SearchRouteStopsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchRouteStopsReturnItem(a[loop]);
	}
}

	
