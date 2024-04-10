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
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = ProjectComment.TABLE_NAME)
public class ProjectComment extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "project_comment";
	// Fields    
	private String commentId;
	public static final String COMMENTID = "commentId";
	private Person person;
	public static final String PERSON = "person";
	private Date dateEntered;
	public static final String DATEENTERED = "dateEntered";
	private String commentTxt;
	public static final String COMMENTTXT = "commentTxt";
	private char internal = 'Y';
	public static final String INTERNAL = "internal";
	public static final String PROJECTSHIFT = "projectShift";
	private ProjectShift projectShift;

	public ProjectComment() {
	}

	@Id
	@Column(name = "comment_id")
	public String getCommentId() {
		return this.commentId;
	}

	public void setCommentId(final String commentId) {
		this.commentId = commentId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_shift_id")
	public ProjectShift getProjectShift() {
		return projectShift;
	}

	public void setProjectShift(final ProjectShift projectShift) {
		this.projectShift = projectShift;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "date_entered")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateEntered() {
		return this.dateEntered;
	}

	public void setDateEntered(final Date dateEntered) {
		this.dateEntered = dateEntered;
	}

	@Column(name = "comment_txt")
	public String getCommentTxt() {
		return this.commentTxt;
	}

	public void setCommentTxt(final String commentTxt) {
		this.commentTxt = commentTxt;
	}

	@Column(name = "internal")
	public char getInternal() {
		return this.internal;
	}

	public void setInternal(final char internal) {
		this.internal = internal;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return "comment_id";
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */
	@Override
	public String tableName() {
		return TABLE_NAME;
	}


	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		setCommentId(IDGenerator.generate(this));
		return commentId;
	}

	@Override
	public boolean equals(Object o) {
		if (commentId == null && o == null)
			return true;
		if (commentId != null && o instanceof ProjectComment)
			return commentId.equals(((ProjectComment) o).getCommentId());

		return false;
	}

	@Override
	public int hashCode() {
		if (commentId == null)
			return 0;
		return commentId.hashCode();
	}
}
