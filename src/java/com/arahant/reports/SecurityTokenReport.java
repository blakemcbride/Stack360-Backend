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


/**
 * Created on Jun 8, 2007
 * 
 */
package com.arahant.reports;
import java.util.Collection;

import com.arahant.beans.Right;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jun 8, 2007
 *
 */
public class SecurityTokenReport extends ReportBase {


	public SecurityTokenReport() throws ArahantException {
		super("SecTok","Security Token Report");
	}


	
	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String getReport(final Collection <Right> toks) throws ArahantException {

		try
		{         
        	// write out the parts of our report
			addHeaderLine();
            writeTokens(toks);
		}
		catch (final Exception e)
		{
			throw new ArahantException(e);
		}
		finally {
            close();
        }
		return getFilename();
		
	}
	

	
	protected void writeTokens(final Collection <Right> toks) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(new int[] { 25,75 });
       
        writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Description", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
        
        for (final Right right : toks) {
//        	 toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, right.getIdentifier(), alternateRow);
			write(table, right.getDescription(), alternateRow);
			
		}
	
        
        addTable(table);
	}

		
}

	
