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
import javax.persistence.*;

@Entity
@Table(name = "time_reject")
public class TimeReject extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "time_reject";
	// Fields    
	private String timeRejectId;
	public static final String TIMEREJECTID = "timeRejectId";
	private Person person;
	public static final String PERSON = "person";
	private int rejectDate;
	public static final String REJECTDATE = "rejectDate";

	// Constructors
	/**
	 * default constructor
	 */
	public TimeReject() {
	}

	/**
	 * full constructor
	 */
	public TimeReject(final String timeRejectId, final Person person, final int rejectDate) {
		this.timeRejectId = timeRejectId;
		this.person = person;
		this.rejectDate = rejectDate;
	}

	// Property accessors
	@Id
	@Column(name = "time_reject_id")
	public String getTimeRejectId() {
		return this.timeRejectId;
	}

	public void setTimeRejectId(final String timeRejectId) {
		this.timeRejectId = timeRejectId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "person_id")
	public Person getPerson() {
		return this.person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}

	@Column(name = "reject_date")
	public int getRejectDate() {
		return this.rejectDate;
	}

	public void setRejectDate(final int rejectDate) {
		this.rejectDate = rejectDate;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {

		return "time_reject_id";
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
		setTimeRejectId(IDGenerator.generate(this));
		return timeRejectId;
	}

	@Override
	public boolean equals(Object o) {
		if (timeRejectId == null && o == null)
			return true;
		if (timeRejectId != null && o instanceof TimeReject)
			return timeRejectId.equals(((TimeReject) o).getTimeRejectId());

		return false;
	}

	@Override
	public int hashCode() {
		if (timeRejectId == null)
			return 0;
		return timeRejectId.hashCode();
	}
}
