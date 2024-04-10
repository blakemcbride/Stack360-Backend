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
package com.arahant.services.standard.time.timesheetEntrySemiMonthly;

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;

public class TimesheetTransmit {

	private String state;
	private String finalized;
	private String messageId;
	private String companyName;
	private String projectName;
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
	private String personId;
	private String description;
	private double totalHours;
	private int workDate;
	private int endDate;
	private int beginningTime;
	private int endTime;
	private String billable;
	private String startDateFormatted;
	private String endDateFormatted;

	public TimesheetTransmit() {
	}

	/**
	 * @param timesheet
	 */
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
		personId = t.getPersonId();
		description = t.getProjectDescription();
		totalHours = t.getTotalHours();
		workDate = t.getWorkDate();
		endDate = t.getEndDate();
		beginningTime = t.getBeginningTime();
		endTime = t.getEndTime();
		billable = t.getBillable() + "";
		
		startDateFormatted = DateUtils.dateFormat("MM/dd/yy", workDate) + " " + DateUtils.getTimeFormatted(beginningTime);
		endDateFormatted   = DateUtils.dateFormat("MM/dd/yy", endDate) + " " + DateUtils.getTimeFormatted(endTime);
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(final String projectName) {
		this.projectName = projectName;
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

	/**
	 * @return Returns the beginningTime.
	 */
	public int getBeginningTime() {
		return beginningTime;
	}

	/**
	 * @param beginningTime The beginningTime to set.
	 */
	public void setBeginningTime(final int beginningTime) {
		this.beginningTime = beginningTime;
	}

	/**
	 * @return Returns the billable.
	 */
	public String getBillable() {
		return billable;
	}

	/**
	 * @param billable The billable to set.
	 */
	public void setBillable(final String billable) {
		this.billable = billable;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the endTime.
	 */
	public int getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime The endTime to set.
	 */
	public void setEndTime(final int endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return Returns the personId.
	 */
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
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
	 * @return Returns the timesheetId.
	 */
	public String getTimesheetId() {
		return timesheetId;
	}

	/**
	 * @param timesheetId The timesheetId to set.
	 */
	public void setTimesheetId(final String timesheetId) {
		this.timesheetId = timesheetId;
	}

	/**
	 * @return Returns the totalHours.
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/**
	 * @param totalHours The totalHours to set.
	 */
	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * @return Returns the workDate.
	 */
	public int getWorkDate() {
		return workDate;
	}

	/**
	 * @param workDate The workDate to set.
	 */
	public void setWorkDate(final int workDate) {
		this.workDate = workDate;
	}

	public int getEndDate() {
		return endDate;
	}

	public void setEndDate(int endDate) {
		this.endDate = endDate;
	}

	public String getStartDateFormatted() {
		return startDateFormatted;
	}

	public void setStartDateFormatted(String startDateFormatted) {
		this.startDateFormatted = startDateFormatted;
	}

	public String getEndDateFormatted() {
		return endDateFormatted;
	}

	public void setEndDateFormatted(String endDateFormatted) {
		this.endDateFormatted = endDateFormatted;
	}
	
}
