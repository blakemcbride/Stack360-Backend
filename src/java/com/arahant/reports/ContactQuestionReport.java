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

import com.arahant.business.BContactQuestion;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class ContactQuestionReport extends ReportBase {

	public ContactQuestionReport() {
		super("cqdr", "Contact Questions");
	}

	public String build() {
		try {


			PdfPTable table = new PdfPTable(1);



			addTable(table);
			addHeaderLine();


			table = makeTable(new int[]{100});

			writeColHeader(table, "Question");

			boolean alternateRow = true;

			for (BContactQuestion d : BContactQuestion.list(true)) {


				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, d.getQuestion(), alternateRow);

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
