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
 * Created on Dec 11, 2009
 *
 */
package com.arahant.reports;

import com.arahant.beans.AssemblyTemplate;
import com.arahant.beans.ProductTypeChild;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class AssemblyTemplateReport extends ReportBase {

    public AssemblyTemplateReport() throws ArahantException {
        super("AssemblyTemp", "Assembly Template", true);
    }

    public String build() throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            HibernateCriteriaUtil<AssemblyTemplate> hcu = ArahantSession.getHSU().createCriteria(AssemblyTemplate.class)
			.orderBy(ProductTypeChild.DESCRIPTION);

            HibernateScrollUtil<AssemblyTemplate> scr = hcu.scroll();

            int count = 0;

            table = makeTable(new int[]{32, 3, 62});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            while (scr.next()) {
                count++;

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, scr.get().getAssemblyName(), alternateRow);
                write(table,"",alternateRow);
                write(table, scr.get().getDescription(), alternateRow);


            }

            scr.close();
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new AssemblyTemplateReport().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
