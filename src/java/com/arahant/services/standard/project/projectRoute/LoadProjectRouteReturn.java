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

import java.util.Set;

import com.arahant.beans.RouteTypeAssoc;
import com.arahant.business.BRoute;
import com.arahant.business.BRouteStop;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class LoadProjectRouteReturn extends TransmitReturnBase {

	private LoadProjectRouteReturnItem item[];
	
	void setData(final BRoute bc)
	{
		
		routeName=bc.getName();
		routeDescription=bc.getDescription();
		allCompanies=bc.getCompany()==null;
		lastActiveDate=bc.getLastActiveDate();
		
		final Set<RouteTypeAssoc>rtaset=bc.getRouteTypeAssociations();
		
		item=new LoadProjectRouteReturnItem[rtaset.size()];
		
		int index=0;
		
		for (final RouteTypeAssoc assoc : rtaset) 
			item[index++]=new LoadProjectRouteReturnItem(assoc);
		
		if (bc.getRouteStopId()!=null && !bc.getRouteStopId().equals(""))
		{
			BRouteStop rs=new BRouteStop(bc.getRouteStopId());
			initialRouteStopCompanyId=rs.getCompanyId();
			initialRouteStopOrgGroupId=rs.getOrgGroupId();
			initialRouteStopId=bc.getRouteStopId();
			initialProjectStatusId=bc.getProjectStatusId();
		}
		else
		{
			initialRouteStopCompanyId="";
			initialRouteStopOrgGroupId="";
			initialRouteStopId="";
			initialProjectStatusId="";
		}

	}
	
	private String routeName;
	private String routeDescription;
	private String initialRouteStopCompanyId;
	private String initialRouteStopOrgGroupId;
	private String initialRouteStopId;
	private String initialProjectStatusId;
	private boolean allCompanies;
	private int lastActiveDate;

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public String getInitialProjectStatusId() {
		return initialProjectStatusId;
	}

	public void setInitialProjectStatusId(String initialProjectStatusId) {
		this.initialProjectStatusId = initialProjectStatusId;
	}
	
	
	

	public String getInitialRouteStopCompanyId() {
		return initialRouteStopCompanyId;
	}

	public void setInitialRouteStopCompanyId(String initialRouteStopCompanyId) {
		this.initialRouteStopCompanyId = initialRouteStopCompanyId;
	}

	public String getInitialRouteStopId() {
		return initialRouteStopId;
	}

	public void setInitialRouteStopId(String initialRouteStopId) {
		this.initialRouteStopId = initialRouteStopId;
	}

	public String getInitialRouteStopOrgGroupId() {
		return initialRouteStopOrgGroupId;
	}

	public void setInitialRouteStopOrgGroupId(String initialRouteStopOrgGroupId) {
		this.initialRouteStopOrgGroupId = initialRouteStopOrgGroupId;
	}
	
	
	
			

	public String getRouteName()
	{
		return routeName;
	}
	public void setRouteName(final String routeName)
	{
		this.routeName=routeName;
	}
	public String getRouteDescription()
	{
		return routeDescription;
	}
	public void setRouteDescription(final String routeDescription)
	{
		this.routeDescription=routeDescription;
	}
	/**
	 * @return Returns the item.
	 */
	public LoadProjectRouteReturnItem[] getItem() {
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final LoadProjectRouteReturnItem[] item) {
		this.item = item;
	}

}

	
