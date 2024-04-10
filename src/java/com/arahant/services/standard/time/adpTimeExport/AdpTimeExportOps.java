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
package com.arahant.services.standard.time.adpTimeExport;

import com.arahant.beans.PaySchedule;
import com.arahant.beans.PaySchedulePeriod;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.exports.ADPAccrualExport;
import com.arahant.exports.ADPTimeExport;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeAdpTimeExportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class AdpTimeExportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(AdpTimeExportOps.class);
	
	public AdpTimeExportOps() {
		super();
	}
	
    @WebMethod()
	public GetMetaReturn getMeta(/*@WebParam(name = "in")*/final GetMetaInput in) {

		final GetMetaReturn ret = new GetMetaReturn();

		try
		{
			checkLogin(in);
			
			PaySchedule ps = ArahantSession.getHSU().getCurrentCompany().getPaySchedule();
			if(ps != null)
			{
				BPaySchedule bps = new BPaySchedule(ps);
				ret.setFromDate(bps.getCurrentPeriodStart());
				ret.setToDate(bps.getCurrentPeriodEnd());
			}
			
			if(ret.getFromDate() == 0 || ret.getToDate() == 0)
			{
				ret.setFromDate(DateUtils.addDays(DateUtils.now(), -14));
				ret.setToDate(DateUtils.now());
			}

		
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	


	@WebMethod()
	public ListAssociatedOrgGroupsReturn listAssociatedOrgGroups(/*@WebParam(name = "in")*/final ListAssociatedOrgGroupsInput in)	{
		final ListAssociatedOrgGroupsReturn ret=new ListAssociatedOrgGroupsReturn();

		try
		{
			checkLogin(in);

			ret.setOrgGroups(BOrgGroup.listAssociatedCompanyGroups(hsu, in.getGroupId(), COMPANY_TYPE, ret.getCap(), in.getExcludeIds()));

			finishService(ret);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}

		return ret;
	}

    @WebMethod()
	public GetExportReturn getExport(/*@WebParam(name = "in")*/final GetExportInput in) {

		final GetExportReturn ret = new GetExportReturn();

		try
		{
			checkLogin(in);

			if(in.getExportType().equals("A"))
				ret.setReportUrl(new ADPAccrualExport().build(in.getFromDate(), in.getToDate(), in.getOrgGroupIds()));
			else if(in.getExportType().equals("H"))
				ret.setReportUrl(new ADPTimeExport().build(in.getFromDate(), in.getToDate(), in.getOrgGroupIds()));
			else
				throw new ArahantWarning("Export type not specified.");
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
