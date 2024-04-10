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
import com.arahant.annotation.Validation;

import com.arahant.business.BProjectComment;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewCommentInput extends TransmitInputBase {

	void setData(final BProjectComment bc)
	{
		
		bc.setCommentTxt(comment);
		bc.setProjectId(projectId);
	}
	
	@Validation (min=0,max=40000,table="project_comment",column="comment_txt",required=true)
	private String comment;
	@Validation (required=false)
	private String projectId;

	/**
	 * @return Returns the projectId.
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId The projectId to set.
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(final String comment)
	{
		this.comment=comment;
	}

}

	
