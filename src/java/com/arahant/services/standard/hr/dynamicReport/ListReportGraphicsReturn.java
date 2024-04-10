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

import com.arahant.beans.ReportGraphic;
import com.arahant.business.BReport;
import com.arahant.services.TransmitReturnBase;
import java.util.List;


public class ListReportGraphicsReturn extends TransmitReturnBase {

	SaveDynamicReportGraphic graphic[];

	void setData(BReport bReport) {
		setGraphic(bReport.getReportGraphics());
	}

	public SaveDynamicReportGraphic[] getGraphic() {
		return graphic;
	}

	public void setGraphic(SaveDynamicReportGraphic[] graphic) {
		this.graphic = graphic;
	}

	public void setGraphic(List<ReportGraphic> graphics) {
		SaveDynamicReportGraphic[] arr = new SaveDynamicReportGraphic[graphics.size()];
		for(int loop = 0; loop < graphics.size() ; loop++)
		{
			arr[loop] = new SaveDynamicReportGraphic(graphics.get(loop));
		}
		this.graphic = arr;
	}
}

	
