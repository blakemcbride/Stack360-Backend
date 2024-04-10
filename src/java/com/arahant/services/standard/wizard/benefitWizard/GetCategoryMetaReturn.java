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


/**
 *
 */
package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateCriterionUtil;
import com.arahant.utils.StandardProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GetCategoryMetaReturn extends TransmitReturnBase {

	private QuestionAnswerItem[] questions;
	private GetCategoryMetaReturnItem item[];
	private String categoryDescriptionHTML;
	private String categoryNameHTML;
	private boolean allowMultipleBenefits;
	private boolean enrolledInConfig;
	private String instructions;
	private boolean requiresDecline;
	private String enrolledBenefitId;
	private int dependentCount;
	private boolean currentEnrollmentReplaced = false;
	private String avatarUrl;
	private int cap = BProperty.getInt(StandardProperty.SEARCH_MAX);

	void setData(BHRBenefitCategory bc, String employeeId, int asOfDate, BWizardConfiguration wizConf, String bcrId) {
		WizardConfigurationCategory wcc = ArahantSession.getHSU().createCriteria(WizardConfigurationCategory.class).eq(WizardConfigurationCategory.CATEGORY, bc.getBean()).eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, wizConf.getBean()).first();
		BWizardConfigurationCategory bwcc = new BWizardConfigurationCategory(wcc);

		BEmployee be = new BEmployee(employeeId);
		dependentCount = be.getDependents().length;
		categoryNameHTML = "<font size='14'><b>" + bwcc.getDescription() + "</b></font>";

//		if (bc.getDescription() == null) //Description and CategoryName are the same field
		categoryDescriptionHTML = "";
//		else
//			categoryDescriptionHTML  = "<font size='11'>" + bc.getDescription() + "</font>";

		allowMultipleBenefits = bc.getAllowsMultipleBenefits();

		enrolledInConfig = false;

		enrolledBenefitId = "";

		if (!allowMultipleBenefits) {
			BPerson bpp = new BPerson();
			//bpp.loadPending(employeeId);
			//if there is a pending record, get it
			if (bpp.hasPending(employeeId))
				bpp.loadPending(employeeId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(employeeId); //bpp = new BPerson(ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, employeeId).first());


			@SuppressWarnings("unchecked")
			List<String> configIds = (List) ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bc.getBean()).list();

			for (String s : configIds)
				if (enrolledInConfig = BBenefitEnrollments.enrolledInPendingConfig(s, new String[]{employeeId})) {
					enrolledBenefitId = new BHRBenefitConfig(s).getBenefit().getBenefitId();
					currentEnrollmentReplaced = new BHRBenefit(enrolledBenefitId).getReplacingBenefit() != null;
					break;
				} else if (enrolledInConfig = BBenefitEnrollments.enrolledInApprovedConfig(s, new String[]{employeeId})) {
					enrolledBenefitId = new BHRBenefitConfig(s).getBenefit().getBenefitId();
					currentEnrollmentReplaced = new BHRBenefit(enrolledBenefitId).getReplacingBenefit() != null;
					break;
				}
		}

		instructions = bwcc.getInstructions();

		requiresDecline = bc.getRequiresDecline();

		List<WizardConfigurationBenefit> benesInWizConf = ArahantSession.getHSU().createCriteria(WizardConfigurationBenefit.class).eq(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY, wcc).list();

		//are they not allowed to change benefits within this category?
		if (ArahantSession.getHSU().createCriteria(BenefitRestriction.class).eq(BenefitRestriction.BENEFIT_CATEGORY, wcc.getBenefitCategory()).eq(BenefitRestriction.BENEFIT_CHANGE_REASON_ID, bcrId).exists()) {
			HrBenefitConfig c = be.getBenefitOf(wcc.getBenefitCategory());
			if (c != null) {
				HrBenefit b = c.getHrBenefit();
				List<WizardConfigurationBenefit> removals = new ArrayList<WizardConfigurationBenefit>();
				for (WizardConfigurationBenefit wbb : benesInWizConf)
					if (wbb.getBenefit() != b)
						removals.add(wbb);
				benesInWizConf.removeAll(removals);
			}
		}

		{
			HrEmplDependent spouse = be.getSpouse();
			Collection children = be.getChildren();
			setItem(BWizardConfigurationBenefit.makeArray(benesInWizConf), employeeId, spouse != null, !children.isEmpty());
		}

		avatarUrl = bwcc.getAvatarPath();

		setQuestions(ArahantSession.getHSU().createCriteria(HrBenefitCategoryQuestion.class).eq(HrBenefitCategoryQuestion.BENEFIT_CAT, bc.getBean()).orderBy(HrBenefitCategoryQuestion.SEQ).list());

	}

	void setData(BHRBenefitCategory bc, String employeeId, int asOfDate) {
		BEmployee be = new BEmployee(employeeId);
		dependentCount = be.getDependents().length;
		categoryNameHTML = "<font size='14'><b>" + bc.getCategoryName() + "</b></font>";

//		if (bc.getDescription() == null) //Description and CategoryName are the same field
		categoryDescriptionHTML = "";
//		else
//			categoryDescriptionHTML  = "<font size='11'>" + bc.getDescription() + "</font>";

		allowMultipleBenefits = bc.getAllowsMultipleBenefits();

		enrolledInConfig = false;

		enrolledBenefitId = "";

		if (!allowMultipleBenefits) {
			BPerson bpp = new BPerson();
			//bpp.loadPending(employeeId);
			//if there is a pending record, get it
			if (bpp.hasPending(employeeId))
				bpp.loadPending(employeeId);
			else
				//otherwise, get the Real person record
				bpp.loadRealPerson(employeeId); //bpp = new BPerson(ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'R').eq(Person.PERSONID, employeeId).first());


			@SuppressWarnings("unchecked")
			List<String> configIds = (List) ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFIT_CATEGORY, bc.getBean()).list();

			for (String s : configIds)
				if (enrolledInConfig = BBenefitEnrollments.enrolledInPendingConfig(s, new String[]{employeeId})) {
					enrolledBenefitId = new BHRBenefitConfig(s).getBenefit().getBenefitId();
					currentEnrollmentReplaced = new BHRBenefit(enrolledBenefitId).getReplacingBenefit() != null;
					break;
				} else if (enrolledInConfig = BBenefitEnrollments.enrolledInApprovedConfig(s, new String[]{employeeId})) {
					enrolledBenefitId = new BHRBenefitConfig(s).getBenefit().getBenefitId();
					currentEnrollmentReplaced = new BHRBenefit(enrolledBenefitId).getReplacingBenefit() != null;
					break;
				}
		}

		instructions = bc.getInstructions();

		requiresDecline = bc.getRequiresDecline();

		HibernateCriteriaUtil<HrBenefit> bhcu = ArahantSession.getHSU().createCriteria(HrBenefit.class).orderBy(HrBenefit.SEQ).eq(HrBenefit.BENEFIT_CATEGORY, bc.getBean()).eq(HrBenefit.OPEN_ENROLLMENT_WIZARD, 'Y').dateInside(HrBenefit.START_DATE, HrBenefit.END_DATE, asOfDate);

		//Don't forget where this code is because you'll want to use it in a lot of places.
		if (be.getBenefitClass() != null) {
			HibernateCriterionUtil orcriBen = bhcu.makeCriteria();
			HibernateCriterionUtil orcriAll = bhcu.makeCriteria();
			HibernateCriterionUtil cri1Ben = bhcu.makeCriteria();

			HibernateCriteriaUtil classHcuBen = bhcu.leftJoinTo("benefitClasses", "classesBen");

			HibernateCriterionUtil cri2Ben = classHcuBen.makeCriteria();

			cri1Ben.sizeEq("benefitClasses", 0);
			cri2Ben.eq("classesBen." + BenefitClass.BENEFIT_CLASS_ID, be.getBenefitClassId());

			orcriBen.or(cri1Ben, cri2Ben);

			HibernateCriteriaUtil chcu = bhcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS);
			//either has no classes specified or they match
			HibernateCriterionUtil orcri = chcu.makeCriteria();
			HibernateCriterionUtil cri1 = chcu.makeCriteria();

			HibernateCriteriaUtil classHcu = chcu.leftJoinTo(HrBenefitConfig.BENEFIT_CLASSES, "classes");

			HibernateCriterionUtil cri2 = classHcu.makeCriteria();

			cri1.sizeEq(HrBenefitConfig.BENEFIT_CLASSES, 0);
			cri2.eq("classes." + BenefitClass.BENEFIT_CLASS_ID, be.getBenefitClass().getBenefitClassId());

			orcri.or(cri1, cri2);
			orcriAll.or(orcri, orcriBen);
			orcriAll.add();
		}

		setItem(BHRBenefit.makeArray(bhcu.list()));

		avatarUrl = bc.getAvatarPath();
	}

	public QuestionAnswerItem[] getQuestions() {
		return questions;
	}

	public void setQuestions(QuestionAnswerItem[] questions) {
		this.questions = questions;
	}

	public void setQuestions(List<HrBenefitCategoryQuestion> q) {
		this.questions = new QuestionAnswerItem[q.size()];
		for (int loop = 0; loop < q.size(); loop++)
			this.questions[loop] = new QuestionAnswerItem(q.get(loop));
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public void setCap(int x) {
		cap = x;
	}

	public int getCap() {
		return cap;
	}

	/**
	 * @return Returns the item.
	 */
	public GetCategoryMetaReturnItem[] getItem() {
		return item;
	}

	/**
	 * @param item The item to set.
	 */
	public void setItem(final GetCategoryMetaReturnItem[] item) {
		this.item = item;
	}

	private void setItem(final BWizardConfigurationBenefit[] a, String employeeId, boolean hasSpouse, boolean hasChildren) {
		item = new GetCategoryMetaReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++) {
//			boolean supportsEmployee=false, supportsSpouse=false, supportsChildren=false;
			BWizardConfigurationBenefit wc = a[loop];
//			HrBenefit ben = wc.getBenefit();
//			Set<HrBenefitConfig> bcSet = ben.getBenefitConfigs();
//			for (HrBenefitConfig bc : bcSet) {
//				if (bc.getEmployee() == 'Y') {
//					supportsEmployee = true;
//					break;
//				}
//				if (bc.getSpouseNonEmployee() == 'Y'  ||  bc.getSpouseNonEmpOrChildren() == 'Y'  ||  bc.getSpouseEmpOrChildren() == 'Y'  ||  bc.getSpouseEmployee() == 'Y')
//					supportsSpouse = true;
//				if (bc.getChildren() == 'Y' ||  bc.getSpouseNonEmpOrChildren() == 'Y'  ||  bc.getSpouseEmpOrChildren() == 'Y')
//					supportsChildren = true;
//			}
//			if (supportsEmployee  ||  supportsSpouse  &&  hasSpouse  ||  supportsChildren  &&  hasChildren)
				item[loop] = new GetCategoryMetaReturnItem(wc, employeeId);
		}
	}

	private void setItem(final BHRBenefit[] a) {
		item = new GetCategoryMetaReturnItem[a.length];
		for (int loop = 0; loop < a.length; loop++)
			item[loop] = new GetCategoryMetaReturnItem(a[loop]);
	}

	public String getCategoryDescriptionHTML() {
		return categoryDescriptionHTML;
	}

	public void setCategoryDescriptionHTML(String categoryDescriptionHTML) {
		this.categoryDescriptionHTML = categoryDescriptionHTML;
	}

	public String getCategoryNameHTML() {
		return categoryNameHTML;
	}

	public void setCategoryNameHTML(String categoryNameHTML) {
		this.categoryNameHTML = categoryNameHTML;
	}

	public boolean getAllowMultipleBenefits() {
		return allowMultipleBenefits;
	}

	public void setAllowMultipleBenefits(boolean allowMultipleBenefits) {
		this.allowMultipleBenefits = allowMultipleBenefits;
	}

	public boolean getEnrolledInConfig() {
		return enrolledInConfig;
	}

	public void setEnrolledInConfig(boolean enrolledInConfig) {
		this.enrolledInConfig = enrolledInConfig;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean getRequiresDecline() {
		return requiresDecline;
	}

	public void setRequiresDecline(boolean requiresDecline) {
		this.requiresDecline = requiresDecline;
	}

	public String getEnrolledBenefitId() {
		return enrolledBenefitId;
	}

	public void setEnrolledBenefitId(String enrolledBenefitId) {
		this.enrolledBenefitId = enrolledBenefitId;
	}

	public int getDependentCount() {
		return dependentCount;
	}

	public void setDependentCount(int dependentCount) {
		this.dependentCount = dependentCount;
	}

	public boolean getCurrentEnrollmentReplaced() {
		return currentEnrollmentReplaced;
	}

	public void setCurrentEnrollmentReplaced(boolean currentEnrollmentReplaced) {
		this.currentEnrollmentReplaced = currentEnrollmentReplaced;
	}
}
