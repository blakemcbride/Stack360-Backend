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
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class BProjectType extends BusinessLogicBase implements IDBFunctions {

	private static final transient ArahantLogger logger = new ArahantLogger(BProjectType.class);
	private ProjectType projectType;

	public BProjectType() {
	}

	/**
	 * @param type
	 */
	public BProjectType(final ProjectType type) {
		projectType = type;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BProjectType(final String key) throws ArahantException {
		internalLoad(key);
	}

	@Override
	public String create() throws ArahantException {
		projectType = new ProjectType();
		projectType.generateId();
		return getProjectTypeId();
	}

	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(projectType);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public String getRoutedEmployee(String personId) {

		String categoryId = new AIProperty("CalculateDefaultCategory", projectType.getProjectTypeId(), personId).getValue();

		if (isEmpty(categoryId))
			categoryId = BProperty.get(StandardProperty.DefaultProjectCategoryId);
		if (isEmpty(categoryId))
			throw new ArahantException("Please set up system property DefaultProjectCategoryId");

		BRouteTypeAssoc rta = new BRouteTypeAssoc(categoryId, projectType.getProjectTypeId());

		if (!isEmpty(rta.getRouteId())) {
			BRoute rt = new BRoute(rta.getRouteId());
			if (rt.hasInitialRouteStop() && rt.hasInitialStatus() && rt.getRouteStop().getAutoAssignToSupervisors()) {
				BOrgGroup borg = new BOrgGroup(rt.getRouteStop().getOrgGroup());

				return borg.getFirstSupervisorId();
			}
		} else {
			BProjectCategory cat = new BProjectCategory(categoryId);

			throw new ArahantWarning("There was no route found for Category: " + cat.getCode() + " Type: " + projectType.getCode());
		}

		return "";
	}

	public String getScope() {
		return projectType.getScope() + "";
	}

	public String getScopeFormatted() {
		switch (projectType.getScope()) {
			case 'G':
				return "Global";
			case 'I':
				return "Internal";
			default:
				return "Unknown";
		}
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(projectType);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		projectType = ArahantSession.getHSU().get(ProjectType.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setScope(char scope) {
		projectType.setScope(scope);
	}

	public void setScope(String scope) {
		if (!isEmpty(scope))
			projectType.setScope(scope.charAt(0));
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(projectType);
	}

	/**
	 * @return @see com.arahant.beans.ProjectType#getCode()
	 */
	public String getCode() {
		return projectType.getCode();
	}

	/**
	 * @return @see com.arahant.beans.ProjectType#getDescription()
	 */
	public String getDescription() {
		return projectType.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.ProjectType#getProjectTypeId()
	 */
	public String getProjectTypeId() {
		return projectType.getProjectTypeId();
	}

	/**
	 * @param code
	 * @see com.arahant.beans.ProjectType#setCode(java.lang.String)
	 */
	public void setCode(String code) {
		if (code.length() > 20)
			code = code.substring(0, 20);
		projectType.setCode(code);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.ProjectType#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		projectType.setDescription(description);
	}

	public CompanyDetail getCompany() {
		return projectType.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		projectType.setCompany(company);
	}

	public int getLastActiveDate() {
		return projectType.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		projectType.setLastActiveDate(lastActiveDate);
	}

	/**
	 * @param projectTypeId
	 * @see com.arahant.beans.ProjectType#setProjectTypeId(java.lang.String)
	 */
	public void setProjectTypeId(final String projectTypeId) {
		projectType.setProjectTypeId(projectTypeId);
	}

	;

	public static BProjectType[] search(final boolean showInternal, final String code, final String description, final int max) {
		return search(showInternal, code, description, null, max);
	}

	public static BProjectType[] search(final HibernateSessionUtil hsu, final String code, final String description, String[] exclude, final int max) {

		final HibernateCriteriaUtil hcu = hsu.createCriteria(ProjectType.class).geOrEq(ProjectCategory.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		hcu.setMaxResults(max);
		hcu.orderBy(ProjectType.CODE);

		if (!isEmpty(code))
			hcu.like(ProjectType.CODE, code);
		if (!isEmpty(description))
			hcu.like(ProjectType.DESCRIPTION, description);

		hcu.notIn(ProjectType.PROJECTTYPEID, exclude);

		final List pcl = hcu.list();

		final BProjectType[] pct = new BProjectType[pcl.size()];

		for (int loop = 0; loop < pct.length; loop++)
			pct[loop] = new BProjectType((ProjectType) pcl.get(loop));
		return pct;
	}

	public static BProjectType[] search(final boolean showInternal, final String code, final String description, final String[] exclude, final int max) {

		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(ProjectType.class).geOrEq(ProjectType.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		hcu.setMaxResults(max);
		hcu.orderBy(ProjectType.CODE);

		if (!isEmpty(code))
			hcu.like(ProjectType.CODE, code);
		if (!isEmpty(description))
			hcu.like(ProjectType.DESCRIPTION, description);

		if (!showInternal)
			hcu.eq(ProjectType.SCOPE, 'G');

		hcu.notIn(ProjectType.PROJECTTYPEID, exclude);

		final List pcl = hcu.list();

		final BProjectType[] pct = new BProjectType[pcl.size()];

		for (int loop = 0; loop < pct.length; loop++)
			pct[loop] = new BProjectType((ProjectType) pcl.get(loop));
		return pct;
	}

	public static BProjectType[] search(final HibernateSessionUtil hsu, final String code, final String description, final int max) {
		return search(hsu, code, description, new String[0], max);
	}

	/**
	 * @param hsu
	 * @param projectTypeIds
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] projectTypeIds) throws ArahantException {
		for (final String element : projectTypeIds)
			new BProjectType(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BProjectType[] list(final HibernateSessionUtil hsu) {
		//Check for restriction

		String restricted = AIProperty.getValue("RestrictedProjectType");

		HibernateCriteriaUtil<ProjectType> hcu = hsu.createCriteria(ProjectType.class).orderBy(ProjectType.CODE);

		if (!isEmpty(restricted)) {
			StringTokenizer stok = new StringTokenizer(restricted, ",");
			List<String> ids = new LinkedList<String>();
			while (stok.hasMoreTokens())
				ids.add(stok.nextToken());

			hcu.in(ProjectType.PROJECTTYPEID, ids);
		}

		final List ptl = hcu.list();

		final BProjectType[] ptt = new BProjectType[ptl.size()];

		final Iterator ptlItr = ptl.iterator();

		for (int loop = 0; loop < ptt.length; loop++)
			ptt[loop] = new BProjectType((ProjectType) ptlItr.next());
		return ptt;

	}

	public static BProjectType get(String typeId) {
		ProjectType pt = ArahantSession.getHSU().get(ProjectType.class, typeId);
		if (pt == null)
			return null;
		return new BProjectType(pt);
	}

	public ProjectType getBean() {
		return projectType;
	}

	public static String findOrMake(String code) {
		ProjectType pt = ArahantSession.getHSU().createCriteria(ProjectType.class).eq(ProjectType.CODE, code).first();

		if (pt != null)
			return pt.getProjectTypeId();

		BProjectType bpt = new BProjectType();
		bpt.create();
		bpt.setCode(code);
		bpt.setDescription(code);
		bpt.setScope(ProjectType.SCOPE_INTERNAL);
		bpt.insert();

		return bpt.getProjectTypeId();
	}

	static BProjectType[] makeArray(List<ProjectType> l) {
		final BProjectType[] ret = new BProjectType[l.size()];

		final Iterator<ProjectType> itr = l.iterator();

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectType(itr.next());

		return ret;
	}
}
