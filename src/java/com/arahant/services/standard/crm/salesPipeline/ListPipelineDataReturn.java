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
package com.arahant.services.standard.crm.salesPipeline;

import com.arahant.beans.Employee;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.business.BProspectStatus;
import com.arahant.services.TransmitReturnBase;

public class ListPipelineDataReturn extends TransmitReturnBase {

	ListPipelineDataReturnItem item[];

	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);

	public void setCap(int x)
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
	public ListPipelineDataReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListPipelineDataReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BProspectStatus[] a, final Employee person) {
		item=new ListPipelineDataReturnItem[a.length+1];
		item[0]=new ListPipelineDataReturnItem(a, person);
		for (int loop=1;loop<a.length+1;loop++)
			item[loop]=new ListPipelineDataReturnItem(a[loop-1], person);
	}

	void setItem(final BProspectStatus[] a) {
		item=new ListPipelineDataReturnItem[a.length+1];
		item[0]=new ListPipelineDataReturnItem(a);
		for (int loop=1;loop<a.length+1;loop++)
			item[loop]=new ListPipelineDataReturnItem(a[loop-1]);
	}

}

	
