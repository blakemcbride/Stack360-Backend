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
package com.arahant.services.standard.hr.paySchedule;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.PayScheduleReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrPayScheduleOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PayScheduleOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			PayScheduleOps.class);
	
	public PayScheduleOps() {
		super();
	}
	
    @WebMethod()
	public ListPaySchedulesReturn listPaySchedules(/*@WebParam(name = "in")*/final ListPaySchedulesInput in)		
	{
		final ListPaySchedulesReturn ret=new ListPaySchedulesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPaySchedule.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewPayScheduleReturn newPaySchedule(/*@WebParam(name = "in")*/final NewPayScheduleInput in)		
	{
		final NewPayScheduleReturn ret=new NewPayScheduleReturn();
		try
		{
			checkLogin(in);
			
			final BPaySchedule x=new BPaySchedule();
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
	public SavePayScheduleReturn savePaySchedule(/*@WebParam(name = "in")*/final SavePayScheduleInput in)		
	{
		final SavePayScheduleReturn ret=new SavePayScheduleReturn();
		try
		{
			checkLogin(in);
			
			final BPaySchedule x=new BPaySchedule(in.getId());
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
	public DeletePayScheduleReturn deletePaySchedule(/*@WebParam(name = "in")*/final DeletePayScheduleInput in)		
	{
		final DeletePayScheduleReturn ret=new DeletePayScheduleReturn();
		try
		{
			checkLogin(in);
			
			new BPaySchedule(in.getId()).delete();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SearchPayPeriodsReturn searchPayPeriods(/*@WebParam(name = "in")*/final SearchPayPeriodsInput in)		
	{
		final SearchPayPeriodsReturn ret=new SearchPayPeriodsReturn();
		try
		{
			checkLogin(in);
			
			BPaySchedulePeriod p[]=BPaySchedulePeriod.search(in.getId(),in.getFromDate(),in.getToDate(),ret.getCap());
  			ret.setItem(p);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public LoadDefaultPayPeriodsReturn loadDefaultPayPeriods(/*@WebParam(name = "in")*/final LoadDefaultPayPeriodsInput in)		
	{
		final LoadDefaultPayPeriodsReturn ret=new LoadDefaultPayPeriodsReturn();
		try
		{
			checkLogin(in);
			BPaySchedule bps=new BPaySchedule(in.getId());
			switch (in.getPeriodType())
			{
				case 1 : bps.generateWeekly(in.getFromDate(), in.getToDate(), in.getPeriodStart(), 1, in.getSetFirstPayPeriodAsBeginning());
						break;
				case 2 : bps.generateWeekly(in.getFromDate(), in.getToDate(), in.getPeriodStart(), 2, in.getSetFirstPayPeriodAsBeginning());
						break;
				case 3 : bps.generateMonthly(in.getPeriodStart(), in.getFromDate(), in.getToDate(), in.getSetFirstPayPeriodAsBeginning());
						break;
				case 4 : bps.generateTwiceMonthly(in.getPeriodStart(), in.getPeriodStart2(), in.getFromDate(), in.getToDate(), in.getSetFirstPayPeriodAsBeginning());
						break;
			}
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public NewPayPeriodReturn newPayPeriod(/*@WebParam(name = "in")*/final NewPayPeriodInput in)		
	{
		final NewPayPeriodReturn ret=new NewPayPeriodReturn();
		try
		{
			checkLogin(in);
			
			final BPaySchedulePeriod x=new BPaySchedulePeriod();
			x.create();
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
    public DeletePayPeriodsReturn deletePayPeriods(/*@WebParam(name = "in")*/final DeletePayPeriodsInput in)		
	{
		final DeletePayPeriodsReturn ret=new DeletePayPeriodsReturn();
		try
		{
			checkLogin(in);
			
			BPaySchedulePeriod.delete(in.getIds());
			
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
			
			ret.setReportUrl(new PayScheduleReport().build(in.getId(), in.getFromDate(), in.getToDate()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

    @WebMethod()
	public SavePayPeriodReturn savePayPeriod(/*@WebParam(name = "in")*/final SavePayPeriodInput in)		
	{
		final SavePayPeriodReturn ret=new SavePayPeriodReturn();
		try
		{
			checkLogin(in);
			
			final BPaySchedulePeriod x=new BPaySchedulePeriod(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
