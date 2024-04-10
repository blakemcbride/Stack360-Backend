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
package com.arahant.services.standard.hr.projectCheckList;
import com.arahant.business.BRouteStop;


/**
 * 
 *
 *
 */
public class ListRouteStopsForProjectReturnItem {
	
	public ListRouteStopsForProjectReturnItem()
	{
		
	}

	ListRouteStopsForProjectReturnItem (BRouteStop bc)
	{
		
		routeStopId=bc.getRouteStopId();
		routeStopNameFormatted=bc.getRouteStopNameWithAssignment();
		personId=bc.getPersonId();

	}
	
	private String routeStopId;
	private String routeStopNameFormatted;
	private String personId;

	public String getRouteStopNameFormatted() {
		return routeStopNameFormatted;
	}

	public void setRouteStopNameFormatted(String routeStopNameFormatted) {
		this.routeStopNameFormatted = routeStopNameFormatted;
	}

	
	
	public String getRouteStopId()
	{
		return routeStopId;
	}
	public void setRouteStopId(String routeStopId)
	{
		this.routeStopId=routeStopId;
	}
	public String getPersonId()
	{
		return personId;
	}
	public void setPersonId(String personId)
	{
		this.personId=personId;
	}

}

	
