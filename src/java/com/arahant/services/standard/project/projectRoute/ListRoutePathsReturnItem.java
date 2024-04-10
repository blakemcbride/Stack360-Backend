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
import com.arahant.business.BRoutePath;


/**
 * 
 *
 *
 */
public class ListRoutePathsReturnItem {
	
	public ListRoutePathsReturnItem()
	{
		;
	}

	ListRoutePathsReturnItem (final BRoutePath bc)
	{
		
		routePathId=bc.getRoutePathId();
		fromStatusCode=bc.getFromStatusCode();
		toStatusCode=bc.getToStatusCode();
		toNameFormatted=bc.getToNameFormatted();
		toRouteStopName=bc.getToRouteStopName();
		toOrgGroupId=bc.getToOrgGroupId();
		toRouteStopId=bc.getToRouteStopId();
		toCompanyId=bc.getToCompanyId();
		toStatusId=bc.getToStatusId();
		fromStatusId=bc.getFromStatusId();
		
		if (toCompanyId.equals(toOrgGroupId))
			toOrgGroupId="";
	}
	
	private String routePathId;
	private String fromStatusCode;
	private String toStatusId;
	private String toCompanyId;
	private String fromStatusId;
	
	public String getFromStatusId() {
		return fromStatusId;
	}

	public void setFromStatusId(String fromStatusId) {
		this.fromStatusId = fromStatusId;
	}
	
	
	

	public String getToStatusId() {
		return toStatusId;
	}

	public void setToStatusId(String toStatusId) {
		this.toStatusId = toStatusId;
	}
	
	
	
	
	public String getToCompanyId() {
		return toCompanyId;
	}

	public void setToCompanyId(String toCompanyId) {
		this.toCompanyId = toCompanyId;
	}
	
	public String getToOrgGroupId() {
		return toOrgGroupId;
	}

	public void setToOrgGroupId(String toOrgGroupId) {
		this.toOrgGroupId = toOrgGroupId;
	}
	private String toOrgGroupId;

	public String getToRouteStopName() {
		return toRouteStopName;
	}

	public void setToRouteStopName(String toRouteStopName) {
		this.toRouteStopName = toRouteStopName;
	}
	private String toStatusCode;
	private String toRouteStopName;

	public String getToNameFormatted() {
		return toNameFormatted;
	}

	public void setToNameFormatted(String toNameFormatted) {
		this.toNameFormatted = toNameFormatted;
	}

	private String toNameFormatted;
	private String toRouteStopId;


	public String getRoutePathId()
	{
		return routePathId;
	}
	public void setRoutePathId(final String routePathId)
	{
		this.routePathId=routePathId;
	}
	public String getFromStatusCode()
	{
		return fromStatusCode;
	}
	public void setFromStatusCode(final String fromStatusName)
	{
		this.fromStatusCode=fromStatusName;
	}
	public String getToStatusCode()
	{
		return toStatusCode;
	}
	public void setToStatusCode(final String toStatusName)
	{
		this.toStatusCode=toStatusName;
	}
	

	/**
	 * @return Returns the toRouteStopId.
	 */
	public String getToRouteStopId() {
		return toRouteStopId;
	}

	/**
	 * @param toRouteStopId The toRouteStopId to set.
	 */
	public void setToRouteStopId(final String toRouteStopId) {
		this.toRouteStopId = toRouteStopId;
	}

}

	
