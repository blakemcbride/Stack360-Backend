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
package com.arahant.services.standard.hr.hrEmployee;

import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;

public class LoadPersonTimeReturn extends TransmitReturnBase {

	public LoadPersonTimeReturn() {
	}

	public void setData(BPerson person)
	{
		if (person.isEmployee())
		{
            BEmployee emp = new BEmployee(person);

            timeLog = emp.getClockInTimeLog();
            overtimeLogout = emp.getOvertimeLogout();
            workHours = emp.getWorkHours();
            breakHours = emp.getBreakHours();
			autoTimesheet = emp.getAutoLogTime() == 'Y'?true:false;
        }
	}
	
	private boolean timeLog;
    private boolean overtimeLogout;
    private double workHours;
    private double breakHours;
	private boolean autoTimesheet;

	public double getBreakHours() {
		return breakHours;
	}

	public void setBreakHours(double breakHours) {
		this.breakHours = breakHours;
	}

	public boolean isOvertimeLogout() {
		return overtimeLogout;
	}

	public void setOvertimeLogout(boolean overtimeLogout) {
		this.overtimeLogout = overtimeLogout;
	}

	public boolean isTimeLog() {
		return timeLog;
	}

	public void setTimeLog(boolean timeLog) {
		this.timeLog = timeLog;
	}

	public double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(double workHours) {
		this.workHours = workHours;
	}

	public boolean getAutoTimesheet() {
		return autoTimesheet;
	}

	public void setAutoTimesheet(boolean autoTimesheet) {
		this.autoTimesheet = autoTimesheet;
	}
}

	
