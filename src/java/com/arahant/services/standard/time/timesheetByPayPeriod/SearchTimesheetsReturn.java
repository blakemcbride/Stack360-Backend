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
package com.arahant.services.standard.time.timesheetByPayPeriod;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BTimesheet;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import java.util.List;


public class SearchTimesheetsReturn extends TransmitReturnBase {

	SearchTimesheetsReturnItem item[];
	SearchTimesheetsReturnTimeRelated timeRelated[];
	
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x) {
		cap = x;
	}
	
	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchTimesheetsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchTimesheetsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BTimesheet[] a) {
		item=new SearchTimesheetsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchTimesheetsReturnItem(a[loop]);
	}

	void setItem(List<SearchTimesheetsReturnItem> item) {
		this.item = makeArray(item);
	}

	/**
	 * @return Returns the item.
	 */
	public SearchTimesheetsReturnTimeRelated[] getTimeRelated() {
		return timeRelated;
	}

	/**
	 * @param item The item to set.
	 */
	public void setTimeRelated(final SearchTimesheetsReturnTimeRelated[] timeRelated) {
		this.timeRelated = timeRelated;
	}

	/**
	 * @param accounts
	 */
//	void setTimeRelated(final BTimesheet[] a) {
//		timeRelated=new SearchTimesheetsReturnTimeRelated[a.length];
//		for (int loop=0;loop<a.length;loop++)
//			timeRelated[loop]=new SearchTimesheetsReturnTimeRelated(a[loop]);
//	}

	void setTimeRelated(List<SearchTimesheetsReturnTimeRelated> timeRelated) {
		this.timeRelated = makeArray2(timeRelated);
	}

	private SearchTimesheetsReturnItem[] makeArray(List<SearchTimesheetsReturnItem> item) {
		SearchTimesheetsReturnItem[] newArray = new SearchTimesheetsReturnItem[item.size()];
		for(int i = 0; i < item.size(); i++) {
			newArray[i] = item.get(i);
		}

		return newArray;
	}

	private SearchTimesheetsReturnTimeRelated[] makeArray2(List<SearchTimesheetsReturnTimeRelated> timeRelated) {
		SearchTimesheetsReturnTimeRelated[] newArray = new SearchTimesheetsReturnTimeRelated[timeRelated.size()];
		for(int i = 0; i < timeRelated.size(); i++) {
			newArray[i] = timeRelated.get(i);
		}

		return newArray;
	}
}

	
