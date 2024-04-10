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
import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashMap;

/**
 *
 */
public class TimesheetEntryByClock1Report extends ReportBase {

	public TimesheetEntryByClock1Report() {
		super("torep", "Timesheet Entry By Clock");
	}
	
	public String build(String personId, int startDate, int endDate) throws DocumentException {

        try {

            PdfPTable table;
			
			BPerson bp=new BPerson(personId);
			writeHeaderLine("For", bp.getNameLFM());
			
			if (startDate!=0)
				writeHeaderLine("Start Date", DateUtils.getDateFormatted(startDate));
			
			if (endDate!=0)
				writeHeaderLine("End Date", DateUtils.getDateFormatted(endDate));
			
            addHeaderLine();

            table = makeTable(new int[]{33,33,34});

            writeColHeader(table, "Date/Time In", Element.ALIGN_LEFT);
            writeColHeader(table, "Date/Time Out", Element.ALIGN_LEFT);
            writeColHeader(table, "Elapsed Time", Element.ALIGN_LEFT);


            boolean alternateRow = true;

			BTimesheet []tsar=BTimesheet.search(personId, startDate, endDate, 0);
            for (BTimesheet bt : tsar) {

                // toggle the alternate row
                alternateRow = !alternateRow;

				write(table, DateUtils.getDateTimeFormatted(bt.getWorkDate(), bt.getBeginningTime()), alternateRow);
                write(table, DateUtils.getDateTimeFormatted(bt.getEndDate(), bt.getEndTime()), alternateRow);
                write(table, bt.getElapsedTimeFormatted(), alternateRow);

            }
            
            addTable(table);
			
			//calculate timesheet totals by person by day with rounding rules
			
			HashMap<String,HashMap<Integer,Integer>> totals=new HashMap<String, HashMap<Integer, Integer>>();
			
			for (BTimesheet t : tsar)
			{
				if (!totals.containsKey(t.getPersonId()))
					totals.put(t.getPersonId(), new HashMap<Integer, Integer>());
				
				HashMap<Integer, Integer> dayTotals=totals.get(t.getPersonId());
				
				if (!dayTotals.containsKey(t.getWorkDate()))
					dayTotals.put(t.getWorkDate(), 0);
				
				dayTotals.put(t.getWorkDate(), dayTotals.get(t.getWorkDate())+t.getElapsedTime());
			}

			int masterTotal=0;
			
			for (String p : totals.keySet())
				for (int date : totals.get(p).keySet())
					masterTotal+=BTimesheet.round(totals.get(p).get(date));
			
			table=makeTable(new int[]{100});
			write(table,"Total "+BTimesheet.getSpan(masterTotal));
			
			addTable(table);
			

        } finally {
            close();

        }

        return getFilename();
    }

}
