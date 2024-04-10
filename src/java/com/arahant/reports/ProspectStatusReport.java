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

import com.arahant.beans.ProspectStatus;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ProspectStatusReport extends ReportBase
{

	public ProspectStatusReport() {
		super("prrp", "Prospect Status");
	}

	public String build() throws  DocumentException {
        
        try {
        	// write out the parts of our report
        	
			addHeaderLine();
            PdfPTable table;

			boolean alternateRow = true;

			table = makeTable(new int[] { 25, 50, 15, 10 });

			writeColHeader(table, "Code", Element.ALIGN_LEFT);
			writeColHeader(table, "Description", Element.ALIGN_LEFT);
			writeColHeader(table, "Priority", Element.ALIGN_LEFT);
			writeColHeader(table, "Active", Element.ALIGN_LEFT);

			table.setHeaderRows(1);
			
			List<ProspectStatus> l=hsu.createCriteria(ProspectStatus.class)
					.orderBy(ProspectStatus.SEQ)
					.list();

			// spin through all exceptions passed in
			for (final ProspectStatus s : l) {

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, s.getCode(), alternateRow);
				write(table, s.getDescription(), alternateRow);
				write(table, s.getSequence(), alternateRow);
				write(table, s.getActive()=='Y'?"Yes":"No", alternateRow);


			}

			addTable(table);
		} finally {
			close();

		}
        
		return getFilename();
	}
	

	
}
