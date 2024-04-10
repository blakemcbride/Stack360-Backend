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

import com.arahant.beans.PaySchedule;
import com.arahant.beans.PaySchedulePeriod;
import com.arahant.business.BPaySchedule;
import com.arahant.business.BPaySchedulePeriod;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class PayScheduleReport extends ReportBase {
	public PayScheduleReport() throws ArahantException {
        super("pay", "Pay Schedule", true);
    }

    public String build(String id, int start, int end) throws DocumentException {

        try {

            PdfPTable table;
			
			BPaySchedule bps=new BPaySchedule(id);
			
			writeHeaderLine("Payroll Name", bps.getName());
			writeHeaderLine("Payroll Description", bps.getDescription());
			
			if (start!=0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(start));
			
			if (end!=0)
				writeHeaderLine("End Date", DateUtils.getDateFormatted(end));

            addHeaderLine();


            List<PaySchedulePeriod> l = hsu.createCriteria(PaySchedulePeriod.class)
					.dateBetween(PaySchedulePeriod.PAY_DATE, start, end)
					.orderBy(PaySchedulePeriod.PAY_DATE)
					.joinTo(PaySchedulePeriod.PAY_SCHEDULE)
					.eq(PaySchedule.ID, id)
					.list();
			

            table = makeTable(new int[]{33,33,34});

            writeColHeader(table, "First Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Last Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Pay Date", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for  (PaySchedulePeriod period : l) {

				BPaySchedulePeriod bpsp=new BPaySchedulePeriod(period);
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, DateUtils.getDateFormatted(bpsp.getStartDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(bpsp.getEndDate()), alternateRow);
                write(table, DateUtils.getDateFormatted(bpsp.getPayDate()), alternateRow);

            }
            
            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

}
