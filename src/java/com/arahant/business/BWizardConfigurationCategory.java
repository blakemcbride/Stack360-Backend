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

import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.Screen;
import com.arahant.beans.WizardConfiguration;
import com.arahant.beans.WizardConfigurationBenefit;
import com.arahant.beans.WizardConfigurationCategory;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;


public class BWizardConfigurationCategory extends SimpleBusinessObjectBase<WizardConfigurationCategory> {

	public BWizardConfigurationCategory() {
	}

	public BWizardConfigurationCategory(final WizardConfigurationCategory bean) {
		this.bean = bean;
	}

	public BWizardConfigurationCategory(final String key) throws ArahantException {
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
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			for (WizardConfigurationBenefit bene : hsu.createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY, this.getBean()).list()) {
				hsu.createCriteria(WizardConfigurationConfig.class).eq(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT, bene).delete();
				hsu.flush();
				hsu.delete(bene);
			}

			hsu.flush();
			hsu.delete(bean);
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	public static void delete(final String[] wizardConfigurationCategoryIds) throws ArahantException {
		for (final String element : wizardConfigurationCategoryIds)
			new BWizardConfigurationCategory(element).delete();
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardConfigurationCategory();
		bean.generateId();
		return getWizardConfigurationCategoryId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(WizardConfigurationCategory.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public int getAvatarLocation() {
		return bean.getAvatarLocation();
	}

	public void setAvatarLocation(int avatarLocation) {
		bean.setAvatarLocation(avatarLocation);
	}

	public String getAvatarPath() {
		return bean.getAvatarPath();
	}

	public void setAvatarPath(String avatarPath) {
		bean.setAvatarPath(avatarPath);
	}

	public HrBenefitCategory getBenefitCategory() {
		return bean.getBenefitCategory();
	}

	public void setBenefitCategory(HrBenefitCategory benefitCategory) {
		bean.setBenefitCategory(benefitCategory);
	}

	public String getInstructions() {
		return bean.getInstructions();
	}

	public void setInstructions(String instructions) {
		bean.setInstructions(instructions);
	}

	public Screen getScreen() {
		return bean.getScreen();
	}

	public void setScreen(Screen screen) {
		bean.setScreen(screen);
	}

	public int getSeqNo() {
		return bean.getSeqNo();
	}

	public void setSeqNo(int seqNo) {
		bean.setSeqNo(seqNo);
	}

	public String getWizardConfigurationCategoryId() {
		return bean.getWizardConfigurationCategoryId();
	}

	public void setWizardConfigurationCategoryId(String wizardConfigurationCategoryId) {
		bean.setWizardConfigurationCategoryId(wizardConfigurationCategoryId);
	}

	public WizardConfiguration getWizardConfiguration() {
		return bean.getWizardConfiguration();
	}

	public void setWizardConfiguration(WizardConfiguration wizardConfiguration) {
		bean.setWizardConfiguration(wizardConfiguration);
	}

	public static BWizardConfigurationCategory[] list(final int max, final String wizardConfigurationId) {
		return makeArray(ArahantSession.getHSU().createCriteria(WizardConfigurationCategory.class).setMaxResults(max).orderBy(WizardConfigurationCategory.SEQNO).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(wizardConfigurationId).getBean()).setMaxResults(max).list());
	}

	private static BWizardConfigurationCategory[] makeArray(List<WizardConfigurationCategory> l) {
		final BWizardConfigurationCategory[] ret = new BWizardConfigurationCategory[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardConfigurationCategory(l.get(loop));

		return ret;
	}

	public List<WizardConfigurationBenefit> getWizardBenefits() {
		return ArahantSession.getHSU().createCriteria(WizardConfigurationBenefit.class).orderBy(WizardConfigurationBenefit.SEQNO).eq(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY, this.getBean()).list();
	}

	public BScreenOrGroup getWizardScreen() {
		if (getScreen() != null)
			return new BScreen(getScreen());
		else
			if (getWizardConfiguration().getWizardVersion().equals("2"))
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/category/BenefitWizardCategoryScreen.swf");
			else if (getWizardConfiguration().getWizardVersion().equals("3"))
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleCategory/SimpleCategoryScreen.swf");
			else
				throw new ArahantWarning("Wizard Configuration version not specified.");
	}
	
	public static int nextSeq(String wizardConfigurationId) {
		List<WizardConfigurationCategory> wcs = ArahantSession.getHSU().createCriteria(WizardConfigurationCategory.class).setMaxResults(1).orderByDesc(WizardConfigurationCategory.SEQNO).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(wizardConfigurationId).getBean()).setMaxResults(1).list();
		if (wcs.isEmpty())
			return 0;
		else
			return wcs.get(0).getSeqNo() + 1;
	}
}
