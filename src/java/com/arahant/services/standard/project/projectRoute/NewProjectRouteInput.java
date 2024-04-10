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

import com.arahant.business.BRoute;
import com.arahant.services.TransmitInputBase;
import com.arahant.utils.ArahantSession;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class NewProjectRouteInput extends TransmitInputBase {

	@Validation (required=false)
	private NewProjectRouteInputItem item[];
	/**
	 * @return Returns the item.
	 */
	public NewProjectRouteInputItem[] getItem() {
            if (item==null)
                return new NewProjectRouteInputItem[0];
		return item;
	}
	/**
	 * @param item The item to set.
	 */
	public void setItem(final NewProjectRouteInputItem[] item) {
		this.item = item;
	}
	void setData(final BRoute bc)
	{
		
		bc.setName(routeName);
		bc.setDescription(routeDescription);
		bc.setDefaultRouteStop(defaultRouteStopId);
		bc.setDefaultStatusId(defaultStatusId);
		bc.setLastActiveDate(lastActiveDate);
		if(allCompanies)
			bc.setCompany(null);
		else
			bc.setCompany(ArahantSession.getHSU().getCurrentCompany());
	}
	
	@Validation (table="route",column="name",required=true)
	private String routeName;
	@Validation (table="route",column="description",required=false)
	private String routeDescription;
	@Validation (table="route",column="route_stop_id",required=false)
	private String defaultRouteStopId;
	@Validation (table="route",column="project_status_id",required=false)
	private String defaultStatusId;
	@Validation (required=false, type="date")
	private int lastActiveDate;
	@Validation (required=false)
	private boolean allCompanies;

	public boolean getAllCompanies() {
		return allCompanies;
	}

	public void setAllCompanies(boolean allCompanies) {
		this.allCompanies = allCompanies;
	}

	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
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
	 * @return Returns the defaultRouteStopId.
	 */
	public String getDefaultRouteStopId() {
		return defaultRouteStopId;
	}
	/**
	 * @param defaultRouteStopId The defaultRouteStopId to set.
	 */
	public void setDefaultRouteStopId(final String defaultRouteStopId) {
		this.defaultRouteStopId = defaultRouteStopId;
	}
	/**
	 * @return Returns the defaultStatusId.
	 */
	public String getDefaultStatusId() {
		return defaultStatusId;
	}
	/**
	 * @param defaultStatusId The defaultStatusId to set.
	 */
	public void setDefaultStatusId(final String defaultStatusId) {
		this.defaultStatusId = defaultStatusId;
	}

}

	
