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
package com.arahant.services.standard.misc.onboardingHomePage;

import com.arahant.business.BMessage;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;

public class LoadMessageReturn extends TransmitReturnBase {

	private String message;
	private String messageId;
	private String messageDateTime;
	private String subject;
	private String fromPersonId;
	private String fromDisplayName;
	private String toPersonId;
	private String toDisplayName;
	
	public LoadMessageReturn() {
	}

	public LoadMessageReturn(final String string) {
		super(string);
	}

	LoadMessageReturn(final BMessage m) {
		message = m.getMessage();
		messageId = m.getMessageId();
		messageDateTime = DateUtils.getDateTimeFormatted(m.getCreatedDate());
		subject = m.getSubject();
		fromPersonId = m.getFromPersonId();
		fromDisplayName = m.getFromName();

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		try {
			List<Record> recs = db.fetchAll("select p.person_id, p.lname, p.fname, p.mname from message_to to " +
					"join person p " +
					"  to.to_person_id = p.person_id " +
					"where to.message_id=?", messageId);
			toPersonId = recs.size() == 1 ? recs.get(0).getString("person_id") : "";
			toDisplayName = "";
			for (Record rec : recs) {
				if (!toDisplayName.isEmpty())
					toDisplayName += ", ";
				toDisplayName += rec.getString("fname");
				String mname = rec.getString("mname");
				if (mname != null  &&  !mname.isEmpty())
					toDisplayName += " " + mname;
				toDisplayName += rec.getString("lname");
			}
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	/**
	 * @return Returns the fromDisplayName.
	 */
	public String getFromDisplayName() {
		return fromDisplayName;
	}

	/**
	 * @param fromDisplayName The fromDisplayName to set.
	 */
	public void setFromDisplayName(final String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	/**
	 * @return Returns the fromPersonId.
	 */
	public String getFromPersonId() {
		return fromPersonId;
	}

	/**
	 * @param fromPersonId The fromPersonId to set.
	 */
	public void setFromPersonId(final String fromPersonId) {
		this.fromPersonId = fromPersonId;
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
	 * @return Returns the messageDateTime.
	 */
	public String getMessageDateTime() {
		return messageDateTime;
	}

	/**
	 * @param messageDateTime The messageDateTime to set.
	 */
	public void setMessageDateTime(final String messageDateTime) {
		this.messageDateTime = messageDateTime;
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
	 * @return Returns the toDisplayName.
	 */
	public String getToDisplayName() {
		return toDisplayName;
	}

	/**
	 * @param toDisplayName The toDisplayName to set.
	 */
	public void setToDisplayName(final String toDisplayName) {
		this.toDisplayName = toDisplayName;
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
}

	
