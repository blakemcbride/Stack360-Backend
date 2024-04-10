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
package com.arahant.services.standard.billing.service;

import com.arahant.business.BGlAccount;
import com.arahant.business.BService;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardBillingServiceOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ServiceOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ServiceOps.class);

	public ServiceOps() {
	}

	@WebMethod()
	public SearchForServicesReturn searchForServices(/*@WebParam(name = "in")*/final SearchForServicesInput in) {
		final SearchForServicesReturn ret = new SearchForServicesReturn();

		try {
			checkLogin(in);

			ret.setItem(BService.search(hsu, in.getAccSysId(), in.getDescription(), ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewServiceReturn newService(/*@WebParam(name = "in")*/final NewServiceInput in) {
		final NewServiceReturn ret = new NewServiceReturn();

		try {
			checkLogin(in);

			final BService p = new BService();
			ret.setProductServiceId(p.create());
			in.setData(p);
			p.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SaveServiceReturn saveService(/*@WebParam(name = "in")*/final SaveServiceInput in) {
		final SaveServiceReturn ret = new SaveServiceReturn();

		try {
			checkLogin(in);

			final BService p = new BService(in.getServiceId());
			in.setData(p);
			p.update();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteServicesReturn deleteServices(/*@WebParam(name = "in")*/final DeleteServicesInput in) {
		final DeleteServicesReturn ret = new DeleteServicesReturn();

		try {
			checkLogin(in);

			BService.delete(in.getServiceIds());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetServicesReportReturn getServicesReport(/*@WebParam(name = "in")*/final GetServicesReportInput in) {
		final GetServicesReportReturn ret = new GetServicesReportReturn();

		try {
			checkLogin(in);

			ret.setReportUrl(BService.getReport(in.getAccSysId(), in.getDescription()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListGLAccountsReturn listGLAccounts(/*@WebParam(name = "in")*/final ListGLAccountsInput in) {
		final ListGLAccountsReturn ret = new ListGLAccountsReturn();

		try {
			checkLogin(in);

			ret.setItem(BGlAccount.list(hsu));

			if (!isEmpty(in.getServiceId())) {
				BService bp = new BService(in.getServiceId());
				if (bp.getGlAccount() != null)
					ret.setSelectedItem(new ListGLAccountsReturnItem(bp.getGlAccount()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
