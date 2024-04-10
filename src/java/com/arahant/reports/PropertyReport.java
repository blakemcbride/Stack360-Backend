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

import com.arahant.business.BProperty;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class PropertyReport extends ReportBase {

	public PropertyReport() {
		super("PropRpt","System Properties");
	}

	public String build() {

		try {
			// write out the parts of our report
			addHeaderLine();
			PdfPTable table;

			boolean alternateRow = true;

			table = makeTable(new int[]{25, 25, 50});

			writeColHeader(table, "Name", Element.ALIGN_LEFT);
			writeColHeader(table, "Value", Element.ALIGN_LEFT);
			writeColHeader(table, "Description", Element.ALIGN_LEFT);

			table.setHeaderRows(1);



			// spin through all exceptions passed in
			for (final BProperty p : BProperty.list()) {

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, p.getName(), alternateRow);
				write(table, p.getValue(), alternateRow);
				write(table, p.getDescription(), alternateRow);


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
