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
import com.arahant.beans.OnboardingConfig;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantJessException;
import com.arahant.exceptions.ArahantSecurityException;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;

public class BOnboardingConfig extends SimpleBusinessObjectBase<OnboardingConfig> {

	public BOnboardingConfig() {
	}

	public BOnboardingConfig(final OnboardingConfig o) {
		bean = o;
	}

	public BOnboardingConfig(final String key) throws ArahantException {
		internalLoad(key);
	}

	public String getOnboardingConfigId() {
		return bean.getOnboardingConfigId();
	}

	public String getOnboardingConfigName() {
		return bean.getConfigName();
	}

	public void setOnboardingConfigName(String name) {
		bean.setConfigName(name);
	}

	public CompanyDetail getCompany() {
		return bean.getCompany();
	}

	public void setCompany(CompanyDetail c) {
		bean.setCompany(c);
	}

	@Override
	public String create() throws ArahantException {
		bean = new OnboardingConfig();
		bean.generateId();
		return getOnboardingConfigId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(OnboardingConfig.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
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
			throw new ArahantDeleteException("Unable to delete current Onboarding Config.");
		}
	}

	public static void delete(final String[] onboardingConfigIds) throws ArahantException {
		for (final String element : onboardingConfigIds)
			new BOnboardingConfig(element).delete();
	}

	public static List<OnboardingConfig> list(int cap) {
		@SuppressWarnings("unchecked")
		List<OnboardingConfig> ret = new ArrayList();

		ret.addAll(ArahantSession.getHSU().createCriteria(OnboardingConfig.class).orderBy(OnboardingConfig.CONFIG_NAME).isNull(OnboardingConfig.COMPANY).list());

		ret.addAll(ArahantSession.getHSU().createCriteria(OnboardingConfig.class).orderBy(OnboardingConfig.CONFIG_NAME).eq(OnboardingConfig.COMPANY, ArahantSession.getHSU().getCurrentCompany()).list());

		return ret;
	}

	public static BOnboardingConfig[] makeArray(List<OnboardingConfig> l) {
		BOnboardingConfig[] ret = new BOnboardingConfig[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BOnboardingConfig(l.get(loop));
		return ret;
	}
}
