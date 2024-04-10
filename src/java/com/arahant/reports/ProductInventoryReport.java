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
import com.arahant.beans.ProductType;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BProduct;
import com.arahant.business.BProductType;
import com.arahant.business.BVendorCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class ProductInventoryReport extends ReportBase {

	public ProductInventoryReport() throws ArahantException {
		super("ProdInv", "Product Inventory", true);
	}

	public String build(String lotNumber, String orgGroupId, String productId, String[] productTypeIds, String serialNumber, String[] vendorIds, int productAvailable) throws DocumentException {

		try {

			PdfPTable table;

			if (!isEmpty(lotNumber))
				writeHeaderLine("Lot Number", lotNumber);
			if (!isEmpty(orgGroupId))
				writeHeaderLine("Location", new BOrgGroup(orgGroupId).getName());
			if (!isEmpty(productId))
				writeHeaderLine("Product", new BProduct(productId).getDescription());
			if (vendorIds.length>0)
			{
				String vends="";
				for (String id : vendorIds)
					vends+=new BVendorCompany(id).getName()+", ";
				vends=vends.substring(0, vends.length()-2);
				writeHeaderLine("Vendors",vends);
			}
			if (!isEmpty(serialNumber))
				writeHeaderLine("Serial Number", serialNumber);

			if (productTypeIds.length>0)
			{
				String pts="";
				for (String id : productTypeIds)
					pts+=new BProductType(id).getDescription()+", ";
				pts=pts.substring(0, pts.length()-2);
				writeHeaderLine("Vendors",pts);
			}

			switch (productAvailable)
			{
				case 1 : writeHeaderLine("Product Availability" , "Available");
					break;
				case 2 : writeHeaderLine("Product Availability" , "Not Available");
					break;
			}



			addHeaderLine();

			HibernateCriteriaUtil<Item> hcu = ArahantSession.getHSU().createCriteria(Item.class);

	//		HibernateCriteriaUtil detHcu = hcu.joinTo(Inventory.INVENTORY_DETAILS);
			HibernateCriteriaUtil prodHcu = hcu.joinTo(Item.PRODUCT);
			HibernateCriteriaUtil locHcu = hcu.joinTo(Item.LOCATION);




			if (!isEmpty(orgGroupId)) {
				locHcu.eq(OrgGroup.ORGGROUPID, orgGroupId);
			}

			switch (productAvailable) {
				case 1:
					prodHcu.dateInside(Product.AVAILABLE_DATE, Product.TERM_DATE, DateUtils.now());
					break;

				case 2:
					prodHcu.dateOutside(Product.AVAILABLE_DATE, Product.TERM_DATE, DateUtils.now());
					break;
			}

			if (!isEmpty(productId)) {
				prodHcu.eq(Product.PRODUCTID, productId);
			}

			if (productTypeIds.length > 0) {
				prodHcu.joinTo(Product.PRODUCT_TYPE).in(ProductType.PRODUCT_TYPE_ID, productTypeIds);
			}

			if (!isEmpty(serialNumber)) {
				hcu.eq(Item.SERIAL_NUMBER, serialNumber);

			if (!isEmpty(lotNumber))
				hcu.joinTo(Item.LOT).eq(Lot.LOT_NUMBER, lotNumber);
			}

			if (vendorIds.length > 0) {
				prodHcu.joinTo(Product.VENDOR).in(VendorCompany.ORGGROUPID, vendorIds);
			}

			prodHcu.orderBy(Product.DESCRIPTION);


			HibernateScrollUtil<Item> scr = hcu.scroll();

			int count = 0;


			table = makeTable(new int[]{34,33,33});

			writeColHeader(table, "Description", Element.ALIGN_LEFT);
			writeColHeader(table, "Location", Element.ALIGN_LEFT);
			//writeColHeader(table, "Reorder Level", Element.ALIGN_RIGHT);
			writeColHeader(table, "Remaining Quantity", Element.ALIGN_RIGHT);

			boolean alternateRow = true;

			while (scr.next()) {
				count++;

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, scr.get().getProduct().getDescription(), alternateRow);
				write(table, scr.get().getLocation().getName(), alternateRow);
			//	write(table, scr.get().getReorderLevel(), alternateRow);
				write(table, scr.get().getQuantity(), alternateRow);

			}

			scr.close();
			addTable(table);

			table = makeTable(new int[]{100});

			write(table, "Total: " + count);

			addTable(table);

		} finally {
			close();

		}

		return getFilename();
	}
}
