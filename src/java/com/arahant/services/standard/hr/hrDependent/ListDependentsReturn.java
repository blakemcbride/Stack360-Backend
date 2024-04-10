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
 * Created on Jun 11, 2007
 * 
 */
package com.arahant.services.standard.hr.hrDependent;
import com.arahant.business.BHREmplDependent;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Jun 11, 2007
 *
 */
public class ListDependentsReturn  extends TransmitReturnBase {

	private ListDependentsItem item[];
	private boolean taskComplete = false;

	
	
	public boolean getTaskComplete() {
		return taskComplete;
	}

	public void setTaskComplete(boolean taskComplete) {
		this.taskComplete = taskComplete;
	}

	/**
	 * @return Returns the item.
	 */
	public ListDependentsItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListDependentsItem[] item) {
		this.item = item;
	}

	/**
	 * @param dependents
	 */
	void setItem(final BHREmplDependent[] d) {
		item=new ListDependentsItem[d.length];
		for (int loop=0;loop<d.length;loop++)
			item[loop]=new ListDependentsItem(d[loop]);
	}
}

	
