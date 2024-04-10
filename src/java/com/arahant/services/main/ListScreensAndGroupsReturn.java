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
 * Created on May 15, 2007
 * 
 */
package com.arahant.services.main;
import com.arahant.business.BScreenOrGroup;
import com.arahant.business.BWizardScreen;
import com.arahant.services.TransmitReturnBase;
import java.util.List;



/**
 * 
 *
 * Created on May 15, 2007
 *
 */
public class ListScreensAndGroupsReturn extends TransmitReturnBase {

	private ListScreensAndGroupsItem []item;

	/**
	 * @return Returns the item.
	 */
	public ListScreensAndGroupsItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final List<BWizardScreen> list, String wizConfId, int totalLength) {
		item=new ListScreensAndGroupsItem[list.size()];
		for (int loop=0;loop<list.size();loop++)
		{
			if(loop == totalLength - 1)
				item[loop]=new ListScreensAndGroupsItem(list.get(loop), true, wizConfId);
			else
				item[loop]=new ListScreensAndGroupsItem(list.get(loop), false, wizConfId);
		}
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListScreensAndGroupsItem[] item) {
		this.item = item;
	}

	/**
	 * @param groups
	 */
	void setData(final BScreenOrGroup[] groups, final String groupId, boolean isLocalHost) {
		item=new ListScreensAndGroupsItem[groups.length];
		for (int loop=0;loop<groups.length;loop++)
			item[loop]=new ListScreensAndGroupsItem(groups[loop], groupId, isLocalHost);
	}
	
	void setData(final BScreenOrGroup[] groups) {
		item=new ListScreensAndGroupsItem[groups.length];
		for (int loop=0;loop<groups.length;loop++)
			item[loop]=new ListScreensAndGroupsItem(groups[loop], loop);
	}
}

	
