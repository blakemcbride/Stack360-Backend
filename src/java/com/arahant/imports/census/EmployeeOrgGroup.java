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


package com.arahant.imports.census;

public class EmployeeOrgGroup {

	private String id;
	private boolean primary;

	/**
	 * Creates an employee org group representation
	 *
	 * @param id required org group id that must exist already
	 * @param id required status id that must exist already
	 */
	public EmployeeOrgGroup(String id) {
		this(id, false);
	}

	/**
	 * Creates an employee org group representation
	 *
	 * @param id required org group id that must exist already
	 * @param primary required indicator if employee is primary (supervisor) in
	 * this group
	 */
	public EmployeeOrgGroup(String id, boolean primary) {
		this.id = id;
		this.primary = primary;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}
}
