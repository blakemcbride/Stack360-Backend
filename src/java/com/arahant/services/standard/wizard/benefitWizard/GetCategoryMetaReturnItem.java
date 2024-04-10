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

import com.arahant.beans.BenefitDependency;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BBenefitDependency;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BWizardConfigurationBenefit;
import com.arahant.utils.ArahantSession;
import java.util.List;

public class GetCategoryMetaReturnItem {

	private String benefitName;
	private String benefitId;
	private String benefitDescription;
	private String benefitInstructions;
	private boolean reloadWizard;
	private boolean reloadWizardAfterDecline;

	public GetCategoryMetaReturnItem() {
	}

	GetCategoryMetaReturnItem(BWizardConfigurationBenefit wb, String empId) {
		BHRBenefit b = new BHRBenefit(wb.getBenefit());
		benefitName = wb.getName();
		benefitId = b.getBenefitId();
		benefitDescription = b.getDescription();
		benefitInstructions = wb.getInstructions();
		List<BenefitDependency> bdrl = BBenefitDependency.getBenefitDependenciesWhereRequired(b);
		if (bdrl.size() > 0)
			for (BenefitDependency bd : bdrl) {
				List<BenefitDependency> bdl2 = BBenefitDependency.getBenefitDependenciesWhereDependent(new BHRBenefit(bd.getDependentBenefit()));
				boolean thisDependentBenefitMeetsRequirement = false;
				for (BenefitDependency depBen : bdl2)
					//is there an unapproved enrollment in the required benefit?
					if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
					//is there an approved enrollment in the required benefit and no unapproved decline?
					else if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists()
							&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, empId).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
				if (!thisDependentBenefitMeetsRequirement)
					setReloadWizard(true);
				else
					setReloadWizardAfterDecline(true);
			}
	}

	GetCategoryMetaReturnItem(BHRBenefit b) {
		benefitName = b.getName();
		benefitId = b.getBenefitId();
		benefitDescription = b.getDescription();
		benefitInstructions = b.getAdditionalInstructions();
	}

	public boolean getReloadWizard() {
		return reloadWizard;
	}

	public final void setReloadWizard(boolean reloadWizard) {
		this.reloadWizard = reloadWizard;
	}

	public boolean getReloadWizardAfterDecline() {
		return reloadWizardAfterDecline;
	}

	public final void setReloadWizardAfterDecline(boolean reloadWizardAfterDecline) {
		this.reloadWizardAfterDecline = reloadWizardAfterDecline;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public String getBenefitId() {
		return benefitId;
	}

	public void setBenefitId(String benefitId) {
		this.benefitId = benefitId;
	}

	public String getBenefitDescription() {
		return benefitDescription;
	}

	public void setBenefitDescription(String benefitDescription) {
		this.benefitDescription = benefitDescription;
	}

	public String getBenefitInstructions() {
		return benefitInstructions;
	}

	public void setBenefitInstructions(String benefitInstructions) {
		this.benefitInstructions = benefitInstructions;
	}
}
