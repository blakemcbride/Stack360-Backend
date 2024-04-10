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

package com.arahant.services.standard.project.projectCurrentStatus;

import org.kissweb.database.Record;

public class ListPossibleRoutesReturnItem {

	private String routeId;
	private String routeName;

	public ListPossibleRoutesReturnItem() {
	}

	ListPossibleRoutesReturnItem(final Record bc) {
		try {
			routeId = bc.getString("route_id");
			routeName = bc.getString("name");
		} catch (Exception e) {
			routeId = routeName = "";
		}
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(final String routeId) {
		this.routeId = routeId;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(final String routeName) {
		this.routeName = routeName;
	}
}

	
