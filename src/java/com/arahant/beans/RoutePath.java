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
@Table(name = "route_path")
public class RoutePath extends ArahantBean implements java.io.Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8557333399856289560L;
	public static final String TO_ROUTE_STOP = "toRouteStop";
	public static final String FROM_ROUTE_STOP = "fromRouteStop";
	public static final String TO_PROJECT_STATUS = "toProjectStatus";
	public static final String FROM_PROJECT_STATUS = "fromProjectStatus";
	public static final String ROUTE_PATH_ID = "routePathId";
	private String routePathId;
	private RouteStop toRouteStop;
	private RouteStop fromRouteStop;
	private ProjectStatus toProjectStatus;
	private ProjectStatus fromProjectStatus;

	public RoutePath() {
	}

	/**
	 * @return Returns the routePathId.
	 */
	@Id
	@Column(name = "route_path_id")
	public String getRoutePathId() {
		return routePathId;
	}

	/**
	 * @param routePathId The routePathId to set.
	 */
	public void setRoutePathId(final String routePathId) {
		this.routePathId = routePathId;
	}

	/**
	 * @return Returns the fromProjectStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_status_id")
	public ProjectStatus getFromProjectStatus() {
		return fromProjectStatus;
	}

	/**
	 * @param fromProjectStatus The fromProjectStatus to set.
	 */
	public void setFromProjectStatus(final ProjectStatus fromProjectStatus) {
		this.fromProjectStatus = fromProjectStatus;
	}

	/**
	 * @return Returns the fromRouteStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_stop_id")
	public RouteStop getFromRouteStop() {
		return fromRouteStop;
	}

	/**
	 * @param fromRouteStop The fromRouteStop to set.
	 */
	public void setFromRouteStop(final RouteStop fromRouteStop) {
		this.fromRouteStop = fromRouteStop;
	}

	/**
	 * @return Returns the toProjectStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_status_id")
	public ProjectStatus getToProjectStatus() {
		return toProjectStatus;
	}

	/**
	 * @param toProjectStatus The toProjectStatus to set.
	 */
	public void setToProjectStatus(final ProjectStatus toProjectStatus) {
		this.toProjectStatus = toProjectStatus;
	}

	/**
	 * @return Returns the toRouteStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_stop_id")
	public RouteStop getToRouteStop() {
		return toRouteStop;
	}

	/**
	 * @param toRouteStop The toRouteStop to set.
	 */
	public void setToRouteStop(final RouteStop toRouteStop) {
		this.toRouteStop = toRouteStop;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {
		routePathId = IDGenerator.generate(this);
		return routePathId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {
		return "route_path_id";
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {
		return "route_path";
	}

	@Override
	public boolean equals(Object o) {
		if (routePathId == null && o == null)
			return true;
		if (routePathId != null && o instanceof RoutePath)
			return routePathId.equals(((RoutePath) o).getRoutePathId());

		return false;
	}

	@Override
	public int hashCode() {
		if (routePathId == null)
			return 0;
		return routePathId.hashCode();
	}
}
