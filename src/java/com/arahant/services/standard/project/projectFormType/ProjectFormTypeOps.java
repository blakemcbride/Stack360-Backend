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
package com.arahant.services.standard.project.projectFormType;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BFormType;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardProjectProjectFormTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProjectFormTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ProjectFormTypeOps.class);
	
	public ProjectFormTypeOps() {
		super();
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("AccessProjects"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
	
	
	@WebMethod()
	public ListFormTypesReturn listFormTypes(/*@WebParam(name = "in")*/final ListFormTypesInput in)			{
		final ListFormTypesReturn ret=new ListFormTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BFormType.list('P'));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public NewFormTypeReturn newFormType(/*@WebParam(name = "in")*/final NewFormTypeInput in)			{
		final NewFormTypeReturn ret=new NewFormTypeReturn();
		try
		{
			checkLogin(in);
			
			final BFormType x=new BFormType();
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
	public SaveFormTypeReturn saveFormType(/*@WebParam(name = "in")*/final SaveFormTypeInput in)			{
		final SaveFormTypeReturn ret=new SaveFormTypeReturn();
		try
		{
			checkLogin(in);
			
			final BFormType x=new BFormType(in.getId());
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
	public DeleteFormTypesReturn deleteFormTypes(/*@WebParam(name = "in")*/final DeleteFormTypesInput in)			{
		final DeleteFormTypesReturn ret=new DeleteFormTypesReturn();
		try
		{
			checkLogin(in);
			
			BFormType.delete(hsu,in.getIds());
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetFormTypesReportReturn getFormTypesReport(/*@WebParam(name = "in")*/final GetFormTypesReportInput in)			{
		final GetFormTypesReportReturn ret=new GetFormTypesReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(BFormType.getReport('P'));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
