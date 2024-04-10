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


package com.arahant.business;

import com.arahant.beans.*;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BRouteStop extends SimpleBusinessObjectBase<RouteStop> {

	static BRouteStop[] makeArray(List<RouteStop> l) {
		BRouteStop[] ret = new BRouteStop[l.size()];

		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BRouteStop(l.get(loop));

		return ret;
	}

	public BRouteStop() {
	}

	/**
	 * @param routeStopId
	 * @throws ArahantException
	 */
	public BRouteStop(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param stop
	 */
	public BRouteStop(final RouteStop stop) {
		bean = stop;
	}

	public static String findOrMake(String id) {
		RouteStop rs = ArahantSession.getHSU().createCriteria(RouteStop.class).eq(RouteStop.ROUTE_STOP_ID, id).first();

		if (rs != null)
			return rs.getRouteStopId();

		String projectStatusId;

		ProjectStatus projectStatusBean = ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, "Misc").first();
		if (projectStatusBean == null) {
			BProjectStatus status = new BProjectStatus();
			projectStatusId = status.create();
			status.setActive('Y');
			status.setCode("Misc");
			status.setDescription("Catch-all Status for Projects");
			status.insert();
		} else
			projectStatusId = projectStatusBean.getProjectStatusId();

		String ret;

		Route routeBean = ArahantSession.getHSU().createCriteria(Route.class).eq(Route.NAME, "Misc").first();
		if (routeBean == null) {
			BProjectPhase phase = new BProjectPhase();
			phase.create();
			phase.setCategoryId(2); // development, which is 1 (setter subtracts 1)
			phase.setCode("Misc");
			phase.setDescription("Catch-all Phase for Route Stops");
			phase.insert();

			BRoute route = new BRoute();
			route.create();
			route.setDescription("Catch-all Route for Projects");
			route.setName("Misc");
			route.setDefaultStatusId(projectStatusId);
			route.insert();

			BRouteStop routeStop = new BRouteStop();
			ret = routeStop.create();
			routeStop.setDescription("Single Route Stop");
			routeStop.setPhaseId(phase.getId());
			routeStop.setProjectStatusIds(new String[]{projectStatusId});
			routeStop.setRouteId(route.getRouteId());
			routeStop.insert();

			route.setDefaultRouteStop(routeStop.getRouteStopId());
			route.update();
		} else
			ret = new BRoute(routeBean.getRouteId()).getRouteStopId();

		return ret;
	}

	public static String findOrMakeByName(String name) {
		RouteStop rs = ArahantSession.getHSU().createCriteria(RouteStop.class).eq(RouteStop.NAME, name).first();

		if (rs != null)
			return rs.getRouteStopId();

		String projectStatusId;

		ProjectStatus projectStatusBean = ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, "Misc").first();
		if (projectStatusBean == null) {
			BProjectStatus status = new BProjectStatus();
			projectStatusId = status.create();
			status.setActive('Y');
			status.setCode("Misc");
			status.setDescription("Catch-all Status for Projects");
			status.insert();
		} else
			projectStatusId = projectStatusBean.getProjectStatusId();

		String ret;

		Route routeBean = ArahantSession.getHSU().createCriteria(Route.class).eq(Route.NAME, "Misc").first();
		if (routeBean == null) {
			BProjectPhase phase = new BProjectPhase();
			phase.create();
			phase.setCategoryId(2); // development, which is 1 (setter subtracts 1)
			phase.setCode("Misc");
			phase.setDescription("Catch-all Phase for Route Stops");
			phase.insert();

			BRoute route = new BRoute();
			route.create();
			route.setDescription("Catch-all Route for Projects");
			route.setName("Misc");
			route.setDefaultStatusId(projectStatusId);
			route.insert();

			BRouteStop routeStop = new BRouteStop();
			ret = routeStop.create();
			routeStop.setDescription("Single Route Stop");
			routeStop.setPhaseId(phase.getId());
			routeStop.setProjectStatusIds(new String[]{projectStatusId});
			routeStop.setRouteId(route.getRouteId());
			routeStop.insert();

			route.setDefaultRouteStop(routeStop.getRouteStopId());
			route.update();
		} else
			ret = routeBean.getInitalRouteStop().getRouteStopId();

		return ret;
	}

	public void addCheckListItem(int activeDate, int inactiveDate, String description, String detail, boolean required, int loop) {
		BRouteStopChecklist brsc = new BRouteStopChecklist();
		brsc.create();
		brsc.setActiveDate(activeDate);
		brsc.setInactiveDate(inactiveDate);
		brsc.setDescription(description);
		brsc.setDetail(detail);
		brsc.setRequired(required);
		brsc.setPriority(loop);
		brsc.setRouteStop(bean);
		updates.add(brsc.bean);
	}

	public void clearChecklist() {
		ArahantSession.getHSU().createCriteria(RouteStopChecklist.class).eq(RouteStopChecklist.ROUTE_STOP, bean).delete();
	}

	public void clearChecklist(List<String> keepIds) {
		ArahantSession.getHSU().createCriteria(RouteStopChecklist.class).eq(RouteStopChecklist.ROUTE_STOP, bean).notIn(RouteStopChecklist.ID, keepIds).delete();

		//need to set the seq high on these

		short high = 1000;

		for (RouteStopChecklist c : ArahantSession.getHSU().createCriteria(RouteStopChecklist.class).eq(RouteStopChecklist.ROUTE_STOP, bean).list()) {
			c.setItemPriority(high++);
			ArahantSession.getHSU().saveOrUpdate(c);
		}
	}


	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new RouteStop();
		return bean.generateId();
	}

	@Override
	public void delete() throws ArahantDeleteException {

		if (bean.equals(bean.getRoute().getInitalRouteStop())) {
			bean.getRoute().setInitalRouteStop(null);
			ArahantSession.getHSU().insert(bean.getRoute());
		}

		for (final RoutePath rp : bean.getFromRoutePaths())
			new BRoutePath(rp).delete();

		for (final RoutePath rp : bean.getToRoutePaths())
			new BRoutePath(rp).delete();

		super.delete();
	}

	public Set<ProjectStatus> getAllowedStatuses() {
		return bean.getAllowedStatuses();
	}

	public boolean getAutoAssignToSupervisors() {
		return bean.getAutoAssignSupervisor() == 'Y';
	}

	public String getCompanyId() {
		if (bean.getOrgGroup() == null)
			return "ReqCo";
		return bean.getOrgGroup().getOwningCompany().getOrgGroupId();
	}

	public String getCompanyName() {
		if (bean.getOrgGroup() == null)
			return "Requesting Company";
		if (bean.getOrgGroup().getOwningCompany() != null)
			return bean.getOrgGroup().getOwningCompany().getName();
		return bean.getOrgGroup().getName();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getDescription()
	 */
	public String getDescription() {
		return bean.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getFromRoutePaths()
	 */
	public Set<RoutePath> getFromRoutePaths() {
		return bean.getFromRoutePaths();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getOrgGroup()
	 */
	public OrgGroup getOrgGroup() {
		return bean.getOrgGroup();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getOrgGroupId()
	 */
	public String getOrgGroupId() {
		try {
			if (bean.getOrgGroup() == null || isEmpty(bean.getOrgGroup().getOrgGroupId()))
				return "ReqOrg";

			return bean.getOrgGroup().getOrgGroupId();
		} catch (Exception e) {
			return "";
		}
	}

	public String getPersonDisplayName() {
		if (bean.getOrgGroup() == null)
			return "";
		Person p = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, bean.getOrgGroup()).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').first();

		if (p == null)
			return "";

		return p.getNameLFM();
	}

	public String getPersonId() {
		if (bean.getOrgGroup() == null)
			return "";
		Person p = ArahantSession.getHSU().createCriteria(Person.class).joinTo(Person.ORGGROUPASSOCIATIONS).eq(OrgGroupAssociation.ORGGROUP, bean.getOrgGroup()).eq(OrgGroupAssociation.PRIMARYINDICATOR, 'Y').first();

		if (p == null)
			return "";

		return p.getPersonId();
	}

	public String getPhaseCode() {
		return bean.getProjectPhase().getCode();
	}

	public String getPhaseId() {
		try {
			return bean.getProjectPhase().getProjectPhaseId();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getProjects()
	 */
	public Set<Project> getProjects() {
		return bean.getProjects();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getRoute()
	 */
	public Route getRoute() {
		return bean.getRoute();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getRouteId()
	 */
	public String getRouteId() {
		return bean.getRoute().getRouteId();
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getRouteStopId()
	 */
	public String getRouteStopId() {
		return bean.getRouteStopId();
	}

	public String getRouteStopNameFormatted() {
		return bean.getDescription() + " (" + bean.getProjectPhase().getCode() + ")";
	}

	public String getRouteStopNameWithAssignment() {
		if (bean.getAutoAssignSupervisor() != 'Y')
			return bean.getDescription() + " (anyone)";
		BOrgGroup borg = new BOrgGroup(getOrgGroup());
		BPerson bp = new BPerson(borg.getFirstSupervisorId());
		return bean.getDescription() + " (" + bp.getNameLFM() + ")";
	}

	/**
	 * @return @see com.arahant.beans.RouteStop#getToRoutePaths()
	 */
	public Set<RoutePath> getToRoutePaths() {
		return bean.getToRoutePaths();
	}

	public String getTypeFormatted() {
		if (bean.getOrgGroup() == null)
			return "Any";

		String tname = getOrgGroupTypeName();

		if (isEmpty(tname))
			tname = "Any";

		return tname;
	}

	public void setAutoAssignToSupervisors(boolean autoAssignToSupervisors) {
		bean.setAutoAssignSupervisor(autoAssignToSupervisors ? 'Y' : 'N');
	}

	/**
	 * @param description
	 * @see com.arahant.beans.RouteStop#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		bean.setDescription(description);
	}

	/**
	 * @param fromRoutePaths
	 * @see com.arahant.beans.RouteStop#setFromRoutePaths(java.util.Set)
	 */
	public void setFromRoutePaths(final Set<RoutePath> fromRoutePaths) {
		bean.setFromRoutePaths(fromRoutePaths);
	}

	/**
	 * @param orgGroup
	 * @see com.arahant.beans.RouteStop#setOrgGroup(com.arahant.beans.OrgGroup)
	 */
	public void setOrgGroup(final OrgGroup orgGroup) {
		bean.setOrgGroup(orgGroup);
	}

	/**
	 * @param orgGroupId
	 * @see com.arahant.beans.RouteStop#setOrgGroupId(java.lang.String)
	 */
	public void setOrgGroupId(final String orgGroupId) {
		bean.setOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setPhaseId(String phaseId) {
		bean.setProjectPhase(ArahantSession.getHSU().get(ProjectPhase.class, phaseId));
	}

	public void setProjectStatusIds(String[] statusIds) {
		//need to remove ones that were assigned, but aren't in this list

		Set<String> newValues = new HashSet<String>();
		Collections.addAll(newValues, statusIds);


		for (ProjectStatus ps : bean.getAllowedStatuses())
			if (!newValues.contains(ps.getProjectStatusId())) {
				//an appropriate error message should be returned if a status is in use in a route path where the from route stop is the specified routeStopId
				if (ArahantSession.getHSU().createCriteria(RoutePath.class).eq(RoutePath.FROM_ROUTE_STOP, bean).joinTo(RoutePath.FROM_PROJECT_STATUS).eq(ProjectStatus.PROJECTSTATUSID, ps.getProjectStatusId()).exists())
					throw new ArahantWarning("Can not remove status " + ps.getCode() + " because it is in use by a Next Project Route Stop.");


				if (ArahantSession.getHSU().createCriteria(RoutePath.class).eq(RoutePath.TO_ROUTE_STOP, bean).joinTo(RoutePath.TO_PROJECT_STATUS).eq(ProjectStatus.PROJECTSTATUSID, ps.getProjectStatusId()).exists())
					throw new ArahantWarning("Can not remove status " + ps.getCode() + " because it is in use.");


				if (ArahantSession.getHSU().createCriteria(Route.class).eq(Route.INITIAL_ROUTE_STOP, bean).joinTo(Route.INITIAL_STATUS).eq(ProjectStatus.PROJECTSTATUSID, ps.getProjectStatusId()).exists())
					throw new ArahantWarning("Can not remove status " + ps.getCode() + " because it is in use by the initial Route Stop.");
			}
		//clear old
		bean.getAllowedStatuses().clear();
		//add new
		for (String id : statusIds)
			bean.getAllowedStatuses().add(ArahantSession.getHSU().get(ProjectStatus.class, id));
	}

	/**
	 * @param projects
	 * @see com.arahant.beans.RouteStop#setProjects(java.util.Set)
	 */
	public void setProjects(final Set<Project> projects) {
		bean.setProjects(projects);
	}

	/**
	 * @param route
	 * @see com.arahant.beans.RouteStop#setRoute(com.arahant.beans.Route)
	 */
	public void setRoute(final Route route) {
		bean.setRoute(route);
	}

	/**
	 * @param routeId
	 * @see com.arahant.beans.RouteStop#setRouteId(java.lang.String)
	 */
	public void setRouteId(final String routeId) {
		bean.setRoute(ArahantSession.getHSU().get(Route.class, routeId));
	}

	/**
	 * @param routeStopId
	 * @see com.arahant.beans.RouteStop#setRouteStopId(java.lang.String)
	 */
	public void setRouteStopId(final String routeStopId) {
		bean.setRouteStopId(routeStopId);
	}

	/**
	 * @param toRoutePaths
	 * @see com.arahant.beans.RouteStop#setToRoutePaths(java.util.Set)
	 */
	public void setToRoutePaths(final Set<RoutePath> toRoutePaths) {
		bean.setToRoutePaths(toRoutePaths);
	}

	/**
	 * @return
	 */
	public String getOrgGroupName() {
		if (bean.getOrgGroup() == null || isEmpty(bean.getOrgGroup().getOrgGroupId()))
			return "Requesting Group";
		if (!bean.getOrgGroup().getOwningCompany().getOrgGroupId().equals(bean.getOrgGroup().getOrgGroupId()))
			return bean.getOrgGroup().getName();
		return "";
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String id) throws ArahantDeleteException, ArahantException {
		new BRouteStop(id).delete();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(RouteStop.class, key);
		if (bean == null)
			throw new ArahantException("Could not find route stop for key " + key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param routeStopId
	 * @param cap
	 * @return
	 */
	public BRoutePath[] list(final int cap) {

		final List<RoutePath> l = ArahantSession.getHSU().createCriteria(RoutePath.class).eq(RoutePath.FROM_ROUTE_STOP, bean).setMaxResults(cap).list();

		final BRoutePath[] ret = new BRoutePath[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BRoutePath(l.get(loop));


		return ret;
	}

	/**
	 * @return
	 */
	public String getOrgGroupTypeName() {

		final BOrgGroup borg = new BOrgGroup(bean.getOrgGroup());
		return borg.getTypeFormatted();
	}

	String getOrgGroupNameFormatted() {
		if (bean.getOrgGroup() != null)
			return new BOrgGroup(bean.getOrgGroup()).getNameFormatted();
		return "Requesting Company (Requesting Organizational Group)";
	}

	public BProjectStatus[] listProjectStatuses(int max) {
		return BProjectStatus.makeArray(ArahantSession.getHSU().createCriteria(ProjectStatus.class).setMaxResults(max).orderBy(ProjectStatus.CODE).joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).eq(RouteStop.ROUTE_STOP_ID, bean.getRouteStopId()).list());

	}

	public BProjectStatus[] searchForNotAssignedStatuses(String code, String description, int max) {

		List excludes = ArahantSession.getHSU().createCriteria(ProjectStatus.class).selectFields(ProjectStatus.PROJECTSTATUSID).joinTo(ProjectStatus.ALLOWED_ROUTE_STOPS).eq(RouteStop.ROUTE_STOP_ID, bean.getRouteStopId()).list();

		return BProjectStatus.makeArray(ArahantSession.getHSU().createCriteria(ProjectStatus.class).like(ProjectStatus.CODE, code).like(ProjectStatus.DESCRIPTION, description).setMaxResults(max).notIn(ProjectStatus.PROJECTSTATUSID, excludes).list());
	}
}
