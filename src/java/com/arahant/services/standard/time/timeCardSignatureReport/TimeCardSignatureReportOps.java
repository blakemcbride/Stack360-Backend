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
package com.arahant.services.standard.time.timeCardSignatureReport;

import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.reports.TimeCardSignatureReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.DateUtils;
import static com.arahant.utils.IntegerDates.*;
import com.arahant.utils.IntegerDates.PeriodInfo;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeTimeCardSignatureReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class TimeCardSignatureReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(TimeCardSignatureReportOps.class);
	
	public TimeCardSignatureReportOps() {
		super();
	}
	
	@WebMethod()
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in)	{
		final ListAssociatedOrgGroupsReturn ret=new ListAssociatedOrgGroupsReturn();

		try
		{
			checkLogin(in);

			// ret.setOrgGroups(BOrgGroup.getAllOrgGroupParents(BPerson.getSupvOrgGroups()));
			// if (!isEmpty(in.getGroupId()))
				ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap(), in.getExcludeIds()));
			// else
			//	ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, hsu.getCurrentCompany().getOrgGroupId(), COMPANY_TYPE, ret.getCap(), in.getExcludeIds()));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

    @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in) {

		final GetReportReturn ret = new GetReportReturn();

		try
		{
			checkLogin(in);

			if(in.getReportType().equals("1"))
				ret.setReportUrl(new TimeCardSignatureReport().build(new BOrgGroup(in.getOrgGroupId()).getOrgGroup(), in.getFromDate(), in.getToDate()));
//			else if(in.getExportType().equals("2"))
//				ret.setReportUrl("");
			else
				throw new ArahantWarning("Export type not specified.");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

    @WebMethod()
	public GetAvailableReportsReturn getAvailableReports(/*@WebParam(name = "in")*/final GetAvailableReportsInput in) {

		final GetAvailableReportsReturn ret = new GetAvailableReportsReturn();

		try
		{
			checkLogin(in);

			GetAvailableReportsReturnItem[] arr = new GetAvailableReportsReturnItem[1];
			arr[0] = new GetAvailableReportsReturnItem("Time Card Signature Report", "1");
			ret.setItem(arr);

			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetCurrentPeriodReturn getCurrentPeriod(/*@WebParam(name = "in")*/final GetCurrentPeriodInput in) {
		final GetCurrentPeriodReturn ret = new GetCurrentPeriodReturn();

		try {
			checkLogin(in);
			
			String personId = in.getPersonId();
			if (personId.length() > 5) {
				final BPerson p = new BPerson(in.getPersonId());
				PeriodInfo pi = new PeriodInfo(p);

				ret.setStartDate(beginningPeriodDate(pi, DateUtils.today()));
				ret.setEndDate(endingPeriodDate(pi, DateUtils.today()));
//				ret.setShowBillable(pi.showBillable());
			}
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public GetNextPeriodReturn getNextPeriod(/*@WebParam(name = "in")*/final GetNextPeriodInput in) {
		final GetNextPeriodReturn ret = new GetNextPeriodReturn();
		try {
			checkLogin(in);

			final BPerson p = new BPerson(in.getPersonId());
			
			PeriodInfo pi = new PeriodInfo(p);
			int startDate;
			if (in.getForward())
				startDate = nextPeriodStartDate(pi, in.getStartDate());
			else
				startDate = prevPeriodStartDate(pi, in.getStartDate());
			ret.setStartDate(startDate);
			ret.setEndDate(endingPeriodDate(pi, startDate));

			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
