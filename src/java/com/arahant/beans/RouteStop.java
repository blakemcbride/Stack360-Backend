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
@Table(name = "route_stop")
public class RouteStop extends ArahantBean implements java.io.Serializable {

	private static final long serialVersionUID = 6977269884577252767L;
	public static final String NAME = "description";
	public static final String ORG_GROUP = "orgGroup";
	public static final String ROUTE = "route";
	public static final String ROUTE_STOP_ID = "routeStopId";
	public static final String PROJECTS = "projects";
	public static final String ORG_GROUP_ID = "orgGroupId";
	public static final String ROUTE_PATH_AS_TO = "toRoutePaths";
	public static final String ROUTE_PATH_AS_FROM = "fromRoutePaths";
	public static final String PROJECT_PHASE = "projectPhase";
	private String routeStopId;
	private String description;
	private OrgGroup orgGroup;
	private ProjectPhase projectPhase;
	private Route route;
	private Set<RoutePath> toRoutePaths = new HashSet<RoutePath>(0);
	private Set<RoutePath> fromRoutePaths = new HashSet<RoutePath>(0);
	private Set<Project> projects = new HashSet<Project>(0);
	private Set<Route> routesWhereInitialStop = new HashSet<Route>(0);
	private Set<ProjectHistory> projectHistoriesByToStop = new HashSet<ProjectHistory>(0);
	private Set<ProjectHistory> projectHistoriesByFromStop = new HashSet<ProjectHistory>(0);
	private Set<ProjectStatus> allowedStatuses = new HashSet<ProjectStatus>();
	private String orgGroupId;
	private char autoAssignSupervisor = 'N';

	public RouteStop() {
	}

	@Column(name = "auto_assign_supervisor")
	public char getAutoAssignSupervisor() {
		return autoAssignSupervisor;
	}

	public void setAutoAssignSupervisor(char autoAssignSupervisor) {
		this.autoAssignSupervisor = autoAssignSupervisor;
	}

	@Column(name = "org_group_id", insertable = false, updatable = false)
	public String getOrgGroupId() {
		return orgGroupId;
	}

	public void setOrgGroupId(String orgGroupId) {
		this.orgGroupId = orgGroupId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_phase_id")
	public ProjectPhase getProjectPhase() {
		return projectPhase;
	}

	public void setProjectPhase(ProjectPhase projectPhase) {
		this.projectPhase = projectPhase;
	}

	/**
	 * @return Returns the projects.
	 */
	@OneToMany(mappedBy = Project.CURRENT_ROUTE_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projects The projects to set.
	 */
	public void setProjects(final Set<Project> projects) {
		this.projects = projects;
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
	 * @return Returns the routeStopId.
	 */
	@Id
	@Column(name = "route_stop_id")
	public String getRouteStopId() {
		return routeStopId;
	}

	/**
	 * @param routeStopId The routeStopId to set.
	 */
	public void setRouteStopId(final String routeStopId) {
		this.routeStopId = routeStopId;
	}

	/**
	 * @return Returns the fromRoutePaths.
	 */
	@OneToMany(mappedBy = RoutePath.FROM_ROUTE_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RoutePath> getFromRoutePaths() {
		return fromRoutePaths;
	}

	/**
	 * @param fromRoutePaths The fromRoutePaths to set.
	 */
	public void setFromRoutePaths(final Set<RoutePath> fromRoutePaths) {
		this.fromRoutePaths = fromRoutePaths;
	}

	/**
	 * @return Returns the orgGroup.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "org_group_id")
	public OrgGroup getOrgGroup() {
		return orgGroup;
	}

	/**
	 * @param orgGroup The orgGroup to set.
	 */
	public void setOrgGroup(final OrgGroup orgGroup) {
		this.orgGroup = orgGroup;
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
	 * @return Returns the toRoutePaths.
	 */
	@OneToMany(mappedBy = RoutePath.TO_ROUTE_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<RoutePath> getToRoutePaths() {
		return toRoutePaths;
	}

	/**
	 * @param toRoutePaths The toRoutePaths to set.
	 */
	public void setToRoutePaths(final Set<RoutePath> toRoutePaths) {
		this.toRoutePaths = toRoutePaths;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#generateId()
	 */

	@Override
	public String generateId() throws ArahantException {

		routeStopId = IDGenerator.generate(this);
		return routeStopId;
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#keyColumn()
	 */

	@Override
	public String keyColumn() {

		return "route_stop_id";
	}
	/*
	 * (non-Javadoc) @see com.arahant.beans.ArahantBean#tableName()
	 */

	@Override
	public String tableName() {

		return "route_stop";
	}

	/**
	 * @return Returns the routesWhereInitialStop.
	 */
	@OneToMany(mappedBy = Route.INITIAL_ROUTE_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<Route> getRoutesWhereInitialStop() {
		return routesWhereInitialStop;
	}

	/**
	 * @param routesWhereInitialStop The routesWhereInitialStop to set.
	 */
	public void setRoutesWhereInitialStop(final Set<Route> routesWhereInitialStop) {
		this.routesWhereInitialStop = routesWhereInitialStop;
	}

	/**
	 * @return Returns the projectHistoriesByFromStop.
	 */
	@OneToMany(mappedBy = ProjectHistory.FROM_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectHistory> getProjectHistoriesByFromStop() {
		return projectHistoriesByFromStop;
	}

	/**
	 * @param projectHistoriesByFromStop The projectHistoriesByFromStop to set.
	 */
	public void setProjectHistoriesByFromStop(
			final Set<ProjectHistory> projectHistoriesByFromStop) {
		this.projectHistoriesByFromStop = projectHistoriesByFromStop;
	}

	/**
	 * @return Returns the projectHistoriesByToStop.
	 */
	@OneToMany(mappedBy = ProjectHistory.TO_STOP, fetch = FetchType.LAZY)
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	public Set<ProjectHistory> getProjectHistoriesByToStop() {
		return projectHistoriesByToStop;
	}

	/**
	 * @param projectHistoriesByToStop The projectHistoriesByToStop to set.
	 */
	public void setProjectHistoriesByToStop(
			final Set<ProjectHistory> projectHistoriesByToStop) {
		this.projectHistoriesByToStop = projectHistoriesByToStop;
	}

	@Override
	public boolean equals(Object o) {

		if (routeStopId == null && o == null)
			return true;
		if (routeStopId != null && o instanceof RouteStop) {
			String id = ((RouteStop) o).getRouteStopId();
			return routeStopId.equals(id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (routeStopId == null)
			return 0;
		return routeStopId.hashCode();
	}

	@ManyToMany
	@JoinTable(name = "project_status_rs_join",
	joinColumns = {
		@JoinColumn(name = "route_stop_id")},
	inverseJoinColumns = {
		@JoinColumn(name = "project_status_id")})
	public Set<ProjectStatus> getAllowedStatuses() {
		return allowedStatuses;
	}

	public void setAllowedStatuses(Set<ProjectStatus> allowedStatuses) {
		this.allowedStatuses = allowedStatuses;
	}

	@Override
	public String toString() {
		return this.description;
	}
}
