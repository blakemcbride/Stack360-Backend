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


package com.arahant.beans;

import com.arahant.exceptions.ArahantException;
import com.arahant.utils.IDGenerator;
import javax.persistence.*;

@Entity
@Table(name = "route_type_assoc")
public class RouteTypeAssoc extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 7853796633151192228L;
	public static final String PROJECT_CATEGORY = "projectCategory";
	public static final String PROJECT_TYPE = "projectType";
	public static final String ROUTE = "route";
	private String routeAssocId;
	private Route route;
	private ProjectCategory projectCategory;
	private ProjectType projectType;

	public RouteTypeAssoc() {
	}

	/**
	 * @return Returns the projectCategory.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_category_id")
	public ProjectCategory getProjectCategory() {
		return projectCategory;
	}

	/**
	 * @param projectCategory The projectCategory to set.
	 */
	public void setProjectCategory(final ProjectCategory projectCategory) {
		this.projectCategory = projectCategory;
	}

	/**
	 * @return Returns the projectType.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_type_id")
	public ProjectType getProjectType() {
		return projectType;
	}

	/**
	 * @param projectType The projectType to set.
	 */
	public void setProjectType(final ProjectType projectType) {
		this.projectType = projectType;
	}

	/**
	 * @return Returns the route.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_id")
	public Route getRoute() {
		return route;
	}

	/**
	 * @param route The route to set.
	 */
	public void setRoute(final Route route) {
		this.route = route;
	}

	/**
	 * @return Returns the routeAssocId.
	 */
	@Id
	@Column(name = "route_assoc_id")
	public String getRouteAssocId() {
		return routeAssocId;
	}

	/**
	 * @param routeAssocId The routeAssocId to set.
	 */
	public void setRouteAssocId(final String routeAssocId) {
		this.routeAssocId = routeAssocId;
	}

	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */
	@Override
	public String generateId() throws ArahantException {
		routeAssocId = IDGenerator.generate(this);
		return routeAssocId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return "route_assoc_id";
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {
		return "route_type_assoc";
	}

	@Override
	public boolean equals(Object o) {
		if (routeAssocId == null && o == null)
			return true;
		if (routeAssocId != null && o instanceof RouteTypeAssoc)
			return routeAssocId.equals(((RouteTypeAssoc) o).getRouteAssocId());

		return false;
	}

	@Override
	public int hashCode() {
		if (routeAssocId == null)
			return 0;
		return routeAssocId.hashCode();
	}
}
