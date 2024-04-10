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
package com.arahant.services.aqDevImport;
import com.arahant.beans.CompanyBase;
import com.arahant.business.BCompanyBase;
import com.arahant.services.TransmitReturnBase;
import java.util.List;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchCompanyByTypeReturn extends TransmitReturnBase {

	private int cap=500;

	private Companies [] item;
	
	public SearchCompanyByTypeReturn() {
		super();
	}

	/**
	 * @return Returns the companies.
	 */
	public Companies[] getItem() {
		return item;
	}

	/**
	 * @param companies The companies to set.
	 */
	public void setItem(final Companies[] companies) {
		this.item = companies;
	}

	/**
	 * @param bases
	 */
	void setCompanies(final BCompanyBase[] bases) {
		item=new Companies[bases.length];
		for (int loop=0;loop<bases.length;loop++)
			item[loop]=new Companies(bases[loop]);
	}

        /**
	 * @param bases
	 */
	void setCompanies(List<CompanyBase> bases) {
		item=new Companies[bases.size()];
		for (int loop=0;loop<bases.size();loop++)
			item[loop]=new Companies(bases.get(loop));
	}
	/**
	 * @return Returns the cap.
	 */
	public int getCap() {
		return cap;
	}

	/**
	 * @param cap The cap to set.
	 */
	public void setCap(final int cap) {
		this.cap = cap;
	}
}

	
