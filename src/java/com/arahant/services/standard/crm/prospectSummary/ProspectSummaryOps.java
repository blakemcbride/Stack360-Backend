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



package com.arahant.services.standard.crm.prospectSummary;

import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.CompanyAddressReport;
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
@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardCrmProspectSummaryOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class ProspectSummaryOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(ProspectSummaryOps.class);

	public ProspectSummaryOps() {
		super();
	}

	@WebMethod()
	public LoadSummaryReturn loadSummary(/*
			 * @WebParam(name = "in")
			 */final LoadSummaryInput in) {
		final LoadSummaryReturn ret = new LoadSummaryReturn();
		try {
			checkLogin(in);

			ret.setData(new BProspectCompany(in.getId()));

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

			final BProspectCompany x = new BProspectCompany(in.getId());
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
	public SearchProspectStatusesReturn searchProspectStatuses(/*
			 * @WebParam(name = "in")
			 */final SearchProspectStatusesInput in) {
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
	public SearchProspectSourcesReturn searchProspectSources(/*
			 * @WebParam(name = "in")
			 */final SearchProspectSourcesInput in) {
		final SearchProspectSourcesReturn ret = new SearchProspectSourcesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectSource.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				BProspectCompany bpc = new BProspectCompany(in.getId());
				ret.setSelectedItem(new SearchProspectSourcesReturnItem(bpc.getSource()));
			}

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchProspectTypesReturn searchProspectTypes(/*
			 * @WebParam(name = "in")
			 */final SearchProspectTypesInput in) {
		final SearchProspectTypesReturn ret = new SearchProspectTypesReturn();
		try {
			checkLogin(in);

			ret.setItem(BProspectType.search(in.getCode(), in.getDescription(), ret.getHighCap()));

			if (!isEmpty(in.getId())) //this is a prospect id
				ret.setSelectedItem(new SearchProspectTypesReturnItem(new BProspectType(new BProspectCompany(in.getId()).getProspectTypeId())));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public SearchEmployeesReturn searchEmployees(/*
			 * @WebParam(name = "in")
			 */final SearchEmployeesInput in) {
		final SearchEmployeesReturn ret = new SearchEmployeesReturn();
		try {
			checkLogin(in);

			ret.setItem(BEmployee.searchEmployees(in.getFirstName(), in.getLastName(), "", ret.getHighCap()));

			if (!isEmpty(in.getId())) {
				BProspectCompany bp = new BProspectCompany(in.getId());

				ret.setSelectedItem(new SearchEmployeesReturnItem(bp.getSalesPerson()));
			}


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

			ret.setReportUrl(new CompanyAddressReport("Prospect Summary").build(in.getId(), in.getIncludeContactDetail()));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
