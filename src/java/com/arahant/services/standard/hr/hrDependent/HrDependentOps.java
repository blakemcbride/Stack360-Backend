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
 * Created on Jun 11, 2007
 * 
 */
package com.arahant.services.standard.hr.hrDependent;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**      
 *        
 *    
 * Created on Jun 11, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrDependentOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrDependentOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			HrDependentOps.class);

	
	@WebMethod()
	public ListDependentsReturn listDependents (/*@WebParam(name = "in")*/final ListDependentsInput in)	{
		final ListDependentsReturn ret=new ListDependentsReturn();
		try
		{
			checkLogin(in);
			
			ret.setItem(new BEmployee(in.getEmployeeId()).listDependents());
			ret.setTaskComplete(true);
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public NewDependentReturn newDependent (/*@WebParam(name = "in")*/final NewDependentInput in)	{
		final NewDependentReturn ret=new NewDependentReturn();
		try
		{
			checkLogin(in);
			
			final BHREmplDependent d=new BHREmplDependent();
			
			if (!isEmpty(in.getPersonId()))
			{
				d.create(in.getPersonId(),in.getEmployeeId(),in.getRelationshipType());
				ret.setDependentId(d.getDependentId());
				
				if (new BPerson(in.getPersonId()).getBEmployee()==null)
				{
					in.setData(d);
				}
				else
				{
					in.setDataSubset(d);
				}
			}
			else
			{	
				ret.setDependentId(d.create());
				in.setData(d);
			}
			d.insert();
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public SaveDependentReturn saveDependent (/*@WebParam(name = "in")*/final SaveDependentInput in)	{
		final SaveDependentReturn ret=new SaveDependentReturn();
		try
		{
			checkLogin(in);
			
			final BHREmplDependent d=new BHREmplDependent(in.getDependentId());
			
			if (!isEmpty(in.getPersonId()))
			{
				d.moveTo(in.getPersonId());
				
			}
			else
			{
				if (new BPerson(d.getPerson()).getBEmployee()!=null)
				{
					in.setDataSubset(d);
					d.update();
				}
				else
				{
					in.setData(d);
					d.update();
				}
			}
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	
	@WebMethod()
	public DeleteDependentReturn deleteDependent (/*@WebParam(name = "in")*/final DeleteDependentInput in)	{
		final DeleteDependentReturn ret=new DeleteDependentReturn();
		try
		{
			checkLogin(in);
			
			ret.setDeactivationOccured(BHREmplDependent.delete(in.getDependentIds()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public GetReportReturn getReport (/*@WebParam(name = "in")*/final GetReportInput in)	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
	 		
			ret.setReportUrl(new BEmployee(in.getEmployeeId()).getReport());
			
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
			
			ret.setAccessLevel(BRight.checkRight("AccessHrDependent"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	} 	
	@WebMethod()
	public LoadDependentReturn loadDependent(/*@WebParam(name = "in")*/final LoadDependentInput in)			{
		final LoadDependentReturn ret=new LoadDependentReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BHREmplDependent(in.getDependentId()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public SearchPersonsReturn searchPersons(/*@WebParam(name = "in")*/final SearchPersonsInput in)			{
		final SearchPersonsReturn ret=new SearchPersonsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).searchEmployeesForDependent(in.getFirstName(),in.getLastName(),in.getSsn(),ret.getCap()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public LoadSpouseSexReturn loadSpouseSex(/*@WebParam(name = "in")*/final LoadSpouseSexInput in)			{
		final LoadSpouseSexReturn ret=new LoadSpouseSexReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BEmployee(in.getEmployeeId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public ListDependeesReturn listDependees(/*@WebParam(name = "in")*/final ListDependeesInput in)			{
		final ListDependeesReturn ret=new ListDependeesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmplDependent.listDependees(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public GetDependeeReportReturn getDependeeReport(/*@WebParam(name = "in")*/final GetDependeeReportInput in)			{
		final GetDependeeReportReturn ret=new GetDependeeReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(BHREmplDependent.getDependeeReport(in.getPersonId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
