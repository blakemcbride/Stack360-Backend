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
package com.arahant.services.standard.time.timesheetByPayPeriod;

import com.arahant.business.BPaySchedulePeriod;
import com.arahant.utils.DateUtils;


public class ListPayPeriodForGroupReturnItem {
	
	ListPayPeriodForGroupReturnItem(BPaySchedulePeriod bp) {
		periodStart = bp.getStartDate();
		periodEnd = bp.getEndDate();
		periodFormatted = DateUtils.getDateFormatted(bp.getStartDate()) + " - " + DateUtils.getDateFormatted(bp.getEndDate());
	}

	private String periodFormatted;
	private int periodStart;
	private int periodEnd;

	public int getPeriodEnd() {
		return periodEnd;
	}

	public void setPeriodEnd(int periodEnd) {
		this.periodEnd = periodEnd;
	}

	public String getPeriodFormatted() {
		return periodFormatted;
	}

	public void setPeriodFormatted(String periodFormatted) {
		this.periodFormatted = periodFormatted;
	}

	public int getPeriodStart() {
		return periodStart;
	}

	public void setPeriodStart(int periodStart) {
		this.periodStart = periodStart;
	}

	public ListPayPeriodForGroupReturnItem() {
		
	}	
}

	
