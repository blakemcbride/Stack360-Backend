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
import com.arahant.business.BBenefitDependency;
import com.arahant.business.BEmployee;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BWizardConfigurationBenefit;
import com.arahant.services.TransmitReturnBase;
import com.arahant.utils.ArahantSession;
import java.util.ArrayList;
import java.util.List;

public class GetBenefitMetaReturn extends TransmitReturnBase {

	private String dependencyHTML;
	private String benefitNameHTML;
	private String benefitDescriptionHTML;
	private String instructions;
	private boolean hasBeneficiaries;
	private boolean hasPhysicians;
	private boolean contingentBeneficiaries;
	private boolean requiresDecline;
	private int dependentCount;
	private String avatarUrl;
	private boolean autoAssign;
	private boolean reloadWizard;
	private boolean reloadWizardAfterDecline;
	private String employerCostModel;
	private String employeeCostModel;
	private String benefitAmountModel;
	private String declineMessage;

	void setData(BWizardConfigurationBenefit wcb, String empId) {
		BHRBenefit b = new BHRBenefit(wcb.getBenefit());
		BEmployee be = new BEmployee(empId);
		dependentCount = be.getDependentCount();//be.getDependents().length;
		benefitNameHTML = "<font size='14'><b>" + wcb.getName() + "</b></font>";

		if (b.getDescription() == null)
			benefitDescriptionHTML = "";
		else
			benefitDescriptionHTML = "<font size='11'>" + b.getDescription() + "</font>";

		instructions = wcb.getInstructions();
		hasBeneficiaries = b.getHasBeneficiariesBool();
		requiresDecline = b.getRequiresDecline();
		avatarUrl = wcb.getAvatarPath();
		contingentBeneficiaries = b.getHasContingentBeneficiariesBool();
		hasPhysicians = b.getHasPhysicians() && (wcb.getWizardConfigurationCategory().getWizardConfiguration().getPhysicianSelectionMode() == 'I');		
		employerCostModel = b.getEmployerCostModel() + "";
		employeeCostModel = b.getEmployeeCostModel() + "";
		benefitAmountModel = b.getBenefitAmountModel() + "";
		declineMessage = wcb.getDeclineMessage();

		List<BenefitDependency> bdl = BBenefitDependency.getBenefitDependenciesWhereDependent(b);
		if (bdl.size() > 0) {
			List<BenefitDependency> needBenefits = new ArrayList<BenefitDependency>();
			HrBenefitJoin requiredEnrollment = null;
			for (BenefitDependency bd : bdl) {
				HrBenefitJoin j = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, be.getPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).first();
				if (j != null)
					requiredEnrollment = j;
				else
					needBenefits.add(bd);
			}
			if (requiredEnrollment == null) {
				dependencyHTML = "<font size='14'>You do not have the necessary enrollment to be eligible for this benefit. "
						+ " You must have an enrollment in any of the following benefits: ";
				for (BenefitDependency nb : needBenefits)
					dependencyHTML += "<b>" + nb.getRequiredBenefit().getName() + "</b>, ";
				dependencyHTML = dependencyHTML.substring(0, dependencyHTML.lastIndexOf(","));
				dependencyHTML += ".</font>";
			}
		}
		autoAssign = b.getAutoAssignBoolean();

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
	}

	void setData2(BHRBenefit b, String empId) {
		BEmployee be = new BEmployee(empId);
		dependentCount = be.getDependentCount();//be.getDependents().length;
		benefitNameHTML = "<font size='14'><b>" + b.getName() + "</b></font>";

		if (b.getDescription() == null)
			benefitDescriptionHTML = "";
		else
			benefitDescriptionHTML = "<font size='11'>" + b.getDescription() + "</font>";

		instructions = b.getDescription();
		hasBeneficiaries = b.getHasBeneficiariesBool();
		requiresDecline = b.getRequiresDecline();
		avatarUrl = "";
		contingentBeneficiaries = b.getHasContingentBeneficiariesBool();
		hasPhysicians = b.getHasPhysicians();
		employerCostModel = b.getEmployerCostModel() + "";
		employeeCostModel = b.getEmployeeCostModel() + "";
		benefitAmountModel = b.getBenefitAmountModel() + "";
		declineMessage = null;

		List<BenefitDependency> bdl = BBenefitDependency.getBenefitDependenciesWhereDependent(b);
		if (bdl.size() > 0) {
			List<BenefitDependency> needBenefits = new ArrayList<BenefitDependency>();
			HrBenefitJoin requiredEnrollment = null;
			for (BenefitDependency bd : bdl) {
				HrBenefitJoin j = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, be.getPerson()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bd.getRequiredBenefit()).first();
				if (j != null)
					requiredEnrollment = j;
				else
					needBenefits.add(bd);
			}
			if (requiredEnrollment == null) {
				dependencyHTML = "<font size='14'>You do not have the necessary enrollment to be eligible for this benefit. "
						+ " You must have an enrollment in any of the following benefits: ";
				for (BenefitDependency nb : needBenefits)
					dependencyHTML += "<b>" + nb.getRequiredBenefit().getName() + "</b>, ";
				dependencyHTML = dependencyHTML.substring(0, dependencyHTML.lastIndexOf(","));
				dependencyHTML += ".</font>";
			}
		}
		autoAssign = b.getAutoAssignBoolean();

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
	}

	void setData(BHRBenefit b, String empId) {
		BEmployee be = new BEmployee(empId);
		dependentCount = be.getDependentCount();//be.getDependents().length;
		benefitNameHTML = "<font size='14'><b>" + b.getName() + "</b></font>";

		if (b.getDescription() == null)
			benefitDescriptionHTML = "";
		else
			benefitDescriptionHTML = "<font size='11'>" + b.getDescription() + "</font>";

		instructions = b.getAdditionalInstructions();
		hasBeneficiaries = b.getHasBeneficiariesBool();
		requiresDecline = b.getRequiresDecline();
		avatarUrl = b.getAvatarPath();
		contingentBeneficiaries = b.getHasContingentBeneficiariesBool();
		hasPhysicians = b.getHasPhysicians();
		autoAssign = b.getAutoAssignBoolean();
		reloadWizard = false;
		employerCostModel = b.getEmployerCostModel() + "";
		employeeCostModel = b.getEmployeeCostModel() + "";
		benefitAmountModel = b.getBenefitAmountModel() + "";
		declineMessage = null;
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

	public boolean getAutoAssign() {
		return autoAssign;
	}

	public void setAutoAssign(boolean autoAssign) {
		this.autoAssign = autoAssign;
	}

	public String getDependencyHTML() {
		return dependencyHTML;
	}

	public void setDependencyHTML(String dependencyHTML) {
		this.dependencyHTML = dependencyHTML;
	}

	public boolean getHasPhysicians() {
		return hasPhysicians;
	}

	public void setHasPhysicians(boolean hasPhysicians) {
		this.hasPhysicians = hasPhysicians;
	}

	public boolean getContingentBeneficiaries() {
		return contingentBeneficiaries;
	}

	public void setContingentBeneficiaries(boolean contingentBeneficiaries) {
		this.contingentBeneficiaries = contingentBeneficiaries;
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

	public boolean getHasBeneficiaries() {
		return hasBeneficiaries;
	}

	public void setHasBeneficiaries(boolean hasBeneficiaries) {
		this.hasBeneficiaries = hasBeneficiaries;
	}

	public boolean getRequiresDecline() {
		return requiresDecline;
	}

	public void setRequiresDecline(boolean requiresDecline) {
		this.requiresDecline = requiresDecline;
	}

	public int getDependentCount() {
		return dependentCount;
	}

	public void setDependentCount(int dependentCount) {
		this.dependentCount = dependentCount;
	}

	public String getEmployerCostModel() {
		return employerCostModel;
	}

	public void setEmployerCostModel(String employerCostModel) {
		this.employerCostModel = employerCostModel;
	}

	public String getEmployeeCostModel() {
		return employeeCostModel;
	}

	public void setEmployeeCostModel(String employeeCostModel) {
		this.employeeCostModel = employeeCostModel;
	}

	public String getBenefitAmountModel() {
		return benefitAmountModel;
	}

	public void setBenefitAmountModel(String benefitAmountModel) {
		this.benefitAmountModel = benefitAmountModel;
	}

	public String getDeclineMessage() {
		return declineMessage;
	}

	public void setDeclineMessage(String declineMessage) {
		this.declineMessage = declineMessage;
	}
	
}
