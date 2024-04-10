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
package com.arahant.services.standard.site.companySecurityOverride;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProphetLoginOverride;

public class SearchCompaniesForEmployeeReturn extends TransmitReturnBase {
	SearchCompaniesForEmployeeReturnItem item[];
	String screenGroupName;
        String securityGroupName;

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
	public SearchCompaniesForEmployeeReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchCompaniesForEmployeeReturnItem[] item) {
		this.item = item;
	}

	/**
	 * @param accounts
	 */
	void setItem(final BProphetLoginOverride[] a) {
		item=new SearchCompaniesForEmployeeReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			item[loop]=new SearchCompaniesForEmployeeReturnItem(a[loop]);
	}

        public String getScreenGroupName() {
            return screenGroupName;
        }

        public void setScreenGroupName(String screenGroupName) {
            this.screenGroupName = screenGroupName;
        }

        public String getSecurityGroupName() {
            return securityGroupName;
        }

        public void setSecurityGroupName(String securityGroupName) {
            this.securityGroupName = securityGroupName;
        }


}

	
