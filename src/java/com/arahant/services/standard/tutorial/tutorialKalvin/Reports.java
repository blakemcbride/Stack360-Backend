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
package com.arahant.services.standard.tutorial.tutorialKalvin;

import com.arahant.exceptions.ArahantException;
import com.arahant.reports.ReportBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class Reports extends ReportBase {

    public HibernateSessionUtil hsu = ArahantSession.getHSU();
    public Reports() throws ArahantException {
        super("ReportFileName", "Report Title Goes Here, passed in from super", true);
    }

    public void printReport() {
        try {
            PdfPTable table;


            table = makeTable(new int[]{1});
            //written before other data, centered
            writeHeader(table, "Report headers 1");
            writeHeader(table, "Report headers 2 with some other stuff");
            addTable(table); //whenever you create a table, once the data is set, call addTable
            addHeaderLine();//add a solid line before writing other data below

            //create the table columns, percentage
            table = makeTable(new int[]{15, 22, 22, 22, 22, 19});

            //write column header for first page
            writeColHeader(table, "Paying SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Paying Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Covered Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Policy Start Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Policy End Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit Name", Element.ALIGN_LEFT);

            //addColumnHeader: this will print on page > 1
            addColumnHeader("Paying SSN", Element.ALIGN_LEFT, 15);
            addColumnHeader("Paying Name", Element.ALIGN_LEFT, 22);
            addColumnHeader("Covered Name", Element.ALIGN_LEFT, 22);
            addColumnHeader("Policy Start Date", Element.ALIGN_LEFT, 22);
            addColumnHeader("Policy End Date", Element.ALIGN_LEFT, 22);
            addColumnHeader("Benefit Name", Element.ALIGN_LEFT, 19);

            boolean alternateRow = true;
            //write several row, enough to go to new page
            for (int i = 0; i < 50; i++) {
                alternateRow = !alternateRow;   //toggle alternate row,highlights
                write(table, "123-45-6789", alternateRow);
                write(table, "I'm paying", alternateRow);
                write(table, "for him", alternateRow);
                writeRight(table, "20000101", alternateRow);
                write(table, "20100101", alternateRow);
                writeLeft(table, "Health", alternateRow);
            }
            writeFooter("this is the footer");
            setConfidential();

            addTable(table);

            //make a new table and print something else
            table = makeTable(new int[]{100});

            writeRight(table, "Total: " + 100, true);

            addTable(table);
            close();
        } catch (DocumentException ex) {
            Logger.getLogger(Reports.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    public static void main(String[] args) {
        Reports rpt = new Reports();
        rpt.printReport();

    }
}
