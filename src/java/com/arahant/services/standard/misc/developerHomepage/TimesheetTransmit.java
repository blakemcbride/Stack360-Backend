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
 * Created on Jul 25, 2006
 * 
 */
package com.arahant.services.standard.misc.developerHomepage;

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Jul 25, 2006
 *
 */
public class TimesheetTransmit  {
	
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
	
	public TimesheetTransmit()
	{
		;
	}

	/**
	 * @param timesheet
	 */
	TimesheetTransmit(final BTimesheet t) {
		super();
		 state=t.getState()+"";
		 finalized=t.getFinalized();
		 messageId=t.getMessageId();
		 companyName=t.getCompanyName();
		 projectName=t.getProjectName();
		 billingRate=t.getBillingRate();
		 firstName=t.getFirstName();
		 lastName=t.getLastName();
		 externalReference=t.getExternalReference();
		 projectDescription=t.getProjectDescription();
		 acctSystemAccount=t.getAcctSystemAccount();
		 acctSystemId=t.getAcctSystemId();
		 timeDescription=t.getDescription();
		 companyId=t.getCompanyId();
		 message=t.getMessage();
		 timesheetId=t.getTimesheetId();
		 projectId=t.getProjectID();
		 personId=t.getPersonId();
		 description=t.getProjectDescription();
	 totalHours=t.getTotalHours();
		 workDate=t.getWorkDate();
		 beginningTime=t.getBeginningTime();
		 endTime=t.getEndTime();
		 billable=t.getBillable()+"";
		 
		 workDateFormatted=DateUtils.getDateFormatted(workDate);
		 beginTimeFormatted=DateUtils.getTimeFormatted(beginningTime);
		 endTimeFormatted=DateUtils.getTimeFormatted(endTime);
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getState()
	 */
	public String getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setState(java.lang.String)
	 */
	public void setState(final String statusCode) {
		this.state = statusCode;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getFinalized()
	 */
	public String getFinalized() {
		return finalized;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setFinalized(java.lang.String)
	 */
	public void setFinalized(final String finalized) {
		this.finalized = finalized;
	}


	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getMessageId()
	 */
	public String getMessageId() {
		return messageId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setMessageId(java.lang.String)
	 */
	public void setMessageId(final String messageId) {
		this.messageId = messageId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getCompanyName()
	 */
	public String getCompanyName() {
		return companyName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setCompanyName(java.lang.String)
	 */
	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getBeginTimeFormatted()
	 */
	public String getBeginTimeFormatted() {
		return beginTimeFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setBeginTimeFormatted(java.lang.String)
	 */
	public void setBeginTimeFormatted(final String beginTimeFormatted) {
		this.beginTimeFormatted = beginTimeFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getEndTimeFormatted()
	 */
	public String getEndTimeFormatted() {
		return endTimeFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setEndTimeFormatted(java.lang.String)
	 */
	public void setEndTimeFormatted(final String endTimeFormatted) {
		this.endTimeFormatted = endTimeFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getProjectName()
	 */
	public String getProjectName() {
		return projectName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setProjectName(java.lang.String)
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getWorkDateFormatted()
	 */
	public String getWorkDateFormatted() {
		return workDateFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setWorkDateFormatted(java.lang.String)
	 */
	public void setWorkDateFormatted(final String workDateFormatted) {
		this.workDateFormatted = workDateFormatted;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getBillingRate()
	 */
	public double getBillingRate() {
		return billingRate;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setBillingRate(double)
	 */
	public void setBillingRate(final double billingRate) {
		this.billingRate = billingRate;
	}


	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getFirstName()
	 */
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setFirstName(java.lang.String)
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}


	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getLastName()
	 */
	public String getLastName() {
		return lastName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setLastName(java.lang.String)
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getExternalReference()
	 */
	public String getExternalReference() {
		return externalReference;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setExternalReference(java.lang.String)
	 */
	public void setExternalReference(final String externalReference) {
		this.externalReference = externalReference;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getAcctSystemAccount()
	 */
	public String getAcctSystemAccount() {
		return acctSystemAccount;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setAcctSystemAccount(java.lang.String)
	 */
	public void setAcctSystemAccount(final String glAcctId) {
		this.acctSystemAccount = glAcctId;
	}



	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getAcctSystemId()
	 */
	public String getAcctSystemId() {
		return acctSystemId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setAcctSystemId(java.lang.String)
	 */
	public void setAcctSystemId(final String glAcctDefNumber) {
		this.acctSystemId = glAcctDefNumber;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getProjectDescription()
	 */
	public String getProjectDescription() {
		return projectDescription;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setProjectDescription(java.lang.String)
	 */
	public void setProjectDescription(final String projectDescription) {
		this.projectDescription = projectDescription;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getTimeDescription()
	 */
	public String getTimeDescription() {
		return timeDescription;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setTimeDescription(java.lang.String)
	 */
	public void setTimeDescription(final String timeDescription) {
		this.timeDescription = timeDescription;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getCompanyId()
	 */
	public String getCompanyId() {
		return companyId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setCompanyId(java.lang.String)
	 */
	public void setCompanyId(final String companyId) {
		this.companyId = companyId;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getMessage()
	 */
	public String getMessage() {
		return message;
	}
	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setMessage(java.lang.String)
	 */
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

}

	
