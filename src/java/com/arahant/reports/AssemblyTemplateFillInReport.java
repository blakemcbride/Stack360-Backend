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
 * Created on Dec 10, 2009
 *
 */
package com.arahant.reports;

import com.arahant.business.BAssemblyTemplate;
import com.arahant.business.BAssemblyTemplateDetail;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class AssemblyTemplateFillInReport extends ReportBase {

    public AssemblyTemplateFillInReport() throws ArahantException {
        super("AssemblyFillIn", "Assembly Template Fill In", true);
    }

    public String build(String id) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            BAssemblyTemplate at = new BAssemblyTemplate(id);

            table = makeTable(new int[]{30, 3, 10, 3, 10, 3, 10, 3, 25});

            writeColHeader(table, "Product", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "SKU", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Quantity", Element.ALIGN_RIGHT);
            write(table,"",false);
            writeColHeader(table, "Lot", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Location", Element.ALIGN_LEFT);

            for (BAssemblyTemplateDetail btd : getTemplateChildren(at))
                doElement(table, btd, 0);
            
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

    boolean alternateRow = true;

    private void doElement(PdfPTable table, BAssemblyTemplateDetail btd, int depth)
    {
        alternateRow = !alternateRow;

        String indent = "";
        for (int i = 0; i < depth; i++)
            indent += "  ";

        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");

        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");
        write(table, "");

        write(table, indent + btd.getProductDescription());
        write(table,"");
        write(table, btd.getProductSku());
        write(table,"");
        write(table, "___________");
        write(table,"");
        write(table, "___________");
        write(table,"");
        write(table, "_____________________________");

        for (BAssemblyTemplateDetail dets : getChildren(btd))
        {
            doElement(table, dets, depth + 1);
        }

    }

    private BAssemblyTemplateDetail[] getTemplateChildren(BAssemblyTemplate at) {
        return at.listChildren();
    }

    private BAssemblyTemplateDetail[] getChildren(BAssemblyTemplateDetail btd) {
        return btd.listChildren();
    }


    public static void main(String args[]) {
        String id = "00001-0000000004"; //, "00001-0000000001", "00001-0000000002", "00001-0000000003", "00001-0000000004", "00001-0000000005"};
        try {
            new AssemblyTemplateFillInReport().build(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


