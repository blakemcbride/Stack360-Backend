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


package com.arahant.reports;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.arahant.business.BBillingBatch;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class BillingBatchReport extends ReportBase {

	public BillingBatchReport() {
		super("AppLoc", "Billing Batch Report");
	}

	public String build ()
	{
		try {
			PdfPTable table = new PdfPTable(1);
	      
			
	        addTable(table);
            addHeaderLine();


			BBillingBatch [] jobs=BBillingBatch.list();
			

            table = makeTable(new int[]{40,30,30});

            
			writeColHeader(table, "Name");
			writeColHeader(table, "Count");
			writeColHeader(table, "Amount if done now");
			
            boolean alternateRow = true;

            for (BBillingBatch cc : jobs) {


                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, cc.getDescription(), alternateRow);
				write(table, cc.getPersonCount(), alternateRow);
				write(table, MoneyUtils.formatMoney(cc.getAmountIfPostedNow()), alternateRow);

            }
            

            addTable(table);

        } 
		catch (Exception e)
		{
			throw new ArahantException(e);
		}
		finally {
            close();

        }

        return getFilename();
	}
}
