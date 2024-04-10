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
package com.arahant.services.standard.project.projectListReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ProjectListReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectListReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectListReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectListReportOps.class);
	
	public ProjectListReportOps() {
		super();
	}
	
        @WebMethod()
	public SearchCompaniesReturn searchCompanies(/*@WebParam(name = "in")*/final SearchCompaniesInput in)		
	{
		final SearchCompaniesReturn ret=new SearchCompaniesReturn();
		try
		{
			checkLogin(in);

			if (!hsu.currentlySuperUser() && hsu.getCurrentPerson().getOrgGroupType()!=COMPANY_TYPE)
			{
				SearchCompaniesReturnItem []itms={new SearchCompaniesReturnItem(BPerson.getCurrent().getCompany())};
				ret.setItem(itms);
			}
			else
				ret.setItem(BCompanyBase.searchCompanySpecific(in.getName(), false, ret.getHighCap()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in)		
	{
		final SearchProjectCategoriesReturn ret=new SearchProjectCategoriesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProjectCategory.search(hsu,in.getCode(),in.getDescription(),in.getExcludeIds(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in)		
	{
		final SearchProjectTypesReturn ret=new SearchProjectTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProjectType.search(hsu,in.getCode(),in.getDescription(),in.getExcludeIds(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in)		
	{
		final SearchProjectStatusesReturn ret=new SearchProjectStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProjectStatus.search(hsu,in.getCode(),in.getDescription(),in.getExcludeIds(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
		
			ret.setReportUrl(new ProjectListReport().build(in.getIncludeName(),
					in.getIncludeDescription(), in.getIncludeDateReported(),
					in.getIncludeReference(), in.getIncludeRequestingCompany(),
					in.getIncludeCategory(), in.getIncludeType(), 
					in.getIncludeStatus(), in.getIncludeAssignedOrgGroup(),
					in.getIncludeAssignedPerson(), in.getSortAsc(),
					in.getSortType(), in.getCategoryIds(), in.getTypeIds(), 
					in.getStatusIds(), in.getStatusType(), in.getRequestingCompanyId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
