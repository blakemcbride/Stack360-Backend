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

import com.arahant.business.BPerson;
import com.arahant.business.BProspectCompany;
import com.arahant.business.BSearchMetaInput;
import com.arahant.business.BSearchOutput;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class SalesProspectReport extends ReportBase {

	public SalesProspectReport() {
		super("spr", "Prospects", true);
	}

	public String build(BSearchMetaInput searchMetaInput, String personId, int fromDate, int toDate, String []statusIds, String []sourceIds,
			boolean inactiveStatuses, String name) {
		try {
			PdfPTable table = new PdfPTable(1);
			
			BPerson salesPerson=new BPerson(personId);
			
			writeHeaderLine("Salesperson", salesPerson.getNameLFM());
	
			if (fromDate>0)
				writeHeaderLine("From", DateUtils.getDateFormatted(fromDate));
			if (toDate>0)
				writeHeaderLine("To", DateUtils.getDateFormatted(toDate));
	           
            addHeaderLine();
/*
			HibernateCriteriaUtil<ProspectCompany> hcu=hsu.createCriteria(ProspectCompany.class)
				//.orderBy(ProspectCompany.NAME)
				.dateBetween(ProspectCompany.DATE, fromDate, toDate);
			
			HibernateCriteriaUtil statHCU=hcu.joinTo(ProspectCompany.PROSPECT_STATUS);
		
			if (statusIds.length>0)
				statHCU.in(ProspectStatus.PROSPECT_STATUS_ID, statusIds);
			else
				if (inactiveStatuses==false)
					statHCU.eq(ProspectStatus.ACTIVE, 'Y');
			
			switch (sortType)
			{
				case 0 : hcu.orderBy(ProspectCompany.NAME);
						break;
				case 1 : statHCU.orderBy(ProspectStatus.CODE);//sort by status name
						break;
				case 2 : statHCU.orderBy(ProspectStatus.SEQ);//sort by status seq
						break;
			}
			
			if (sourceIds.length>0)
				hcu.joinTo(ProspectCompany.PROSPECT_SOURCE).in(ProspectSource.PROSPECT_SOURCE_ID, sourceIds);
			
			hcu.joinTo(ProspectCompany.SALESPERSON).eq(Employee.PERSONID, personId);
			*/
			
			BSearchOutput bSearchOutput = BProspectCompany.searchBySalesperson(searchMetaInput, personId, sourceIds, statusIds, fromDate, toDate, inactiveStatuses, name);
			BProspectCompany[] companies = (BProspectCompany[])bSearchOutput.getItems();
					
		//	HibernateScrollUtil <ProspectCompany> scr =hcu.scroll();
			

            table = makeTable(new int[]{25,13,21,10,10,22,16});

			writeColHeader(table, "Name");
			writeColHeader(table, "Date");
			writeColHeader(table, "Last Log");
			writeColHeader(table, "Status");
			writeColHeader(table, "Source");
			writeColHeader(table, "Contact");
			writeColHeader(table, "Phone");
			
            
            boolean alternateRow = true;

           // while (scr.next()) {
		   for (BProspectCompany bpc : companies)
		   {
                // toggle the alternate row
                alternateRow = !alternateRow;

				//ProspectCompany apt=scr.get();
				
				//BProspectCompany bpc=new BProspectCompany(apt);
				
				write(table, bpc.getName(), alternateRow);
				write(table, DateUtils.getDateFormatted(bpc.getFirstContactDate()), alternateRow);
				write(table, DateUtils.getDateTimeFormatted(bpc.getLastLogDate(), bpc.getLastLogTime()), alternateRow);
				write(table, bpc.getStatusCode(), alternateRow);
				write(table, bpc.getSourceCode(), alternateRow);
				write(table, bpc.getMainContactName(), alternateRow);
				write(table, bpc.getMainContactWorkPhone(), alternateRow);
				
            }
            
		//	scr.close();

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
