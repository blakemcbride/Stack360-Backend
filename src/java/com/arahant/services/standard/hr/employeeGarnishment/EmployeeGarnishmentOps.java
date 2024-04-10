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
package com.arahant.services.standard.hr.employeeGarnishment;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.EmployeeGarnishmentReport;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmployeeGarnishmentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeGarnishmentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeGarnishmentOps.class);
	
	public EmployeeGarnishmentOps() {
		super();
	}
	
        @WebMethod()
	public ListGarnishmentsReturn listGarnishments(/*@WebParam(name = "in")*/final ListGarnishmentsInput in)		
	{
		final ListGarnishmentsReturn ret=new ListGarnishmentsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BGarnishment.list(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteGarnishmentsReturn deleteGarnishments(/*@WebParam(name = "in")*/final DeleteGarnishmentsInput in)		
	{
		final DeleteGarnishmentsReturn ret=new DeleteGarnishmentsReturn();
		try
		{
			checkLogin(in);
			
			BGarnishment.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public LoadGarnishmentReturn loadGarnishment(/*@WebParam(name = "in")*/final LoadGarnishmentInput in)		
	{
		final LoadGarnishmentReturn ret=new LoadGarnishmentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BGarnishment(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public NewGarnishmentReturn newGarnishment(/*@WebParam(name = "in")*/final NewGarnishmentInput in)		
	{
		final NewGarnishmentReturn ret=new NewGarnishmentReturn();
		try
		{
			checkLogin(in);
			
			final BGarnishment x=new BGarnishment();
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
	public SaveGarnishmentReturn saveGarnishment(/*@WebParam(name = "in")*/final SaveGarnishmentInput in)		
	{
		final SaveGarnishmentReturn ret=new SaveGarnishmentReturn();
		try
		{
			checkLogin(in);
			
			final BGarnishment x=new BGarnishment(in.getId());
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
	public MoveGarnishmentReturn moveGarnishment(/*@WebParam(name = "in")*/final MoveGarnishmentInput in)		
	{
		final MoveGarnishmentReturn ret=new MoveGarnishmentReturn();
		try
		{
			checkLogin(in);
			
			final BGarnishment x=new BGarnishment(in.getId());
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
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)	{
		final CheckRightReturn ret=new CheckRightReturn();
	
		try {
			checkLogin(in);
			
			ret.setAccessLevel(BRight.checkRight("HRTraining"));

			finishService(ret);
		} catch (final Throwable e) {
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
			
			ret.setReportUrl(new EmployeeGarnishmentReport().build(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	
	

        @WebMethod()
	public SearchGarnishmentTypesReturn searchGarnishmentTypes(/*@WebParam(name = "in")*/final SearchGarnishmentTypesInput in)		
	{
		final SearchGarnishmentTypesReturn ret=new SearchGarnishmentTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BGarnishmentType.search(in.getCode(),in.getDescription(),ret.getHighCap()));
			
			//populate the return item
			if (!isEmpty(in.getTypeId()))
				ret.setSelectedItem(new SearchGarnishmentTypesReturnItem(new BGarnishmentType(in.getTypeId())));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
