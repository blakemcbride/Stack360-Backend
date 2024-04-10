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
package com.arahant.services.standard.hr.garnishmentType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.GarnishmentTypeReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrGarnishmentTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class GarnishmentTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			GarnishmentTypeOps.class);
	
	public GarnishmentTypeOps() {
		super();
	}
	
        @WebMethod()
	public ListTypesReturn listTypes(/*@WebParam(name = "in")*/final ListTypesInput in)		
	{
		final ListTypesReturn ret=new ListTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BGarnishmentType.list(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewTypeReturn newType(/*@WebParam(name = "in")*/final NewTypeInput in)		
	{
		final NewTypeReturn ret=new NewTypeReturn();
		try
		{
			checkLogin(in);
			
			final BGarnishmentType x=new BGarnishmentType();
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
	public SaveTypeReturn saveType(/*@WebParam(name = "in")*/final SaveTypeInput in)		
	{
		final SaveTypeReturn ret=new SaveTypeReturn();
		try
		{
			checkLogin(in);
			
			final BGarnishmentType x=new BGarnishmentType(in.getId());
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
        public DeleteTypesReturn deleteTypes(/*@WebParam(name = "in")*/final DeleteTypesInput in)		
	{
		final DeleteTypesReturn ret=new DeleteTypesReturn();
		try
		{
			checkLogin(in);
			
			BGarnishmentType.delete(in.getIds());
			
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
			
			ret.setReportUrl(new GarnishmentTypeReport().build(in.getActiveType()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
		public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		{
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

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*@WebParam(name = "in")*/final ListWageTypesInput in)		
	{
		final ListWageTypesReturn ret=new ListWageTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BWageType.list(ret.getHighCap()));
			

			if (!isEmpty(in.getWageTypeId()))
				ret.setSelectedItem(new ListWageTypesReturnItem(new BWageType(in.getWageTypeId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
