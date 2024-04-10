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

import com.arahant.beans.Person;
import com.arahant.beans.TimeOffRequest;
import com.arahant.business.BPerson;
import com.arahant.business.BTimeOffRequest;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class TimeOffRequestReport extends ReportBase {

	public TimeOffRequestReport() {
		super("torep", "Time Off Requests");
	}
	
	public String build(String personId, int startDate, int endDate) throws DocumentException {

        try {

            PdfPTable table;

			BPerson bp=new BPerson(personId);
			writeHeaderLine("For", bp.getNameLFM());
			
			if (startDate!=0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(startDate));
			
			if (endDate!=0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(endDate));
			
            addHeaderLine();


            List<TimeOffRequest> ltor = hsu.createCriteria(TimeOffRequest.class)
				.dateSpanCompare(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, startDate, endDate)
				.joinTo(TimeOffRequest.REQUESTING_PERSON)
				.eq(Person.PERSONID, personId)
				.list();


            table = makeTable(new int[]{17, 15, 15, 15, 38});

            writeColHeader(table, "First Day Off", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Day Off", Element.ALIGN_LEFT);
            writeColHeader(table, "Status", Element.ALIGN_LEFT);
            writeColHeader(table, "Status Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Reviewer Comments", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (TimeOffRequest tor : ltor) {

				BTimeOffRequest btor=new BTimeOffRequest(tor);

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, btor.getFirstDateTimeFormatted(), alternateRow);
                write(table, btor.getLastDateTimeFormatted(), alternateRow);
                write(table, btor.getStatusFormatted(), alternateRow);
                write(table, DateUtils.getDateFormatted(btor.getStatusDate()), alternateRow);
                write(table, btor.getApprovingComment(), alternateRow);

            }
            
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

}
