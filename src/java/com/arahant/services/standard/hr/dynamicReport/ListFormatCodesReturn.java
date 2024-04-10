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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.services.TransmitReturnBase;
import java.util.ArrayList;
import java.util.List;


public class ListFormatCodesReturn extends TransmitReturnBase {

	ListFormatCodesReturnItem[] item;
	int currentFormatCode;

	public int getCurrentFormatCode() {
		return currentFormatCode;
	}

	public void setCurrentFormatCode(int currentFormatCode) {
		this.currentFormatCode = currentFormatCode;
	}

	public ListFormatCodesReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListFormatCodesReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(DynamicReportColumn drc) {
		List<ListFormatCodesReturnItem> itemList = new ArrayList<ListFormatCodesReturnItem>();

		for(int loop = 0; loop < DynamicReportBase.FORMAT_CODES_COUNT; loop++) {
			ListFormatCodesReturnItem i = new ListFormatCodesReturnItem(loop + 1, drc);
			if(!isEmpty(i.getCodeTemplate()))
				itemList.add(i);
		}

		item = new ListFormatCodesReturnItem[itemList.size()];
		for (int loop = 0; loop < itemList.size(); loop++)
			item[loop] = itemList.get(loop);
	}
}

	
