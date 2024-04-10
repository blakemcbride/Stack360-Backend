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
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;

public class BRouteTypeAssoc extends SimpleBusinessObjectBase<RouteTypeAssoc> {

	public BRouteTypeAssoc() {
	}

	/**
	 * @param projectCategoryId
	 * @param projectTypeId
	 */
	public BRouteTypeAssoc(final String projectCategoryId, final String projectTypeId) {
		final HibernateCriteriaUtil<RouteTypeAssoc> hcu = ArahantSession.getHSU().createCriteria(RouteTypeAssoc.class);
		hcu.joinTo(RouteTypeAssoc.PROJECT_CATEGORY)
				.eq(ProjectCategory.PROJECTCATEGORYID, projectCategoryId);
		hcu.joinTo(RouteTypeAssoc.PROJECT_TYPE)
				.eq(ProjectType.PROJECTTYPEID, projectTypeId);
		bean = hcu.first();
	}

	public String getCompanyName() {
		try {
			if (bean.getRoute() == null || bean.getRoute().getInitalRouteStop() == null)
				return "Requesting Company";

			BOrgGroup borg = new BOrgGroup(bean.getRoute().getInitalRouteStop().getOrgGroup());
			if (borg.isCompany())
				return borg.getName();
			return borg.getCompanyName();
		} catch (final Exception e) {
			return "";
		}
	}

	public String getRouteStopTypeFormatted() {
		try {
			return new BRouteStop(bean.getRoute().getInitalRouteStop()).getTypeFormatted();
		} catch (final Exception e) {
			return "";
		}
	}

	@Override
	public void insert() throws ArahantException {
		final RouteTypeAssoc rta = ArahantSession.getHSU().createCriteria(RouteTypeAssoc.class)
				.eq(RouteTypeAssoc.PROJECT_CATEGORY, bean.getProjectCategory())
				.eq(RouteTypeAssoc.PROJECT_TYPE, bean.getProjectType())
				.first();
		if (rta != null)
			throw new ArahantWarning("The Category/Type combination of '"
					+ bean.getProjectCategory().getCode() + "/" + bean.getProjectType().getCode()
					+ "' is already in use in Project Route '" + rta.getRoute().getName() + "'.");
		super.insert();
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new RouteTypeAssoc();
		return bean.generateId();
	}

	/**
	 * @return @see com.arahant.beans.RouteTypeAssoc#getProjectCategory()
	 */
	public ProjectCategory getProjectCategory() {
		return bean.getProjectCategory();
	}

	/**
	 * @return @see com.arahant.beans.RouteTypeAssoc#getProjectType()
	 */
	public ProjectType getProjectType() {
		return bean.getProjectType();
	}

	/**
	 * @return @see com.arahant.beans.RouteTypeAssoc#getRoute()
	 */
	public Route getRoute() {
		return bean.getRoute();
	}

	/**
	 * @return @see com.arahant.beans.RouteTypeAssoc#getRouteAssocId()
	 */
	public String getRouteAssocId() {
		return bean.getRouteAssocId();
	}

	/**
	 * @param projectCategory
	 * @see
	 * com.arahant.beans.RouteTypeAssoc#setProjectCategory(com.arahant.beans.ProjectCategory)
	 */
	public void setProjectCategory(final ProjectCategory projectCategory) {
		bean.setProjectCategory(projectCategory);
	}

	/**
	 * @param projectType
	 * @see
	 * com.arahant.beans.RouteTypeAssoc#setProjectType(com.arahant.beans.ProjectType)
	 */
	public void setProjectType(final ProjectType projectType) {
		bean.setProjectType(projectType);
	}

	/**
	 * @param route
	 * @see com.arahant.beans.RouteTypeAssoc#setRoute(com.arahant.beans.Route)
	 */
	public void setRoute(final Route route) {
		bean.setRoute(route);
	}

	/**
	 * @param routeAssocId
	 * @see com.arahant.beans.RouteTypeAssoc#setRouteAssocId(java.lang.String)
	 */
	public void setRouteAssocId(final String routeAssocId) {
		bean.setRouteAssocId(routeAssocId);
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	@Override
	public void load(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(RouteTypeAssoc.class, key);
	}

	/**
	 * @param projectCategoryId
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		bean.setProjectCategory(ArahantSession.getHSU().get(ProjectCategory.class, projectCategoryId));
	}

	/**
	 * @param projectCategoryId
	 */
	public void setProjectTypeId(final String projectTypeId) {
		bean.setProjectType(ArahantSession.getHSU().get(ProjectType.class, projectTypeId));
	}

	/**
	 * @param routeId
	 */
	public void setRouteId(final String routeId) {
		bean.setRoute(ArahantSession.getHSU().get(Route.class, routeId));
	}

	/**
	 * @return
	 */
	public String getRouteId() {
		try {
			return bean.getRoute().getRouteId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getRouteName() {

		try {
			return bean.getRoute().getName();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getRouteStopId() {

		try {
			return bean.getRoute().getInitalRouteStop().getRouteStopId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getOrgGroupName() {


		try {
			if (bean.getRoute() == null || bean.getRoute().getInitalRouteStop() == null)
				return "Requesting Group";

			BOrgGroup borg = new BOrgGroup(bean.getRoute().getInitalRouteStop().getOrgGroup());
			if (borg.isCompany())
				return "";
			return borg.getName();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getRouteStopName() {

		try {
			return bean.getRoute().getInitalRouteStop().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getProjectStatusId() {

		try {
			return bean.getRoute().getInitialProjectStatus().getProjectStatusId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getProjectStatusCode() {

		try {
			return bean.getRoute().getInitialProjectStatus().getCode();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getProjectStatusDescription() {

		try {
			return bean.getRoute().getInitialProjectStatus().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getOrgGroupId() {

		try {
			return bean.getRoute().getInitalRouteStop().getOrgGroup().getOrgGroupId();
		} catch (final Exception e) {
			return "";
		}
	}
}
