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
import com.arahant.beans.WizardConfiguration;
import com.arahant.beans.WizardConfigurationProjectAssignment;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BWizardConfigurationProjectAssignment extends SimpleBusinessObjectBase<WizardConfigurationProjectAssignment> {

	public BWizardConfigurationProjectAssignment() {
	}

	public BWizardConfigurationProjectAssignment(final WizardConfigurationProjectAssignment bean) {
		this.bean = bean;
	}

	public BWizardConfigurationProjectAssignment(final String key) throws ArahantException {
		internalLoad(key);
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
	public void delete() throws ArahantDeleteException {
		try {
			ArahantSession.getHSU().delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardConfigurationProjectAssignment();
		bean.generateId();
		return getWizardConfigProjectAssId();
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(WizardConfigurationProjectAssignment.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public Person getPerson() {
		return bean.getPerson();
	}

	public void setPerson(Person person) {
		bean.setPerson(person);
	}

	public String getPersonId() {
		return bean.getPersonId();
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public WizardConfiguration getWizardConfig() {
		return bean.getWizardConfig();
	}

	public void setWizardConfig(WizardConfiguration wizardConfig) {
		bean.setWizardConfig(wizardConfig);
	}

	public String getWizardConfigid() {
		return bean.getWizardConfigid();
	}

	public void setWizardConfigid(String wizardConfigid) {
		bean.setWizardConfig(ArahantSession.getHSU().get(WizardConfiguration.class, wizardConfigid));
	}

	public String getWizardConfigProjectAssId() {
		return bean.getWizardConfigProjectAssId();
	}

	public void setWizardConfigProjectAssId(String wizardConfigProjectAssId) {
		bean.setWizardConfigProjectAssId(wizardConfigProjectAssId);
	}

	public static BWizardConfigurationProjectAssignment[] list(int max) {
		return makeArray(ArahantSession.getHSU().createCriteria(WizardConfigurationProjectAssignment.class).list());
	}

	private static BWizardConfigurationProjectAssignment[] makeArray(List<WizardConfigurationProjectAssignment> l) {
		final BWizardConfigurationProjectAssignment[] ret = new BWizardConfigurationProjectAssignment[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardConfigurationProjectAssignment(l.get(loop));

		return ret;
	}
}
