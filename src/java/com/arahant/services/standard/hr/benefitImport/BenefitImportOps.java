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
package com.arahant.services.standard.hr.benefitImport;
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

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrBenefitImportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class BenefitImportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			BenefitImportOps.class);
	
	public BenefitImportOps() {
		super();
	}
	
    @WebMethod()
	public SearchFileTypesReturn searchFileTypes(/*@WebParam(name = "in")*/final SearchFileTypesInput in)		
	{
		final SearchFileTypesReturn ret=new SearchFileTypesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BImportType.searchBenefitImportConfigs(in.getName()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
