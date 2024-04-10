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
 * Created on Sep 13, 2007
 * 
 */
package com.arahant.reports;
import com.arahant.business.BProjectHistory;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;



/**
 * 
 *
 * Created on Sep 13, 2007
 *
 */
public class ProjectHistoryReport extends ReportBase {

	public ProjectHistoryReport() throws ArahantException {
		super("ProjHist","Project Routing and Assignment History Report", true, 350F);
	}
	
	public String build (final BProjectHistory []projHist) throws ArahantException
	{
		
		try
		{
			if (projHist.length==0)
				throw new ArahantWarning("This is no history for this project.");
			writeHeaderLine("Project", projHist[0].getProjectName());
			addHeaderLine();
			
			PdfPTable table;

			boolean alternateRow = true;

		    table = makeTable(new int[] { 15,22,15,15,15,18 });
		       
	        writeColHeader(table, "Status", Element.ALIGN_LEFT);
	        writeColHeader(table, "Assigned To", Element.ALIGN_LEFT);
	        writeColHeader(table, "Route Stop Organizational Group", Element.ALIGN_LEFT);
	        writeColHeader(table, "Route Stop Decision Point", Element.ALIGN_LEFT);
			writeColHeader(table, "Change Made By", Element.ALIGN_LEFT);
			writeColHeader(table, "Date", Element.ALIGN_LEFT);

	        table.setHeaderRows(1);
			
	    	// spin through all exceptions passed in
	        for (final BProjectHistory hist : projHist) {
	        		
	        	// toggle the alternate row
				alternateRow = !alternateRow;
	            
				write(table, hist.getToStatusCode(), alternateRow);
				write(table, hist.getToNames(),alternateRow);
				write(table, hist.getToRouteStopOrgGroupName(), alternateRow);
				write(table, hist.getToRouteStopName(), alternateRow);
				write(table, hist.getByNameLFM(), alternateRow);
				write(table, hist.getDateTimeFormatted(), alternateRow);
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
	
	public static void main(String[] args) {
		try {
			new ProjectHistoryReport().build(BProjectHistory.list("00001-0000013493"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

	
