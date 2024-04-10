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
import com.arahant.annotation.Validation;

import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SearchRouteStopsInput extends TransmitInputBase {


	@Validation (table="route",column="route_id",required=true)
	private String routeId;
	@Validation (table="route_stop",column="description",required=false)
	private String routeStopName;
	@Validation (table="org_group",column="name",required=false)
	private String orgGroupName;;
	@Validation (min=2,max=5,required=false)
	private int orgGroupNameSearchType;
	@Validation (min=2,max=5,required=false)
	private int routeStopNameSearchType;

	@Validation (min=1,max=4,required=false)
	private int orgGroupType;
	@Validation (table="org_group",column="name",required=false)
	private String companyName;
	@Validation (min=2,max=5,required=false)
	private int companyNameSearchType;
	@Validation (required=false)
	private int searchType;
	@Validation (required=false)
	private String routeStopId;
	@Validation (required=false)
	private String orgGroupId;
	@Validation (required=false)
	private String companyId;
	
	
	
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
	
	
	public String getRouteStopId() {
		return routeStopId;
	}

	public void setRouteStopId(String routeStopId) {
		this.routeStopId = routeStopId;
	}
	
	
	

	public int getSearchType() {
		return searchType;
	}

	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	
	

	public String getCompanyName() {
		return modifyForSearch(companyName, companyNameSearchType);
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public int getCompanyNameSearchType() {
		return companyNameSearchType;
	}

	public void setCompanyNameSearchType(int companyNameSearchType) {
		this.companyNameSearchType = companyNameSearchType;
	}

	
	/**
	 * @return Returns the orgGroupType.
	 */
	public int getOrgGroupType() {
		return orgGroupType;
	}
	/**
	 * @param orgGroupType The orgGroupType to set.
	 */
	public void setOrgGroupType(final int orgGroupType) {
		this.orgGroupType = orgGroupType;
	}
	public String getRouteStopName()
	{
		return modifyForSearch(routeStopName, routeStopNameSearchType);
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}
	public String getOrgGroupName()
	{
		return modifyForSearch(orgGroupName, orgGroupNameSearchType);
	}
	public void setOrgGroupName(final String orgGroupName)
	{
		this.orgGroupName=orgGroupName;
	}
	/**
	 * @return Returns the orgGroupNameSearchType.
	 */
	public int getOrgGroupNameSearchType() {
		return orgGroupNameSearchType;
	}
	/**
	 * @param orgGroupNameSearchType The orgGroupNameSearchType to set.
	 */
	public void setOrgGroupNameSearchType(final int orgGroupNameSearchType) {
		this.orgGroupNameSearchType = orgGroupNameSearchType;
	}
	/**
	 * @return Returns the routeStopNameSearchType.
	 */
	public int getRouteStopNameSearchType() {
		return routeStopNameSearchType;
	}
	/**
	 * @param routeStopNameSearchType The routeStopNameSearchType to set.
	 */
	public void setRouteStopNameSearchType(final int routeStopNameSearchType) {
		this.routeStopNameSearchType = routeStopNameSearchType;
	}
	/**
	 * @return Returns the routeId.
	 */
	public String getRouteId() {
		return routeId;
	}
	/**
	 * @param routeId The routeId to set.
	 */
	public void setRouteId(final String routeId) {
		this.routeId = routeId;
	}
	

}

	
