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

import com.arahant.beans.Employee;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashMap;

/**
 *
 */
public class EmployeeTimesheetByClockReport extends ReportBase {

	public EmployeeTimesheetByClockReport() throws ArahantException {
		super("ETCR", "Employee Timesheet", true);
	}

	public String build(int startDate, int endDate, String orgGroupId) throws DocumentException {

		try {

			HibernateCriteriaUtil<Employee> hcu=hsu.createCriteria(Employee.class)
				.orderBy(Employee.LNAME).orderBy(Employee.FNAME);
			
			if (!isEmpty(orgGroupId))
				hcu.in(Employee.PERSONID, new BOrgGroup(orgGroupId).getAllPersonIdsForOrgGroupHierarchy(true));
			
			boolean first=true;
			
			HibernateScrollUtil <Employee> scr=hcu.scroll();
			while (scr.next())	
			{

				BEmployee bp = new BEmployee(scr.get());

				BTimesheet[] tsar = BTimesheet.search(bp.getPersonId(), startDate, endDate, 0);

				if (tsar.length==0 && bp.isActive()>0)
					continue;

				if (first)
					first=false;
				else
					newPage();
				
				PdfPTable table;

				
				writeHeaderLine("For", bp.getNameLFM());

				if (startDate != 0) {
					writeHeaderLine("Start Date", DateUtils.getDateFormatted(startDate));
				}
				if (endDate != 0) {
					writeHeaderLine("End Date", DateUtils.getDateFormatted(endDate));
				}
				addHeaderLine();

				table = makeTable(new int[]{33, 33, 34});

				writeColHeader(table, "Date/Time In", Element.ALIGN_LEFT);
				writeColHeader(table, "Date/Time Out", Element.ALIGN_LEFT);
				writeColHeader(table, "Elapsed Time", Element.ALIGN_LEFT);


				boolean alternateRow = true;

				
				for (BTimesheet bt : tsar) {

					// toggle the alternate row
					alternateRow = !alternateRow;

					write(table, DateUtils.getDateTimeFormatted(bt.getWorkDate(), bt.getBeginningTime()), alternateRow);
					write(table, DateUtils.getDateTimeFormatted(bt.getEndDate(), bt.getEndTime()), alternateRow);
					write(table, bt.getElapsedTimeFormatted(), alternateRow);

				}

				addTable(table);

				//calculate timesheet totals by person by day with rounding rules

				HashMap<String, HashMap<Integer, Integer>> totals = new HashMap<String, HashMap<Integer, Integer>>();

				for (BTimesheet t : tsar) {
					if (!totals.containsKey(t.getPersonId())) {
						totals.put(t.getPersonId(), new HashMap<Integer, Integer>());
					}
					HashMap<Integer, Integer> dayTotals = totals.get(t.getPersonId());

					if (!dayTotals.containsKey(t.getWorkDate())) {
						dayTotals.put(t.getWorkDate(), 0);
					}
					dayTotals.put(t.getWorkDate(), dayTotals.get(t.getWorkDate()) + t.getElapsedTime());
				}

				int masterTotal = 0;

				for (String p : totals.keySet()) {
					for (int date : totals.get(p).keySet()) {
						masterTotal += BTimesheet.round(totals.get(p).get(date));
					}
				}
				table = makeTable(new int[]{100});
				write(table, "Total " + BTimesheet.getSpan(masterTotal));

				addTable(table);
			}


		} finally {
			close();

		}

		return getFilename();
	}
}
