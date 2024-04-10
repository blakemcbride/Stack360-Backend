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

import com.arahant.business.BProduct;
import com.arahant.business.BProductType;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class ProductTypeReport extends ReportBase {

	public ProductTypeReport() throws ArahantException {
        super("ProdType", "Products and Product Types", true);
    }

    public String build(String parentProductId) throws DocumentException {

        try {

            PdfPTable table;

			if (isEmpty(parentProductId))
				writeHeaderLine("Product Type", "Top Level");
			else
				writeHeaderLine("Product Type", new BProductType(parentProductId).getDescription());

            addHeaderLine();

			BProductType []types;
			BProduct []prods;

			if (isEmpty(parentProductId))
			{
				types=BProductType.getTopLevelTypes();
				prods=new BProduct[0];
			}
			else
			{
				BProductType pt=new BProductType(parentProductId);

				types=pt.getSubTypes();
				prods=pt.getProducts();
			}


            table = makeTable(new int[]{15, 3, 35, 3, 15, 3, 25});

            writeColHeader(table, "Type", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "SKU", Element.ALIGN_LEFT);
            write(table,"",false);
            writeColHeader(table, "Vendor", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (BProductType p : types) {

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, "Product Type", alternateRow);
                write(table,"",alternateRow);
                write(table, p.getDescription(), alternateRow);
                write(table,"",alternateRow);
                write(table,"",alternateRow);
                write(table,"",alternateRow);
                write(table,"",alternateRow);


            }

	    for (BProduct p : prods) {

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, "Product", alternateRow);
                write(table,"",alternateRow);
                write(table, p.getDescription(), alternateRow);
                write(table,"",alternateRow);
                write(table, p.getSku(), alternateRow);
                write(table,"",alternateRow);
                write(table, p.getVendorName(), alternateRow);

            }


            addTable(table);


        } finally {
            close();

        }

        return getFilename();
    }

}
