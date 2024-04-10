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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = Agent.TABLE_NAME)
@PrimaryKeyJoinColumn(name = "agent_id")
public class Agent extends Person implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String TABLE_NAME = "agent";
	public static final String EXT_REF = "extRef";

	// Fields
	// Constructors
	/**
	 * default constructor
	 */
	public Agent() {
	}

	// Property accessors
	@Override
	public boolean equals(Object o) {
		if (getPersonId() == null && o == null)
			return true;
		if (getPersonId() != null && o instanceof Agent)
			return getPersonId().equals(((Agent) o).getPersonId());
		return false;
	}
	private String extRef;

	@Column(name = "ext_ref")
	public String getExtRef() {
		return extRef;
	}

	public void setExtRef(String extRef) {
		this.extRef = extRef;
	}

	@Override
	public int hashCode() {
		if (getPersonId() == null)
			return 0;

		return getPersonId().hashCode();
	}
}
