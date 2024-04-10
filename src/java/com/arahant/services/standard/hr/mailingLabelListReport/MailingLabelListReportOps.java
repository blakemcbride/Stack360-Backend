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
package com.arahant.services.standard.hr.mailingLabelListReport;

import com.arahant.beans.OrgGroupAssociation;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmployeeStatus;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.reports.MailingLabelsReport;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardHrMailingLabelListReportOps")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class MailingLabelListReportOps extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			MailingLabelListReportOps.class);
	
	public MailingLabelListReportOps() {
		super();
	}
	
	@WebMethod()
	public ListStatusesReturn listStatuses(/*@WebParam(name = "in")*/final ListStatusesInput in)			{
		final ListStatusesReturn ret=new ListStatusesReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHREmployeeStatus.list(hsu));
			
			finishService(ret);
		}
		catch (final Exception e) {
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

			ret.setReportUrl(new MailingLabelsReport().build(in.getStatusIds(), in.getBenefitIds(),in.getOrgGroupIds(),in.isIncludeEmployeeId() ));
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

        @WebMethod()
	public ListBenefitsReturn listBenefits(/*@WebParam(name = "in")*/final ListBenefitsInput in)		
	{
		final ListBenefitsReturn ret=new ListBenefitsReturn();
		try
		{
			checkLogin(in);

			ret.setItem(BHRBenefit.listByCategory());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

	
	

        @WebMethod()
	public ListOrgGroupsReturn listOrgGroups(/*@WebParam(name = "in")*/final ListOrgGroupsInput in)		
	{
		final ListOrgGroupsReturn ret=new ListOrgGroupsReturn();
		try
		{
			checkLogin(in);

			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = hsu.getCurrentPerson().getOrgGroupAssociations();
				Set<BOrgGroup> orgGroups = new HashSet<BOrgGroup>();

				for(OrgGroupAssociation o : oga)
					orgGroups.add(new BOrgGroup(o.getOrgGroup()));
				BOrgGroup[] bogs = new BOrgGroup[orgGroups.size()];

				ret.setItem(orgGroups.toArray(bogs));
			}
			else
				ret.setItem(BOrgGroup.getLocations());
			
			finishService(ret);
		}
		catch (final Exception e) {
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
	
	

}
