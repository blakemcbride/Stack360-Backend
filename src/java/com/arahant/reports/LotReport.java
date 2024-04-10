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
import com.arahant.business.BLot;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class LotReport extends ReportBase {

    public LotReport() throws ArahantException {
        super("Lot", "Lots", true);
    }
//in.getLotNumber(),in.getOrgGroupId(),in.getProductId(),in.getSerialNumber()
    public String build(String lotNumber, String orgGroupId, String productId, String serialNumber) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();


            HibernateCriteriaUtil<Lot> hcu = ArahantSession.getHSU().createCriteria(Lot.class)
				.orderByDesc(Lot.DATE_RECEIVED);

			hcu.like(Lot.LOT_NUMBER, lotNumber);

			HibernateCriteriaUtil ihcu=hcu.joinTo(Lot.ITEMS);

			if(!isEmpty(orgGroupId))
				ihcu.joinTo(Item.LOCATION).eq(OrgGroup.ORGGROUPID, orgGroupId);

			if (!isEmpty(productId))
				ihcu.joinTo(Item.PRODUCT).eq(Product.PRODUCTID,productId);

			if (!isEmpty(serialNumber))
				ihcu.eq(Item.SERIAL_NUMBER,serialNumber);

            HibernateScrollUtil<Lot> scr = hcu.scroll();

            int count = 0;


            table = makeTable(new int[]{10, 3, 15, 3, 10, 3, 10, 3, 30, 3, 30});

            writeColHeader(table, "Lot Number", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Recieved", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Original Quantity", Element.ALIGN_RIGHT);
            write(table,"",false);
            writeColHeader(table, "Cost", Element.ALIGN_RIGHT);
            write(table,"",false);
            writeColHeader(table, "Location", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Product", Element.ALIGN_LEFT);
            boolean alternateRow = true;

            while (scr.next()) {
                count++;

                // toggle the alternate row
                alternateRow = !alternateRow;

		BLot blot=new BLot(scr.get());

                write(table, scr.get().getLotNumber(), alternateRow);
                write(table,"",alternateRow);
                write(table, DateUtils.getDateFormatted(scr.get().getDateReceived()), alternateRow);
                write(table,"",alternateRow);
                write(table, scr.get().getOriginalQuantity(), alternateRow);
                write(table,"",alternateRow);
                write(table, MoneyUtils.formatMoney(scr.get().getLotCost()), alternateRow);
                write(table,"",alternateRow);
                write(table, blot.getOrgGroupName(), alternateRow);
                write(table,"",alternateRow);
                write(table, blot.getProductDescription(), alternateRow);

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
}

