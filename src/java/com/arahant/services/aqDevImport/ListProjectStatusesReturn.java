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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.aqDevImport;
import com.arahant.business.BProjectStatus;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListProjectStatusesReturn extends TransmitReturnBase {

	private FoundStatuses [] item;
	
	public ListProjectStatusesReturn() {
		super();
	}

	/**
	 * @return Returns the projectStatuses.
	 */
	public FoundStatuses[] getItem() {
		return item;
	}

	/**
	 * @param projectStatuses The projectStatuses to set.
	 */
	public void setItem(final FoundStatuses[] projectStatuses) {
		this.item = projectStatuses;
	}

	/**
	 * @param status
	 */
	void setProjectStatuses(final BProjectStatus[] status) {
		item=new FoundStatuses[status.length];
		for (int loop=0;loop<status.length;loop++)
			item[loop]=new FoundStatuses(status[loop]);
		
	}
}

	
