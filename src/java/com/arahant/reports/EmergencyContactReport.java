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
 *
 */

package com.arahant.reports;

import com.arahant.business.BHrEmergencyContact;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


public class EmergencyContactReport extends ReportBase {

	public EmergencyContactReport() throws ArahantException {
        super("EmerConRep", "Emergency Contacts", true);
    }

	 public String build(String empId) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

			addColumnHeader("Name", Element.ALIGN_LEFT, 15);
			addColumnHeader("Relationship", Element.ALIGN_LEFT, 15);
			addColumnHeader("Home Phone", Element.ALIGN_LEFT, 15);
			addColumnHeader("Work Phone", Element.ALIGN_LEFT, 15);
			addColumnHeader("Cell Phone", Element.ALIGN_LEFT, 15);

            table = makeTable(getColHeaderWidths());

			writeColHeaders(table);

            boolean alternateRow = true;

			for (BHrEmergencyContact bec : BHrEmergencyContact.list(new BPerson(empId).getPerson()))
            {
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, bec.getName(), alternateRow);
                write(table, bec.getRelationship(), alternateRow);
                write(table, bec.getHomePhone(), alternateRow);
                write(table, bec.getWorkPhone(), alternateRow);
                write(table, bec.getCellPhone(), alternateRow);
            }

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }

	  public static void main(String args[]) {
        try {
            new EmergencyContactReport().build("00001-0000000093");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
