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

package com.arahant.services.standard.time.timesheetEntrySemiMonthly;

import com.arahant.services.TransmitReturnBase;

/**
 *
 * @author Blake McBride
 */
public class GetCurrentPeriodReturn extends TransmitReturnBase {

	private int startDate;
	private int endDate;
	private boolean showBillable;

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

	public boolean isShowBillable() {
		return showBillable;
	}

	public void setShowBillable(boolean showBillable) {
		this.showBillable = showBillable;
	}
	
}
