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
package com.arahant.services.standard.project.projectBillingReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProjectBillingReport;
import com.arahant.reports.ProjectModifiedBillingReport;
import com.arahant.reports.ProjectTimesheetReport;
import com.arahant.utils.HibernateScrollUtil;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectBillingReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectBillingReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectBillingReportOps.class);
	
	public ProjectBillingReportOps() {
		super();
	}
	

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setBillableAccessLevel(BRight.checkRight("AccessBillableLevel"));
				

			finishService(ret);
		} catch (final Throwable e) {
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

	@WebMethod()
	public GetProjectReportDataReturn  getProjectReportData(/*@WebParam(name = "in")*/final GetProjectReportDataInput in)	{
		
		final GetProjectReportDataReturn ret=new GetProjectReportDataReturn();

		try {
			checkLogin(in);
			
			final HibernateScrollUtil sumAndProjects=BTimesheet.getTimesheetsForProjectReport(hsu, BPerson.getCurrent(), 
					in.getClientCompanyId(), in.getProjectId(), in.getOrgGroupId(), in.getEmployeeId(), 
					in.getStartDate(), in.getEndDate(), in.isApproved(), in.isNotApproved(), in.isInvoiced(),-1);
			
			
			ret.makeProjectReportDetails(sumAndProjects,in.getSortOn(),in.getSortAsc(),-1);
		
			ret.setTotalBusinessHours(ProjectTimesheetReport.getBusinessHoursBetween(in.getStartDate(), in.getEndDate(), hsu));

			
		//	BProject p=new BProject(in.getProjectId());
			
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

	@WebMethod()
	public GetProjectReportReturn  getProjectReport(/*@WebParam(name = "in")*/final GetProjectReportInput in)	{
		final GetProjectReportReturn ret= new GetProjectReportReturn();

		try {
			checkLogin(in);

			if(in.getReportType().equals("D"))
			{
				ret.setReportUrl(new ProjectBillingReport().build(in.getClientCompanyId(),in.getEmployeeId(),
					in.getOrgGroupId(),in.getProjectId(), in.isApproved(),in.isNotApproved(),in.isInvoiced()));
			}
			else
			{
				ret.setReportUrl(new ProjectModifiedBillingReport().build(in.getClientCompanyId(),in.getEmployeeId(),
					in.getOrgGroupId(),in.getProjectId(), in.isApproved(),in.isNotApproved(),in.isInvoiced()));
			}
			

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in)	{
		final SearchCompanyReturn ret=new SearchCompanyReturn();
		
		try {
			checkLogin(in);
						
			ret.setCompanies(BCompanyBase.searchCompanySpecific(in.getName(),false,ret.getHighCap()));
			
			if (!isEmpty(in.getCompanyId()))
				ret.setSelectedItem(new SearchCompanyReturnItem(BCompanyBase.get(in.getCompanyId())));
			
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
				
			ret.setProjectCategories(BProjectCategory.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));
			
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
			
			ret.setProjectTypes(BProjectType.search(hsu,in.getCode(),in.getDescription(),ret.getHighCap()));
			finishService(ret);	
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

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
	public SearchSubordinateEmployeesReturn searchSubordinateEmployees(/*@WebParam(name = "in")*/final SearchSubordinateEmployeesInput in)	{
		final SearchSubordinateEmployeesReturn ret=new SearchSubordinateEmployeesReturn();
		
		try
		{
			checkLogin(in);

			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu,in.getFirstName(),in.getLastName(),
					0,in.getSsn(),in.getOrgGroupId(),ret.getHighCap()));
			
			if (!isEmpty(in.getPersonId()))
				ret.setSelectedItem(new SearchSubordinateEmployeesReturnItem(new BEmployee(in.getPersonId())));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		

		return ret;
	}

	@WebMethod()
	public SearchSubordinateOrgGroupsReturn searchSubordinateOrgGroups(/*@WebParam(name = "in")*/final SearchSubordinateOrgGroupsInput in)	{
		final SearchSubordinateOrgGroupsReturn ret=new SearchSubordinateOrgGroupsReturn();
		
		try {
			checkLogin(in);

			ret.setOrgGroups(BPerson.getCurrent().searchSubordinateGroups(hsu,in.getName(),COMPANY_TYPE,ret.getHighCap()));
			
			if (!isEmpty(in.getOrgGroupId()))
				ret.setSelectedItem(new SearchSubordinateOrgGroupsReturnItem(new BOrgGroup(in.getOrgGroupId())));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

}
