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

import com.arahant.beans.CompanyBase;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.ProspectLog;
import com.arahant.business.BProspectCompany;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ProspectLogReport extends ReportBase {

	public ProspectLogReport() {
		super("CliCon", "Prospect Log Report", true);
	}

	
	public String build (String id)
	{
		try {
			
			BProspectCompany bc=new BProspectCompany(id);

            PdfPTable table = new PdfPTable(1);
	       
	        writeHeaderLine("Prospect", bc.getName());
	        
	        addTable(table);
            addHeaderLine();


			List <ProspectLog> l =hsu.createCriteria(ProspectLog.class)
				.orderByDesc(ProspectLog.CONTACT_DATE)
				.joinTo(ProspectLog.ORG_GROUP)
				.joinTo(OrgGroup.OWNINGCOMPANY)
				.eq(CompanyBase.ORGGROUPID, id)
				.list();
			
			


            table = makeTable(new int[]{10,10,40,20,20});

            writeColHeader(table, "Date");
			writeColHeader(table, "Time");
            writeColHeader(table, "Log");
			writeColHeader(table, "Employees");
			writeColHeader(table, "Contacts");
            
            boolean alternateRow = true;

            for (ProspectLog pl : l) {
                // toggle the alternate row
                alternateRow = !alternateRow;

				write(table, DateUtils.getDateFormatted(pl.getContactDate()), alternateRow);
				write(table, DateUtils.getTimeFormatted(pl.getContactTime()), alternateRow);
				write(table, pl.getContactTxt(), alternateRow);
				write(table, pl.getEmployees(), alternateRow);
				write(table, pl.getProspectEmployees(), alternateRow);
						
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
