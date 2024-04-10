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

package com.arahant.services.standard.misc.processSchedule;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.timertasks.AvailableTasks;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardMiscProcessScheduleOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class ProcessScheduleOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ProcessScheduleOps.class);
	
	public ProcessScheduleOps() {
	}
	
	@WebMethod()
	public ListProcessSchedulesReturn listProcessSchedules(/*@WebParam(name = "in")*/final ListProcessSchedulesInput in) {
		final ListProcessSchedulesReturn ret = new ListProcessSchedulesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProcessSchedule.list());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProcessesReturn listProcesses(/*@WebParam(name = "in")*/final ListProcessesInput in) {
		final ListProcessesReturn ret = new ListProcessesReturn();
		try {
			checkLogin(in);

			ListProcessesReturnItem[] items = new ListProcessesReturnItem[AvailableTasks.classes.length];
			for (int loop = 0; loop < items.length; loop++) {
				items[loop] = new ListProcessesReturnItem();
				items[loop].setId(AvailableTasks.classes[loop]);
				items[loop].setDescription(AvailableTasks.descriptions[loop]);
			}

			ret.setItem(items);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteProcessSchedulesReturn deleteProcessSchedules(/*@WebParam(name = "in")*/final DeleteProcessSchedulesInput in) {
		final DeleteProcessSchedulesReturn ret = new DeleteProcessSchedulesReturn();
		try {
			checkLogin(in);

			BProcessSchedule.delete(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			//TODO:	ret.setReportUrl(new BProcessSchedule().getReport());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListProcessScheduleHistoryReturn listProcessScheduleHistory(/*@WebParam(name = "in")*/final ListProcessScheduleHistoryInput in) {
		final ListProcessScheduleHistoryReturn ret = new ListProcessScheduleHistoryReturn();
		try {
			checkLogin(in);

			ret.setItem(new BProcessSchedule(in.getId()).listHistory(ret.getCap()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveProcessScheduleReturn saveProcessSchedule(/*@WebParam(name = "in")*/final SaveProcessScheduleInput in) {
		final SaveProcessScheduleReturn ret = new SaveProcessScheduleReturn();
		try {
			checkLogin(in);

			final BProcessSchedule x = new BProcessSchedule(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewProcessScheduleReturn newProcessSchedule(/*@WebParam(name = "in")*/final NewProcessScheduleInput in) {
		final NewProcessScheduleReturn ret = new NewProcessScheduleReturn();
		try {
			checkLogin(in);

			final BProcessSchedule x = new BProcessSchedule();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public LoadProcessScheduleReturn loadProcessSchedule(/*@WebParam(name = "in")*/final LoadProcessScheduleInput in) {
		final LoadProcessScheduleReturn ret = new LoadProcessScheduleReturn();
		try {
			checkLogin(in);

			ret.setData(new BProcessSchedule(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

}
