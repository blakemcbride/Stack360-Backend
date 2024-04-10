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
package com.arahant.services.standard.time.timesheetEntryByClockSimple;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;

public class ListAllTimesheetsReturn extends TransmitReturnBase {

	private ListAllTimesheetsReturnItem item[];
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private double totalHours;
	private int begDateRange;
	private int endDateRange;

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public ListAllTimesheetsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListAllTimesheetsReturnItem[] item) {
		this.item = item;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BTimesheet[] a) {
		totalHours = 0.0;
		item = new ListAllTimesheetsReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++) {
			item[loop] = new ListAllTimesheetsReturnItem(a[loop]);
			totalHours += item[loop].hours();
		}
	}

	public int getBegDateRange() {
		return begDateRange;
	}

	public void setBegDateRange(int begDateRange) {
		this.begDateRange = begDateRange;
	}

	public int getEndDateRange() {
		return endDateRange;
	}

	public void setEndDateRange(int endDateRange) {
		this.endDateRange = endDateRange;
	}
	
}
