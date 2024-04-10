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
import com.arahant.beans.Route;
import com.arahant.beans.RouteStop;
import com.arahant.business.BOrgGroup;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateCriterionUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *
 */
public class ProjectGroupReportClient extends ReportBase {

	public ProjectGroupReportClient() {
		super("projGrp", "Projects", true);
	}

	public String build(String orgGroupId, String extReference, int fromDate, int toDate,
			String []projectStatusId, String [] projectCategoryId,
			String []projectTypeId, String projectSummary, 
			boolean showAssigned, boolean showUnassigned) {

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

			
			if (projectCategoryId.length>0)
			{
				String items="Category: ";
				if (projectCategoryId.length>1)
					items="Categories: ";
				for (String id : projectCategoryId)
					items+=hsu.get(ProjectCategory.class,id).getDescription()+" ";
			
				writeHeader(table, items);
			}
			if (projectStatusId.length>0)
			{
				String items="Status: ";
				if (projectStatusId.length>1)
					items="Statuses: ";
				for (String id : projectStatusId)
					items+=hsu.get(ProjectStatus.class,id).getDescription()+" ";
				
				writeHeader(table, items);
			}
			if (projectTypeId.length>0)
			{
				String items="Type: ";
				if (projectTypeId.length>1)
					items="Types: ";
				for (String id : projectTypeId)
					items+=hsu.get(ProjectType.class,id).getDescription()+" ";
				writeHeader(table, items);
			}

			
			addTable(table);
			
			
			addHeaderLine();


		final HibernateCriteriaUtil<Project> hcu=ArahantSession.getHSU().createCriteria(Project.class,"proj");
		

		// or the following with the requested company
		BOrgGroup borg=new BOrgGroup(orgGroupId);

		if (showUnassigned&&showAssigned)
		{
			HibernateCriteriaUtil hc2=hcu.joinTo(Project.CURRENT_ROUTE_STOP).joinTo(RouteStop.ROUTE).joinTo(Route.ROUTE_STOPS).leftJoinTo(RouteStop.ORG_GROUP);
			HibernateCriterionUtil hcri1=hc2.makeCriteria().eq(OrgGroup.ORGGROUPID,orgGroupId);
			HibernateCriterionUtil hcri2=hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
			HibernateCriterionUtil hcri3=hc2.makeCriteria().eq("proj."+Project.REQUESTING_ORG_GROUP, borg.getOrgGroup().getOwningCompany());
			HibernateCriterionUtil hcriand=hc2.makeCriteria().and(hcri2, hcri3);
			HibernateCriterionUtil hcrior=hc2.makeCriteria().or(hcriand, hcri1);
			hcrior.add();
		}
		else
			if (showAssigned)
			{
				HibernateCriteriaUtil hc2=hcu.joinTo(Project.CURRENT_ROUTE_STOP).leftJoinTo(RouteStop.ORG_GROUP);
				HibernateCriterionUtil hcri1=hc2.makeCriteria().eq(OrgGroup.ORGGROUPID,orgGroupId);
				HibernateCriterionUtil hcri2=hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
				HibernateCriterionUtil hcri3=hc2.makeCriteria().eq("proj."+Project.REQUESTING_ORG_GROUP, borg.getOrgGroup().getOwningCompany());
				HibernateCriterionUtil hcriand=hc2.makeCriteria().and(hcri2, hcri3);
				HibernateCriterionUtil hcrior=hc2.makeCriteria().or(hcriand, hcri1);
				hcrior.add();
			}
			else 
				if (showUnassigned)
				{
					HibernateCriteriaUtil hc2=hcu.joinTo(Project.CURRENT_ROUTE_STOP)
						.isNotNull(RouteStop.ORG_GROUP)
						.ne(RouteStop.ORG_GROUP_ID, orgGroupId)
						.joinTo(RouteStop.ROUTE).joinTo(Route.ROUTE_STOPS).leftJoinTo(RouteStop.ORG_GROUP);
					HibernateCriterionUtil hcri1=hc2.makeCriteria().eq(OrgGroup.ORGGROUPID,orgGroupId);
					HibernateCriterionUtil hcri2=hc2.makeCriteria().isNull(OrgGroup.ORGGROUPID);
					HibernateCriterionUtil hcri3=hc2.makeCriteria().eq("proj."+Project.REQUESTING_ORG_GROUP, borg.getOrgGroup().getOwningCompany());
					HibernateCriterionUtil hcriand=hc2.makeCriteria().and(hcri2, hcri3);
					HibernateCriterionUtil hcrior=hc2.makeCriteria().or(hcriand, hcri1);
					hcrior.add();
				}
				else
					hcu.eq(Project.PROJECTID, "Find nothing");
		
		hcu.orderBy(Project.PRIORITY_CLIENT);
	
		
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
		

			HibernateScrollUtil<Project> scr = hcu.scroll();

			int count = 0;

			table = makeTable(new int[]{20,45,20,15});

			writeColHeader(table, "ID", Element.ALIGN_LEFT);
			writeColHeader(table, "Summary", Element.ALIGN_LEFT);
			writeColHeader(table, "Status", Element.ALIGN_LEFT);
			writeColHeader(table, "Client", Element.ALIGN_RIGHT);

			boolean alternateRow = true;

			while (scr.next()) {
				count++;



				// toggle the alternate row
				alternateRow = !alternateRow;

				write(table, scr.get().getProjectName(), alternateRow);
				write(table, scr.get().getDescription(), alternateRow);
				write(table, scr.get().getProjectStatus().getCode(), alternateRow);

				write(table, scr.get().getPriorityClient(), alternateRow);

			}

			scr.close();
			addTable(table);

			table=makeTable(new int[]{100});
			write(table, "Total: " + count);

			addTable(table);

		} catch (Exception e) {

		} finally {
			close();

		}

		return getFilename();

	}
}
