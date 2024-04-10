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

import com.arahant.business.BPerson;
import com.arahant.business.BWagePaid;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class WagePaidReport extends ReportBase{

	 public WagePaidReport() throws ArahantException {
        super("Report", "Wages Paid", true);
    }

    public String build(String personId, int checkNumber, int fromDate, int toDate) throws DocumentException {

        try {

            PdfPTable table;

			writeHeaderLine("Employee", new BPerson(personId).getNameLFM());

            addHeaderLine();

            table = makeTable(new int[]{25,25,25,25});

            writeColHeader(table, "Check Number", Element.ALIGN_LEFT);
            writeColHeader(table, "Pay Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Payment Method", Element.ALIGN_LEFT);
            writeColHeader(table, "Total", Element.ALIGN_RIGHT);
            boolean alternateRow = true;

			BWagePaid [] warr=BWagePaid.search(personId,checkNumber, fromDate, toDate, 0);

            for (BWagePaid wp : warr) {

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, ""+wp.getCheckNumber(), alternateRow);
                write(table, DateUtils.getDateFormatted(wp.getPayDate()), alternateRow);
                write(table, wp.getPaymentMethodString(), alternateRow);
                write(table, MoneyUtils.formatMoney(wp.getTotalAmount()), alternateRow);

            }

            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }


	public static void main(String args[])
	{
		try
		{
			ArahantSession.getHSU().setCurrentPersonToArahant();
			new WagePaidReport().build("00001-0000000009", 0, 0, 0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
