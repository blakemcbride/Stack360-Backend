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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.project.projectTimesheetReportClient;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetProjectReportInput extends TransmitInputBase {


	@Validation (required=false)
	private String projectId;
	@Validation (required=false)
	private String clientCompanyId;
	@Validation (type="date",required=false)
	private int startDate;
	@Validation (type="date",required=false)
	private int endDate;
	@Validation (required=false)
	private boolean notApproved;
	@Validation (required=false)
	private boolean invoiced;
	@Validation (required=false)
	private boolean approved;
	/**
	 * @return Returns the approved.
	 */
	public boolean isApproved() {
		return approved;
	}
	/**
	 * @param approved The approved to set.
	 */
	public void setApproved(final boolean approved) {
		this.approved = approved;
	}
	/**
	 * @return Returns the clientCompanyId.
	 */
	public String getClientCompanyId() {
		return clientCompanyId;
	}
	/**
	 * @param clientCompanyId The clientCompanyId to set.
	 */
	public void setClientCompanyId(final String clientCompanyId) {
		this.clientCompanyId = clientCompanyId;
	}

	/**
	 * @return Returns the endDate.
	 */
	public int getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate The endDate to set.
	 */
	public void setEndDate(final int endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return Returns the invoiced.
	 */
	public boolean isInvoiced() {
		return invoiced;
	}
	/**
	 * @param invoiced The invoiced to set.
	 */
	public void setInvoiced(final boolean invoiced) {
		this.invoiced = invoiced;
	}
	/**
	 * @return Returns the notApproved.
	 */
	public boolean isNotApproved() {
		return notApproved;
	}
	/**
	 * @param notApproved The notApproved to set.
	 */
	public void setNotApproved(final boolean notApproved) {
		this.notApproved = notApproved;
	}
	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return Returns the startDate.
	 */
	public int getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate The startDate to set.
	 */
	public void setStartDate(final int startDate) {
		this.startDate = startDate;
	}
}

	
