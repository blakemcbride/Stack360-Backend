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
package com.arahant.services.standard.project.projectCurrentStatus;

import com.arahant.business.BRoute;
import com.arahant.services.TransmitReturnBase;

 /**
 * 
 *
 *
 */
public class GetDefaultsForRouteReturn extends TransmitReturnBase {

	void setData(final BRoute bc)
	{
		
		routeStopId=bc.getRouteStopId();
		orgGroupName=bc.getOrgGroupName();
		routeStopName=bc.getRouteStopName();
		projectStatusId=bc.getProjectStatusId();
		//projectStatusCode=bc.getProjectStatusCode();
		orgGroupId=bc.getOrgGroupId();
		companyId=bc.getCompanyId();
		companyName=bc.getCompanyName();
		//projectStatusDescription=bc.getProjectStatusDescription();
		routeStopTypeFormatted=bc.getRouteStopTypeFormatted();
	}
	
	private String routeStopId;
	private String orgGroupName;
	private String routeStopName;
	private String projectStatusId;
	//private String projectStatusCode;
	private String orgGroupId;
	//private String projectStatusDescription;
	private String routeStopTypeFormatted;
	private String companyId;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	
	
	public String getRouteStopTypeFormatted() {
		return routeStopTypeFormatted;
	}

	public void setRouteStopTypeFormatted(String routeStopTypeFormatted) {
		this.routeStopTypeFormatted = routeStopTypeFormatted;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	private String companyName;

	/**
	 * @return Returns the orgGroupId.
	 */
	public String getOrgGroupId() {
		return orgGroupId;
	}
	/**
	 * @param orgGroupId The orgGroupId to set.
	 */
	public void setOrgGroupId(final String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	public String getRouteStopId()
	{
		return routeStopId;
	}
	public void setRouteStopId(final String routeStopId)
	{
		this.routeStopId=routeStopId;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(final String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	public String getRouteStopName()
	{
		return routeStopName;
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}
	public String getProjectStatusId()
	{
		return projectStatusId;
	}
	public void setProjectStatusId(final String projectStatusId)
	{
		this.projectStatusId=projectStatusId;
	}

}
