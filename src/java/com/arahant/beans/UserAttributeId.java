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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserAttributeId implements Serializable {

	private static final long serialVersionUID = 9164881503013191969L;
	private String personId = "";
	private String userAttribute = "";

	public UserAttributeId() {
	}

	/**
	 * @return Returns the personId.
	 */
	@Column(name = "person_id")
	public String getPersonId() {
		return personId;
	}

	/**
	 * @param personId The personId to set.
	 */
	public void setPersonId(final String personId) {
		this.personId = personId;
	}

	/**
	 * @return Returns the userAttribute.
	 */
	@Column(name = "user_attribute")
	public String getUserAttribute() {
		return userAttribute;
	}

	/**
	 * @param userAttribute The userAttribute to set.
	 */
	public void setUserAttribute(final String userAttribute) {
		this.userAttribute = userAttribute;
	}

	/*
	 * (non-Javadoc) @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		try {

			final UserAttributeId id = (UserAttributeId) obj;

			return id.personId.equals(personId) && id.userAttribute.equals(userAttribute);
		} catch (final Exception e) {
			return false;
		}

	}

	/*
	 * (non-Javadoc) @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		return (personId + userAttribute).hashCode();
	}
}
