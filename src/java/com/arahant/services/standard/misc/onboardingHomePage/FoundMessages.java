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
 * Created on Feb 11, 2007
 * 
 */
package com.arahant.services.standard.misc.onboardingHomePage;
import com.arahant.business.BMessage;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;
import java.util.List;


/**
 * 
 *
 * Created on Feb 11, 2007
 *
 */
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

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getFromDisplayName()
	 */
	public String getFromDisplayName() {
		return fromDisplayName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setFromDisplayName(java.lang.String)
	 */
	public void setFromDisplayName(final String fromDisplayName) {
		this.fromDisplayName = fromDisplayName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getMessageDateTime()
	 */
	public String getMessageDateTime() {
		return messageDateTime;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setMessageDateTime(java.lang.String)
	 */
	public void setMessageDateTime(final String messageDateTime) {
		this.messageDateTime = messageDateTime;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getMessageId()
	 */
	public String getMessageId() {
		return messageId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setMessageId(java.lang.String)
	 */
	public void setMessageId(final String messageId) {
		this.messageId = messageId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getSubject()
	 */
	public String getSubject() {
		return subject;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setSubject(java.lang.String)
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getToDisplayName()
	 */
	public String getToDisplayName() {
		return toDisplayName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setToDisplayName(java.lang.String)
	 */
	public void setToDisplayName(final String toDisplayName) {
		this.toDisplayName = toDisplayName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getFromPersonId()
	 */
	public String getFromPersonId() {
		return fromPersonId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setFromPersonId(java.lang.String)
	 */
	public void setFromPersonId(final String fromPersonId) {
		this.fromPersonId = fromPersonId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#getToPersonId()
	 */
	public String getToPersonId() {
		return toPersonId;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IMessageSummaryTransmit#setToPersonId(java.lang.String)
	 */
	public void setToPersonId(final String toPersonId) {
		this.toPersonId = toPersonId;
	}
}

	
