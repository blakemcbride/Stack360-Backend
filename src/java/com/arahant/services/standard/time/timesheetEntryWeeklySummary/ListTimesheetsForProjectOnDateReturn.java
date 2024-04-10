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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;

public class ListTimesheetsForProjectOnDateReturn extends TransmitReturnBase {

	private TimesheetTransmit timesheetTransmit[];
	private double totalHours;

	public ListTimesheetsForProjectOnDateReturn() {
	}

	public TimesheetTransmit[] getTimesheetTransmit() {
		return timesheetTransmit;
	}

	public void setTimesheetTransmit(final TimesheetTransmit[] timesheetTransmit) {
		this.timesheetTransmit = timesheetTransmit;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	void setTimesheetTransmit(final BTimesheet[] ts) {
		timesheetTransmit = new TimesheetTransmit[ts.length];
		for (int loop = 0; loop < ts.length; loop++)
			timesheetTransmit[loop] = new TimesheetTransmit(ts[loop]);
	}
}
