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
 * Created on Jan 18, 2008
 * 
 */
package com.arahant.reports;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 * 
 *
 * Created on Jan 18, 2008
 *
 */
public class HRDependeeReport extends ReportBase {
	public HRDependeeReport() throws ArahantException {
		super("HrDep","HR Dependent Sponsor Report");
	}
	
	/**
	 * @return
	 * @throws ArahantException 
	 */
	public String build(final BPerson person, final BHREmplDependent[] dependees) throws ArahantException {
		try { 
			PdfPTable table = null;
			boolean alternateRow = true;
			
			this.writeHeaderLine("Dependent Name", person.getNameLFM());
			this.writeHeaderLine("Dependent SSN", person.getSsn());
			this.addHeaderLine();
			
			table = makeTable(new int[] {30, 17, 30, 23 });
			writeColHeader(table, "Sponsor Name", Element.ALIGN_LEFT);
			writeColHeader(table, "Sponsor SSN", Element.ALIGN_LEFT);
			writeColHeader(table, "Dependent's Relationship", Element.ALIGN_LEFT);
			writeColHeader(table, "Dependency Status", Element.ALIGN_LEFT);
			table.setHeaderRows(1);
			
			for (BHREmplDependent dependee : dependees) {
				alternateRow = !alternateRow;
				
				writeLeft(table, dependee.getEmployeeNameLFM(), alternateRow);
				writeLeft(table, dependee.getEmployeeSSN(), alternateRow);
				writeLeft(table, dependee.getTextRelationship(), alternateRow);
				writeLeft(table, dependee.getStatus(), alternateRow);
			}
			
			this.addTable(table);
		} catch (final Exception e)	{
			throw new ArahantException(e);
		} finally {
            close();
        }
		
		return getFilename();
	}
}

	
