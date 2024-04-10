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
 * Created on May 14, 2007
 * 
 */
package com.arahant.services.standard.hrConfig.wizardConfigurator;
import com.arahant.business.BScreen;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on May 14, 2007
 *
 */
public class SearchScreensReturn extends TransmitReturnBase {

	private SearchScreensReturnItem item[];

	/**
	 * @return Returns the item.
	 */
	public SearchScreensReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchScreensReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param screens
	 */
	void setData(final BScreen[] s) {
		item=new SearchScreensReturnItem[s.length];
		for (int loop=0;loop<s.length;loop++)
			item[loop]=new SearchScreensReturnItem(s[loop]);
	}
}

	
