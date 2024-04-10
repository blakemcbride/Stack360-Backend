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

package com.arahant.services.standard.hr.billingRate;

import com.arahant.beans.EmployeeRate;
import com.arahant.business.BEmployee;
import com.arahant.services.TransmitReturnBase;

import java.util.List;

public class LoadEmployeeRatesReturn extends TransmitReturnBase {

    private LoadEmployeeRatesReturnItem[] item;

	public LoadEmployeeRatesReturn() {
	}

	public LoadEmployeeRatesReturnItem[] getItem() {
		return item;
	}

	public void setItem(final LoadEmployeeRatesReturnItem[] item) {
		this.item = item;
	}

	void setItem(BEmployee be, List<EmployeeRate> n) {
		int loop = 0;
		item = new LoadEmployeeRatesReturnItem[n.size()];
		for (EmployeeRate r : n)
			item[loop++] = new LoadEmployeeRatesReturnItem(r);
	}
}

	
