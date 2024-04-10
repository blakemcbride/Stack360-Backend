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
 * Created on Feb 8, 2007
*/

package com.arahant.services.standard.project.projectComment;

import com.arahant.annotation.Validation;
import com.arahant.services.TransmitInputBase;

public class AddProjectCommentInput extends TransmitInputBase {

	@Validation (table="project_comment",column="internal",required=false)
	private boolean internalFlag;
	@Validation (required=true)
	private String projectId;
	@Validation (min=1,max=40000,table="project_comment",column="comment_txt",required=true)
	private String comment;
	private String shiftId;
	
	public AddProjectCommentInput() {
		super();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(final String comment) {
		this.comment = comment;
	}

	public boolean getInternalFlag() {
		return internalFlag;
	}

	public void setInternalFlag(final boolean internalFlag) {
		this.internalFlag = internalFlag;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(final String project_id) {
		this.projectId = project_id;
	}

	public String getShiftId() {
		return shiftId;
	}

	public void setShiftId(String shiftId) {
		this.shiftId = shiftId;
	}
}

	
