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
import com.arahant.exceptions.ArahantDeleteException;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BWizardConfiguration extends SimpleBusinessObjectBase<WizardConfiguration> {

	public BWizardConfiguration() {
	}

	public BWizardConfiguration(final WizardConfiguration bean) {
		this.bean = bean;
	}

	public BWizardConfiguration(final String key) throws ArahantException {
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

//    public static void delete(final String[] wizardConfigurationIds) throws ArahantException {
//        for (final String element : wizardConfigurationIds) {
//            new BWizardConfiguration(element).delete();
//        }
//    }
	public static void delete(final String wizardConfigurationId) throws ArahantException {
		try {
			BWizardConfiguration bWizConf = new BWizardConfiguration(wizardConfigurationId);

			for (WizardConfigurationCategory wizCat : bWizConf.getWizardCategories()) {
				BWizardConfigurationCategory bWizCat = new BWizardConfigurationCategory(wizCat);
				bWizCat.delete();
			}
			bWizConf.delete();
		} catch (final RuntimeException e) {
			throw new ArahantDeleteException();
		}
	}

	@Override
	public String create() throws ArahantException {
		bean = new WizardConfiguration();
		bean.generateId();
		return getWizardConfigurationId();
	}
	
	private void internalLoad(String key) throws ArahantException {
		logger.debug("Loading " + key);
		bean = ArahantSession.getHSU().get(WizardConfiguration.class, key);
	}

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	public Employee getHrContact() {
		return bean.getHrContact();
	}

	public void setHrContact(Employee hrContact) {
		bean.setHrContact(hrContact);
	}

	public String getHrContactId() {
		return bean.getHrContactId();
	}

	public void setHrContactId(String hrContactId) {
		bean.setHrContact(ArahantSession.getHSU().get(Employee.class, hrContactId));
	}

	public char getAllowInapplicable() {
		return bean.getAllowInapplicable();
	}

	public void setAllowInapplicable(char allowInapplicable) {
		bean.setAllowInapplicable(allowInapplicable);
	}

	public BenefitClass getBenefitClass() {
		return bean.getBenefitClass();
	}

	public void setBenefitClass(BenefitClass benefitClass) {
		bean.setBenefitClass(benefitClass);
	}

	public String getCompanyId() {
		return bean.getCompanyId();
	}

	public void setCompanyId(String companyId) {
		bean.setCompanyId(companyId);
	}

	public String getDescription() {
		return bean.getDescription();
	}

	public void setDescription(String description) {
		bean.setDescription(description);
	}

	public boolean getAutoApproveDeclines() {
		return bean.getAutoApproveDeclines() == 'Y' ? true : false;
	}

	public void setAutoApproveDeclines(boolean autoApproveDeclines) {
		bean.setAutoApproveDeclines(autoApproveDeclines ? 'Y' : 'N');
	}

	public void setAutoApproveDeclines(char autoApproveDeclines) {
		bean.setAutoApproveDeclines(autoApproveDeclines);
	}

	public char getFullscreen() {
		return bean.getFullscreen();
	}

	public void setFullscreen(char fullscreen) {
		bean.setFullscreen(fullscreen);
	}

	public String getName() {
		return bean.getName();
	}

	public void setName(String name) {
		bean.setName(name);
	}

	public char getProgressPaneButtons() {
		return bean.getProgressPaneButtons();
	}

	public void setProgressPaneButtons(char progressPaneButtons) {
		bean.setProgressPaneButtons(progressPaneButtons);
	}

	public ProjectCategory getProjectCategory() {
		return bean.getProjectCategory();
	}

	public void setProjectCategory(ProjectCategory projectCategory) {
		bean.setProjectCategory(projectCategory);
	}

	public ProjectStatus getProjectStatus() {
		return bean.getProjectStatus();
	}

	public void setProjectStatus(ProjectStatus projectStatus) {
		bean.setProjectStatus(projectStatus);
	}

	public String getProjectSummary() {
		return bean.getProjectSummary();
	}

	public void setProjectSummary(String projectSummary) {
		bean.setProjectSummary(projectSummary);
	}

	public ProjectType getProjectType() {
		return bean.getProjectType();
	}

	public void setProjectType(ProjectType projectType) {
		bean.setProjectType(projectType);
	}

	public char getRememberState() {
		return bean.getRememberState();
	}

	public void setRememberState(char rememberState) {
		bean.setRememberState(rememberState);
	}

	public char getShowDemographics() {
		return bean.getShowDemographics();
	}

	public void setShowDemographics(char showDemographics) {
		bean.setShowDemographics(showDemographics);
	}

	public char getShowDependents() {
		return bean.getShowDependents();
	}

	public void setShowDependents(char showDependents) {
		bean.setShowDependents(showDependents);
	}

	public char getShowInapplicable() {
		return bean.getShowInapplicable();
	}

	public void setShowInapplicable(char showInapplicable) {
		bean.setShowInapplicable(showInapplicable);
	}

	public char getSkipPresentation() {
		return bean.getSkipPresentation();
	}

	public void setSkipPresentation(char skipPresentation) {
		bean.setSkipPresentation(skipPresentation);
	}

	public char getPaymentInfo() {
		return bean.getPaymentInfo();
	}

	public boolean getPaymentInfoBoolean() {
		return bean.getPaymentInfo() == 'Y';
	}

	public void setPaymentInfoBoolean(boolean paymentInfo) {
		bean.setPaymentInfo(paymentInfo ? 'Y' : 'N');
	}

	public void setPaymentInfo(char paymentInfo) {
		bean.setPaymentInfo(paymentInfo);
	}

	public char getUseToolTips() {
		return bean.getUseToolTips();
	}

	public void setUseToolTips(char useToolTips) {
		bean.setUseToolTips(useToolTips);
	}

	public String getWizardConfigurationId() {
		return bean.getWizardConfigurationId();
	}

	public void setWizardConfigurationId(String wizardConfigurationId) {
		bean.setWizardConfigurationId(wizardConfigurationId);
	}

	public char getWizardType() {
		return bean.getWizardType();
	}

	public void setWizardType(char wizardType) {
		bean.setWizardType(wizardType);
	}

	public String getWizardVersion() {
		return bean.getWizardVersion();
	}

	public void setWizardVersion(String wizardVersion) {
		bean.setWizardVersion(wizardVersion);
	}

	public void setLockOnFinalize(String lockOnFinalize) {
		bean.setLockOnFinalize(lockOnFinalize.charAt(0));
	}

	public String getLockOnFinalize() {
		return bean.getLockOnFinalize() + "";
	}

	public void setLockOnFinalizeBool(boolean lockOnFinalize) {
		bean.setLockOnFinalize(lockOnFinalize ? 'Y' : 'N');
	}

	public boolean getLockOnFinalizeBool() {
		return (bean.getLockOnFinalize() == 'Y');
	}

	public char getPhysicianSelectionMode() {
		return bean.getPhysicianSelectionMode();
	}

	public void setPhysicianSelectionMode(char physicianSelectionMode) {
		bean.setPhysicianSelectionMode(physicianSelectionMode);
	}

	public void setPhysicianSelectionMode(String mode) {
		bean.setPhysicianSelectionMode(mode.charAt(0));
	}

	public boolean getPhysicianModeImmediate() {
		return bean.getPhysicianSelectionMode() == 'I';
	}

	public short getBenefitReport() {
		return bean.getBenefitReport();
	}

	public void setBenefitReport(short benefitReport) {
		bean.setBenefitReport(benefitReport);
	}

	public String getDemographicInstructions() {
		return bean.getDemographicInstructions();
	}

	public void setDemographicInstructions(String demographicInstructions) {
		bean.setDemographicInstructions(demographicInstructions);
	}

	public Screen getDemographicScreen() {
		return bean.getDemographicScreen();
	}

	public void setDemographicScreen(Screen demographicScreen) {
		bean.setDemographicScreen(demographicScreen);
	}

	public String getDemographicScreenId() {
		return bean.getDemographicScreenId();
	}

	public short getReviewReport() {
		return bean.getReviewReport();
	}

	public void setReviewReport(short reviewReport) {
		bean.setReviewReport(reviewReport);
	}
	
	public String getDemographicsAvatar() {
		return bean.getDemographicsAvatar();
	}

	public void setDemographicsAvatar(String demographicsAvatar) {
		bean.setDemographicsAvatar(demographicsAvatar);
	}

	public String getDependentsAvatar() {
		return bean.getDependentsAvatar();
	}

	public void setDependentsAvatar(String dependentsAvatar) {
		bean.setDependentsAvatar(dependentsAvatar);
	}

	public char getEnableAvatars() {
		return bean.getEnableAvatars();
	}
	
	public boolean getEnableAvatarsBoolean() {
		return bean.getEnableAvatars() == 'Y';
	}

	public void setEnableAvatars(char enableAvatars) {
		bean.setEnableAvatars(enableAvatars);
	}

	public void setEnableAvatarsBoolean(boolean enableAvatars) {
		bean.setEnableAvatars(enableAvatars ? 'Y' : 'N');
	}

	public String getFinishAvatar() {
		return bean.getFinishAvatar();
	}

	public void setFinishAvatar(String finishAvatar) {
		bean.setFinishAvatar(finishAvatar);
	}

	public String getQualifyingEventAvatar() {
		return bean.getQualifyingEventAvatar();
	}

	public void setQualifyingEventAvatar(String qualifyingEventAvatar) {
		bean.setQualifyingEventAvatar(qualifyingEventAvatar);
	}

	public String getReviewAvatar() {
		return bean.getReviewAvatar();
	}

	public void setReviewAvatar(String reviewAvatar) {
		bean.setReviewAvatar(reviewAvatar);
	}

	public String getWelcomeAvatar() {
		return bean.getWelcomeAvatar();
	}

	public void setWelcomeAvatar(String welcomeAvatar) {
		bean.setWelcomeAvatar(welcomeAvatar);
	}

	public char getShowAnnualCost() {
		return bean.getShowAnnualCost();
	}

	public void setShowAnnualCost(char show) {
		bean.setShowAnnualCost(show);
	}

	public char getShowMonthlyCost() {
		return bean.getShowMonthlyCost();
	}

	public void setShowMonthlyCost(char show) {
		bean.setShowMonthlyCost(show);
	}

	public char getShowPPPCost() {
		return bean.getShowPPPCost();
	}

	public void setShowPPPCost(char show) {
		bean.setShowPPPCost(show);
	}
	
	public char getShowQualifyingEvent() {
		return bean.getShowQualifyingEvent();
	}

	public void setShowQualifyingEvent(char showQualifyingEvent) {
		bean.setShowQualifyingEvent(showQualifyingEvent);
	}

	public char getAllowDemographicChanges() {
		return bean.getAllowDemographicChanges();
	}

	public void setAllowDemographicChanges(char allowDemographicChanges) {
		bean.setAllowDemographicChanges(allowDemographicChanges);
	}

	public short getHdeType() {
		return bean.getHdeType();
	}

	public void setHdeType(short hdeType) {
		bean.setHdeType(hdeType);
	}

	public short getHdePeriod() {
		return bean.getHdePeriod();
	}

	public void setHdePeriod(short hdePeriod) {
		bean.setHdePeriod(hdePeriod);
	}

	public short getHdeDaysBefore() {
		return bean.getHdeDaysBefore();
	}

	public void setHdeDaysBefore(short hdeDaysBefore) {
		bean.setHdeDaysBefore(hdeDaysBefore);
	}

	public short getHdeDaysAfter() {
		return bean.getHdeDaysAfter();
	}

	public void setHdeDaysAfter(short hdeDaysAfter) {
		bean.setHdeDaysAfter(hdeDaysAfter);
	}
		
	public char getAllowReportFromReview() {
		return bean.getAllowReportFromReview();
	}

	public void setAllowReportFromReview(char allowReportFromReview) {
		bean.setAllowReportFromReview(allowReportFromReview);
	}

	public static BWizardConfiguration[] list(int max) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		return makeArray(hsu.createCriteria(WizardConfiguration.class).eq(WizardConfiguration.COMPANY_ID, hsu.getCurrentCompany().getCompanyId()).setMaxResults(max).list());
	}

	private static BWizardConfiguration[] makeArray(List<WizardConfiguration> l) {
		final BWizardConfiguration[] ret = new BWizardConfiguration[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BWizardConfiguration(l.get(loop));

		return ret;
	}

	public static BHRBenefitCategory[] listAvailableCategories(final int max, final String wizardConfigurationId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<WizardConfigurationCategory> wizcat = hsu.createCriteria(WizardConfigurationCategory.class).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, new BWizardConfiguration(wizardConfigurationId).getBean()).list();
		List<String> catids = new ArrayList<String>();

		for (WizardConfigurationCategory c : wizcat)
			catids.add(c.getBenefitCategory().getBenefitCatId());

		HibernateCriteriaUtil<HrBenefitCategory> hcu = hsu.createCriteria(HrBenefitCategory.class).setMaxResults(max).notIn(HrBenefitCategory.BENEFIT_CATEGORY_ID, catids);

		if (new BWizardConfiguration(wizardConfigurationId).getBenefitClass() != null) {
			HibernateCriteriaUtil chcu = hcu.joinTo(HrBenefitCategory.HRBENEFIT).joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = chcu.makeCriteria();
			HibernateCriterionUtil cri1 = chcu.makeCriteria();

			HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, new BWizardConfiguration(wizardConfigurationId).getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}
		return BHRBenefitCategory.makeArray(hcu.list());
	}

	public static BHRBenefit[] listAvailableBenefits(final int max, final String categoryWizardId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BWizardConfigurationCategory cat = new BWizardConfigurationCategory(categoryWizardId);
		List<WizardConfigurationBenefit> wizbens = hsu.createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY, new BWizardConfigurationCategory(categoryWizardId).getBean()).list();
		List<String> benids = new ArrayList<String>();

		for (WizardConfigurationBenefit c : wizbens)
			benids.add(c.getBenefit().getBenefitId());

		HibernateCriteriaUtil<HrBenefit> hcu = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_CATEGORY, cat.getBenefitCategory()).notIn(HrBenefit.BENEFITID, benids);

		if (cat.getWizardConfiguration().getBenefitClass() != null) {
			HibernateCriteriaUtil chcu = hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS, "configs");
			HibernateCriterionUtil cri1 = chcu.makeCriteria();
			HibernateCriterionUtil cri2 = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes").makeCriteria();
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri3 = hcu.makeCriteria();
			HibernateCriterionUtil cri4 = hcu.leftJoinTo(HrBenefit.BENEFIT_CLASS, "beneClasses").makeCriteria();
			HibernateCriterionUtil orcri2 = hcu.makeCriteria();

			cri1.sizeEq("configs." + HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, cat.getWizardConfiguration().getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);
			orcri.add();
			cri3.sizeEq(HrBenefit.BENEFIT_CLASS, 0);
			cri4.eq("beneClasses." + BenefitClass.BENEFIT_CLASS_ID, cat.getWizardConfiguration().getBenefitClass().getBenefitClassId());

			orcri2.or(cri3, cri4);
			orcri2.add();
		} else {
			//HibernateCriteriaUtil chcu=hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = hcu.makeCriteria();
			HibernateCriterionUtil cri1 = hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS, "configs").makeCriteria();
			HibernateCriterionUtil cri2 = hcu.makeCriteria();

			cri1.sizeEq("configs." + HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.sizeEq(HrBenefit.BENEFIT_CLASS, 0);

			orcri.and(cri1, cri2);
			orcri.add();
		}

		return BHRBenefit.makeArray(hcu.list());
	}

	public static BHRBenefitConfig[] listAvailableConfigs(final int max, final String benefitWizardId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		BWizardConfigurationBenefit bene = new BWizardConfigurationBenefit(benefitWizardId);
		List<WizardConfigurationConfig> wizconfs = hsu.createCriteria(WizardConfigurationConfig.class).eq(WizardConfigurationConfig.WIZARD_CONFIGURATION_BENEFIT, new BWizardConfigurationBenefit(benefitWizardId).getBean()).list();
		List<String> confids = new ArrayList<String>();

		for (WizardConfigurationConfig c : wizconfs)
			confids.add(c.getBenefitConfig().getBenefitConfigId());

		HibernateCriteriaUtil<HrBenefitConfig> hcu = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.HR_BENEFIT, bene.getBenefit()).notIn(HrBenefitConfig.BENEFIT_CONFIG_ID, confids);

		if (bene.getWizardConfigurationCategory().getWizardConfiguration().getBenefitClass() != null) {
			HibernateCriteriaUtil chcu = hcu;
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = chcu.makeCriteria();
			HibernateCriterionUtil cri1 = chcu.makeCriteria();

			HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, bene.getWizardConfigurationCategory().getWizardConfiguration().getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);

			orcri.add();
		}

		return BHRBenefitConfig.makeArray(hcu.list());
	}

	public List<WizardConfigurationCategory> getWizardCategories() {
		return ArahantSession.getHSU().createCriteria(WizardConfigurationCategory.class).orderBy(WizardConfigurationCategory.SEQNO).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, this.getBean()).list();
	}
	
	private static boolean validBenefit(HrBenefit ben, BEmployee emp) {
		if (emp == null)
			return true;
		
		boolean employee=false, spouse=false, children=false;
		for (HrBenefitConfig bc : ben.getBenefitConfigs()) {
			if (bc.getEmployee() == 'Y') {
				employee = true;
				break;
			}
			if (!spouse  &&  (bc.getSpouseDeclinesOutside() == 'Y'  ||  bc.getSpouseEmpOrChildren() == 'Y'  ||  bc.getSpouseEmployee() == 'Y'  ||  bc.getSpouseNonEmpOrChildren() == 'Y'  ||
					bc.getSpouseNonEmployee() == 'Y') &&  emp.hasSpouse())
				spouse = true;
			if (!children  &&  (bc.getChildren() == 'Y'  ||  bc.getSpouseEmpOrChildren() == 'Y'  ||  bc.getSpouseNonEmpOrChildren() == 'Y')  &&  emp.hasChildren())
				children = true;
		}
		if (!employee  &&  !children  &&  !spouse)
			return false;
		
		short minAge = ben.getMinAge();
		short maxAge = ben.getMaxAge();
		short empAge = emp.getAgeAsOf(DateUtils.now());
		if (maxAge > 0 && empAge > maxAge)
			return false;
		if (empAge < minAge)
			return false;
		
		double minPay = ben.getMinPay();
		double maxPay = ben.getMaxPay();
		double empPay = emp.getCurrentSalary();
		if (minPay > 1000.0  ||  maxPay > 1000.0  ||  empPay > 1000.0) {  //  then some numbers are in yearly salary, convert all to salary
			if (minPay > .01  &&  minPay < 1000.0)
				minPay *= (40 * 52);
			if (maxPay > .01  &&  maxPay < 1000.0)
				maxPay *= (40 * 52);
			if (empPay > .01  &&  empPay < 1000.0)
				empPay *= (40 * 52);
		}
		if (minPay > .01  &&  empPay < minPay)
			return false;
		if (maxPay > .01  &&  empPay > maxPay)
			return false;
		
		double minHours = ben.getMinHoursPerWeek();
		if (minHours > 1.0) {
			double empHours = emp.getHoursPerWeek();
			if (empHours < minHours)
				return false;
		}
		
		int endDate = ben.getEndDate();
		int lastEnrollmentDate = ben.getLastEnrollmentDate();
		int today = DateUtils.today();
		if (endDate != 0  &&  endDate < today)
			return false;
		if (lastEnrollmentDate != 0  &&  lastEnrollmentDate < today)
			return false;
		
		return true;
	}
	
	private void deleteAllBenefitJoins() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		
		hsu.commitTransaction();
		hsu.beginTransaction();
		
		Person p = hsu.getCurrentPerson();
		
		HibernateCriteriaUtil<PersonCR> crit = hsu.createCriteria(PersonCR.class).eq(PersonCR.PERSON_ID, p.getPersonId());
		
		List<PersonCR> pcrlst = crit.list();
		
		LinkedList<String> plst = new LinkedList<String>();
		plst.addFirst(p.getPersonId());
		for (PersonCR pcr : pcrlst)
			plst.addFirst(pcr.getChangeRecordId());
		
		List<HrBenefitJoin> bjs = hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.PAYING_PERSON_ID, plst).list();
		List<String> bjlst = new LinkedList<String>();
		for (HrBenefitJoin bj : bjs) {
			HrBenefitConfig bc = bj.getHrBenefitConfig();
			if (bc == null  ||  bc.getHrBenefit().getTimeRelated() == 'N')
				bjlst.add(bj.getBenefitJoinId());
		}
		
		hsu.createCriteria(WizardProject.class).in(WizardProject.BENEFIT_JOIN_ID, bjlst).delete();
		hsu.flush();
		hsu.createCriteria(HrBeneficiary.class).in(HrBeneficiary.BENEFIT_JOIN_ID, bjlst).delete();
		hsu.flush();
		hsu.createCriteria(HrEmployeeBeneficiaryH.class).in(HrEmployeeBeneficiaryH.BENEFIT_JOIN_ID, bjlst).delete();
		hsu.flush();
		hsu.createCriteria(HrPhysician.class).in(HrPhysician.BENEFIT_JOIN_ID, bjlst).delete();
		hsu.flush();
		hsu.createCriteria(HrBenefitJoin.class).in(HrBenefitJoin.BENEFIT_JOIN_ID, bjlst).delete();
		hsu.flush();
		hsu.createCriteria(HrBenefitJoinH.class).in(HrBenefitJoinH.BENEFIT_JOIN_ID, bjlst).delete();
		
		hsu.commitTransaction();
		hsu.beginTransaction();
		
/*		
		hsu.deleteAllRecords(WizardProject.class);
		hsu.deleteAllRecords(HrBeneficiary.class);
		hsu.deleteAllRecords(HrEmployeeBeneficiaryH.class);
		hsu.deleteAllRecords(HrPhysician.class);
		hsu.deleteAllRecords(HrBenefitJoin.class);
		hsu.deleteAllRecords(HrBenefitJoinH.class);
		* */
	}

	public List<BWizardScreen> loadWizard(BEmployee be) {
		List<BWizardScreen> itemList = new ArrayList<BWizardScreen>();
		List<HrBenefit> doneBenefits = new ArrayList<HrBenefit>();
		
		if (BProperty.getBoolean("DeleteBenefitJoins", false)) 
			deleteAllBenefitJoins();

		if (getWizardVersion().equals("2")) {
			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/welcome/BenefitWizardWelcomeScreen.swf")));
			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/qualifyingEvent/BenefitWizardQualifyingEventScreen.swf")));

			if (getShowDemographics() == 'Y')
				if (getDemographicScreen() != null)
					itemList.add(new BWizardScreen(new BScreen(getDemographicScreen())));
				else
					itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/demographics/BenefitWizardDemographicsScreen.swf")));
			if (getShowDependents() == 'Y')
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/dependents/BenefitWizardDependentsScreen.swf")));
			boolean flexAdded = false;
			for (WizardConfigurationCategory wizCat : getWizardCategories()) {
				BWizardConfigurationCategory bwizCat = new BWizardConfigurationCategory(wizCat);
				if (bwizCat.getBenefitCategory().getAllowsMultipleBenefitsBoolean()) {
					if (bwizCat.getBenefitCategory().getBenefitType() == HrBenefitCategory.FLEX_TYPE)
						if (!flexAdded) {
							itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/flex/BenefitWizardFlexScreen.swf")));
							flexAdded = true;
						}
					for (WizardConfigurationBenefit wizBen : bwizCat.getWizardBenefits()) {
						BWizardConfigurationBenefit bwizBen = new BWizardConfigurationBenefit(wizBen);
						itemList.add(new BWizardScreen(bwizBen.getWizardScreen(), bwizBen.getBenefit().getBenefitId(), bwizBen.getName() + " (Ben)", bwizBen));
					}
				} else
					itemList.add(new BWizardScreen(bwizCat.getWizardScreen(), bwizCat.getBenefitCategory().getBenefitCatId(), bwizCat.getDescription() + " (Cat)", bwizCat));
			}

			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/review/BenefitWizardReviewScreen.swf")));
			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/finish/FinishScreen.swf")));
		} else if (getWizardVersion().equals("3")) {
			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleWelcome/SimpleWelcomeScreen.swf")));
			if (isEmpty(BProperty.get(StandardProperty.DefaultWizardBcrId)))
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleQualifyingEvent/SimpleQualifyingEventScreen.swf")));

			if (getShowDemographics() == 'Y')
				if (getDemographicScreen() != null)
					itemList.add(new BWizardScreen(new BScreen(getDemographicScreen())));
				else
					itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleDemographics/SimpleDemographicsScreen.swf")));
			if (getShowDependents() == 'Y')
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleDependents/SimpleDependentsScreen.swf")));
//			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/w4PDF/W4Screen.swf")));
			for (WizardConfigurationCategory wizCat : getWizardCategories()) {
				BWizardConfigurationCategory bwizCat = new BWizardConfigurationCategory(wizCat);
				if (bwizCat.getBenefitCategory().getAllowsMultipleBenefitsBoolean()) //allows multiple benefits
				
					for (WizardConfigurationBenefit wizBen : bwizCat.getWizardBenefits()) {
						//add each benefit individually in this category
						BWizardConfigurationBenefit bwizBen = new BWizardConfigurationBenefit(wizBen);
						//if the employee is too old or too young to be eligible for this benefit, continue
						if (!validBenefit(bwizBen.getBenefit(), be))
							continue;

						if (!doneBenefits.contains(bwizBen.getBenefit())) {
							itemList.add(new BWizardScreen(bwizBen.getWizardScreen(), bwizBen.getBenefit().getBenefitId(), bwizBen.getName() + " (Ben)", bwizBen));
							doneBenefits.add(bwizBen.getBenefit());
						}
						//now lets see if there are any dependencies that have been unlocked
						BHRBenefit bhb = new BHRBenefit(bwizBen.getBenefit());
						for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bhb)) {
							BBenefitDependency bbd = new BBenefitDependency(bd);
							if (bbd.getHiddenBoolean())
								continue;
							BHRBenefit bdb = new BHRBenefit(bd.getDependentBenefit());
							//add them if they have been met
							if (be != null && bdb.getDependencyEnrollment(be.getPerson()) != null && !doneBenefits.contains(bdb.getBean())) {
								itemList.add(new BWizardScreen(BWizardConfigurationBenefit.getWizardScreen(bdb, "3"), bdb.getBenefitId(), bdb.getName() + " (Ben)", bdb));
								doneBenefits.add(bdb.getBean());
							}
						}

					}
				else if (bwizCat.getWizardBenefits().size() == 1) {
					BWizardConfigurationBenefit bwizBen = new BWizardConfigurationBenefit(bwizCat.getWizardBenefits().get(0));
					if (!validBenefit(bwizBen.getBenefit(), be))
						continue;
					if (!doneBenefits.contains(bwizBen.getBenefit())) {
						itemList.add(new BWizardScreen(bwizBen.getWizardScreen(), bwizBen.getBenefit().getBenefitId(), bwizBen.getName() + " (Ben)", bwizBen));
						doneBenefits.add(bwizBen.getBenefit());
					}

					//now lets see if there are any dependencies that have been unlocked
					BHRBenefit bhb = new BHRBenefit(bwizBen.getBenefit());
					for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bhb)) {
						BHRBenefit bdb = new BHRBenefit(bd.getDependentBenefit());
						//add them if they have been met
						if (be != null && bdb.getDependencyEnrollment(be.getPerson()) != null && !doneBenefits.contains(bdb.getBean())) {
							BBenefitDependency bbd = new BBenefitDependency(bd);
							if (bbd.getHiddenBoolean())
								continue;
							itemList.add(new BWizardScreen(BWizardConfigurationBenefit.getWizardScreen(bdb, "3"), bdb.getBenefitId(), bdb.getName() + " (Ben)", bdb));
							doneBenefits.add(bdb.getBean());
						}
					}
				} else {
					int numThatPassed = 0;
					itemList.add(new BWizardScreen(bwizCat.getWizardScreen(), bwizCat.getBenefitCategory().getBenefitCatId(), bwizCat.getDescription() + " (Cat)", bwizCat));
					for (WizardConfigurationBenefit wizBen : bwizCat.getWizardBenefits()) {
						BWizardConfigurationBenefit bwizBen = new BWizardConfigurationBenefit(wizBen);
						if (!validBenefit(bwizBen.getBenefit(), be))
							continue;
						numThatPassed++;
						//lets see if there are any dependencies that have been unlocked
						BHRBenefit bhb = new BHRBenefit(bwizBen.getBenefit());
						for (BenefitDependency bd : BBenefitDependency.getBenefitDependenciesWhereRequired(bhb)) {
							BBenefitDependency bbd = new BBenefitDependency(bd);
							if (bbd.getHiddenBoolean())
								continue;
							BHRBenefit bdb = new BHRBenefit(bd.getDependentBenefit());
							//add them if they have been met
							if (be != null && bdb.getDependencyEnrollment(be.getPerson()) != null && !doneBenefits.contains(bdb.getBean())) {
								itemList.add(new BWizardScreen(BWizardConfigurationBenefit.getWizardScreen(bdb, "3"), bdb.getBenefitId(), bdb.getName() + " (Ben)", bdb));
								doneBenefits.add(bdb.getBean());
							}
						}
					}
					if (numThatPassed == 0)
						itemList.remove(itemList.size()-1);
				}
			}

			if (!getPhysicianModeImmediate())
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/physicians/PhysiciansScreen.swf")));
			if (BProperty.prismVersion() == 1)
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/prismReview/PrismReviewScreen.swf")));
			else
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleReview/SimpleReviewScreen.swf")));
			if (BProperty.prismVersion() == 1)
				itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/prismDocSign/PrismDocSignScreen.swf"), "Sign Documents"));
			itemList.add(new BWizardScreen(BScreen.getByFilename("com/arahant/app/screen/standard/wizard/benefitWizard/page/simpleFinish/SimpleFinishScreen.swf")));
		}
		return itemList;
	}

	/**
	 * This is (currently) the only place wizard_project records are created.
	 * Projects are created as directed by project_template_benefit.
	 * Each project can have several wizard_project records based on the change types.
	 * wizard_project also designates a project to be created.  This will only occur if a similar project hasn't already been created.
	 * 
	 * 
	 * @param emp 
	 */
	public void createWizardProjects(BEmployee emp) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<BHRBenefitJoin> notDoneBenefits = new ArrayList<BHRBenefitJoin>();
		List<Project> createdProjects = new ArrayList<Project>();
		List<ProjectTemplateBenefit> donePTBs = new ArrayList<ProjectTemplateBenefit>();

		HrEmployeeStatus empStatus = emp.getCurrentStatusHistory().getHrEmployeeStatus();
		List<OrgGroup> orgGroups = new ArrayList<OrgGroup>();
		orgGroups.add(emp.getCompany().getBean());
		for (OrgGroupAssociation oga : emp.getOrgGroupAssociations()) {
			BOrgGroup bog = new BOrgGroup(oga.getOrgGroup());
			orgGroups.add(bog.getOrgGroup());
			orgGroups.addAll(bog.getAllParentOrgGroups());

		}
		List<HrBenefitJoin> joins = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, emp.getPerson()).eq(HrBenefitJoin.APPROVED, 'N').eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).list();
		for (HrBenefitJoin j : joins) {

			BHRBenefitJoin bbj = new BHRBenefitJoin(j);

			if (bbj.getBean().getHrBenefitCategory() != null) { //if this is a category decline, just deal with it later
				notDoneBenefits.add(bbj);
				continue;
			}

			HibernateCriteriaUtil<ProjectTemplateBenefit> hcu = hsu.createCriteria(ProjectTemplateBenefit.class);

			//The following criteria are for the benefit / BCR combinations
			HibernateCriterionUtil critBene = hcu.makeCriteria();
			HibernateCriterionUtil critBene2 = hcu.makeCriteria();
			HibernateCriterionUtil critBene3 = hcu.makeCriteria();
			HibernateCriterionUtil critBCR = hcu.makeCriteria();
			HibernateCriterionUtil critBCR2 = hcu.makeCriteria();
			HibernateCriterionUtil critBCR3 = hcu.makeCriteria();
			HibernateCriterionUtil critBeneBCR = hcu.makeCriteria();
			HibernateCriterionUtil critBeneBCR2 = hcu.makeCriteria();
			HibernateCriterionUtil critBeneBCR3 = hcu.makeCriteria();
			HibernateCriterionUtil critNull = hcu.makeCriteria();
			HibernateCriterionUtil critNull2 = hcu.makeCriteria();
			HibernateCriterionUtil critNull3 = hcu.makeCriteria();
			HibernateCriterionUtil critOr = hcu.makeCriteria();

			//benefit matches and BCR is null
			critBene.eq(ProjectTemplateBenefit.BENEFIT, new BHRBenefit(bbj.getBenefitId()).getBean());
			critBene2.isNull(ProjectTemplateBenefit.BCR);
			critBene3.and(critBene, critBene2);
			//bcr matches and benefit is null
			critBCR.eq(ProjectTemplateBenefit.BCR_ID, bbj.getBenefitChangeReasonId());
			critBCR2.isNull(ProjectTemplateBenefit.BENEFIT);
			critBCR3.and(critBCR, critBCR2);
			//both benefit and bcr match
			critBeneBCR.eq(ProjectTemplateBenefit.BCR_ID, bbj.getBenefitChangeReasonId());
			critBeneBCR2.eq(ProjectTemplateBenefit.BENEFIT, new BHRBenefit(bbj.getBenefitId()).getBean());
			critBeneBCR3.and(critBeneBCR, critBeneBCR2);


			critNull.isNull(ProjectTemplateBenefit.BCR);
			critNull2.isNull(ProjectTemplateBenefit.BENEFIT);
			critNull3.and(critNull, critNull2);

			critOr.or(critBene3, critBCR3, critBeneBCR3, critNull3);
			critOr.add();

			//The following criterias are for the org group / status combinations
			HibernateCriterionUtil crit1 = hcu.makeCriteria();
			HibernateCriterionUtil crit2 = hcu.makeCriteria();
			HibernateCriterionUtil crit3 = hcu.makeCriteria();
			HibernateCriterionUtil crit4 = hcu.makeCriteria();
			HibernateCriterionUtil crit5 = hcu.makeCriteria();
			HibernateCriterionUtil crit6 = hcu.makeCriteria();
			HibernateCriterionUtil crit7 = hcu.makeCriteria();
			HibernateCriterionUtil crit8 = hcu.makeCriteria();
			HibernateCriterionUtil crit9 = hcu.makeCriteria();
			HibernateCriterionUtil crit10 = hcu.makeCriteria();
			HibernateCriterionUtil crit11 = hcu.makeCriteria();
			HibernateCriterionUtil crit12 = hcu.makeCriteria();
			HibernateCriterionUtil crit13 = hcu.makeCriteria();

			//both are null qualify
			crit1.isNull(ProjectTemplateBenefit.EMPLOYEE_STATUS);
			crit2.isNull(ProjectTemplateBenefit.ORG_GROUP);
			crit3.and(crit1, crit2);

			//employee status null and org group not null
			crit4.isNull(ProjectTemplateBenefit.EMPLOYEE_STATUS);
			crit5.in(ProjectTemplateBenefit.ORG_GROUP, orgGroups);
			crit6.and(crit4, crit5);

			//employee status not null and org group null
			crit7.eq(ProjectTemplateBenefit.EMPLOYEE_STATUS, empStatus);
			crit8.isNull(ProjectTemplateBenefit.ORG_GROUP);
			crit9.and(crit7, crit8);

			//employee status not null and org group not null
			crit10.eq(ProjectTemplateBenefit.EMPLOYEE_STATUS, empStatus);
			crit11.in(ProjectTemplateBenefit.ORG_GROUP, orgGroups);
			crit12.and(crit10, crit11);

			crit13.or(crit3, crit6, crit9, crit12);
			crit13.add();

			List<ProjectTemplateBenefit> ptbList = hcu.list();
			if (!ptbList.isEmpty())
				for (ProjectTemplateBenefit ptb : ptbList) {
					if (donePTBs.contains(ptb)) //dont create the same project twice just because more than one bj qualifies for it
						continue;

					BProject bp = new BProject();
					bp.create();
					bp.setProjectCategoryId(ptb.getProjectCategoryId());
					bp.setProjectStatusId(ptb.getProjectStatusId());
					bp.setProjectTypeId(ptb.getProjectTypeId());
					bp.setBillable('N');
					bp.setDoneForPersonId(emp.getPersonId());

					/* XXYY
					for (ProjectTemplateBenefitAssignment pta : hsu.createCriteria(ProjectTemplateBenefitAssignment.class).eq(ProjectTemplateBenefitAssignment.PROJECT_TEMPLATE_BENEFIT, ptb).list())
						bp.assignPerson(pta.getPersonId(), 10000, false);
					 */

					BRouteTypeAssoc rta = new BRouteTypeAssoc(ptb.getProjectCategoryId(), ptb.getProjectTypeId());
					if (!isEmpty(rta.getRouteId())) {
						BRoute rt = new BRoute(rta.getRouteId());
						if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
							if (isEmpty(bp.getProjectStatusId()))
								bp.setProjectStatusId(rt.getProjectStatusId());
							bp.setRouteStopId(rt.getRouteStop().getRouteStopId());
						}
					}

					bp.setRequestingOrgGroupId(isEmpty(emp.getOrgGroupId()) ? emp.getCompany().getCompanyId() : emp.getOrgGroupId());
					bp.setDescription(ptb.getProjectDescription());

					List<BWizardProject> wpl = new ArrayList<BWizardProject>();
					//now handle description and wizard projects
					if (ptb.getBenefit() == null && ptb.getBcr() != null) {
						String allBenefitsDesc = emp.getNameFML() + "\n\n";
						BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(bbj.getBenefitChangeReasonId());
						allBenefitsDesc += bcr.getDescription() + "\n    " + bbj.getEmployeeExplanation() + "\n\n";

						BWizardProject wpp = new BWizardProject();
						wpp.create();
						wpp.setProject(bp.getBean());
						wpp.setBenefitJoin(j);
						wpp.setProjectAction("C");
						wpl.add(wpp);

						for (HrBenefitJoin j2 : joins) {
							BHRBenefitJoin bj2 = new BHRBenefitJoin(j2);
							allBenefitsDesc += BWizardConfiguration.getBenefitJoinDescription(bj2.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

							BWizardProject wp = new BWizardProject();
							wp.create();
							wp.setProject(bp.getBean());
							wp.setBenefitJoin(j2);
							wp.setProjectAction("A");
							wpl.add(wp);

						}
						bp.setDetailDesc(allBenefitsDesc);
					} else if (ptb.getBenefit() != null && ptb.getBcr() == null) {
						String allBenefitsDesc = emp.getNameFML() + "\n\n";
						List<String> bcrl = new ArrayList<String>();
						for (HrBenefitJoin j2 : joins) {
							BHRBenefitJoin bj2 = new BHRBenefitJoin(j2);
							if (!bcrl.contains(bj2.getBenefitChangeReasonId())) {
								BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(bj2.getBenefitChangeReasonId());
								allBenefitsDesc += bcr.getDescription() + "\n    " + bj2.getEmployeeExplanation() + "\n\n";
								BWizardProject wpp = new BWizardProject();
								wpp.create();
								wpp.setProject(bp.getBean());
								wpp.setBenefitJoin(j2);
								wpp.setProjectAction("C");
								wpl.add(wpp);
								bcrl.add(bj2.getBenefitChangeReasonId());
							}
						}
						allBenefitsDesc += BWizardConfiguration.getBenefitJoinDescription(bbj.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

						BWizardProject wp = new BWizardProject();
						wp.create();
						wp.setProject(bp.getBean());
						wp.setBenefitJoin(j);
						wp.setProjectAction("A");
						wpl.add(wp);

						bp.setDetailDesc(allBenefitsDesc);
					} else  if (ptb.getBenefit() != null && ptb.getBcr() != null) {  //both not null
						String allBenefitsDesc = emp.getNameFML() + "\n\n";
						BHRBenefitChangeReason bcr = new BHRBenefitChangeReason(bbj.getBenefitChangeReasonId());
						allBenefitsDesc += bcr.getDescription() + "\n    " + bbj.getEmployeeExplanation() + "\n\n";
						allBenefitsDesc += BWizardConfiguration.getBenefitJoinDescription(bbj.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

						BWizardProject wpp = new BWizardProject();
						wpp.create();
						wpp.setProject(bp.getBean());
						wpp.setBenefitJoin(j);
						wpp.setProjectAction("C");
						wpl.add(wpp);

						BWizardProject wp = new BWizardProject();
						wp.create();
						wp.setProject(bp.getBean());
						wp.setBenefitJoin(j);
						wp.setProjectAction("A");
						wpl.add(wp);

						bp.setDetailDesc(allBenefitsDesc);
					} else {  //  both null
						String allBenefitsDesc = emp.getNameFML() + "\n\n";
						List<String> bcrl = new ArrayList<String>();
						for (HrBenefitJoin j2 : joins) {
							BHRBenefitJoin bj2 = new BHRBenefitJoin(j2);
							if (!bcrl.contains(bj2.getBenefitChangeReasonId())) {
								BHRBenefitChangeReason bcr2 = new BHRBenefitChangeReason(bj2.getBenefitChangeReasonId());
								allBenefitsDesc += bcr2.getDescription() + "\n    " + bj2.getEmployeeExplanation() + "\n\n";
								BWizardProject wpp = new BWizardProject();
								wpp.create();
								wpp.setProject(bp.getBean());
								wpp.setBenefitJoin(j2);
								wpp.setProjectAction("C");
								wpl.add(wpp);
								bcrl.add(bj2.getBenefitChangeReasonId());
							}
						}
						allBenefitsDesc += BWizardConfiguration.getBenefitJoinDescription(bbj.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

						for (HrBenefitJoin j2 : joins) {
							BHRBenefitJoin bj2 = new BHRBenefitJoin(j2);
							allBenefitsDesc += BWizardConfiguration.getBenefitJoinDescription(bj2.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

							BWizardProject wp = new BWizardProject();
							wp.create();
							wp.setProject(bp.getBean());
							wp.setBenefitJoin(j2);
							wp.setProjectAction("A");
							wpl.add(wp);
						}
						bp.setDetailDesc(allBenefitsDesc);
					}
					bp.insert();
					donePTBs.add(ptb);
					createdProjects.add(bp.getBean());

					for (BWizardProject bwp : wpl)
						bwp.insert();
				}
			else
				notDoneBenefits.add(bbj);
		}

		Project pp = hsu.createCriteria(Project.class).eq(Project.PROJECTSTATUS, getProjectStatus()).eq(Project.PROJECTCATEGORY, getProjectCategory()).eq(Project.PROJECTTYPE, getProjectType()).eq(Project.DONE_FOR_PERSON, emp.getPerson()).first();
		BProject bp;
		if (pp == null) {
			//NOW CREATE THE WIZARD CONFIGURATOR DEFAULT PROJECT
			bp = new BProject();
			bp.create();
			bp.setProjectCategoryId(this.getProjectCategory().getProjectCategoryId());
			bp.setProjectStatusId(this.getProjectStatus().getProjectStatusId());
			bp.setProjectTypeId(this.getProjectType().getProjectTypeId());
			bp.setBillable('N');
			bp.setDescription(this.getProjectSummary());
			bp.setRequestingOrgGroupId(isEmpty(emp.getOrgGroupId()) ? emp.getCompany().getCompanyId() : emp.getOrgGroupId());
			bp.setDoneForPersonId(emp.getPersonId());
			bp.insert();
			hsu.flush();
		} else {
			bp = new BProject(pp);

			//be sure to get rid of existing incompleted wizard project connections as they should not exist anymore
			//and we don't want to create duplicates
			for (BWizardProject bwp : bp.getWizardProjects(true))
				bwp.delete();
		}

		//assign the main project if they aren't already assigned to it
		/* XXYY
		for (WizardConfigurationProjectAssignment pta : hsu.createCriteria(WizardConfigurationProjectAssignment.class).eq(WizardConfigurationProjectAssignment.WIZARD_CONFIG, this.getBean()).list()) {
			boolean alreadyAssigned = false;
			for (Person bap : bp.getAssignedPersons2(null))
				if (bap.getPersonId().equals(pta.getPersonId()))
					alreadyAssigned = true;
			if (!alreadyAssigned)
				bp.assignPerson(pta.getPersonId(), 10000, false);
		}
		 */

		BRouteTypeAssoc rta = new BRouteTypeAssoc(this.getProjectCategory().getProjectCategoryId(), this.getProjectType().getProjectTypeId());
		if (!isEmpty(rta.getRouteId())) {
			BRoute rt = new BRoute(rta.getRouteId());
			if (rt.hasInitialRouteStop() && rt.hasInitialStatus()) {
				if (isEmpty(bp.getProjectStatusId()))
					bp.setProjectStatusId(rt.getProjectStatusId());
				bp.setRouteStopId(rt.getRouteStop().getRouteStopId());
			}
		}

		String detailDesc = bp.getDetailDesc();
		if (detailDesc == null  ||  "".equals(detailDesc))
			detailDesc = emp.getNameFML() + "\n\n";

		for (BHRBenefitJoin bj : notDoneBenefits)
			detailDesc += BWizardConfiguration.getBenefitJoinDescription(bj.getPolicyAndDependentBenefitJoins(false)) + "\n\n";

		boolean firstProject = true;
		for (Project p : createdProjects) {
			if (bp.getBean() == p)
				continue;
			if (firstProject) {
				firstProject = false;
				detailDesc += "Other Projects Created:\n";
			}
			detailDesc += "   " + p.getDescription() + " (" + p.getProjectType().getCode() + ")\n";
		}

		bp.setDetailDesc(detailDesc);
		bp.update();
		hsu.flush();

		//make sure the person change request is linked to the correct project
		new BPerson(emp.getPerson()).fixChangeRequestProject(bp.getProjectId());

		//create these wizard projects after inserting the project
		BWizardProject wp = new BWizardProject();
		wp.create();
		wp.setProject(bp.getBean());
		wp.setProjectAction("D");
		wp.insert();

		for (BHRBenefitJoin bj : notDoneBenefits) {
			wp = new BWizardProject();
			wp.create();
			wp.setProject(bp.getBean());
			wp.setBenefitJoin(bj.getBean());
			wp.setProjectAction("A");
			wp.insert();
		}
	}

	public static String getBenefitJoinDescription(BHRBenefitJoin[] bjlst) {
		String policyMessage = "";
		String dependentMessage = "";
		String benefitMessage = "Enrollment Change\n";
		String endline = "\n";
		String terminateMessage = "";
		for (BHRBenefitJoin bj : bjlst) {
			BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
			//List personList = info.getPersons().get(info.getBenefitJoinList().indexOf(j));
			String coveredPersonId = bj.getCoveredPersonId();
			String payingPersonId = bj.getPayingPersonId();
			int bcrDate = bj.getChangeReasonDate();
			BPerson coveredPerson = new BPerson(coveredPersonId);

			if (coveredPersonId.equals(payingPersonId)) {
				if (bj.getEmployeeCovered().equals("Y"))
					policyMessage = "   Employee: " + coveredPerson.getNameLFM() + endline
							+ "   Employee SSN: " + coveredPerson.getSsn() + endline
							+ "   Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
							+ "   Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
							+ "   Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
							+ "   Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
							+ "   Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
							+ "   Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
							+ "   Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + //QE date
							"";
			} else
				dependentMessage += "   Covered Person: " + coveredPerson.getNameLFM() + endline
						+ "   Covered SSN: " + coveredPerson.getSsn() + endline
						+ "   Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
						+ "   Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
						+ "   Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
						+ "   Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
						+ "   Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
						+ "   Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
						+ "   Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + endline + //QE date
						"";
			if (benefitMessage.equals("Enrollment Change\n")) {
				BHRBenefitCategory bcat = new BHRBenefitCategory(bj.getBenefitCategoryId());
				BEmployee be = new BEmployee(payingPersonId);
				String sb = new String();
				if (!isEmpty(bj.getBenefitId())) {
					BHRBenefit bh = new BHRBenefit(bj.getBenefitId());

					sb += benefitMessage;
					sb += ("   Transaction Type: " + bj.getRecordChangeType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
					if (bh.getBean().getProvider() != null)
						sb += ("   Carrier: " + bh.getBean().getProvider().getName()) + endline; //Carrier
					else
						sb += ("   Carrier: (Not Specified)") + endline;
					sb += ("   Policy Owner: " + be.getNameFML()) + endline;
					sb += ("   Policy Owner SSN: " + be.getSsn()) + endline;
					if (bj.getBenefitConfig() != null) {
						sb += ("   Benefit: " + bj.getBenefitConfig().getBenefitName()) + endline;
						sb += ("   Level: " + bj.getBenefitConfigName()) + endline;
					} else {
						sb += ("   Benefit: " + bj.getBenefitName()) + endline;
						sb += ("   Level: " + "DECLINE") + endline;
					}
					sb += ("   Policy: " + bh.getGroupId()) + endline;
					sb += ("   Plan: " + bh.getPlan()) + endline;
					sb += ("   Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
					sb += ("   Plan Name: " + bh.getPlanName()) + endline;
				} else //must be a category decline
				{
					sb += benefitMessage;
					sb += ("   Benefit Category: " + bj.getBenefitCategoryName()) + endline;
					sb += ("   Level: " + "DECLINE") + endline;
				}
				if (bj.getUsingCOBRA()) {
					sb += ("   Cobra: " + "Yes") + endline;
					sb += ("   Accepted Cobra Date: " + DateUtils.getDateFormatted(bj.getAcceptedDateCOBRA())) + endline;
					sb += ("   Max Cobra Months:" + bj.getMaxMonthsCOBRA()) + endline;
				}

				sb += ("   DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
				sb += ("   Email: " + be.getPersonalEmail()) + endline;
				if (ArahantSession.multipleCompanySupport)
					sb += ("   Company: " + be.getCompanyName()) + endline;

				benefitMessage = sb;

				if (!bcat.getAllowsMultipleBenefits()) {
					HrBenefitJoin tbj = bj.getPayingPerson().getApprovedBenefitJoinOf(bcat);
					BHRBenefitJoin btbj = new BHRBenefitJoin(tbj);
					if (tbj != null && tbj.getHrBenefitConfig() != null) {
						terminateMessage += "Terminating Benefit Enrollment: " + endline;
						terminateMessage += "   Benefit: " + btbj.getBenefitName() + endline;
						terminateMessage += "   Configuration: " + btbj.getBenefitConfigName() + endline;
						terminateMessage += "   Termination Date: " + DateUtils.getDateFormatted(DateUtils.addDays(bj.getPolicyStartDate(), -1)) + endline;

						List<HrBenefitJoin> djs = btbj.getDependentBenefitJoins();
						if (djs.isEmpty())
							terminateMessage += "   Covered Person: " + btbj.getCoveredPerson().getPerson().getNameFL();
						else if (djs.size() > 0)
							terminateMessage += "   Covered People: " + btbj.getCoveredPerson().getPerson().getNameFL();
						for (HrBenefitJoin dj : djs)
							terminateMessage += ", " + dj.getCoveredPerson().getNameFL();
						terminateMessage += endline;
					}
				}
			}
		}
		return benefitMessage + "   ---------------------------  \n" + policyMessage + "\n" + dependentMessage + "\n" + terminateMessage;
	}

	public static String getBenefitJoinDescription(BHRBenefitJoinH[] bjlst) {
		String policyMessage = "";
		String dependentMessage = "";
		String benefitMessage = "Enrollment Change\n";
		String endline = "\n";
		for (BHRBenefitJoinH bj : bjlst) {
			BHRBenefitChangeReason bc = new BHRBenefitChangeReason(bj.getBenefitChangeReasonId());
			//List personList = info.getPersons().get(info.getBenefitJoinList().indexOf(j));
			String coveredPersonId = bj.getCoveredPersonId();
			String payingPersonId = bj.getPayingPersonId();
			int bcrDate = bj.getChangeReasonDate();
			BPerson coveredPerson = new BPerson(coveredPersonId);

			if (coveredPersonId.equals(payingPersonId)) {
				if (bj.getEmployeeCovered().equals("Y"))
					policyMessage = "   Employee: " + coveredPerson.getNameLFM() + endline
							+ "   Employee SSN: " + coveredPerson.getSsn() + endline
							+ "   Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
							+ "   Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
							+ "   Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
							+ "   Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
							+ "   Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
							+ "   Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
							+ "   Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + //QE date
							"";
			} else
				dependentMessage += "   Covered Person: " + coveredPerson.getNameLFM() + endline
						+ "   Covered SSN: " + coveredPerson.getSsn() + endline
						+ "   Policy Start: " + DateUtils.getDateFormatted(bj.getPolicyStartDate()) + endline
						+ "   Policy End: " + DateUtils.getDateFormatted(bj.getPolicyEndDate()) + endline
						+ "   Coverage Start: " + DateUtils.getDateFormatted(bj.getCoverageStartDate()) + endline
						+ "   Coverage End: " + DateUtils.getDateFormatted(bj.getCoverageEndDate()) + endline
						+ "   Coverage Amount: " + MoneyUtils.formatMoney(bj.getAmountCovered()) + endline
						+ "   Qualifying Event: " + (bc.getBean() != null ? bc.getDescription() : " (None)") + endline
						+ "   Qualifying Event date: " + DateUtils.getDateFormatted((bcrDate == 0 ? DateUtils.now() : bcrDate)) + endline + endline + //QE date
						"";
			if (benefitMessage.equals("Enrollment Change\n")) {

				BHRBenefitCategory bcat = new BHRBenefitCategory(bj.getCategoryId());
				BEmployee be = new BEmployee(payingPersonId);
				String sb = new String();
				if (!isEmpty(bj.getBenefitId())) {
					BHRBenefit bh = new BHRBenefit(bj.getBenefitId());

					sb += benefitMessage;
					sb += ("   Transaction Type: " + bj.getRecordChangeType() + endline);//;+ (bj.getCoverageEndDate() > 0 ? "Delete" : bj.getRecordChangeType())) + endline; //Transaction Type
					if (bh.getBean().getProvider() != null)
						sb += ("   Carrier: " + bh.getBean().getProvider().getName()) + endline; //Carrier
					else
						sb += ("   Carrier: (Not Specified)") + endline;
					sb += ("   Policy Owner: " + be.getNameFML()) + endline;
					sb += ("   Policy Owner SSN: " + be.getSsn()) + endline;
					if (bj.getBenefitConfig() != null) {
						sb += ("   Benefit: " + bj.getBenefitConfig().getBenefitName()) + endline;
						sb += ("   Level: " + bj.getBenefitConfigName()) + endline;
					} else if (!isEmpty(bj.getBean().getHrBenefitId())) {
						sb += ("   Benefit: " + bj.getBenefitName()) + endline;
						sb += ("   Level: " + "DECLINE") + endline;
					}
					sb += ("   Policy: " + bh.getGroupId()) + endline;
					sb += ("   Plan: " + bh.getPlan()) + endline;
					sb += ("   Sub Group: " + bh.getSubGroupId()) + endline;		//Sub Group
					sb += ("   Plan Name: " + bh.getPlanName()) + endline;
				} else {  //must be a category decline
					sb += ("   Benefit Category: " + (new BHRBenefitCategory(bj.getCategoryId()).getDescription())) + endline;
					sb += ("   Level: " + "DECLINE") + endline;
				}
				if (bj.getUsingCOBRA()) {
					sb += ("   Cobra: " + "Yes") + endline;
					sb += ("   Accepted Cobra Date: " + DateUtils.getDateFormatted(bj.getAcceptedDateCOBRA())) + endline;
					sb += ("   Max Cobra Months:" + bj.getMaxMonthsCOBRA()) + endline;
				}
				sb += ("   DOB: " + DateUtils.getDateFormatted(be.getDob())) + endline;
				sb += ("   Email: " + be.getPersonalEmail()) + endline;
				if (ArahantSession.multipleCompanySupport)
					sb += ("   Company: " + be.getCompanyName()) + endline;

				benefitMessage = sb;

				//BHRBenefitCategory bcat = new BHRBenefitCategory(bh.getBenefitCategoryId());
//				if(!bcat.getAllowsMultipleBenefits())
//				{
//					HrBenefitJoin tbj = bj.getPayingPerson().getApprovedBenefitJoinOf(bcat);
//					BHRBenefitJoin btbj = new BHRBenefitJoin(tbj);
//					if(tbj != null && tbj.getHrBenefitConfig() != null)
//					{
//						terminateMessage += "Terminating Benefit Enrollment: " + endline;
//						terminateMessage += "   Benefit: " + btbj.getBenefitName() + endline;
//						terminateMessage += "   Configuration: " + btbj.getBenefitConfigName() + endline;
//						terminateMessage += "   Termination Date: " + DateUtils.getDateFormatted(DateUtils.addDays(bj.getPolicyStartDate(), -1)) + endline;
//
//						List<HrBenefitJoin> djs = btbj.getDependentBenefitJoins();
//						if(djs.size() == 0)
//						{
//							terminateMessage += "   Covered Person: " + btbj.getCoveredPerson().getPerson().getNameFL();
//						}
//						else if(djs.size() > 0)
//						{
//							terminateMessage += "   Covered People: " + btbj.getCoveredPerson().getPerson().getNameFL();
//						}
//						for(HrBenefitJoin dj : djs)
//						{
//							terminateMessage += ", " + dj.getCoveredPerson().getNameFL();
//						}
//						terminateMessage += endline;
//					}
//				}
			}
		}
		return benefitMessage + "   ---------------------------  \n" + policyMessage + "\n" + dependentMessage + "\n";// + terminateMessage;
	}
}
