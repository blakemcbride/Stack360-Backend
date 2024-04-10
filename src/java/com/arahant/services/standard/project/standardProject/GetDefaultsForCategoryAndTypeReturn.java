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
package com.arahant.services.standard.project.standardProject;

import com.arahant.business.BRouteTypeAssoc;
import com.arahant.services.TransmitReturnBase;


/**
 * 
 *
 *
 */
public class GetDefaultsForCategoryAndTypeReturn extends TransmitReturnBase {

	void setData(final BRouteTypeAssoc bc)
	{
		try
		{
			routeId=bc.getRouteId();
			routeName=bc.getRouteName();
			
			//projectStatusId=bc.getProjectStatusId();
			//projectStatusCode=bc.getProjectStatusCode();
			//projectStatusDescription=bc.getProjectStatusDescription();

		}
		catch (final Exception e)
		{
			;//there wasn't anything
		}

	}
	
	private String routeId;
	private String routeName;

	//private String projectStatusId;
	//private String projectStatusCode;
	//private String projectStatusDescription;



	public String getRouteId()
	{
		return routeId;
	}
	public void setRouteId(final String routeId)
	{
		this.routeId=routeId;
	}
	public String getRouteName()
	{
		return routeName;
	}
	public void setRouteName(final String routeName)
	{
		this.routeName=routeName;
	}


}

	
