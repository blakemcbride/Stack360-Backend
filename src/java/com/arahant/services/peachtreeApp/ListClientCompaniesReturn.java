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
 * Created on Feb 10, 2007
 * 
 */
package com.arahant.services.peachtreeApp;
import com.arahant.business.BCompanyBase;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 10, 2007
 *
 */
public class ListClientCompaniesReturn extends TransmitReturnBase {

	private Companies []companies;
	private int cap=50000;
	
	/**
	 * @return Returns the max.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param max The max to set.
	 */
	public void setCap(final int max) {
		this.cap = max;
	}

	public ListClientCompaniesReturn() {
		super();
	}

	/**
	 * @return Returns the companies.
	 */
	public Companies[] getCompanies() {
		return companies;
	}

	/**
	 * @param companies The companies to set.
	 */
	public void setCompanies(final Companies[] companies) {
		this.companies = companies;
	}

	/**
	 * @param companies2
	 */
	void setCompanies(final BCompanyBase[] c) {
		companies=new Companies[c.length];
		for (int loop=0;loop<c.length;loop++)
			companies[loop]=new Companies(c[loop]);
	}
}

	
