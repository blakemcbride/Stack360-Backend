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
 * Created on Feb 9, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetEntryExceptions;
import com.arahant.business.BPerson;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 * Created on Feb 9, 2007
 *
 */
public class ListEmployeesForTimesheetExceptionReportReturn extends TransmitReturnBase {

	private Slackers [] missingTimes;
	
	public ListEmployeesForTimesheetExceptionReportReturn() {
		super();
	}

	/**
	 * @return Returns the missingTimes.
	 */
	public Slackers[] getMissingTimes() {
		return missingTimes;
	}

	/**
	 * @param missingTimes The missingTimes to set.
	 */
	public void setMissingTimes(final Slackers[] missingTimes) {
		this.missingTimes = missingTimes;
	}

	/**
	 * @param p
	 */
	void setMissingTimes(final BPerson[] p) {
		missingTimes=new Slackers[p.length];
		for (int loop=0;loop<p.length;loop++)
			missingTimes[loop]=new Slackers(p[loop]);
	}
}

	
