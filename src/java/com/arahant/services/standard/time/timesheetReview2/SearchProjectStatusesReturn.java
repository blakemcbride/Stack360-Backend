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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BProjectStatus;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class SearchProjectStatusesReturn extends TransmitReturnBase {

	private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
	private SearchProjectStatusesReturnItem[] item;

	public SearchProjectStatusesReturn() {
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

	public SearchProjectStatusesReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchProjectStatusesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param status
	 */
	void setProjectStatuses(final BProjectStatus[] status) {
		item = new SearchProjectStatusesReturnItem[status.length];
		for (int loop = 0; loop < status.length; loop++)
			item[loop] = new SearchProjectStatusesReturnItem(status[loop]);
	}
}
