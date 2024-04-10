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
package com.arahant.services.standard.misc.serviceSubscribed;
import com.arahant.beans.Right;
import com.arahant.beans.ServiceSubscribed;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscServiceSubscribedOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ServiceSubscribedOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ServiceSubscribedOps.class);
	
	public ServiceSubscribedOps() {
		super();
	}
	
        @WebMethod()
	public SearchServicesReturn searchServices(/*@WebParam(name = "in")*/final SearchServicesInput in)		
	{
		final SearchServicesReturn ret=new SearchServicesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BServiceSubscribed.search(in.getName(), in.getShowActive(), in.getShowInactive(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in)		
	{
		final CheckRightReturn ret=new CheckRightReturn();
		try
		{
			checkLogin(in);
			
			ret.setAllCompaniesAccessLevel(BRight.checkRight(Right.RIGHT_CAN_ACCESS_ALL_COMPANIES));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
        public DeleteServicesReturn deleteServices(/*@WebParam(name = "in")*/final DeleteServicesInput in)
	{
		final DeleteServicesReturn ret=new DeleteServicesReturn();
		try
		{
			checkLogin(in);
			
			BServiceSubscribed.delete(in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public LoadServiceReturn loadService(/*@WebParam(name = "in")*/final LoadServiceInput in)		
	{
		final LoadServiceReturn ret=new LoadServiceReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BServiceSubscribed(in.getServiceId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
        @WebMethod()
	public NewServiceReturn newService(/*@WebParam(name = "in")*/final NewServiceInput in)		
	{
		final NewServiceReturn ret=new NewServiceReturn();
		try
		{
			checkLogin(in);
			
			final BServiceSubscribed x=new BServiceSubscribed();
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
	public SaveServiceReturn saveService(/*@WebParam(name = "in")*/final SaveServiceInput in)		
	{
		final SaveServiceReturn ret=new SaveServiceReturn();
		try
		{
			checkLogin(in);
			
			final BServiceSubscribed x=new BServiceSubscribed(in.getId());
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
	public ListServiceTypeReturn listServiceType(/*@WebParam(name = "in")*/final ListServiceTypeInput in)		
	{
		final ListServiceTypeReturn ret=new ListServiceTypeReturn();
		try
		{
			checkLogin(in);

			ListServiceTypeReturnItem item[] = new ListServiceTypeReturnItem[ServiceSubscribed.INTERFACES.length];
			String[] interfaces =ServiceSubscribed.INTERFACES;
			 for (short i=0; i <  interfaces.length; i++){
				item[i] = new ListServiceTypeReturnItem();
				item[i].setTypeName(interfaces[i]);
				item[i].setTypeNumber(i);
			}
			ret.setItem(item);
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
