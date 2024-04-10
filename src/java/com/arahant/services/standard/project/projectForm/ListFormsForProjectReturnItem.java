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


package com.arahant.services.standard.project.projectForm;

import com.arahant.business.BProjectForm;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import org.kissweb.database.Record;

import java.sql.SQLException;


public class ListFormsForProjectReturnItem {

    private String id, formCode, dateFormatted, comments, formDescription, formid;
    private String extension;
    private String internal;

    public ListFormsForProjectReturnItem()
	{
	}

	ListFormsForProjectReturnItem (final BProjectForm bc) {
        id = bc.getId();
        formCode = bc.getFormCode();
        dateFormatted = bc.getDateFormatted();
        comments = bc.getComments();
        formDescription = bc.getFormDescription();
        extension = bc.getExtension();
        internal = bc.getInternal() == 'Y' ? "Yes" : "No";
        formid = id.substring(10);
    }
	ListFormsForProjectReturnItem (final Record rec) {
		try {
			id = rec.getString("project_form_id");
			formCode = rec.getString("form_code");
			dateFormatted = DateUtils.getDateFormatted(rec.getInt("form_date"));
			comments = rec.getString("comments");
			formDescription = rec.getString("description");
			extension = rec.getString("file_name_extension");
			internal = "Y".equals(rec.getString("internal")) ? "Yes" : "No";
			formid = id.substring(10);
		} catch (SQLException throwables) {
			throw new ArahantException(throwables);
		}
	}


	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(final String comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns the dateFormatted.
	 */
	public String getDateFormatted() {
		return dateFormatted;
	}

	/**
	 * @param dateFormatted The dateFormatted to set.
	 */
	public void setDateFormatted(final String dateFormatted) {
		this.dateFormatted = dateFormatted;
	}

	/**
	 * @return Returns the formCode.
	 */
	public String getFormCode() {
		return formCode;
	}

	/**
	 * @param formCode The formCode to set.
	 */
	public void setFormCode(final String formCode) {
		this.formCode = formCode;
	}

	/**
	 * @return Returns the id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * @return Returns the formDescription.
	 */
	public String getFormDescription() {
		return formDescription;
	}

	/**
	 * @param formDescription The formDescription to set.
	 */
	public void setFormDescription(final String formDescription) {
		this.formDescription = formDescription;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

    public String getInternal() {
        return internal;
    }

    public void setInternal(String internal) {
        this.internal = internal;
    }

	public String getFormid() {
		return formid;
	}

	public void setFormid(String formid) {
		this.formid = formid;
	}
}

	
