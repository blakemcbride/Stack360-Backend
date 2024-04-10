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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.reports;

import com.arahant.business.BProject;
import com.arahant.business.BProjectChecklistDetail;
import com.arahant.business.BRouteStop;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class ProjectCheckListReport extends ReportBase {

	public ProjectCheckListReport() {
		super("CheckRpt", "Check List Report");
	}

	public String build(String projectId, String routeStopId) throws DocumentException {
		 try {

            PdfPTable table;

			BProject proj=new BProject(projectId);
			writeHeaderLine("Project", proj.getName().trim());
			writeHeaderLine("Summary", proj.getSummary());
			writeHeaderLine("Route Stop", new BRouteStop(routeStopId).getRouteStopNameFormatted());
					
            addHeaderLine();

			BProjectChecklistDetail [] det=BProjectChecklistDetail.list(projectId,routeStopId);
			
            table = makeTable(new int[]{25,15,20,33,7});

            writeColHeader(table, "Description");
            writeColHeader(table, "Completed");
			writeColHeader(table, "Completed By");
			writeColHeader(table, "Comments");
            writeColHeader(table, "Req.");
          
            boolean alternateRow = true;

            for (BProjectChecklistDetail d : det) {
  

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, d.getDescription(), alternateRow);
                write(table, DateUtils.getDateFormatted(d.getCompletedDate()), alternateRow);
                write(table, d.getCompletedName(), alternateRow);
				write(table, d.getComments(), alternateRow);
				write(table, d.getRequired(), alternateRow);

            }

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
	}

}
