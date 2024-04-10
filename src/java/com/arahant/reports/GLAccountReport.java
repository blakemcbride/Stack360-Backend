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
 * Created on Jun 12, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.business.BGlAccount;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Jun 12, 2007
 *
 */
public class GLAccountReport extends ReportBase {

	public GLAccountReport() throws ArahantException {
		super("GlAcctRpt","GL Account Report");
	}
	

	public String build(final BGlAccount []accts) throws FileNotFoundException, DocumentException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeAccounts(accts);
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	
	protected void writeAccounts(final BGlAccount []accts) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(new int[] { 34, 33, 23, 10 });
       
        writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Number", Element.ALIGN_LEFT);
        writeColHeader(table, "Type", Element.ALIGN_LEFT);
        writeColHeader(table, "Default", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        for (final BGlAccount account : accts) {
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, account.getAccountName(), alternateRow);
			write(table, account.getAccountNumber(), alternateRow);
			write(table, account.getAccountTypeFormatted(), alternateRow);
			write(table, (account.getDefaultFlag()==1)?"Yes" : "No", alternateRow);
		
        }
        
        addTable(table);
	}
	
}

	
