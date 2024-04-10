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
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "route")
public class Route extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = -2946647139087299901L;
	public static final String DESCRIPTION = "description";
	public static final String NAME = "name";
	public static final String INITIAL_ROUTE_STOP = "initalRouteStop";
	public static final String INITIAL_STATUS = "initialProjectStatus";
	public static final String ROUTE_ID = "routeId";
	public static final String ROUTE_STOPS = "routeStops";
	private String routeId;
	private String name;
	private String description;
	private ProjectStatus initialProjectStatus;
	private RouteStop initalRouteStop;
	private Set<RouteStop> routeStops = new HashSet<RouteStop>(0);
	private Set<RouteTypeAssoc> routeTypeAssociations = new HashSet<RouteTypeAssoc>(0);
	public static final String COMPANY_ID = "companyId";
	private String companyId;
	public static final String COMPANY = "company";
	private CompanyDetail company;
	public static final String LAST_ACTIVE_DATE = "lastActiveDate";
	private int lastActiveDate;

	public Route() {
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	public CompanyDetail getCompany() {
		return company;
	}

	public void setCompany(CompanyDetail company) {
		this.company = company;
	}

	@Column(name = "company_id", insertable = false, updatable = false)
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Column(name = "last_active_date")
	public int getLastActiveDate() {
		return lastActiveDate;
	}

	public void setLastActiveDate(int lastActiveDate) {
		this.lastActiveDate = lastActiveDate;
	}

	/**
	 * @return Returns the description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description The description to set.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return Returns the name.
	 */
	@Column(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return Returns the routeId.
	 */
	@Id
	@Column(name = "route_id")
	public String getRouteId() {
		return routeId;
	}

	/**
	 * @param routeId The routeId to set.
	 */
	public void setRouteId(final String routeId) {
		this.routeId = routeId;
	}

	/**
	 * @return Returns the routeStops.
	 */
	@OneToMany(mappedBy = RouteStop.ROUTE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RouteStop> getRouteStops() {
		return routeStops;
	}

	/**
	 * @param routeStops The routeStops to set.
	 */
	public void setRouteStops(final Set<RouteStop> routeStops) {
		this.routeStops = routeStops;
	}

	/**
	 * @return Returns the routeTypeAssociations.
	 */
	@OneToMany(mappedBy = RouteTypeAssoc.ROUTE, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(value = org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RouteTypeAssoc> getRouteTypeAssociations() {
		return routeTypeAssociations;
	}

	/**
	 * @param routeTypeAssociations The routeTypeAssociations to set.
	 */
	public void setRouteTypeAssociations(final Set<RouteTypeAssoc> routeTypeAssociations) {
		this.routeTypeAssociations = routeTypeAssociations;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {
		routeId = IDGenerator.generate(this);
		return routeId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {

		return "route_id";
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {

		return "route";
	}

	/**
	 * @return Returns the initalRouteStop.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "route_stop_id")
	public RouteStop getInitalRouteStop() {
		return initalRouteStop;
	}

	/**
	 * @param initalRouteStop The initalRouteStop to set.
	 */
	public void setInitalRouteStop(final RouteStop initalRouteStop) {
		this.initalRouteStop = initalRouteStop;
	}

	/**
	 * @return Returns the initialProjectStatus.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_status_id")
	public ProjectStatus getInitialProjectStatus() {
		return initialProjectStatus;
	}

	/**
	 * @param initialProjectStatus The initialProjectStatus to set.
	 */
	public void setInitialProjectStatus(final ProjectStatus initialProjectStatus) {
		this.initialProjectStatus = initialProjectStatus;
	}

	@Override
	public boolean equals(Object o) {
		if (routeId == null && o == null)
			return true;
		if (routeId != null && o instanceof Route)
			return routeId.equals(((Route) o).getRouteId());

		return false;
	}

	@Override
	public int hashCode() {
		if (routeId == null)
			return 0;
		return routeId.hashCode();
	}

	@Override
	public String toString() {
		return this.description;
	}
}
