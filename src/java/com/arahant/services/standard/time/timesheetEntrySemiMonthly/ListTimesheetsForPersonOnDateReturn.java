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
package com.arahant.services.standard.time.timesheetEntrySemiMonthly;

import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;

public class ListTimesheetsForPersonOnDateReturn extends TransmitReturnBase {

	private TimesheetTransmit timesheetTransmit[];
	private double totalHours;
	private double totalBillableHours;
	private int employeeFinalizedDate;
	private int mode;  //0 = normal; 1=rejections and currently read only; 2=rejections and on reject day
	private int remainingRejectedDays;
	private int startDate;  //  period start date
	private int endDate;  // ending period date
	private boolean showBillable;

	public ListTimesheetsForPersonOnDateReturn() {
	}

	public TimesheetTransmit[] getTimesheetTransmit() {
		return timesheetTransmit;
	}

	public void setTimesheetTransmit(final TimesheetTransmit[] timesheetTransmit) {
		this.timesheetTransmit = timesheetTransmit;
	}

	public double getTotalBillableHours() {
		return totalBillableHours;
	}

	public void setTotalBillableHours(final double totalBillableHours) {
		this.totalBillableHours = totalBillableHours;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	public int getEmployeeFinalizedDate() {
		return employeeFinalizedDate;
	}

	public void setEmployeeFinalizedDate(final int employeeFinalizedDate) {
		this.employeeFinalizedDate = employeeFinalizedDate;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(final int mode) {
		this.mode = mode;
	}

	public int getRemainingRejectedDays() {
		return remainingRejectedDays;
	}

	public void setRemainingRejectedDays(final int remainingRejectedDays) {
		this.remainingRejectedDays = remainingRejectedDays;
	}

	void setTimesheetTransmit(final BTimesheet[] ts) {
		timesheetTransmit = new TimesheetTransmit[ts.length];
		for (int loop = 0; loop < ts.length; loop++)
			timesheetTransmit[loop] = new TimesheetTransmit(ts[loop]);
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public boolean getShowBillable() {
		return showBillable;
	}

	public void setShowBillable(boolean showBillable) {
		this.showBillable = showBillable;
	}
}
