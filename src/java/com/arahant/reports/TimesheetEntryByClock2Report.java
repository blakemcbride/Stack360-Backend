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
import com.arahant.services.standard.time.timesheetEntryByClock.SearchTimesheetsRolledUpReturnItem;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 */
public class TimesheetEntryByClock2Report extends ReportBase {

	public TimesheetEntryByClock2Report() {
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

            table = makeTable(new int[]{50,50});
            writeColHeader(table, "Date", Element.ALIGN_LEFT);
            writeColHeader(table, "Elapsed Time", Element.ALIGN_LEFT);


			BTimesheet []tsa=BTimesheet.search(personId, startDate, endDate, 0);

			HashMap<Integer,SearchTimesheetsRolledUpReturnItem> map=new HashMap<Integer,SearchTimesheetsRolledUpReturnItem>();
			
			for (BTimesheet t : tsa)
			{
				if (t.getEndTime()==-1)
					continue;
				//for every day in timesheet
				for (int loop=t.getStartDate();loop<=t.getEndDate();loop=DateUtils.add(loop,1))
				{
					if (map.containsKey(loop))
					{
						SearchTimesheetsRolledUpReturnItem ri=map.get(loop);
						ri.setElapsedTime(ri.getElapsedTime()+t.getElapsedTime());
						ri.setElapsedTimeFormatted(BTimesheet.getSpan(ri.getElapsedTime()));
					}
					else
					{
						SearchTimesheetsRolledUpReturnItem i=new SearchTimesheetsRolledUpReturnItem();
						i.setDate(loop);
						i.setElapsedTime(t.getElapsedTime());
						i.setElapsedTimeFormatted(t.getElapsedTimeFormatted());
						map.put(loop,i);
						
					}
				}
			}
			
			ArrayList <SearchTimesheetsRolledUpReturnItem> al=new ArrayList<SearchTimesheetsRolledUpReturnItem>(map.values().size());

			al.addAll(map.values());

			Collections.sort(al);
			
			
            boolean alternateRow = true;

            for (SearchTimesheetsRolledUpReturnItem bt : al) {

                // toggle the alternate row
                alternateRow = !alternateRow;
                write(table, DateUtils.getDateFormatted(bt.getDate()), alternateRow);
                write(table, bt.getElapsedTimeFormatted(), alternateRow);

            }
            
            addTable(table);
			
			int masterTotal=0;
			for (SearchTimesheetsRolledUpReturnItem si : al)
				masterTotal+=BTimesheet.round(si.getElapsedTime());
			
			table=makeTable(new int[]{100});
			write(table,"Total "+BTimesheet.getSpan(masterTotal));
			
			addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

}
