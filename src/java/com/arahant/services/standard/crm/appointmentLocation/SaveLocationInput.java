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
package com.arahant.services.standard.crm.appointmentLocation;

import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BAppointmentLocation;
import com.arahant.utils.ArahantSession;

/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveLocationInput extends TransmitInputBase {

	void setData(BAppointmentLocation bc) {
		bc.setDescription(description);
		bc.setCode(code);
		bc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bc.setCompany(null);
		else
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}
	@Validation(min = 1, max = 16, required = true)
	private String id;
	@Validation(table = "appointment_location", column = "description", required = true)
	private String description;
	@Validation(table = "appointment_location", column = "code", required = true)
	private String code;
	@Validation(table = "appointment_location", column = "last_active_date", required = false, type = "date")
	private int lastActiveDate;
	@Validation(required = false)
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

	
