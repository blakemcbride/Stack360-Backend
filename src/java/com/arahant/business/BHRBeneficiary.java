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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.util.List;

public class BHRBeneficiary extends SimpleBusinessObjectBase<HrBeneficiary> {

	public static void deleteNotIn(BHRBenefitJoin x, List<String> doneIds) {
		ArahantSession.getHSU().createCriteria(HrBeneficiary.class)
				.eq(HrBeneficiary.EMPL_BENEFIT_JOIN, x.bean)
				.notIn(HrBeneficiary.ID, doneIds)
				.delete();
	}

	public BHRBeneficiary() {
	}

	/**
	 * @param id
	 * @throws ArahantException
	 */
	public BHRBeneficiary(final String id) throws ArahantException {
		internalLoad(id);
	}

	/**
	 * @param beneficiary
	 */
	public BHRBeneficiary(final HrBeneficiary beneficiary) {
		bean = beneficiary;
	}

	/* (non-Javadoc)
	 * @see com.arahant.business.interfaces.IDBFunctions#create()
	 */
	@Override
	public String create() throws ArahantException {
		bean = new HrBeneficiary();
		return bean.generateId();
	}

	private void internalLoad(final String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(HrBeneficiary.class, key);
		if (bean == null)
			throw new ArahantException("Couldn't find beneficiary.");
	}

	@Override
	public void load(final String key) throws ArahantException {
		internalLoad(key);
	}

	/**
	 * @param hsu
	 * @param ids
	 * @throws ArahantException
	 * @throws ArahantDeleteException
	 */
	public static void delete(final HibernateSessionUtil hsu, final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String id : ids)
			new BHRBeneficiary(id).delete();
	}

	public static void delete(final String[] ids) throws ArahantDeleteException, ArahantException {
		for (final String id : ids)
			new BHRBeneficiary(id).delete();
	}

	/**
	 * @param employeeId
	 * @param benefitCategoryId
	 * @return
	 */
	public static BHRBeneficiary[] list(final String employeeId, final String benefitId) {
		final HibernateCriteriaUtil<HrBeneficiary> hcu = ArahantSession.getHSU().createCriteria(HrBeneficiary.class);

		final HibernateCriteriaUtil ebjhcu = hcu.joinTo(HrBeneficiary.EMPL_BENEFIT_JOIN);
		ebjhcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Person.PERSONID, employeeId);
		ebjhcu.joinTo(HrBenefitJoin.HRBENEFIT).eq(HrBenefit.BENEFITID, benefitId);


		return makeArray(hcu.list());
	}

	/**
	 * @param name
	 * @return
	 */
	private static BHRBeneficiary[] makeArray(final List<HrBeneficiary> l) {
		final BHRBeneficiary[] ret = new BHRBeneficiary[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BHRBeneficiary(l.get(loop));

		return ret;
	}

	/**
	 * @param benefitJoin
	 */
	public void setAssociatedBenefit(final HrBenefitJoin benefitJoin) {
		bean.setBenefitJoin(benefitJoin);
	}

	/**
	 * @return
	 */
	public String getBeneficiaryId() {
		return bean.getBeneficiaryId();
	}

	/**
	 * @return
	 */
	public String getBeneficiary() {
		return bean.getBeneficiary();
	}

	/**
	 * @return
	 */
	public String getRelationship() {
		return bean.getRelationship();
	}

	/**
	 * @return
	 */
	public int getPercentage() {
		return bean.getBenefitPercent();
	}

	/**
	 * @param beneficiary
	 */
	public void setBeneficiary(final String beneficiary) {
		bean.setBeneficiary(beneficiary);
	}

	public void setBeneficiaryType(char type) {
		bean.setBeneficiaryType(type);
	}

	/**
	 * @param relationship
	 */
	public void setRelationship(String relationship) {
		if (relationship != null && relationship.length() > 10)
			relationship = relationship.substring(0, 10);
		bean.setRelationship(relationship);
	}

	/**
	 * @param percentage
	 */
	public void setPercentage(final int percentage) {
		bean.setBenefitPercent((short) percentage);
	}

	/**
	 * @param employeeId
	 * @return
	 */
	public static BHRBeneficiary[] listForConfig(final String benefitJoinId) {
		final HibernateCriteriaUtil<HrBeneficiary> hcu = ArahantSession.getHSU().createCriteria(HrBeneficiary.class)
				.joinTo(HrBeneficiary.EMPL_BENEFIT_JOIN)
				.eq(HrBenefitJoin.BENEFIT_JOIN_ID, benefitJoinId);

		return makeArray(hcu.list());
	}

	public static BHRBeneficiary[] listPrimaries(final BHRBenefitJoin bj) {
		final HibernateCriteriaUtil<HrBeneficiary> hcu = ArahantSession.getHSU().createCriteria(HrBeneficiary.class)
				.eq(HrBeneficiary.EMPL_BENEFIT_JOIN, bj.bean)
				.eq(HrBeneficiary.BENEFICIARY_TYPE, HrBeneficiary.PRIMARY);

		return makeArray(hcu.list());
	}

	public static BHRBeneficiary[] listSecondaries(final BHRBenefitJoin bj) {
		final HibernateCriteriaUtil<HrBeneficiary> hcu = ArahantSession.getHSU().createCriteria(HrBeneficiary.class)
				.eq(HrBeneficiary.EMPL_BENEFIT_JOIN, bj.bean)
				.eq(HrBeneficiary.BENEFICIARY_TYPE, HrBeneficiary.CONTINGENT);

		return makeArray(hcu.list());
	}

	/**
	 * @param benefitJoinId
	 * @throws ArahantException
	 */
	public void setBenefitJoin(String benefitJoinId) throws ArahantException {
		final BHRBenefitJoin ebj = new BHRBenefitJoin(benefitJoinId);
		bean.setBenefitJoin(ebj.bean);
	}

	/**
	 * @param ebj
	 */
	public void setAssociatedBenefit(BHRBenefitJoin ebj) {
		setAssociatedBenefit(ebj.bean);
	}

	/**
	 * @param dob
	 */
	public void setDob(int dob) {
		bean.setDob(dob);
	}

	/**
	 * @param ssn
	 */
	public void setSsn(String ssn) {
		bean.setSsn(ssn);
	}

	/**
	 * @param address
	 */
	public void setAddress(String address) {
		bean.setAddress(address);
	}

	/**
	 * @param beneficiaryType
	 */
	public void setBeneficiaryType(String beneficiaryType) {
		if (!isEmpty(beneficiaryType))
			bean.setBeneficiaryType(beneficiaryType.charAt(0));
	}

	/**
	 * @return
	 */
	public int getDob() {
		return bean.getDob();
	}

	/**
	 * @return
	 */
	public String getSsn() {
		return bean.getSsn();
	}

	/**
	 * @return
	 */
	public String getAddress() {
		return bean.getAddress();
	}

	/**
	 * @return
	 */
	public String getBeneficiaryType() {
		return bean.getBeneficiaryType() + "";
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

		return "N/A";
	}
}
