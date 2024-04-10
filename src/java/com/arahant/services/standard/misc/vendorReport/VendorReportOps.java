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
package com.arahant.services.standard.misc.vendorReport;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BVendorCompany;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;


/**
 * 
 * Created on Mar 29, 2007
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscVendorReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class VendorReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			VendorReportOps.class);
	
	
	public VendorReportOps() {
		super();
	}
	
	@WebMethod()
	public GetVendorReportReturn getVendorReport(/*@WebParam(name = "in")*/final GetVendorReportInput in)	{
		final GetVendorReportReturn ret=new GetVendorReportReturn();
		
		try
		{
			checkLogin(in);
			ret.setFileName(BVendorCompany.getReport(hsu,in.isAddress(),in.isCompanyPhone(),
					in.isIdentifier(),in.isPrimaryContact(),
					in.isAccountNumber(),in.getSortType(),in.isSortAsc()));
			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}


	
