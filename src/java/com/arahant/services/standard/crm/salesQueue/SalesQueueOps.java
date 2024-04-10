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
package com.arahant.services.standard.crm.salesQueue;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmSalesQueueOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class SalesQueueOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			SalesQueueOps.class);
	
	public SalesQueueOps() {
		super();
	}
	
    @WebMethod()
	public SearchSalesQueueReturn searchSalesQueue(/*@WebParam(name = "in")*/final SearchSalesQueueInput in)		
	{
		final SearchSalesQueueReturn ret=new SearchSalesQueueReturn();
		try
		{
			checkLogin(in);

			ret.fillFromSearchOutput(BProspectCompany.searchSalesQueue(ret.getCap(), in.getProspectName(), in.getProspectStatusId(), in.getContactDate(), in.getLastContactFrom(), in.getLastContactTo(), in.getActivityId(), in.getResultId(), in.getActive(), in.getSearchMetaInput(), in.getEmployeeId()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchProspectStatusesReturn searchProspectStatuses(/*@WebParam(name = "in")*/final SearchProspectStatusesInput in) {
		final SearchProspectStatusesReturn ret = new SearchProspectStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectStatus.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getStatusId())) 
				ret.setSelectedItem(new SearchProspectStatusesReturnItem(new BProspectStatus(in.getStatusId())));
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListActivitiesReturn listActivities(/*@WebParam(name = "in")*/final ListActivitiesInput in)		
	{
		final ListActivitiesReturn ret=new ListActivitiesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BSalesActivity.list(ret.getCap(), true, false));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListResultsReturn listResults(/*@WebParam(name = "in")*/final ListResultsInput in)		
	{
		final ListResultsReturn ret=new ListResultsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BSalesActivityResult.listActives(ret.getCap(), in.getActivityId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadLogReturn loadLog(/*@WebParam(name = "in")*/final LoadLogInput in)		
	{
		final LoadLogReturn ret=new LoadLogReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProspectLog(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewLogReturn newLog(/*@WebParam(name = "in")*/final NewLogInput in)		
	{
		final NewLogReturn ret=new NewLogReturn();
		try
		{
			checkLogin(in);
			
			final BProspectLog x=new BProspectLog();
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

	@WebMethod()
	public ListContactsReturn listContacts(/*@WebParam(name = "in")*/final ListContactsInput in)		
	{
		final ListContactsReturn ret=new ListContactsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BOrgGroup(in.getOrgGroupId()).listPeople(ret.getCap()),in.getOrgGroupId());
			
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

			boolean includeUser=false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES)==ACCESS_LEVEL_WRITE)
				includeUser=true;

			if (hsu.currentlySuperUser())
				includeUser=false;


			BEmployee []emps=BPerson.getCurrent().searchSubordinates(hsu, "",in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser);

			ret.setItem(emps);

			if (in.getIncludeSelected() && !hsu.currentlySuperUser())
				ret.setSelectedItem(new SearchEmployeesReturnItem(BPerson.getCurrent().getBEmployee()));
				
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
