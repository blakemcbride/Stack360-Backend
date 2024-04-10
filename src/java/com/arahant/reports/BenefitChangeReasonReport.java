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
/**
/**
 * Created on Aug 28, 2007
 * 
 */
package com.arahant.reports;
import java.io.FileNotFoundException;

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Aug 28, 2007
 *
 */
public class BenefitChangeReasonReport extends ReportBase {


	public BenefitChangeReasonReport() throws ArahantException {
		super("BnChrRsn","Benefit Change Reason Report");
	}
	

	public String build() throws FileNotFoundException, DocumentException {
        
        try {
        	// write out the parts of our report
        	addHeaderLine();

    		PdfPTable table;

    		boolean alternateRow = true;

            table = makeTable(new int[] { 31, 30, 13, 13, 13 });
            
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            writeColHeader(table, "Type", Element.ALIGN_LEFT);
            writeColHeader(table, "Start Date", Element.ALIGN_LEFT);
            writeColHeader(table, "End Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Effective Date", Element.ALIGN_LEFT);

            table.setHeaderRows(1);
    		
        	// spin through all exceptions passed in
            for (final HrBenefitChangeReason	reason : ArahantSession.getHSU().createCriteria(HrBenefitChangeReason.class)
            		.orderBy(HrBenefitChangeReason.TYPE).orderBy(HrBenefitChangeReason.NAME).list()) {
            		
            	// toggle the alternate row
    			alternateRow = !alternateRow;
                
    			write(table, reason.getDescription(), alternateRow);
    			switch (reason.getType())
    			{
    				case HrBenefitChangeReason.QUALIFYING_EVENT : write(table, "Qualifying Event", alternateRow);
    					break;
    				case HrBenefitChangeReason.OPEN_ENROLLMENT : write(table, "Open Enrollment", alternateRow);
						break;
    				case HrBenefitChangeReason.NEW_HIRE : write(table, "New Hire", alternateRow);
						break;
    				case HrBenefitChangeReason.INTERNAL_STAFF_EDIT : write(table, "Internal Staff Edit", alternateRow);
						break;
					default: write(table, "Unknown", alternateRow);
					
    			}
    			
    			write(table, DateUtils.getDateFormatted(reason.getStartDate()), alternateRow);
    			write(table, DateUtils.getDateFormatted(reason.getEndDate()), alternateRow);
    			write(table, DateUtils.getDateFormatted(reason.getEffectiveDate()), alternateRow);

    		
            }
            
            addTable(table);
        	
        	
        } finally {
            close();

        }
        
        return getFilename();
	}
	

}
	
