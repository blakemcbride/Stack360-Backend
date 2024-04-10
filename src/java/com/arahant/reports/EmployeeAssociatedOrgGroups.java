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
 * Created on Jul 19, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.beans.Employee;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Jul 19, 2007
 *
 */
public class EmployeeAssociatedOrgGroups extends ReportBase{
	

	public EmployeeAssociatedOrgGroups() throws ArahantException {
		super("EmpOrgGrps","Employee's Associated Organizational Groups");
	}


	
	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String getReport(final Employee employee) throws ArahantException {

		try
		{         
        	// write out the parts of our report
			final PdfPTable table = new PdfPTable(1);
	        table.setHorizontalAlignment(Element.ALIGN_CENTER);
	        table.setWidthPercentage(100F);
	        table.setSpacingBefore(5F);
	        
	        writeHeader(table, "Employee: " + employee.getNameLFM());
	        
	        addTable(table);
	        
			addHeaderLine();
            writeTokens(employee);
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
	

	
	protected void writeTokens(final Employee employee) throws DocumentException {
		boolean alternateRow = true;
		final PdfPTable table = makeTable(new int[] { 55, 15, 15, 15 });
       
        writeColHeader(table, "Name", Element.ALIGN_LEFT);
        writeColHeader(table, "Supervisor", Element.ALIGN_LEFT);
        writeColHeader(table, "Start Date", Element.ALIGN_RIGHT);
        writeColHeader(table, "Final Date", Element.ALIGN_RIGHT);
      
        table.setHeaderRows(1);
        
        for (final OrgGroupAssociation oga : employee.getOrgGroupAssociations()) {

			alternateRow = !alternateRow; // toggle the alternate row
            
			write(table, oga.getOrgGroup().getName(), alternateRow);
			write(table, oga.getPrimaryIndicator()=='Y'?"Yes":"No", alternateRow);
			write(table, DateUtils.getDateFormatted(oga.getStartDate()), alternateRow);
			write(table, DateUtils.getDateFormatted(oga.getFinalDate()), alternateRow);
			
		}
	
        
        addTable(table);
	}
}

	
