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
 * Created on Feb 8, 2007
 */
package com.arahant.services.standard.misc.employeeHomePage;

import com.arahant.annotation.Validation;
import com.arahant.business.BMessage;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.Date;

public class CreateMessageInput extends TransmitInputBase {

	@Validation (required=true)
	private String toPersonId;
	@Validation (max=4000,table="message",column="message",required=false)
	private String message;
	@Validation (table="message",column="subject",required=true)
	private String subject;
	
	public CreateMessageInput() {
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
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the toPersonId.
	 */
	public String getToPersonId() {
		return toPersonId;
	}

	/**
	 * @param toPersonId The toPersonId to set.
	 */
	public void setToPersonId(final String toPersonId) {
		this.toPersonId = toPersonId;
	}

	/**
	 * @param bm
	 */
	void makeMessage(final BMessage bm) {
		bm.setSubject(subject);
		bm.setMessage(getMessage());
		bm.setCreatedDate(new Date());
		bm.setFromShow('Y');

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		final Record rec = db.newRecord("message_to");
		rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
		rec.set("message_id", bm.getMessageId());
		rec.set("to_person_id", getToPersonId());
		rec.set("send_type", "T");
		rec.set("to_show", "Y");
		rec.set("sent", "Y");
		try {
			rec.addRecord();
		} catch (SQLException throwables) {
			throw new ArahantException(throwables);
		}
	}
}

	
