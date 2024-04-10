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

import com.arahant.business.BReceipt;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class EmployeeBillingAdjustmentReport extends ReportBase {

	
    public EmployeeBillingAdjustmentReport() throws ArahantException {
        super("report", "Employee Adjustments", true);
    }

    public String getReport(int fromDate, int toDate, String personId, String[] ids) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			BReceipt []arr=BReceipt.searchAdjustments(fromDate, toDate, personId, 0);

            table = makeTable(new int[]{ 20, 20, 20, 25});

            writeColHeader(table, "Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Amount", Element.ALIGN_RIGHT);
            writeColHeader(table, "Invoice ID", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            for (BReceipt inv : arr) {

               // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, DateUtils.getDateFormatted(inv.getDate()), alternateRow);
                write(table, MoneyUtils.formatMoney(inv.getAmount()), alternateRow);
                write(table, inv.getInvoiceId(), alternateRow);
                write(table, inv.getDescription(), alternateRow);

            }
            

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }


}
