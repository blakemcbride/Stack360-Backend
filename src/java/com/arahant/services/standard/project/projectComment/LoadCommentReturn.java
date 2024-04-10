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


package com.arahant.services.standard.project.projectComment;

import com.arahant.business.BProjectComment;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.DateUtils;


public class LoadCommentReturn extends TransmitReturnBase  {

	private String dateTimeEnteredFormatted;
	private String commentTxt;
	private boolean internalComment;
	private String lastName;
	private String firstName;
	
	
	public LoadCommentReturn()
	{
	}

	LoadCommentReturn(final BProjectComment pc) {
		super();
		dateTimeEnteredFormatted=DateUtils.getDateTimeFormatted(pc.getDateEntered());
		setInternalComment(pc.getInternal()=='Y');

		setCommentTxt(pc.getCommentTxt());

		lastName=pc.getLastName();
		firstName=pc.getFirstName();
		
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#getCommentTxt()
	 */
	public String getCommentTxt() {
		return commentTxt;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#setCommentTxt(java.lang.String)
	 */
	public void setCommentTxt(final String commentTxt) {
		this.commentTxt = commentTxt;
	}

	/**
	 * @return Returns the internalComment.
	 */
	public boolean getInternalComment() {
		return internalComment;
	}


	/**
	 * @param internalComment The internalComment to set.
	 */
	public void setInternalComment(final boolean internalComment) {
		this.internalComment = internalComment;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#getFirstName()
	 */
	public String getFirstName() {
		return firstName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#setFirstName(java.lang.String)
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#getLastName()
	 */
	public String getLastName() {
		return lastName;
	}

	/* (non-Javadoc)
	 * @see com.arahant.operations.transmit.IProjectCommentTransmit#setLastName(java.lang.String)
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return Returns the dateTimeEnteredFormatted.
	 */
	public String getDateTimeEnteredFormatted() {
		return dateTimeEnteredFormatted;
	}

	/**
	 * @param dateTimeEnteredFormatted The dateTimeEnteredFormatted to set.
	 */
	public void setDateTimeEnteredFormatted(final String dateTimeEnteredFormatted) {
		this.dateTimeEnteredFormatted = dateTimeEnteredFormatted;
	}

}

	
