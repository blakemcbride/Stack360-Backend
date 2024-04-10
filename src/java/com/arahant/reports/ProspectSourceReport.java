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

import com.arahant.beans.ProspectSource;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ProspectSourceReport extends ReportBase
{

	public ProspectSourceReport() {
		super("prrp", "Prospect Source");
	}

	public String build() throws  DocumentException {
        
        try {
        	// write out the parts of our report
        	
			addHeaderLine();
            PdfPTable table;

			boolean alternateRow = true;

			table = makeTable(new int[] { 25,75 });

			writeColHeader(table, "Code", Element.ALIGN_LEFT);
			writeColHeader(table, "Description", Element.ALIGN_LEFT);

			table.setHeaderRows(1);
			
			List<ProspectSource> l=hsu.createCriteria(ProspectSource.class)
					.orderBy(ProspectSource.SOURCE_CODE)
					.list();

			// spin through all exceptions passed in
			for (final ProspectSource s : l) {

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, s.getSourceCode(), alternateRow);
				write(table, s.getDescription(), alternateRow);
			}

			addTable(table);
		} finally {
			close();

		}
        
		return getFilename();
	}
	

	
}
