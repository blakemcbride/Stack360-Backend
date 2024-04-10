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
package com.arahant.services.standard.misc.message;

import com.arahant.business.BMessage;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoadMessageReturn extends TransmitReturnBase {

	private String message;
	private String messageId;
	private String messageDateTime;
	private String subject;
	private String fromPersonId;
	private String fromDisplayName;
	private String toPersonId; // for Flash
	private String toDisplayName;  // both Flash & HTML
	private Attachment [] attachments;  // for HTML
	private MessagePerson [] toPersons;
	private MessagePerson [] ccPersons;
	private MessagePerson [] bccPersons;
	private boolean sendEmail;
	private boolean sendText;
	private boolean noBody;
	private String mobilePhone;
	private String emailAddress;
	
	public LoadMessageReturn() {
	}

	public LoadMessageReturn(final String string) {
		super(string);
	}

	void setMessageReturn(final BMessage m, boolean includeBcc) {
		messageId = m.getMessageId();
		messageDateTime = DateUtils.getDateTimeFormatted(m.getCreatedDate());
		subject = m.getSubject();
		fromPersonId = m.getFromPersonId(); // sometime null when sent by the system
		fromDisplayName = m.getFromName();
		message = m.getMessage();
		sendEmail = m.getSendEmail() == 'Y';
		sendText = m.getSendText() == 'Y';
		noBody = m.getDontSentBody() == 'Y';
		if (fromPersonId != null) {
			final BPerson bp = new BPerson(fromPersonId);
			mobilePhone = bp.getMobilePhone();
			emailAddress = bp.getPersonalEmail();
		}

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		try {
			List<Record> recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_name, mt.to_address " +
					"from message_to mt " +
					"join person p " +
					"  on mt.to_person_id = p.person_id " +
					"where mt.message_id=? and send_type = 'T'", messageId);
			List<MessagePerson> mpl = new ArrayList<>();
			for (Record rec : recs) {
				MessagePerson mp = new MessagePerson();
				mp.setDisplayName(BMessage.getToName(rec));
				mp.setPersonId(rec.getString("to_person_id"));
				mp.setEmailAddress(rec.getString("to_address"));
				mpl.add(mp);
			}
			toPersons = mpl.toArray(new MessagePerson[mpl.size()]);

			recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_name, mt.to_address " +
					"from message_to mt " +
					"join person p " +
					"  on mt.to_person_id = p.person_id " +
					"where mt.message_id=? and send_type = 'C'", messageId);
			mpl = new ArrayList<>();
			for (Record rec : recs) {
				MessagePerson mp = new MessagePerson();
				mp.setDisplayName(BMessage.getToName(rec));
				mp.setPersonId(rec.getString("to_person_id"));
				mp.setEmailAddress(rec.getString("to_address"));
				mpl.add(mp);
			}
			ccPersons = mpl.toArray(new MessagePerson[mpl.size()]);

			if (includeBcc) {
				recs = db.fetchAll("select mt.to_person_id, p.lname, p.fname, p.mname, mt.to_name, mt.to_address " +
						"from message_to mt " +
						"join person p " +
						"  on mt.to_person_id = p.person_id " +
						"where mt.message_id=? and send_type = 'B'", messageId);
				mpl = new ArrayList<>();
				for (Record rec : recs) {
					MessagePerson mp = new MessagePerson();
					mp.setDisplayName(BMessage.getToName(rec));
					mp.setPersonId(rec.getString("to_person_id"));
					mp.setEmailAddress(rec.getString("to_address"));
					mpl.add(mp);
				}
				bccPersons = mpl.toArray(new MessagePerson[mpl.size()]);
			}

			List<Record> atts = db.fetchAll("select message_attachment_id, source_file_name from message_attachment where message_id=?", messageId);
			attachments = new Attachment[atts.size()];
			for (int i=0 ; i < atts.size() ; i++) {
				Record att = atts.get(i);
				attachments[i] = new Attachment();
				attachments[i].setId(att.getString("message_attachment_id"));
				attachments[i].setName(att.getString("source_file_name"));
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

	public Attachment[] getAttachments() {
		return attachments;
	}

	public void setAttachments(Attachment[] attachments) {
		this.attachments = attachments;
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

	public boolean isNoBody() {
		return noBody;
	}

	public void setNoBody(boolean noBody) {
		this.noBody = noBody;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}

	
