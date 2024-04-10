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
package com.arahant.services.standard.crm.salesPipeline;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmSalesPipelineOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class SalesPipelineOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			SalesPipelineOps.class);
	
	public SalesPipelineOps() {
		super();
	}
	
    @WebMethod()
	public GetMetaReturn getMeta(/*@WebParam(name = "in")*/final GetMetaInput in)		
	{
		final GetMetaReturn ret=new GetMetaReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProspectStatus.listPipelineStatuses());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public ListPipelineDataReturn listPipelineData(/*@WebParam(name = "in")*/final ListPipelineDataInput in)		
	{
		final ListPipelineDataReturn ret=new ListPipelineDataReturn();
		try
		{
			checkLogin(in);

			if (isEmpty(in.getPersonId()))
				ret.setItem(BProspectStatus.listPipelineStatuses());
			else
				ret.setItem(BProspectStatus.listPipelineStatuses(), new BEmployee(in.getPersonId()).getEmployee());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public ListPipelineStatusesReturn listPipelineStatuses(/*@WebParam(name = "in")*/final ListPipelineStatusesInput in)		
	{
		final ListPipelineStatusesReturn ret=new ListPipelineStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProspectStatus.listPipelineStatuses());

			if (!isEmpty(in.getProspectId()))
			{
				ret.setSelectedId(new BProspectCompany(in.getProspectId()).getStatus().getId());
			}
			else
				ret.setSelectedId("");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public SaveProspectDataReturn saveProspectData(/*@WebParam(name = "in")*/final SaveProspectDataInput in)		
	{
		final SaveProspectDataReturn ret=new SaveProspectDataReturn();
		try
		{
			checkLogin(in);
			
			final BProspectCompany x=new BProspectCompany(in.getProspectId());
			in.setData(x);
			x.update();
			
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
	public SearchProspectSourcesReturn searchProspectSources(/*@WebParam(name = "in")*/final SearchProspectSourcesInput in) {
		final SearchProspectSourcesReturn ret = new SearchProspectSourcesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectSource.search(in.getCode(), in.getDescription(), ret.getHighCap()));
			
			if (!isEmpty(in.getId())) {
				BProspectCompany bpc = new BProspectCompany(in.getId());
				ret.setSelectedItem(new SearchProspectSourcesReturnItem(bpc.getSource()));
			}
			
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

			ret.setItem(BProspectStatus.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				BProspectCompany bpc = new BProspectCompany(in.getId());
				ret.setSelectedItem(new SearchProspectStatusesReturnItem(bpc.getStatus()));
			}
			
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadSummaryReturn loadSummary(/*@WebParam(name = "in")*/final LoadSummaryInput in)		
	{
		final LoadSummaryReturn ret=new LoadSummaryReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BProspectCompany(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveSummaryReturn saveSummary(/*@WebParam(name = "in")*/final SaveSummaryInput in)		
	{
		final SaveSummaryReturn ret=new SaveSummaryReturn();
		try
		{
			checkLogin(in);
			
			final BProspectCompany x=new BProspectCompany(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
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

			ret.setItem(BSalesActivity.list(ret.getCap()));
			
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
}
