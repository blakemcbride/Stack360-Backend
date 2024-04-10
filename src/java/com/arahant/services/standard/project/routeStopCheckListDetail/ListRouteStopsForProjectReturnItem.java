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
package com.arahant.services.standard.project.routeStopCheckListDetail;
import com.arahant.business.BRouteStop;


/**
 * 
 *
 *
 */
public class ListRouteStopsForProjectReturnItem {
	
	public ListRouteStopsForProjectReturnItem()
	{
		;
	}

	ListRouteStopsForProjectReturnItem (final BRouteStop bc)
	{
		routeStopId=bc.getRouteStopId();
		routeStopName=bc.getDescription();
		phaseId=bc.getPhaseId();
		phaseCode=bc.getPhaseCode();
		routeStopNameFormatted=bc.getRouteStopNameFormatted();
		autoAssignToSupervisors=bc.getAutoAssignToSupervisors();
	}
	
	private String routeStopId ;
	private String routeStopName;
	private String phaseId;
	private String phaseCode;
	private String routeStopNameFormatted;
	private boolean autoAssignToSupervisors;

	public boolean getAutoAssignToSupervisors() {
		return autoAssignToSupervisors;
	}

	public void setAutoAssignToSupervisors(boolean autoAssignToSupervisors) {
		this.autoAssignToSupervisors = autoAssignToSupervisors;
	}
	
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
	
	

	public String getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(String phaseId) {
		this.phaseId = phaseId;
	}
	
	

	public String getRouteStopName()
	{
		return routeStopName;
	}
	public void setRouteStopName(final String routeStopName)
	{
		this.routeStopName=routeStopName;
	}

	/**
	 * @return Returns the routeStopId.
	 */
	public String getRouteStopId() {
		return routeStopId;
	}

	/**
	 * @param routeStopId The routeStopId to set.
	 */
	public void setRouteStopId(final String routeStopId) {
		this.routeStopId = routeStopId;
	}

}

	
