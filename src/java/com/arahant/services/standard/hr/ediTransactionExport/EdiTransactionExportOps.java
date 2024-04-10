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
package com.arahant.services.standard.hr.ediTransactionExport;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exports.EDITransactionChangeLogExport;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrEdiTransactionExportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EdiTransactionExportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(EdiTransactionExportOps.class);
	
	public EdiTransactionExportOps() {
		super();
	}
	
    @WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {

		final GetExportReturn ret = new GetExportReturn();

		try
		{
			checkLogin(in);
			
			ret.setExportUrl(new EDITransactionChangeLogExport().build(in.getTransactionTypes(), in.getVendorIds(), in.getFromDate(), in.getToDate()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	

	@WebMethod()
	public SearchVendorCompanyReturn searchVendorCompany(/*@WebParam(name = "in")*/final SearchVendorCompanyInput in)	{
		final SearchVendorCompanyReturn ret=new SearchVendorCompanyReturn();


		try
		{
			checkLogin(in);

			final BVendorCompany v[]=BVendorCompany.searchVendors("%", 100);

			ret.setVendorList(v);

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

}
