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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BProphetLoginOverride extends SimpleBusinessObjectBase<ProphetLoginOverride> {

	public BProphetLoginOverride() {
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BProphetLoginOverride(id).delete();
	}

	/**
	 * @param override
	 */
	public BProphetLoginOverride(final ProphetLoginOverride override) {
		bean = override;
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BProphetLoginOverride(final String string) throws ArahantException {
		super(string);
	}

	@Override
	public String create() throws ArahantException {
		bean = new ProphetLoginOverride();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ProphetLoginOverride.class, key);
	}

	public String getLoginExceptionId() {
		return bean.getLoginExceptionId();
	}

	public void setLoginExceptionId(String loginExceptionId) {
		bean.setLoginExceptionId(loginExceptionId);
	}

	public void setPersonId(String personId) {
		bean.setPerson(ArahantSession.getHSU().get(Person.class, personId));
	}

	public String getPersonId() {
		return bean.getPerson().getPersonId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompany(ArahantSession.getHSU().get(CompanyDetail.class, companyId));
	}

	public String getCompanyId() {
		return bean.getCompany().getOrgGroupId();
	}

	public void setScreenGroupId(String screenGroupId) {
		bean.setScreenGroup(ArahantSession.getHSU().get(ScreenGroup.class, screenGroupId));
	}

	public String getScreenGroupId() {
		return bean.getScreenGroup().getScreenGroupId();
	}

	public void setSecurityGroupId(String securityGroupId) {
		bean.setSecurityGroup(ArahantSession.getHSU().get(SecurityGroup.class, securityGroupId));
	}

	public String getSecurityGroupId() {
		return bean.getSecurityGroup().getSecurityGroupId();
	}

	public static BProphetLoginOverride[] searchCompaniesForEmployee(String employeeId, int max) {
		HibernateCriteriaUtil<ProphetLoginOverride> hcu = ArahantSession.getHSU().createCriteria(ProphetLoginOverride.class).orderBy(ProphetLoginOverride.COMPANY).eq(ProphetLoginOverride.PERSON, new BPerson(employeeId).getPerson());

		return makeArray(hcu.list());
	}

	private static BProphetLoginOverride[] makeArray(List<ProphetLoginOverride> list) {
		final BProphetLoginOverride[] ret = new BProphetLoginOverride[list.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BProphetLoginOverride(list.get(loop));
		return ret;

	}
}
