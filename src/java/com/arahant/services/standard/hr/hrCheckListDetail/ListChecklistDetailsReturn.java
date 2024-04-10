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
 * Created on Feb 25, 2007
 * 
 */
package com.arahant.services.standard.hr.hrCheckListDetail;
import com.arahant.business.BHRCheckListDetail;
import com.arahant.services.TransmitReturnBase;

/**
 * 
 *
 * Created on Feb 25, 2007
 *
 */
public class ListChecklistDetailsReturn extends TransmitReturnBase {


	public ListChecklistDetailsReturn() {
		super();
	}
	
	private ListChecklistDetailsItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListChecklistDetailsItem[] getDetail() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setDetail(final ListChecklistDetailsItem[] item) {
		this.item = item;
	}

	/**
	 * @param items
	 */
	void setDetail(final BHRCheckListDetail[] i) {
		item=new ListChecklistDetailsItem[i.length];
		for (int loop=0;loop<i.length;loop++)
			item[loop]=new ListChecklistDetailsItem(i[loop]);
	}
}

	
