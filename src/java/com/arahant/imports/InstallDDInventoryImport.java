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

package com.arahant.imports;


import com.arahant.beans.Product;
import com.arahant.business.BProduct;
import com.arahant.business.BProductType;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class InstallDDInventoryImport {

	HibernateSessionUtil hsu = ArahantSession.getHSU();

    public void importInventory(String filename) {
        try {
            DelimitedFileReader fr = new DelimitedFileReader(filename);
            //skip header line
            fr.nextLine();

			BProductType defaultType = BProductType.findOrMake("Inventory Default");

			hsu.flush();

			int count = 0;
			boolean doLine = false;
			HashMap<String,Product> addedItems = new HashMap<String, Product>();

            while (fr.nextLine()) {
                if (++count % 50 == 0) {
                    //System.out.println(count);
                    hsu.commitTransaction();
                    hsu.beginTransaction();
                }

				String itemName = fr.nextString();
				
				if(itemName.startsWith("LINE")) //all the products are below "Line 1", "Line 2", etc.
				{
					doLine = true;
					continue;
				}

				if(doLine)
				{
					if(StringUtils.isEmpty(itemName)) //if you reached an empty line, thats the end of that section of products
					{
						doLine = false;
						continue;
					}

					if(!addedItems.containsKey(itemName))
					{
						BProduct newProduct = new BProduct();
						newProduct.create();
						newProduct.setDescription(itemName);
						newProduct.setProductTypeId(defaultType.getId());
						newProduct.setVendorId("00001-0000000002");
						newProduct.setCost(0);
						newProduct.setWholesalePrice(0);
						newProduct.setRetailPrice(0);
						newProduct.setAvailabilityDate(0);
						newProduct.setTermDate(0);
						newProduct.setManHours(0);
						newProduct.setSellAsType("P");
						//System.out.println(itemName);
						newProduct.insert();

						addedItems.put(itemName, newProduct.getBean());
						System.out.println("Added item " + itemName);
					}
					else
					{
						System.out.println("Duplicate for " + itemName);
					}
				}

			}
        } catch (IOException ex) {
            Logger.getLogger(InstallDDInventoryImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(InstallDDInventoryImport.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public static void main(String args[]) {
        InstallDDInventoryImport imp = new InstallDDInventoryImport();
        try {

            imp.hsu = ArahantSession.getHSU();
            imp.hsu.dontAIIntegrate();
            imp.hsu.setCurrentPersonToArahant();

			//imp.hsu.setCurrentCompany(new BCompany("00001-0000072679").getBean());

			imp.importInventory("/Users/arahant/Desktop/installDDImport.csv");

            imp.hsu.commitTransaction();
        } catch (Exception ex) {
            imp.hsu.rollbackTransaction();
            ex.printStackTrace();
            Logger.getLogger(ProspectImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
