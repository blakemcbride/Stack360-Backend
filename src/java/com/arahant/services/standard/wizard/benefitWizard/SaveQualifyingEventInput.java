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
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.annotation.Validation;
import com.arahant.business.BLifeEvent;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;

public class SaveQualifyingEventInput extends TransmitInputBase {

	@Validation(required = false)
	private String explanation;
	@Validation(required = false)
	private String eventId;
	@Validation(type = "date", required = false)
	private int eventDate;
	@Validation(required = true)
	private String employeeId;

	void setData(BLifeEvent bc) {
		bc.setEventId(eventId);
		bc.setEventDate(eventDate);
		String empId = isEmpty(employeeId) ? ArahantSession.getHSU().getCurrentPerson().getPersonId() : employeeId;
		bc.setEmployeeId(empId);
		bc.setDescription(explanation);
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public int getEventDate() {
		return eventDate;
	}

	public void setEventDate(int eventDate) {
		this.eventDate = eventDate;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
