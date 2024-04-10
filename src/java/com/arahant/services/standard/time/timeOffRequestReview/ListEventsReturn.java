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
package com.arahant.services.standard.time.timeOffRequestReview;

import com.arahant.services.TransmitReturnBase;

public class ListEventsReturn extends TransmitReturnBase {

	private ListEventsReturnItem item[];

	public ListEventsReturnItem[] getItem() {
		if (item == null)
			item = new ListEventsReturnItem[0];
		return item;
	}

	public void setItem(ListEventsReturnItem[] item) {
		this.item = item;
	}
}
