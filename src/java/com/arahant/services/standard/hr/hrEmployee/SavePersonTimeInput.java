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

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;

public class SavePersonTimeInput extends TransmitInputBase {

	@Validation(required = true)
    private String personId;
	@Validation(required = false)
    private boolean timeLog;
    @Validation(required = false)
    private boolean overtimeLogout;
    @Validation(required = false, min = 0, max = 16)
    private double workHours;
    @Validation(required = false, min = 0, max = 8)
    private double breakHours;
	@Validation(required=false)
	private boolean autoTimesheet;

	void makeEmployee(final BEmployee emp) throws ArahantException
	{
        emp.setWorkHours(workHours);
        emp.setBreakHours(breakHours);
        emp.setOvertimeLogout(overtimeLogout);
        emp.setTimeLog(timeLog);
		emp.setAutoLogTime(autoTimesheet?'Y':'N');
    }

	void makePerson(BPerson per) throws ArahantException
	{

    }

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

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
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

	
