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

import com.arahant.business.BRoutePath;
import com.arahant.services.TransmitInputBase;



/**
 * 
 *
 * Created on Feb 8, 2007
 *
 */
public class SaveRoutePathInput extends TransmitInputBase {

	void setData(final BRoutePath bc)
	{
		
		bc.setRoutePathId(routePathId);
		bc.setToRouteStopId(toRouteStopId);
		bc.setFromStatusId(fromStatusId);
		bc.setToStatusId(toStatusId);

	}
	
	@Validation (table="route_path",column="route_path_id",required=true)
	private String routePathId;
	@Validation (table="route_stop",column="route_stop_id",required=true)
	private String toRouteStopId;
	@Validation (table="project_status",column="project_status_id",required=true)
	private String fromStatusId;
	@Validation (table="project_status",column="project_status_id",required=true)
	private String toStatusId;

	public String getRoutePathId()
	{
		return routePathId;
	}
	public void setRoutePathId(final String routePathId)
	{
		this.routePathId=routePathId;
	}
	public String getToRouteStopId()
	{
		return toRouteStopId;
	}
	public void setToRouteStopId(final String toRouteStopId)
	{
		this.toRouteStopId=toRouteStopId;
	}
	public String getFromStatusId()
	{
		return fromStatusId;
	}
	public void setFromStatusId(final String fromStatusId)
	{
		this.fromStatusId=fromStatusId;
	}
	public String getToStatusId()
	{
		return toStatusId;
	}
	public void setToStatusId(final String toStatusId)
	{
		this.toStatusId=toStatusId;
	}

}

	
