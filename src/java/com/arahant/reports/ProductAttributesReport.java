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
 *
 */

package com.arahant.reports;

import com.arahant.business.BProductAttribute;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class ProductAttributesReport extends ReportBase {

	public ProductAttributesReport() throws ArahantException {
        super("report", "Product Attributes", true);
    }

    public String build(BProductAttribute []attributes) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            int count = 0;

            table = makeTable(new int[]{50,25,25});

            writeColHeader(table, "Attribute", Element.ALIGN_LEFT);
            writeColHeader(table, "Inactive Date", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (BProductAttribute a : attributes) {
                count++;



                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, a.getAttribute(), alternateRow);
                write(table, DateUtils.getDateFormatted(a.getInactiveDate()), alternateRow);


            }


            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

}
