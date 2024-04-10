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
package com.arahant.services.standard.time.timesheetAccountingReview;
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchTimesheetsInput extends TransmitInputBase {

	@Validation (required=false)
	private String companyId;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int timesheetStartDate;
	@Validation (min=19000101,max=30000101,type="date",required=false)
	private int timesheetEndDate;
	@Validation (required=false)
	private String projectId;
	@Validation (required=false)
	private String personId;
	@Validation (required=false)
	private int billableItemsIndicator;
	@Validation (required=false)
	private int timesheetStateIndicator;
	@Validation (required=false)
	private String timesheetState;
	@Validation (table="invoice",column="accounting_invoice_identifier",required=false)
	private String invoiceId;
	@Validation (min=2,max=5,required=false)
	private int invoiceIdSearchType;

	public String getInvoiceId() {
		return modifyForSearch(invoiceId,invoiceIdSearchType);
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public int getInvoiceIdSearchType() {
		return invoiceIdSearchType;
	}
	public void setInvoiceIdSearchType(int invoiceIdSearchType) {
		this.invoiceIdSearchType = invoiceIdSearchType;
	}
	public String getTimesheetState() {
		return timesheetState;
	}
	public void setTimesheetState(String timesheetState) {
		this.timesheetState = timesheetState;
	}
	public String getCompanyId()
	{
		return companyId;
	}
	public void setCompanyId(String companyId)
	{
		this.companyId=companyId;
	}
	public int getTimesheetStartDate()
	{
		return timesheetStartDate;
	}
	public void setTimesheetStartDate(int timesheetStartDate)
	{
		this.timesheetStartDate=timesheetStartDate;
	}
	public int getTimesheetEndDate()
	{
		return timesheetEndDate;
	}
	public void setTimesheetEndDate(int timesheetEndDate)
	{
		this.timesheetEndDate=timesheetEndDate;
	}
	public String getProjectId()
	{
		return projectId;
	}
	public void setProjectId(String projectId)
	{
		this.projectId=projectId;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public int getBillableItemsIndicator()
	{
		return billableItemsIndicator;
	}
	public void setBillableItemsIndicator(int billableItemsIndicator)
	{
		this.billableItemsIndicator=billableItemsIndicator;
	}
	public int getTimesheetStateIndicator()
	{
		return timesheetStateIndicator;
	}
	public void setTimesheetStateIndicator(int timesheetStateIndicator)
	{
		this.timesheetStateIndicator=timesheetStateIndicator;
	}


}

	
