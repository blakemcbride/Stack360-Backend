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
 * Created on Mar 7, 2007
 * 
 */
package com.arahant.services.aqDevImport;
import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Mar 7, 2007
 *
 */
public class ListProjectsReturn extends TransmitReturnBase {

	public ListProjectsReturn() {
		super();
	}
	
	private ListProjectsItem item[];

	/**
	 * @return Returns the item.
	 */
	public ListProjectsItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListProjectsItem[] item) {
		this.item = item;
	}

	/**
	 * @param projects
	 */
	void setItem(final BProject[] projects) {
		item=new ListProjectsItem[projects.length];
		for (int loop=0;loop<item.length;loop++)
			item[loop]=new ListProjectsItem(projects[loop]);
	}
}

	
