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


package com.arahant.services.standard.hr.birthdayListReport;

import com.arahant.services.TransmitInputBase;
import com.arahant.annotation.Validation;

public class GetReportInput extends TransmitInputBase {
	@Validation (min=1,required=true)
	private String [] statusIds;
	@Validation (min=0,max=11,required=false)
	private int month;
	@Validation (type="date",required=false)
	private int dateFrom;
	@Validation (type="date",required=false)
	private int dateTo;

	public int getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(int dateFrom) {
		this.dateFrom = dateFrom;
	}

	public int getDateTo() {
		return dateTo;
	}

	public void setDateTo(int dateTo) {
		this.dateTo = dateTo;
	}


	public String [] getStatusIds() {
		if (statusIds == null)
			statusIds = new String [0];
		return statusIds;
	}
	public void setStatusIds(String [] statusIds) {
		this.statusIds=statusIds;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month=month;
	}
}

	
