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
package com.arahant.services.standard.time.timeOffRequest;
import com.arahant.beans.TimeOffRequest;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.TimeOffRequestReport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeTimeOffRequestOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimeOffRequestOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			TimeOffRequestOps.class);
	
	public TimeOffRequestOps() {
		super();
	}
	
    @WebMethod()
	public SearchTimeOffRequestsReturn searchTimeOffRequests(/*@WebParam(name = "in")*/final SearchTimeOffRequestsInput in)		
	{
		final SearchTimeOffRequestsReturn ret=new SearchTimeOffRequestsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BTimeOffRequest.search(in.getPersonId(),in.getFromDate(),in.getToDate(),null,ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadTimeOffRequestReturn loadTimeOffRequest(/*@WebParam(name = "in")*/final LoadTimeOffRequestInput in)		
	{
		final LoadTimeOffRequestReturn ret=new LoadTimeOffRequestReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BTimeOffRequest(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewTimeOffRequestReturn newTimeOffRequest(/*@WebParam(name = "in")*/final NewTimeOffRequestInput in)		
	{
		final NewTimeOffRequestReturn ret=new NewTimeOffRequestReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffRequest x=new BTimeOffRequest();
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
	public SaveTimeOffRequestReturn saveTimeOffRequest(/*@WebParam(name = "in")*/final SaveTimeOffRequestInput in)		
	{
		final SaveTimeOffRequestReturn ret=new SaveTimeOffRequestReturn();
		try
		{
			checkLogin(in);
			
			final BTimeOffRequest x=new BTimeOffRequest(in.getId());
			if (x.getRequestState()!=TimeOffRequest.ORIGINATED)
				throw new ArahantWarning("Request can not be changed after it has been submitted.");
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
    public DeleteTimeOffRequestsReturn deleteTimeOffRequests(/*@WebParam(name = "in")*/final DeleteTimeOffRequestsInput in)		
	{
		final DeleteTimeOffRequestsReturn ret=new DeleteTimeOffRequestsReturn();
		try
		{
			checkLogin(in);
			
			BTimeOffRequest.delete(in.getIds());
			
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
			
			ret.setReportUrl(new TimeOffRequestReport().build(in.getPersonId(), in.getFromDate(), in.getToDate()));
			
			finishService(ret);
		}
		catch (final Exception e) {
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
			boolean includeUser=false;
			if (BRight.checkRight(SEE_SELF_IN_SEARCHES)==ACCESS_LEVEL_WRITE)
				includeUser=true;
			
			if (hsu.currentlyArahantUser())
				includeUser=false;
			
			ret.setEmployees(BPerson.getCurrent().searchSubordinates(hsu, in.getSsn(),in.getFirstName(), in.getLastName(), null, 0, ret.getHighCap(), 1,includeUser));
			
			if (!hsu.currentlyArahantUser())
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BEmployee(hsu.getCurrentPerson().getPersonId())));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
    @WebMethod()
	public ListEventsReturn listEvents(/*@WebParam(name = "in")*/final ListEventsInput in)		
	{
		final ListEventsReturn ret=new ListEventsReturn();
		try
		{
			checkLogin(in);

			BEvent []reqs=BTimeOffRequest.search(in.getPersonId(),in.getDate(),new String[]{in.getCurrentEventId()},ret.getCap());
			
			BEvent []appts=BAppointment.search(in.getDate(), in.getDate(), "", 0, in.getPersonId(), "", "", ret.getCap()-reqs.length);
			List <BEvent> events=new ArrayList<BEvent>();
			
			Collections.addAll(events, reqs);
			Collections.addAll(events, appts);
		
			ret.setItem(events.toArray(new BEvent[events.size()]),in.getDate());
						
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchTimeOffProjectsReturn searchTimeOffProjects(/*@WebParam(name = "in")*/final SearchTimeOffProjectsInput in)		
	{
		final SearchTimeOffProjectsReturn ret=new SearchTimeOffProjectsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(BHRAccruedTimeOff.listAccrualProjects(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public ListAccruedTimeOffReturn listAccruedTimeOff(/*@WebParam(name = "in")*/final ListAccruedTimeOffInput in)		
	{
		final ListAccruedTimeOffReturn ret=new ListAccruedTimeOffReturn();
		try
		{
			checkLogin(in);
			
			BEmployee bemp=new BEmployee(in.getPersonId());
			
			//get all benefit id's that this guy has access to
			BProject [] projs=BHRAccruedTimeOff.listAccrualProjects(in.getPersonId());
			
			ListAccruedTimeOffReturnItem [] i=new ListAccruedTimeOffReturnItem[projs.length];
			
			for (int loop=0;loop<i.length;loop++)
			{
				i[loop]=new ListAccruedTimeOffReturnItem();
				i[loop].setName(projs[loop].getBenefitName());
				i[loop].setHours(projs[loop].getBenefitAccrual(bemp));
			}
			

			ret.setItem(i);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
