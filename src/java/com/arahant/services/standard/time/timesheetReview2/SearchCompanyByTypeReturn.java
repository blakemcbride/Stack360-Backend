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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;

public class SearchCompanyByTypeReturn extends TransmitReturnBase {

	private int highCap = BProperty.getInt(StandardProperty.SEARCH_MAX);
	private int lowCap = BProperty.getInt(StandardProperty.COMBO_MAX);
	private SearchCompanyByTypeReturnItem[] item;

	public SearchCompanyByTypeReturn() {
	}

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

	/**
	 * @return Returns the companies.
	 */
	public SearchCompanyByTypeReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param companies The companies to set.
	 */
	public void setItem(final SearchCompanyByTypeReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param bases
	 */
	void setCompanies(final BCompanyBase[] bases) {
		item = new SearchCompanyByTypeReturnItem[bases.length];
		for (int loop = 0; loop < bases.length; loop++)
			item[loop] = new SearchCompanyByTypeReturnItem(bases[loop]);
	}
}
