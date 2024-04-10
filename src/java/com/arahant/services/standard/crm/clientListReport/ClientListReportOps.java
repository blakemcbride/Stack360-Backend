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
 * Created on Mar 29, 2007
 * 
 */
package com.arahant.services.standard.crm.clientListReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BClientCompany;
import com.arahant.reports.ClientStatusReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 *  
 *
 * Created on Mar 29, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmClientListReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ClientListReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			ClientListReportOps.class);
	
	

	public ClientListReportOps() {
		super();
	}
	
	@WebMethod()
	public GetClientReportReturn getClientReport(/*@WebParam(name = "in")*/final GetClientReportInput in)	{
		final GetClientReportReturn ret=new GetClientReportReturn();
		
		try
		{
			checkLogin(in);
			ret.setFileName(BClientCompany.getReport(hsu,in.isAddress(),in.isBillingRate(),in.isCompanyPhoneNumber(),
					in.isContractDate(),in.isIdentifier(),in.isPrimaryContactName(),
					in.getSortType(),in.isSortAsc()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}

	@WebMethod()
	public GetClientDetailReportReturn getClientDetailReport(/*@WebParam(name = "in")*/final GetClientDetailReportInput in)		
	{
		final GetClientDetailReportReturn ret=new GetClientDetailReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new ClientStatusReport().build());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
