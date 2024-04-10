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
package com.arahant.services.standard.misc.agencyHomePage;

import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.standard.misc.message.MessagePerson;
import com.arahant.utils.ArahantSession;
import org.kissweb.DateTime;
import org.kissweb.DateUtils;
import org.kissweb.StringUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageMessage {

	private String messageId;
	private String messageDateTime;
	private String subject;
	private boolean handled;
	private MessagePerson fromPerson;
	private MessagePerson [] toPersons;
	private MessagePerson [] ccPersons;
	private MessagePerson [] bccPersons;
	private boolean sendEmail;
	private boolean sendText;
	private boolean sendInternal;

	public MessageMessage() {
	}

	public MessageMessage(final BMessage m, boolean includeBcc) {
		messageId = m.getMessageId();
		Date dt = m.getCreatedDate();
		messageDateTime = StringUtils.take(DateUtils.dayOfWeekName(dt), 3);
		messageDateTime += " " + DateTime.format(dt);
		subject = m.getSubject();
		MessagePerson mp = new MessagePerson();
		final String fromPersonId = m.getFromPersonId();
		mp.setPersonId(fromPersonId);
		mp.setDisplayName(m.getFromName());
		if (fromPersonId != null)
			mp.setPhoneNumber(new BPerson(fromPersonId).getMobilePhone());
		fromPerson = mp;
		handled = m.getFromShow() == 'N';
		sendEmail = m.getSendEmail() == 'Y';
		sendText = m.getSendText() == 'Y';
		sendInternal = m.getSendInternal() == 'Y';

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		try {
			List<MessagePerson> mpl = new ArrayList<>();
			List<Record> recs = db.fetchAll("select p.person_id, p.lname, p.fname, p.mname, mt.to_show " +
					"from message_to mt " +
					"join person p " +
					"  on mt.to_person_id = p.person_id " +
					"where mt.message_id=? and send_type='T'", messageId);
			for (Record rec : recs) {
				MessagePerson tmp = new MessagePerson();
				tmp.setPersonId(rec.getString("person_id"));
				tmp.setDisplayName(makeName(rec));
				tmp.setHandled(rec.getChar("to_show") == 'N');
				tmp.setPhoneNumber(new BPerson(rec.getString("person_id")).getMobilePhone());
				mpl.add(tmp);
			}
			toPersons = mpl.toArray(new MessagePerson[mpl.size()]);

			mpl = new ArrayList<>();
			recs = db.fetchAll("select p.person_id, p.lname, p.fname, p.mname, mt.to_show " +
					"from message_to mt " +
					"join person p " +
					"  on mt.to_person_id = p.person_id " +
					"where mt.message_id=? and send_type='C'", messageId);
			for (Record rec : recs) {
				MessagePerson tmp = new MessagePerson();
				tmp.setPersonId(rec.getString("person_id"));
				tmp.setDisplayName(makeName(rec));
				tmp.setHandled(rec.getChar("to_show") == 'N');
				tmp.setPhoneNumber(new BPerson(rec.getString("person_id")).getMobilePhone());
				mpl.add(tmp);
			}
			ccPersons = mpl.toArray(new MessagePerson[mpl.size()]);

			if (includeBcc) {
				mpl = new ArrayList<>();
				recs = db.fetchAll("select p.person_id, p.lname, p.fname, p.mname, mt.to_show " +
						"from message_to mt " +
						"join person p " +
						"  on mt.to_person_id = p.person_id " +
						"where mt.message_id=? and send_type='B'", messageId);
				for (Record rec : recs) {
					MessagePerson tmp = new MessagePerson();
					tmp.setPersonId(rec.getString("person_id"));
					tmp.setDisplayName(makeName(rec));
					tmp.setHandled(rec.getChar("to_show") == 'N');
					tmp.setPhoneNumber(new BPerson(rec.getString("person_id")).getMobilePhone());
					mpl.add(tmp);
				}
				bccPersons = mpl.toArray(new MessagePerson[mpl.size()]);
			}
		} catch (Exception throwables) {
			throw new ArahantException(throwables);
		}
	}

	private static String makeName(Record r) {
		try {
			String name = r.getString("fname");
			if (name == null)
				name = "";
			String lname = r.getString("lname");
			if (lname != null  && !lname.isEmpty())
				name += " " + lname;
			return name;
		} catch (SQLException e) {
			throw new ArahantException(e);
		}
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

	public MessagePerson getFromPerson() {
		return fromPerson;
	}

	public void setFromPerson(MessagePerson fromPerson) {
		this.fromPerson = fromPerson;
	}

	public MessagePerson[] getToPersons() {
		return toPersons;
	}

	public void setToPersons(MessagePerson[] toPersons) {
		this.toPersons = toPersons;
	}

	public MessagePerson[] getCcPersons() {
		return ccPersons;
	}

	public void setCcPersons(MessagePerson[] ccPersons) {
		this.ccPersons = ccPersons;
	}

	public MessagePerson[] getBccPersons() {
		return bccPersons;
	}

	public void setBccPersons(MessagePerson[] bccPersons) {
		this.bccPersons = bccPersons;
	}

	public boolean isHandled() {
		return handled;
	}

	public void setHandled(boolean handled) {
		this.handled = handled;
	}

	public boolean isSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	public boolean isSendText() {
		return sendText;
	}

	public void setSendText(boolean sendText) {
		this.sendText = sendText;
	}

	public boolean isSendInternal() {
		return sendInternal;
	}

	public void setSendInternal(boolean sendInternal) {
		this.sendInternal = sendInternal;
	}
}

	
