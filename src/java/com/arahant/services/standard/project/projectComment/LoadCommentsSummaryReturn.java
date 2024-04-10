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

import com.arahant.business.BProject;
import com.arahant.business.BProjectComment;
import com.arahant.services.TransmitReturnBase;

public class LoadCommentsSummaryReturn extends TransmitReturnBase {

	private LoadCommentsSummaryReturnItem [] item;
	private String detail;
	
	/**
	 * @return Returns the detail.
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail The detail to set.
	 */
	public void setDetail(final String detail) {
		this.detail = detail;
	}

	public LoadCommentsSummaryReturn() {
		super();
	}

	/**
	 * @return Returns the projectComments.
	 */
	public LoadCommentsSummaryReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param projectComments The projectComments to set.
	 */
	public void setItem(final LoadCommentsSummaryReturnItem[] projectComments) {
		this.item = projectComments;
	}

	private void setProjectComments(final BProjectComment[] c) {
		item = new LoadCommentsSummaryReturnItem[c.length];
		for (int loop = 0; loop < c.length; loop++)
			item[loop] = new LoadCommentsSummaryReturnItem(c[loop]);
	}

	void setData(final BProject project, final String shiftId) {
		detail = project.getDetailDesc();
		setProjectComments(project.getProjectComments(shiftId));
	}
}

	
