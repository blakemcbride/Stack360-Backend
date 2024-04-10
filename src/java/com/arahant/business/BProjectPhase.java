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
import com.arahant.beans.ProjectPhase;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class BProjectPhase extends SimpleBusinessObjectBase<ProjectPhase> {

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProjectPhase(id).delete();
	}

	public static String[] getSecurityCategoryList() {
		return ProjectPhase.SECURITY_CATEGORIES;
	}

	public BProjectPhase() {
	}

	/**
	 * @param id
	 * @throws ArahantException
	 */
	public BProjectPhase(final String id) throws ArahantException {
		internalLoad(id);
	}

	/**
	 * @param form
	 */
	public BProjectPhase(final ProjectPhase projectPhase) {
		bean = projectPhase;
	}

	/*
	 * (non-Javadoc) @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new ProjectPhase();
		return bean.generateId();
	}

	public int getCategoryId() {
		return bean.getSecurityLevel() + 1;
	}

	public String getCategoryName() {
		try {
			if (bean.getSecurityLevel() > ProjectPhase.SECURITY_CATEGORIES.length - 1)
				return "INVALID";

			return ProjectPhase.SECURITY_CATEGORIES[bean.getSecurityLevel()];
		} catch (Exception e) {
			return "INVALID";
		}
	}

	public String getCode() {
		return bean.getCode();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectPhase.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param name
	 * @return
	 */
	static BProjectPhase[] makeArray(final List<ProjectPhase> l) {

		final BProjectPhase[] ret = new BProjectPhase[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectPhase(l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public String getId() {

		return bean.getProjectPhaseId();
	}

	public static BProjectPhase[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(ProjectPhase.class).orderBy(ProjectPhase.CODE).list());
	}

	public static BProjectPhase[] search(String code, String description, final int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(ProjectPhase.class).geOrEq(ProjectPhase.LAST_ACTIVE_DATE, DateUtils.now(), 0).like(ProjectPhase.CODE, code).like(ProjectPhase.DESCRIPTION, description).orderBy(ProjectPhase.CODE).setMaxResults(max).list());
	}

	public void setCategoryId(int categoryId) {
		bean.setSecurityLevel((short) (categoryId - 1));
	}

	public void setCode(String code) {
		bean.setCode(code);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setId(String id) {
		bean.setProjectPhaseId(id);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail company) {
		bean.setCompany(company);
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}
}
