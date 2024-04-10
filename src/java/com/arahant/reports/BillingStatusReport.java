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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.arahant.business.BBillingStatus;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class BillingStatusReport extends ReportBase {

	public BillingStatusReport() {
		super("AppLoc", "Billing Status Report");
	}

	public String build ()
	{
		try {
			PdfPTable table = new PdfPTable(1);
	      
			
	        addTable(table);
            addHeaderLine();


			BBillingStatus [] jobs=BBillingStatus.list();
			

            table = makeTable(new int[]{100});

            
			writeColHeader(table, "Name");
			
            boolean alternateRow = true;

            for (BBillingStatus cc : jobs) {


                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, cc.getName(), alternateRow);

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
