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
package com.arahant.services.standard.hr.hrEmployeesByStatusReport;

import com.arahant.beans.OrgGroupAssociation;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrHrEmployeesByStatusReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class HrEmployeesByStatusReportOps extends ServiceBase {
 
	private static final transient ArahantLogger logger = new ArahantLogger(
			HrEmployeesByStatusReportOps.class);
	
	public HrEmployeesByStatusReportOps() {
		super();
	}
	
	@WebMethod()
	public ListEmployeeStatusesReturn listEmployeeStatuses(/*@WebParam(name = "in")*/final ListEmployeeStatusesInput in)			{
		final ListEmployeeStatusesReturn ret=new ListEmployeeStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	@WebMethod()
	public SearchOrgGroupsReturn searchOrgGroups(/*@WebParam(name = "in")*/final SearchOrgGroupsInput in)			{
		final SearchOrgGroupsReturn ret=new SearchOrgGroupsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BOrgGroup.searchOrgGroupsGeneric(hsu, in.getName(), 0, COMPANY_TYPE, ret.getHighCap()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	 
	

	@WebMethod()
	public GetReportReturn getReport(/*@WebParam(name = "in")*/final GetReportInput in)			{
		final GetReportReturn ret=new GetReportReturn();
		try
		{
			checkLogin(in);

			ret.setReportUrl(new BHREmployeeStatus(in.getStatusId()).getEmployeeByStatusReport(in.getOrgGroupId(), in.getDate(), in.getDepth(), in.getIncludeSubOrgGroups()));
			
			finishService(ret);
		}
		catch (final Throwable e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	


	@WebMethod()
	public ListOrgGroupsForGroupReturn listOrgGroupsForGroup(/*@WebParam(name = "in")*/final ListOrgGroupsForGroupInput in) {
		final ListOrgGroupsForGroupReturn ret = new ListOrgGroupsForGroupReturn();
		try {
			checkLogin(in);

			if (!isEmpty(in.getOrgGroupId())) {
				ret.setItem(new BOrgGroup(in.getOrgGroupId()).getChildren(in.getExcludeIds()));
			} else if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
				Set<BOrgGroup> orgGroups = new HashSet<BOrgGroup>();

				for(OrgGroupAssociation o : oga)
					orgGroups.add(new BOrgGroup(o.getOrgGroup()));
				BOrgGroup[] bogs = new BOrgGroup[orgGroups.size()];

				ret.setItem(orgGroups.toArray(bogs));
			}
			else
				ret.setItem(BOrgGroup.listTopLevel(in.getExcludeIds()));
			finishService(ret);
		} catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
