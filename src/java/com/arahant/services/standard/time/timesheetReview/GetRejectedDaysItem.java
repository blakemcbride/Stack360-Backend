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
package com.arahant.services.standard.time.timesheetReview;

import com.arahant.utils.DateUtils;

public class GetRejectedDaysItem {

	private int date;
	private String dateFormatted;

	public GetRejectedDaysItem() {
	}

	/**
	 * @param i
	 */
	GetRejectedDaysItem(final int i) {
		date = i;
		dateFormatted = DateUtils.getDateFormatted(i);
	}

	/**
	 * @return Returns the date.
	 */
	public int getDate() {
		return date;
	}

	/**
	 * @param date The date to set.
	 */
	public void setDate(final int date) {
		this.date = date;
	}

	/**
	 * @return Returns the dateFormatted.
	 */
	public String getDateFormatted() {
		return dateFormatted;
	}

	/**
	 * @param dateFormatted The dateFormatted to set.
	 */
	public void setDateFormatted(final String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}
}
