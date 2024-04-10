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
package com.arahant.services.standard.hr.projectComment;

import com.arahant.business.BProjectComment;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;

 
/**
 * 
 *
 *
 */
public class LoadCommentReturn extends TransmitReturnBase {

	void setData(final BProjectComment bc)
	{
		bc.getDateEntered();
		dateTimeEnteredFormatted=DateUtils.getDateTimeFormatted(bc.getDateEntered());
		comment=bc.getCommentTxt();
		firstName=bc.getFirstName();
		lastName=bc.getLastName();
	}
	
	private String dateTimeEnteredFormatted;

	private String firstName,lastName;
	private String comment;

	public String getDateTimeEnteredFormatted()
	{
		return dateTimeEnteredFormatted;
	}
	public void setDateTimeEnteredFormatted(final String dateEnteredFormatted)
	{
		this.dateTimeEnteredFormatted=dateEnteredFormatted;
	}

	public String getComment()
	{
		return comment;
	}
	public void setComment(final String comment)
	{
		this.comment=comment;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

}

	
