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

import com.arahant.beans.Person;
import com.arahant.beans.ProjectTemplateBenefit;
import com.arahant.beans.ProjectTemplateBenefitAssignment;
import com.arahant.business.interfaces.IDBFunctions;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BProjectTemplateBenefitAssignment extends SimpleBusinessObjectBase<ProjectTemplateBenefitAssignment> implements IDBFunctions {

	/**
	 */
	public BProjectTemplateBenefitAssignment() {
	}

	/**
	 * @param projectTypeAssignId
	 * @throws ArahantException
	 */
	public BProjectTemplateBenefitAssignment(final String p) throws ArahantException {
		internalLoad(p);
	}

	/**
	 * @param ProjectTemplateBCRAssignment
	 */
	public BProjectTemplateBenefitAssignment(final ProjectTemplateBenefitAssignment pAssign) {
		bean = pAssign;
	}

	public String getId() {
		return bean.getId();
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getProjectTemplateBenefitId() {
		return bean.getProjectTemplateBenefitId();
	}

	public void setProjectTemplateBenefit(ProjectTemplateBenefit p) {
		bean.setProjectTemplateBenefit(p);
	}

	public ProjectTemplateBenefit getProjectTemplateBenefit() {
		return bean.getProjectTemplateBenefit();
	}

	@Override
	public void update() throws ArahantException {
		ArahantSession.getHSU().saveOrUpdate(bean);
	}

	@Override
	public void insert() throws ArahantException {
		ArahantSession.getHSU().insert(bean);
	}

	@Override
	public void delete() throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(String[] ids) throws ArahantDeleteException, ArahantSecurityException, ArahantJessException {
		for (String id : ids)
			new BProjectTemplateBenefitAssignment(id).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProjectTemplateBenefitAssignment();
		bean.generateId();
		return getId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProjectTemplateBenefitAssignment.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param name
	 * @return
	 * @throws ArahantException
	 */
	public static BProjectTemplateBenefitAssignment[] makeArray(final List<ProjectTemplateBenefitAssignment> l) throws ArahantException {

		final BProjectTemplateBenefitAssignment[] ret = new BProjectTemplateBenefitAssignment[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProjectTemplateBenefitAssignment(l.get(loop));

		return ret;
	}
}
