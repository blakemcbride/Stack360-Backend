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
 * Created on Jul 12, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.beans.HrEmplDependent;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jul 12, 2007
 *
 */
public class HRDependentsAgeLimitReport extends ReportBase {
	public HRDependentsAgeLimitReport() throws ArahantException {
		super("HRDepAgeLmtRpt", "Dependent Age Report",true);
	}

	public String build(final String[] benefitCategoryCategoryNames, final int age, final HibernateScrollUtil<HrEmplDependent> l) throws FileNotFoundException, DocumentException, ArahantException {
        
        try {
    		final PdfPTable table = makeTable(new int[] { 1 });
    		
	        writeHeader(table, "Age Greater than or Equal to: " + age);
	        writeHeader(table, "Show Dependents Enrolled in: " + this.getBenefitCategoryCategoriesString(benefitCategoryCategoryNames));
	        
	        addTable(table);	        
        	
        	addHeaderLine();
            writeDependents(benefitCategoryCategoryNames,l);
        } finally {
            close();

        }
        
        return getFilename();
	}
	
	
	protected String getBenefitCategoryCategoriesString(final String[] benefitCategoryCategoryNames) {
		String namesFormated = "";
		
		for (int idx = 0; idx < benefitCategoryCategoryNames.length; idx++)
		{
			if (idx > 0)
			{
				namesFormated += ", ";
			}			
			namesFormated += benefitCategoryCategoryNames[idx];
		}
		
		return namesFormated;
	}
		
	protected void writeDependents(final String[] benefitCategoryCategoryNames, final HibernateScrollUtil<HrEmplDependent> l) throws DocumentException, ArahantException {
		PdfPTable table;

		boolean alternateRow = true;
		
		final int[] columns = new int[6 + benefitCategoryCategoryNames.length];
		final int averageWidth = 20 / benefitCategoryCategoryNames.length;
		
		columns[0] = 14;
		columns[1] = 19;
		columns[2] = 14;
		columns[3] = 19;
		columns[4] = 4;
		columns[5] = 10;			
		for (int idx = 0; idx < benefitCategoryCategoryNames.length; idx++) {
			columns[6 + idx] = averageWidth;
		} 
		
		table = makeTable(columns);
       
        writeColHeader(table, "Employee SSN", Element.ALIGN_LEFT);
        writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Dependent SSN", Element.ALIGN_LEFT);
        writeColHeader(table, "Dependent Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Age", Element.ALIGN_RIGHT);
        writeColHeader(table, "DOB", Element.ALIGN_RIGHT);
        for (String element : benefitCategoryCategoryNames) {
        	if (benefitCategoryCategoryNames.length>2)
        		writeColHeaderRotate(table,element);
        	else
        		writeColHeader(table, element, Element.ALIGN_LEFT);
        }

        table.setHeaderRows(1);
		

        while (l.next())
        {
        	final HrEmplDependent depen=l.get();
	    	// toggle the alternate row
			alternateRow = !alternateRow;
			
			final BHREmplDependent dep=new BHREmplDependent(depen);
	        
			write(table, dep.getEmployeeSSN(), alternateRow);
			write(table, dep.getEmployeeNameLFM(), alternateRow);
			write(table, dep.getSsn(), alternateRow);
			write(table, dep.getNameLFM(), alternateRow);
			writeRight(table, dep.getAge(), alternateRow);
			writeRight(table, DateUtils.getDateFormatted(dep.getDob()), alternateRow);
			for (String element : benefitCategoryCategoryNames) {
				// write Y or N, depending on if they have a benefit category associated
				// that itself has a matching benefit category category
				write(table, dep.hasBenefitCategoryByName(element), alternateRow);
			}
		}
        
        l.close();
        
        addTable(table);
	}

	
}

	
