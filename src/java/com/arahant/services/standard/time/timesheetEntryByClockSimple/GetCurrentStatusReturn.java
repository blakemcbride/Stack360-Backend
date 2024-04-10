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
package com.arahant.services.standard.time.timesheetEntryByClockSimple;

import com.arahant.services.standard.time.timesheetEntryByClock.*;
import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;

/**
 * 
 *
 *
 */
public class GetCurrentStatusReturn extends TransmitReturnBase {

	void setData(BTimesheet bc) {
		currentStatusSince = bc.getCurrentStatusSince();
		currentStatus = bc.getCurrentStatus();
	}
	private int currentTime = DateUtils.nowTime();
	private String currentStatusSince;
	private String currentStatus;
	private int currentDate = DateUtils.now();
	private int serverTimeZone = DateUtils.getTimeZoneOffset();

	public int getServerTimeZone() {
		return serverTimeZone;
	}

	public void setServerTimeZone(int serverTimeZone) {
		this.serverTimeZone = serverTimeZone;
	}

	public int getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(int currentDate) {
		this.currentDate = currentDate;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public String getCurrentStatusSince() {
		return currentStatusSince;
	}

	public void setCurrentStatusSince(String currentStatusSince) {
		this.currentStatusSince = currentStatusSince;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}
}

	
