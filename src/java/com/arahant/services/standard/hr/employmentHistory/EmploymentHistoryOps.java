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
 *
 */
package com.arahant.services.standard.hr.employmentHistory;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEmploymentHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmploymentHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmploymentHistoryOps.class);
	
	public EmploymentHistoryOps() {
		super();
	}
	
    @WebMethod()
	public ListEmploymentHistoryReturn listEmploymentHistory(/*@WebParam(name = "in")*/final ListEmploymentHistoryInput in)		
	{
		final ListEmploymentHistoryReturn ret=new ListEmploymentHistoryReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPreviousEmployment.list(in.getEmployeeId(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteEmploymentHistoryReturn deleteEmploymentHistory(/*@WebParam(name = "in")*/final DeleteEmploymentHistoryInput in)		
	{
		final DeleteEmploymentHistoryReturn ret=new DeleteEmploymentHistoryReturn();
		try
		{
			checkLogin(in);
			
			BPreviousEmployment.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public LoadEmploymentHistoryReturn loadEmploymentHistory(/*@WebParam(name = "in")*/final LoadEmploymentHistoryInput in)		
	{
		final LoadEmploymentHistoryReturn ret=new LoadEmploymentHistoryReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPreviousEmployment(in.getEmploymentId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewEmploymentHistoryReturn newEmploymentHistory(/*@WebParam(name = "in")*/final NewEmploymentHistoryInput in)		
	{
		final NewEmploymentHistoryReturn ret=new NewEmploymentHistoryReturn();
		try
		{
			checkLogin(in);
			
			final BPreviousEmployment x=new BPreviousEmployment();
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
	public SaveEmploymentHistoryReturn saveEmploymentHistory(/*@WebParam(name = "in")*/final SaveEmploymentHistoryInput in)		
	{
		final SaveEmploymentHistoryReturn ret=new SaveEmploymentHistoryReturn();
		try
		{
			checkLogin(in);
			
			final BPreviousEmployment x=new BPreviousEmployment(in.getEmploymentId());
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
