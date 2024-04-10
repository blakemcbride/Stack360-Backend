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
 * Created on Mar 13, 2007
 * 
 */
package com.arahant.services.standard.hr.hrAccruedTimeOff;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Mar 13, 2007
 *
 */
public class ListAccruedTimeOffReturn extends TransmitReturnBase {

	public ListAccruedTimeOffReturn() {
		super();
	}
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private ListAccruedTimeOffItem []item;
	private double total;

	/**
	 * @return Returns the total.
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total The total to set.
	 */
	public void setTotal(final double total) {
		this.total = total;
	}

	/**
	 * @return Returns the max.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param max The max to set.
	 */
	public void setCap(final int max) {
		this.cap = max;
	}

	/**
	 * @param offs
	 */
	void setItem(final BHRAccruedTimeOff[] offs) {
		item=new ListAccruedTimeOffItem[offs.length];
		for (int loop=0;loop<offs.length;loop++)
			item[loop]=new ListAccruedTimeOffItem(offs[loop]);
		if (item.length>0)
			total=item[item.length-1].getRunningTotal();
		
	}

	/**
	 * @return Returns the item.
	 */
	public ListAccruedTimeOffItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListAccruedTimeOffItem[] item) {
		this.item = item;
	}
}

	
