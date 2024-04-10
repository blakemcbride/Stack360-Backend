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

import com.arahant.beans.CompanyBase;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.Person;
import com.arahant.beans.Project;
import com.arahant.beans.ProjectCategory;
import com.arahant.beans.ProjectEmployeeJoin;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.ProjectType;
import com.arahant.beans.RouteStop;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.Objects;

public class ProjectGroupReport extends ReportBase {

	public ProjectGroupReport() {
		super("projGrp", "Projects", true);
	}

	public String build(String orgGroupId, String extReference, int fromDate, int toDate,
			String personId, String []projectStatusId, String [] projectCategoryId,
			String []projectTypeId, String projectSummary, String companyId,
			boolean showAssigned) {

		throw new ArahantException("XXYY");

		/*

		try {

			PdfPTable table=new PdfPTable(1);


			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.setWidthPercentage(100F);
			table.setSpacingBefore(5F);

			writeHeader(table, "Org Group: " + hsu.get(OrgGroup.class,orgGroupId).getName());
			if (fromDate>0)
				writeHeader(table, "Start Date: " + DateUtils.getDateFormatted(fromDate));
			if (toDate>0)
				writeHeader(table, "End Date: " + DateUtils.getDateFormatted(toDate));

			if (!isEmpty(personId))
				writeHeader(table, "Employee: "+hsu.get(Person.class,personId).getNameLFM());

			if (projectCategoryId.length>0) {
				StringBuilder items = new StringBuilder("Category: ");
				if (projectCategoryId.length > 1)
					items = new StringBuilder("Categories: ");
				for (String id : projectCategoryId)
					items.append(hsu.get(ProjectCategory.class, id).getDescription()).append(" ");

				writeHeader(table, items.toString());
			}
			if (projectStatusId.length>0) {
				StringBuilder items = new StringBuilder("Status: ");
				if (projectStatusId.length > 1)
					items = new StringBuilder("Statuses: ");
				for (String id : projectStatusId)
					items.append(hsu.get(ProjectStatus.class, id).getDescription()).append(" ");

				writeHeader(table, items.toString());
			}
			if (projectTypeId.length>0) {
				StringBuilder items = new StringBuilder("Type: ");
				if (projectTypeId.length > 1)
					items = new StringBuilder("Types: ");
				for (String id : projectTypeId)
					items.append(Objects.requireNonNull(hsu.get(ProjectType.class, id)).getDescription()).append(" ");
				writeHeader(table, items.toString());
			}
			if (!isEmpty(companyId))
				writeHeader(table, "Company: "+ Objects.requireNonNull(hsu.get(OrgGroup.class, companyId)).getName());

			addTable(table);

			addHeaderLine();

			HibernateCriteriaUtil<Project> hcu = ArahantSession.getHSU().createCriteria(Project.class);
			if (isEmpty(personId)) {
				// if person is empty we want to find projects that are not yet assigned to a person
				// but are in the specified org group via the route stop
				if (!showAssigned)
					hcu.sizeEq(Project.PROJECT_EMPLOYEE_JOIN,0);

				hcu.joinTo(Project.CURRENT_ROUTE_STOP).joinTo(RouteStop.ORG_GROUP).eq(OrgGroup.ORGGROUPID, orgGroupId);

				hcu.orderBy(Project.PRIORITY_ORGGROUP).orderBy(Project.PROJECTNAME);
			} else {
				// else if person is present we want to see all projects assigned to person, regardless of org group
				hcu.joinTo(Project.PROJECT_EMPLOYEE_JOIN)
						.orderBy(ProjectEmployeeJoin.EMPLOYEE_PRIORITY)
						.joinTo(ProjectEmployeeJoin.PERSON)
						.eq(Person.PERSONID,personId);
				hcu.orderBy(Project.PROJECTNAME);
			}

			if (!isEmpty(projectSummary))
				hcu.like(Project.DESCRIPTION, projectSummary);

			if (!isEmpty(extReference))
				hcu.like(Project.REFERENCE, extReference);

			if (fromDate > 0)
				hcu.ge(Project.DATEREPORTED, fromDate);

			if (toDate > 0)
				hcu.le(Project.DATEREPORTED, toDate);

			if (projectCategoryId.length>0)
				hcu.joinTo(Project.PROJECTCATEGORY).in(ProjectCategory.PROJECTCATEGORYID, projectCategoryId);

			if (projectTypeId.length>0)
				hcu.joinTo(Project.PROJECTTYPE).in(ProjectType.PROJECTTYPEID, projectTypeId);

			if (projectStatusId.length>0)
				hcu.joinTo(Project.PROJECTSTATUS).in(ProjectStatus.PROJECTSTATUSID, projectStatusId);
			else
				hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');

			if (!isEmpty(companyId))
				hcu.eq(Project.REQUESTING_ORG_GROUP, ArahantSession.getHSU().get(CompanyBase.class,companyId));
			/*
			if (isEmpty(personId)) {
				BOrgGroup borg=new BOrgGroup(orgGroupId);
				
				hcu.joinTo(Project.CURRENT_ROUTE_STOP).joinTo(RouteStop.ORG_GROUP).in(OrgGroup.ORGGROUPID, borg.getAllOrgGroupsInHierarchy());
			} else {
				// else if person is present we want to see all projects assigned to person, regardless of org group
				hcu.joinTo(Project.CURRENT_PERSON).eq(Person.PERSONID, personId);
				hcu.orderBy(Project.EMPLOYEE_PRIORITY);
			}

			if (!isEmpty(projectSummary)) {
				hcu.like(Project.DESCRIPTION, projectSummary);
			}

			if (!isEmpty(extReference)) {
				hcu.like(Project.REFERENCE, extReference);
			}

			if (fromDate > 0) {
				hcu.ge(Project.DATEREPORTED, fromDate);
			}

			if (toDate > 0) {
				hcu.le(Project.DATEREPORTED, toDate);
			}

			if (!isEmpty(projectCategoryId)) {
				hcu.eq(Project.PROJECTCATEGORY, ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
			}

			if (!isEmpty(projectTypeId)) {
				hcu.eq(Project.PROJECTTYPE, ArahantSession.getHSU().get(ProjectType.class, projectTypeId));
			}

			if (!isEmpty(projectStatusId)) {
				hcu.eq(Project.PROJECTSTATUS, ArahantSession.getHSU().get(ProjectStatus.class, projectStatusId));
			}
			else
				hcu.joinTo(Project.PROJECTSTATUS).eq(ProjectStatus.ACTIVE, 'Y');

			if (!isEmpty(companyId)) {
				hcu.eq(Project.REQUESTING_ORG_GROUP, ArahantSession.getHSU().get(CompanyBase.class, companyId));
			}
XXYY  *  /

			HibernateScrollUtil<Project> scr = hcu.scroll();

			int count = 0;

			if (!isEmpty(personId))
				table = makeTable(7, 31, 10, 8, 8, 8, 8, 20);
			else
				table = makeTable(7, 31, 10, 8, 8, 8, 28);

			writeColHeader(table, "ID", Element.ALIGN_LEFT);
			writeColHeader(table, "Summary", Element.ALIGN_LEFT);
			writeColHeader(table, "Status", Element.ALIGN_LEFT);
			writeColHeader(table, "Company", Element.ALIGN_RIGHT);
			writeColHeader(table, "Org", Element.ALIGN_RIGHT);
			writeColHeader(table, "Client", Element.ALIGN_RIGHT);
			if (!isEmpty(personId))
				writeColHeader(table, "Employee", Element.ALIGN_RIGHT);
			writeColHeader(table, "Assigned", Element.ALIGN_LEFT);
			boolean alternateRow = true;

			while (scr.next()) {
				count++;

				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, scr.get().getProjectName(), alternateRow);
				write(table, scr.get().getDescription(), alternateRow);
				write(table, scr.get().getProjectStatus().getCode(), alternateRow);
				write(table, scr.get().getPriorityCompany(), alternateRow);
				write(table, scr.get().getPriorityDepartment(), alternateRow);
				write(table, scr.get().getPriorityClient(), alternateRow);

				StringBuilder emp = new StringBuilder();
				StringBuilder priority = new StringBuilder();
				boolean first = true;
				for (ProjectEmployeeJoin pej : scr.get().getProjectEmployeeJoins()) {

					if (!isEmpty(personId) && !pej.getPerson().getPersonId().equals(personId))
						continue;

					if (first)
						first = false;
					else {
						emp.append("\n");
						priority.append("\n");
					}


					emp.append(pej.getPerson().getNameLFM());
					priority.append(pej.getPersonPriority());
				}

				if (!isEmpty(personId))
					write(table, priority.toString(), alternateRow);
				write(table, emp.toString(), alternateRow);
			}

			scr.close();
			addTable(table);

			table=makeTable(100);
			write(table, "Total: " + count);

			addTable(table);

		} catch (Exception ignored) {

		} finally {
			close();

		}
		return getFilename();

		 */
	}
}
