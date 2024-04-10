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
package com.arahant.services.standard.billing.billingStatus;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.BillingStatusReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardBillingBillingStatusOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BillingStatusOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BillingStatusOps.class);
	
	public BillingStatusOps() {
		super();
	}
	
        @WebMethod()
	public ListBillingStatusesReturn listBillingStatuses(/*@WebParam(name = "in")*/final ListBillingStatusesInput in)		
	{
		final ListBillingStatusesReturn ret=new ListBillingStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BBillingStatus.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewBillingStatusReturn newBillingStatus(/*@WebParam(name = "in")*/final NewBillingStatusInput in)		
	{
		final NewBillingStatusReturn ret=new NewBillingStatusReturn();
		try
		{
			checkLogin(in);
			
			final BBillingStatus x=new BBillingStatus();
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
	public SaveBillingStatusReturn saveBillingStatus(/*@WebParam(name = "in")*/final SaveBillingStatusInput in)		
	{
		final SaveBillingStatusReturn ret=new SaveBillingStatusReturn();
		try
		{
			checkLogin(in);
			
			final BBillingStatus x=new BBillingStatus(in.getId());
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
        public DeleteBillingStatusesReturn deleteBillingStatuses(/*@WebParam(name = "in")*/final DeleteBillingStatusesInput in)		
	{
		final DeleteBillingStatusesReturn ret=new DeleteBillingStatusesReturn();
		try
		{
			checkLogin(in);
			
			BBillingStatus.delete(in.getIds());
			
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
		
			ret.setReportUrl(new BillingStatusReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();


		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessHR"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}
