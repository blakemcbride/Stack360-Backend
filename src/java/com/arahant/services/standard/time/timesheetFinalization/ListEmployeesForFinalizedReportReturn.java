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
 * Created on Feb 9, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetFinalization;
import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class ListEmployeesForFinalizedReportReturn extends TransmitReturnBase {

	private FinalizedList [] item;
	
	public ListEmployeesForFinalizedReportReturn() {
		super();
	}

	

	/**
	 * @param p
	 */
	void setEmployees(final BEmployee[] p) {
		item=new FinalizedList[p.length];
		for (int loop=0;loop<p.length;loop++)
			item[loop]=new FinalizedList(p[loop]);
	}



	/**
	 * @return Returns the item.
	 */
	public FinalizedList[] getItem() {
		return item;
	}



	/**
	 * @param item The item to set.
	 */
	public void setItem(final FinalizedList[] item) {
		this.item = item;
	}
}

	
