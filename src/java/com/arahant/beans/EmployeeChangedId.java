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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.beans;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 */
@Embeddable
public class EmployeeChangedId implements java.io.Serializable {
	private static final long serialVersionUID = -7402761769434443085L;

	private String personId;
	private short interfaceId;

	@Column(name="interface_id")
	public short getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(short interfaceId) {
		this.interfaceId = interfaceId;
	}

	@Column(name="person_id")
	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final EmployeeChangedId other = (EmployeeChangedId) obj;
		if (this.personId != other.personId && (this.personId == null || !this.personId.equals(other.personId))) {
			return false;
		}
		if (this.interfaceId != other.interfaceId) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + (this.personId != null ? this.personId.hashCode() : 0);
		hash = 37 * hash + this.interfaceId;
		return hash;
	}
	
	
}
