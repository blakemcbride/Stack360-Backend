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
import java.util.Calendar;
import java.util.HashSet;

import org.hibernate.ScrollableResults;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jul 12, 2007
 *
 */
public class HRDependentsAgeReport extends ReportBase {
	public HRDependentsAgeReport() throws ArahantException {
		super("HRDepAgeLmtRpt", "Dependent Child Age Report",true);
	}

	public String build(final String[] benefitCategoryCategoryNames, final int age, final ScrollableResults l, final int year, int inactiveAsOf) throws FileNotFoundException, DocumentException {
        try {
    		final PdfPTable table = makeTable(new int[] { 1 });
    		
    		writeHeader(table, "Year: "+year);
	        writeHeader(table, "Age: " + age);
	        if (inactiveAsOf==0)
	        	writeHeader(table, "Ignore Inactive Dependents: No");
	        else
	        	writeHeader(table, "Ignore Inactive Dependents as of: " + DateUtils.getDateFormatted(inactiveAsOf));
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
		
		for (int idx = 0; idx < benefitCategoryCategoryNames.length; idx++) {
			if (idx > 0) {
				namesFormated += ", ";
			}			
			namesFormated += benefitCategoryCategoryNames[idx];
		}
		
		return namesFormated;
	}
		
	protected void writeDependents(final String[] benefitCategoryCategoryNames, final ScrollableResults l) throws DocumentException {
		PdfPTable table;

		boolean alternateRow = true;
		
		final int[] columns = new int[6 + benefitCategoryCategoryNames.length];
                int averageWidth = 0;
                if (benefitCategoryCategoryNames.length!=0)
                    averageWidth= 24 / benefitCategoryCategoryNames.length;
		
		columns[0] = 14;
		columns[1] = 19;
		columns[2] = 14;
		columns[3] = 19;
		columns[4] = 0;
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
		
        String lastPersonId="";
        
        final HashSet<String> beneCats=new HashSet<String>();
        

        while (l.next()) {			
			if (!lastPersonId.equals(l.getString(8))) {
				//dump out the benefit data
				if (!isEmpty(lastPersonId))	{
					for (String element : benefitCategoryCategoryNames) {
						// write Y or N, depending on if they have a benefit category associated
						// that itself has a matching benefit category category
						write(table, beneCats.contains(element)?"Y":"N", alternateRow);
					}
				}
				
				
				beneCats.clear();
				
				alternateRow = !alternateRow;
				
				//write out the new guy info
				write(table, l.getString(7), alternateRow);
				write(table, l.getString(5)+", "+l.getString(6), alternateRow);
				write(table, l.getString(2), alternateRow);
				write(table, l.getString(1)+", "+l.getString(0), alternateRow);
				writeRight(table, getAge(l.getInteger(3)), alternateRow);
				writeRight(table, DateUtils.getDateFormatted(l.getInteger(3)), alternateRow);
				
				lastPersonId=l.getString(8);
			}

			beneCats.add(l.getString(4));
		}

        if (!isEmpty(lastPersonId))	{
			for (String element : benefitCategoryCategoryNames) {
				// write Y or N, depending on if they have a benefit category associated
				// that itself has a matching benefit category category
				write(table, beneCats.contains(element)?"Y":"N", alternateRow);
			}
		}
        
        l.close();
        
        addTable(table);
	}
	
	
	
	Calendar now=DateUtils.getNow();
	final Calendar dob= Calendar.getInstance();


	public final String getAge(final int birthday) {
		if (birthday<1)
			return "";
		
		final int day=birthday%100;
		final int month=(birthday%10000-day)/100;
		final int year=birthday/10000;
		
		dob.set(Calendar.YEAR, year);
		dob.set(Calendar.MONTH, month-1);
		dob.set(Calendar.DAY_OF_MONTH, day);
		
		int age=now.get(Calendar.YEAR)-dob.get(Calendar.YEAR);
		
		dob.set(Calendar.YEAR, now.get(Calendar.YEAR));
		
		if (dob.after(now))
			age--;
		
		if (age<0)
			return "";
		return age+"";
	}
	
	
}

	
