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
 * 
 */
package com.arahant.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.arahant.beans.Employee;
import com.arahant.beans.Timesheet;
import com.arahant.business.BMessage;
import com.arahant.business.BRight;
import com.arahant.business.interfaces.RightNames;



/**
 * 
 *
 * Created on Oct 25, 2006
 *
 */
public class NotifyTimesheetEntryNeeded extends TimerTask {

	Logger logger = Logger.getLogger(NotifyTimesheetEntryNeeded.class);
	
	/* (non-Javadoc)
	 * @see java.util.TimerTask#run()
	 */
	@Override
	public void run() {
		
		
		final HibernateSessionUtil hsu = new HibernateSessionUtil();
		
		final Date nextSunday=new Date();
		final Date today=new Date();
		final Calendar cal=Calendar.getInstance();
		cal.setTime(today);
		cal.add(Calendar.DAY_OF_YEAR,2);
		cal.set(Calendar.HOUR_OF_DAY,23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		
		cal.add(Calendar.DAY_OF_YEAR,-7);
		final Date lastSunday=cal.getTime();

		
		try
		{	
			hsu.beginTransaction();
			//get all employees
			final HibernateScrollUtil<Employee> scr=hsu.createCriteria(Employee.class).scroll();
			
			//for each employee, get his timesheets for the current week
			while (scr.next())
			{
				try {
					final Employee emp=scr.get();
					
					if (BRight.checkRight(RightNames.MUST_LOG_TIME)==ArahantConstants.ACCESS_LEVEL_WRITE)
						continue;
					
					
					final HibernateCriteriaUtil hcu=hsu.createCriteria(Timesheet.class);
					hcu.eq("person", emp);
					hcu.le("workDate", nextSunday);
					hcu.gt("workDate", lastSunday);
					hcu.sum(Timesheet.TOTALHOURS);
				
					double hours=0;
					
					final Double d=(Double)hcu.list().get(0);
					
					if (d!=null)
						hours=d.doubleValue();
					
					if (hours<40)
						BMessage.send(emp, emp, "Time Entry Reminder", "Please log your time for the last week.");
				} catch (final Exception e) {
					logger.error(e);
					break;
				}
					
			
			}
			
			scr.close();
			hsu.commitTransaction();
		}
		catch (final Exception e)
		{
			hsu.rollbackTransaction();
			logger.error(e);
		}
		hsu.close();
	}

}

	
