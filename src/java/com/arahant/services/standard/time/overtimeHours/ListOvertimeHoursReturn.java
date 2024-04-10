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
package com.arahant.services.standard.time.overtimeHours;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BOvertimeApproval;
import com.arahant.business.BProperty;
import com.arahant.utils.StandardProperty;


/**
 * 
 *
 *
 */
public class ListOvertimeHoursReturn extends TransmitReturnBase {
	private ListOvertimeHoursReturnItem item[];
	private double approvedHoursTotal;
	private double hoursTakenTotal;
	
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
	public ListOvertimeHoursReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final ListOvertimeHoursReturnItem[] item) {
		this.item = item;
	}

	public double getApprovedHoursTotal() {
		return approvedHoursTotal;
	}

	public void setApprovedHoursTotal(double approvedHoursTotal) {
		this.approvedHoursTotal = approvedHoursTotal;
	}

	public double getHoursTakenTotal() {
		return hoursTakenTotal;
	}

	public void setHoursTakenTotal(double hoursTakenTotal) {
		this.hoursTakenTotal = hoursTakenTotal;
	}

	

	/**
	 * @param accounts
	 */
	void setItem(final BOvertimeApproval[] a) {
		item=new ListOvertimeHoursReturnItem[a.length];
		for (int loop=0;loop<a.length;loop++)
		{
			item[loop]=new ListOvertimeHoursReturnItem(a[loop]);
			hoursTakenTotal+=item[loop].getHoursTaken();
			approvedHoursTotal+=item[loop].getApprovedHours();
		}
	}
}

	
