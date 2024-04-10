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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import java.io.Serializable;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = ProjectForm.TABLE_NAME)
public class ProjectForm extends ArahantBean implements Serializable, Comparable<ProjectForm> {

    public static final String TABLE_NAME = "project_form";
	public static final String FORM_TYPE = "formType";
	private String projectFormId;
	private FormType formType;
	private int formDate;
	private ProjectShift projectShift;
	public static final String COMMENTS = "comments";
	private String comments;
	private String source;
	private String fileNameExtension = "pdf";  //default to pdf for old code
    public static final String INTERNAL = "internal";
    private char internal = 'Y';
	private static final long serialVersionUID = -4439406137841443618L;
	public static final String PROJECTSHIFT = "projectShift";
	public static final String FORM_DATE = "formDate";

	public ProjectForm() {
	}

	@Override
	public String generateId() throws ArahantException {
		projectFormId = IDGenerator.generate(this);
		return projectFormId;
	}

	@Override
	public String keyColumn() {
		return "project_form_id";
	}

	@Override
	public String tableName() {
		return TABLE_NAME;
	}

	@Column(name = "comments")
	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@Column(name = "file_name_extension")
	public String getFileNameExtension() {
		return fileNameExtension;
	}

	public void setFileNameExtension(String fileNameExtension) {
		this.fileNameExtension = fileNameExtension;
	}

	@Column(name = "form_date")
	public int getFormDate() {
		return formDate;
	}

	public void setFormDate(final int formDate) {
		this.formDate = formDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_type_id")
	public FormType getFormType() {
		return formType;
	}

	/**
	 * @param formType The formType to set.
	 */
	public void setFormType(final FormType formType) {
		this.formType = formType;
	}

	/**
	 * @return Returns the projectFormId.
	 */
	@Id
	@Column(name = "project_form_id")
	public String getProjectFormId() {
		return projectFormId;
	}

	/**
	 * @param projectFormId The projectFormId to set.
	 */
	public void setProjectFormId(final String projectFormId) {
		this.projectFormId = projectFormId;
	}

    /**
	 * @return Returns the project.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_shift_id")
	public ProjectShift getProjectShift() {
		return projectShift;
	}

	/**
	 * @param projectShift The project to set.
	 */
	public void setProjectShift(final ProjectShift projectShift) {
		this.projectShift = projectShift;
	}

	/**
	 * @return Returns the source.
	 */
	@Column(name = "source")
	public String getSource() {
		return source;
	}

	/**
	 * @param source The source to set.
	 */
	public void setSource(final String source) {
		this.source = source;
	}

	@Override
	public int compareTo(final ProjectForm o) {
		int ret = formType.getFormCode().compareTo(o.formType.getFormCode());

		if (ret == 0)
			ret = formDate - o.formDate;

		return ret;
	}

    @Column(name = "internal")
    public char getInternal() {
        return this.internal;
    }

    public void setInternal(final char internal) {
        this.internal = internal;
    }

    @Override
	public boolean equals(Object o) {
		if (projectFormId == null && o == null)
			return true;
		if (projectFormId != null && o instanceof ProjectForm)
			return projectFormId.equals(((ProjectForm) o).getProjectFormId());

		return false;
	}

	@Override
	public int hashCode() {
		if (projectFormId == null)
			return 0;
		return projectFormId.hashCode();
	}
}
