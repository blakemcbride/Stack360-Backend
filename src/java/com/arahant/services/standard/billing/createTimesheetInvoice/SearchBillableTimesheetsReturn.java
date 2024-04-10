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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.business.BSearchOutput;
import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;

public class SearchBillableTimesheetsReturn extends TransmitReturnBase<BTimesheet> {

	private SearchBillableTimesheetsReturnItem item[];
	private int cap = 250;

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchBillableTimesheetsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchBillableTimesheetsReturnItem[] item) {
		this.item = item;
	}

	void setItem(final BSearchOutput<BTimesheet> a) {
		BTimesheet [] bts = a.getItems();
		item = new SearchBillableTimesheetsReturnItem[bts.length];
		for (int loop = 0; loop < item.length; loop++)
			item[loop] = new SearchBillableTimesheetsReturnItem(bts[loop]);
		setStandard(a);
	}
}
