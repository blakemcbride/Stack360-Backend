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
import com.arahant.beans.ProphetLogin;
import com.arahant.beans.ScreenGroup;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ScreenGroupUsersReport extends ReportBase {

	public ScreenGroupUsersReport() throws ArahantException {
        super("report", "Screen Group Users");
    }

    public String build() throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<ScreenGroup> hcu = ArahantSession.getHSU().createCriteria(ScreenGroup.class)
				.orderBy(ScreenGroup.NAME);

            List<ScreenGroup> groups = hcu.list();

            table = makeTable(new int[]{30,70});

            writeColHeader(table, "Screen Group", Element.ALIGN_LEFT);
            writeColHeader(table, "User Name", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            for (ScreenGroup g : groups) {
            
                // toggle the alternate row
                alternateRow = !alternateRow;

				HibernateScrollUtil<Person> scr=hsu.createCriteria(Person.class)
					.orderBy(Person.LNAME)
					.orderBy(Person.FNAME)
					.orderBy(Person.MNAME)
					.joinTo(Person.PROPHETLOGINS)
					.eq(ProphetLogin.SCREENGROUP, g)
					.scroll();

				while (scr.next())
				{

	                write(table, g.getName(), alternateRow);
					write(table, scr.get().getNameLFM(), alternateRow);

				}
				scr.close();

            }


            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new ScreenGroupUsersReport().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
