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
package com.arahant.services.standard.billing.bankDraftBatch;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BBankDraftHistory;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * 
 *
 *
 */
public class ListBatchHistoryReturn extends TransmitReturnBase {
	ListBatchHistoryReturnItem item[];
	
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
	public ListBatchHistoryReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListBatchHistoryReturnItem[] item) {
		this.item = item;
	}
	
	private HashMap<String, ListBatchHistoryReturnItem> rets=new HashMap<String, ListBatchHistoryReturnItem>();

	/**
	 * @param accounts
	 */
	void setItem(final BBankDraftHistory[] a) {
		
		for (int loop=0;loop<a.length;loop++)
		{
			ListBatchHistoryReturnItem i=rets.get(a[loop].getConfirmationNumber());
			if (i==null)
			{
				i=new ListBatchHistoryReturnItem(a[loop]);
				rets.put(i.getConfirmationNumber(), i);
			}
			else
			{
				i.setAmount(i.getAmount()+a[loop].getAmount());
			}
		}
		
		ArrayList<ListBatchHistoryReturnItem> retList=new ArrayList<ListBatchHistoryReturnItem>(rets.values().size());
		retList.addAll(rets.values());
		Collections.sort(retList);
		
		item=retList.toArray(new ListBatchHistoryReturnItem[retList.size()]);
		
	}
}

	
