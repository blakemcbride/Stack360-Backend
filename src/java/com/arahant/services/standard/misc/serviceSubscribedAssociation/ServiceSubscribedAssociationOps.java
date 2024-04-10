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
package com.arahant.services.standard.misc.serviceSubscribedAssociation;
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
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscServiceSubscribedAssociationOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ServiceSubscribedAssociationOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ServiceSubscribedAssociationOps.class);
	
	public ServiceSubscribedAssociationOps() {
		super();
	}
	
        @WebMethod()
	public SearchAvailableServicesReturn searchAvailableServices(/*@WebParam(name = "in")*/final SearchAvailableServicesInput in)		
	{
		final SearchAvailableServicesReturn ret=new SearchAvailableServicesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BServiceSubscribed.searchAvailableServices(in.getName(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public SearchSubscribedServicesReturn searchSubscribedServices(/*@WebParam(name = "in")*/final SearchSubscribedServicesInput in)		
	{
		final SearchSubscribedServicesReturn ret=new SearchSubscribedServicesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BServiceSubscribedJoin.searchSubscribedServices(in.getName(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SubscribeReturn subscribe(/*@WebParam(name = "in")*/final SubscribeInput in)		
	{
		final SubscribeReturn ret=new SubscribeReturn();
		try
		{
			checkLogin(in);
			
                        new BCompany(ArahantSession.getHSU().getCurrentCompany()).addServicesToCompany(in.getServiceIds(), in.getDate(),in.getExternalId());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public UnsubscribeReturn unsubscribe(/*@WebParam(name = "in")*/final UnsubscribeInput in)		
	{
		final UnsubscribeReturn ret=new UnsubscribeReturn();
		try
		{
			checkLogin(in);
			
                        new BCompany(ArahantSession.getHSU().getCurrentCompany()).removeServicesFromCompany(in.getServiceIds(), in.getDate());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
