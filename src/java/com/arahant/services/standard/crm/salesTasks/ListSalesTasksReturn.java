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
 *
 */
package com.arahant.services.standard.crm.salesTasks;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.beans.SalesActivityResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListSalesTasksReturn extends TransmitReturnBase {
	ListSalesTasksReturnItem item[];
	
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
	public ListSalesTasksReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListSalesTasksReturnItem[] item) {
		this.item = item;
	}

	void makeItems(final List<SalesActivityResult> l) {
		List<ListSalesTasksReturnItem> ri = new ArrayList<ListSalesTasksReturnItem>();

		for (SalesActivityResult sa: l)
		{
			if (!isEmpty(sa.getFirstFollowUpTask()))
				ri.add(new ListSalesTasksReturnItem(sa.getFirstFollowUpTask(), sa.getSalesActivityResultId()));

			if (!isEmpty(sa.getSecondFollowUpTask()))
				ri.add(new ListSalesTasksReturnItem(sa.getSecondFollowUpTask(), sa.getSalesActivityResultId()));

			if (!isEmpty(sa.getThirdFollowUpTask()))
				ri.add(new ListSalesTasksReturnItem(sa.getThirdFollowUpTask(), sa.getSalesActivityResultId()));
		}

		Set<ListSalesTasksReturnItem> tasks = new HashSet<ListSalesTasksReturnItem>();

		for (ListSalesTasksReturnItem lstri : ri)
		{
			boolean found = false;
			for(ListSalesTasksReturnItem da : tasks)
			{
				if(lstri.getTaskName().equalsIgnoreCase(da.getTaskName()))
				{
					found = true;
					break;
				}
			}
			if(!found)
				tasks.add(lstri);
		}

		final ListSalesTasksReturnItem[]ret=new ListSalesTasksReturnItem[tasks.size()];

		int loop = 0;
		for (ListSalesTasksReturnItem li : tasks)
				ret[loop++]=li;

		setItem(ret);
	}
}

	
