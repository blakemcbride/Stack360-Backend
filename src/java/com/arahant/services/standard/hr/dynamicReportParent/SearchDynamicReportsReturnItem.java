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
package com.arahant.services.standard.hr.dynamicReportParent;

import com.arahant.beans.ReportSelection;
import com.arahant.business.BReport;


public class SearchDynamicReportsReturnItem {

	private String name;
	private int reportType;
	private String description;
	private String reportId;
	private boolean runtime;

	
	public SearchDynamicReportsReturnItem() {
		
	}

	SearchDynamicReportsReturnItem(BReport br) {
		runtime = false;
		name = br.getReportName();
		reportType = br.getReportType();
		description = br.getDescription();
		reportId = br.getReportId();

		for(ReportSelection rs : br.getReportSelections())
			if(rs.getSelectionType() == 'R') {
				runtime = true;
				break;
			}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public boolean isRuntime() {
		return runtime;
	}

	public void setRuntime(boolean runtime) {
		this.runtime = runtime;
	}
}

	