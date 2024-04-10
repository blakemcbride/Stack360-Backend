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

import com.arahant.business.BService;
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
public class ProductServiceReport extends ReportBase {

	public ProductServiceReport() throws ArahantException {
		super("ProdSrvRpt","Product/Service Report");
	}
	

	public String build(final BService []prods) throws FileNotFoundException, DocumentException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeProducts(prods);
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	
	protected void writeProducts(final BService []prods) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(new int[] { 30, 30, 40 });
        
        
        writeColHeader(table, "ID", Element.ALIGN_LEFT);
        writeColHeader(table, "Account", Element.ALIGN_LEFT);
        writeColHeader(table, "Description", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        for (final BService product : prods) {
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, product.getAccsysId(), alternateRow);
			write(table, product.getAccsysAccount(), alternateRow);
			write(table, product.getDescription(), alternateRow);
        }
        
        addTable(table);
	}
	
}

	
