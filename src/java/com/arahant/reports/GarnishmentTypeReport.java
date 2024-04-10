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

import com.arahant.business.BGarnishmentType;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class GarnishmentTypeReport extends ReportBase {
	
	public GarnishmentTypeReport() throws ArahantException {
        super("GarnTyp", "Garnishment Types", true);
    }

    public String build(int type) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			BGarnishmentType[] types=BGarnishmentType.list(type);

            table = makeTable(new int[]{25, 50, 25});

            writeColHeader(table, "Code", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Active Date", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (BGarnishmentType gt : types) {

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, gt.getCode(), alternateRow);
                write(table, gt.getDescription(), alternateRow);
                write(table, DateUtils.getDateFormatted(gt.getLastActiveDate()), alternateRow);

            }
            
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    public static void main(String args[]) {
        try {
            new GarnishmentTypeReport().build(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
