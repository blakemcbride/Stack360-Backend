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

import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.services.TransmitReturnBase;

public class ListEmployeeTimeOffReturn extends TransmitReturnBase {

	private ListEmployeeTimeOffItem item[];

	public ListEmployeeTimeOffReturn() {
	}

	/**
	 * @return Returns the item.
	 */
	public ListEmployeeTimeOffItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListEmployeeTimeOffItem[] item) {
		this.item = item;
	}

	/**
	 * @param employee
	 * @throws ArahantJessException
	 */
	void setData(final BEmployee e) throws ArahantJessException {
		final String[] types = e.getPaidTimeOffTypes();

		item = new ListEmployeeTimeOffItem[types.length];

		for (int loop = 0; loop < types.length; loop++) {
			item[loop] = new ListEmployeeTimeOffItem();
			item[loop].setAccrualAccountName(types[loop]);
			item[loop].setHours(e.getHoursLeftOnBenefit(types[loop]));
		}
	}
}
