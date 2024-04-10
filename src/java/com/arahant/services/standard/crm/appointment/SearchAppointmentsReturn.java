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
package com.arahant.services.standard.crm.appointment;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BAppointment;


/**
 * 
 *
 *
 */
public class SearchAppointmentsReturn extends TransmitReturnBase {
	private SearchAppointmentsReturnItem item[];
	private SearchAppointmentsReturnItem selectedItem;

	private int cap=250;

	public int getCap() {
		return cap;
	}

	public void setCap(int cap) {
		this.cap = cap;
	}
	
	/**
	 * @return Returns the item.
	 */
	public SearchAppointmentsReturnItem getSelectedItem() {
		return selectedItem;
	}

	/**
	 * @param item The item to set.
	 */
	public void setSelectedItem(final SearchAppointmentsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchAppointmentsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchAppointmentsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BAppointment[] a) {
		boolean dayGroup=true;
		int last=0;
		item=new SearchAppointmentsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
		{
			if (a[loop].getDate()!=last)
			{
				dayGroup=!dayGroup;
				last=a[loop].getDate();
			}
			item[loop]=new SearchAppointmentsReturnItem(a[loop], dayGroup);
		}
	}
}

	
