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

import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectEmployeeJoin;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectType;
import com.arahant.business.BOrgGroup;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Arrays;

public class ProjectListReport extends ReportBase {

	public ProjectListReport() {
		super("plr", "Project Report");
	}

	public String build(boolean includeName, boolean includeDescription, boolean includeDateReported,
						boolean includeReference, boolean includeRequestingCompany, boolean includeCategory,
						boolean includeType, boolean includeStatus, boolean includeAssignedOrgGroup,
						boolean includeAssignedPerson, boolean sortAsc, int sortType, String[] categoryIds,
						String[] typeIds, String[] statusIds, int statusType, String requestingCompanyId) throws DocumentException, Exception {
		throw new ArahantDeleteException("XXYY");
		/*  XXYY
		try {


			HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class);

			HibernateCriteriaUtil catHcu = hcu.joinTo(Project.PROJECTCATEGORY);
			if (categoryIds.length > 0)
				catHcu.in(ProjectCategory.PROJECTCATEGORYID, categoryIds);
			HibernateCriteriaUtil typeHcu = hcu.joinTo(Project.PROJECTTYPE);
			if (typeIds.length > 0)
				typeHcu.in(ProjectType.PROJECTTYPEID, typeIds);
			HibernateCriteriaUtil statHcu = hcu.joinTo(Project.PROJECTSTATUS);
			HibernateCriteriaUtil orgHcu = hcu.joinTo(Project.REQUESTING_ORG_GROUP);
			if (!isEmpty(requestingCompanyId))
				orgHcu.in(OrgGroup.ORGGROUPID, new BOrgGroup(requestingCompanyId).getAllGroupsForCompany());


			switch (statusType) {
				case 1:
					statHcu.eq(ProjectStatus.ACTIVE, 'Y');
					break;
				case 2:
					statHcu.eq(ProjectStatus.ACTIVE, 'N');
					break;
				case 3:
					statHcu.in(ProjectStatus.PROJECTSTATUSID, statusIds);
					break;
			}

			if (sortAsc) {
				switch (sortType) {
					case 1:
						hcu.orderBy(Project.PROJECTNAME);
						break;
					case 2:
						hcu.orderBy(Project.DESCRIPTION);
						break;
					case 3:
						hcu.orderBy(Project.DATEREPORTED);
						break;
					case 4:
						hcu.orderBy(Project.REFERENCE);
						break;
					case 5:
						orgHcu.joinTo(OrgGroup.OWNINGCOMPANY).orderBy(OrgGroup.NAME);
						break;
					case 6:
						catHcu.orderBy(ProjectCategory.CODE);
						break;
					case 7:
						typeHcu.orderBy(ProjectType.CODE);
						break;
					case 8:
						statHcu.orderBy(ProjectStatus.CODE);
						break;
					case 9:
						hcu.joinTo(Project.ORGGROUP).orderBy(OrgGroup.NAME);
						break;
					case 10:
						hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN)
								.joinTo(ProjectEmployeeJoin.PERSON)
								.orderBy(Person.LNAME)
								.orderBy(Person.FNAME)
								.orderBy(Person.MNAME);
						break;
				}
			} else {
				switch (sortType) {
					case 1:
						hcu.orderByDesc(Project.PROJECTNAME);
						break;
					case 2:
						hcu.orderByDesc(Project.DESCRIPTION);
						break;
					case 3:
						hcu.orderByDesc(Project.DATEREPORTED);
						break;
					case 4:
						hcu.orderByDesc(Project.REFERENCE);
						break;
					case 5:
						orgHcu.joinTo(OrgGroup.OWNINGCOMPANY).orderByDesc(OrgGroup.NAME);
						break;
					case 6:
						catHcu.orderByDesc(ProjectCategory.CODE);
						break;
					case 7:
						typeHcu.orderByDesc(ProjectType.CODE);
						break;
					case 8:
						statHcu.orderByDesc(ProjectStatus.CODE);
						break;
					case 9:
						hcu.joinTo(Project.ORGGROUP).orderByDesc(OrgGroup.NAME);
						break;
					case 10:
						hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN)
								.joinTo(ProjectEmployeeJoin.PERSON)
								.orderByDesc(Person.LNAME).orderByDesc(Person.FNAME).orderByDesc(Person.MNAME);
						break;
				}
			}


			int cols = 0;

			if (includeName)
				cols++;
			if (includeDescription)
				cols++;
			if (includeDateReported)
				cols++;
			if (includeReference)
				cols++;
			if (includeRequestingCompany)
				cols++;
			if (includeCategory)
				cols++;
			if (includeType)
				cols++;
			if (includeStatus)
				cols++;
			if (includeAssignedOrgGroup)
				cols++;
			if (includeAssignedPerson)
				cols++;


			int[] sizes = new int[cols];
			Arrays.fill(sizes, 100 / cols);

			if (cols > 4)
				resetLandscape();

			PdfPTable table;

			addHeaderLine();

			HibernateScrollUtil<Project> scr = hcu.scroll();

			int count = 0;

			table = makeTable(sizes);

			if (includeName)
				writeColHeader(table, "ID", Element.ALIGN_LEFT);
			if (includeDescription)
				writeColHeader(table, "Summary", Element.ALIGN_LEFT);
			if (includeDateReported)
				writeColHeader(table, "Date Reported", Element.ALIGN_LEFT);
			if (includeReference)
				writeColHeader(table, "Reference", Element.ALIGN_LEFT);
			if (includeRequestingCompany)
				writeColHeader(table, "Requester", Element.ALIGN_LEFT);
			if (includeCategory)
				writeColHeader(table, "Category", Element.ALIGN_LEFT);
			if (includeType)
				writeColHeader(table, "Type", Element.ALIGN_LEFT);
			if (includeStatus)
				writeColHeader(table, "Status", Element.ALIGN_LEFT);
			if (includeAssignedOrgGroup)
				writeColHeader(table, "Assigned Group", Element.ALIGN_LEFT);
			if (includeAssignedPerson)
				writeColHeader(table, "Assigned Person", Element.ALIGN_LEFT);


			boolean alternateRow = true;

			while (scr.next()) {
				count++;

				// toggle the alternate row
				alternateRow = !alternateRow;

				Project p = scr.get();

				if (includeName)
					write(table, p.getProjectName(), alternateRow);
				if (includeDescription)
					write(table, p.getDescription(), alternateRow);
				if (includeDateReported)
					write(table, DateUtils.getDateFormatted(p.getDateReported()), alternateRow);
				if (includeReference)
					write(table, p.getReference(), alternateRow);
				BOrgGroup borg = new BOrgGroup(p.getRequestingOrgGroup());
				if (includeRequestingCompany)
					write(table, borg.getCompanyName(), alternateRow);
				if (includeCategory)
					write(table, p.getProjectCategory().getCode(), alternateRow);
				if (includeType)
					write(table, p.getProjectType().getCode(), alternateRow);
				if (includeStatus)
					write(table, p.getProjectStatus().getCode(), alternateRow);
				if (includeAssignedOrgGroup)
					write(table, (p.getOrgGroup() == null) ? "" : p.getOrgGroup().getName(), alternateRow);
				if (includeAssignedPerson) {
					StringBuilder emp = new StringBuilder();
					boolean first = true;
					for (ProjectEmployeeJoin pej : p.getProjectEmployeeJoins()) {
						if (first)
							first = false;
						else
							emp.append("; ");
						emp.append(pej.getPerson().getNameLFM())/*+" "+pej.getPersonPriority()* XXYY /;
					}
					write(table, emp.toString(), alternateRow);
				}
			}

			scr.close();
			addTable(table);
			table = makeTable(100);

			write(table, "Total: " + count);

			addTable(table);

		} finally {
			close();

		}

		return getFilename();

		 */

	}
}
