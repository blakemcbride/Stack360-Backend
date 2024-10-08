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
 */
package com.arahant.services.standard.hr.employeeListReport;

import com.arahant.business.BProperty;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class ListFieldsReturn extends TransmitReturnBase {

	private ListFieldsReturnItem []item;

	private boolean canAccess="BDRJDBK".equals(BProperty.get("Export System"));

	public ListFieldsReturnItem[] getItem() {
		return item;
	}

	public void setItem(ListFieldsReturnItem[] item) {
		this.item = item;
	}

	public boolean getCanAccess() {
		return canAccess;
	}

	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}

}

	
