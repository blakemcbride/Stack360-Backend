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
package com.arahant.services.standard.time.timesheetReview2;

import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;

public class ListTimesheetsForReviewReturn extends TransmitReturnBase {

	private double total;
	private TimesheetTransmit timesheetTransmit[];
	private int employeeFinalizedDate;
	private boolean benefitOverrun;
	private int begDateRange;
	private int endDateRange;

	public ListTimesheetsForReviewReturn() {
	}

	/**
	 * @return Returns the benefitOverrun.
	 */
	public boolean isBenefitOverrun() {
		return benefitOverrun;
	}

	/**
	 * @param benefitOverrun The benefitOverrun to set.
	 */
	public void setBenefitOverrun(final boolean benefitOverrun) {
		this.benefitOverrun = benefitOverrun;
	}

	public TimesheetTransmit[] getTimesheetTransmit() {
		return timesheetTransmit;
	}

	public void setTimesheetTransmit(final TimesheetTransmit[] t) {

		timesheetTransmit = t;
	}

	void setTimesheetTransmit(final BTimesheet[] t) throws ArahantException {
		benefitOverrun = false;
		this.timesheetTransmit = new TimesheetTransmit[t.length];
		for (int loop = 0; loop < t.length; loop++) {
			total += t[loop].getTotalHours();
			timesheetTransmit[loop] = new TimesheetTransmit(t[loop]);
			if (t[loop].hasRunOver())
				benefitOverrun = true;
		}
	}

	public int getEmployeeFinalizedDate() {
		return employeeFinalizedDate;
	}

	public void setEmployeeFinalizedDate(final int employeeFinalizedDate) {
		this.employeeFinalizedDate = employeeFinalizedDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getBegDateRange() {
		return begDateRange;
	}

	public void setBegDateRange(int begDateRange) {
		this.begDateRange = begDateRange;
	}

	public int getEndDateRange() {
		return endDateRange;
	}

	public void setEndDateRange(int endDateRange) {
		this.endDateRange = endDateRange;
	}
	
}
