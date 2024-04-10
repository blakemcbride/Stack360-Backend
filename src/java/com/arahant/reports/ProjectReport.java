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

import com.arahant.beans.OrgGroup;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectType;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;

import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class ProjectReport extends ReportBase {

    public ProjectReport() {
        super("proj", "Projects");
    }


    public String build(String projectName, String summary, String extRef, String requestEntityId, String categoryId,
                        String typeId, String statusId, int startDate, int endDate,
                        boolean showStatus, boolean showCategory, int statusType
    ) throws DocumentException {

        try {

            PdfPTable table;

            addHeaderLine();

            HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class);


            hcu.orderBy(Project.PROJECTNAME);


            hcu.like(Project.REFERENCE, extRef);
            hcu.like(Project.DESCRIPTION, summary);

            if (!isEmpty(categoryId))
                hcu.joinTo(Project.PROJECTCATEGORY).eq(ProjectCategory.PROJECTCATEGORYID, categoryId);

            switch (statusType) {
                case 3:
                    hcu.eq(Project.PROJECTSTATUS, hsu.get(ProjectStatus.class, statusId));
                    break;
                case 1:
                    hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');
                    break;
                case 2:
                    hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'N');
                    break;

            }

            if (!isEmpty(typeId))
                hcu.joinTo(Project.PROJECTTYPE).eq(ProjectType.PROJECTTYPEID, typeId);

            if (!isEmpty(requestEntityId))
                hcu.joinTo(Project.REQUESTING_ORG_GROUP)
                        .in(OrgGroup.ORGGROUPID, new BOrgGroup(requestEntityId).getAllGroupsForCompany());

            if (!isEmpty(projectName))
                hcu.like(Project.PROJECTNAME, projectName);

            if (startDate > 0)
                hcu.ge(Project.DATEREPORTED, startDate);

            if (endDate > 0)
                hcu.le(Project.DATEREPORTED, endDate);


            HibernateScrollUtil<Project> scr = hcu.scroll();

            int count = 0;

            int cols = 5;

            table = makeTable(new int[]{10, 35, 13, 15, 27});

            writeColHeader(table, "ID", Element.ALIGN_LEFT);
            writeColHeader(table, "Summary", Element.ALIGN_LEFT);
            writeColHeader(table, "Created", Element.ALIGN_LEFT);
            writeColHeader(table, "Reference", Element.ALIGN_LEFT);
            writeColHeader(table, "Requesting Group", Element.ALIGN_LEFT);

            boolean alternateRow = true;

            while (scr.next()) {
                count++;

                // toggle the alternate row
                alternateRow = !alternateRow;

                Project p = scr.get();
                write(table, p.getProjectName().trim(), alternateRow);
                write(table, p.getDescription(), alternateRow);
                write(table, DateUtils.getDateFormatted(p.getDateReported()), alternateRow);
                write(table, p.getReference(), alternateRow);
                write(table, p.getRequestingOrgGroup().getName(), alternateRow);
            }

            scr.close();
            addTable(table);

            table = makeTable(new int[]{100});
            write(table, "Total: " + count);

            addTable(table);

        } finally {
            close();

        }

        return getFilename();
    }
}
