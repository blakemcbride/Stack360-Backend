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


/**
 * Created on Apr 10, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.business.BTimesheet;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Apr 10, 2007
 *
 */
public class UnpaidTimesheetReport extends ReportBase {

	public UnpaidTimesheetReport() throws ArahantException {
		super("UpdTm","Unpaid Time Report");
	}
	/*
	 * include employee f name, employee l name, date, hours, project name, project description

	 */
	public String getReport(final BTimesheet [] time) throws ArahantException
	{
		try
		{
			PdfPTable table;
			boolean alternateRow = true;
			
			table=makeTable(new int[]{30,10,10,20,30});
			
			writeCentered(table,"Name");
			writeCentered(table,"Date");
			writeCentered(table,"Hours");
			writeCentered(table,"Project");
			writeCentered(table,"Description");

			for (final BTimesheet element : time) {
				write(table,element.getEmployeeNameLFM(),alternateRow);
				write(table,DateUtils.getDateFormatted(element.getWorkDate()),alternateRow);
				write(table,element.getTotalHours(),alternateRow);
				write(table,element.getProjectName(),alternateRow);
				write(table,element.getProjectDescription(),alternateRow);
				
				alternateRow=!alternateRow;
			}
			
			addTable(table);
		}
		catch (final Exception e)
		{
			throw new ArahantException(e);
		}
		finally {
            close();
        }
		return getFilename();
	}
}

	
