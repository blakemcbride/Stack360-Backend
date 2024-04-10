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
 *
 * Created on November 24, 2016
*/

package com.arahant.services.standard.hr.billingRateSetup;

import com.arahant.business.BRateType;
import com.arahant.services.TransmitReturnBase;

public class ListReturn extends TransmitReturnBase {

	private ListItem[] item;

	public ListItem[] getItem() {
		return item;
	}

	public void setItem(final ListItem[] item) {
		this.item = item;
	}

	void setItem(final BRateType[] a) {
		item = new ListItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new ListItem(a[loop]);
	}
}

	