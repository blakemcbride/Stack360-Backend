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

import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.WizardConfigurationBenefit;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class BWizardConfigurationConfig extends SimpleBusinessObjectBase<WizardConfigurationConfig> {

	public BWizardConfigurationConfig() {
	}

	public BWizardConfigurationConfig(final WizardConfigurationConfig bean) {
		this.bean = bean;
	}

	public BWizardConfigurationConfig(final String key) throws ArahantException {
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

	public static void delete(final String[] wizardConfigurationConfigIds) throws ArahantException {
		if (wizardConfigurationConfigIds.length > 0) {
			BWizardConfigurationBenefit wizBen = new BWizardConfigurationBenefit();
			for (final String element : wizardConfigurationConfigIds) {
				wizBen = new BWizardConfigurationBenefit(new BWizardConfigurationConfig(element).getWizardConfigurationBenefit());
				new BWizardConfigurationConfig(element).delete();
			}
			ArahantSession.getHSU().flush();
			int tempCount = 1000;
			for (WizardConfigurationConfig conf : wizBen.getWizardConfigs())
				conf.setSeqNo(tempCount++);
			ArahantSession.getHSU().flush();
			int count = 0;
			for (WizardConfigurationConfig conf : wizBen.getWizardConfigs())
				conf.setSeqNo(count++);
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardConfigurationConfig();
		bean.generateId();
		return getWizardConfigurationConfigId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(WizardConfigurationConfig.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public String getName() {
		return bean.getName();
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public HrBenefitConfig getBenefitConfig() {
		return bean.getBenefitConfig();
	}

	public void setBenefitConfig(HrBenefitConfig benefitConfig) {
		bean.setBenefitConfig(benefitConfig);
	}

	public int getSeqNo() {
		return bean.getSeqNo();
	}

	public void setSeqNo(int seqNo) {
		bean.setSeqNo(seqNo);
	}

	public String getWizardConfigurationConfigId() {
		return bean.getWizardConfigurationConfigId();
	}

	public void setWizardConfigurationConfigId(String wizardConfigurationConfigId) {
		bean.setWizardConfigurationConfigId(wizardConfigurationConfigId);
	}

	public WizardConfigurationBenefit getWizardConfigurationBenefit() {
		return bean.getWizardConfigurationBenefit();
	}

	public void setWizardConfigurationBenefit(WizardConfigurationBenefit wizardConfigurationBenefit) {
		bean.setWizardConfigurationBenefit(wizardConfigurationBenefit);
	}

	public static BWizardConfigurationConfig[] list(final int max, final String benefitWizId) {
		return makeArray(ArahantSession.getHSU().createCriteria(WizardConfigurationConfig.class).setMaxResults(max).orderBy(WizardConfigurationConfig.SEQNO).eq(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT, new BWizardConfigurationBenefit(benefitWizId).getBean()).list());
	}

	public static BWizardConfigurationConfig[] makeArray(List<WizardConfigurationConfig> l) {
		final BWizardConfigurationConfig[] ret = new BWizardConfigurationConfig[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardConfigurationConfig(l.get(loop));

		return ret;
	}
}
