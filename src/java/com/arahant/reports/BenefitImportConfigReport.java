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
 *
 *
 */
package com.arahant.reports;

import com.arahant.business.BImportType;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class BenefitImportConfigReport extends ReportBase {

    public BenefitImportConfigReport() throws ArahantException {
        super("BenImpCfg", "Benefit Import Configuration", true);
    }

    public String build(final String name) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			BImportType[] bit = BImportType.searchBenefitImportConfigs(name);

            table = makeTable(new int[]{22, 2, 22, 2, 15, 2, 22});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);
			write(table, "", false);
            writeColHeader(table, "Source", Element.ALIGN_LEFT);
			write(table, "", false);
            writeColHeader(table, "Filetype", Element.ALIGN_LEFT);
			write(table, "", false);
            writeColHeader(table, "Program Name", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            for (BImportType bi : bit)
			{
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, bi.getImportName(), alternateRow);
				write(table, "", alternateRow);
                write(table, bi.getImportSource(), alternateRow);
				write(table, "", alternateRow);
				if (bi.getFileFormat().charAt(0) == 'D')
					write(table, "Delimited", alternateRow);
				else if (bi.getFileFormat().charAt(0) == 'F')
					write(table, "Fixed Length", alternateRow);
				write(table, "", alternateRow);
                write(table, bi.getImportProgramName(), alternateRow);
			}

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new BenefitImportConfigReport().build("%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

