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
 * Created on Apr 5, 2007
 * 
 */
package com.arahant.services.standard.time.unpaidTime;
import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Apr 5, 2007
 *
 */
public class ListUnpaidTimeReturn extends TransmitReturnBase {

	private ListUnpaidTimeItem item[];

	public ListUnpaidTimeReturn() {
		super();
	}

	/**
	 * @return Returns the item.
	 */
	public ListUnpaidTimeItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListUnpaidTimeItem[] item) {
		this.item = item;
	}

	/**
	 * @param timesheets
	 */
	void setData(final BTimesheet[] timesheets) {
		item=new ListUnpaidTimeItem[timesheets.length];
		for (int loop=0;loop<timesheets.length;loop++)
			item[loop]=new ListUnpaidTimeItem(timesheets[loop]);
			
	}
}

	
