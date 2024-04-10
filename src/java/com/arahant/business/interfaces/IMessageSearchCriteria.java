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
 *  Created on Feb 18, 2007
 */
package com.arahant.business.interfaces;

public interface IMessageSearchCriteria {

	/**
	 * @return Returns the fromDate.
	 */
	int getFromDate();

	/**
	 * @param fromDate The fromDate to set.
	 */
	void setFromDate(int fromDate);

	/**
	 * @return Returns the fromDateIndicator.
	 */
	int getFromDateIndicator();

	/**
	 * @param fromDateIndicator The fromDateIndicator to set.
	 */
	void setFromDateIndicator(int fromDateIndicator);

	/**
	 * @return Returns the receiverId.
	 */
	String getReceiverId();

	/**
	 * @param receiverId The receiverId to set.
	 */
	void setReceiverId(String receiverId);

	/**
	 * @return Returns the senderId.
	 */
	String getSenderId();

	/**
	 * @param senderId The senderId to set.
	 */
	void setSenderId(String senderId);

	/**
	 * @return Returns the subject.
	 */
	String getSubject();

	/**
	 * @param subject The subject to set.
	 */
	void setSubject(String subject);

	/**
	 * @return Returns the toDate.
	 */
	int getToDate();

	/**
	 * @param toDate The toDate to set.
	 */
	void setToDate(int toDate);

	/**
	 * @return Returns the toDateIndicator.
	 */
	int getToDateIndicator();

	/**
	 * @param toDateIndicator The toDateIndicator to set.
	 */
	void setToDateIndicator(int toDateIndicator);

}
