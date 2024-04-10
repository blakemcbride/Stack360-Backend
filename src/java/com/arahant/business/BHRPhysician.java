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
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrPhysician;
import com.arahant.beans.Person;
import com.arahant.beans.WizardConfigurationBenefit;
import com.arahant.beans.WizardConfigurationCategory;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.IDGenerator;
import java.util.ArrayList;
import java.util.List;

public class BHRPhysician extends SimpleBusinessObjectBase<HrPhysician> {

	public static void delete(String[] ids) {
		ArahantSession.getHSU().delete(ArahantSession.getHSU().createCriteria(HrPhysician.class).in(HrPhysician.ID, ids).list());
	}

	public BHRPhysician() {
	}

	/**
	 * @param string
	 * @throws ArahantException
	 */
	public BHRPhysician(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param hsu
	 * @param account
	 */
	public BHRPhysician(final HibernateSessionUtil hsu, final HrPhysician account) {
		bean = account;
	}

	public BHRPhysician(HrPhysician physician) {
		bean = physician;
	}

	public boolean getAnnualVisit() {
		return bean.getAnnualVisit() == 'Y' ? true : false;
	}

	public void setAnnualVisit(boolean annualVisit) {
		bean.setAnnualVisit(annualVisit ? 'Y' : 'N');
	}

	public int getChangeDate() {
		return bean.getChangeDate();
	}

	public void setChangeDate(int changeDate) {
		bean.setChangeDate(changeDate);
	}

	public String getChangeReason() {
		return bean.getChangeReason();
	}

	public void setChangeReason(String changeReason) {
		bean.setChangeReason(changeReason);
	}

	/**
	 * @return Returns the address.
	 */
	public String getAddress() {
		if (bean.getAddress() == null)
			return "";
		return bean.getAddress();
	}

	/**
	 * @param address The address to set.
	 */
	public void setAddress(String address) {
		bean.setAddress(address);
	}

	/**
	 * @return Returns the ssn.
	 */
	public String getPhysicianCode() {
		return bean.getPhysicianCode();
	}

	/**
	 * @param ssn The ssn to set.
	 */
	public void setPhysicianCode(String physicianCode) {
		bean.setPhysicianCode(physicianCode);
	}

	/* (non-Javadoc)
	 * @see com.arahant.beans.ArahantBean#generateId()
	 */
	public String generateId() throws ArahantException {

		bean.setPhysicianId(IDGenerator.generate(bean));
		return bean.getPhysicianId();
	}

	/**
	 * @return Returns the beneficiary.
	 */
	public String getPhysicianName() {
		return bean.getPhysicianName();
	}

	/**
	 * @param beneficiary The beneficiary to set.
	 */
	public void setPhysicianName(String physicianName) {
		bean.setPhysicianName(physicianName);
	}

	/**
	 * @return Returns the beneficiaryId.
	 */
	public String getPhysicianId() {
		return bean.getPhysicianId();
	}

	/**
	 * @param beneficiaryId The beneficiaryId to set.
	 */
	public void setPhysicianId(String physicianId) {
		bean.setPhysicianId(physicianId);
	}

	/**
	 * @return Returns the benefitJoin.
	 */
	public HrBenefitJoin getBenefitJoin() {
		return bean.getBenefitJoin();
	}

	/**
	 * @param benefitJoin The benefitJoin to set.
	 */
	public void setBenefitJoin(HrBenefitJoin benefitJoin) {
		bean.setBenefitJoin(benefitJoin);
	}

	/**
	 * @return Returns the benefitJoinId.
	 */
	public String getBenefitJoinId() {
		return bean.getBenefitJoinId();
	}

	/**
	 * @param benefitJoinId The benefitJoinId to set.
	 */
	public void setBenefitJoinId(String benefitJoinId) {
		bean.setBenefitJoinId(benefitJoinId);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantException {
		for (final String element : ids)
			new BHRPosition(element).delete();
	}

	/**
	 * @param hsu
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static BHRPhysician[] list(final HibernateSessionUtil hsu) {

		final List l = hsu.createCriteria(HrPhysician.class)
				.orderBy(HrPhysician.NAME)
				.list();

		return makeArray(l);
	}

	/**
	 * @param hsu
	 * @return
	 */
	public static BHRPhysician[] listPhysiciansForEmployee(String empId, int max) {

		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrPhysician.class);

		hcu.joinTo(HrPhysician.EMPL_BENEFIT_JOIN)
				.eq(HrBenefitJoin.PAYING_PERSON, new BEmployee(empId).getPerson())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.orderBy(HrBenefit.NAME);

		hcu.joinTo(HrPhysician.EMPL_BENEFIT_JOIN)
				.joinTo(HrBenefitJoin.COVERED_PERSON)
				.orderBy(Person.LNAME)
				.orderBy(Person.FNAME);

		@SuppressWarnings("unchecked")
		List<HrPhysician> plist = hcu.list();

		return makeArray(plist);
	}

	@SuppressWarnings("unchecked")
	public static BHRPhysician[] listPhysiciansForWizard(String empId, String wizardConfigurationId, int max) {
		BWizardConfiguration bwc = new BWizardConfiguration(wizardConfigurationId);
		List<HrBenefit> benefitsInWizard = ArahantSession.getHSU().createCriteria(HrBenefit.class)
				.joinTo(HrBenefit.WIZARD_CONF_BENEFITS)
				.joinTo(WizardConfigurationBenefit.WIZARD_CONFIGURATION_CATEGORY)
				.eq(WizardConfigurationCategory.WIZARD_CONFIGURATION, bwc.getBean()).list();

		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrPhysician.class);

		hcu.joinTo(HrPhysician.EMPL_BENEFIT_JOIN)
				.eq(HrBenefitJoin.PAYING_PERSON, new BEmployee(empId).getPerson())
				.eq(HrBenefitJoin.APPROVED, 'N')
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.in(HrBenefitConfig.HR_BENEFIT, benefitsInWizard)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.orderBy(HrBenefit.NAME);

//		hcu.joinTo(HrPhysician.EMPL_BENEFIT_JOIN)
//			.joinTo(HrBenefitJoin.COVERED_PERSON)
//			.orderBy(Person.LNAME)
//			.orderBy(Person.FNAME);

		List<HrPhysician> plist = hcu.list();

		for (HrPhysician p : plist)
			benefitsInWizard.remove(p.getBenefitJoin().getHrBenefitConfig().getHrBenefit());

		HibernateCriteriaUtil hcu2 = ArahantSession.getHSU().createCriteria(HrPhysician.class).joinTo(HrPhysician.EMPL_BENEFIT_JOIN);

		hcu2.eq(HrBenefitJoin.PAYING_PERSON, new BEmployee(empId).getPerson())
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.in(HrBenefitConfig.HR_BENEFIT, benefitsInWizard)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.orderBy(HrBenefit.NAME);

//		hcu2.joinTo(HrBenefitJoin.COVERED_PERSON)
//			.orderBy(Person.LNAME)
//			.orderBy(Person.FNAME);

		plist.addAll(hcu2.list());

		return makeArray(plist);
	}

	static BHRPhysician[] makeArray(List<HrPhysician> l) {
		final BHRPhysician[] ret = new BHRPhysician[l.size()];
		for (int loop = 0; loop < l.size(); loop++)
			ret[loop] = new BHRPhysician(ArahantSession.getHSU(), l.get(loop));
		return ret;
	}

	public static String findOrMake(String physicianName, String coveredPersonId) {
		HrPhysician hrp = ArahantSession.getHSU().createCriteria(HrPhysician.class)
				.eq(HrPhysician.NAME, physicianName)
				.joinTo(HrPhysician.EMPL_BENEFIT_JOIN)
				.eq(HrBenefitJoin.COVERED_PERSON_ID, coveredPersonId)
				.first();

		if (hrp != null)
			return hrp.getPhysicianId();

		BHRPhysician physician = new BHRPhysician();
		String ret = physician.create();
		physician.setPhysicianName(physicianName);
		physician.insert();

		return ret;
	}

	public static BHRPhysician[] listPhysicians(final BHRBenefitJoin bj) {

		final HibernateCriteriaUtil<HrPhysician> hcu = ArahantSession.getHSU().createCriteria(HrPhysician.class)
				.eq(HrPhysician.EMPL_BENEFIT_JOIN, bj.bean);

		return makeArray(hcu.list());
	}

	public static BHRPhysician[] listAllPhysiciansInGroup(BHRBenefitJoin bj) {
		List<HrBenefitJoin> beneJoin = new ArrayList<HrBenefitJoin>();
		beneJoin.add(bj.getBean());
		beneJoin.addAll(bj.getActiveDependentBenefitJoins());
		final HibernateCriteriaUtil<HrPhysician> hcu = ArahantSession.getHSU().createCriteria(HrPhysician.class)
				.in(HrPhysician.EMPL_BENEFIT_JOIN, beneJoin);

		return makeArray(hcu.list());
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrPhysician();
		bean.generateId();

		return getPhysicianId();
	}

	@Override
	public void insert() {
		if (ArahantSession.getHSU().createCriteria(HrPhysician.class).eq(HrPhysician.EMPL_BENEFIT_JOIN, bean.getBenefitJoin()).count() > 1)
			throw new ArahantException("Person already has the maximum of two primary care physicians selected.");

		super.insert();
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#load(java.lang.String)
	 */
	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrPhysician.class, key);
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	public static void deleteNotIn(BHRBenefitJoin x, List<String> donePhysIds) {
		ArahantSession.getHSU().createCriteria(HrPhysician.class)
				.eq(HrPhysician.EMPL_BENEFIT_JOIN, x.bean)
				.notIn(HrPhysician.ID, donePhysIds)
				.delete();
	}
}
