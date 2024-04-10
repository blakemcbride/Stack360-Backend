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
package com.arahant.services.standard.billing.billingDetailReport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;


public class GetReportInput extends TransmitInputBase {

	@Validation (required = false)
	private boolean nonApproved;
	@Validation (required = false)
	private boolean approved;
	@Validation (required = false)
	private boolean invoiced;
	@Validation (type="date", required = false)
	private int startDate;
	@Validation (type="date", required = false)
	private int endDate;
	@Validation (required = false)
	private String clientId;
	@Validation (required = false)
	private String employeeId;
	@Validation (required = false)
	private String projectId;
	@Validation (required = false)
	private boolean billable;
	@Validation (required = false)
	private boolean nonBillable;
	

	public boolean getNonApproved()
	{
		return nonApproved;
	}
	public void setNonApproved(boolean nonApproved)
	{
		this.nonApproved = nonApproved;
	}
	public boolean getApproved()
	{
		return approved;
	}
	public void setApproved(boolean approved)
	{
		this.approved = approved;
	}
	public boolean getInvoiced()
	{
		return invoiced;
	}
	public void setInvoiced(boolean invoiced)
	{
		this.invoiced = invoiced;
	}
	public int getStartDate()
	{
		return startDate;
	}
	public void setStartDate(int startDate)
	{
		this.startDate = startDate;
	}
	public int getEndDate()
	{
		return endDate;
	}
	public void setEndDate(int endDate)
	{
		this.endDate = endDate;
	}
	public String getClientId()
	{
		return clientId;
	}
	public void setClientId(String clientId)
	{
		this.clientId = clientId;
	}
	public String getEmployeeId()
	{
		return employeeId;
	}
	public void setEmployeeId(String employeeId)
	{
		this.employeeId = employeeId;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId = projectId;
	}
	public boolean getBillable()
	{
		return billable;
	}
	public void setBillable(boolean billable)
	{
		this.billable = billable;
	}
	public boolean getNonBillable()
	{
		return nonBillable;
	}
	public void setNonBillable(boolean nonBillable)
	{
		this.nonBillable = nonBillable;
	}


}

	
