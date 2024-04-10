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

package com.arahant.services.standard.project.projectComment;

import com.arahant.business.BProjectComment;
import com.arahant.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadCommentsSummaryReturnItem {

	private String commentTxt;
	private String internalComment;
	private String commentId;
	private String lastName;
	private String firstName;
	private String dateTimeEnteredFormatted;

	public LoadCommentsSummaryReturnItem() {
	}

	LoadCommentsSummaryReturnItem(final BProjectComment c) {
		commentTxt = c.getCommentTxt();
		internalComment = c.getInternal() == 'Y' ? "Yes" : "No";
		commentId = c.getCommentId();
		lastName = c.getLastName();
		firstName = c.getFirstName();
		dateTimeEnteredFormatted = DateUtils.getDateTimeFormatted(c.getDateEntered());
	}

	public String getCommentTxt() {
		return commentTxt;
	}

	public void setCommentTxt(final String commentTxt) {
		this.commentTxt = commentTxt;
	}

	public String getInternalComment() {
		return internalComment;
	}

	public void setInternalComment(final String internal) {
		this.internalComment = internal;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(final String commentId) {
		this.commentId = commentId;
	}

	public String getDateTimeEnteredFormatted() {
		return dateTimeEnteredFormatted;
	}

	public void setDateTimeEnteredFormatted(final String dateEnteredFormatted) {
		this.dateTimeEnteredFormatted = dateEnteredFormatted;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
}

	
