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
package com.arahant.services.standard.hr.reference;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrReferenceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ReferenceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ReferenceOps.class);
	
	public ReferenceOps() {
		super();
	}
	
    @WebMethod()
	public ListReferencesReturn listReferences(/*@WebParam(name = "in")*/final ListReferencesInput in)		
	{
		final ListReferencesReturn ret=new ListReferencesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BPersonalReference.list(in.getEmployeeId(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
    public DeleteReferencesReturn deleteReferences(/*@WebParam(name = "in")*/final DeleteReferencesInput in)		
	{
		final DeleteReferencesReturn ret=new DeleteReferencesReturn();
		try
		{
			checkLogin(in);
			
			BPersonalReference.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
    @WebMethod()
	public LoadReferenceReturn loadReference(/*@WebParam(name = "in")*/final LoadReferenceInput in)		
	{
		final LoadReferenceReturn ret=new LoadReferenceReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BPersonalReference(in.getReferenceId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public NewReferenceReturn newReference(/*@WebParam(name = "in")*/final NewReferenceInput in)		
	{
		final NewReferenceReturn ret=new NewReferenceReturn();
		try
		{
			checkLogin(in);
			
			final BPersonalReference x=new BPersonalReference();
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
	public SaveReferenceReturn saveReference(/*@WebParam(name = "in")*/final SaveReferenceInput in)		
	{
		final SaveReferenceReturn ret=new SaveReferenceReturn();
		try
		{
			checkLogin(in);
			
			final BPersonalReference x=new BPersonalReference(in.getReferenceId());
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
