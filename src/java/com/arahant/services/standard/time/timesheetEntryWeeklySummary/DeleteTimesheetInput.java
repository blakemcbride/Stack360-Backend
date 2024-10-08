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
package com.arahant.services.standard.time.timesheetEntryWeeklySummary;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class DeleteTimesheetInput extends TransmitInputBase {

	@Validation(required = false)
	private String[] timesheetIds;
    @Validation(required = true)
	private String projectID;
	private String shiftId;
	@Validation(required = true)
	private String personID;

	public DeleteTimesheetInput() {
	}

	/**
	 * @return Returns the timesheetIds.
	 */
	public String[] getTimesheetIds() {
		if (timesheetIds == null)
			return new String[0];
		return timesheetIds;
	}

	/**
	 * @param timesheetIds The timesheetIds to set.
	 */
	public void setTimesheetIds(final String[] timesheetIds) {
		this.timesheetIds = timesheetIds;
	}

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
