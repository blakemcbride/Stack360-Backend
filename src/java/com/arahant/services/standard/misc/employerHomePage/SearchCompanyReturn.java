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
package com.arahant.services.standard.misc.employerHomePage;
import com.arahant.business.BCompanyBase;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchCompanyReturn extends TransmitReturnBase {

	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);
	private SearchCompanyReturnItem [] item;
	private SearchCompanyReturnItem selectedItem;
	
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
	
	
	public SearchCompanyReturn() {
		super();
	}

	public SearchCompanyReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchCompanyReturnItem[] item) {
		this.item = item;
	}

	public SearchCompanyReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchCompanyReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}


	/**
	 * @param bases
	 */
	void setCompanies(final BCompanyBase[] bases) {
		item=new SearchCompanyReturnItem[bases.length];
		for (int loop=0;loop<bases.length;loop++)
			item[loop]=new SearchCompanyReturnItem(bases[loop]);
	}

}

	
