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
package com.arahant.services.standard.billing.billingDetailReport;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.BillingDetailReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingBillingDetailReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BillingDetailReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BillingDetailReportOps.class);
	
	public BillingDetailReportOps() {
		super();
	}
	
    @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {

		final GetReportReturn ret = new GetReportReturn();

		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new BillingDetailReport().build(in.getNonApproved(), in.getApproved(), in.getInvoiced(), in.getStartDate(), in.getEndDate(), in.getClientId(), in.getEmployeeId(), in.getProjectId(), in.getBillable(), in.getNonBillable()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchCompanyReturn searchCompany(/*@WebParam(name = "in")*/final SearchCompanyInput in) {
		final SearchCompanyReturn ret = new SearchCompanyReturn();

		try {
			checkLogin(in);

			ret.setCompanies(BCompanyBase.search(in.getName(), false, ret.getHighCap()));

			if (!isEmpty(in.getCompanyId())) {
				ret.setSelectedItem(new SearchCompanyReturnItem(BCompanyBase.get(in.getCompanyId())));
			}

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectCategoriesReturn searchProjectCategories(/*@WebParam(name = "in")*/final SearchProjectCategoriesInput in) {
		final SearchProjectCategoriesReturn ret = new SearchProjectCategoriesReturn();

		try {
			checkLogin(in);

			ret.setProjectCategories(BProjectCategory.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectTypesReturn searchProjectTypes(/*@WebParam(name = "in")*/final SearchProjectTypesInput in) {
		final SearchProjectTypesReturn ret = new SearchProjectTypesReturn();

		try {
			checkLogin(in);

			ret.setProjectTypes(BProjectType.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectStatusesReturn searchProjectStatuses(/*@WebParam(name = "in")*/final SearchProjectStatusesInput in) {
		final SearchProjectStatusesReturn ret = new SearchProjectStatusesReturn();

		try {
			checkLogin(in);

			ret.setProjectStatuses(BProjectStatus.search(hsu, in.getCode(), in.getDescription(), ret.getHighCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchProjectsReturn searchProjects(/*@WebParam(name = "in")*/final SearchProjectsInput in) {
		final SearchProjectsReturn ret = new SearchProjectsReturn();

		try {
			checkLogin(in);

			ret.setProjects(BProject.search(hsu, in.getSummary(), in.getCategory(), in.getStatus(), in.getType(), in.getCompanyId(), in.getProjectName(),
					0, 0, null, false, in.getUser(), null, null, ret.getHighCap(), true));


			if (!isEmpty(in.getPersonId())) {
				String defaultProjectId = new BEmployee(in.getPersonId()).getExplicitProjectId();

				if (!isEmpty(defaultProjectId)) {
					ret.setSelectedItem(new SearchProjectsReturnItem(new BProject(defaultProjectId)));
				}
			}
			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();

		try
		{
			checkLogin(in);
			boolean includeUser=true;
			
			if (hsu.currentlySuperUser())
				includeUser=false;
			
			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser));
			
			if (in.getAutoDefault() && includeUser && BPerson.getCurrent().isEmployee())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}
