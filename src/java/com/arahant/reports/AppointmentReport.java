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

import com.arahant.business.BAppointment;
import com.arahant.business.BCompanyBase;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.SimpleDateFormat;

/**
 *
 */
public class AppointmentReport extends ReportBase {

	public AppointmentReport() {
		super("appt", "Appointments", true);
	}

	public String build(String id, int fromDate, int toDate, String type, String status, boolean attendeeOnly) {
		try {
			PdfPTable table = new PdfPTable(1);

			BCompanyBase comp = BCompanyBase.get(id);

			if (comp.getOrgGroupType() == ArahantConstants.CLIENT_TYPE) {
				writeHeaderLine("Client", comp.getName());
			} else {
				writeHeaderLine("Prospect", comp.getName());
			}
			if (fromDate > 0) {
				writeHeaderLine("From", DateUtils.getDateFormatted(fromDate));
			}
			if (toDate > 0) {
				writeHeaderLine("To", DateUtils.getDateFormatted(toDate));
			}
			if (!isEmpty(type)) {
				writeHeaderLine("Type", (type.charAt(0) == 'P') ? "Appointments" : "Arrangements");
			}
			if (!isEmpty(status)) {
				writeHeaderLine("Status", "Active");
			}
			if (attendeeOnly) {
				writeHeaderLine("For", ArahantSession.getCurrentPerson().getNameLFM());
			}
			addHeaderLine();

			BAppointment[] l = BAppointment.list(id, fromDate, toDate, type, status, attendeeOnly, 0);
			/*	HibernateCriteriaUtil<Appointment> hcu=hsu.createCriteria(Appointment.class)
			.orderBy(Appointment.DATE)
			.orderBy(Appointment.TIME);
			
			hcu.joinTo(Appointment.COMPANY).eq(CompanyBase.ORGGROUPID, id);
			hcu.ge(Appointment.DATE, fromDate);
			
			if (toDate>0)
			hcu.le(Appointment.DATE, toDate);
			
			HibernateScrollUtil <Appointment> scr =hcu.scroll();
			 */

			addHeader("Day", 10);
			addHeader("Date", 10);
			addHeader("Time", 10);
			addHeader("Length", 10);
			if (isEmpty(type)) {
				addHeader("Type", 10);
			}
			if (isEmpty(status)) {
				addHeader("Status", 10);
			}
			if (isEmpty(id)) {
				addHeader("Company", 30);
			}
			addHeader("Purpose", 30);

			table = makeTableFromHeader();


			boolean altColor = true;
			int lastDate=0;
			
			SimpleDateFormat sdf=new SimpleDateFormat("EEEEEEEEEE");

			//   while (scr.next()) {
			for (BAppointment ap : l) {
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
				if (isEmpty(type))
					write(table,ap.getTypeName(),altColor);
				if (isEmpty(status))
					write(table,ap.getStatusName(),altColor);
				if (isEmpty(id))
					write(table,ap.getCompanyName(),altColor);
				write(table,ap.getPurpose(),altColor);
			}

			//	scr.close();

			addTable(table);

		} catch (Exception e) {
			throw new ArahantException(e);
		} finally {
			close();

		}

		return getFilename();
	}
}
