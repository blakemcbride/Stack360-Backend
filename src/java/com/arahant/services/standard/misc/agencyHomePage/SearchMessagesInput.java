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
package com.arahant.services.standard.misc.agencyHomePage;
import com.arahant.annotation.Validation;
import com.arahant.business.interfaces.IMessageSearchCriteria;
import com.arahant.services.TransmitInputBase;


/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchMessagesInput extends TransmitInputBase implements IMessageSearchCriteria {

	@Validation (min=6,max=10,type="date",required=false)
	private int fromDateIndicator; // (0=all 1=on 2=before 3=after 4=not equal)
	@Validation (type="date",required=false)
	private int fromDate;
	@Validation (min=6,max=10,type="date",required=false)
	private int toDateIndicator; // (0=all 1=on 2=before 3=after 4=not equal)
	@Validation (type="date",required=false)
	private int toDate;
	@Validation (required=false)
	private String senderId;
	@Validation (required=false)
	private String receiverId;
	@Validation (table="message",column="subject",required=false)
	private String subject; // (like clause)
	
	@Validation (min=2,max=5,required=false)
	private int subjectSearchType;
	
	/**
	 * @return Returns the subjectSearchType.
	 */
	public int getSubjectSearchType() {
		return subjectSearchType;
	}

	/**
	 * @param subjectSearchType The subjectSearchType to set.
	 */
	public void setSubjectSearchType(final int subjectSearchType) {
		this.subjectSearchType = subjectSearchType;
	}

	public SearchMessagesInput() {
		super();
	}

	/**
	 * @return Returns the fromDate.
	 */
	public int getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate The fromDate to set.
	 */
	public void setFromDate(final int fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return Returns the fromDateIndicator.
	 */
	public int getFromDateIndicator() {
		return fromDateIndicator;
	}

	/**
	 * @param fromDateIndicator The fromDateIndicator to set.
	 */
	public void setFromDateIndicator(final int fromDateIndicator) {
		this.fromDateIndicator = fromDateIndicator;
	}

	/**
	 * @return Returns the receiverId.
	 */
	public String getReceiverId() {
		return receiverId;
	}

	/**
	 * @param receiverId The receiverId to set.
	 */
	public void setReceiverId(final String receiverId) {
		this.receiverId = receiverId;
	}

	/**
	 * @return Returns the senderId.
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId The senderId to set.
	 */
	public void setSenderId(final String senderId) {
		this.senderId = senderId;
	}

	/**
	 * @return Returns the subject.
	 */
	public String getSubject() {
		return modifyForSearch(subject, subjectSearchType);
	}

	/**
	 * @param subject The subject to set.
	 */
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	/**
	 * @return Returns the toDate.
	 */
	public int getToDate() {
		return toDate;
	}

	/**
	 * @param toDate The toDate to set.
	 */
	public void setToDate(final int toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return Returns the toDateIndicator.
	 */
	public int getToDateIndicator() {
		return toDateIndicator;
	}

	/**
	 * @param toDateIndicator The toDateIndicator to set.
	 */
	public void setToDateIndicator(final int toDateIndicator) {
		this.toDateIndicator = toDateIndicator;
	}

	
}

	
