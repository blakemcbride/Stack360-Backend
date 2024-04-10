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
package com.arahant.services.standard.project.projectComment;
import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;


/**
 * commentId:String, internalFlag:Boolean, and comment:String
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveProjectCommentInput extends TransmitInputBase {

	@Validation (required=true)
	private String commentId;
	@Validation (required=true)
	private boolean internalFlag;
	@Validation (required=true)
	private String comment;
	
	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment The comment to set.
	 */
	public void setComment(final String comment) {
		this.comment = comment;
	}
	public SaveProjectCommentInput() {
		super();
	}
	/**
	 * @return Returns the commentId.
	 */
	public String getCommentId() {
		return commentId;
	}
	/**
	 * @param commentId The commentId to set.
	 */
	public void setCommentId(final String commentId) {
		this.commentId = commentId;
	}
	/**
	 * @return Returns the internalFlag.
	 */
	public boolean getInternalFlag() {
		return internalFlag;
	}
	/**
	 * @param internalFlag The internalFlag to set.
	 */
	public void setInternalFlag(final boolean internalFlag) {
		this.internalFlag = internalFlag;
	}
}

	
