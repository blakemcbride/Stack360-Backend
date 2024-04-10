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
package com.arahant.services.standard.project.projectBillingReport;
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
public class SearchSubordinateOrgGroupsReturn extends TransmitReturnBase {
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private SearchSubordinateOrgGroupsReturnItem selectedItem;
	private SearchSubordinateOrgGroupsReturnItem[] item;
	
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

	public SearchSubordinateOrgGroupsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchSubordinateOrgGroupsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	

	public SearchSubordinateOrgGroupsReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchSubordinateOrgGroupsReturnItem[] item) {
		this.item = item;
	}

	
	public SearchSubordinateOrgGroupsReturn() {
		super();
	}
	/**
	 * @param groups
	 */
	public void setOrgGroups(final BOrgGroup[] groups) {
		item=new SearchSubordinateOrgGroupsReturnItem[groups.length];
		for (int loop=0;loop<groups.length;loop++)
			item[loop]=new SearchSubordinateOrgGroupsReturnItem(groups[loop]);
	}
}

	
