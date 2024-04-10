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
import com.arahant.beans.ProjectCategory;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.Iterator;
import java.util.List;

public class BProjectCategory extends BusinessLogicBase implements IDBFunctions {

	/**
	 */
	public BProjectCategory() {
	}

	/**
	 * @param category
	 */
	public BProjectCategory(final ProjectCategory category) {
		projectCategory = category;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BProjectCategory(final String key) throws ArahantException {
		internalLoad(key);
	}
	
	private static final transient ArahantLogger logger = new ArahantLogger(BProjectCategory.class);
	ProjectCategory projectCategory;

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		projectCategory = new ProjectCategory();
		projectCategory.generateId();

		return getProjectCategoryId();
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#delete()
	 */
	@Override
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(projectCategory);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public ProjectCategory getBean() {
		return projectCategory;
	}

	public String getScope() {
		return projectCategory.getScope() + "";
	}

	public String getScopeFormatted() {
		switch (projectCategory.getScope()) {
			case 'G':
				return "Global";
			case 'I':
				return "Internal";
			default:
				return "Unknown";
		}
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#insert()
	 */
	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(projectCategory);
	}

	private void internalLoad(final String key) throws ArahantException {
		logger.debug("Loading " + key);
		projectCategory = ArahantSession.getHSU().get(ProjectCategory.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public void setScope(char scope) {
		projectCategory.setScope(scope);
	}

	public void setScope(String scope) {
		if (!isEmpty(scope))
			projectCategory.setScope(scope.charAt(0));
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#update()
	 */
	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(projectCategory);
	}

	/**
	 * @return @see com.arahant.beans.ProjectCategory#getCode()
	 */
	public String getCode() {
		return projectCategory.getCode();
	}

	/**
	 * @return @see com.arahant.beans.ProjectCategory#getDescription()
	 */
	public String getDescription() {
		return projectCategory.getDescription();
	}

	/**
	 * @return @see com.arahant.beans.ProjectCategory#getProjectCategoryId()
	 */
	public String getProjectCategoryId() {
		return projectCategory.getProjectCategoryId();
	}

	/**
	 * @param code
	 * @see com.arahant.beans.ProjectCategory#setCode(java.lang.String)
	 */
	public void setCode(String code) {
		if (code.length() > 20)
			code = code.substring(0, 20);
		projectCategory.setCode(code);
	}

	/**
	 * @param description
	 * @see com.arahant.beans.ProjectCategory#setDescription(java.lang.String)
	 */
	public void setDescription(final String description) {
		projectCategory.setDescription(description);
	}

	public CompanyDetail getCompany() {
		return projectCategory.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		projectCategory.setCompany(company);
	}

	public int getLastActiveDate() {
		return projectCategory.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		projectCategory.setLastActiveDate(lastActiveDate);
	}


	/*
	 * public String getOrgGroupId() { return projectCategory.getOrgGroupId(); }
	 *
	 * public void setOrgGroupId(String orgGroupId) {
	 * projectCategory.getOrgGroupId();
	}
	 */
	/**
	 * @param projectCategoryId
	 * @see
	 * com.arahant.beans.ProjectCategory#setProjectCategoryId(java.lang.String)
	 */
	public void setProjectCategoryId(final String projectCategoryId) {
		projectCategory.setProjectCategoryId(projectCategoryId);
	}

	public static BProjectCategory[] search(final boolean showInternal, final String code, final String description, final int max) {
		return search(showInternal, code, description, null, max);
	}

	public static BProjectCategory[] search(final boolean showInternal, final String code, final String description, String[] exclude, final int max) {

		final HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(ProjectCategory.class).geOrEq(ProjectCategory.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		hcu.setMaxResults(max);
		hcu.orderBy(ProjectCategory.CODE);

		if (!isEmpty(code))
			hcu.like(ProjectCategory.CODE, code);
		if (!isEmpty(description))
			hcu.like(ProjectCategory.DESCRIPTION, description);

		if (!showInternal)
			hcu.eq(ProjectCategory.SCOPE, 'G');

		hcu.notIn(ProjectCategory.PROJECTCATEGORYID, exclude);

		final List pcl = hcu.list();

		final BProjectCategory[] pct = new BProjectCategory[pcl.size()];

		for (int loop = 0; loop < pct.length; loop++)
			pct[loop] = new BProjectCategory((ProjectCategory) pcl.get(loop));
		return pct;
	}

	public static BProjectCategory[] search(final HibernateSessionUtil hsu, final String code, final String description, String[] exclude, final int max) {

		final HibernateCriteriaUtil hcu = hsu.createCriteria(ProjectCategory.class).geOrEq(ProjectCategory.LAST_ACTIVE_DATE, DateUtils.now(), 0);

		hcu.setMaxResults(max);
		hcu.orderBy(ProjectCategory.CODE);

		if (!isEmpty(code))
			hcu.like(ProjectCategory.CODE, code);
		if (!isEmpty(description))
			hcu.like(ProjectCategory.DESCRIPTION, description);

		if (exclude.length > 0)
			hcu.notIn(ProjectCategory.PROJECTCATEGORYID, exclude);

		final List pcl = hcu.list();

		final BProjectCategory[] pct = new BProjectCategory[pcl.size()];

		for (int loop = 0; loop < pct.length; loop++)
			pct[loop] = new BProjectCategory((ProjectCategory) pcl.get(loop));
		return pct;
	}

	public static BProjectCategory[] search(final HibernateSessionUtil hsu, final String code, final String description, final int max) {
		return search(hsu, code, description, new String[0], max);
	}

	/**
	 * @param hsu
	 * @param projectCategoryIds
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] projectCategoryIds) throws ArahantException {
		for (final String element : projectCategoryIds)
			new BProjectCategory(element).delete();
	}

	public static BProjectCategory[] list(final HibernateSessionUtil hsu) {
		final List ptl = hsu.createCriteria(ProjectCategory.class).orderBy(ProjectCategory.CODE).list();

		final BProjectCategory[] ptt = new BProjectCategory[ptl.size()];

		final Iterator ptlItr = ptl.iterator();

		for (int loop = 0; loop < ptt.length; loop++)
			ptt[loop] = new BProjectCategory((ProjectCategory) ptlItr.next());

		return ptt;

	}

	public static BProjectCategory get(String id) {
		ProjectCategory pc = ArahantSession.getHSU().get(ProjectCategory.class, id);
		if (pc == null)
			return null;
		return new BProjectCategory(pc);
	}

	public static String findOrMake(String code) {
		ProjectCategory pc = ArahantSession.getHSU().createCriteria(ProjectCategory.class).eq(ProjectCategory.CODE, code).first();

		if (pc != null)
			return pc.getProjectCategoryId();

		BProjectCategory bpc = new BProjectCategory();
		bpc.create();
		bpc.setCode(code);
		bpc.setDescription(code);
		bpc.setScope(ProjectCategory.SCOPE_INTERNAL);
		bpc.insert();

		return bpc.getProjectCategoryId();
	}
}
