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
package com.arahant.services.standard.time.timesheetReview;

import com.arahant.business.BTimesheet;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;

public class LoadTimeEntryReturn extends TransmitReturnBase {

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
	private String personId;
	private String description;
	private double totalHours;
	private int workDate;
	private int beginningTime;
	private int endTime;
	private String billable;

	public LoadTimeEntryReturn() {
	}

	/**
	 * @return Returns the acctSystemAccount.
	 */
	public String getAcctSystemAccount() {
		return acctSystemAccount;
	}

	/**
	 * @param acctSystemAccount The acctSystemAccount to set.
	 */
	public void setAcctSystemAccount(final String acctSystemAccount) {
		this.acctSystemAccount = acctSystemAccount;
	}

	/**
	 * @return Returns the acctSystemId.
	 */
	public String getAcctSystemId() {
		return acctSystemId;
	}

	/**
	 * @param acctSystemId The acctSystemId to set.
	 */
	public void setAcctSystemId(final String acctSystemId) {
		this.acctSystemId = acctSystemId;
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
	 * @return Returns the beginTimeFormatted.
	 */
	public String getBeginTimeFormatted() {
		return beginTimeFormatted;
	}

	/**
	 * @param beginTimeFormatted The beginTimeFormatted to set.
	 */
	public void setBeginTimeFormatted(final String beginTimeFormatted) {
		this.beginTimeFormatted = beginTimeFormatted;
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
	 * @return Returns the billingRate.
	 */
	public double getBillingRate() {
		return billingRate;
	}

	/**
	 * @param billingRate The billingRate to set.
	 */
	public void setBillingRate(final double billingRate) {
		this.billingRate = billingRate;
	}

	/**
	 * @return Returns the companyId.
	 */
	public String getCompanyId() {
		return companyId;
	}

	/**
	 * @param companyId The companyId to set.
	 */
	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return Returns the companyName.
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName The companyName to set.
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
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
	 * @return Returns the endTimeFormatted.
	 */
	public String getEndTimeFormatted() {
		return endTimeFormatted;
	}

	/**
	 * @param endTimeFormatted The endTimeFormatted to set.
	 */
	public void setEndTimeFormatted(final String endTimeFormatted) {
		this.endTimeFormatted = endTimeFormatted;
	}

	/**
	 * @return Returns the externalReference.
	 */
	public String getExternalReference() {
		return externalReference;
	}

	/**
	 * @param externalReference The externalReference to set.
	 */
	public void setExternalReference(final String externalReference) {
		this.externalReference = externalReference;
	}

	/**
	 * @return Returns the finalized.
	 */
	public String getFinalized() {
		return finalized;
	}

	/**
	 * @param finalized The finalized to set.
	 */
	public void setFinalized(final String finalized) {
		this.finalized = finalized;
	}

	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(final String message) {
		this.message = message;
	}

	/**
	 * @return Returns the messageId.
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId The messageId to set.
	 */
	public void setMessageId(final String messageId) {
		this.messageId = messageId;
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
	 * @return Returns the projectDescription.
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/**
	 * @param projectDescription The projectDescription to set.
	 */
	public void setProjectDescription(final String projectDescription) {
		this.projectDescription = projectDescription;
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
	 * @return Returns the projectName.
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName The projectName to set.
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state The state to set.
	 */
	public void setState(final String state) {
		this.state = state;
	}

	/**
	 * @return Returns the timeDescription.
	 */
	public String getTimeDescription() {
		return timeDescription;
	}

	/**
	 * @param timeDescription The timeDescription to set.
	 */
	public void setTimeDescription(final String timeDescription) {
		this.timeDescription = timeDescription;
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

	/**
	 * @return Returns the workDateFormatted.
	 */
	public String getWorkDateFormatted() {
		return workDateFormatted;
	}

	/**
	 * @param workDateFormatted The workDateFormatted to set.
	 */
	public void setWorkDateFormatted(final String workDateFormatted) {
		this.workDateFormatted = workDateFormatted;
	}

	/**
	 * @param t
	 */
	void setData(final BTimesheet t) {
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
		beginningTime = t.getBeginningTime();
		endTime = t.getEndTime();
		billable = t.getBillable() + "";

		workDateFormatted = DateUtils.getDateFormatted(workDate);
		beginTimeFormatted = DateUtils.getTimeFormatted(beginningTime);
		endTimeFormatted = DateUtils.getTimeFormatted(endTime);
	}
}
