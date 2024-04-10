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

import com.arahant.business.BApplicationStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class ApplicantAppStatusReport extends ReportBase {

	public ApplicantAppStatusReport() {
		super("AppLoc", "Application Status Report");
	}

	public String build(int activeType) {
		try {
			PdfPTable table = new PdfPTable(1);

			if (activeType == 0)
				writeHeaderLine("Show", "All");
			if (activeType == 1)
				writeHeaderLine("Show", "Have no inactive dates");
			if (activeType == 2)
				writeHeaderLine("Show", "Only have inactive dates");

			addTable(table);
			addHeaderLine();


			BApplicationStatus[] jobs = BApplicationStatus.list(activeType);


			table = makeTable(new int[]{60, 20, 20});


			writeColHeader(table, "Description");
			writeColHeader(table, "Inactive Date");
			writeColHeader(table, "Active");

			boolean alternateRow = true;

			for (BApplicationStatus cc : jobs) {


				// toggle the alternate row
				alternateRow = !alternateRow;


				write(table, cc.getName(), alternateRow);
				write(table, DateUtils.getDateFormatted(cc.getInactiveDate()), alternateRow);
				writeRight(table, cc.getActive() ? "Yes" : "No", alternateRow);

			}


			addTable(table);

		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}

		return getFilename();
	}
}
