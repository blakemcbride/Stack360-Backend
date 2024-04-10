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

package com.arahant.services.standard.time.timesheetAccountingReview;

import com.arahant.beans.Timesheet;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BTimesheet;
import com.arahant.utils.StandardProperty;
import com.arahant.business.BProperty;
import com.arahant.utils.HibernateScrollUtil;

import java.util.ArrayList;


public class SearchTimesheetsReturn extends TransmitReturnBase {
	private SearchTimesheetsReturnItem [] item;
	private double billableHours, nonBillableHours, totalHours;
	
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
	public SearchTimesheetsReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final SearchTimesheetsReturnItem[] item) {
		this.item = item;
	}

	public double getBillableHours() {
		return billableHours;
	}

	public void setBillableHours(double billableHours) {
		this.billableHours = billableHours;
	}

	public double getNonBillableHours() {
		return nonBillableHours;
	}

	public void setNonBillableHours(double nonBillableHours) {
		this.nonBillableHours = nonBillableHours;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	void setItem(HibernateScrollUtil<Timesheet> a) {
		ArrayList<SearchTimesheetsReturnItem> bta = new ArrayList<>();
		while (a.next()) {
		    BTimesheet bts = new BTimesheet(a.get());
		    bta.add(new SearchTimesheetsReturnItem(bts));
            if (bts.getBillable() == 'Y')
                billableHours += bts.getTotalHours();
            else
                nonBillableHours += bts.getTotalHours();
        }
		item = bta.toArray(new SearchTimesheetsReturnItem[bta.size()]);
		totalHours = billableHours + nonBillableHours;
	}
}

	
