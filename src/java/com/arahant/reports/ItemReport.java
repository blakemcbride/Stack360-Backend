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

import com.arahant.beans.Item;
import com.arahant.beans.Lot;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Product;
import com.arahant.business.BItem;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class ItemReport extends ReportBase {

    public ItemReport() throws ArahantException {
        super("Lot", "Items", true);
    }

    public String build(String lotId,String orgGroupId,String productId, String serialNumber) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class)
					.like(Item.SERIAL_NUMBER, serialNumber);

			if (!isEmpty(productId))
				hcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID, productId).orderBy(Product.DESCRIPTION);
			else
				hcu.joinTo(Item.PRODUCT).orderBy(Product.DESCRIPTION);


			if (!isEmpty(orgGroupId))
				hcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, orgGroupId).orderBy(OrgGroup.NAME);
			else
				hcu.joinTo(Item.LOCATION).orderBy(OrgGroup.NAME);

			if (!isEmpty(lotId))
				hcu.joinTo(Item.LOT).eq(Lot.LOT_ID, lotId);



            HibernateScrollUtil<Item> scr = hcu.scroll();

            int count = 0;


            table = makeTable(new int[]{20, 3, 13, 3, 10, 3, 33, 3, 36});

            writeColHeader(table, "Serial Number", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Lot Number", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Quantity", Element.ALIGN_RIGHT);
            write(table,"",false);
            writeColHeader(table, "Location", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Product", Element.ALIGN_LEFT);


            boolean alternateRow = true;

            while (scr.next()) {
                count++;

                // toggle the alternate row
                alternateRow = !alternateRow;

                BItem bi=new BItem(scr.get());

                write(table, bi.getSerialNumber(), alternateRow);
                write(table,"",alternateRow);
                write(table, bi.getLotNumber(), alternateRow);
                write(table,"",alternateRow);
                write(table, bi.getQuantity(), alternateRow);
                write(table,"",alternateRow);
                write(table, bi.getLocation().getName(), alternateRow);
                write(table,"",alternateRow);
		write(table, bi.getProductDescription(), alternateRow);




            }

            scr.close();
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
            new Cobras().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

