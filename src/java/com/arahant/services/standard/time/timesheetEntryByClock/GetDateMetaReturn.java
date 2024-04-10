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
 *
 */
package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;
import java.util.Calendar;

public class GetDateMetaReturn extends TransmitReturnBase {

	private int fromDate;
	private int toDate;

	public void setData()
	{
		Calendar c = Calendar.getInstance();

        int d = c.get(Calendar.DAY_OF_MONTH);
		int m =  c.get(Calendar.MONTH) + 1;
		int y = c.get(Calendar.YEAR);

        int date = y * 10000 + m * 100 + d;

		int day = c.get(Calendar.DAY_OF_WEEK);

		if (day == Calendar.SUNDAY)
		{
			fromDate=DateUtils.add(date, -6);
			toDate=date;
		}
		else
		{
			if (day != Calendar.MONDAY)
				day -= Calendar.MONDAY;
			else
				day = 0;

			date = DateUtils.add(date, -day);
			fromDate=date;

			date = DateUtils.add(date, 6);
			toDate=date;
		}
	}

	public int getFromDate()
	{
		return fromDate;
	}
	public void setFromDate(int fromDate)
	{
		this.fromDate=fromDate;
	}
	public int getToDate()
	{
		return toDate;
	}
	public void setToDate(int toDate)
	{
		this.toDate=toDate;
	}

}

	
