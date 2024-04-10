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
 * Created on Feb 11, 2007
 */
package com.arahant.services.standard.misc.onboardingMonitoring;

import com.arahant.business.BMessage;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;

public class FoundMessages   {

	private String messageId;
	private String messageDateTime;
	private String subject;
	private String fromPersonId;
	private String fromDisplayName;
	private String toPersonId;
	private String toDisplayName;

	public FoundMessages()
	{
	}

	public FoundMessages(final BMessage m) {
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

	public String getFromDisplayName() {
		return fromDisplayName;
	}

	public void setFromDisplayName(final String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	public String getMessageDateTime() {
		return messageDateTime;
	}

	public void setMessageDateTime(final String messageDateTime) {
		this.messageDateTime = messageDateTime;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(final String messageId) {
		this.messageId = messageId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	public String getToDisplayName() {
		return toDisplayName;
	}

	public void setToDisplayName(final String toDisplayName) {
		this.toDisplayName = toDisplayName;
	}

	public String getFromPersonId() {
		return fromPersonId;
	}

	public void setFromPersonId(final String fromPersonId) {
		this.fromPersonId = fromPersonId;
	}

	public String getToPersonId() {
		return toPersonId;
	}

	public void setToPersonId(final String toPersonId) {
		this.toPersonId = toPersonId;
	}
}

	
