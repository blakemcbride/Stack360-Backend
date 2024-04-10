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
public class ColumnDisplayObject {

	private Object display;
	private Object breakCompareItem;

	public ColumnDisplayObject() {
	}

	public ColumnDisplayObject(Object display, String breakCompareItem) {
		this.display = display;
		this.breakCompareItem = breakCompareItem;
	}

	public Object getBreakCompareItem() {
		return breakCompareItem;
	}

	public void setBreakCompareItem(Object breakCompareItem) {
		this.breakCompareItem = breakCompareItem;
	}

	public Object getDisplay() {
		return display;
	}

	public void setDisplay(Object display) {
		this.display = display;
	}
}
