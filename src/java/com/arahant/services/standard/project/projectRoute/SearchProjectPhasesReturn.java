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
package com.arahant.services.standard.project.projectRoute;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProjectPhase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;


/**
 * 
 *
 *
 */
public class SearchProjectPhasesReturn extends TransmitReturnBase {
	private SearchProjectPhasesReturnItem item[];
	private SearchProjectPhasesReturnItem selectedItem;
	
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
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
	public SearchProjectPhasesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchProjectPhasesReturnItem[] item) {
		this.item = item;
	}


	public SearchProjectPhasesReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(final SearchProjectPhasesReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}


	/**
	 * @param accounts
	 */
	void setItem(final BProjectPhase[] a) {
		item=new SearchProjectPhasesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchProjectPhasesReturnItem(a[loop]);
	}
}

	
