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
package com.arahant.services.standard.crm.standardProspectImport;

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


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardCrmStandardProspectImportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class StandardProspectImportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(StandardProspectImportOps.class);
	
	public StandardProspectImportOps() {
		super();
	}

    @WebMethod()
	public SearchEmployeesReturn searchEmployees(/*@WebParam(name = "in")*/final SearchEmployeesInput in)
	{
		final SearchEmployeesReturn ret=new SearchEmployeesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(),in.getLastName(),"",ret.getHighCap()));

			if(!isEmpty(in.getId()))  //prospect id
				ret.setSelectedItem(new SearchEmployeesReturnItem(new BProspectCompany(in.getId()).getSalesPerson()));

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}


	@WebMethod()
	public SearchProspectStatusesReturn searchProspectStatuses(/*@WebParam(name = "in")*/final SearchProspectStatusesInput in) {
		final SearchProspectStatusesReturn ret = new SearchProspectStatusesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectStatus.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				BProspectCompany bpc = new BProspectCompany(in.getId());
				ret.setSelectedItem(new SearchProspectStatusesReturnItem(bpc.getStatus()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectSourcesReturn searchProspectSources(/*@WebParam(name = "in")*/final SearchProspectSourcesInput in) {
		final SearchProspectSourcesReturn ret = new SearchProspectSourcesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectSource.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectSourcesReturnItem(new BProspectCompany(in.getId()).getSource()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectTypesReturn searchProspectTypes(/*@WebParam(name = "in")*/final SearchProspectTypesInput in) {
		final SearchProspectTypesReturn ret = new SearchProspectTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectType.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				BProspectCompany bpc = new BProspectCompany(in.getId());
				ret.setSelectedItem(new SearchProspectTypesReturnItem(new BProspectType(bpc.getProspectTypeId())));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
