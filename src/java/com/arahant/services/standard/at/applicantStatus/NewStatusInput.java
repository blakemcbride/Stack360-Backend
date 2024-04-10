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
package com.arahant.services.standard.at.applicantStatus;

import com.arahant.services.TransmitInputBase;
import com.arahant.business.BApplicantStatus;
import com.arahant.annotation.Validation;

  
/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewStatusInput extends TransmitInputBase {

	void setData(BApplicantStatus bc)
	{
		
		bc.setName(name);
		bc.setConsiderForHire(considerForHire);
		bc.setAddAfterId(addAfterId);
		bc.setInactiveDate(inactiveDate);
		bc.setSendEmail(sendEmail?'Y':'N');
		bc.setEmailSource(emailAddress);
		bc.setEmailText(emailBody);
		bc.setEmailSubject(emailSubject);

	}
	
	@Validation (table="applicant_status",column="name",required=true)
	private String name;
	@Validation (required=true)
	private boolean considerForHire;
	@Validation (min=0,max=16,required=false)
	private String addAfterId;
	@Validation (min=19000000,max=30000000,required=false)
	private int inactiveDate;
	@Validation (table="applicant_status", column="send_email", required=false)
	private boolean sendEmail;
	@Validation (table="applicant_status", column="email_text", required=false)
	private String emailBody;
	@Validation (table="applicant_status", column="email_source", required=false)
	private String emailAddress;
	@Validation (table="applicant_status", column="email_subject", required=false)
	private String emailSubject;
	

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public boolean getConsiderForHire()
	{
		return considerForHire;
	}
	public void setConsiderForHire(boolean considerForHire)
	{
		this.considerForHire=considerForHire;
	}
	public String getAddAfterId()
	{
		return addAfterId;
	}
	public void setAddAfterId(String addAfterId)
	{
		this.addAfterId=addAfterId;
	}
	public int getInactiveDate()
	{
		return inactiveDate;
	}
	public void setInactiveDate(int inactiveDate)
	{
		this.inactiveDate=inactiveDate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public boolean getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}
}

	
