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

package com.arahant.services.standard.hr.billingRateSetup;

import com.arahant.business.BRateType;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;


/**
 * 
 *
 * Created on November 24, 2016
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBillingRateSetupOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BillingRateSetupOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(BillingRateSetupOps.class);

	public BillingRateSetupOps() {
		super();
	}
	
	@WebMethod()
	public DeleteBillingRateReturn deleteBillingRate(/*@WebParam(name = "in")*/final DeleteBillingRateInput in)	{
		final DeleteBillingRateReturn ret=new DeleteBillingRateReturn();
		try
		{
			checkLogin(in);

			BRateType.delete(in.getIds());

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);

			//ret.setFileName(BRateType.getReport());

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListReturn list(/*@WebParam(name = "in")*/final ListInput in)	{
		final ListReturn ret=new ListReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BRateType.list());

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewBillingRateReturn newBillingRate(/*@WebParam(name = "in")*/final NewBillingRateInput in)			{
		final NewBillingRateReturn ret=new NewBillingRateReturn();
		try
		{
			checkLogin(in);

			final BRateType x = new BRateType();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveReturn save(/*@WebParam(name = "in")*/final SaveInput in)	{
		final SaveReturn ret=new SaveReturn();
		try
		{
			checkLogin(in);
			
			final BRateType x=new BRateType(in.getId());
			in.setData(x);
			x.update();
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	} 		
}

	
