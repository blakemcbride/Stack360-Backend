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
 * Created on Jun 15, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Jun 15, 2007
 *
 */
public class HRDependentReport extends ReportBase {


	public HRDependentReport() throws ArahantException {
		super("HrDep","HR Dependent Report");
	}


	
	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String getReport(final BEmployee employee) throws ArahantException {

		try
		{         
        	// write out the parts of our report
			final PdfPTable table = new PdfPTable(1);
	        table.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.setWidthPercentage(100F);
	        table.setSpacingBefore(5F);
	        
	        writeHeader(table, "Employee: " + employee.getNameLFM());
	        writeHeader(table, "SSN: " + ((employee.getSsn()==null)?"":employee.getSsn()));
	        
	        addTable(table);
	        
			addHeaderLine();
            writeTokens(employee.listDependents());
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
	

	
	protected void writeTokens(final BHREmplDependent[] deps) throws DocumentException {
		boolean alternateRow = true;
		final PdfPTable table = makeTable(new int[] { 30,20,10,25,15 });
       
        writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "SSN", Element.ALIGN_LEFT);
        writeColHeader(table, "Sex", Element.ALIGN_LEFT);
        writeColHeader(table, "Relationship", Element.ALIGN_LEFT);
        writeColHeader(table, "DOB", Element.ALIGN_LEFT);

        table.setHeaderRows(1);
        
        for (final BHREmplDependent dep : deps) {

			alternateRow = !alternateRow; // toggle the alternate row
            
			write(table, dep.getNameLFM(), alternateRow);
			write(table, dep.getSsn(), alternateRow);
			write(table, dep.getSex()+"", alternateRow);
			write(table, dep.getTextRelationship(), alternateRow);
			write(table, DateUtils.getDateFormatted(dep.getDob()), alternateRow);
			
		}
	
        
        addTable(table);
	}

}

	
