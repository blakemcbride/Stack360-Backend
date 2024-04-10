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
package com.arahant.services.standard.time.timesheetClientReview;

import com.arahant.business.BProject;
import com.arahant.services.TransmitReturnBase;

public class GetProjectDetailReturn extends TransmitReturnBase {

	private String detail;
	private String phaseFormatted;
	private String requestingNameFormatted;
	private String statusFormatted;
	private String summary;
	private String requestingPersonOrCreatedBy;

	public String getRequestingPersonOrCreatedBy() {
		return requestingPersonOrCreatedBy;
	}

	public void setRequestingPersonOrCreatedBy(String requestingPersonOrCreatedBy) {
		this.requestingPersonOrCreatedBy = requestingPersonOrCreatedBy;
	}

	void setData(BProject bProject) {
		detail = bProject.getDetailDesc();
		phaseFormatted = bProject.getPhaseCode() + " - " + bProject.getPhaseDescription();
		requestingNameFormatted = bProject.getRequestingGroup();
		statusFormatted = bProject.getProjectStatusCode() + " - " + bProject.getProjectStatusDescription();
		summary = bProject.getSummary();
		requestingPersonOrCreatedBy = bProject.getRequestingPersonOrCreatedBy();
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getPhaseFormatted() {
		return phaseFormatted;
	}

	public void setPhaseFormatted(String phaseFormatted) {
		this.phaseFormatted = phaseFormatted;
	}

	public String getRequestingNameFormatted() {
		return requestingNameFormatted;
	}

	public void setRequestingNameFormatted(String requestingNameFormatted) {
		this.requestingNameFormatted = requestingNameFormatted;
	}

	public String getStatusFormatted() {
		return statusFormatted;
	}

	public void setStatusFormatted(String statusFormatted) {
		this.statusFormatted = statusFormatted;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
}
