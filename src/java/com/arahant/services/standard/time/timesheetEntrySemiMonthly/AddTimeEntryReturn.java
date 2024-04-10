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

import com.arahant.services.TransmitReturnBase;

public class AddTimeEntryReturn extends TransmitReturnBase {

	private String overrunBenefitName;
	private double overrunBenefitHours;

	public AddTimeEntryReturn() {
	}

	/**
	 * @return Returns the overrunBenefitHours.
	 */
	public double getOverrunBenefitHours() {
		return overrunBenefitHours;
	}

	/**
	 * @param overrunBenefitHours The overrunBenefitHours to set.
	 */
	public void setOverrunBenefitHours(final double overrunBenefitHours) {
		this.overrunBenefitHours = overrunBenefitHours;
	}

	/**
	 * @return Returns the overrunBenefitName.
	 */
	public String getOverrunBenefitName() {
		return overrunBenefitName;
	}

	/**
	 * @param overrunBenefitName The overrunBenefitName to set.
	 */
	public void setOverrunBenefitName(final String overrunBenefitName) {
		this.overrunBenefitName = overrunBenefitName;
	}
}
