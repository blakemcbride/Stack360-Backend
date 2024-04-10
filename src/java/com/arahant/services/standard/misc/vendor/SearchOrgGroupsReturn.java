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
package com.arahant.services.standard.misc.vendor;
import com.arahant.business.BOrgGroup;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchOrgGroupsReturn extends TransmitReturnBase {
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private SearchOrgGroupsReturnItem selectedItem;
	private SearchOrgGroupsReturnItem[] item;
	
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

	public SearchOrgGroupsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchOrgGroupsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	

	public SearchOrgGroupsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchOrgGroupsReturnItem[] item) {
		this.item = item;
	}

	
	public SearchOrgGroupsReturn() {
		super();
	}
	/**
	 * @param groups
	 */
	public void setOrgGroups(final BOrgGroup[] groups) {
		item=new SearchOrgGroupsReturnItem[groups.length];
		for (int loop=0;loop<groups.length;loop++)
			item[loop]=new SearchOrgGroupsReturnItem(groups[loop]);
	}

	void setSelectedItem(BOrgGroup bOrgGroup) {
		selectedItem = new SearchOrgGroupsReturnItem(bOrgGroup);
	}
}

	
