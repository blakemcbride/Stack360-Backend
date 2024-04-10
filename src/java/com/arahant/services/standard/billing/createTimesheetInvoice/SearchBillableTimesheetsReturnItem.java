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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.business.BTimesheet;

public class SearchBillableTimesheetsReturnItem {

	private String billable;
	private double billingRate;
	private String externalReference;
	private String firstName;
	private String lastName;
	private String defaultServiceId;
	private String projectName;
	private String timeDescription;
	private String timesheetId;
	private double totalHours;
	private int workDate;
	private String projectId;
	private String purchaseOrder;
	private String billingType;  //  (H)ourly / (P)roject

	public SearchBillableTimesheetsReturnItem() {
	}

	private static String getShiftSuffix(BTimesheet bc) {
		String shiftStart = bc.getProjectShift().getShiftStart();
		if (shiftStart != null  &&  !shiftStart.isEmpty())
			return " (" + shiftStart + ")";
		else
			return "";
	}

	SearchBillableTimesheetsReturnItem(BTimesheet bc) {
		billable = bc.getBillable() + "";
		billingRate = bc.getBillingRate();
		externalReference = bc.getExternalReference();
		firstName = bc.getFirstName();
		lastName = bc.getLastName();
		defaultServiceId = bc.getProductProphetId();
		projectName = bc.getProjectName();
		timeDescription = bc.getDescription();
		billingType = "" + bc.getProject().getBillingType();

		if (timeDescription == null  ||  timeDescription.isEmpty())
		    timeDescription = bc.getProjectDescription() + getShiftSuffix(bc);

		timesheetId = bc.getTimesheetId();
		totalHours = bc.getTotalHours();
		workDate = bc.getWorkDate();
		projectId = bc.getProjectID();
		purchaseOrder = bc.getProject().getPurchaseOrder();
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(String billable) {
		this.billable = billable;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(double billingRate) {
		this.billingRate = billingRate;
	}

	public String getDefaultServiceId() {
		return defaultServiceId;
	}

	public void setDefaultServiceId(String defaultServiceId) {
		this.defaultServiceId = defaultServiceId;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTimeDescription() {
		return timeDescription;
	}

	public void setTimeDescription(String timeDescription) {
		this.timeDescription = timeDescription;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(double totalHours) {
		this.totalHours = totalHours;
	}

	public int getWorkDate() {
		return workDate;
	}

	public void setWorkDate(int workDate) {
		this.workDate = workDate;
	}

	public String getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(String purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getBillingType() {
		return billingType;
	}

	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
}
