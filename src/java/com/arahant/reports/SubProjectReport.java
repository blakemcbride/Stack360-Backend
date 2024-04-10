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
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class SubProjectReport extends ReportBase {

	public SubProjectReport()  {
		super("subproj","Sub-projects");
	}
	

	public String build(String id) throws DocumentException {
        
        try {
        	// write out the parts of our report
			BProject proj=new BProject(id);
			
			writeHeaderLine("Project", proj.getProjectName());
			writeHeaderLine("Title", proj.getDescription());
			
        	addHeaderLine();
            
			PdfPTable table;

			boolean alternateRow = true;

			table = makeTable(new int[] { 10,30,15,15,30 });

			writeColHeader(table, "ID", Element.ALIGN_LEFT);
			writeColHeader(table, "Summary", Element.ALIGN_LEFT);
			writeColHeader(table, "Date Created", Element.ALIGN_LEFT);
			writeColHeader(table, "External Reference", Element.ALIGN_LEFT);
			writeColHeader(table, "Requesting Company (Group)", Element.ALIGN_LEFT);
		
			

			table.setHeaderRows(1);

			
			
			
			// spin through all exceptions passed in
			for (final BProject p : proj.listSubProjects()) {

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, p.getProjectName(), alternateRow);
				write(table, p.getDescription(), alternateRow);
				write(table, p.getDateReportedFormatted(), alternateRow);
				write(table, p.getReference(), alternateRow);
				write(table, p.getRequestingCompanyName(), alternateRow);
				
			}

			addTable(table);
	
			
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	

}
