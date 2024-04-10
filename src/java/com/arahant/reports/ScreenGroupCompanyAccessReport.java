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
 */
package com.arahant.reports;

import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class ScreenGroupCompanyAccessReport extends ReportBase {

    public ScreenGroupCompanyAccessReport() throws ArahantException {
        super("ScrGrpCoRpt", "Screen Group Company Access", true);
    }

    public String build(final String firstName, final String lastName, final String screenGroupId, final String securityGroupId, final String companyId) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			BPerson[] bp = BPerson.searchScreenAndSecurityGroups(firstName, lastName, screenGroupId, securityGroupId, companyId);

            table = makeTable(new int[]{18, 3, 18, 3, 18, 3, 18, 3, 25, 3, 25});

			writeColHeader(table, "Company", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Last Name", Element.ALIGN_LEFT);
            write(table,"",false);
			writeColHeader(table, "First Name", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Last Signed In", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Screen Group", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Security Group", Element.ALIGN_LEFT);
            boolean alternateRow = true;

			for (BPerson bpp: bp)
			{
				// toggle the alternate row
                alternateRow = !alternateRow;

				write(table, bpp.getCompanyName(), alternateRow);
				write(table,"" ,alternateRow);
                write(table, bpp.getLastName(), alternateRow);
				write(table,"",alternateRow);
				write(table, bpp.getFirstName(), alternateRow);
				write(table,"" ,alternateRow);
                write(table, bpp.getSignInDate(bpp.getPersonId()), alternateRow);
				write(table,"",alternateRow);
                write(table, bpp.getScreenGroupName(), alternateRow);
				write(table,"",alternateRow);
                write(table, bpp.getSecurityGroupName(), alternateRow);
			}

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
			new ScreenGroupCompanyAccessReport().build("", "", "", "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

