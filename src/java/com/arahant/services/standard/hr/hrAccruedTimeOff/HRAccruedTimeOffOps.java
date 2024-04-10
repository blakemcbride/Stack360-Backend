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
 * Created on Mar 13, 2007
 *
 */
package com.arahant.services.standard.hr.hrAccruedTimeOff;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.*;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;

@WebService(targetNamespace = "http://operations.arahant.com", serviceName = "StandardHrHrAccruedTimeOffOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
@ThreadScope()
public class HRAccruedTimeOffOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(HRAccruedTimeOffOps.class);

	public HRAccruedTimeOffOps() {
	}

	@WebMethod()
	public ListAccruedTimeOffReturn listAccruedTimeOff(/*@WebParam(name = "in")*/final ListAccruedTimeOffInput in) {
		final ListAccruedTimeOffReturn ret = new ListAccruedTimeOffReturn();
		try {
			checkLogin(in);

			ret.setItem(new BEmployee(in.getEmployeeId()).listTimeOff(in.getAccrualAccountId(), in.getStartDate(), in.getEndDate(), ret.getCap()));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public NewAccruedTimeOffReturn newAccruedTimeOff(/*@WebParam(name = "in")*/final NewAccruedTimeOffInput in) {
		final NewAccruedTimeOffReturn ret = new NewAccruedTimeOffReturn();
		try {
			checkLogin(in);

			final BHRAccruedTimeOff x = new BHRAccruedTimeOff();
			ret.setId(x.create());
			in.setData(x);
			x.insert();

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ReportAccruedTimeOffReturn reportAccruedTimeOff(/*@WebParam(name = "in")*/final ReportAccruedTimeOffInput in) {
		final ReportAccruedTimeOffReturn ret = new ReportAccruedTimeOffReturn();
		try {
			checkLogin(in);

			ret.setFileName(new BEmployee(in.getEmployeeId()).getAccruedTimeOffReport(in.getAccrualAccountId(), in.getStartDate(), in.getEndDate()));

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

			ret.setAccessLevel(BRight.checkRight("HRAccruedTimeOff"));

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListTimeRelatedBenefitsReturn listTimeRelatedBenefits(/*@WebParam(name = "in")*/final ListTimeRelatedBenefitsInput in) {
		final ListTimeRelatedBenefitsReturn ret = new ListTimeRelatedBenefitsReturn();

		try {
			checkLogin(in);

			ret.setItem(BHRBenefit.listTimeRelatedPaidBenefits(hsu, in.getPersonId()), in.getPersonId());

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public GetBenefitPeriodReturn getBenefitPeriod(/*@WebParam(name = "in")*/final GetBenefitPeriodInput in) {
		final GetBenefitPeriodReturn ret = new GetBenefitPeriodReturn();

		try {
			checkLogin(in);

			final BEmployee emp = new BEmployee(in.getEmployeeId());
			ret.setData(emp);

			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

	@WebMethod()
	public ListApprovedTimeOffRequestsReturn listApprovedTimeOffRequests(/*@WebParam(name = "in")*/final ListApprovedTimeOffRequestsInput in) {
		final ListApprovedTimeOffRequestsReturn ret = new ListApprovedTimeOffRequestsReturn();
		try {
			checkLogin(in);

			BTimeOffRequest bto[] = BTimeOffRequest.listApprovedRequests(in.getPersonId());
			ret.setItem(bto);

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public MarkTimeOffRequestsEnteredReturn markTimeOffRequestsEntered(/*@WebParam(name = "in")*/final MarkTimeOffRequestsEnteredInput in) {
		final MarkTimeOffRequestsEnteredReturn ret = new MarkTimeOffRequestsEnteredReturn();
		try {
			checkLogin(in);

			BTimeOffRequest.markEntered(in.getIds());

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
