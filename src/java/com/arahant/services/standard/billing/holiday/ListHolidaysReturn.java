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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.billing.holiday;

import com.arahant.business.BHoliday;
import com.arahant.services.TransmitReturnBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class ListHolidaysReturn extends TransmitReturnBase {
	Holidays [] holidays;

	/**
	 * @return Returns the holidays.
	 */
	public Holidays[] getHolidays() {
		return holidays;
	}

	/**
	 * @param holidays The holidays to set.
	 */
	public void setHolidays(final Holidays[] holidays) {
		this.holidays = holidays;
	}

	/**
	 * @param holidaies
	 */
	void setHolidays(final BHoliday[] h) {
		holidays=new Holidays[h.length];
		for (int loop=0;loop<h.length;loop++)
			holidays[loop]=new Holidays(h[loop]);
	}
}

	
