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
 * Created on Mar 15, 2007
 * 
 */
package com.arahant.services.standard.hr.hrAccruedTimeOff;
import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;
import java.util.Calendar;


/**
 * 
 *
 * Created on Mar 15, 2007
 *
 */
public class GetBenefitPeriodReturn extends TransmitReturnBase {

	private int startDate;
	private String startDateFormatted;
	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return Returns the startDateFormatted.
	 */
	public String getStartDateFormatted() {
		return startDateFormatted;
	}
	/**
	 * @param startDateFormatted The startDateFormatted to set.
	 */
	public void setStartDateFormatted(final String startDateFormatted) {
		this.startDateFormatted = startDateFormatted;
	}
	/**
	 * @param emp
	 */
	void setData(final BEmployee emp) {
		/* we have a request to make this go yearly
		 * we need to make it take the type of accrual in the future
		 * so we can decide what to use

		startDate=emp.getLastAnniversaryDate();

		 *
		 */
		Calendar startOfYear=Calendar.getInstance();
		startOfYear.set(Calendar.DAY_OF_YEAR,1);
		startDate=DateUtils.getDate(startOfYear);
		startDateFormatted=DateUtils.getDateFormatted(startDate);
	}
	
	
}

	
