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
package com.arahant.services.standard.hr.cignaEnrollmentForm;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BEmployee;
import com.arahant.business.BPersonChangeRequest;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;    
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrCignaEnrollmentFormOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class CignaEnrollmentFormOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			CignaEnrollmentFormOps.class);
	
	public CignaEnrollmentFormOps() {
		super();
	}
	
        @WebMethod()
	public NewCignaEnrollmentFormReturn newCignaEnrollmentForm(/*@WebParam(name = "in")*/final NewCignaEnrollmentFormInput in)		
	{
		final NewCignaEnrollmentFormReturn ret=new NewCignaEnrollmentFormReturn();
		try
		{
			checkLogin(in);

                        BPersonChangeRequest bcr=new BPersonChangeRequest();
                        bcr.create();
                        bcr.setData(in.toXML());
                        bcr.setType(PersonChangeRequest.TYPE_CIGNA);
                        bcr.setPerson(hsu.getCurrentPerson());
                        bcr.insert();


                        System.out.println(in.toXML());
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetMetaReturn getMeta(/*@WebParam(name = "in")*/final GetMetaInput in)		
	{
		final GetMetaReturn ret=new GetMetaReturn();
		try
		{
			checkLogin(in);
			
            //they want them to always access            ret.setCanAccess(!BPersonChangeRequest.currentUserHasPending(PersonChangeRequest.TYPE_CIGNA));
			ret.setCanAccess(true);
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


        @WebMethod()
	public LoadCignaEnrollmentFormReturn loadCignaEnrollmentForm(/*@WebParam(name = "in")*/final LoadCignaEnrollmentFormInput in)		
	{
		final LoadCignaEnrollmentFormReturn ret=new LoadCignaEnrollmentFormReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(BEmployee.getCurrent().getBEmployee());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
