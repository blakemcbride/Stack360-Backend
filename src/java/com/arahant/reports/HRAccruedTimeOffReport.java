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
 * Created on Mar 16, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.business.BHRAccruedTimeOff;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Mar 16, 2007
 *
 */
public class HRAccruedTimeOffReport extends ReportBase {

	private HibernateSessionUtil hsu;
    
    public HRAccruedTimeOffReport() throws ArahantException {
    	super("HRATORept","Accrued Time Off Report");
    }

	public void build(final HibernateSessionUtil hsu, final BHRAccruedTimeOff[] accruedTimeOff, final String employeeId, final String benefitId, final int startDate, final int endDate) throws FileNotFoundException, DocumentException {
        this.hsu = hsu;
        
        try {

        	// write out the parts of our report
            this.writeHeader(employeeId, benefitId, startDate, endDate);
            this.writeAccruedTimeOff(accruedTimeOff);
        } finally {
           close();
            
            this.hsu = null;
        }
	}
	
	protected void writeHeader(final String employeeId, final String benefitId, final int startDate, final int endDate) throws DocumentException {
    	final Employee employee = this.hsu.get(Employee.class, employeeId);
		final HrBenefit benefit = this.hsu.get(HrBenefit.class, benefitId);
		
		
		
		final PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.setWidthPercentage(100F);
        table.setSpacingBefore(5F);
        
        writeHeader(table, "Employee: " + employee.getNameLFM());
        writeHeader(table, "Benefit Name: " + benefit.getName());
        writeHeader(table, "Start Date: " + DateUtils.getDateFormatted(startDate));
        
        if (endDate == 0) {           
        	writeHeader(table, "Showing Entries for Entire Period.");   
        } else {      
        	writeHeader(table, "Showing Entries up to Ending Date:" + DateUtils.getDateFormatted(endDate));   
        }
        
        addTable(table);
        
        addHeaderLine();
	}
	
	protected void writeAccruedTimeOff(final BHRAccruedTimeOff[] accruedTimeOffs) throws DocumentException {
		PdfPTable table;

        boolean alternateRow = true;

		BHRAccruedTimeOff accruedTimeOff;
		
        table = makeTable(new int[] { 16, 16, 16, 52 });

        writeColHeader(table, "Date", Element.ALIGN_RIGHT);
        writeColHeader(table, "Hours", Element.ALIGN_RIGHT);
        writeColHeader(table, "Net Time Off", Element.ALIGN_RIGHT);
        writeColHeader(table, "Description", Element.ALIGN_RIGHT);
        table.setHeaderRows(1);
		
        for (final BHRAccruedTimeOff element : accruedTimeOffs) {
        	accruedTimeOff = element;
        	
        	// toggle the alternate row
			alternateRow = !alternateRow;
			
			writeRight(table, DateUtils.getDateFormatted(accruedTimeOff.getAccrualDate()), alternateRow);
			writeRight(table, com.arahant.utils.Formatting.formatNumber(accruedTimeOff.getAccrualHours(), 2), alternateRow);
			writeRight(table, com.arahant.utils.Formatting.formatNumber(accruedTimeOff.getRunningTotal(),2), alternateRow);
			writeRight(table, accruedTimeOff.getDescription(), alternateRow);
           

        }
        
        addTable(table);
	}
	

}

	
