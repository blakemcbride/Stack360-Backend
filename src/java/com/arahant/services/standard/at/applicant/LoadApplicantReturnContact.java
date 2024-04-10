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

package com.arahant.services.standard.at.applicant;

import com.arahant.business.BApplicantContact;
import com.arahant.utils.DateUtils;

/**
 *
 */
public class LoadApplicantReturnContact {
	private String id;
	private String dateTimeFormatted;
	private int date;
	private int time;
	private String mode;
	private String description;
	private String status;
	
	public LoadApplicantReturnContact() {
	}


	LoadApplicantReturnContact(BApplicantContact bc) {
		id=bc.getId();
		dateTimeFormatted=DateUtils.getDateTimeFormatted(bc.getDate(), bc.getTime());
		date=bc.getDate();
		time=bc.getTime();
		mode=bc.getMode();
		description=bc.getDescription();
		status=bc.getStatus();
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public String getDateTimeFormatted() {
		return dateTimeFormatted;
	}

	public void setDateTimeFormatted(String dateTimeFormatted) {
		this.dateTimeFormatted = dateTimeFormatted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	
	
}
