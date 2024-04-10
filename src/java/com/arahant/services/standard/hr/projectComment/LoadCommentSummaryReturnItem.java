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
import com.arahant.utils.DateUtils;


/**
 * 
 *
 *
 */
public class LoadCommentSummaryReturnItem {
	
	public LoadCommentSummaryReturnItem()
	{
		;
	}

	LoadCommentSummaryReturnItem (final BProjectComment bc)
	{
		
		commentId=bc.getCommentId();
		dateTimeEnteredFormatted=DateUtils.getDateTimeFormatted(bc.getDateEntered());
		lastName=bc.getLastName();
		firstName=bc.getFirstName();
		if (bc.getCommentTxt().length()>100)
			commentPreview=bc.getCommentTxt().substring(0,100).replace("\n", " ");
		else
			commentPreview=bc.getCommentTxt().replace("\n", " ");

	}
	
	private String commentId;
	private String dateTimeEnteredFormatted;
	private String lastName;
	private String firstName;
	private String commentPreview;
	


	public String getCommentId()
	{
		return commentId;
	}
	public void setCommentId(final String commentId)
	{
		this.commentId=commentId;
	}
	public String getDateTimeEnteredFormatted()
	{
		return dateTimeEnteredFormatted;
	}
	public void setDateTimeEnteredFormatted(final String dateEnteredFormatted)
	{
		this.dateTimeEnteredFormatted=dateEnteredFormatted;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(final String lastName)
	{
		this.lastName=lastName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(final String firstName)
	{
		this.firstName=firstName;
	}
	public String getCommentPreview()
	{
		return commentPreview;
	}
	public void setCommentPreview(final String commentPreview)
	{
		this.commentPreview=commentPreview;
	}

}

	
