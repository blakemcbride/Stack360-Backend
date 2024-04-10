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
package com.arahant.services.standard.hr.hrEmployeeStatusChangedReport;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class GetReportInput extends TransmitInputBase {

	@Validation (table="hr_employee_status",column="status_id",required=true)
	private String statusId;
	@Validation (type="date",required=true)
	private int startDate;
	@Validation (type="date",required=true)
	private int endDate;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private boolean includeSubOrgGroups;
	@Validation(required=true)
	private int depth;

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public int getStartDate() {
		return startDate;
	}

	public void setStartDate(int startDate) {
		this.startDate = startDate;
	}

	public String getStatusId()
	{
		return statusId;
	}
	public void setStatusId(final String statusId)
	{
		this.statusId=statusId;
	}

    public String getOrgGroupId() {
        return orgGroupId;
    }

    public void setOrgGroupId(String orgGroupId) {
        this.orgGroupId = orgGroupId;
    }

    /**
     * @return the includeSubOrgGroups
     */
    public boolean getIncludeSubOrgGroups() {
        return includeSubOrgGroups;
    }

    /**
     * @param includeSubOrgGroups the includeSubOrgGroups to set
     */
    public void setIncludeSubOrgGroups(boolean includeSubOrgGroups) {
        this.includeSubOrgGroups = includeSubOrgGroups;
    }

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

}

	
