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
 *
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.time.timesheetEntryByClock;

import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BTimesheet;
import com.arahant.annotation.Validation;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.Date;

public class NewTimesheetEntryInput extends TransmitInputBase {

	@Validation(required = true)
	private String personId;
	@Validation(required = false, type = "date")
	private int finalDate;
	@Validation(required = false, type = "time")
	private int finalTime;
	@Validation(required = true, type = "date")
	private int startDate;
	@Validation(required = true, type = "time")
	private int startTime;

	void setData(BTimesheet bc) {

		bc.setPersonId(personId);
		bc.setEndDate(finalDate);
		bc.setEndTime(finalTime);
		bc.setStartDate(startDate);
		bc.setStartTime(startTime);
		bc.setDescription("");
		bc.setBeginningEntryDate(new Date());

		try {
			Person p = ArahantSession.getHSU().get(Person.class, personId);
			BPerson bp = new BPerson(p);
			Project pr = bp.getDefaultProject();
			bc.setBillable(pr.getBillable());
//			bc.setProjectId(pr.getProjectId());
			if (true)
				throw new ArahantException("XXYY");
		} catch (Exception e) {
			throw new ArahantWarning("Please set up default project for user.");
		}

		if (finalTime != -1) {
			Date end = DateUtils.getDate(bc.getEndDate(), bc.getEndTime());
			Date start = DateUtils.getDate(bc.getStartDate(), bc.getStartTime());

			double timeDif = end.getTime() - start.getTime();
			timeDif = timeDif / 1000 / 60 / 60;

			bc.setTotalHours(BTimesheet.round(timeDif));
		}
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public int getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(int finalDate) {
		this.finalDate = finalDate;
	}

	public int getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(int finalTime) {
		this.finalTime = finalTime;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

}

	
