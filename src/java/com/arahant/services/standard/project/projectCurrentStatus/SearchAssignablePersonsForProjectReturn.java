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


package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;

import java.util.Arrays;
import java.util.HashSet;


public class SearchAssignablePersonsForProjectReturn extends TransmitReturnBase {

	private SearchAssignablePersonsForProjectReturnItem item[];
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private boolean isMore;

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public SearchAssignablePersonsForProjectReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchAssignablePersonsForProjectReturnItem[] item) {
		this.item = item;
	}

	public boolean isMore() {
		return isMore;
	}

	public void setMore(boolean more) {
		isMore = more;
	}

	void setItem(final BPerson[] a, final String [] excludedIds) {
		if (a == null  ||  a.length == 0) {
			isMore = false;
			item = new SearchAssignablePersonsForProjectReturnItem[0];
		}
		HashSet<String> hs = new HashSet<>(Arrays.asList(excludedIds));
		item = new SearchAssignablePersonsForProjectReturnItem[cap];
		int max = a.length > cap ? cap+1 : a.length;
		int n = 0;  //  number added to list
		isMore = false;
		for (int i=0 ; n < max  &&  i < a.length ; i++)
			if (n == cap) {
				isMore = true;
				break;
			} else if (!hs.contains(a[i].getPersonId()))
				item[n++] = new SearchAssignablePersonsForProjectReturnItem(a[i]);
		if (n < cap)
			item = Arrays.copyOf(item, n);
	}
}

	
