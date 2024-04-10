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
 * Created on Oct 25, 2006
 */
package com.arahant.timertasks;

import java.util.Calendar;
import java.util.Date;


import com.arahant.beans.Employee;
import com.arahant.beans.Timesheet;
import com.arahant.business.BMessage;
import com.arahant.business.BRight;
import com.arahant.business.interfaces.RightNames;
import com.arahant.utils.*;

public class NotifyTimesheetEntryNeeded extends TimerTaskBase {

	private static final ArahantLogger logger = new ArahantLogger(NotifyTimesheetEntryNeeded.class);

	@Override
	public void execute() {
		final Date nextSunday = new Date();
		final Date today = new Date();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_YEAR, 2);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		cal.add(Calendar.DAY_OF_YEAR, -7);
		final Date lastSunday = cal.getTime();

		//get all employees
		final HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).scroll();

		//for each employee, get his timesheets for the current week
		while (scr.next()) {
			try {
				final Employee emp = scr.get();

				if (emp.getProphetLogin() == null) {
					continue; //can't login to do time anyway
				}
				if (BRight.checkRight(RightNames.MUST_LOG_TIME) == ArahantConstants.ACCESS_LEVEL_WRITE) {
					continue;
				}

				final HibernateCriteriaUtil hcu = hsu.createCriteria(Timesheet.class);
				hcu.eq("person", emp);
				hcu.le("workDate", nextSunday);
				hcu.gt("workDate", lastSunday);
				hcu.sum(Timesheet.TOTALHOURS);

				double hours = 0;

				final Double d = (Double) hcu.list().get(0);

				if (d != null) {
					hours = d.doubleValue();
				}

				if (hours < 40) {
					BMessage.send(emp, emp, "Time Entry Reminder", "Please log your time for the last week.");
				}
			} catch (final Exception e) {
				logger.error(e);
				e.printStackTrace();
				break;
			}
		}
		scr.close();
	}

	public static void main(String args[]) {
		new NotifyTimesheetEntryNeeded().run();
	}
}

	
