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
import com.arahant.business.BRouteStop;


/**
 * 
 *
 *
 */
public class SearchRouteStopsReturnItem {
	
	public SearchRouteStopsReturnItem()
	{
		;
	}

	SearchRouteStopsReturnItem (final BRouteStop bc)
	{
		
		routeStopId=bc.getRouteStopId();
		routeStopName=bc.getDescription();
		orgGroupName=bc.getOrgGroupName();
		companyName=bc.getCompanyName();
		typeFormatted=bc.getTypeFormatted();
		companyId=bc.getCompanyId();
		orgGroupId=bc.getOrgGroupId();
		
		if (orgGroupName.equals(companyName))
			orgGroupName="";
		if (orgGroupId.equals(companyId))
			orgGroupId="";

		phaseCode=bc.getPhaseCode();
		routeStopNameFormatted=bc.getRouteStopNameFormatted();
	}
	
	private String routeStopId;
	private String routeStopName;
	private String orgGroupName;
	private String companyId;
	private String orgGroupId;
	private String phaseCode;
	private String routeStopNameFormatted;

	public String getPhaseCode() {
		return phaseCode;
	}

	public void setPhaseCode(String phaseCode) {
		this.phaseCode = phaseCode;
	}

	public String getRouteStopNameFormatted() {
		return routeStopNameFormatted;
	}

	public void setRouteStopNameFormatted(String routeStopNameFormatted) {
		this.routeStopNameFormatted = routeStopNameFormatted;
	}
	
	
	

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}
	
	

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTypeFormatted() {
		return typeFormatted;
	}

	public void setTypeFormatted(String typeFormatted) {
		this.typeFormatted = typeFormatted;
	}

	private String companyName;
	private String typeFormatted;



	public String getRouteStopId()
	{
		return routeStopId;
	}
	public void setRouteStopId(final String routeStopId)
	{
		this.routeStopId=routeStopId;
	}
	public String getRouteStopName()
	{
		return routeStopName;
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}
	public String getOrgGroupName()
	{
		return orgGroupName;
	}
	public void setOrgGroupName(final String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}

}

	
