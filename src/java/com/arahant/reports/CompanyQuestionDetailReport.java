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

import com.arahant.business.BCompanyQuestionDetail;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class CompanyQuestionDetailReport extends ReportBase {

	public CompanyQuestionDetailReport(String title) {
		super("cqdr", title);
	}

	public String build(String id)
	{
		try {
			
			BCompanyQuestionDetail [] dets=BCompanyQuestionDetail.list(id);

			BOrgGroup borg=new BOrgGroup(id);
			
            PdfPTable table = new PdfPTable(1);
	       
			if (borg.getOrgGroupType()==ArahantConstants.CLIENT_TYPE)
				writeHeaderLine("Client", borg.getName());
			else
				writeHeaderLine("Prospect", borg.getName());
	        
	        addTable(table);
            addHeaderLine();


	        table = makeTable(new int[]{35,45,20});

            writeColHeader(table, "Question");
			writeColHeader(table, "Response");
            writeColHeader(table, "Entered");

            boolean alternateRow = true;

            for (BCompanyQuestionDetail d : dets) {


                // toggle the alternate row
                alternateRow = !alternateRow;

				write(table, d.getQuestion(), alternateRow);
				write(table, d.getResponse(), alternateRow);
				write(table, d.getWhenAddedFormatted(), alternateRow);
						
            }
            

            addTable(table);

        } 
		catch (Exception e)
		{
			throw new ArahantException(e);
		}
		  finally {
            close();

        }

        return getFilename();
	}
	
	
}
