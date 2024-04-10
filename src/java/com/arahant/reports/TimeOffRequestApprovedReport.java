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
package com.arahant.reports;

import com.arahant.beans.TimeOffRequest;
import com.arahant.business.BTimeOffRequest;
import com.arahant.lisp.ABCL;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author Blake McBride
 */
public class TimeOffRequestApprovedReport extends ReportBase {

	public TimeOffRequestApprovedReport() {
		super("atorep", "Approved Time Off Requests", true);
	}

	public String build(int startDate, int endDate) throws DocumentException {

		try {
			PdfPTable table;

			if (startDate != 0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(startDate));

			if (endDate != 0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(endDate));
			
			writeHeaderLine("Ordered by Beginning Date & Time");

			addHeaderLine();
			
			HibernateScrollUtil<TimeOffRequest> scr = hsu.createCriteria(TimeOffRequest.class)
					.dateSpanCompare(TimeOffRequest.START_DATE, TimeOffRequest.END_DATE, startDate, endDate)
					.eq(TimeOffRequest.STATUS, TimeOffRequest.APPROVED)
					.orderBy(TimeOffRequest.START_DATE)
					.orderBy(TimeOffRequest.FROM_TIME)
					.joinTo(TimeOffRequest.REQUESTING_PERSON)
					.scroll();

			table = makeTable(new int[]{30, 10, 10, 15, 35});

			writeColHeader(table, "Employee", Element.ALIGN_LEFT);
			writeColHeader(table, "Beginning", Element.ALIGN_LEFT);
			writeColHeader(table, "Ending", Element.ALIGN_LEFT);
			writeColHeader(table, "Benefit", Element.ALIGN_LEFT);
			writeColHeader(table, "Employee Comments", Element.ALIGN_LEFT);

			boolean alternateRow = true;

			while (scr.next()) {
				
				BTimeOffRequest btor = new BTimeOffRequest(scr.get());

				// toggle the alternate row
				alternateRow = !alternateRow;
				
				write(table, btor.getRequestingPersonFormatted(), alternateRow);
				write(table, DateUtils.getDateTimeFormatted(btor.getFirstDateOff(), btor.getFirstTimeOff()), alternateRow);
				write(table, DateUtils.getDateTimeFormatted(btor.getLastDateOff(), btor.getLastTimeOff()), alternateRow);
				write(table, btor.getBenefitName(), alternateRow);
				write(table, btor.getRequestingComment(), alternateRow);
			}

			addTable(table);
		} finally {
			close();
		}
		return getFilename();
	}
	
	public static void main(String args[]) throws Exception {
		ABCL.init();
		System.out.println("Report = " + 
			new TimeOffRequestApprovedReport().build(20000101, 20140101));
    }

}
