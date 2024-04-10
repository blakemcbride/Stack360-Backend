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



package com.arahant.services.standard.hr.wageType;

import com.arahant.business.BGlAccount;
import com.arahant.business.BRight;
import com.arahant.business.BService;
import com.arahant.business.BWageType;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrWageTypeOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class WageTypeOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(WageTypeOps.class);

	public WageTypeOps() {
	}

	@WebMethod()
	public ListWageTypesReturn listWageTypes(/*
			 * @WebParam(name = "in")
			 */final ListWageTypesInput in) {
		final ListWageTypesReturn ret = new ListWageTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BWageType.list(in.getActiveType()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public DeleteWageTypesReturn deleteWageTypes(/*
			 * @WebParam(name = "in")
			 */final DeleteWageTypesInput in) {
		final DeleteWageTypesReturn ret = new DeleteWageTypesReturn();
		try {
			checkLogin(in);

			BWageType.delete(in.getIds());

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

			if (true)
				throw new Exception("No report defined.");

//TODO:			ret.setReportUrl(new BWageType().getReport());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewWageTypeReturn newWageType(/*
			 * @WebParam(name = "in")
			 */final NewWageTypeInput in) {
		final NewWageTypeReturn ret = new NewWageTypeReturn();
		try {
			checkLogin(in);

			final BWageType x = new BWageType();
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
	public SaveWageTypeReturn saveWageType(/*
			 * @WebParam(name = "in")
			 */final SaveWageTypeInput in) {
		final SaveWageTypeReturn ret = new SaveWageTypeReturn();
		try {
			checkLogin(in);

			final BWageType x = new BWageType(in.getId());
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

			ret.setAccessLevel(BRight.checkRight(ACCESS_HRSETUPS));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public SearchGLAccountsReturn searchGLAccounts(/*
			 * @WebParam(name = "in")
			 */final SearchGLAccountsInput in) {
		final SearchGLAccountsReturn ret = new SearchGLAccountsReturn();
		try {
			checkLogin(in);
			BGlAccount accts[] = BGlAccount.search(in.getAccountName(), in.getAccountNumber(), false, ret.getHighCap());
			ret.setItem(accts);

			if (!isEmpty(in.getSelectFromId()))
				ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(in.getSelectFromId())));


			if (!isEmpty(in.getId()))
				ret.setSelectedItem(new SearchGLAccountsReturnItem(new BGlAccount(in.getId())));

			if (!isEmpty(in.getSelectFromProductServiceId()))
				try {
					ret.setSelectedItem(new SearchGLAccountsReturnItem(new BService(in.getSelectFromProductServiceId()).getGlAccount()));
				} catch (Exception e) {
				}
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
