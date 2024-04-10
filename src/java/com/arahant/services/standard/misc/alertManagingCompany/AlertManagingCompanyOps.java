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
package com.arahant.services.standard.misc.alertManagingCompany;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscAlertManagingCompanyOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AlertManagingCompanyOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			AlertManagingCompanyOps.class);
	
	public AlertManagingCompanyOps() {
		super();
	}
	
        @WebMethod()
	public SearchAlertsReturn searchAlerts(/*@WebParam(name = "in")*/final SearchAlertsInput in)		
	{
		final SearchAlertsReturn ret=new SearchAlertsReturn();
		try
		{
			checkLogin(in);

			ret.setAlerts(BAlert.getAllAlertsManagingCompanyUserCanEdit(in.getAlertDate(),in.isPersonFilter(), ret.getCap()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
        public DeleteAlertsReturn deleteAlerts(/*@WebParam(name = "in")*/final DeleteAlertsInput in)
	{
		final DeleteAlertsReturn ret=new DeleteAlertsReturn();
		try
		{
			checkLogin(in);
			
			BAlert.deleteAlerts(hsu, in.getIds());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public SaveAlertReturn saveAlert(/*@WebParam(name = "in")*/final SaveAlertInput in)		
	{
		final SaveAlertReturn ret=new SaveAlertReturn();
		try
		{
			checkLogin(in);
			
			final BAlert x=new BAlert(in.getId());

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
	public NewAlertReturn newAlert(/*@WebParam(name = "in")*/final NewAlertInput in)		
	{
		final NewAlertReturn ret=new NewAlertReturn();
		try
		{
			checkLogin(in);
			
                        if (in.getAlertType() == 5)
                            BAlert.managingCompanySendToAllAgents(in.getMessage(), in.getStartDate(), in.getEndDate());
                        else if (in.getAlertType() == 6)
                            BAlert.managingCompanySendToAllMainContacts(in.getMessage(), in.getStartDate(), in.getEndDate());

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public LoadAlertReturn loadAlert(/*@WebParam(name = "in")*/final LoadAlertInput in)		
	{
		final LoadAlertReturn ret=new LoadAlertReturn();
		try
		{
			checkLogin(in);
			
			ret.setData(new BAlert(in.getId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
}
