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

import com.arahant.beans.ClientCompany;
import com.arahant.beans.ClientStatus;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



public class ClientStatusReport extends ReportBase {

	public ClientStatusReport() throws ArahantException {
        super("ClientStat", "Client Status Report", true);
    }

    public String build() throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            HibernateCriteriaUtil<ClientCompany> hcu = ArahantSession.getHSU().createCriteria(ClientCompany.class)
					.joinTo(ClientCompany.CLIENT_STATUS)
					.orderBy(ClientStatus.SEQ);

            HibernateScrollUtil<ClientCompany> scr = hcu.scroll();

            int count = 0;

			//client names, status, last contact date, and status comments - sorted by status

            table = makeTable(new int[]{20, 2, 8, 2, 11, 2, 50});

            writeColHeader(table, "Client Name", Element.ALIGN_TOP);
			write(table,"",false);
            writeColHeader(table, "Status", Element.ALIGN_TOP);
			write(table,"",false);
			writeColHeader(table, "Last Contact", Element.ALIGN_TOP);
			write(table,"",false);
			writeColHeader(table, "Status Comments", Element.ALIGN_TOP);

            boolean alternateRow = true;

            while (scr.next()) {
                count++;

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, scr.get().getName(), alternateRow);
				write(table, "", alternateRow);
                write(table, scr.get().getClientStatus().getCode(), alternateRow);
				write(table, "", alternateRow);
				write(table, DateUtils.getDateFormatted(scr.get().getLastContactDate()), alternateRow);
				write(table, "", alternateRow);
				write(table, scr.get().getStatusComments(), alternateRow);

				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);
				write(table, " ", alternateRow);

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
            new ClientStatusReport().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
