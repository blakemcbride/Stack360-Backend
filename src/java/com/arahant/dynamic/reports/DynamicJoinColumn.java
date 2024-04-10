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


package com.arahant.dynamic.reports;

/**
 *
 * Arahant
 */
public class DynamicJoinColumn {

	private String joinColumn;
	private Class joinClass;
	private int index;

	public DynamicJoinColumn() {
	}

	public DynamicJoinColumn(int index, String joinColumn, Class joinClass) {
		this.index = index;
		this.joinColumn = joinColumn;
		this.joinClass = joinClass;
	}

	public Class getJoinClass() {
		return joinClass;
	}

	public void setJoinClass(Class joinClass) {
		this.joinClass = joinClass;
	}

	public String getJoinColumn() {
		return joinColumn;
	}

	public void setJoinColumn(String joinColumn) {
		this.joinColumn = joinColumn;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
