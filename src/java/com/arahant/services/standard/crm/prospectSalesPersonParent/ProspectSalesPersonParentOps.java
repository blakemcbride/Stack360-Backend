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

package com.arahant.services.standard.crm.prospectSalesPersonParent;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.SalesProspectReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmProspectSalesPersonParentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProspectSalesPersonParentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProspectSalesPersonParentOps.class);
	
	public ProspectSalesPersonParentOps() {
		super();
	}
	
	@WebMethod()
	public SearchSalesPersonsReturn searchSalesPersons(/*@WebParam(name = "in")*/final SearchSalesPersonsInput in)		
	{
		final SearchSalesPersonsReturn ret=new SearchSalesPersonsReturn();
		try
		{
			checkLogin(in);

			BSearchOutput<BEmployee> bSearchOutput;    
			if (hsu.currentlySuperUser() || BPerson.getCurrent().isManager())
			{
 				bSearchOutput = BProspectCompany.searchSalesPeople(in.getSearchMetaInput(), in.getFirstName(), in.getLastName());
			}
			else
			{
				// set output manually
				bSearchOutput = new BSearchOutput<BEmployee>(new BEmployee[]{new BEmployee(hsu.getCurrentPerson().getPersonId())},in.getSearchMetaInput().isUsingPaging());

			}
			ret.fillFromSearchOutput(bSearchOutput);
			
			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchSalesPersonsReturnItem(new BEmployee(in.getId())));
			else if (!hsu.currentlyArahantUser())
					ret.setSelectedItem(new SearchSalesPersonsReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
		
	@WebMethod()
	public SearchProspectsReturn searchProspects(/*@WebParam(name = "in")*/final SearchProspectsInput in)		
	{
		final SearchProspectsReturn ret=new SearchProspectsReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(
				BProspectCompany.searchBySalesperson(in.getSearchMetaInput(), in.getPersonId(), in.getSourceIds(),
					in.getStatusIds(), in.getFromDate(), in.getToDate(), in.getIncludeInactiveStatuses(), in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectTypesReturn searchProspectTypes(/*
	 * @WebParam(name = "in")
	 */final SearchProspectTypesInput in) {
		final SearchProspectTypesReturn ret = new SearchProspectTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectType.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getTypeId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectTypesReturnItem(new BProspectType(new BProspectCompany(in.getTypeId()).getProspectTypeId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectSourcesReturn searchProspectSources(/*@WebParam(name = "in")*/final SearchProspectSourcesInput in) {
		final SearchProspectSourcesReturn ret = new SearchProspectSourcesReturn();
		try {
			checkLogin(in);
			
			ret.fillFromSearchOutput(BProspectSource.search(in.getSearchMetaInput(), in.getCode(), in.getDescription(), in.getExcludeIds()));
			
			if (!isEmpty(in.getSourceId()))
				ret.setSelectedItem(new SearchProspectSourcesReturnItem(new BProspectSource(in.getSourceId())));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectStatusesReturn searchProspectStatuses(/*@WebParam(name = "in")*/final SearchProspectStatusesInput in) {
		final SearchProspectStatusesReturn ret = new SearchProspectStatusesReturn();
		try {
			checkLogin(in);

			ret.fillFromSearchOutput(BProspectStatus.search(in.getSearchMetaInput(), in.getCode(), in.getDescription(), in.getExcludeIds()));

			if (!isEmpty(in.getStatusId())) {
				ret.setSelectedItem(new SearchProspectStatusesReturnItem(new BProspectStatus(in.getStatusId())));
			}
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessCRM"));
			
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
			
			ret.setReportUrl(new SalesProspectReport().build(in.getSearchMetaInput(), in.getPersonId(), 
					in.getFromDate(), in.getToDate(), in.getStatusIds(), 
					in.getSourceIds(), in.getIncludeInactiveStatuses(), in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)		
	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(BEmployee.search(in.getSearchMetaInput(), in.getFirstName(), in.getLastName()));
			
			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(in.getId())));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProspectReturn newProspect(/*@WebParam(name = "in")*/final NewProspectInput in)		
	{
		final NewProspectReturn ret=new NewProspectReturn();
		try
		{
			checkLogin(in);
			
			final BProspectCompany x=new BProspectCompany();
			ret.setId(x.create());
			in.setData(x);
			x.insert();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
