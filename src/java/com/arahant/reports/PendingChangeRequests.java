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


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.reports;

import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class PendingChangeRequests extends ReportBase {

	public PendingChangeRequests() throws ArahantException {
        super("report", "People with Pending Change Requests");
    }

    public String build() throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<Person> hcu = ArahantSession.getHSU().createCriteria(Person.class)
					.orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
					.joinTo(Person.CHANGE_REQUESTS);


            int count = 0;

            table = makeTable(new int[]{100});

            writeColHeader(table, "Name", Element.ALIGN_LEFT);

            boolean alternateRow = true;

			for (Person p : hcu.list())
			{
                count++;
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, p.getNameLFM(), alternateRow);

            }

            addTable(table);

			table=makeTable(new int[]{100});

            write(table, "Total: " + count);

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new PendingChangeRequests().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
