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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.services.TransmitReturnBase;
import com.arahant.business.BProject;


/**
 * 
 *
 *
 */
public class GetProjectDetailForToolTipReturn extends TransmitReturnBase {

	void setData(BProject bc)
	{
		summary=bc.getSummary();
		detail=bc.getDetailDesc();
		phaseFormatted=bc.getPhaseCode()+" - "+bc.getPhaseDescription();
		statusFormatted=bc.getProjectStatusCode()+" - "+bc.getProjectStatusDescription();
		requestingNameFormatted=bc.getRequestingGroup();
		requestingPersonOrCreatedBy=bc.getRequestingPersonOrCreatedBy();
	}
	
	private String summary;
	private String phaseFormatted;
	private String statusFormatted;
	private String detail;
	private String requestingNameFormatted;
	private String requestingPersonOrCreatedBy;

	public String getRequestingPersonOrCreatedBy() {
		return requestingPersonOrCreatedBy;
	}

	public void setRequestingPersonOrCreatedBy(String requestingPersonOrCreatedBy) {
		this.requestingPersonOrCreatedBy = requestingPersonOrCreatedBy;
	}

	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary=summary;
	}

	public String getDetail()
	{
		return detail;
	}
	public void setDetail(String detail)
	{
		this.detail=detail;
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

}

	
