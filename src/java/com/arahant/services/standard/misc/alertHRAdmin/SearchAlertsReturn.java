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
package com.arahant.services.standard.misc.alertHRAdmin;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BAlert;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;

public class SearchAlertsReturn extends TransmitReturnBase {
	SearchAlertsReturnItem alerts[];
	
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
	public SearchAlertsReturnItem[] getAlerts() {
		return alerts;
	}

	/**
	 * @param item The item to set.
	 */
	public void setAlerts(final SearchAlertsReturnItem[] alerts) {
		this.alerts = alerts;
	}

	/**
	 * @param accounts
	 */
	void setAlerts(final BAlert[] a) {
		alerts=new SearchAlertsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			alerts[loop]=new SearchAlertsReturnItem(a[loop]);
	}
}

	