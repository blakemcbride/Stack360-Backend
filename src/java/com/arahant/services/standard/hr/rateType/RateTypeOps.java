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
package com.arahant.services.standard.hr.rateType;

import com.arahant.beans.Right;
import com.arahant.business.BRateType;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrRateTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class RateTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(RateTypeOps.class);
	
	public RateTypeOps() {
	}
	
	
	@WebMethod()
	public ListRateTypesReturn listRateTypes(/*@WebParam(name = "in")*/final ListRateTypesInput in)			{
		final ListRateTypesReturn ret=new ListRateTypesReturn();
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
	public NewRateTypeReturn newRateType(/*@WebParam(name = "in")*/final NewRateTypeInput in)			{
		final NewRateTypeReturn ret=new NewRateTypeReturn();
		try
		{
			checkLogin(in);
			
			final BRateType x=new BRateType();
			ret.setId(x.create());
                        if (in.isApplyToAll())
                            x.setAllCompanies(true);
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
	public SaveRateTypeReturn saveRateType(/*@WebParam(name = "in")*/final SaveRateTypeInput in)			{
		final SaveRateTypeReturn ret=new SaveRateTypeReturn();
		try
		{
			checkLogin(in);
			
			final BRateType x=new BRateType(in.getId());
                        if(in.isApplyToAll())
                            x.setAllCompanies(true);
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
	public DeleteRateTypesReturn deleteRateTypes(/*@WebParam(name = "in")*/final DeleteRateTypesInput in)			{
		final DeleteRateTypesReturn ret=new DeleteRateTypesReturn();
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
	public GetRateTypesReportReturn getRateTypesReport(/*@WebParam(name = "in")*/final GetRateTypesReportInput in)			{
		final GetRateTypesReportReturn ret=new GetRateTypesReportReturn();
		try
		{
			checkLogin(in);
			
//			ret.setReportUrl(BRateType.getReport('E'));
			
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

                        if ((BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES)==BRight.ACCESS_LEVEL_WRITE) && ArahantSession.multipleCompanySupport)
                        {
                            ret.setAllCompanyAccess(true);
                        }
                        else
                            ret.setAllCompanyAccess(false);
			
			ret.setAccessLevel(BRight.checkRight("HREvents"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	

}
