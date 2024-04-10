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
 * Created on Dec 11, 2009
 *
 */
package com.arahant.reports;

import com.arahant.business.BScreen;
import com.arahant.business.BScreenGroup;
import com.arahant.business.BScreenOrGroup;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;

public class ScreenGroupSiteMap extends ReportBase {

    public ScreenGroupSiteMap() throws ArahantException {
        super("ScreenSiteMap", "Screen Group Site Map");
    }

    public String build(String id) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            table = makeTable(new int[]{100});

            doElement(table, new BScreenGroup(id), 0);

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    boolean alternateRow = true;

    private void doElement(PdfPTable table, BScreenGroup btd, int depth)
    {

        String indent = "";
        for (int i = 0; i < depth; i++)
            indent += "     ";

        write(table, indent + btd.getName());
        write(table,"");

        for (BScreenOrGroup dets : btd.listChildren())
        {
            if (dets instanceof BScreenGroup)
                doElement(table, (BScreenGroup)dets, depth + 1);
            else
            {
                indent = "";
                for (int i = 0; i <= depth; i++)
                    indent += "     ";
                write(table, indent + dets.getName());
                write(table,"");
            }
        }

    }

    public static void main(String args[]) {
        /*
         * Agency - 00001-0000000272
         * Agent - 00001-0000000287
         * Arahant - 00000-0000000000
         * DRC - 00001-0000000208
         * Employee - 00001-0000000260
         * Employer - 00001-0000000246
        */
        String id = "00000-0000000000";
        try {
            new ScreenGroupSiteMap().build(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
