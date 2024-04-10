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
package com.arahant.services.standard.tutorial.webServiceTest;
import com.arahant.beans.FormType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
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
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTutorialWebServiceTestOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class WebServiceTestOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			WebServiceTestOps.class);
	
	public WebServiceTestOps() {
		super();
	}
	
	


	@WebMethod()
	public BasicWebServiceReturn basicWebService(/*@WebParam(name = "in")*/final BasicWebServiceInput in)
	{
		final BasicWebServiceReturn ret=new BasicWebServiceReturn();
		try
		{
			checkLogin(in);

			if(in.getServiceId() == 10)
			{
				int x = 10;
			}

			//System.out.println("Received Service " + in.getServiceId());
			ArahantSession.getHSU().createCriteria(FormType.class).list();
			Thread.sleep(in.getSleep()*1000);

			ret.setServiceId(in.getServiceId());
			//System.out.println("Finishing Service " + in.getServiceId());
			finishService(ret);
		}
		catch (final Exception e) {
			ret.setStackTrace(e.getMessage());
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
