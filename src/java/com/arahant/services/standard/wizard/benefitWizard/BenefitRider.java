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

import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class BenefitRider {

	private String benefitRiderId;
	private String riderBenefitId;
	private String baseBenefitId;
	private String riderBenefitName;
	private String baseBenefitName;
	private boolean required;
	private boolean hidden;
	private boolean selectCheckbox;
	private String configId;
	private String configName;
	private double coverageAmount;
	private double monthlyCost;
	private double pppCost;
	private double annualCost;
	private Enrollee[] enrollees;
	private String instructions;
	private String riderType;

	public BenefitRider() {
	}

	public BenefitRider(BBenefitRider bbr, BHRBenefitJoin bj, int date) {
		BHRBenefit baseBenefit = new BHRBenefit(bbr.getBaseBenefit());
		BHRBenefit riderBenefit = new BHRBenefit(bbr.getRiderBenefit());
		benefitRiderId = bbr.getBenefitRiderId();
		riderBenefitId = riderBenefit.getBenefitId();
		baseBenefitId = baseBenefit.getBenefitId();
		riderBenefitName = riderBenefit.getName();
		baseBenefitName = baseBenefit.getName();
		required = bbr.getRequiredBoolean();
		riderType = required ? "Included" : "Optional";
		hidden = bbr.getHiddenBoolean();
		instructions = riderBenefit.getDescription();
		HrBenefitConfig match = null;
		List<HrBenefitConfig> hrbcl = riderBenefit.getConfigs();
		if (bj.getBean() != null) {
			BHRBenefitConfig bc = bj.getBenefitConfig();
			if (hrbcl.size() == 1)
				match = hrbcl.get(0);
			else
				for (HrBenefitConfig newConfig : hrbcl)
					if (bc.getBean().getEmployee() == newConfig.getEmployee()
							&& bc.getBean().getChildren() == newConfig.getChildren()
							&& bc.getBean().getSpouseEmployee() == newConfig.getSpouseEmployee()
							&& bc.getBean().getSpouseNonEmployee() == newConfig.getSpouseNonEmployee()
							&& bc.getBean().getSpouseEmpOrChildren() == newConfig.getSpouseEmpOrChildren()
							&& bc.getBean().getSpouseNonEmpOrChildren() == newConfig.getSpouseNonEmpOrChildren()
							&& bc.getBean().getMaxChildren() == newConfig.getMaxChildren()) {
						match = newConfig;
						break;
					}
			selectCheckbox = required;
			BEmployee be = new BEmployee(bj.getPayingPersonId());
			BHRBenefitConfig bbc = new BHRBenefitConfig(match);
			List<Enrollee> el = new ArrayList<Enrollee>();
			List<IPerson> estimated = bbc.estimateCoveredPeople(be);
			for (IPerson p : estimated)
				if (!p.getPersonId().equals(be.getPersonId())) //we want dependents only here
				{
					Person pp = (Person) p;
					HrEmplDependent dep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, be.getEmployee()).eq(HrEmplDependent.PERSON, pp).first();
					if (dep != null) {
						Enrollee e = new Enrollee();
						e.setRelationshipId(dep.getRelationshipId());
						e.setName(pp.getFname() + " " + pp.getLname());
						el.add(e);
					}
				}
			setEnrollees(el);
			if (!required)
				selectCheckbox = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, bj.getPayingPerson().getPerson()).eq(HrBenefitJoin.HR_BENEFIT_CONFIG, match).exists();
			configId = match.getBenefitConfigId();
			configName = match.getName();
			if (Utils.doubleEqual(match.deprecatedGetMaxAmount(), match.deprecatedGetMinValue(), 0.001) && Utils.doubleEqual(match.deprecatedGetMaxAmount(), match.deprecatedGetStepValue(), 0.001) && !Utils.doubleEqual(match.deprecatedGetMaxAmount(), 0, 0.001))
				coverageAmount = match.deprecatedGetMaxAmount();
			else
				coverageAmount = bj.getAmountCovered();
			int bjType = bj.getChangeReasonType();
			annualCost = BenefitCostCalculator.calculateCostNewMethodAnnual(date, match, bj.getPayingPerson().getPerson(), false, coverageAmount, bj.getPolicyStartDate(), 0, estimated, bjType, baseBenefit.getInternalId());
			pppCost = BenefitCostCalculator.calculateCostNewMethod(date, match, bj.getPayingPerson().getPerson(), false, coverageAmount, bj.getPolicyStartDate(), 0, estimated, bjType, baseBenefit.getInternalId());
			if (pppCost < 0)
				pppCost = 0;
			monthlyCost = BenefitCostCalculator.calculateCostNewMethodMonthly(date, match, bj.getPayingPerson().getPerson(), false, coverageAmount, bj.getPolicyStartDate(), 0, estimated, bjType, baseBenefit.getInternalId());
		}
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getRiderType() {
		return riderType;
	}

	public void setRiderType(String riderType) {
		this.riderType = riderType;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public double getAnnualCost() {
		return annualCost;
	}

	public void setAnnualCost(double annualCost) {
		this.annualCost = annualCost;
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

	public Enrollee[] getEnrollees() {
		return enrollees;
	}

	public void setEnrollees(Enrollee[] enrollees) {
		this.enrollees = enrollees;
	}

	public void setEnrollees(List<Enrollee> el) {
		this.enrollees = new Enrollee[el.size()];
		for (int i = 0; i < el.size(); i++)
			this.enrollees[i] = el.get(i);
	}

	public double getMonthlyCost() {
		return monthlyCost;
	}

	public void setMonthlyCost(double monthlyCost) {
		this.monthlyCost = monthlyCost;
	}

	public double getPppCost() {
		return pppCost;
	}

	public void setPppCost(double pppCost) {
		this.pppCost = pppCost;
	}

	public boolean getSelectCheckbox() {
		return selectCheckbox;
	}

	public void setSelectCheckbox(boolean selectCheckbox) {
		this.selectCheckbox = selectCheckbox;
	}

	public String getBaseBenefitId() {
		return baseBenefitId;
	}

	public void setBaseBenefitId(String baseBenefitId) {
		this.baseBenefitId = baseBenefitId;
	}

	public String getBaseBenefitName() {
		return baseBenefitName;
	}

	public void setBaseBenefitName(String baseBenefitName) {
		this.baseBenefitName = baseBenefitName;
	}

	public String getBenefitRiderId() {
		return benefitRiderId;
	}

	public void setBenefitRiderId(String benefitRiderId) {
		this.benefitRiderId = benefitRiderId;
	}

	public boolean getHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean getRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getRiderBenefitId() {
		return riderBenefitId;
	}

	public void setRiderBenefitId(String riderBenefitId) {
		this.riderBenefitId = riderBenefitId;
	}

	public String getRiderBenefitName() {
		return riderBenefitName;
	}

	public void setRiderBenefitName(String riderBenefitName) {
		this.riderBenefitName = riderBenefitName;
	}
}
