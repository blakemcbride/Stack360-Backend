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


package com.arahant.exports;

import com.arahant.beans.ProjectPhase;
import com.arahant.business.BProject;
import com.arahant.business.BRouteStop;
import com.arahant.exceptions.ArahantException;
import com.arahant.fields.ProjectDetailFields;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import java.io.File;

public class ProjectDetailExport {

	private String[] ids;
	private String projectId;

	public ProjectDetailExport(String[] ids, String projectId) throws ArahantException {
		this.ids = ids;
		this.projectId = projectId;
	}

	public String build() throws Exception {
		File csvFile = FileSystemUtils.createTempFile("ProjDet", ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			// Pass one is to write out all used columns
			for (String id : ids)
				if (id.equals(ProjectDetailFields.APPROVAL_DATE_TIME))
					writer.writeField(ProjectDetailFields.APPROVAL_DATE_TIME);
				else if (id.equals(ProjectDetailFields.APPROVAL_ENTERED_BY))
					writer.writeField(ProjectDetailFields.APPROVAL_ENTERED_BY);
				else if (id.equals(ProjectDetailFields.APPROVED_BY))
					writer.writeField(ProjectDetailFields.APPROVED_BY);
				else if (id.equals(ProjectDetailFields.BENEFIT_ASSOCIATIONS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.BILLABLE_STATUS))
					writer.writeField(ProjectDetailFields.BILLABLE_STATUS);
				else if (id.equals(ProjectDetailFields.ACCESIBLE_TO_ALL))
					writer.writeField(ProjectDetailFields.ACCESIBLE_TO_ALL);
				else if (id.equals(ProjectDetailFields.ACTUAL_BILLABLE))
					writer.writeField(ProjectDetailFields.ACTUAL_BILLABLE);
				else if (id.equals(ProjectDetailFields.ACTUAL_NON_BILLABLE))
					writer.writeField(ProjectDetailFields.ACTUAL_NON_BILLABLE);
				else if (id.equals(ProjectDetailFields.ASSIGNED_PEOPLE)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.BILLING_RATE))
					writer.writeField(ProjectDetailFields.BILLING_RATE);
				else if (id.equals(ProjectDetailFields.CATEGORY))
					writer.writeField(ProjectDetailFields.CATEGORY);
				else if (id.equals(ProjectDetailFields.CHECKLISTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.CLIENT_PRIORITY))
					writer.writeField(ProjectDetailFields.CLIENT_PRIORITY);
				else if (id.equals(ProjectDetailFields.COMMENTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.COMPANY_PRIORITY))
					writer.writeField(ProjectDetailFields.COMPANY_PRIORITY);
				else if (id.equals(ProjectDetailFields.DECISION_POINT))
					writer.writeField(ProjectDetailFields.DECISION_POINT);
				else if (id.equals(ProjectDetailFields.DEF_BILLING_RATE))
					writer.writeField(ProjectDetailFields.DEF_BILLING_RATE);
				else if (id.equals(ProjectDetailFields.DEF_PROD_SERVICE))
					writer.writeField(ProjectDetailFields.DEF_PROD_SERVICE);
				else if (id.equals(ProjectDetailFields.DETAIL))
					writer.writeField(ProjectDetailFields.DETAIL);
				else if (id.equals(ProjectDetailFields.DOLLAR_CAP))
					writer.writeField(ProjectDetailFields.DOLLAR_CAP);
				else if (id.equals(ProjectDetailFields.ESTIMATE_ON_DATE))
					writer.writeField(ProjectDetailFields.ESTIMATE_ON_DATE);
				else if (id.equals(ProjectDetailFields.EST_BILLABLE))
					writer.writeField(ProjectDetailFields.EST_BILLABLE);
				else if (id.equals(ProjectDetailFields.EST_TIME_SPAN))
					writer.writeField(ProjectDetailFields.EST_TIME_SPAN);
				else if (id.equals(ProjectDetailFields.FORMS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.GROUP_PRIORITY))
					writer.writeField(ProjectDetailFields.GROUP_PRIORITY);
				else if (id.equals(ProjectDetailFields.MANAGING_EMP))
					writer.writeField(ProjectDetailFields.MANAGING_EMP);
				else if (id.equals(ProjectDetailFields.NAME))
					writer.writeField(ProjectDetailFields.NAME);
				else if (id.equals(ProjectDetailFields.DATE_TIME_REPORTED))
					writer.writeField(ProjectDetailFields.DATE_TIME_REPORTED);
				else if (id.equals(ProjectDetailFields.PERCENT_COMPLETE))
					writer.writeField(ProjectDetailFields.PERCENT_COMPLETE);
				else if (id.equals(ProjectDetailFields.PROMISED_ON_DATE))
					writer.writeField(ProjectDetailFields.PROMISED_ON_DATE);
				else if (id.equals(ProjectDetailFields.REFERENCE))
					writer.writeField(ProjectDetailFields.REFERENCE);
				else if (id.equals(ProjectDetailFields.REQ_COMPANY))
					writer.writeField(ProjectDetailFields.REQ_COMPANY);
				else if (id.equals(ProjectDetailFields.REQ_GROUP))
					writer.writeField(ProjectDetailFields.REQ_GROUP);
				else if (id.equals(ProjectDetailFields.REQ_PERSON))
					writer.writeField(ProjectDetailFields.REQ_PERSON);
				else if (id.equals(ProjectDetailFields.ROUTE))
					writer.writeField(ProjectDetailFields.ROUTE);
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_COMPANY))
					writer.writeField(ProjectDetailFields.ROUTE_STOP_COMPANY);
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_GROUP))
					writer.writeField(ProjectDetailFields.ROUTE_STOP_GROUP);
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_PHASE))
					writer.writeField(ProjectDetailFields.ROUTE_STOP_PHASE);
				else if (id.equals(ProjectDetailFields.ROUTING_HISTORY)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.SPONSORING_EMP))
					writer.writeField(ProjectDetailFields.SPONSORING_EMP);
				else if (id.equals(ProjectDetailFields.STATUS))
					writer.writeField(ProjectDetailFields.STATUS);
				else if (id.equals(ProjectDetailFields.SUBPROJECTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.SUMMARY))
					writer.writeField(ProjectDetailFields.SUMMARY);
				else if (id.equals(ProjectDetailFields.TIMESHEETS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.TYPE))
					writer.writeField(ProjectDetailFields.TYPE);

			writer.endRecord();

			// Pass two is to write out data for used columns
			BProject proj = new BProject(projectId);

			for (String id : ids)
				if (id.equals(ProjectDetailFields.APPROVAL_DATE_TIME))
					writer.writeField(DateUtils.getDateTimeFormatted(proj.getApprovalDate(), proj.getApprovalTime()));
				else if (id.equals(ProjectDetailFields.APPROVAL_ENTERED_BY))
					writer.writeField(proj.getApprovalEnteredBy());
				else if (id.equals(ProjectDetailFields.APPROVED_BY))
					writer.writeField(proj.getApprovedBy());
				else if (id.equals(ProjectDetailFields.BENEFIT_ASSOCIATIONS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.BILLABLE_STATUS))
					switch (proj.getBillable()) {
						case 'Y':
							writer.writeField("Billable");
							break;
						case 'N':
							writer.writeField("Non-billable");
							break;
						case 'I':
							writer.writeField("Billed inside parent project");
							break;
						case 'U':
							writer.writeField("Unknown");
							break;
						default:
							writer.writeField(proj.getBillable() + "");
					}
				else if (id.equals(ProjectDetailFields.ACCESIBLE_TO_ALL))
					writer.writeField(proj.getAllEmployees() == 'Y' ? "Yes" : "No");
				else if (id.equals(ProjectDetailFields.ACTUAL_BILLABLE))
					writer.writeField(proj.getBillableHours());
				else if (id.equals(ProjectDetailFields.ACTUAL_NON_BILLABLE))
					writer.writeField(proj.getNonBillableHours());
				else if (id.equals(ProjectDetailFields.ASSIGNED_PEOPLE)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.BILLING_RATE))
					writer.writeField(proj.getBillingRate());
				else if (id.equals(ProjectDetailFields.CATEGORY))
					writer.writeField(proj.getProjectCategoryCode() + " " + proj.getProjectCategoryDescription());
				else if (id.equals(ProjectDetailFields.CHECKLISTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.CLIENT_PRIORITY))
					writer.writeField(proj.getClientPriority());
				else if (id.equals(ProjectDetailFields.COMMENTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.COMPANY_PRIORITY))
					writer.writeField(proj.getCompanyPriority());
				else if (id.equals(ProjectDetailFields.DECISION_POINT))
					if (proj.getRouteStopId() == null || proj.getRouteStopId().length() == 0)
						writer.writeField("");
					else {
						BRouteStop rs = new BRouteStop(proj.getRouteStopId());
						writer.writeField(rs.getRouteStopNameFormatted());
					}
				else if (id.equals(ProjectDetailFields.DEF_BILLING_RATE))
					writer.writeField(proj.getDefaultBillingRate(null));
				else if (id.equals(ProjectDetailFields.DEF_PROD_SERVICE))
					writer.writeField(proj.getProductName());
				else if (id.equals(ProjectDetailFields.DETAIL))
					writer.writeField(proj.getDetailDesc());
				else if (id.equals(ProjectDetailFields.DOLLAR_CAP))
					writer.writeField(proj.getDollarCap());
				else if (id.equals(ProjectDetailFields.ESTIMATE_ON_DATE))
					writer.writeDate(proj.getEstimateOnDate());
				else if (id.equals(ProjectDetailFields.EST_BILLABLE))
					writer.writeField(proj.getEstimateHours());
				else if (id.equals(ProjectDetailFields.EST_TIME_SPAN))
					writer.writeField(proj.getEstimateTimeSpan());
				else if (id.equals(ProjectDetailFields.FORMS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.GROUP_PRIORITY))
					writer.writeField(proj.getOrgGroupPriority());
				else if (id.equals(ProjectDetailFields.MANAGING_EMP))
					if (proj.getManagingEmployee() == null)
						writer.writeField("");
					else
						writer.writeField(proj.getManagingEmployee().getNameLFM());
				else if (id.equals(ProjectDetailFields.NAME))
					writer.writeField(proj.getName().trim());
				else if (id.equals(ProjectDetailFields.DATE_TIME_REPORTED))
					writer.writeDate(proj.getDateReported());
				else if (id.equals(ProjectDetailFields.PERCENT_COMPLETE))
					writer.writeField(proj.getPercentComplete());
				else if (id.equals(ProjectDetailFields.PROMISED_ON_DATE))
					writer.writeDate(proj.getPromisedDate());
				else if (id.equals(ProjectDetailFields.REFERENCE))
					writer.writeField(proj.getReference());
				else if (id.equals(ProjectDetailFields.REQ_COMPANY))
					writer.writeField(proj.getRequestingCompanyName());
				else if (id.equals(ProjectDetailFields.REQ_GROUP))
					writer.writeField(proj.getRequestingOrgGroupName());
				else if (id.equals(ProjectDetailFields.REQ_PERSON))
					writer.writeField(proj.getRequesterNameFormatted());
				else if (id.equals(ProjectDetailFields.ROUTE))
					writer.writeField(proj.getRouteName());
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_COMPANY))
					if (proj.getRouteStopId() == null || proj.getRouteStopId().length() == 0)
						writer.writeField("");
					else {
						BRouteStop rs = new BRouteStop(proj.getRouteStopId());
						writer.writeField(rs.getCompanyName());
					}
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_GROUP))
					if (proj.getRouteStopId() == null || proj.getRouteStopId().length() == 0)
						writer.writeField("");
					else {
						BRouteStop rs = new BRouteStop(proj.getRouteStopId());
						writer.writeField(rs.getOrgGroupName());
					}
				else if (id.equals(ProjectDetailFields.ROUTE_STOP_PHASE))
					if (proj.getRouteStopId() == null || proj.getRouteStopId().length() == 0)
						writer.writeField("");
					else {
						BRouteStop rs = new BRouteStop(proj.getRouteStopId());
						ProjectPhase pp = ArahantSession.getHSU().get(ProjectPhase.class, rs.getPhaseId());
						writer.writeField(rs.getPhaseCode() + " " + pp.getDescription());
					}
				else if (id.equals(ProjectDetailFields.ROUTING_HISTORY)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.SPONSORING_EMP))
					writer.writeField(proj.getSponsorNameFormatted());
				else if (id.equals(ProjectDetailFields.STATUS))
					writer.writeField(proj.getProjectStatusCode() + " " + proj.getProjectStatusDescription());
				else if (id.equals(ProjectDetailFields.SUBPROJECTS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.SUMMARY))
					writer.writeField(proj.getSummary());
				else if (id.equals(ProjectDetailFields.TIMESHEETS)) {
					// TODO multi-value column - how to handle ?
				} else if (id.equals(ProjectDetailFields.TYPE))
					writer.writeField(proj.getProjectType().getCode() + " " + proj.getProjectType().getDescription());

			writer.endRecord();
		} finally {
			try {
				writer.close();
			} catch (Exception ignored) {
			}
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}
}
