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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.ProjectStatus;
import com.arahant.beans.RoutePath;
import com.arahant.beans.RouteStop;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.Collection;
import java.util.List;

/**
 *
 *
 *
 */
public class BProjectStatus extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BProjectStatus.class);
	ProjectStatus projectStatus;

	/**
	 */
	public BProjectStatus() {
	}

	/**
	 * @param status
	 */
	public BProjectStatus(final ProjectStatus status) {
		projectStatus = status;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BProjectStatus(final String key) throws ArahantException {
		internalLoad(key);
	}

	public ProjectStatus getBean() {
		return projectStatus;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		projectStatus = new ProjectStatus();
		projectStatus.generateId();
		return getProjectStatusId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(projectStatus);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public String getId() {
		return projectStatus.getProjectStatusId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(projectStatus);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		projectStatus = ArahantSession.getHSU().get(ProjectStatus.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(projectStatus);
	}

	/**
	 * @return @see com.arahant.beans.ProjectStatus#getCode()
	 */
	public String getCode() {
		return projectStatus.getCode();
	}

	/**
	 * @return @see com.arahant.beans.ProjectStatus#getDescription()
	 */
	public String getDescription() {
		return projectStatus.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.ProjectStatus#getProjectStatusId()
	 */
	public String getProjectStatusId() {
		return projectStatus.getProjectStatusId();
	}

	/**
	 * @param code
	 * @see com.arahant.beans.ProjectStatus#setCode(java.lang.String)
	 */
	public void setCode(String code) {
		if (code.length() > 20)
			code = code.substring(0, 20);

		projectStatus.setCode(code);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.ProjectStatus#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		projectStatus.setDescription(description);
	}

	public CompanyDetail getCompany() {
		return projectStatus.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		projectStatus.setCompany(company);
	}

	public int getLastActiveDate() {
		return projectStatus.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		projectStatus.setLastActiveDate(lastActiveDate);
	}

	/**
	 * @param projectStatusId
	 * @see com.arahant.beans.ProjectStatus#setProjectStatusId(java.lang.String)
	 */
	public void setProjectStatusId(final String projectStatusId) {
		projectStatus.setProjectStatusId(projectStatusId);
	}

	public static BProjectStatus[] search(final HibernateSessionUtil hsu, final String code, final String description, final int cap) {
		return search(hsu, code, description, new String[0], cap);
	}

	public static BProjectStatus[] search(final HibernateSessionUtil hsu, final String code, final String description, String[] exclude, final int cap) {
		return makeArray(hsu.createCriteria(ProjectStatus.class).geOrEq(ProjectStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0).setMaxResults(cap).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, code).like(ProjectStatus.DESCRIPTION, description).notIn(ProjectStatus.PROJECTSTATUSID, exclude).list());
	}

	public static BProjectStatus[] search(final HibernateSessionUtil hsu, final String code, final String description, String routeStopId, int statusType, final int cap) {
		HibernateCriteriaUtil<ProjectStatus> hcu = hsu.createCriteria(ProjectStatus.class).setMaxResults(cap).geOrEq(ProjectStatus.LAST_ACTIVE_DATE, DateUtils.now(), 0).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, code).like(ProjectStatus.DESCRIPTION, description);

		if (!isEmpty(routeStopId))
			hcu.joinTo(ProjectStatus.FROMROUTEPATHS).joinTo(RoutePath.FROM_ROUTE_STOP).eq(RouteStop.ROUTE_STOP_ID, routeStopId);
		List<ProjectStatus> l = hcu.list();
		return makeArray(l);
	}

	/**
	 * @param hsu
	 * @param projectStatusIds
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] projectStatusIds) throws ArahantException {
		for (final String element : projectStatusIds)
			new BProjectStatus(element).delete();
	}

	public static BProjectStatus[] list(final HibernateSessionUtil hsu) {
		return BProjectStatus.makeArray(hsu.createCriteria(ProjectStatus.class).orderBy(ProjectStatus.CODE).list());
	}

	/**
	 * @param active
	 */
	public void setActive(final int active) {
		switch (active) {
			case 0:
				projectStatus.setActive('N');
				break;
			case 1:
				projectStatus.setActive('Y');
				break;
			case 2:
				projectStatus.setActive('O');
				break;
		}
	}

	public void setActive(final char active) {
		projectStatus.setActive(active);
	}

	/**
	 * @return
	 */
	public int getActive() {
		return ((projectStatus.getActive() == 'Y') ? 1 : (projectStatus.getActive() == 'N') ? 0 : 2);
	}

	static public BProjectStatus[] makeArray(Collection<ProjectStatus> c) {
		int loop = 0;
		BProjectStatus ret[] = new BProjectStatus[c.size()];

		for (ProjectStatus s : c)
			ret[loop++] = new BProjectStatus(s);
		return ret;
	}

	public static BProjectStatus[] search(String code, String[] excludeStatusIds, int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProjectStatus.class).setMaxResults(max).orderBy(ProjectStatus.CODE).like(ProjectStatus.CODE, code).notIn(ProjectStatus.PROJECTSTATUSID, excludeStatusIds).list());
	}

	public static String findOrMake(String code) {
		ProjectStatus ps = ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, code).first();

		if (ps != null)
			return ps.getProjectStatusId();

		BProjectStatus bps = new BProjectStatus();
		bps.create();
		bps.setCode(code);
		bps.setActive('Y');
		bps.setDescription(code);
		bps.insert();

		return bps.getProjectStatusId();
	}

	public static String findOrMakeInactive(String code) {
		ProjectStatus ps = ArahantSession.getHSU().createCriteria(ProjectStatus.class).eq(ProjectStatus.CODE, code).eq(ProjectStatus.ACTIVE, 'N').first();

		if (ps != null)
			return ps.getProjectStatusId();

		BProjectStatus bps = new BProjectStatus();
		bps.create();
		bps.setCode(code);
		bps.setActive('N');
		bps.setDescription(code);
		bps.insert();

		return bps.getProjectStatusId();
	}
}
