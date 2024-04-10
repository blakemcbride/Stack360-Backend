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
package com.arahant.services.standard.misc.alertAgent;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BAgent;
import com.arahant.business.BCompany;

public class LoadCompaniesReturn extends TransmitReturnBase {
	LoadCompaniesReturnItem company[];

        void setData(final BAgent ba, final String companyName) {
            BCompany[] comps = ba.getAgentCompanies(ba.getId(), companyName);

            company = new LoadCompaniesReturnItem[comps.length];
            
                for (int loop = 0; loop < comps.length; loop++) {
                    company[loop] = new LoadCompaniesReturnItem(comps[loop]);
                }
        }

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
	public LoadCompaniesReturnItem[] getCompany() {
		return company;
	}

	public void setCompany(final LoadCompaniesReturnItem[] company) {
		this.company = company;
	}

	void setCompany(final BCompany[] a) {
		company=new LoadCompaniesReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			company[loop]=new LoadCompaniesReturnItem(a[loop]);
	}
}

	
