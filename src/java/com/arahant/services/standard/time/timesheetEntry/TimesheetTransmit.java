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

package com.arahant.services.standard.time.timesheetEntry;

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;

public class TimesheetTransmit {

	private String state;
	private String finalized;
	private String messageId;
	private String companyName;
	private String beginTimeFormatted;
	private String endTimeFormatted;
	private String projectName;
	private String workDateFormatted;
	private double billingRate;
	private String firstName;
	private String lastName;
	private String externalReference;
	private String projectDescription;
	private String acctSystemAccount;
	private String acctSystemId;
	private String timeDescription;
	private String companyId;
	private String message;
	private String timesheetId;
	private String projectId;
	private String shiftId;
	private String personId;
	private String description;
	private double totalHours;
	private int workDate;
	private int beginningTime;
	private int endTime;
	private String billable;
    private String timeTypeId;
    private String timeType;

	public TimesheetTransmit() {
	}

	TimesheetTransmit(final BTimesheet t) {
		state = t.getState() + "";
		finalized = t.getFinalized();
		messageId = t.getMessageId();
		companyName = t.getCompanyName();
		projectName = t.getProjectName();
		billingRate = t.getBillingRate();
		firstName = t.getFirstName();
		lastName = t.getLastName();
		externalReference = t.getExternalReference();
		projectDescription = t.getProjectDescription();
		acctSystemAccount = t.getAcctSystemAccount();
		acctSystemId = t.getAcctSystemId();
		timeDescription = t.getDescription();
		companyId = t.getCompanyId();
		message = t.getMessage();
		timesheetId = t.getTimesheetId();
		projectId = t.getProjectID();
		shiftId = t.getProjectShiftId();
		personId = t.getPersonId();
		description = t.getProjectDescription();
		totalHours = t.getTotalHours();
		workDate = t.getWorkDate();
		beginningTime = t.getBeginningTime();
		endTime = t.getEndTime();
		billable = t.getBillable() + "";
        timeTypeId = t.getTimeTypeId();
        timeType = t.getTimeType().getDescription();

		workDateFormatted = DateUtils.getDateFormatted(workDate);
		beginTimeFormatted = DateUtils.getTimeFormatted(beginningTime);
		endTimeFormatted = DateUtils.getTimeFormatted(endTime);
	}

	public String getState() {
		return state;
	}

	public void setState(final String statusCode) {
		this.state = statusCode;
	}

	public String getFinalized() {
		return finalized;
	}

	public void setFinalized(final String finalized) {
		this.finalized = finalized;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(final String messageId) {
		this.messageId = messageId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	public String getBeginTimeFormatted() {
		return beginTimeFormatted;
	}

	public void setBeginTimeFormatted(final String beginTimeFormatted) {
		this.beginTimeFormatted = beginTimeFormatted;
	}

	public String getEndTimeFormatted() {
		return endTimeFormatted;
	}

	public void setEndTimeFormatted(final String endTimeFormatted) {
		this.endTimeFormatted = endTimeFormatted;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	public String getWorkDateFormatted() {
		return workDateFormatted;
	}

	public void setWorkDateFormatted(final String workDateFormatted) {
		this.workDateFormatted = workDateFormatted;
	}

	public double getBillingRate() {
		return billingRate;
	}

	public void setBillingRate(final double billingRate) {
		this.billingRate = billingRate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(final String externalReference) {
		this.externalReference = externalReference;
	}

	public String getAcctSystemAccount() {
		return acctSystemAccount;
	}

	public void setAcctSystemAccount(final String glAcctId) {
		this.acctSystemAccount = glAcctId;
	}

	public String getAcctSystemId() {
		return acctSystemId;
	}

	public void setAcctSystemId(final String glAcctDefNumber) {
		this.acctSystemId = glAcctDefNumber;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(final String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getTimeDescription() {
		return timeDescription;
	}

	public void setTimeDescription(final String timeDescription) {
		this.timeDescription = timeDescription;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public int getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime(final int beginningTime) {
		this.beginningTime = beginningTime;
	}

	public String getBillable() {
		return billable;
	}

	public void setBillable(final String billable) {
		this.billable = billable;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(final int endTime) {
		this.endTime = endTime;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(final String timesheetId) {
		this.timesheetId = timesheetId;
	}

	public double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	public int getWorkDate() {
		return workDate;
	}

	public void setWorkDate(final int workDate) {
		this.workDate = workDate;
	}

    public String getTimeTypeId() {
        return timeTypeId;
    }

    public void setTimeTypeId(String timeTypeId) {
        this.timeTypeId = timeTypeId;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }
    
}
