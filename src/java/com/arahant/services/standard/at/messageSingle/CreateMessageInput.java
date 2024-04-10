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
 *
 *  Created on Feb 8, 2007
*/

package com.arahant.services.standard.at.messageSingle;

import com.arahant.business.BMessage;
import com.arahant.services.TransmitInputBase;

import java.util.Date;

public class CreateMessageInput extends TransmitInputBase {

	private String fromPersonId;
	private String toPersonId;
	private String message;
	private String subject;
	private String sendEmail;
	private String sendText;
	private String sendInternal;
	private String dontSendBody;
	private String [] ccPersonIds;
	private String [] bccPersonIds;
	
	public CreateMessageInput() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getToPersonId() {
		return toPersonId;
	}

	public void setToPersonId(final String toPersonId) {
		this.toPersonId = toPersonId;
	}

	public String getFromPersonId() {
		return fromPersonId;
	}

	public void setFromPersonId(String fromPersonId) {
		this.fromPersonId = fromPersonId;
	}

	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getSendText() {
		return sendText;
	}

	public void setSendText(String sendText) {
		this.sendText = sendText;
	}

	public String getSendInternal() {
		return sendInternal;
	}

	public void setSendInternal(String sendInternal) {
		this.sendInternal = sendInternal;
	}

	public String getDontSendBody() {
		return dontSendBody;
	}

	public void setDontSendBody(String dontSendBody) {
		this.dontSendBody = dontSendBody;
	}

	public String[] getCcPersonIds() {
		return ccPersonIds;
	}

	public void setCcPersonIds(String[] ccPersonIds) {
		this.ccPersonIds = ccPersonIds;
	}

	public String[] getBccPersonIds() {
		return bccPersonIds;
	}

	public void setBccPersonIds(String[] bccPersonIds) {
		this.bccPersonIds = bccPersonIds;
	}

	void makeMessage(final BMessage bm) {
		bm.setSubject(subject);
		bm.setMessage(getMessage());
		bm.setCreatedDate(new Date());
		bm.setFromShow('Y');
	}
}

	
