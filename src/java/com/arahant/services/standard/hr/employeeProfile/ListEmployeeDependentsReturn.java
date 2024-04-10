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
package com.arahant.services.standard.hr.employeeProfile;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;

public class ListEmployeeDependentsReturn extends TransmitReturnBase {
	private String employeeName;

	ListEmployeeDependentsReturnItem dependents[];
	
	private int cap=BProperty.getInt(StandardProperty.SEARCH_MAX);
	
	public void setCap(int x)
	{
		cap=x;
	}
	
	public int getCap()
	{
		return cap;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return Returns the item.
	 */
	public ListEmployeeDependentsReturnItem[] getDependents() {
		return dependents;
	}

	/**
	 * @param item The item to set.
	 */
	public void setDependents(final ListEmployeeDependentsReturnItem[] dependents) {
		this.dependents = dependents;
	}

	/**
	 * @param accounts
	 */
	void setDependents(final BHREmplDependent[] a) {
		dependents=new ListEmployeeDependentsReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
			dependents[loop]=new ListEmployeeDependentsReturnItem(a[loop]);
	}
}

	
