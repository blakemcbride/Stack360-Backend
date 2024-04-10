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
 * Created on Feb 27, 2007
 * 
 */
package com.arahant.reports;

import com.arahant.business.BHREmployeeStatus;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Feb 27, 2007
 *
 */
public class HREmployeeStatusReport  extends ReportBase {

     public HREmployeeStatusReport() {
    	super("report", "Employee Status Report");
    }

	public String build(int activeType) throws DocumentException {

        try {

            PdfPTable table;

			if (activeType==1)
				writeHeaderLine("Show only", "Actives");

			if (activeType==2)
				writeHeaderLine("Show only", "Inactives");

            addHeaderLine();


            table = makeTable(new int[] { 50,30,20});

            writeColHeader(table, "Status", Element.ALIGN_LEFT);
            writeColHeader(table, "Type", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Active Date", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (BHREmployeeStatus evalCategory : BHREmployeeStatus.list(activeType)) {
				 // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, evalCategory.getName(), alternateRow);
                write(table, evalCategory.getActiveFlag()=='Y'?"Active":"Inactive", alternateRow);
                write(table, DateUtils.getDateFormatted(evalCategory.getLastActiveDate()), alternateRow);
            }

            addTable(table);



        } finally {
            close();

        }

        return getFilename();
    }


}

	
