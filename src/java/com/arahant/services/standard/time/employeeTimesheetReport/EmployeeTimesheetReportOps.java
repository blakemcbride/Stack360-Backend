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
package com.arahant.services.standard.time.employeeTimesheetReport;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import com.arahant.reports.EmployeeTimesheetReport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeEmployeeTimesheetReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeTimesheetReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeTimesheetReportOps.class);
	
	public EmployeeTimesheetReportOps() {
		super();
	}
	
        @WebMethod()
	public GetReportDataReturn getReportData(/*@WebParam(name = "in")*/final GetReportDataInput in)		
	{
		final GetReportDataReturn ret=new GetReportDataReturn();
		try
		{
			checkLogin(in);

			
			Map<String,GetReportDataReturnItem> retData=new HashMap<String,GetReportDataReturnItem>();
			
			BTimesheet[] times=BTimesheet.search(in.getFromDate(),in.getToDate(),in.getType(),in.getOrgGroupId(),0);
			
			for (BTimesheet t : times)
			{
				GetReportDataReturnItem ri=retData.get(t.getPersonId());
				if (ri==null)
				{
					ri=new GetReportDataReturnItem();
					retData.put(t.getPersonId(), ri);
					BPerson bp=new BPerson(t.getPersonId());
					ri.setFirstName(bp.getFirstName());
					ri.setLastName(bp.getLastName());
					ri.sortType=in.getSort();
				}
				
				ri.setTotalHours(ri.getTotalHours()+t.getTotalHours());
				
				if (t.getBillable()=='Y')
					ri.setBillableHours(ri.getBillableHours()+t.getTotalHours());
				else
					ri.setNonBillableHours(ri.getNonBillableHours()+t.getTotalHours());
			}
			
			ArrayList<GetReportDataReturnItem> rl=new ArrayList<GetReportDataReturnItem>(retData.values().size());
			rl.addAll(retData.values());
			Collections.sort(rl);
			
			GetReportDataReturnItem [] items=new GetReportDataReturnItem[rl.size()];
			
			ret.setItem(rl.toArray(items));
				
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)		
	{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);
			
			ret.setReportUrl(new EmployeeTimesheetReport().build(in.getFromDate(), in.getToDate(), in.getType(), in.getSort(), in.getOrgGroupId()));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public SearchSubordinateOrgGroupsReturn searchSubordinateOrgGroups(/*@WebParam(name = "in")*/final SearchSubordinateOrgGroupsInput in)	{
		final SearchSubordinateOrgGroupsReturn ret=new SearchSubordinateOrgGroupsReturn();
		
		try {
			checkLogin(in);

			ret.setOrgGroups(BPerson.getCurrent().searchSubordinateGroups(hsu,in.getName(),COMPANY_TYPE,ret.getHighCap()));
						
			finishService(ret);
		} catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		
		return ret;
	}
}
