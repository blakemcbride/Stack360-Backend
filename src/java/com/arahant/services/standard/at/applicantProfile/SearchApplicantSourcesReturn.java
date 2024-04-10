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
package com.arahant.services.standard.at.applicantProfile;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BApplicantSource;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;


/**
 * 
 *
 *
 */
public class SearchApplicantSourcesReturn extends TransmitReturnBase {
	private SearchApplicantSourcesReturnItem item[];
	private SearchApplicantSourcesReturnItem selectedItem;

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
	public SearchApplicantSourcesReturnItem getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param item The item to set.
	 */
	public void setSelectedItem(final SearchApplicantSourcesReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchApplicantSourcesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchApplicantSourcesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BApplicantSource[] a) {
		item=new SearchApplicantSourcesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchApplicantSourcesReturnItem(a[loop]);
	}
}

	
