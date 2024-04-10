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


package com.arahant.services.standard.hr.workHistory;

import com.arahant.business.BPerson;
import com.arahant.business.BRight;
import com.arahant.rest.GroovyService;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import org.kissweb.DateTime;
import org.kissweb.Groff;
import org.kissweb.database.Connection;
import org.kissweb.database.Record;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import java.io.IOException;
import java.sql.SQLException;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrWorkHistoryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class WorkHistoryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(WorkHistoryOps.class);

	public WorkHistoryOps() {
	}

	@WebMethod()
	public WorkerHistoryReportReturn workerHistoryReport(/*@WebParam(name = "in")*/final WorkerHistoryReportInput in) {
		final WorkerHistoryReportReturn ret = new WorkerHistoryReportReturn();
		try {
			checkLogin(in);

			String reportName = (String) GroovyService.run("com.arahant.services.standard.hr.workHistory",
					"WorkHistoryReport",
					"workerHistoryReport",
					null,
					hsu.getKissConnection(), in.getEmployeeId(), in.getFirstDate(), in.getLastDate());
			ret.setFileName(reportName);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public WorkerHistoryItemsReturn workerHistoryItems(/*@WebParam(name = "in")*/ final WorkerHistoryItemsInput in) {
		final WorkerHistoryItemsReturn ret = new WorkerHistoryItemsReturn();
		try {
			checkLogin(in);

			ret.setItem(hsu, in.getEmployeeId(), in.getFirstDate(), in.getLastDate());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*@WebParam(name = "in")*/final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();

		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessStatusHistory"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

}
	
