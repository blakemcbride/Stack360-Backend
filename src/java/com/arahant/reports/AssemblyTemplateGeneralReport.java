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

import com.arahant.business.BAssemblyTemplate;
import com.arahant.business.BAssemblyTemplateDetail;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

public class AssemblyTemplateGeneralReport extends ReportBase {

    public AssemblyTemplateGeneralReport() throws ArahantException {
        super("AssemblyTempGen", "General Assembly Template", true);
    }

    public String build(String id) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            BAssemblyTemplate at = new BAssemblyTemplate(id);

            table = makeTable(new int[]{40, 3, 15, 3, 12, 3, 12, 3, 40});

            writeColHeader(table, "Product", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Product SKU", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Quantity", Element.ALIGN_RIGHT);
            write(table,"",false);
            writeColHeader(table, "Track", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Additional Information", Element.ALIGN_LEFT);


            //TODO: print out assembly template info


            //now do his children
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

        write(table, indent + btd.getProductDescription(), alternateRow);
        write(table,"",alternateRow);
        write(table, btd.getProductSku(), alternateRow);
        write(table,"",alternateRow);
        write(table, btd.getQuantity(), alternateRow);
        write(table,"",alternateRow);
        write(table, btd.getTrackToItem() ?"Yes":"No", alternateRow);
        write(table,"",alternateRow);
        write(table, btd.getItemParticulars(), alternateRow);


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
            new AssemblyTemplateGeneralReport().build(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
