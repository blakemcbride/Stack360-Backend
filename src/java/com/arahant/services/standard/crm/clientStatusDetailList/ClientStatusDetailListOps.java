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
package com.arahant.services.standard.crm.clientStatusDetailList;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.ClientStatusReport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmClientStatusDetailListOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ClientStatusDetailListOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ClientStatusDetailListOps.class);
	
	public ClientStatusDetailListOps() {
		super();
	}
	
    @WebMethod()
	public ListClientStatusDetailReturn listClientStatusDetail(/*@WebParam(name = "in")*/final ListClientStatusDetailInput in)		
	{
		final ListClientStatusDetailReturn ret=new ListClientStatusDetailReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BClientCompany.listActiveClients(ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetClientDetailReportReturn getClientDetailReport(/*@WebParam(name = "in")*/final GetClientDetailReportInput in)		
	{
		final GetClientDetailReportReturn ret=new GetClientDetailReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new ClientStatusReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListClientStatusesReturn listClientStatuses(/*@WebParam(name = "in")*/final ListClientStatusesInput in)		
	{
		final ListClientStatusesReturn ret=new ListClientStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BClientStatus.list(true));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveClientStatusReturn saveClientStatus(/*@WebParam(name = "in")*/final SaveClientStatusInput in)		
	{
		final SaveClientStatusReturn ret=new SaveClientStatusReturn();
		try
		{
			checkLogin(in);
			
			final BClientCompany x=new BClientCompany(in.getId());
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
