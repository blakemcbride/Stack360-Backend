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
package com.arahant.services.standard.crm.clientSummary;

import com.arahant.beans.ClientStatus;
import com.arahant.business.BClientCompany;
import com.arahant.business.BClientStatus;
import com.arahant.business.BGlAccount;
import com.arahant.business.BRight;
import com.arahant.reports.CompanyAddressReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmClientSummaryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ClientSummaryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ClientSummaryOps.class);

	public ClientSummaryOps() {
	}

	@WebMethod()
	public LoadSummaryReturn loadSummary(/*
			 * @WebParam(name = "in")
			 */final LoadSummaryInput in) {
		final LoadSummaryReturn ret = new LoadSummaryReturn();
		try {
			checkLogin(in);

			ret.setData(new BClientCompany(in.getId()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveSummaryReturn saveSummary(/*
			 * @WebParam(name = "in")
			 */final SaveSummaryInput in) {
		final SaveSummaryReturn ret = new SaveSummaryReturn();
		try {
			checkLogin(in);

			final BClientCompany x = new BClientCompany(in.getId());
			in.setData(x);
			x.update();

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public CheckRightReturn checkRight(/*
			 * @WebParam(name = "in")
			 */final CheckRightInput in) {
		final CheckRightReturn ret = new CheckRightReturn();
		try {
			checkLogin(in);

			ret.setAccessLevel(BRight.checkRight("AccessCRM"));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListGLSalesAccountsReturn listGLSalesAccounts(/*
			 * @WebParam(name = "in")
			 */final ListGLSalesAccountsInput in) {
		final ListGLSalesAccountsReturn ret = new ListGLSalesAccountsReturn();

		try {
			checkLogin(in);

			ret.setGLAccounts(BGlAccount.listByType(hsu, 21));

			finishService(ret);

		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListClientStatusesReturn listClientStatuses(/*
			 * @WebParam(name = "in")
			 */final ListClientStatusesInput in) {
		final ListClientStatusesReturn ret = new ListClientStatusesReturn();
		try {
			checkLogin(in);

			List<ClientStatus> clientList = new ArrayList<ClientStatus>();

			for (BClientStatus cs : BClientStatus.list(false)) {
				int lastActive = cs.getLastActiveDate();

				if (lastActive > DateUtils.now() || lastActive == 0 || new BClientCompany(in.getId()).getClientStatusId().equals(cs.getClientStatusId()))
					clientList.add(cs.getBean());
			}

			ret.setItem(BClientStatus.makeArray(clientList));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetReportReturn getReport(/*
			 * @WebParam(name = "in")
			 */final GetReportInput in) {
		final GetReportReturn ret = new GetReportReturn();
		try {
			checkLogin(in);

			ret.setReportUrl(new CompanyAddressReport("Client Summary").build(in.getId(), in.getIncludeContactDetail()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
