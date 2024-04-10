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
package com.arahant.services.standard.tutorial.tutorial5;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTutorialTutorial5Ops")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class Tutorial5Ops extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			Tutorial5Ops.class);
	
	public Tutorial5Ops() {
		super();
	}
	

	@WebMethod()
	public GetFullNameReturn getFullName(/*@WebParam(name = "in")*/final GetFullNameInput in)	{
		final GetFullNameReturn ret=new GetFullNameReturn();
		try
		{
			checkLogin(in);
			
			ret.setFullName(in.getLastName()+", "+in.getFirstName());
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
