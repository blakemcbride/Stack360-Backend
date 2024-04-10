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

import com.arahant.business.BOrgGroup;
import com.arahant.business.BProjectStatus;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class OrgGroupProjectStatusReport extends ReportBase {
	public OrgGroupProjectStatusReport()
	{
		super("OgPsr","Organization Group Project Status Report");
	}
	
	public String build (BOrgGroup borg)
	{
		try {
        	// write out the parts of our report
			PdfPTable table = makeTable(new int[] { 1 });
    		if (borg.isCompany())
				writeHeader(table, "Company : " + borg.getName());
			else
			{
				writeHeader(table, "Company : " + borg.getCompanyName());
				writeHeader(table, "Organization Group : " + borg.getName());
			}
				
			addTable(table);	        
        	addHeaderLine();


    		boolean alternateRow = true;

            table = makeTable(new int[] { 30,70 });
            
			writeColHeader(table, "Code", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);


            table.setHeaderRows(1);
    /*		
			BProjectStatus []stats=borg.listAssociatedStatuses();
			
        	// spin through all exceptions passed in
            for (BProjectStatus stat : stats) {
            		
            	// toggle the alternate row
    			alternateRow = !alternateRow;

    			write(table, stat.getCode(), alternateRow);
    			write(table, stat.getDescription(), alternateRow);

            }
       */     
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
