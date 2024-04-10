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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class LoginLogId implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String TABLE_NAME = "login_log";

	// Fields    

	private Date ltime;

	public static final String LTIME = "ltime";

	private String logName;

	public static final String LOGNAME = "logName";

	// Constructors

	/** default constructor */
	public LoginLogId() {
	}

	/** full constructor */
	public LoginLogId(final Date ltime, final String logName) {
		this.ltime = ltime;
		this.logName = logName;
	}

	// Property accessors
	@Column(name="ltime")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLtime() {
		return this.ltime;
	}

	public void setLtime(final Date ltime) {
		this.ltime = ltime;
	}

	@Column(name="log_name")
	public String getLogName() {
		return this.logName;
	}

	public void setLogName(final String logName) {
		this.logName = logName;
	}

	@Override
	public boolean equals(final Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof LoginLogId))
			return false;
		final LoginLogId castOther = (LoginLogId) other;

		return ((this.getLtime() == castOther.getLtime()) || (this.getLtime() != null
				&& castOther.getLtime() != null && this.getLtime().equals(
				castOther.getLtime())))
				&& ((this.getLogName() == castOther.getLogName()) || (this
						.getLogName() != null
						&& castOther.getLogName() != null && this.getLogName()
						.equals(castOther.getLogName())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getLtime() == null ? 0 : this.getLtime().hashCode());
		result = 37 * result
				+ (getLogName() == null ? 0 : this.getLogName().hashCode());
		return result;
	}

}
