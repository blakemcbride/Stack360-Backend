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
import com.arahant.business.BHRBenefitConfig;
import com.arahant.business.BProject;
import com.arahant.business.BProjectHistory;
import com.arahant.business.BRouteStop;
import com.arahant.fields.ProjectDetailFields;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.MoneyUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.List;

/**
 *
 */
public class ProjectDetailReport extends ReportBase {
	private static final int left = 25;
	private static final int right = 100 - left;
	
	public ProjectDetailReport() {
		super("Proj", "Project Detail Report");
	}
	
	public String build(String projectId, String[] ids) throws DocumentException {
		  try {
        	addHeaderLine();

    		PdfPTable table = makeTable(left, right);
            
    		BProject proj = new BProject(projectId);
			
			for (String id : ids) {
				if (id.equals(ProjectDetailFields.APPROVAL_DATE_TIME)) {
					write(table, ProjectDetailFields.APPROVAL_DATE_TIME);
					write(table, DateUtils.getDateTimeFormatted(proj.getApprovalDate(), proj.getApprovalTime()));
				} else if (id.equals(ProjectDetailFields.APPROVAL_ENTERED_BY)) {
					write(table, ProjectDetailFields.APPROVAL_ENTERED_BY);
					write(table, proj.getApprovalEnteredBy());
				} else if (id.equals(ProjectDetailFields.APPROVED_BY)) {
					write(table, ProjectDetailFields.APPROVED_BY);
					write(table, proj.getApprovedBy());
				} else if (id.equals(ProjectDetailFields.BENEFIT_ASSOCIATIONS)) {
					boolean first=true;
					
					for (BHRBenefitConfig conf : proj.getAssociatedBenefitConfigs()) {
						if (first) {
							first=false;
							write(table, "Benefits");
						}
						else
							write(table, " ");
						write(table, conf.getName());
					}
				} else if (id.equals(ProjectDetailFields.BILLABLE_STATUS)) {
					write(table, ProjectDetailFields.BILLABLE_STATUS);
					switch (proj.getBillable()) {
						case 'Y' : write(table, "Billable");
									break;
						case 'N' : write(table, "Non-billable");
									break;
						case 'I' : write(table, "Billed inside parent project");
									break;
						case 'U' : write(table, "Unknown");
									break;
						default: write(table,proj.getBillable()+"");
					}
				} else if (id.equals(ProjectDetailFields.ACCESIBLE_TO_ALL)) {
					write(table, ProjectDetailFields.ACCESIBLE_TO_ALL);
					write(table,proj.getAllEmployees()=='Y'?"Yes":"No");
				} else if (id.equals(ProjectDetailFields.ACTUAL_BILLABLE)) {
					write(table, ProjectDetailFields.ACTUAL_BILLABLE);
					write(table, proj.getBillableHours()+"");
				} else if (id.equals(ProjectDetailFields.ACTUAL_NON_BILLABLE)) {
					write(table, ProjectDetailFields.ACTUAL_NON_BILLABLE);
					write(table, ""+proj.getNonBillableHours());
				} else if (id.equals(ProjectDetailFields.ASSIGNED_PEOPLE)) {
					boolean first=true;
					
					for (Person pej : proj.getAssignedPersons2(null)) {
						if (first) {
							first=false;
							write(table, "Currently Assigned");
						}
						else
							write(table, " ");
						write(table, pej.getNameLFM());
					}
				} else if (id.equals(ProjectDetailFields.BILLING_RATE)) {
					write(table, ProjectDetailFields.BILLING_RATE);
					writeLeft(table, MoneyUtils.formatMoney(proj.getBillingRate()),false);
				} else if (id.equals(ProjectDetailFields.CATEGORY)) {
					write(table, ProjectDetailFields.CATEGORY);
					write(table, proj.getProjectCategoryCode()+" "+proj.getProjectCategoryDescription());
				} else if (id.equals(ProjectDetailFields.CHECKLISTS)) {
					List<ProjectChecklistDetail> pcldList=hsu.createCriteria(ProjectChecklistDetail.class)
						.orderBy(ProjectChecklistDetail.DATE)
						.joinTo(ProjectChecklistDetail.PROJECT)
						.eq(Project.PROJECTID, proj.getProjectId())
						.list();
					boolean first=true;

					for (ProjectChecklistDetail pcd : pcldList) {
						if (first) {
							first=false;
							write(table, "Checklist Items");
						}
						else
							write(table, " ");
						
						write(table, pcd.getRouteStopChecklist().getItemDescription()+" "+pcd.getCompletedBy()+" "+DateUtils.getDateFormatted(pcd.getDateCompleted()));
					}
				} else if (id.equals(ProjectDetailFields.CLIENT_PRIORITY)) {
					write(table, ProjectDetailFields.CLIENT_PRIORITY);
					write(table, ""+proj.getClientPriority());
				} else if (id.equals(ProjectDetailFields.COMMENTS)) {
					HibernateScrollUtil<ProjectComment> scr=hsu.createCriteria(ProjectComment.class)
							.orderBy(ProjectComment.DATEENTERED)
							.joinTo(ProjectComment.PROJECTSHIFT)
							.joinTo(ProjectShift.PROJECT)
							.eq(Project.PROJECTID, proj.getProjectId())
							.scroll();

					while (scr.next()) {
						write(table, "Comment - "+scr.get().getPerson().getNameLFM());
						write(table, scr.get().getCommentTxt());			
					}

					scr.close();
				} else if (id.equals(ProjectDetailFields.COMPANY_PRIORITY)) {
					write(table, ProjectDetailFields.COMPANY_PRIORITY);
					write(table, ""+proj.getCompanyPriority());
				} else if (id.equals(ProjectDetailFields.DECISION_POINT)) {
					write(table, ProjectDetailFields.DECISION_POINT);
					if (isEmpty(proj.getRouteStopId()))
						write(table,"");
					else {
						BRouteStop rs=new BRouteStop(proj.getRouteStopId());
						write(table, rs.getRouteStopNameFormatted());
					}
				} else if (id.equals(ProjectDetailFields.DEF_BILLING_RATE)) {
					write(table, ProjectDetailFields.DEF_BILLING_RATE);
					writeLeft(table, proj.getDefaultBillingRateFormatted(),false);
				} else if (id.equals(ProjectDetailFields.DEF_PROD_SERVICE)) {
					write(table, ProjectDetailFields.DEF_PROD_SERVICE);
						write(table, proj.getProductName());
				} else if (id.equals(ProjectDetailFields.DETAIL)) {
					write(table, ProjectDetailFields.DETAIL);
					write(table, proj.getDetailDesc());
				} else if (id.equals(ProjectDetailFields.DOLLAR_CAP)) {
					write(table, ProjectDetailFields.DOLLAR_CAP);
					writeLeft(table, MoneyUtils.formatMoney(proj.getDollarCap()),false);
				} else if (id.equals(ProjectDetailFields.ESTIMATE_ON_DATE)) {
					write(table, ProjectDetailFields.ESTIMATE_ON_DATE);
					write(table, DateUtils.getDateFormatted(proj.getEstimateOnDate()));
				} else if (id.equals(ProjectDetailFields.EST_BILLABLE)) {
					write(table, ProjectDetailFields.EST_BILLABLE);
					write(table, proj.getEstimateHours() + "");
				} else if (id.equals(ProjectDetailFields.EST_TIME_SPAN)) {
					write(table, ProjectDetailFields.EST_TIME_SPAN);
					write(table,""+proj.getEstimateTimeSpan());
				} else if (id.equals(ProjectDetailFields.FORMS)) {
					HibernateScrollUtil<ProjectForm> scr=hsu.createCriteria(ProjectForm.class)
							.orderBy(ProjectForm.FORM_DATE)
							.joinTo(ProjectForm.PROJECTSHIFT)
							.joinTo(ProjectShift.PROJECT)
							.eq(Project.PROJECTID, proj.getProjectId())
							.scroll();
					boolean first=true;

					while (scr.next()) {
						if (first) {
							first=false;
							write(table, ProjectDetailFields.FORMS);
						}
						else
							write(table, " ");
						write(table, scr.get().getFormDate()+" "+scr.get().getComments());			
					}

					scr.close();
				} else if (id.equals(ProjectDetailFields.GROUP_PRIORITY)) {
					write(table, ProjectDetailFields.GROUP_PRIORITY);
					write(table, ""+proj.getOrgGroupPriority());
				} else if (id.equals(ProjectDetailFields.MANAGING_EMP)) {
					write(table, ProjectDetailFields.MANAGING_EMP);
					if (proj.getManagingEmployee()==null)
						write(table, " ");
					else
						write(table,proj.getManagingEmployee().getNameLFM());
				} else if (id.equals(ProjectDetailFields.NAME)) {
					write(table,ProjectDetailFields.NAME);
					write(table, proj.getName().trim());
				} else if (id.equals(ProjectDetailFields.DATE_TIME_REPORTED)) {
					write(table, ProjectDetailFields.DATE_TIME_REPORTED);
					write(table, proj.getDateReportedFormatted());
				} else if (id.equals(ProjectDetailFields.PERCENT_COMPLETE)) {
					write(table, ProjectDetailFields.PERCENT_COMPLETE);
					write(table, proj.getPercentComplete());
				} else if (id.equals(ProjectDetailFields.PROMISED_ON_DATE)) {
					write(table, ProjectDetailFields.PROMISED_ON_DATE);
					write(table, DateUtils.getDateFormatted(proj.getPromisedDate()));	
				} else if (id.equals(ProjectDetailFields.REFERENCE)) {
					write(table, ProjectDetailFields.REFERENCE);
					write(table, proj.getReference());
				} else if (id.equals(ProjectDetailFields.REQ_COMPANY)) {
					write(table, ProjectDetailFields.REQ_COMPANY);
					write(table, proj.getRequestingCompanyName());
				} else if (id.equals(ProjectDetailFields.REQ_GROUP)) {
					write(table, ProjectDetailFields.REQ_GROUP);
					write(table,proj.getRequestingOrgGroupName());
				} else if (id.equals(ProjectDetailFields.REQ_PERSON)) {
					write(table, ProjectDetailFields.REQ_PERSON);
					write(table,proj.getRequesterNameFormatted());					
				} else if (id.equals(ProjectDetailFields.ROUTE)) {
					write(table, ProjectDetailFields.ROUTE);
					write(table, proj.getRouteName());
				} else if (id.equals(ProjectDetailFields.ROUTE_STOP_COMPANY)) {
					write(table, ProjectDetailFields.ROUTE_STOP_COMPANY);
					if (isEmpty(proj.getRouteStopId()))
						write(table,"");
					else {
						BRouteStop rs=new BRouteStop(proj.getRouteStopId());
						write(table, rs.getCompanyName());
					}
				} else if (id.equals(ProjectDetailFields.ROUTE_STOP_GROUP)) {
					write(table, ProjectDetailFields.ROUTE_STOP_GROUP);
					if (isEmpty(proj.getRouteStopId()))
						write(table,"");
					else {
						BRouteStop rs=new BRouteStop(proj.getRouteStopId());
						write(table, rs.getOrgGroupName());
					}
				} else if (id.equals(ProjectDetailFields.ROUTE_STOP_PHASE)) {
					write(table, ProjectDetailFields.ROUTE_STOP_PHASE);
					if (isEmpty(proj.getRouteStopId()))
						write(table,"");
					else {
						BRouteStop rs=new BRouteStop(proj.getRouteStopId());
						ProjectPhase pp=hsu.get(ProjectPhase.class, rs.getPhaseId());
						write(table, rs.getPhaseCode()+" "+pp.getDescription());
					}
				} else if (id.equals(ProjectDetailFields.ROUTING_HISTORY)) {
					HibernateScrollUtil<ProjectHistory> scr=hsu.createCriteria(ProjectHistory.class)
						.orderBy(ProjectHistory.DATE)
						.orderBy(ProjectHistory.TIME)
						.joinTo(ProjectHistory.PROJECT)
						.eq(Project.PROJECTID, proj.getProjectId())
						.scroll();

					while (scr.next()) {
						BProjectHistory ph=new BProjectHistory(scr.get());
						write(table, "History - "+DateUtils.getDateTimeFormatted(scr.get().getDateChanged(),scr.get().getTimeChanged()));
						write(table, scr.get().getPerson().getNameLFM()+" "+ph.getChangeType());			
					}

					scr.close();
				} else if (id.equals(ProjectDetailFields.SPONSORING_EMP)) {
					write(table, ProjectDetailFields.SPONSORING_EMP);
					write(table,proj.getSponsorNameFormatted());
				} else if (id.equals(ProjectDetailFields.STATUS)) {
					write(table, ProjectDetailFields.STATUS);
					write(table, proj.getProjectStatusCode()+" "+proj.getProjectStatusDescription());
				} else if (id.equals(ProjectDetailFields.SUBPROJECTS)) {
					boolean first=true;
						
					for (BProject sp : proj.listSubProjects()) {
						if (first) {
							first=false;
							write(table, ProjectDetailFields.SUBPROJECTS);
						}
						else
							write(table, " ");
						write(table, sp.getProjectName()+" "+sp.getReference()+" "+sp.getSummary()+" "+sp.getDateReportedFormatted());
					}
				} else if (id.equals(ProjectDetailFields.SUMMARY)) {
					write(table, ProjectDetailFields.SUMMARY);
					write(table, proj.getSummary());					
				} else if (id.equals(ProjectDetailFields.TIMESHEETS)) {
					HibernateScrollUtil<Timesheet> scr=hsu.createCriteria(Timesheet.class)
						.orderBy(Timesheet.WORKDATE)
						.orderBy(Timesheet.BEGINNINGTIME)
							.joinTo(Timesheet.PROJECTSHIFT)
						.joinTo(ProjectShift.PROJECT)
						.eq(Project.PROJECTID, proj.getProjectId())
						.scroll();

					while (scr.next()) {
						write(table, "Timesheet - "+DateUtils.getDateFormatted(scr.get().getWorkDate()));
						write(table, scr.get().getPerson().getNameLFM()+" "+scr.get().getTotalHours()+" hours");			
					}
				} else if (id.equals(ProjectDetailFields.TYPE)) {
					write(table, ProjectDetailFields.TYPE);
					write(table, proj.getProjectType().getCode()+" "+proj.getProjectType().getDescription());
				}
			}
			addTable(table);
        } finally {
            close();
        }
        
        return getFilename();	
	}
}
