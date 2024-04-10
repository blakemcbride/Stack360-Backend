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

package com.arahant.services.standard.hr.employeeExport;

import com.arahant.business.BEmployeeChanged;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

/**
 *
 *
 *
 */
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrEmployeeExportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class EmployeeExportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(EmployeeExportOps.class);

	public EmployeeExportOps() {
	}

	@WebMethod()
	public ListAvailableExportsReturn listAvailableExports(/*@WebParam(name = "in")*/final ListAvailableExportsInput in) {
		final ListAvailableExportsReturn ret = new ListAvailableExportsReturn();
		try {
			checkLogin(in);

			ret.setItem(BEmployeeChanged.listExports());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public RunExportReturn runExport(/*@WebParam(name = "in")*/final RunExportInput in) {
		final RunExportReturn ret = new RunExportReturn();
		try {
			checkLogin(in);

			BEmployeeChanged.runExport(in.getId());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight(ACCESS_HRPARENT));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
