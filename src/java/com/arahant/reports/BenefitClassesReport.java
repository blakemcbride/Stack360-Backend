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
 * Created on Jan 9, 2008
 * 
 */
package com.arahant.reports;

import com.arahant.business.BBenefitClass;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * 
 *
 * Created on Jan 9, 2008
 *
 */
public class BenefitClassesReport extends ReportBase {

    public BenefitClassesReport() throws ArahantException {
        super("Rep", "Benefit Classes", true);
    }

    public String build(int activeType) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            table = makeTable(new int[]{26, 50, 12, 12});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            writeColHeader(table, "First Active Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Active Date", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            for (BBenefitClass c : BBenefitClass.list(activeType)) {

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, c.getName(), alternateRow);
                write(table, c.getDescription(), alternateRow);
                write(table, DateUtils.getDateFormatted(c.getFirstActiveDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(c.getLastActiveDate()), alternateRow);
             
            }
            
            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new BenefitClassesReport().build(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

	
