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

import com.arahant.beans.ProjectStatus;
import com.arahant.beans.RoutePath;
import com.arahant.beans.RouteStop;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;

public class BRoutePath extends SimpleBusinessObjectBase<RoutePath> {

	public BRoutePath() {
	}

	/**
	 * @param routePathId
	 * @throws ArahantException
	 */
	public BRoutePath(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param path
	 */
	public BRoutePath(final RoutePath path) {
		bean = path;
	}

	@Override
	public void insert() {
		if (ArahantSession.getHSU().createCriteria(RoutePath.class).eq(RoutePath.FROM_ROUTE_STOP, bean.getFromRouteStop()).eq(RoutePath.FROM_PROJECT_STATUS, bean.getFromProjectStatus()).exists())
			throw new ArahantWarning("The From Stop - From Status combination must be unique.");
		super.insert();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getFromProjectStatus()
	 */
	public ProjectStatus getFromProjectStatus() {
		return bean.getFromProjectStatus();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getFromRouteStop()
	 */
	public RouteStop getFromRouteStop() {
		return bean.getFromRouteStop();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getFromStatusId()
	 */
	public String getFromStatusId() {
		return bean.getFromProjectStatus().getProjectStatusId();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getFromStopId()
	 */
	public String getFromStopId() {
		return bean.getFromRouteStop().getRouteStopId();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getRoutePathId()
	 */
	public String getRoutePathId() {
		return bean.getRoutePathId();
	}

	public String getToCompanyId() {
		try {
			if (bean.getToRouteStop().getOrgGroup() == null)
				return "ReqCo";
			return bean.getToRouteStop().getOrgGroup().getOwningCompany().getOrgGroupId();

		} catch (Exception e) {
			return "";
		}
	}

	public String getToCompanyName() {
		try {
			if (new BOrgGroup(bean.getToRouteStop().getOrgGroup()).isCompany())
				return bean.getToRouteStop().getOrgGroup().getName();
			return bean.getToRouteStop().getOrgGroup().getOwningCompany().getName();

		} catch (Exception e) {
			return "";
		}
	}

	public String getToNameFormatted() {
		return new BRouteStop(bean.getToRouteStop()).getOrgGroupNameFormatted();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getToProjectStatus()
	 */
	public ProjectStatus getToProjectStatus() {
		return bean.getToProjectStatus();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getToRouteStop()
	 */
	public RouteStop getToRouteStop() {
		return bean.getToRouteStop();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getToStatusId()
	 */
	public String getToStatusId() {
		return bean.getToProjectStatus().getProjectStatusId();
	}

	/**
	 * @return @see com.arahant.beans.RoutePath#getToStopId()
	 */
	public String getToStopId() {
		return bean.getToRouteStop().getRouteStopId();
	}

	/**
	 * @param fromProjectStatus
	 * @see
	 * com.arahant.beans.RoutePath#setFromProjectStatus(com.arahant.beans.ProjectStatus)
	 */
	public void setFromProjectStatus(final ProjectStatus fromProjectStatus) {
		bean.setFromProjectStatus(fromProjectStatus);
	}

	/**
	 * @param fromRouteStop
	 * @see
	 * com.arahant.beans.RoutePath#setFromRouteStop(com.arahant.beans.RouteStop)
	 */
	public void setFromRouteStop(final RouteStop fromRouteStop) {
		bean.setFromRouteStop(fromRouteStop);
	}

	/**
	 * @param fromStatusId
	 * @see com.arahant.beans.RoutePath#setFromStatusId(java.lang.String)
	 */
	public void setFromStatusId(final String fromStatusId) {
		bean.setFromProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, fromStatusId));
	}

	/**
	 * @param fromStopId
	 * @see com.arahant.beans.RoutePath#setFromStopId(java.lang.String)
	 */
	public void setFromStopId(final String fromStopId) {
		bean.setFromRouteStop(ArahantSession.getHSU().get(RouteStop.class, fromStopId));
	}

	/**
	 * @param routePathId
	 * @see com.arahant.beans.RoutePath#setRoutePathId(java.lang.String)
	 */
	public void setRoutePathId(final String routePathId) {
		bean.setRoutePathId(routePathId);
	}

	/**
	 * @param toProjectStatus
	 * @see
	 * com.arahant.beans.RoutePath#setToProjectStatus(com.arahant.beans.ProjectStatus)
	 */
	public void setToProjectStatus(final ProjectStatus toProjectStatus) {
		bean.setToProjectStatus(toProjectStatus);
	}

	/**
	 * @param toRouteStop
	 * @see
	 * com.arahant.beans.RoutePath#setToRouteStop(com.arahant.beans.RouteStop)
	 */
	public void setToRouteStop(final RouteStop toRouteStop) {
		bean.setToRouteStop(toRouteStop);
	}

	/**
	 * @param toStatusId
	 * @see com.arahant.beans.RoutePath#setToStatusId(java.lang.String)
	 */
	public void setToStatusId(final String toStatusId) {
		bean.setToProjectStatus(ArahantSession.getHSU().get(ProjectStatus.class, toStatusId));
	}

	/**
	 * @param toStopId
	 * @see com.arahant.beans.RoutePath#setToStopId(java.lang.String)
	 */
	public void setToStopId(final String toStopId) {
		bean.setToRouteStop(ArahantSession.getHSU().get(RouteStop.class, toStopId));
	}

	@Override
	public String create() throws ArahantException {
		bean = new RoutePath();
		return bean.generateId();
	}

	/**
	 * @return
	 */
	public String getFromStatusCode() {

		return bean.getFromProjectStatus().getCode();
	}

	/**
	 * @return
	 */
	public String getToStatusCode() {

		return bean.getToProjectStatus().getCode();
	}

	/**
	 * @return
	 */
	public String getToOrgGroupName() {
		try {
			if (new BOrgGroup(bean.getToRouteStop().getOrgGroup()).isCompany())
				return "";
			return bean.getToRouteStop().getOrgGroup().getName();

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getToRouteStopName() {

		return bean.getToRouteStop().getDescription();
	}

	/**
	 * @return
	 */
	public String getToOrgGroupId() {
		try {
			if (bean.getToRouteStop().getOrgGroup() == null)
				return "ReqOrg";
			return bean.getToRouteStop().getOrgGroup().getOrgGroupId();
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getToRouteStopId() {

		return bean.getToRouteStop().getRouteStopId();
	}

	/**
	 * @param fromRouteStopId
	 */
	public void setFromRouteStopId(final String fromRouteStopId) {
		bean.setFromRouteStop(ArahantSession.getHSU().get(RouteStop.class, fromRouteStopId));
	}

	/**
	 * @param toRouteStopId
	 */
	public void setToRouteStopId(final String toRouteStopId) {
		bean.setToRouteStop(ArahantSession.getHSU().get(RouteStop.class, toRouteStopId));
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (String element : ids)
			new BRoutePath(element).delete();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(RoutePath.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @return
	 */
	public String getRouteId() {
		try {
			return bean.getToRouteStop().getRoute().getRouteId();
		} catch (final Exception e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	public String getRouteName() {

		try {
			return bean.getToRouteStop().getRoute().getDescription();
		} catch (final Exception e) {
			return "";
		}
	}
}
