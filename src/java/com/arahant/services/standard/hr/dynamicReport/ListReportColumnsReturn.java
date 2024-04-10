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
package com.arahant.services.standard.hr.dynamicReport;

import com.arahant.beans.ReportColumn;
import com.arahant.business.BReport;
import com.arahant.dynamic.reports.DynamicReportBase;
import com.arahant.dynamic.reports.DynamicReportColumn;
import com.arahant.services.TransmitReturnBase;
import java.util.List;


public class ListReportColumnsReturn extends TransmitReturnBase {

	SaveDynamicReportColumn[] columns;
	SaveDynamicReportColumn[] availableColumns;


	void setData(BReport bReport) {
		
		setColumns(bReport.getReportColumns());
		setAvailableColumns(DynamicReportBase.getAvailableColumns(bReport.getBean()));
	}

	public void setColumns(List<ReportColumn> columns) {
		SaveDynamicReportColumn[] arr = new SaveDynamicReportColumn[columns.size()];
		for(int loop = 0; loop < columns.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportColumn(columns.get(loop));
		}
		this.columns = arr;
	}

	public void setAvailableColumns(List<DynamicReportColumn> columns) {
		SaveDynamicReportColumn[] arr = new SaveDynamicReportColumn[columns.size()];
		for(int loop = 0; loop < columns.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportColumn(columns.get(loop));
		}
		this.availableColumns = arr;
	}

	public SaveDynamicReportColumn[] getColumns() {
		return columns;
	}

	public void setColumns(SaveDynamicReportColumn[] columns) {
		this.columns = columns;
	}

	public SaveDynamicReportColumn[] getAvailableColumns() {
		return availableColumns;
	}

	public void setAvailableColumns(SaveDynamicReportColumn[] availableColumns) {
		this.availableColumns = availableColumns;
	}

}

	
