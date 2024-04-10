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
package com.arahant.services.standard.billing.billingDetailReport;

import com.arahant.utils.StandardProperty;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 5, 2007
 *
 */
public class SearchEmployeesReturn extends TransmitReturnBase{
	private SearchEmployeesReturnItem [] employees;
	private SearchEmployeesReturnItem selectedItem;
	
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

	public SearchEmployeesReturnItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(SearchEmployeesReturnItem selectedItem) {
		this.selectedItem = selectedItem;
	}
	

	
	/**
	 * @return Returns the employees.
	 */
	public SearchEmployeesReturnItem[] getEmployees() {
		return employees;
	}

	/**
	 * @param employees The employees to set.
	 */
	public void setEmployees(final SearchEmployeesReturnItem[] employees) {
		this.employees = employees;
	}

	/**
	 * @param employees2
	 */
	void setEmployees(final BEmployee[] e) {
		employees=new SearchEmployeesReturnItem[e.length];
		for (int loop=0;loop<e.length;loop++)
			employees[loop]=new SearchEmployeesReturnItem(e[loop]);
	}
}

	
