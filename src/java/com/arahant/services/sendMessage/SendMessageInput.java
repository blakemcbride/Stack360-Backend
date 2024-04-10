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
package com.arahant.services.sendMessage;

import com.arahant.exceptions.ArahantException;
import com.arahant.services.TransmitInputBase;
import com.arahant.business.BMessage;
import com.arahant.annotation.Validation;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.IDGenerator;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import java.sql.SQLException;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SendMessageInput extends TransmitInputBase {

	void setData(BMessage bc)
	{
		bc.setFromPersonId(personId);
		bc.setSubject(subject);
		bc.setMessage(message);
		bc.setFromShow('N');

		final Connection db = new Connection(ArahantSession.getHSU().getConnection());
		final Record rec = db.newRecord("message_to");
		rec.set("message_to_id", IDGenerator.generate("message_to", "message_to_id"));
		rec.set("message_id", bc.getMessageId());
		rec.set("to_person_id", personId);
		rec.set("send_type", "T");
		rec.set("to_show", "Y");
		rec.set("sent", "Y");
		try {
			rec.addRecord();
		} catch (SQLException throwables) {
			throw new ArahantException(throwables);
		}
	}
	
	@Validation (required=false)
	private String personId;
	@Validation (required=false)
	private String subject;
	@Validation (required=false)
	private String message;
	

	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}
	public String getSubject()
	{
		return subject;
	}
	public void setSubject(String subject)
	{
		this.subject=subject;
	}
	public String getMessage()
	{
		return message;
	}
	public void setMessage(String message)
	{
		this.message=message;
	}

}

	
