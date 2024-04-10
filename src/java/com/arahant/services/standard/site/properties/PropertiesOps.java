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
package com.arahant.services.standard.site.properties;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardSitePropertiesOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class PropertiesOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			PropertiesOps.class);
	
	public PropertiesOps() {
		super();
	}
	
        @WebMethod()
	public NewPropertyReturn newProperty(/*@WebParam(name = "in")*/final NewPropertyInput in)		
	{
		final NewPropertyReturn ret=new NewPropertyReturn();
		try
		{
			checkLogin(in);
			
			final BProperty x=new BProperty();
			ret.setId(in.getName());
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
	public SavePropertyReturn saveProperty(/*@WebParam(name = "in")*/final SavePropertyInput in)		
	{
		final SavePropertyReturn ret=new SavePropertyReturn();
		try
		{
			checkLogin(in);
			
			final BProperty x=new BProperty(in.getId());
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
	public ListPropertiesReturn listProperties(/*@WebParam(name = "in")*/final ListPropertiesInput in)		
	{
		final ListPropertiesReturn ret=new ListPropertiesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BProperty.list());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeletePropertiesReturn deleteProperties(/*@WebParam(name = "in")*/final DeletePropertiesInput in)		
	{
		final DeletePropertiesReturn ret=new DeletePropertiesReturn();
		try
		{
			checkLogin(in);
			
			BProperty.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetPropertiesReportReturn getPropertiesReport(/*@WebParam(name = "in")*/final GetPropertiesReportInput in)		
	{
		final GetPropertiesReportReturn ret=new GetPropertiesReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(BProperty.getReport());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
