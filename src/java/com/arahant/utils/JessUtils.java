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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.utils;

import java.util.Calendar;

/**
 *
 */
public class JessUtils {

	public static int addYears(int x, int y)
	{
		Calendar d1=DateUtils.getCalendar(x);
		d1.add(Calendar.YEAR, y);
		return DateUtils.getDate(d1);
	}
	
	public static int subtractDates(int x, int y)
	{
		Calendar d1=DateUtils.getCalendar(y);
		Calendar d2=DateUtils.getCalendar(x);

		return (int) ((d2.getTimeInMillis() - d1.getTimeInMillis()) / 1000 / 60 / 60 / 24);
	}

	public static void reportError(Exception e, ArahantLogger logger) {
		Throwable e2 = e.getCause();
		if (e2 != null) {
			String msg = e2.getMessage();
			if (msg != null)
				logger.error(msg);
		}
		logger.error(e);
	}
}
