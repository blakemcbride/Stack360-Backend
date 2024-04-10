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
package com.arahant.services.standard.billing.createTimesheetInvoice;

import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;


/**
 * 
 *
 * Created on Jul 25, 2006
 *
 */
public class BillingTimesheet   {
	
	private String state;
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
	private String productProphetId;
	private String timeDescription;
	private String companyId;
	private String timesheetId;
	private String personId;
	private double totalHours;
	
	
	public BillingTimesheet()
	{
		;
	}
	
	/**
	 * @param timesheets
	 */
	public BillingTimesheet(final BTimesheet t) {
		super();
		state=t.getState()+"";
		companyName=t.getCompanyName();
		setBeginTimeFormatted(DateUtils.getTimeFormatted(t.getBeginningTime()));
		setEndTimeFormatted(DateUtils.getTimeFormatted(t.getEndTime()));
		setWorkDateFormatted(DateUtils.getDateFormatted(t.getWorkDate()));
		projectName=t.getProjectName();
		billingRate=t.getBillingRate();
		firstName=t.getFirstName();
		lastName=t.getLastName();
		externalReference=t.getExternalReference();
		projectDescription=t.getProjectDescription();
		acctSystemAccount=t.getAcctSystemAccount();
		acctSystemId=t.getAcctSystemId();
		productProphetId=t.getProductProphetId();
		timeDescription=t.getDescription().replace('\n', ' ');
		companyId=t.getCompanyName();		
		timesheetId=t.getTimesheetId();
		personId=t.getPersonId();
		totalHours=t.getTotalHours();
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
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#getProductProphetId()
	 */
	public String getProductProphetId() {
		return productProphetId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.ITimesheetTransmit#setProductProphetId(java.lang.String)
	 */
	public void setProductProphetId(final String glAcctProphetId) {
		this.productProphetId = glAcctProphetId;
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
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#getPersonId()
	 */
	public String getPersonId() {
		return personId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#setPerson(java.lang.String)
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#getTimesheetId()
	 */
	public String getTimesheetId() {
		return timesheetId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#setTimesheetId(java.lang.String)
	 */
	public void setTimesheetId(final String timesheetId) {
		this.timesheetId = timesheetId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#getTotalHours()
	 */
	public double getTotalHours() {
		return totalHours;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IInTimesheetTransmit#setTotalHours(double)
	 */
	public void setTotalHours(final double totalHours) {
		this.totalHours = totalHours;
	}

	
}

	
