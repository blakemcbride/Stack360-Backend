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

package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.business.BTimesheet;

public class SearchTimesheetsReturnItem {

	public SearchTimesheetsReturnItem() {
	}

	SearchTimesheetsReturnItem(BTimesheet bc) {

		id = bc.getId();
		finalTime = bc.getEndTime();
		elapsedTimeFormatted = bc.getElapsedTimeFormatted();
		startDate = bc.getStartDate();
		startTime = bc.getStartTime();
		finalDate = bc.getEndDate();
		elapsedTime = bc.getElapsedTime();

	}
	private String id;
	private int finalTime;
	private String elapsedTimeFormatted;
	private int startDate;
	private int startTime;
	private int finalDate;
	private int elapsedTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(int finalTime) {
		this.finalTime = finalTime;
	}

	public String getElapsedTimeFormatted() {
		return elapsedTimeFormatted;
	}

	public void setElapsedTimeFormatted(String elapsedTimeFormatted) {
		this.elapsedTimeFormatted = elapsedTimeFormatted;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}
}
