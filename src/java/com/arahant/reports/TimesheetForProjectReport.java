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

import com.arahant.business.BProject;
import com.arahant.business.BTimesheet;
import com.arahant.utils.DateUtils;
import com.arahant.utils.Formatting;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 */
public class TimesheetForProjectReport extends ReportBase {

	public TimesheetForProjectReport() {
		super("tmrpt", "Timesheets for Project", true);
	}

	public String build(String projectId, boolean approved, boolean invoiced, boolean notApproved,
			int startDate, int finalDate) throws DocumentException {
		try {

			String showLine = (approved?"Approved, ":"") ;
			showLine+=(notApproved?"NotApproved, ":"");
			showLine+=(invoiced?"Invoiced, ":"");

			if (showLine.length()>2)
				showLine=showLine.substring(0,showLine.trim().length()-1);

			writeHeaderLine("Show", showLine, 0);

			writeHeaderLine("Period", DateUtils.getDateFormatted(startDate) + " - " + DateUtils.getDateFormatted(finalDate), 0);

			BProject bp = new BProject(projectId);

			writeHeaderLine("Project", bp.getName().trim() + " - " + bp.getSummary().trim(), 0);

			addHeaderLine();

			PdfPTable table;

			boolean alternateRow = true;

			table = makeTable(new int[]{2,10,8,8,6,2,44,20});

					
			writeColHeader(table, "S", Element.ALIGN_LEFT);
			writeColHeader(table, "Date", Element.ALIGN_RIGHT);
			writeColHeader(table, "Begin", Element.ALIGN_RIGHT);
			writeColHeader(table, "End", Element.ALIGN_RIGHT);
			writeColHeader(table, "Hours", Element.ALIGN_RIGHT);
			writeColHeader(table, "B", Element.ALIGN_LEFT);
			writeColHeader(table, "Description", Element.ALIGN_LEFT);
			writeColHeader(table, "Employee", Element.ALIGN_LEFT);

			table.setHeaderRows(1);

			double totalHours=0;

			// spin through all exceptions passed in
			for (final BTimesheet t : BTimesheet.search(startDate, finalDate, notApproved, invoiced, approved, projectId, finalDate, null)) {

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, t.getState() + "", alternateRow);
				writeRight(table, DateUtils.getDateFormatted(t.getWorkDate()), alternateRow);
				writeRight(table, DateUtils.getTimeFormatted(t.getBeginningTime()), alternateRow);
				writeRight(table, DateUtils.getTimeFormatted(t.getEndTime()), alternateRow);
				writeRight(table, Formatting.formatNumber(t.getTotalHours(), 2) + "", alternateRow);
				write(table, t.getBillable() + "", alternateRow);
				write(table, t.getDescription(), alternateRow);
				write(table, t.getEmployeeNameLFM(), alternateRow);
				totalHours+=t.getTotalHours();
			}



			addTable(table);

			writeLine("Total Hours: "+Formatting.formatNumber(totalHours, 2));

		} finally {
			close();

		}

		return getFilename();
	}

	private String formatId(String value)
	{
		String value2 = "";
		boolean addZeros = false;
		boolean afterDash = false;
		for (int i = 0; i < value.length(); i++)
		{
			if(addZeros)
			{
				if(value.charAt(i) != '-')
				{
					value2 += value.charAt(i);
				}
				else
				{
					value2 += "-";
					addZeros = false;
					afterDash = true;
				}
			}
			else
			{
				if(value.charAt(i) != '0' && value.charAt(i) != '-')
				{
					value2 += value.charAt(i);
					addZeros = true;
					afterDash = false;
				}
				else if (value.charAt(i) == '-')
				{
					if(value2.length() == 0)
					{
						value2 = "0-";
						afterDash = true;
					}
				}
			}
		}
		if(afterDash)
		{
			value += "0";
		}
		return value2;
	}

	public static void main(String args[])
	{
		//(String projectId, boolean approved, boolean invoiced, boolean notApproved, int startDate, int finalDate)
		try
		{
			new TimesheetForProjectReport().build("00000-0000000001", true, true, true, 20050101, 20101201);
		}
		catch(Exception e)
		{
			
		}
	}
}
