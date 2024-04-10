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


/**
 *
 *
 */

package com.arahant.services.standard.webservices.dynamicWebServices;


public class DataObjectOrderedItem {

	private Object item1;
	private Object item2;
	private DataObject dataObject;

	public Object getItem1() {
		return item1;
	}

	public void setItem1(Object item1) {
		this.item1 = item1;
	}

	public Object getItem2() {
		return item2;
	}

	public void setItem2(Object item2) {
		this.item2 = item2;
	}
	
	public DataObject getDataObject() {
		return dataObject;
	}

	public void setDataObject(DataObject dataObject) {
		this.dataObject = dataObject;
	}
}
