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

import com.arahant.beans.BenefitClass;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.WizardConfiguration;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.Collection;
import java.util.List;

public class BBenefitClass extends SimpleBusinessObjectBase<BenefitClass> {

	public BBenefitClass() {
	}

	public BBenefitClass(String key) {
		super(key);
	}

	public BBenefitClass(BenefitClass o) {
		bean = o;
	}

	static BBenefitClass[] makeArray(Collection<BenefitClass> l) {
		BBenefitClass[] ret = new BBenefitClass[l.size()];
		int loop = 0;
		for (BenefitClass bc : l)
			ret[loop++] = new BBenefitClass(bc);
		return ret;
	}

	public static void delete(String[] ids) {
		for (String id : ids)
			new BBenefitClass(id).delete();
	}

	/*
	 * Do not use until Java 6 @Override
	 */
	@Override
	public String create() throws ArahantException {
		bean = new BenefitClass();
		bean.setOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		return bean.generateId();
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	public String getId() {
		return bean.getBenefitClassId();
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	public String getName() {
		return bean.getName();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitClass.class, key);
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public static BBenefitClass[] list(String excludeIds[]) {
		return makeArray(ArahantSession.getHSU().createCriteria(BenefitClass.class).notIn(BenefitClass.BENEFIT_CLASS_ID, excludeIds).list());
	}

	public static BBenefitClass[] list() {
		return makeArray(ArahantSession.getHSU().createCriteria(BenefitClass.class).list());
	}

	public static BBenefitClass[] list(int activeType) {
		HibernateCriteriaUtil<BenefitClass> hcu = ArahantSession.getHSU().createCriteria(BenefitClass.class);

		if (activeType == 1) //active
		
			hcu.dateInside(BenefitClass.FIRSTACTIVEDATE, BenefitClass.LASTACTIVEDATE, DateUtils.now());

		if (activeType == 2) //inactive
		
			hcu.dateOutside(BenefitClass.FIRSTACTIVEDATE, BenefitClass.LASTACTIVEDATE, DateUtils.now());

		return makeArray(hcu.list());
	}

	public static BBenefitClass[] listAvailableByWizardType(char wizardType, int max) {
		List<BenefitClass> bcl = ArahantSession.getHSU().createCriteria(BenefitClass.class).list();

		List<WizardConfiguration> wcl = ArahantSession.getHSU().createCriteria(WizardConfiguration.class).eqOrNull(WizardConfiguration.COMPANY_ID, ArahantSession.getHSU().getCurrentCompany().getCompanyId()).isNotNull(WizardConfiguration.BENEFIT_CLASS).eq(WizardConfiguration.WIZARD_TYPE, wizardType).list();
		for (WizardConfiguration wc : wcl)
			bcl.remove(wc.getBenefitClass());

		return makeArray(bcl);
	}

	public static BBenefitClass[] listActive() {
		return makeArray(ArahantSession.getHSU().createCriteria(BenefitClass.class).dateInside(BenefitClass.FIRSTACTIVEDATE, BenefitClass.LASTACTIVEDATE, DateUtils.now()).list());
	}

	public void associateBenefit(String benefitId) {
		bean.getBenefits().add(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
	}

	public void disassociateBenefit(String benefitId) {
		bean.getBenefits().remove(ArahantSession.getHSU().get(HrBenefit.class, benefitId));
	}

	public void associateConfig(String configId) {
		bean.getConfigs().add(ArahantSession.getHSU().get(HrBenefitConfig.class, configId));
	}

	public void disassociateConfig(String configId) {
		bean.getConfigs().remove(ArahantSession.getHSU().get(HrBenefitConfig.class, configId));
	}
}
