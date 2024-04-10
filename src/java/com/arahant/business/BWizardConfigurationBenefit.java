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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.Screen;
import com.arahant.beans.WizardConfigurationBenefit;
import com.arahant.beans.WizardConfigurationCategory;
import com.arahant.beans.WizardConfigurationConfig;
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;


public class BWizardConfigurationBenefit extends SimpleBusinessObjectBase<WizardConfigurationBenefit> {

	public BWizardConfigurationBenefit() {
	}

	public BWizardConfigurationBenefit(final WizardConfigurationBenefit bean) {
		this.bean = bean;
	}

	public BWizardConfigurationBenefit(final String key) throws ArahantException {
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

	public static void delete(final String[] wizardBenefitIds) throws ArahantException {
		BWizardConfigurationCategory wizCat;
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		for (final String element : wizardBenefitIds) {

			BWizardConfigurationBenefit bene = new BWizardConfigurationBenefit(element);
			wizCat = new BWizardConfigurationCategory(bene.getWizardConfigurationCategory());
			hsu.delete(bene.getWizardConfigs());
			hsu.flush();
			bene.delete();

			hsu.flush();
			int tempCount = 1000;
			for (WizardConfigurationBenefit ben : wizCat.getWizardBenefits())
				ben.setSeqNo(tempCount++);
			hsu.flush();
			int count = 0;
			for (WizardConfigurationBenefit ben : wizCat.getWizardBenefits())
				ben.setSeqNo(count++);
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardConfigurationBenefit();
		bean.generateId();
		return getWizardConfigurationBenefitId();
	}

	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(WizardConfigurationBenefit.class, key);
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

	public HrBenefit getBenefit() {
		return bean.getBenefit();
	}

	public void setBenefit(HrBenefit benefit) {
		bean.setBenefit(benefit);
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

	public String getDeclineMessage() {
		return bean.getDeclineMessage();
	}

	public void setDeclineMessage(String declineMessage) {
		bean.setDeclineMessage(declineMessage);
	}

	public String getWizardConfigurationBenefitId() {
		return bean.getWizardConfigurationBenefitId();
	}

	public void setWizardConfigurationBenefitId(String wizardConfigurationBenefitId) {
		bean.setWizardConfigurationBenefitId(wizardConfigurationBenefitId);
	}

	public WizardConfigurationCategory getWizardConfigurationCategory() {
		return bean.getWizardConfigurationCategory();
	}

	public void setWizardConfigurationCategory(WizardConfigurationCategory wizardConfigurationCategory) {
		bean.setWizardConfigurationCategory(wizardConfigurationCategory);
	}

	public static BWizardConfigurationBenefit[] list(final int max, final String categoryWizId) {
		return makeArray(ArahantSession.getHSU().createCriteria(WizardConfigurationBenefit.class)
				.setMaxResults(max)
				.orderBy(WizardConfigurationBenefit.SEQNO)
				.eq(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY, new BWizardConfigurationCategory(categoryWizId).getBean())
				.list());
	}

	public static BWizardConfigurationBenefit[] makeArray(List<WizardConfigurationBenefit> l) {
		final BWizardConfigurationBenefit[] ret = new BWizardConfigurationBenefit[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardConfigurationBenefit(l.get(loop));

		return ret;
	}

	public List<WizardConfigurationConfig> getWizardConfigs() {
		return ArahantSession.getHSU().createCriteria(WizardConfigurationConfig.class).eq(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT, this.getBean()).list();
	}

	public BScreenOrGroup getWizardScreen() {
		if (getScreen() != null)
			return new BScreen(getScreen());
		else
			if (getWizardConfigurationCategory().getWizardConfiguration().getWizardVersion().equals("2"))
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/benefit/BenefitWizardBenefitScreen.swf");
			else if (getWizardConfigurationCategory().getWizardConfiguration().getWizardVersion().equals("3")) {
				HrBenefit ben = getBenefit();

				if (ben.getAutoAssign() == 'Y')
					return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefitAutoAssign/SimpleBenefitAutoAssignScreen.swf");
				else if (ben.getBenefitAmountModel() == 'R'  ||  ben.getEmployeeCostModel() == 'R')
					return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleCoverage/SimpleCoverageScreen.swf");
				else if ((new BHRBenefit(ben)).activeConfigsCoverEmployeeOnly())
					return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefitEmpOnly/SimpleBenefitEmpOnlyScreen.swf");
				else
					return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefit/SimpleBenefitScreen.swf");
			} else
				throw new ArahantWarning("Wizard Configuration version not specified.");
	}

	public static BScreenOrGroup getWizardScreen(BHRBenefit b, String wizardVersion) {
		if (wizardVersion.equals("2"))
			return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/benefit/BenefitWizardBenefitScreen.swf");
		else if (wizardVersion.equals("3"))
			if (b.getAutoAssign() == 'Y')
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefitAutoAssign/SimpleBenefitAutoAssignScreen.swf");
			else if (b.getBenefitAmountModel() == 'R'  ||  b.getEmployeeCostModel() == 'R')
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleCoverage/SimpleCoverageScreen.swf");
			else if (b.activeConfigsCoverEmployeeOnly())
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefitEmpOnly/SimpleBenefitEmpOnlyScreen.swf");
			else
				return BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleBenefit/SimpleBenefitScreen.swf");
		else
			throw new ArahantWarning("Wizard Configuration version not specified.");
	}
}
