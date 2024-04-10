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

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.ImportBenefitJoin;
import com.arahant.beans.ImportBenefitJoinH;
import com.arahant.beans.ImportType;
import com.arahant.beans.ImportedEnrollee;
import com.arahant.beans.ImportedEnrolleeH;
import com.arahant.beans.Person;
import com.arahant.exceptions.ArahantException;
import com.arahant.exceptions.ArahantWarning;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.HibernateCriteriaUtil;
import java.util.List;

public class BImportedEnrollee extends SimpleBusinessObjectBase<ImportedEnrollee> {

	public BImportedEnrollee() {
	}

	public BImportedEnrollee(String id) {
		super(id);
	}

	public BImportedEnrollee(ImportedEnrollee o) {
		bean = o;
	}

	@Override
	public String create() throws ArahantException {
		bean = new ImportedEnrollee();
		return bean.generateId();
	}

	public void enrollIn(BImportBenefit cb, int coverageStart, int coverageEnd, BImportedEnrollee sponsor) {
		BImportBenefitJoin ibj = new BImportBenefitJoin();
		ibj.create();
		ibj.setBenefit(cb.getBean());
		ibj.setCoverageStartDate(coverageStart);
		ibj.setCoverageEndDate(coverageEnd);
		ibj.setEnrollee(bean);
		ibj.setSubscriber(sponsor.bean);
		addPendingInsert(ibj);
	}

	@Override
	public void load(String key) throws ArahantException {
		bean = ArahantSession.getHSU().get(ImportedEnrollee.class, key);
	}

	public String getEnrolleeId() {
		return bean.getEnrolleeId();
	}

	public String getCity() {
		return bean.getCity();
	}

	public void setCity(String city) {
		bean.setCity(city);
	}

	public int getDob() {
		return bean.getDob();
	}

	public void setDOB(int dob) {
		bean.setDob(dob);
	}

	public String getFname() {
		return bean.getFname();
	}

	public void setFname(String fname) {
		bean.setFname(fname);
	}

	public void setImportType(ImportType importType) {
		bean.setImportFileType(importType);
	}

	public String getLname() {
		return bean.getLname();
	}

	public void setLname(String lname) {
		bean.setLname(lname);
	}

	public String getMname() {
		return bean.getMname();
	}

	public void setMname(String mname) {
		bean.setMname(mname);
	}

	public String getRelationship() {
		return bean.getRelationship();
	}

	public void setRelationship(String relationship) {
		bean.setRelationship(relationship);
	}

	public String getSsn() {
		return bean.getSsn();
	}

	public void setSSN(String ssn) {
		bean.setSsn(ssn);
	}

	public String getState() {
		return bean.getState();
	}

	public void setState(String state) {
		bean.setState(state);
	}

	public String getStreet1() {
		return bean.getStreet1();
	}

	public void setStreet1(String street1) {
		bean.setStreet1(street1);
	}

	public String getStreet2() {
		return bean.getStreet2();
	}

	public void setStreet2(String street2) {
		bean.setStreet2(street2);
	}

	public String getZip() {
		return bean.getZip();
	}

	public void setZip(String zip) {
		bean.setZip(zip);
	}

	public void unEnroll(BImportBenefit cb) {
		ArahantSession.getHSU().createCriteria(ImportBenefitJoin.class)
				.eq(ImportBenefitJoin.BENEFIT, cb.getBean())
				.eq(ImportBenefitJoin.ENROLLEE, bean)
				.delete();

		//if no longer enrolled anywhere, delete
		if (bean.getBenefitJoins().isEmpty())
			delete();
	}

	public static BImportedEnrollee[] listEnrollees(final String importTypeId, final String firstName, final String lastName, final String ssn, final int max) {
		HibernateCriteriaUtil<ImportedEnrollee> hcu = ArahantSession.getHSU().createCriteria(ImportedEnrollee.class)
				.orderBy(ImportedEnrollee.LNAME)
				.orderBy(ImportedEnrollee.FNAME)
				.setMaxResults(max)
				.like(ImportedEnrollee.FNAME, firstName)
				.like(ImportedEnrollee.LNAME, lastName);
		if (!isEmpty(ssn))
			hcu.eq(ImportedEnrollee.SSN, ssn);
		if (!isEmpty(importTypeId))
			hcu.joinTo(ImportedEnrollee.IMPORT_FILE_TYPE)
					.eq(ImportType.ID, importTypeId);

		return makeArray(hcu.list());
	}

	public BImportBenefitJoin[] listEnrolleesForBenefits(final String benefitId) {
		return BImportBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(ImportBenefitJoin.class)
				.eq(ImportBenefitJoin.BENEFIT, new BImportBenefit(benefitId).getBean())
				.joinTo(ImportBenefitJoin.SPONSOR)
				.joinTo(ImportedEnrollee.PROVIDED_JOINS)
				.eq(ImportBenefitJoin.ENROLLEE, bean)
				.list());

	}

	public static BImportedEnrollee[] makeArray(List<ImportedEnrollee> l) throws ArahantException {

		final BImportedEnrollee[] ret = new BImportedEnrollee[l.size()];

		for (int loop = 0; loop < ret.length; loop++)
			ret[loop] = new BImportedEnrollee(l.get(loop));

		return ret;

	}

	public List<ImportedEnrolleeH> getHistoryRecords() {
		return (ArahantSession.getHSU().createCriteria(ImportedEnrolleeH.class)
				.orderByDesc(ImportedEnrolleeH.RECORD_CHANGE_DATE)
				.eq(ImportedEnrolleeH.ID, bean.getEnrolleeId())
				.list());
	}

	public List<ImportBenefitJoinH> getBenefitHistory() {
		return (ArahantSession.getHSU().createCriteria(ImportBenefitJoinH.class)
				.orderByDesc(ImportBenefitJoinH.RECORD_CHANGE_DATE)
				.eq(ImportBenefitJoinH.ENROLLEE, bean)
				.list());
	}

	public BImportedEnrollee[] getRelatedEnrollees() {
		return makeArray(ArahantSession.getHSU().createCriteria(ImportedEnrollee.class)
				.ne(ImportedEnrollee.ID, bean.getEnrolleeId())
				.joinTo(ImportedEnrollee.BENEFIT_JOINS)
				.joinTo(ImportBenefitJoin.SPONSOR)
				.joinTo(ImportedEnrollee.PROVIDED_JOINS)
				.eq(ImportBenefitJoin.ENROLLEE, bean)
				.list());
	}

	public BImportBenefitJoin[] getBenefitJoins() {
		return BImportBenefitJoin.makeArray(ArahantSession.getHSU().createCriteria(ImportBenefitJoin.class)
				.eq(ImportBenefitJoin.ENROLLEE, bean)
				.list());
	}

	public void apply() {
		//see if person exists
		Employee sponsor;
		Person person;
		if (!isEmpty(bean.getSsn())) {
			Person p = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.SSN, bean.getSsn()).first();

			if (p != null) {
				BPerson bp = new BPerson(p);
				bp.setFirstName(bean.getFname());
				bp.setLastName(bean.getLname());
				bp.setMiddleName(bean.getMname());
				bp.setStreet(bean.getStreet1());
				bp.setStreet2(bean.getStreet2());
				bp.setCity(bean.getCity());
				bp.setState(bean.getState());
				bp.setZip(bean.getZip());
				bp.setDob(bean.getDob());
				bp.update();

				if (bean.getRelationship().equals("EE")) {
					person = bp.getPerson();
					sponsor = (Employee) person;
				} else
					try {
						person = bp.getPerson();
						ImportedEnrollee ieSponsor = bean.getBenefitJoins().iterator().next().getSubscriber();
						Employee emp = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.SSN, ieSponsor.getSsn()).first();
						sponsor = emp;
					} catch (Exception e) {
						throw new ArahantWarning("Sponsoring employee was not found.  Please apply them first.");
					}

			} else
				//need to make a new person
				//is this an EE?
				if (bean.getRelationship().equals("EE")) {
					BEmployee be = new BEmployee();
					be.create();
					be.setFirstName(bean.getFname());
					be.setLastName(bean.getLname());
					be.setMiddleName(bean.getMname());
					be.setStreet(bean.getStreet1());
					be.setStreet2(bean.getStreet2());
					be.setCity(bean.getCity());
					be.setState(bean.getState());
					be.setZip(bean.getZip());
					be.setDob(bean.getDob());
					be.setSex("U");
					be.insert();
					person = be.getEmployee();
					sponsor = be.getEmployee();
				} else
					//find the Employee
					try {
						ImportedEnrollee ieSponsor = bean.getBenefitJoins().iterator().next().getSubscriber();
						sponsor = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.SSN, ieSponsor.getSsn()).first();

						BHREmplDependent dep = new BHREmplDependent();
						dep.create();
						dep.setEmployeeId(sponsor.getPersonId());
						dep.setFirstName(bean.getFname());
						dep.setLastName(bean.getLname());
						dep.setMiddleName(bean.getMname());
						dep.setStreet(bean.getStreet1());
						dep.setStreet2(bean.getStreet2());
						dep.setCity(bean.getCity());
						dep.setState(bean.getState());
						dep.setZip(bean.getZip());
						dep.setDob(bean.getDob());
						dep.setSex("U");
						if (bean.getRelationship().equals("CH"))
							dep.setRelationshipType(HrEmplDependent.TYPE_CHILD);
						if (bean.getRelationship().equals("SP"))
							dep.setRelationshipType(HrEmplDependent.TYPE_SPOUSE);
						dep.insert();
						person = dep.getPerson();

					} catch (Exception e) {
						throw new ArahantWarning("Sponsoring employee was not found.  Please apply them first.");
					}
		} else //social was empty
		
			//if relationship is EE, make the employee
			if (bean.getRelationship().equals("EE")) {
				BEmployee be = new BEmployee();
				be.create();
				be.setFirstName(bean.getFname());
				be.setLastName(bean.getLname());
				be.setMiddleName(bean.getMname());
				be.setStreet(bean.getStreet1());
				be.setStreet2(bean.getStreet2());
				be.setCity(bean.getCity());
				be.setState(bean.getState());
				be.setZip(bean.getZip());
				be.setDob(bean.getDob());
				be.setSsn(bean.getSsn());
				be.setSex("U");
				be.insert();
				sponsor = be.getEmployee();
				person = be.getEmployee();
			} else
				//find the Employee
				try {
					ImportedEnrollee ieSponsor = bean.getBenefitJoins().iterator().next().getSubscriber();
					sponsor = ArahantSession.getHSU().createCriteria(Employee.class).eq(Employee.SSN, ieSponsor.getSsn()).first();


					//does this employee have a dep with similar values
					HibernateCriteriaUtil<HrEmplDependent> hcu = ArahantSession.getHSU().createCriteria(HrEmplDependent.class)
							.eq(HrEmplDependent.EMPLOYEE, sponsor);

					hcu.joinTo(HrEmplDependent.PERSON)
							.eq(Person.DOB, bean.getDob())
							.like(Person.FNAME, bean.getFname() + "%")
							.first();

					HrEmplDependent d = hcu.first();

					if (d != null) {
						BHREmplDependent dep = new BHREmplDependent(d);
						dep.setEmployeeId(sponsor.getPersonId());
						dep.setFirstName(bean.getFname());
						dep.setLastName(bean.getLname());
						dep.setMiddleName(bean.getMname());
						dep.setStreet(bean.getStreet1());
						dep.setStreet2(bean.getStreet2());
						dep.setCity(bean.getCity());
						dep.setState(bean.getState());
						dep.setZip(bean.getZip());
						dep.setDob(bean.getDob());
						dep.setSsn(bean.getSsn());
						dep.setSex("U");
						if (bean.getRelationship().equals("CH"))
							dep.setRelationshipType(HrEmplDependent.TYPE_CHILD);
						if (bean.getRelationship().equals("SP"))
							dep.setRelationshipType(HrEmplDependent.TYPE_SPOUSE);
						dep.update();
						person = dep.getPerson();

					} else {
						BHREmplDependent dep = new BHREmplDependent();
						dep.create();
						dep.setEmployeeId(sponsor.getPersonId());
						dep.setFirstName(bean.getFname());
						dep.setLastName(bean.getLname());
						dep.setMiddleName(bean.getMname());
						dep.setStreet(bean.getStreet1());
						dep.setStreet2(bean.getStreet2());
						dep.setCity(bean.getCity());
						dep.setState(bean.getState());
						dep.setZip(bean.getZip());
						dep.setDob(bean.getDob());
						dep.setSsn(bean.getSsn());
						dep.setSex("U");
						if (bean.getRelationship().equals("CH"))
							dep.setRelationshipType(HrEmplDependent.TYPE_CHILD);
						if (bean.getRelationship().equals("SP"))
							dep.setRelationshipType(HrEmplDependent.TYPE_SPOUSE);
						dep.insert();
						person = dep.getPerson();
					}
				} catch (Exception e) {
					throw new ArahantWarning("Sponsoring employee was not found.  Please apply them first.");
				}


		//now I have person and sponsor, need to go through the benefits and apply
		for (ImportBenefitJoin ibj : bean.getBenefitJoins()) {
			//look to see if they have a benefit join with this config
			HrBenefitJoin bj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
					.eq(HrBenefitJoin.PAYING_PERSON, sponsor)
					.eq(HrBenefitJoin.COVERED_PERSON, person)
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.eq(HrBenefitConfig.NAME, ibj.getBenefit().getBenefitName())
					.first();

			if (bj == null) //create the join
			
				if (sponsor.getPersonId().equals(person.getPersonId())) {
					HrBenefitConfig bc = ArahantSession.getHSU().createCriteria(HrBenefitConfig.class).eq(HrBenefitConfig.INSURANCE_CODE, ibj.getBenefit().getBenefitName()).first();
					if (bc == null)
						throw new ArahantWarning("Benefit Configuration " + ibj.getBenefit().getBenefitName() + " was not found.  Please set it up in the system.");

					BHRBenefitJoin bbj = new BHRBenefitJoin();
					bbj.create();
					bbj.setCoverageStartDate(ibj.getCoverageStartDate());
					bbj.setCoverageEndDate(ibj.getCoverageEndDate());
					bbj.setPayingPerson(sponsor);
					bbj.setCoveredPerson(person);
					bbj.setHrBenefitConfig(bc);
					bbj.setPolicyStartDate(ibj.getCoverageStartDate());
					bbj.setPolicyEndDate(ibj.getCoverageEndDate());
					bbj.insert(true);
				} else //find the sponsor record for this one, if not the sponsor
				{
					HrBenefitJoin dbj = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
							.eq(HrBenefitJoin.PAYING_PERSON, sponsor)
							.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
							.eq(HrBenefitConfig.INSURANCE_CODE, ibj.getBenefit().getBenefitName())
							.first();
					if (dbj == null)
						throw new ArahantWarning("Main policy record not found.  Please import employee record first.");

					BHRBenefitJoin bbj = new BHRBenefitJoin();
					bbj.create();
					bbj.setCoverageStartDate(ibj.getCoverageStartDate());
					bbj.setCoverageEndDate(ibj.getCoverageEndDate());
					bbj.setPayingPerson(sponsor);
					bbj.setCoveredPerson(person);
					bbj.setHrBenefitConfig(dbj.getHrBenefitConfig());
					bbj.setPolicyStartDate(dbj.getPolicyStartDate());
					bbj.setPolicyEndDate(dbj.getPolicyEndDate());
					bbj.insert(true);
				}

		}

		ArahantSession.addReturnMessage("Person information has been applied.");
	}
}
