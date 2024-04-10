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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateSessionUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class BHRBenefitJoinH extends SimpleBusinessObjectBase<HrBenefitJoinH> {

	public BHRBenefitJoinH() {
	}

	/**
	 * @param historyId
	 * @throws ArahantException
	 */
	public BHRBenefitJoinH(String historyId) throws ArahantException {
		internalLoad(historyId);
	}

	/**
	 * @param joinH
	 */
	public BHRBenefitJoinH(HrBenefitJoinH joinH) {
		bean = joinH;
	}

	@Override
	public String create() throws ArahantException {
		if (BRight.checkRight("CanCloneHistoryRecords") == BRight.ACCESS_LEVEL_WRITE) {
			bean = new HrBenefitJoinH();
			return bean.generateId();
		} else
			throw new ArahantException("Can't create history record directly.");
	}

	public Person getHistoryChangePerson() {
		return ArahantSession.getHSU().get(Person.class, bean.getRecordPersonId());
	}

	private void internalLoad(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBenefitJoinH.class, key);
	}
	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */

	@Override
	public void load(String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @return
	 */
	public double getAmountPaid() {
		return bean.getAmountPaid();
	}

	/**
	 * @return
	 */
	public String getHistoryId() {

		return bean.getHistory_id();
	}

	/**
	 * @return
	 */
	public String getDateTimeFormatted() {

		return DateUtils.getDateTimeFormatted(bean.getRecordChangeDate());
	}

	/**
	 * @return
	 */
	public double getAmountCovered() {
		return bean.getAmountCovered();
	}

	public String getEmployeeCovered() {
		return bean.getEmployeeCovered() + "";
	}

	public void setEmployeeCovered(String employeeCovered) {
		bean.setEmployeeCovered(employeeCovered.charAt(0));
	}

	/**
	 * @return
	 */
	public String getBenefitName() {

		try {
			if (!isEmpty(bean.getHrBenefitConfigId()))
				return new BHRBenefitConfig(bean.getHrBenefitConfigId()).getBenefitName();
			else if (!isEmpty(bean.getHrBenefitId()))
				return "DECLINE - " + new BHRBenefit(bean.getHrBenefitId()).getName();
			else if (!isEmpty(bean.getHrBenefitCategoryId()))
				return "DECLINE - " + new BHRBenefitCategory(bean.getHrBenefitCategoryId()).getDescription();
			else
				return "MISSING";
		} catch (Exception ignored) {
			return "MISSING";
		}

		/* RKK - We decided on the above solution because the below code does
		 *       not seem correct and in fact we see Dental showing up in Medical sometimes
		 * 
		 HrBenefitJoin ebj=hsu.get(HrBenefitJoin.class, bean.getBenefitJoinId());
		
		 if (ebj!=null)
		 {
		 if (ebj.getHrBenefitConfig()!=null)
		 return ebj.getHrBenefitConfig().getHrBenefit().getName();
		 if (ebj.getHrBenefit()!=null)
		 return "DECLINE - "+ebj.getHrBenefit().getName();
		 if (ebj.getHrBenefitCategory()!=null)
		 return "DECLINE - "+ebj.getHrBenefitCategory().getDescription();
		 }
		 HrBenefitJoinH ebjh=hsu.createCriteria(HrBenefitJoinH.class)
		 .eq(HrBenefitJoinH.BENEFIT_JOIN_ID,bean.getBenefitJoinId())
		 .first();

		 if (ebjh!=null)
		 {
		 if (!isEmpty(ebjh.getHrBenefitConfigId()))
		 {
		 HrBenefitConfig c=hsu.get(HrBenefitConfig.class, ebjh.getHrBenefitConfigId());
		 if (c==null)
		 return "Missing";
		 return c.getHrBenefit().getName();
		 }
		 if (!isEmpty(ebjh.getHrBenefitId()))
		 {
		 HrBenefit c=hsu.get(HrBenefit.class, ebjh.getHrBenefitId());
		 return  "DECLINE - "+c.getName();
		 }
		 if (!isEmpty(ebjh.getHrBenefitCategoryId()))
		 {
		 HrBenefitCategory c=hsu.get(HrBenefitCategory.class, ebjh.getHrBenefitCategoryId());
		 return  "DECLINE - "+c.getDescription();
		 }
		 }
		 return "MISSING";
		 */
	}

	public String getCategoryId() {
		if (!isEmpty(bean.getHrBenefitCategoryId()))
			return bean.getHrBenefitCategoryId();
		else if (!isEmpty(bean.getHrBenefitId()))
			return new BHRBenefit(bean.getHrBenefitId()).getCategoryId();
		else if (!isEmpty(bean.getHrBenefitConfigId()))
			return new BHRBenefitConfig(bean.getHrBenefitConfigId()).getCategoryId();
		else
			throw new ArahantException("History does not link to any Benefit information.");
	}

	public String getBenefitId() {
		if (!isEmpty(bean.getHrBenefitId()))
			return new BHRBenefit(bean.getHrBenefitId()).getCategoryId();
		else if (!isEmpty(bean.getHrBenefitConfigId()))
			return new BHRBenefitConfig(bean.getHrBenefitConfigId()).getBenefitId();
		return null;
	}

	public String getConfigId() {
		if (!isEmpty(bean.getHrBenefitConfigId()))
			return bean.getHrBenefitConfigId();
		return null;
	}

	/**
	 * @return
	 */
	public String getConfigName() {
		try {
			if (!isEmpty(bean.getHrBenefitConfigId()))
				return new BHRBenefitConfig(bean.getHrBenefitConfigId()).getConfigName();
			else if (!isEmpty(bean.getHrBenefitId()))
				return "DECLINE - " + new BHRBenefit(bean.getHrBenefitId()).getName();
			else if (!isEmpty(bean.getHrBenefitCategoryId()))
				return "DECLINE - " + new BHRBenefitCategory(bean.getHrBenefitCategoryId()).getDescription();
			else
				return "MISSING";
		} catch (Exception ignored) {
			return "MISSING";
		}

		/* RKK - We decided on the above solution because the below code does
		 *       not seem correct and in fact we see Dental showing up in Medical sometimes
		 * 
		 HrBenefitJoin ebj=hsu.get(HrBenefitJoin.class, bean.getBenefitJoinId());
		 if (ebj!=null)
		 {
		 if (ebj.getHrBenefitConfig()!=null)
		 return ebj.getHrBenefitConfig().getName();
		 if (ebj.getHrBenefit()!=null)
		 return "DECLINE - "+ebj.getHrBenefit().getName();
		 if (ebj.getHrBenefitCategory()!=null)
		 return "DECLINE - "+ebj.getHrBenefitCategory().getDescription();
		 }
		 HrBenefitJoinH ebjh=hsu.createCriteria(HrBenefitJoinH.class)
		 .eq(HrBenefitJoinH.BENEFIT_JOIN_ID,bean.getBenefitJoinId())
		 .first();
		
		 if (ebjh!=null)
		 {
		 if (!isEmpty(ebjh.getHrBenefitConfigId()))
		 {
		 HrBenefitConfig c=hsu.get(HrBenefitConfig.class, ebjh.getHrBenefitConfigId());
		 if (c==null)
		 return "Missing";
		 return c.getName();
		 }
		 if (!isEmpty(ebjh.getHrBenefitId()))
		 {
		 HrBenefit c=hsu.get(HrBenefit.class, ebjh.getHrBenefitId());
		 return  "DECLINE - "+c.getName();
		 }
		 if (!isEmpty(ebjh.getHrBenefitCategoryId()))
		 {
		 HrBenefitCategory c=hsu.get(HrBenefitCategory.class, ebjh.getHrBenefitCategoryId());
		 return  "DECLINE - "+c.getDescription();
		 }
		 }
		 return "MISSING";
		 */

	}

	/**
	 * @return
	 */
	public int getPolicyEndDate() {
		return bean.getPolicyEndDate();
	}

	/**
	 * @return
	 */
	public int getPolicyStartDate() {
		return bean.getPolicyStartDate();
	}

	/**
	 * @return
	 */
	public int getCoverageEndDate() {
		return bean.getCoverageEndDate();
	}

	public int getCoverageChangeDate() {
		return bean.getCoverageChangeDate();
	}

	public void setCoverageChangeDate(int coverageChangeDate) {
		bean.setCoverageChangeDate(coverageChangeDate);
	}

	public int getOriginalCoverageDate() {
		return bean.getOriginalCoverageDate();
	}

	public void setOriginalCoverageDate(int originalCoverageDate) {
		bean.setOriginalCoverageDate(originalCoverageDate);
	}

	public double getRequestedCost() {
		return bean.getRequestedCost();
	}

	public void setRequestedCost(double requestedCost) {
		bean.setRequestedCost(requestedCost);
	}

	public char getRequestedCostPeriod() {
		return bean.getRequestedCostPeriod();
	}

	public void setRequestedCostPeriod(char requestedCostPeriod) {
		bean.setRequestedCostPeriod(requestedCostPeriod);
	}

	/**
	 * @return
	 */
	public String getChangeType() {
		return bean.getRecordChangeType() + "";
	}

	/**
	 * @return
	 */
	public String getChangeTypeFormatted() {
		char changeType = bean.getRecordChangeType();
		String changeTypeFormatted = "";

		if (changeType == 'N')
			changeTypeFormatted = "New";
		else if (changeType == 'M')
			changeTypeFormatted = "Modify";
		else if (changeType == 'D')
			changeTypeFormatted = "Delete";

		return changeTypeFormatted;
	}

	/**
	 * @return
	 */
	public int getCoverageStartDate() {
		return bean.getCoverageStartDate();
	}

	/**
	 * @return
	 */
	public boolean getBenefitApproved() {
		return bean.getBenefitApproved() == 'Y';
	}

	/**
	 * @return
	 */
	public String getInsuranceId() {
		return bean.getInsuranceId();
	}

	/**
	 * @return
	 */
	public String getPersonName() {
		Person p = ArahantSession.getHSU().get(Person.class, bean.getRecordPersonId());
		return p.getNameWithLogin();
	}

	/**
	 * @return
	 */
	public String getChangeReason() {
		return bean.getChangeDescription();
	}

	/**
	 * @param categoryId
	 * @param employeeId
	 * @return
	 */
	public static BHRBenefitJoinH[] list(String categoryId, String employeeId) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		List<HrBenefitJoinH> l = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId).eq(HrBenefitJoinH.CATEGORY_ID, categoryId).list();

		List benefitIds = hsu.createCriteria(HrBenefit.class).selectFields(HrBenefit.BENEFITID).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list();

		l.addAll(hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId).in(HrBenefitJoinH.BENEFIT_ID, benefitIds).list());

		List benefitConfigIds = hsu.createCriteria(HrBenefitConfig.class).selectFields(HrBenefitConfig.BENEFIT_CONFIG_ID).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list();

		l.addAll(hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.EMPLOYEE_ID, employeeId).in(HrBenefitJoinH.BENEFIT_CONFIG_ID, benefitConfigIds).list());
		Person emp = hsu.get(Employee.class, employeeId);

		List<HrBenefitJoin> ebjList = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, emp).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list();

		ebjList.addAll(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, emp).joinTo(HrBenefitJoin.HRBENEFIT).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list());

		ebjList.addAll(hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.COVERED_PERSON, emp).joinTo(HrBenefitJoin.HR_BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId).list());

		for (HrBenefitJoin ebj : ebjList) {
			HrBenefitJoinH hist = new HrBenefitJoinH();
			HibernateSessionUtil.copyCorresponding(hist, ebj);
			hist.setHistory_id(ebj.getBenefitJoinId());
			l.add(hist);
		}
		Collections.sort(l);
		return makeArray(l);
	}

	/**
	 * @param l
	 * @return
	 */
	private static BHRBenefitJoinH[] makeArray(List<HrBenefitJoinH> l) {
		BHRBenefitJoinH[] ret = new BHRBenefitJoinH[l.size()];
		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBenefitJoinH(l.get(loop));
		return ret;
	}

	/**
	 * @return
	 */
	public boolean getUsingCOBRA() {
		return bean.getUsingCOBRA() == 'Y';
	}

	/**
	 * @return @throws ArahantException
	 */
	public String getRelationship() throws ArahantException {
		return new BHREmplDependent(bean.getRelationshipId()).getTextRelationship();
	}

	//Try to NOT use these.  They are only used on a DRC custom screen to CLONE join records, NOT EDIT EXISTING
	public void setPolEndDate(int policyEndDate) {
		bean.setPolicyEndDate(policyEndDate);
	}

	public void setPolStartDate(int policyStartDate) {
		bean.setPolicyStartDate(policyStartDate);
	}

	public void setCovEndDate(int coverageEndDate) {
		bean.setCoverageEndDate(coverageEndDate);
	}

	public void setCovStartDate(int coverageStartDate) {
		bean.setCoverageStartDate(coverageStartDate);
	}

	public void setBcrId(HrBenefitChangeReason changeReasonId) {
		bean.setBenefitChangeReason(null);
	}

	public void setChangeDesc(String description) {
		bean.setChangeDescription(description);
	}

	public void setRecChangeDate(Date date) {
		bean.setRecordChangeDate(date);
	}

	public void setRecChangePerson(String currentPerson) {
		bean.setRecordPersonId(currentPerson);
	}

	public void setEmployeeExplanation(String employeeExplanation) {
		bean.setEmployeeExplanation(employeeExplanation);
	}

	public String getEmployeeExplanation() {
		return bean.getEmployeeExplanation();
	}

	public String getPayingPersonId() throws ArahantException {
		return bean.getPayingPersonId();
	}

	public String getBenefitConfigName() {
		if (bean.getHrBenefitConfig() == null)
			return "";
		return bean.getHrBenefitConfig().getName();
	}

	public String getRecordChangeType() {
		if (bean.getRecordChangeType() == 'N')
			return "New";
		else if (bean.getRecordChangeType() == 'M')
			return "Modify";
		else if (bean.getRecordChangeType() == 'D')
			return "Delete";
		else
			throw new ArahantException("Record Change Type not specified");
	}

	public int getAcceptedDateCOBRA() {
		return bean.getAcceptedDateCOBRA();
	}

	public int getMaxMonthsCOBRA() {
		return bean.getMaxMonthsCOBRA();
	}

	public BPerson getPayingPerson() throws ArahantException {
		if (bean.getPayingPerson() == null)
			return null;
		return new BPerson(bean.getPayingPerson());
	}

	public BHRBenefitConfig getBenefitConfig() {
		if (bean.getHrBenefitConfig() == null)
			return null;
		return new BHRBenefitConfig(bean.getHrBenefitConfig());
	}

	public int getChangeReasonDate() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getEffectiveDate();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getEventDate();
		//otherwise, nothing was set
		return 0;
	}

	public String getCoveredPersonId() throws ArahantException {
		return bean.getCoveredPersonId();
	}

	public String getBenefitChangeReasonId() {
		if (bean.getBenefitChangeReason() != null && bean.getBenefitChangeReason().getHrBenefitChangeReasonId() != null)
			return bean.getBenefitChangeReason().getHrBenefitChangeReasonId();

		if (bean.getLifeEvent() != null && bean.getLifeEvent().getLifeEventId() != null)
			return bean.getLifeEvent().getChangeReason().getHrBenefitChangeReasonId();
		//otherwise, nothing was set
		return null;
	}

	public BHRBenefitJoinH[] getPolicyAndDependentBenefitJoins() throws ArahantException {
		final List<BHRBenefitJoinH> benefitJoins = new ArrayList<BHRBenefitJoinH>();

		// try to load the paying person as an employee
		Employee employee = ArahantSession.getHSU().get(Employee.class, bean.getPayingPerson().getPersonId());
		if (employee == null)
			// not an employee, so must be a non-employee dependent - just add this join and we are done
			benefitJoins.add(this);
		else {
			// paying person is an employee, so let's get all dependents
			@SuppressWarnings("unchecked")
			List<String> dependents = (List) ArahantSession.getHSU().createCriteria(HrEmplDependent.class).selectFields(HrEmplDependent.DEP_KEY).eq(HrEmplDependent.EMPLOYEE, employee).gtOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0).list();

			// get policy and all existing dependent benefit joins
			List<HrBenefitJoinH> allExistingBenefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON, bean.getPayingPerson()).eq(HrBenefitJoinH.POLICY_START_DATE, bean.getPolicyStartDate()).eq(HrBenefitJoinH.HR_BENEFIT_CONFIG, bean.getHrBenefitConfig()).eq(HrBenefitJoinH.APPROVED, bean.getBenefitApproved()).list();

			//System.out.println("Looking for " + bean.getPayingPersonId() + " " + bean.getPolicyStartDate() + " " + bean.getHrBenefitConfig().getBenefitConfigId());
			for (final HrBenefitJoinH existingBenefitJoin : allExistingBenefitJoins) {
				// add this benefit join to the list, and remove the dependent if it happens to be a dependent benefit join
				benefitJoins.add(new BHRBenefitJoinH(existingBenefitJoin));
				try {
					dependents.remove(existingBenefitJoin.getRelationshipId());
				} catch (Exception e) {
					BPerson bpp = new BPerson();
					if (bpp.hasPending(existingBenefitJoin.getPayingPersonId())) {
						String person = existingBenefitJoin.getPayingPerson().getNameLFM();
						throw new ArahantException("Please approve pending change request for " + person + " before approving benefits.");
					} else
						throw new ArahantException("Get Policy and Dependent benefit join error: " + e.getMessage());
				}
			}
		}

		// convert to array for return
		final BHRBenefitJoinH[] benefitJoinArray = new BHRBenefitJoinH[benefitJoins.size()];
		return benefitJoins.toArray(benefitJoinArray);
	}
}
