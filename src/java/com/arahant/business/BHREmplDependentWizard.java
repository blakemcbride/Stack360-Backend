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



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.arahant.business;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependentChangeRequest;
import com.arahant.beans.HrEmplDependentWizard;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class BHREmplDependentWizard extends SimpleBusinessObjectBase<HrEmplDependentWizard>{

	public static BHREmplDependentWizard[] makeArray(List<HrEmplDependentWizard> l) {
		Collections.sort(l);
		BHREmplDependentWizard[] ret=new BHREmplDependentWizard[l.size()];
		for (int loop=0;loop<ret.length;loop++)
			ret[loop]=new BHREmplDependentWizard(l.get(loop));
		return ret;
	}

//	static BHREmplDependentWizard[] makeChangeArray(List<HrEmplDependentChangeRequest> l) {
//
//                BHREmplDependentWizard[] ret=new BHREmplDependentWizard[l.size()];
//                for (int loop=0;loop<ret.length;loop++)
//                {
//			ret[loop]=new BHREmplDependentWizard(l.get(loop).getChangeRecord());
//                        ret[loop].pcr=l.get(loop);
//                }
//
//		return ret;
//	}

	public static void terminate(String[] ids, int effectiveDate) {
		for (String id : ids)
			new BHREmplDependentWizard(id).terminate(effectiveDate);
	}

	public BHREmplDependentWizard(HrEmplDependentWizard o) {
		super();
		bean=o;
		bpp=new BPerson(bean.getPersonId());
		if(bpp.getRecordType()!='C')
		{
			bpp = new BPerson(ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, bean.getPersonId()).first().getPersonId());
		}
	}

	public BHREmplDependentWizard() {
		super();
	}

	public BHREmplDependentWizard(String id) {
		super(id);
	}

	public void terminate(final int effectiveDate)
	{
		//if I have no real record, just get rid of me and my change record

		//we don't actually delete, need to mark inactive
		if (effectiveDate == 0)
			bean.setDateInactive(DateUtils.addDays(DateUtils.now(),-1));
		else
			bean.setDateInactive(DateUtils.addDays(effectiveDate,-1));
		update();
		ArahantSession.getHSU().flush();

	}

	@Override
	public String create() throws ArahantException {
		bean=new HrEmplDependentWizard();
		return bean.generateId();
	}

	@Override
	public void load(String key) throws ArahantException {
		bean=ArahantSession.getHSU().get(HrEmplDependentWizard.class, key);
		bpp=new BPerson(bean.getPersonId());
		if(bpp.getRecordType()!='C')
		{
			bpp = new BPerson(ArahantSession.getHSU().createCriteria(Person.class).eq(Person.RECORD_TYPE, 'C').eq(Person.PERSONID, bean.getPersonId()).first().getPersonId());
		}
	}

	BPerson bpp;

	public String getPersonId() {
		return bean.getPersonId();
	}

	public String getLastName() {
		return bpp.getLastName();
	}

	public String getFirstName() {
		return bpp.getFirstName();
	}

	public String getMiddleName() {
		return bpp.getMiddleName();
	}

	public String getTextRelationship()
	{
		switch (bean.getRelationshipType())
		{
			case 'E' : return "Employee"; //used for temporary returns
			case 'S' :return "Spouse";
			case 'C' :return "Child";
			case 'O' :return getOtherRelationshipDescription();
			default:  return "Unknown";
		}
	}

	public void setTextRelationship(String relationship)
	{
		bean.setRelationship(relationship);
	}

	private String getOtherRelationshipDescription() {
		return bean.getRelationship();
	}

	public String getSex() {
		return bpp.getSex();
	}

	public String getSsn() {
		return bpp.getSsn();
	}

	public int getDob() {
		return bpp.getDob();
	}

	public boolean getStudent() {
		return bpp.getStudent();
	}

	public boolean getHandicap() {
		return bpp.getHandicap();
	}

	public boolean getIsEmployee() {
		return bpp.isEmployee();
	}

	public String getDependentId() {
		return bean.getRelationshipId();
	}

	public void setFirstName(String firstName) {
		bpp.setFirstName(firstName);
	}

	public void setLastName(String lastName) {
		bpp.setLastName(lastName);
	}

	public void setMiddleName(String middleName) {
		bpp.setMiddleName(middleName);
	}

	public void setSex(String sex) {
		bpp.setSex(sex);
	}

	public void setDob(int dob) {
		bpp.setDob(dob);
	}

	public void setHandicap(boolean handicap) {
		bpp.setHandicap(handicap);
	}

	public void setRelationshipType(String relationshipType) {
		bean.setRelationshipType(relationshipType.charAt(0));
	}

	public void setSsn(String ssn) {
		bpp.setSsn(ssn);
	}

	public void setStudent(boolean student) {
		bpp.setStudent(student);
	}

	public void setEmployeeId(String personId) {
		bean.setEmployeeId(personId);
	}

	public String getEmployeeId()
	{
		return bean.getEmployeeId();
	}

	public int getDateInactive() {
        return bean.getDateInactive();
    }

    public void setDateInactive(final int dateInactive) {
        bean.setDateInactive(dateInactive);
    }

	public boolean enrolledInConfig(String configId) {
		System.out.println(configId);

		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.BENEFIT_CONFIG_ID, configId)
				.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.eq(HrBenefitJoin.PROJECT, getChangeRequest().getProject())
				.eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPersonId())
				.exists();
	}

	public boolean enrolledInBenefit(String benefitId) {

		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean())
				.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'N')
				.eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPersonId())
				.exists();
	}

	public boolean enrolledInApprovedBenefit(String benefitId) {
		return ArahantSession.getHSU().createCriteria(HrBenefitConfig.class)
				.eq(HrBenefitConfig.HR_BENEFIT, new BHRBenefit(benefitId).getBean())
				.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPersonId())
				.exists();
	}

	public boolean enrolledInCategory(String categoryId) {
		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
		   .joinTo(HrBenefit.BENEFIT_CATEGORY)
		   .eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'N')
				.eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPersonId())
				.exists();
	}

	public boolean enrolledInApprovedCategory(String categoryId) {
		HibernateCriteriaUtil hcu = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class);

		hcu.joinTo(HrBenefitConfig.HR_BENEFIT)
		   .joinTo(HrBenefit.BENEFIT_CATEGORY)
		   .eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, categoryId);

		return hcu.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
				.ne(HrBenefitJoin.COVERAGE_START_DATE,0)
				.gtOrEq(HrBenefitJoin.POLICY_END_DATE, DateUtils.now(), 0)
				.eq(HrBenefitJoin.APPROVED, 'Y')
				.eq(HrBenefitJoin.COVERED_PERSON_ID, bean.getPersonId())
				.exists();
	}

	private HrEmplDependentChangeRequest pcr;

	private HrEmplDependentChangeRequest getChangeRequest() {
		if (pcr!=null)
                    return pcr;

                //System.out.println("looking for "+bean.getRelationshipId());

		 pcr=ArahantSession.getHSU().createCriteria(HrEmplDependentChangeRequest.class)
			.eq(HrEmplDependentChangeRequest.DEP_PENDING, bean)
			.eq(HrEmplDependentChangeRequest.CHANGE_STATUS, HrEmplDependentChangeRequest.STATUS_PENDING)
			.first();
                 if (pcr==null)
                     throw new ArahantException("Could not find pending dep relationship with relationship id of "+bean.getRelationshipId());

                 return pcr;
	}

	public String getEnrollingRelationshipId()
	{
		if (getChangeRequest().getRealRecord()!=null)
			return getChangeRequest().getRealRecord().getRelationshipId();

		return getChangeRequest().getChangeRecord().getRelationshipId();
	}


	public String getEnrollingPersonId()
	{
		if (getChangeRequest().getRealRecord()!=null)
			return getChangeRequest().getRealRecord().getPerson().getPersonId();

		return getChangeRequest().getChangeRecord().getDependentId();
	}
}
