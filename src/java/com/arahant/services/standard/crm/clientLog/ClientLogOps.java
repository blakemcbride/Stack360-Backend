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
package com.arahant.services.standard.crm.clientLog;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ClientLogReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmClientLogOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ClientLogOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ClientLogOps.class);
	
	public ClientLogOps() {
		super();
	}
	
   

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new ClientLogReport().build(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public ListLogsReturn listLogs(/*@WebParam(name = "in")*/final ListLogsInput in)		
	{
		final ListLogsReturn ret=new ListLogsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProspectLog.list(in.getId()));
			
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
        public DeleteLogsReturn deleteLogs(/*@WebParam(name = "in")*/final DeleteLogsInput in)		
	{
		final DeleteLogsReturn ret=new DeleteLogsReturn();
		try
		{
			checkLogin(in);
			
			BProspectLog.delete(in.getIds());
			
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
	public SaveLogReturn saveLog(/*@WebParam(name = "in")*/final SaveLogInput in)		
	{
		final SaveLogReturn ret=new SaveLogReturn();
		try
		{
			checkLogin(in);
			
			final BProspectLog x=new BProspectLog(in.getId());
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
