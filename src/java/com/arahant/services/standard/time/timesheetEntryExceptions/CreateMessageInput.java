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
 * Created on Feb 8, 2007
 * 
 */
package com.arahant.services.standard.time.timesheetEntryExceptions;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class CreateMessageInput extends TransmitInputBase {


	@Validation (required=false)
	private String toPersonId[];
	@Validation (table="message",column="message",required=true)
	private String message;
	@Validation (table="message",column="subject",required=true)
	private String subject;
	
	public CreateMessageInput() {
		super();
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
	public String []getToPersonId() {
            if (toPersonId==null)
                return new String[0];
		return toPersonId;
	}

	/**
	 * @param toPersonId The toPersonId to set.
	 */
	public void setToPersonId(final String []toPersonId) {
		this.toPersonId = toPersonId;
	}
}

	
