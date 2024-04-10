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
package com.arahant.services.standard.project.employeeProjectList;

import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class ListAssignedProjectsReturn extends TransmitReturnBase {
	ListAssignedProjectsReturnItem item[];
	
	private int cap=250;
	
	public void setCap(final int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public ListAssignedProjectsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListAssignedProjectsReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BProject[] a) {
		item=new ListAssignedProjectsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListAssignedProjectsReturnItem(a[loop]);
	}
}

	
