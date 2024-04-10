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

import com.arahant.business.BHREvalCategory;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;


/**
 * 
 *
 * Created on Feb 27, 2007
 *
 */
public class HREvalCategoryReport extends ReportBase {

    
    public HREvalCategoryReport() {
    	super("report", "Evaluation Category Report");
    }

	public String build(int activeType) throws DocumentException {

        try {

            PdfPTable table;

			if (activeType==1)
				writeHeaderLine("Show only", "Actives");

			if (activeType==2)
				writeHeaderLine("Show only", "Inactives");

            addHeaderLine();


            table = makeTable(new int[] { 20,8,40,16,16});

            writeColHeader(table, "Category", Element.ALIGN_LEFT);
            writeColHeader(table, "Weight", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            writeColHeader(table, "First Active", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Active", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (BHREvalCategory evalCategory : BHREvalCategory.list(activeType)) {
				 // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, evalCategory.getName(), alternateRow);
                write(table, evalCategory.getWeight()+"", alternateRow);
                write(table, evalCategory.getDescription(), alternateRow);
                write(table, DateUtils.getDateFormatted(evalCategory.getFirstActiveDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(evalCategory.getInactiveDate()), alternateRow);
            }

            addTable(table);



        } finally {
            close();

        }

        return getFilename();
    }

	
	
}

	
