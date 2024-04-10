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


package com.arahant.services.standard.wizard.benefitWizard;

import com.arahant.beans.BenefitDependency;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.*;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import java.util.List;

public class GetBenefitMetaFidelityReturn extends TransmitReturnBase {

	private boolean contingentBeneficiaries;
	private String benefitNameHTML;
	private String benefitName;
	private String benefitDescriptionHTML;
	private String instructions;
	private String avatarUrl;
	private boolean reloadWizard;
	private boolean reloadWizardAfterDecline;
	private String configId;
	private double coverageAmount;
	private String benefitJoinId;
	private double maxCoverage;
	private double minCoverage;
	private double stepCoverage;
	private String optionalBenefitId;
	private int dependentCount;
	private String spouseId;
	private boolean coversSpouse;
	private String spouseName;
	private String spouseRelationshipId;

	void setData(BWizardConfigurationBenefit wcb, String empId) {
		BHRBenefit b = new BHRBenefit(wcb.getBenefit());
		BEmployee be = new BEmployee(empId);

		benefitNameHTML = "<font size='14'><b>" + wcb.getName() + "</b></font>";
		benefitName = wcb.getName();
		com.arahant.beans.BenefitRider br = ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, b.getBenefitId()).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').first();
		if (br != null)
			optionalBenefitId = br.getRiderBenefitId();

		if (b.getDescription() == null)
			benefitDescriptionHTML = "";
		else
			benefitDescriptionHTML = "<font size='11'>" + b.getDescription() + "</font>";

		instructions = wcb.getInstructions();
		avatarUrl = wcb.getAvatarPath();
		spouseId = be.getSpouse(false) == null ? "" : be.getSpouse(false).getDependentId();
		spouseRelationshipId = be.getSpouse(false) == null ? "" : be.getSpouse(false).getRelationshipId();
		spouseName = be.getSpouse(false) == null ? "" : be.getSpouse(false).getPerson().getFname() + " " + be.getSpouse(false).getPerson().getLname();

		List<BenefitDependency> bdrl = BBenefitDependency.getBenefitDependenciesWhereRequired(b);
		if (bdrl.size() > 0)
			for (BenefitDependency bd : bdrl) {
				List<BenefitDependency> bdl2 = BBenefitDependency.getBenefitDependenciesWhereDependent(new BHRBenefit(bd.getDependentBenefit()));
				boolean thisDependentBenefitMeetsRequirement = false;
				for (BenefitDependency depBen : bdl2)
					//is there an unapproved enrollment in the required benefit?
					if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
					//is there an approved enrollment in the required benefit and no unapproved decline?
					else if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists()
							&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
				if (!thisDependentBenefitMeetsRequirement)
					setReloadWizard(true);
				else
					setReloadWizardAfterDecline(true);
			}

		List<HrBenefitConfig> bcl = b.getConfigs();
		if (bcl.size() == 1) {
			HrBenefitConfig c = b.getConfigs().get(0);
			configId = c.getBenefitConfigId();
			coversSpouse = (c.getSpouseEmployee() == 'Y' || c.getSpouseNonEmployee() == 'Y');
			maxCoverage = c.deprecatedGetMaxAmount();
			minCoverage = c.deprecatedGetMinValue();
			stepCoverage = c.deprecatedGetStepValue();

			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).first();
			if (bj != null) {
				benefitJoinId = bj.getBenefitJoinId();
				coverageAmount = bj.getAmountCovered();
				List<String> excludeRiders = (List) ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').list();
				setBenefitRiders(ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).notIn(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID, excludeRiders).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, b.getBenefitId()).list(), new BHRBenefitJoin(benefitJoinId), DateUtils.now());
			}
		}
		contingentBeneficiaries = b.getHasContingentBeneficiariesBool();
		dependentCount = be.getDependentCount();

	}

	void setData2(BHRBenefit b, String empId) {
		BEmployee be = new BEmployee(empId);
		dependentCount = be.getDependentCount();//be.getDependents().length;
		benefitNameHTML = "<font size='14'><b>" + b.getName() + "</b></font>";

		benefitName = b.getName();
		if (b.getDescription() == null)
			benefitDescriptionHTML = "";
		else
			benefitDescriptionHTML = "<font size='11'>" + b.getDescription() + "</font>";

		instructions = b.getDescription();
		avatarUrl = "";
		contingentBeneficiaries = b.getHasContingentBeneficiariesBool();
		spouseId = be.getSpouse() == null ? "" : be.getSpouse().getRelationshipId();

		List<BenefitDependency> bdrl = BBenefitDependency.getBenefitDependenciesWhereRequired(b);
		if (bdrl.size() > 0)
			for (BenefitDependency bd : bdrl) {
				List<BenefitDependency> bdl2 = BBenefitDependency.getBenefitDependenciesWhereDependent(new BHRBenefit(bd.getDependentBenefit()));
				boolean thisDependentBenefitMeetsRequirement = false;
				for (BenefitDependency depBen : bdl2)
					//is there an unapproved enrollment in the required benefit?
					if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
					//is there an approved enrollment in the required benefit and no unapproved decline?
					else if (ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'Y').joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, depBen.getRequiredBenefit()).exists()
							&& !ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eq(HrBenefitJoin.APPROVED, 'N').eq(HrBenefitJoin.HRBENEFIT, depBen.getRequiredBenefit()).exists())
						thisDependentBenefitMeetsRequirement = true;
				if (!thisDependentBenefitMeetsRequirement)
					setReloadWizard(true);
				else
					setReloadWizardAfterDecline(true);
			}
		List<HrBenefitConfig> bcl = b.getConfigs();
		if (bcl.size() == 1) {
			HrBenefitConfig c = b.getConfigs().get(0);
			configId = c.getBenefitConfigId();
			coversSpouse = (c.getSpouseEmployee() == 'Y' || c.getSpouseNonEmployee() == 'Y');
			maxCoverage = c.deprecatedGetMaxAmount();
			minCoverage = c.deprecatedGetMinValue();
			stepCoverage = c.deprecatedGetStepValue();

			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON_ID, be.getPersonId()).eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON).eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId).first();
			if (bj != null) {
				benefitJoinId = bj.getBenefitJoinId();
				coverageAmount = bj.getAmountCovered();
				List<String> excludeRiders = (List) ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).selectFields(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID).eq(com.arahant.beans.BenefitRider.HIDDEN, 'Y').eq(com.arahant.beans.BenefitRider.REQUIRED, 'N').list();
				setBenefitRiders(ArahantSession.getHSU().createCriteria(com.arahant.beans.BenefitRider.class).notIn(com.arahant.beans.BenefitRider.BENEFIT_RIDER_ID, excludeRiders).eq(com.arahant.beans.BenefitRider.BASE_BENEFIT_ID, b.getBenefitId()).list(), new BHRBenefitJoin(benefitJoinId), DateUtils.now());
			}
		}
	}

	public String getSpouseName() {
		return spouseName;
	}

	public void setSpouseName(String spouseName) {
		this.spouseName = spouseName;
	}

	public boolean getCoversSpouse() {
		return coversSpouse;
	}

	public void setCoversSpouse(boolean coversSpouse) {
		this.coversSpouse = coversSpouse;
	}

	public String getSpouseId() {
		return spouseId;
	}

	public void setSpouseId(String spouseId) {
		this.spouseId = spouseId;
	}
	private BenefitRider[] benefitRiders;

	public BenefitRider[] getBenefitRiders() {
		return benefitRiders;
	}

	public void setBenefitRiders(BenefitRider[] benefitRiders) {
		this.benefitRiders = benefitRiders;
	}

	public void setBenefitRiders(List<com.arahant.beans.BenefitRider> l, BHRBenefitJoin bj, int date) {
		this.benefitRiders = new BenefitRider[l.size()];
		for (int i = 0; i < l.size(); i++)
			benefitRiders[i] = new BenefitRider(new BBenefitRider(l.get(i)), bj, date);
	}

	public int getDependentCount() {
		return dependentCount;
	}

	public void setDependentCount(int dependentCount) {
		this.dependentCount = dependentCount;
	}

	public boolean getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(boolean contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
	}

	public String getOptionalBenefitId() {
		return optionalBenefitId;
	}

	public void setOptionalBenefitId(String optionalBenefitId) {
		this.optionalBenefitId = optionalBenefitId;
	}

	public String getBenefitJoinId() {
		return benefitJoinId;
	}

	public void setBenefitJoinId(String benefitJoinId) {
		this.benefitJoinId = benefitJoinId;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public double getCoverageAmount() {
		return coverageAmount;
	}

	public void setCoverageAmount(double coverageAmount) {
		this.coverageAmount = coverageAmount;
	}

	public double getMaxCoverage() {
		return maxCoverage;
	}

	public void setMaxCoverage(double maxCoverage) {
		this.maxCoverage = maxCoverage;
	}

	public double getMinCoverage() {
		return minCoverage;
	}

	public void setMinCoverage(double minCoverage) {
		this.minCoverage = minCoverage;
	}

	public double getStepCoverage() {
		return stepCoverage;
	}

	public void setStepCoverage(double stepCoverage) {
		this.stepCoverage = stepCoverage;
	}

	public String getBenefitName() {
		return benefitName;
	}

	public void setBenefitName(String benefitName) {
		this.benefitName = benefitName;
	}

	public boolean getReloadWizardAfterDecline() {
		return reloadWizardAfterDecline;
	}

	public void setReloadWizardAfterDecline(boolean reloadWizardAfterDecline) {
		this.reloadWizardAfterDecline = reloadWizardAfterDecline;
	}

	public boolean getReloadWizard() {
		return reloadWizard;
	}

	public void setReloadWizard(boolean reloadWizard) {
		this.reloadWizard = reloadWizard;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getBenefitNameHTML() {
		return benefitNameHTML;
	}

	public void setBenefitNameHTML(String benefitNameHTML) {
		this.benefitNameHTML = benefitNameHTML;
	}

	public String getBenefitDescriptionHTML() {
		return benefitDescriptionHTML;
	}

	public void setBenefitDescriptionHTML(String benefitDescriptionHTML) {
		this.benefitDescriptionHTML = benefitDescriptionHTML;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String information) {
		this.instructions = information;
	}

	public String getSpouseRelationshipId() {
		return spouseRelationshipId;
	}

	public void setSpouseRelationshipId(String spouseRelationshipId) {
		this.spouseRelationshipId = spouseRelationshipId;
	}
	
}
