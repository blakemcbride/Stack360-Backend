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
 * Created on Feb 5, 2007
 * 
 */
package com.arahant.services.standard.misc.company;

import com.arahant.business.BCompany;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class ListCompanyReturn extends TransmitReturnBase {
	private ListCompanyReturnItem [] companies;

	/**
	 * @return Returns the companies.
	 */
	public ListCompanyReturnItem[] getCompanies() {
		return companies;
	}

	/**
	 * @param companies The companies to set.
	 */
	public void setCompanies(final ListCompanyReturnItem[] companies) {
		this.companies = companies;
	}

	/**
	 * @param companies2
	 */
	public void setCompanies(final BCompany[] comps) {
		companies=new ListCompanyReturnItem[comps.length];
		for (int loop=0;loop<comps.length;loop++)
			companies[loop]=new ListCompanyReturnItem(comps[loop]);
	} 
}

	
