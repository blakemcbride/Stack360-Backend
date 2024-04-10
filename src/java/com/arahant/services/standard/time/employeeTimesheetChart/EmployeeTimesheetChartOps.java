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
package com.arahant.services.standard.time.employeeTimesheetChart;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
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
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;

       
/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTimeEmployeeTimesheetChartOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class EmployeeTimesheetChartOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			EmployeeTimesheetChartOps.class);
	
	public EmployeeTimesheetChartOps() {
		super();
	}
	
        @WebMethod()
	public GetChartDataReturn getChartData(/*@WebParam(name = "in")*/final GetChartDataInput in)		
	{
		final GetChartDataReturn ret=new GetChartDataReturn();
		try
		{
			checkLogin(in);

			Map<String,GetChartDataReturnItem> retData=new HashMap<String,GetChartDataReturnItem>();
			
			BTimesheet[] times=BTimesheet.search(in.getFromDate(),in.getToDate(),in.getType(),in.getOrgGroupId(),0);
			
			for (BTimesheet t : times)
			{
				GetChartDataReturnItem ri=retData.get(t.getPersonId());
				if (ri==null)
				{
					ri=new GetChartDataReturnItem();
					retData.put(t.getPersonId(), ri);
					BPerson bp=new BPerson(t.getPersonId());
					ri.setNameFormatted(bp.getLastName()+", "+bp.getFirstName());
				}
				
				if (t.isTimeOff())
					ri.setTimeOff(ri.getTimeOff()+t.getTotalHours());
				else
					if (t.getBillable()=='Y')
						ri.setBillable(ri.getBillable()+t.getTotalHours());
					else
						ri.setNonBillable(ri.getNonBillable()+t.getTotalHours());
			}
			
			ArrayList<GetChartDataReturnItem> rl=new ArrayList<GetChartDataReturnItem>(retData.values().size());
			rl.addAll(retData.values());

			GetChartDataReturnItem [] items=new GetChartDataReturnItem[rl.size()];
			
			ret.setItem(rl.toArray(items));
			
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
