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

public class EmployeeStatus {

	private String id;
	private int effectiveDate;
	private String note;

	/**
	 * Creates an employee status representation
	 *
	 * @param id required status id that must exist already
	 * @param effectiveDate required effective date that must be > 0
	 */
	public EmployeeStatus(String id, int effectiveDate) {
		this(id, effectiveDate, "");
	}

	/**
	 * Creates an employee status representation
	 *
	 * @param id required status id that must exist already
	 * @param effectiveDate required effective date that must be > 0
	 * @param note optional note
	 */
	public EmployeeStatus(String id, int effectiveDate, String note) {
		this.id = id;
		this.effectiveDate = effectiveDate;
		this.note = note;
	}

	public int getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(int effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
