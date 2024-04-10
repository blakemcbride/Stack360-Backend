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
 * Created on Feb 4, 2007
 * 
 */
package com.arahant.services.standard.project.projectTimesheetReportClientLimited;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


import com.arahant.business.*;
import com.arahant.reports.ProjectModifiedBillingReport;
import com.arahant.reports.ProjectTimesheetReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.*;



/**
 * 
 *
 * Created on Feb 4, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectTimesheetReportClientLimitedOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectTimesheetReportClientLimitedOps extends ServiceBase {
	
	private static final ArahantLogger logger = new ArahantLogger(ProjectTimesheetReportClientLimitedOps.class);

	

	@WebMethod()
	public GetProjectReportDataReturn  getProjectReportData(/*@WebParam(name = "in")*/final GetProjectReportDataInput in)	{
		
		final GetProjectReportDataReturn ret=new GetProjectReportDataReturn();

		try {
			checkLogin(in);
			
			final HibernateScrollUtil sumAndProjects=BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(), 
					in.getClientCompanyId(), in.getProjectId(),
					in.getStartDate(), in.getEndDate(), in.isApproved(), in.isNotApproved(), in.isInvoiced(),50, in.getProjectName());
			
			
			ret.makeProjectReportDetails(sumAndProjects,in.getSortOn(),in.getSortAsc(),50);
		
					
			//make the hours neater
			ret.setEstimatedHours(roundToHundredths(ret.getEstimatedHours()));
			
			ret.setBillableHours(roundToHundredths(ret.getBillableHours()));
			ret.setNonBillableHours(roundToHundredths(ret.getNonBillableHours()));
			ret.setUnknownHours(roundToHundredths(ret.getUnknownHours()));
			
			ret.setBillableAmount(roundToHundredths(ret.getBillableAmount()));
			ret.setNonBillableAmount(roundToHundredths(ret.getNonBillableAmount()));
			ret.setUnknownAmount(roundToHundredths(ret.getUnknownAmount()));
			
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	
	@SuppressWarnings("unchecked")
	@WebMethod()
	public GetProjectReportReturn  getProjectReport(/*@WebParam(name = "in")*/final GetProjectReportInput in)	{
		final GetProjectReportReturn ret= new GetProjectReportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl(new ProjectModifiedBillingReport().build(in.getClientCompanyId(), "", "", in.getProjectId(), in.isApproved(), in.isNotApproved(),in.isInvoiced()));
//			ret.setReportUrl(new ProjectTimesheetReport().build(in.getClientCompanyId(),"SKIP",
//					"",in.getProjectId(),in.getStartDate(),in.getEndDate(),
//					in.isApproved(),in.isNotApproved(),in.isInvoiced(), false));
			
			
			finishService(ret);
		} 
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	/*
	 * 1. Return projects based on employee, project status, and the join table
2. Return projects accessible to all persons regardless of status
3. Return projects most recently used by some made up criteria
 
Trickiness - on item 3, suppose you are taking 5 projects, that would be 5 projects that don't necessarily appear in 1 or 2 (so always 5 more if there are 5 more)?
	 */
	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in)	{
		final SearchProjectsReturn ret=new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(),in.getStatus(),in.getType(),in.getCompanyId(),in.getProjectName(),
					0,0,null,false,in.getUser(),null,null,ret.getCap(), true));
			
			finishService(ret);
	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)	{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);
				
			ret.setProjectCategories(BProjectCategory.search(false,in.getCode(),in.getDescription(),ret.getHighCap()));
			
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in)	{
		final SearchProjectStatusesReturn ret=new SearchProjectStatusesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(),ret.getHighCap()));
			
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)	{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();

		try {
			checkLogin(in);
			
			ret.setProjectTypes(BProjectType.search(false,in.getCode(),in.getDescription(),ret.getHighCap()));
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	


        @WebMethod()
	public GetCompanyReturn getCompany(/*@WebParam(name = "in")*/final GetCompanyInput in)		
	{
		final GetCompanyReturn ret=new GetCompanyReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(BPerson.getCurrent().getCompany());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetProjectDetailReturn getProjectDetail(/*@WebParam(name = "in")*/final GetProjectDetailInput in)		
	{
		final GetProjectDetailReturn ret=new GetProjectDetailReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProject(in.getProjectId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
