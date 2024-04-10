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
package com.arahant.services.standard.billing.service;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BGlAccount;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 *
 */
public class ListGLAccountsReturn extends TransmitReturnBase {

	private ListGLAccountsReturnItem item[];
	private int highCap=0;
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private ListGLAccountsReturnItem selectedItem;

	public int getHighCap() {
		return highCap;
	}

	public void setHighCap(int highCap) {
		this.highCap = highCap;
	}

	public int getLowCap() {
		return lowCap;
	}

	public void setLowCap(int lowCap) {
		this.lowCap = lowCap;
	}

	public ListGLAccountsReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ListGLAccountsReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
	
	
	/**
	 * @param accounts
	 */
	void setItem(final BGlAccount[] accounts) {
		item=new ListGLAccountsReturnItem[accounts.length];
		for (int loop=0;loop<item.length;loop++)
			item[loop]=new ListGLAccountsReturnItem(accounts[loop]);
		
	}
	/**
	 * @return Returns the item.
	 */
	public ListGLAccountsReturnItem[] getItem() {
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListGLAccountsReturnItem[] item) {
		this.item = item;
	}
	
}

	
