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
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BBenefitConfigCost extends SimpleBusinessObjectBase<BenefitConfigCost> {
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	private static final transient ArahantLogger logger = new ArahantLogger(BBenefitConfigCost.class);

	
	private static BBenefitConfigCostAge[] makeArrayAge(List<BenefitConfigCostAge> l) {
		BBenefitConfigCostAge[] ret = new BBenefitConfigCostAge[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BBenefitConfigCostAge(l.get(loop));
		return ret;
	}
	
	public BBenefitConfigCost() {
	}

	public BBenefitConfigCost(String id) {
		super(id);
	}

	public BBenefitConfigCost(BenefitConfigCost o) {
		bean = o;
	}

	@Override
	public void delete() {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.createCriteria(BenefitConfigCostStatus.class).setMaxResults(1000).eq(BenefitConfigCostStatus.CONFIG_COST, bean).delete();

		hsu.createCriteria(BenefitConfigCostAge.class).setMaxResults(1000).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, bean).delete();

		hsu.flush();

		super.delete();
	}

	@Override
	public String create() throws ArahantException {
		return this.create(false);
	}

	public String create(boolean setDefaults) throws ArahantException {
		bean = new BenefitConfigCost();
		bean.setAppliesToOrgGroup(ArahantSession.getHSU().getCurrentCompany());
		if (setDefaults) {
			bean.setAppliesToStatus('A');
			bean.setBaseAmountSource('B');
			bean.setBaseRoundAmount(0.1);
			bean.setMultiplierSource('M');
			bean.setMultiplier(1.0);
			bean.setDivider(1.0);
			bean.setFirstActiveDate(0);
			bean.setLastActiveDate(0);
		}

		return bean.generateId();
	}

	public BBenefitConfigCostAge[] getAges() {
		return makeArrayAge(ArahantSession.getHSU().createCriteria(BenefitConfigCostAge.class).eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST, bean).orderBy(BenefitConfigCostAge.AGE).list());
	}

	@Deprecated
	public double getBaseAmount() {
		logger.deprecated();
		return 0;
	}

	@Deprecated
	public String getBaseAmountSource() {
		logger.deprecated();
		return "B";
	}

	@Deprecated
	public double getBaseCapAmount() {
		logger.deprecated();
		return 0;
	}

	@Deprecated
	public double getBaseRoundAmount() {
		logger.deprecated();
		return 0;
	}

	public int getFirstActiveDate() {
		return bean.getFirstActiveDate();
	}

	@Deprecated
	public double getFormulaDivider() {
		logger.deprecated();
		return 1;
	}

	@Deprecated
	public double getFormulaMultiplier() {
		logger.deprecated();
		return 1;
	}

	public String getId() {
		return bean.getBenefitConfigCostId();
	}

	public int getLastActiveDate() {
		return bean.getLastActiveDate();
	}

	@Deprecated
	public String getMultiplierSource() {
		logger.deprecated();
		return "M";
	}

	public String getOrgGroupId() {
		return bean.getAppliesToOrgGroup().getOrgGroupId();
	}

	public String getOrgGroupName() {
		return bean.getAppliesToOrgGroup().getName();
	}

	public String getStatusType() {
		return bean.getAppliesToStatus() + "";
	}

	public BBenefitConfigCostStatus[] getStatuses() {
		return BBenefitConfigCostStatus.makeArray(bean.getStatuses());
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(BenefitConfigCost.class, key);
	}

	public void setAppliedStatuses(String[] ids) {
		ArahantSession.getHSU().createCriteria(BenefitConfigCostStatus.class).eq(BenefitConfigCostStatus.CONFIG_COST, bean).delete();
		for (String id : ids) {
			BenefitConfigCostStatus stat = new BenefitConfigCostStatus();
			stat.generateId();
			stat.setCost(bean);
			stat.setStatus(ArahantSession.getHSU().get(HrEmployeeStatus.class, id));
			ArahantSession.getHSU().insert(stat);
		}
	}

	public void setAppliesToStatus(char appliesToStatus) {
		bean.setAppliesToStatus(appliesToStatus);
	}

	@Deprecated
	public void setBaseAmount(double baseAmount) {
		logger.deprecated();
	}

	@Deprecated
	public void setBaseAmountSource(char source) {
		logger.deprecated();
	}

	@Deprecated
	public void setBaseAmountSource(String baseAmountSource) {
		logger.deprecated();
	}

	@Deprecated
	public void setBaseCapAmount(double baseCapAmount) {
		logger.deprecated();
	}

	@Deprecated
	public void setBaseRoundAmount(double baseRoundAmount) {
		logger.deprecated();
	}

	public void setBenefitConfigId(String configId) {
		bean.setConfig(ArahantSession.getHSU().get(HrBenefitConfig.class, configId));
	}

	public void setConfig(BHRBenefitConfig bc) {
		bean.setConfig(bc.bean);
	}

	public void setDivider(double divider) {
		bean.setDivider(divider);
	}

	public void setFirstActiveDate(int firstActiveDate) {
		bean.setFirstActiveDate(firstActiveDate);
	}

	@Deprecated
	public void setFormulaDivider(double formulaDivider) {
		logger.deprecated();
	}

	@Deprecated
	public void setFormulaMultiplier(double formulaMultiplier) {
		logger.deprecated();
	}

	public void setLastActiveDate(int lastActiveDate) {
		bean.setLastActiveDate(lastActiveDate);
	}

	public void setMultiplier(double multiplier) {
		bean.setMultiplier(multiplier);
	}

	@Deprecated
	public void setMultiplierSource(char multiplierSource) {
		logger.deprecated();
	}

	public void setMultiplierSource(String multiplierSource) {
		if (multiplierSource.length() > 0)
			bean.setMultiplierSource(multiplierSource.charAt(0));
	}

	public void setOrgGroup(String orgGroupId) {
		bean.setAppliesToOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setOrgGroupId(String orgGroupId) {
		bean.setAppliesToOrgGroup(ArahantSession.getHSU().get(OrgGroup.class, orgGroupId));
	}

	public void setStatusIds(String[] statusIds) {
		for (String id : statusIds) {
			BBenefitConfigCostStatus s = new BBenefitConfigCostStatus();
			s.create();
			s.setStatusId(id);
			s.setCost(bean);
			addPendingInsert(s);
		}
	}

	public void setStatusType(String statusType) {
		if (statusType.length() > 0)
			bean.setAppliesToStatus(statusType.charAt(0));
	}

	public void setAgeCalcType(String ageCalcType) {
		if (!isEmpty(ageCalcType))
			bean.setAgeCalcType(ageCalcType.charAt(0));
		else
			bean.setAgeCalcType('N');
	}

	public String getAgeCalcType() {
		return bean.getAgeCalcType() + "";
	}
	
	public double getFixedEmployeeCost() {
		return bean.getFixedEmployeeCost();
	}

	public void setFixedEmployeeCost(double fixedEmployeeCost) {
		bean.setFixedEmployeeCost(fixedEmployeeCost);
	}

	public double getFixedEmployerCost() {
		return bean.getFixedEmployerCost();
	}

	public void setFixedEmployerCost(double fixedEmployerCost) {
		bean.setFixedEmployerCost(fixedEmployerCost);
	}

	public double getBenefitAmount() {
		return bean.getBenefitAmount();
	}

	public void setBenefitAmount(double benefitAmount) {
		bean.setBenefitAmount(benefitAmount);
	}

	public double getMinValue() {
		return bean.getMinValue();
	}

	public void setMinValue(double minValue) {
		bean.setMinValue(minValue);
	}

	public double getMaxValue() {
		return bean.getMaxValue();
	}

	public void setMaxValue(double maxValue) {
		bean.setMaxValue(maxValue);
	}

	public double getStepValue() {
		return bean.getStepValue();
	}

	public void setStepValue(double stepValue) {
		bean.setStepValue(stepValue);
	}

	public double getMaxMultipleOfSalary() {
		return bean.getMaxMultipleOfSalary();
	}

	public void setMaxMultipleOfSalary(double maxMultipleOfSalary) {
		bean.setMaxMultipleOfSalary(maxMultipleOfSalary);
	}

	public double getRatePerUnit() {
		return bean.getRatePerUnit();
	}

	public void setRatePerUnit(double ratePerUnit) {
		bean.setRatePerUnit(ratePerUnit);
	}

	public short getRateFrequency() {
		return bean.getRateFrequency();
	}

	public void setRateFrequency(short rateFrequency) {
		bean.setRateFrequency(rateFrequency);
	}

	public char getRateRelatesTo() {
		return bean.getRateRelatesTo();
	}

	public void setRateRelatesTo(char rateRelatesTo) {
		bean.setRateRelatesTo(rateRelatesTo);
	}

	public char getSalaryRoundType() {
		return bean.getSalaryRoundType();
	}

	public void setSalaryRoundType(char salaryRoundType) {
		bean.setSalaryRoundType(salaryRoundType);
	}

	public double getSalaryRoundAmount() {
		return bean.getSalaryRoundAmount();
	}

	public void setSalaryRoundAmount(double salaryRoundAmount) {
		bean.setSalaryRoundAmount(salaryRoundAmount);
	}

	public char getBenefitRoundType() {
		return bean.getBenefitRoundType();
	}

	public void setBenefitRoundType(char benefitRoundType) {
		bean.setBenefitRoundType(benefitRoundType);
	}

	public double getBenefitRoundAmount() {
		return bean.getBenefitRoundAmount();
	}

	public void setBenefitRoundAmount(double benefitRoundAmount) {
		bean.setBenefitRoundAmount(benefitRoundAmount);
	}

	public char getCapType() {
		return bean.getCapType();
	}
	
	public void setCapType(char cap_type) {
		bean.setCapType(cap_type);
	}
	
	public char getGuaranteedIssueType() {
		return bean.getGuaranteedIssueType();
	}

	public void setGuaranteedIssueType(char guaranteedIssueType) {
		bean.setGuaranteedIssueType(guaranteedIssueType);
	}

	public double getGuaranteedIssueAmount() {
		return bean.getGuaranteedIssueAmount();
	}

	public void setGuaranteedIssueAmount(double guaranteedIssueAmount) {
		bean.setGuaranteedIssueAmount(guaranteedIssueAmount);
	}

	public static void main(String[] args) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.beginTransaction();

		HrBenefitConfig bbc = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, "Test Hibernate Config").first();

		BBenefitConfigCost bbcc = new BBenefitConfigCost();
		String costId = bbcc.create();
		bbcc.setAppliesToStatus('A');
		bbcc.setOrgGroupId("00000-0000000005");
		bbcc.setBaseAmountSource("C");
		bbcc.setBaseAmount(0);
		bbcc.setBaseCapAmount(0);
		bbcc.setBaseRoundAmount(0.01);
		bbcc.setMultiplierSource("M");
		bbcc.setFormulaMultiplier(1);
		bbcc.setFormulaDivider(1000);
		bbcc.setFirstActiveDate(0);
		bbcc.setLastActiveDate(0);
		bbcc.setAgeCalcType("N");
		bbcc.getBean().setCostPerEnrollee('N');
		bbcc.setBenefitConfigId(bbc.getBenefitConfigId());
		bbcc.insert();
		System.out.println("Created Config Cost: " + bbcc.getId());

		hsu.commitTransaction();
		hsu.close();
		hsu = ArahantSession.openHSU();
		hsu.beginTransaction();

		bbcc = new BBenefitConfigCost(costId);
		BBenefitConfigCostAge bbcca = new BBenefitConfigCostAge();
		bbcca.create();
		bbcca.setConfigCost(bbcc);
		bbcca.setMaxAge(30);
		bbcca.setMultiplier(1.5);
		bbcca.insert();
		System.out.println("Created Age Calc 1: " + bbcca.getId());

		bbcca = new BBenefitConfigCostAge();
		bbcca.create();
		bbcca.setConfigCost(bbcc);
		bbcca.setMaxAge(60);
		bbcca.setMultiplier(2.5);
		bbcca.insert();
		System.out.println("Created Age Calc 2: " + bbcca.getId());

		bbcca = new BBenefitConfigCostAge();
		bbcca.create();
		bbcca.setConfigCost(bbcc);
		bbcca.setMaxAge(90);
		bbcca.setMultiplier(3.5);
		bbcca.insert();
		System.out.println("Created Age Calc 3: " + bbcca.getId());

		hsu.commitTransaction();
		hsu.close();
		System.out.println("Committed / Closed HSU");

		hsu = ArahantSession.openHSU();

		System.out.println("Opened HSU");

		HrBenefitConfig bbc2 = hsu.createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.NAME, "Test Hibernate Config").first();
		BHRBenefitConfig bbc3 = new BHRBenefitConfig(bbc2);
		try {
			bbc3.deleteCosts();
		} catch (Exception e) {
			logger.error(e);
		}

		//		for (BBenefitConfigCost cc : BBenefitConfigCost.makeArray(hsu.createCriteria(BenefitConfigCost.class).eq(BenefitConfigCost.BENEFIT_CONFIG, bbc2).list()))
//		{
//			hsu.createCriteria(BenefitConfigCostStatus.class)
//				.eq(BenefitConfigCostStatus.BENEFIT_CONFIG_COST_ID, cc.getId())
//				.delete();
//
//			hsu.createCriteria(BenefitConfigCostAge.class)
//				.eq(BenefitConfigCostAge.BENEFIT_CONFIG_COST_ID, cc.getId())
//				.delete();
//System.out.println("Deleting Age Calcs");
//
//			hsu.flush();
//
//System.out.println("Deleting Config Cost: " + cc.getId());
//			hsu.delete(cc.getBean());
//		}
		hsu.commitTransaction();
		System.out.println("Final Commit ");
	}
}
