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

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = VendorContact.TABLE_NAME)
public class VendorContact extends Person implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final String TABLE_NAME = "vendor_contact";

	// Fields    
	// Constructors
	/**
	 * default constructor
	 */
	public VendorContact() {
	}

	@Override
	public boolean equals(Object o) {
		if (getPersonId() == null && o == null)
			return true;
		if (getPersonId() != null && o instanceof VendorContact)
			return getPersonId().equals(((VendorContact) o).getPersonId());

		return false;
	}

	@Override
	public int hashCode() {
		if (getPersonId() == null)
			return 0;
		return getPersonId().hashCode();
	}
}
