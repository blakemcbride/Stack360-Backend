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
package com.arahant.services.standard.project.projectRoute;
import com.arahant.annotation.Validation;

import com.arahant.business.BRouteStop;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewRouteStopInput extends TransmitInputBase {

	void setData(final BRouteStop bc)
	{
		getCheckListItems();//set if null
		bc.setRouteId(routeId);
		if (orgGroupId!=null && !orgGroupId.trim().equals(""))
			bc.setOrgGroupId(orgGroupId);
		else
			bc.setOrgGroupId(companyId);
		bc.setDescription(routeStopName);
		if (statusIds==null)
			statusIds=new String[0];
		bc.setProjectStatusIds(statusIds);
		bc.setPhaseId(phaseId);
		bc.setAutoAssignToSupervisors(autoAssignToSupervisors);
		
		for (int loop=0;loop<checkListItems.length;loop++)
			bc.addCheckListItem(checkListItems[loop].getActiveDate(),
					checkListItems[loop].getInactiveDate(),
					checkListItems[loop].getDescription(),checkListItems[loop].getDetail(),
					checkListItems[loop].getRequired(), loop);

	}
	
	@Validation (table="route",column="route_id",required=true)
	private String routeId;
	@Validation (table="org_group",column="org_group_id",required=false)
	private String orgGroupId;
	@Validation (table = "route_stop", column = "description", required=true)
	private String routeStopName;
	@Validation (required=true)
	private String companyId;
	@Validation (required=true)
	private boolean autoAssignToSupervisors;
	@Validation (min=0,required=false)
	private NewRouteStopInputItem [] checkListItems;

	public NewRouteStopInputItem[] getCheckListItems() {
            if (checkListItems==null)
                checkListItems=new NewRouteStopInputItem[0];
		return checkListItems;
	}

	public void setCheckListItems(NewRouteStopInputItem []checkListItems) {
		this.checkListItems = checkListItems;
	}
	
	

	public boolean getAutoAssignToSupervisors() {
		return autoAssignToSupervisors;
	}

	public void setAutoAssignToSupervisors(boolean autoAssignToSupervisors) {
		this.autoAssignToSupervisors = autoAssignToSupervisors;
	}
	
	

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}
	@Validation (required=true)
	private String phaseId;
	
	

	public String[] getStatusIds() {
		return statusIds;
	}

	public void setStatusIds(String[] statusIds) {
		this.statusIds = statusIds;
	}
	@Validation (required=false)
	private String []statusIds;
	
	

	public String getRouteId()
	{
		return routeId;
	}
	public void setRouteId(final String routeId)
	{
		this.routeId=routeId;
	}
	public String getOrgGroupId()
	{
		return orgGroupId;
	}
	public void setOrgGroupId(final String orgGroupId)
	{
		this.orgGroupId=orgGroupId;
	}
	public String getRouteStopName()
	{
		return routeStopName;
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}

}

	
