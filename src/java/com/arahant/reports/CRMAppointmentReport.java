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

import com.arahant.business.BAppointment;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.SimpleDateFormat;

/**
 *
 */
public class CRMAppointmentReport extends ReportBase {

	public CRMAppointmentReport() {
		super("AppRpt", "Appointments",true);
	}

	

	public String build(String personId, int type, String companyId, int fromDate, int toDate, String apptType, String status)
	{
		try
		{
		
			BAppointment [] appts=BAppointment.search(fromDate,toDate,companyId,type,personId,apptType,status,0);
			
			if (fromDate>0)
				writeHeaderLine("From", DateUtils.getDateFormatted(fromDate));
			if (toDate>0)
				writeHeaderLine("To", DateUtils.getDateFormatted(toDate));
			if (!isEmpty(companyId))
				writeHeaderLine("With", new BOrgGroup(companyId).getName());
			if (!isEmpty(personId))
				writeHeaderLine("For", new BPerson(personId).getNameLFM());
			if (!isEmpty(status))
				writeHeaderLine("Status",BAppointment.getStatusName(status));
			if (!isEmpty(apptType))
				writeHeaderLine("Type", BAppointment.getTypeName(apptType));
			addHeaderLine();		
			
			addHeader("Day", 10);
			addHeader("Date", 10);
			addHeader("Time", 10);
			addHeader("Length", 10);
			if (isEmpty(apptType))
				addHeader("Type", 10);
			if (isEmpty(status))
				addHeader("Status", 10);
			if (isEmpty(companyId))
				addHeader("Company", 30);
			addHeader("Purpose", 30);
			
			PdfPTable table=makeTableFromHeader();
			
			boolean altColor=true;
			int lastDate=0;
			
			SimpleDateFormat sdf=new SimpleDateFormat("EEEEEEEEEE");
	
		
			for (BAppointment ap : appts)
			{
				if (ap.getDate()!=lastDate)
				{
					altColor=!altColor;
					lastDate=ap.getDate();
				}
				
				String day=sdf.format(DateUtils.getDate(ap.getDate()));
		
				if (ap.getDate()==DateUtils.now())
					day="Today";
					
				write(table,day,altColor);
				write(table,DateUtils.getDateFormatted(ap.getDate()),altColor);
				write(table,DateUtils.getTimeFormatted(ap.getTime()),altColor);
				write(table,(ap.getLength()/60)+" hours",altColor);
				if (isEmpty(apptType))
					write(table,ap.getTypeName(),altColor);
				if (isEmpty(status))
					write(table,ap.getStatusName(),altColor);
				if (isEmpty(companyId))
					write(table,ap.getCompanyName(),altColor);
				write(table,ap.getPurpose(),altColor);
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
