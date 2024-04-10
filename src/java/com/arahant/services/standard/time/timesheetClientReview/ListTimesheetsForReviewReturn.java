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
package com.arahant.services.standard.time.timesheetClientReview;

import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;

public class ListTimesheetsForReviewReturn extends TransmitReturnBase {

	private double total;
	private TimesheetTransmit timesheetTransmit[];
	private int employeeFinalizedDate;
	private boolean benefitOverrun;

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

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getTimesheetTransmit()
	 */
	public TimesheetTransmit[] getTimesheetTransmit() {
		return timesheetTransmit;
	}

	public void setTimesheetTransmit(final TimesheetTransmit[] t) {

		timesheetTransmit = t;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setTimesheetTransmit(com.arahant.operations.transmit.ITimesheetTransmit[])
	 */
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

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#getEmployeeFinalizedDate()
	 */
	public int getEmployeeFinalizedDate() {
		return employeeFinalizedDate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetBatchTransmit#setEmployeeFinalizedDate(int)
	 */
	public void setEmployeeFinalizedDate(final int employeeFinalizedDate) {
		this.employeeFinalizedDate = employeeFinalizedDate;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
