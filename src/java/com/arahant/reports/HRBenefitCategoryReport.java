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
 * Created on Jul 3, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.business.BHRBenefitCategory;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jul 3, 2007
 *
 */
public class HRBenefitCategoryReport  extends ReportBase {


	public HRBenefitCategoryReport() throws ArahantException {
		super("BeneCats","Benefit Category Report");
	}
	
	

	public String build(final BHRBenefitCategory []cats) throws FileNotFoundException, DocumentException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();
            writeCategories(cats);
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	
	protected void writeCategories(final BHRBenefitCategory []cats) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;

        table = makeTable(new int[] { 50,50 });
       
        writeColHeader(table, "Benefit Category", Element.ALIGN_CENTER);
        writeColHeader(table, "Type", Element.ALIGN_CENTER);

        table.setHeaderRows(1);
		
    	// spin through all exceptions passed in
        for (final BHRBenefitCategory cat : cats) {
        		
        	// toggle the alternate row
			alternateRow = !alternateRow;
            
			write(table, cat.getDescription(), alternateRow);
			write(table, cat.getTypeName(), alternateRow);
		
        }
        
        addTable(table);
	}
	
}

	
