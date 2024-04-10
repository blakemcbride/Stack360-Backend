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


package com.arahant.imports.census;

import com.arahant.beans.*;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHREmplDependent;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileReader;

public class DependentImport {

	private static final int EMPLOYEE_SSN = 0;
	private static final int RELATIONSHIP = 1;
	private static final int OTHER_TYPE = 2;
	private static final int SSN = 3;
	private static final int FNAME = 4;
	private static final int MNAME = 5;
	private static final int LNAME = 6;
	private static final int DOB = 7;
	private static final int GENDER = 8;
	private static final int MED1 = 9;
	private static final int MED1START = 10;
	private static final int MED2 = 11;
	private static final int MED2START = 12;
	private static final int DENTAL = 13;
	private static final int DENTALSTART = 14;
	private static final int VISION = 15;
	private static final int VISIONSTART = 16;
	private static final int VOL1 = 17;
	private static final int VOL1START = 18;
	private static final int VOL1AMT = 19;
	private static final int VOL2 = 20;
	private static final int VOL2START = 21;
	private static final int VOL2AMT = 22;
	private static final int VOL3 = 23;
	private static final int VOL3START = 24;
	private static final int VOL3AMT = 25;
	private static final int VOL4 = 26;
	private static final int VOL4START = 27;
	private static final int VOL4AMT = 28;
	private static final int VOL5 = 29;
	private static final int VOL5START = 30;
	private static final int VOL5AMT = 31;
	private static final int VOL6 = 32;
	private static final int VOL6START = 33;
	private static final int VOL6AMT = 34;
	private static final int VOL7 = 35;
	private static final int VOL7START = 36;
	private static final int VOL7AMT = 37;
	private static final int VOL8 = 38;
	private static final int VOL8START = 39;
	private static final int VOL8AMT = 40;
	private static final int MMEXTREF = 41;
	private HibernateSessionUtil hsu = ArahantSession.getHSU();
	private ArahantLogger logger = new ArahantLogger(DependentImport.class);

	public void doImport(String filename) throws Exception {
		DelimitedFileReader dfr = new DelimitedFileReader(filename);

		dfr.nextLine();

		while (dfr.nextLine())
			try {
				HrEmplDependent p;

				if (!dfr.getString(SSN).trim().equals(""))
					p = hsu.createCriteria(HrEmplDependent.class).joinTo(HrEmplDependent.PERSON).eq(Person.SSN, fixSSN(dfr.getString(SSN))).first();
				else
					p = null;

				BHREmplDependent dep = new BHREmplDependent();
				String empssn = fixSSN(dfr.getString(EMPLOYEE_SSN));
				if (empssn.trim().equals(""))
					continue;

				Employee emp = hsu.createCriteria(Employee.class).eq(Employee.SSN, empssn).first();

				if (p == null) {
					if (emp == null) {
						logger.error("Employee with SSN " + empssn + " not found.");
						continue;
						//throw new ArahantWarning("Employee with SSN "+empssn+" not found.");
					}
					if (hsu.createCriteria(Employee.class).eq(Employee.SSN, fixSSN(dfr.getString(SSN))).exists()) {
						String depId = hsu.createCriteria(Employee.class).eq(Employee.SSN, fixSSN(dfr.getString(SSN))).first().getPersonId();
						String empId = hsu.createCriteria(Employee.class).eq(Employee.SSN, fixSSN(dfr.getString(EMPLOYEE_SSN))).first().getPersonId();
						dep.create(depId, empId, dfr.getString(RELATIONSHIP).trim().equals("") ? "O" : dfr.getString(RELATIONSHIP).trim());
					} else
						dep.create();

					System.out.println("Importing... " + dfr.getString(FNAME) + " " + dfr.getString(LNAME));
					dep.setFirstName(dfr.getString(FNAME));
					dep.setMiddleName(dfr.getString(MNAME));
					dep.setLastName(dfr.getString(LNAME));
					dep.setSsn(fixSSN(dfr.getString(SSN)));
					dep.setDob(getDate(dfr, DOB));
					dep.setEmployeeId(emp.getPersonId());
					if (dfr.getString(RELATIONSHIP).trim().equals(""))
						dep.setRelationshipType("O");
					else
						dep.setRelationshipType(dfr.getString(RELATIONSHIP).trim());
					dep.setRelationship(dfr.getString(OTHER_TYPE));
					if (dfr.getString(GENDER).trim().equals(""))
						dep.setSex("U");
					else
						dep.setSex(dfr.getString(GENDER).trim().toUpperCase());
					dep.insert();
				} else {
					System.out.println("Found... " + dfr.getString(FNAME) + " " + dfr.getString(LNAME));
					if (p.getEmployee().getUnencryptedSsn().equals(fixSSN(dfr.getString(EMPLOYEE_SSN))))
						dep = new BHREmplDependent(p);
					else {
						dep = new BHREmplDependent(p);
						dep.setEmployeeId(emp.getPersonId());
					}
				}

				if (dfr.getString(MMEXTREF).trim().equals("")) {
					setBenefit(dep, dfr.getString(MED1), getDate(dfr, MED1START));
					setBenefit(dep, dfr.getString(MED2), getDate(dfr, MED2START));
				} else {
					setCASBenefit(dep, dfr.getString(MED1), getDate(dfr, MED1START), dfr.getString(MMEXTREF));
					setCASBenefit(dep, dfr.getString(MED2), getDate(dfr, MED2START), dfr.getString(MMEXTREF));
				}
				setBenefit(dep, dfr.getString(DENTAL), getDate(dfr, DENTALSTART));
				setBenefit(dep, dfr.getString(VISION), getDate(dfr, VISIONSTART));

				setBenefit(dep, dfr.getString(VOL1), getDate(dfr, VOL1START), !isEmpty(dfr.getString(VOL1AMT)) ? dfr.getDouble(VOL1AMT) : 0);
				setBenefit(dep, dfr.getString(VOL2), getDate(dfr, VOL2START), !isEmpty(dfr.getString(VOL2AMT)) ? dfr.getDouble(VOL2AMT) : 0);
				setBenefit(dep, dfr.getString(VOL3), getDate(dfr, VOL3START), !isEmpty(dfr.getString(VOL3AMT)) ? dfr.getDouble(VOL3AMT) : 0);
				setBenefit(dep, dfr.getString(VOL4), getDate(dfr, VOL4START), !isEmpty(dfr.getString(VOL4AMT)) ? dfr.getDouble(VOL4AMT) : 0);
				setBenefit(dep, dfr.getString(VOL5), getDate(dfr, VOL5START), !isEmpty(dfr.getString(VOL5AMT)) ? dfr.getDouble(VOL5AMT) : 0);
				setBenefit(dep, dfr.getString(VOL6), getDate(dfr, VOL6START), !isEmpty(dfr.getString(VOL6AMT)) ? dfr.getDouble(VOL6AMT) : 0);
				setBenefit(dep, dfr.getString(VOL7), getDate(dfr, VOL7START), !isEmpty(dfr.getString(VOL7AMT)) ? dfr.getDouble(VOL7AMT) : 0);
				setBenefit(dep, dfr.getString(VOL8), getDate(dfr, VOL8START), !isEmpty(dfr.getString(VOL8AMT)) ? dfr.getDouble(VOL8AMT) : 0);

				hsu.commitTransaction();
				hsu.clear();
				hsu.beginTransaction();
			} catch (Exception e) {
				logger.error("Error while doing import of dependent " + fixSSN(dfr.getString(SSN)) + "; continuing import", e);
			}

		System.out.println("Dependent Import Done.");
	}

	private void setBenefit(BHREmplDependent dep, String bene, int date) {
		setBenefit(dep, bene, date, 0);
	}

	private void setBenefit(BHREmplDependent dep, String bene, int date, double amount) {
		if (isEmpty(bene))
			return;

		HrBenefitJoin sponsor = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene).first();

		if (sponsor == null) {
			logger.error("Benefit " + bene + " must first have been assigned to employee " + dep.getEmployeeNameLFM() + " - " + dep.getNameLFM() + ".");
			return;
			//throw new ArahantWarning("Benefit "+bene+" must first have been assigned to employee "+dep.getEmployeeNameLFM()+".");
		}

		HibernateCriteriaUtil sponsorHsu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmplDependent()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene);
		if (!sponsorHsu.exists()) {
			BHRBenefitJoin bbj = new BHRBenefitJoin();
			bbj.create();
			bbj.setHrBenefitConfig(sponsor.getHrBenefitConfig());
			bbj.setPolicyStartDate(sponsor.getPolicyStartDate());
			bbj.setBenefitApproved(sponsor.getBenefitApproved());
			bbj.copyReason(sponsor);
			bbj.setRelationship(dep.getEmplDependent());
			bbj.setPayingPerson(sponsor.getPayingPerson());
			bbj.setCoveredPerson(dep.getPerson());
			bbj.setCoverageStartDate(date);
			bbj.setAmountCovered(amount);
			bbj.insert(true);
		}
	}

	private void setCASBenefit(BHREmplDependent dep, String bene, int date, String groupId) {
		setCASBenefit(dep, bene, date, groupId, 0);
	}

	private void setCASBenefit(BHREmplDependent dep, String bene, int date, String groupId, double amount) {
		if (isEmpty(bene))
			return;

		boolean medical = bene.contains("Blue Plan");

		HrBenefitJoin sponsor;
		HrBenefitJoin rxSponsor = new HrBenefitJoin();
		if (medical) {
			sponsor = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene).eq(HrBenefit.GROUPID, "MM-" + groupId).first();
			rxSponsor = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene).joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.DESCRIPTION, "Medical Rx").first();
		} else
			sponsor = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene).first();

		if (sponsor == null) {
			logger.error("Benefit " + bene + " must first have been assigned to employee " + dep.getEmployeeNameLFM() + " - " + dep.getNameLFM() + ".");
			return;
			//throw new ArahantWarning("Benefit "+bene+" must first have been assigned to employee "+dep.getEmployeeNameLFM()+".");
		}
		if (rxSponsor == null && medical) {
			logger.error("Benefit " + bene.replaceAll("Blue Plan", "EHIM Plan") + " must first have been assigned to employee " + dep.getEmployeeNameLFM() + " - " + dep.getNameLFM() + ".");
			return;
			//throw new ArahantWarning("Benefit "+bene+" must first have been assigned to employee "+dep.getEmployeeNameLFM()+".");
		}

		HibernateCriteriaUtil sponsorHsu = hsu.createCriteria(HrBenefitJoin.class);
		HibernateCriteriaUtil rxSponsorHsu = hsu.createCriteria(HrBenefitJoin.class);
		if (medical) {
			sponsorHsu.eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmplDependent()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene).eq(HrBenefit.GROUPID, "MM-" + groupId);

			rxSponsorHsu.eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmplDependent()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene.replaceAll("Blue Plan", "EHIM Plan"));
		} else
			sponsorHsu.eq(HrBenefitJoin.PAYING_PERSON, dep.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, dep.getEmplDependent()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.NAME, bene);

		if (!sponsorHsu.exists()) {
			BHRBenefitJoin bbj = new BHRBenefitJoin();
			bbj.create();
			bbj.setHrBenefitConfig(sponsor.getHrBenefitConfig());
			bbj.setPolicyStartDate(sponsor.getPolicyStartDate());
			bbj.setBenefitApproved(sponsor.getBenefitApproved());
			bbj.copyReason(sponsor);
			bbj.setRelationship(dep.getEmplDependent());
			bbj.setPayingPerson(sponsor.getPayingPerson());
			bbj.setCoveredPerson(dep.getPerson());
			bbj.setCoverageStartDate(date);
			bbj.setAmountCovered(amount);
			bbj.insert(true);
		}
		if (!rxSponsorHsu.exists() && medical) {
			BHRBenefitJoin bbj = new BHRBenefitJoin();
			bbj.create();
			bbj.setHrBenefitConfig(rxSponsor.getHrBenefitConfig());
			bbj.setPolicyStartDate(rxSponsor.getPolicyStartDate());
			bbj.setBenefitApproved(rxSponsor.getBenefitApproved());
			bbj.copyReason(rxSponsor);
			bbj.setRelationship(dep.getEmplDependent());
			bbj.setPayingPerson(rxSponsor.getPayingPerson());
			bbj.setCoveredPerson(dep.getPerson());
			bbj.setCoverageStartDate(date);
			bbj.setAmountCovered(amount);
			bbj.insert(true);
		}
	}

	private int getDate(DelimitedFileReader dfr, int col) {
		try {
			//System.out.println(dfr.getString(col));
			return DateUtils.getDate(dfr.getString(col));
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private String fixSSN(String val) {
		if (val == null)
			return val;
		val = val.trim();

		if (val.length() == 11)
			return val;
		if (val.length() == 0)
			return null;
		while (val.length() < 9)
			val = '0' + val;

		if (val.length() > 9)
			val = val.substring(0, 9);

		//	System.out.println(val.substring(0,3)+"-"+val.substring(3,5)+"-"+val.substring(5));
		return val.substring(0, 3) + "-" + val.substring(3, 5) + "-" + val.substring(5);
	}

	protected boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	public static void main(String[] args) {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.beginTransaction();
		try {
			hsu.setCurrentPersonToArahant();
			new DependentImport().doImport("/Users/depsample2.csv");
			hsu.commitTransaction();
		} catch (Exception e) {
			e.printStackTrace();
			hsu.rollbackTransaction();
		}
	}
}
