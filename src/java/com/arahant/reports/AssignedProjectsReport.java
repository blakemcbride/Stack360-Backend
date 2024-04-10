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

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.KissConnection;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import org.kissweb.database.*;
import org.kissweb.database.Record;

import java.util.ArrayList;
import java.util.List;

public class AssignedProjectsReport extends ReportBase {

    public AssignedProjectsReport() {
        super("Proj", "Assigned Projects");
    }

    public String build(Person person, String[] projectIds) {
        try {

            Connection db = KissConnection.get();

            PdfPTable table = new PdfPTable(1);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.setWidthPercentage(100F);
            table.setSpacingBefore(5F);

            writeHeader(table, "Assignee: " + person.getNameLFM());

            addTable(table);
            addHeaderLine();

            String sql = "select p.project_name, p.description, pej.person_priority " +
                    "from project_employee_join pej " +
                    "join project_shift shft " +
                    "  on pej.project_shift_id = shft.project_shift_id " +
                    "join project p " +
                    "  on shft.project_id = p.project_id " +
                    "join project_status ps " +
                    "  on p.project_status_id = ps.project_status_id " +
                    "where pej.person_id = ? " +
                    "      and ps.active = 'Y' ";
            ArrayList<Object> args = new ArrayList<>();
            args.add(person.getPersonId());
            if (projectIds.length > 0) {
                sql += "and p.project_id = ANY(?) ";
                args.add(projectIds);
            }
            sql += "order by pej.person_priority";
            List<Record> recs = db.fetchAll(sql, args);

            table = makeTable(new int[]{25, 15, 60});

            writeColHeader(table, "Project Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Priority", Element.ALIGN_LEFT);
            writeColHeader(table, "Description", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            for (Record rec : recs) {
                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, rec.getString("project_name"), alternateRow);
                write(table, rec.getShort("person_priority"), alternateRow);
                write(table, rec.getString("description"), alternateRow);
            }

            addTable(table);
        } catch (Exception e) {
            throw new ArahantException(e);
        } finally {
            close();
        }
        return getFilename();
    }

}
