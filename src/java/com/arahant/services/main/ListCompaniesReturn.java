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
package com.arahant.services.main;

import com.arahant.beans.CompanyDetail;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BCompany;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import java.util.List;


/**
 * 
 *
 *
 */
public class ListCompaniesReturn extends TransmitReturnBase {
	ListCompaniesReturnItem item[];
	private String screen;
	private String screenTitle;
	
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
	public ListCompaniesReturnItem[] getItem() {
		return item;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListCompaniesReturnItem[] item) {
		this.item = item;
	}

	void setItem(List<CompanyDetail> a) {
		item=new ListCompaniesReturnItem[a.size()];
		for (int loop=0;loop<a.size();loop++)
			item[loop]=new ListCompaniesReturnItem(a.get(loop));
	}

	/**
	 * @param accounts
	 */
	void setItem(final BCompany[] a) {
		item=new ListCompaniesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new ListCompaniesReturnItem(a[loop]);
	}
}

	
