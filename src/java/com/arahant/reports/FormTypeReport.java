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
 * Created on Jul 10, 2007
 * 
 */
package com.arahant.reports;

import java.io.FileNotFoundException;

import com.arahant.business.BFormType;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jul 10, 2007
 *
 */
public class FormTypeReport extends ReportBase {

	public FormTypeReport() throws ArahantException {
		super("FrmTyRpt","Form Type List");
	}

	public String build(final BFormType []types) throws FileNotFoundException, DocumentException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeAccounts(types);
        } finally {
            close();
        }
        
        return getFilename();
	}
	
	
	protected void writeAccounts(final BFormType []types) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(25, 25, 75);
       
        writeColHeader(table, "Code", Element.ALIGN_LEFT);
        writeColHeader(table, "Field Downloadable", Element.ALIGN_LEFT);
        writeColHeader(table, "Description", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        for (final BFormType typ : types) {
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, typ.getCode(), alternateRow);
            write(table, typ.getFieldDownloadable()+"", alternateRow);
			write(table, typ.getDescription(), alternateRow);
        }
        
        addTable(table);
	}
	
}

	
