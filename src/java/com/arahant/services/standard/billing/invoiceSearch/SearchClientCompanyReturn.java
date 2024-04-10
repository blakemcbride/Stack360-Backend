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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.billing.invoiceSearch;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
public class SearchClientCompanyReturn extends TransmitReturnBase {

	private SearchClientCompanyReturnItem [] item;
	private int highCap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap=BProperty.getInt(StandardProperty.COMBO_MAX);

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

	public SearchClientCompanyReturnItem[] getItem() {
		return item;
	}

	public void setItem(SearchClientCompanyReturnItem[] item) {
		this.item = item;
	}



	/**
	 * @param companies
	 */
	void setClientList(final BCompanyBase[] companies) {
		item=new SearchClientCompanyReturnItem[companies.length];
		
		for (int loop=0;loop<companies.length;loop++)
			item[loop]=new SearchClientCompanyReturnItem(companies[loop]);
	}
}

	
