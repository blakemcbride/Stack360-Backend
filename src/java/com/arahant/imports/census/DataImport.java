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

import com.arahant.beans.HrBenefitChangeReason;
import com.arahant.beans.Person;
import com.arahant.business.*;
import com.arahant.utils.*;
import org.kissweb.StringUtils;

import java.util.List;

public class DataImport {

	private static final ArahantLogger logger = new ArahantLogger(DataImport.class);

	public static void doEmployeeImport(ImportMap data) throws Exception {

		int num = 0;

		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentPersonToArahant();

		data.preProcess();

		String changeReasonId = hsu.createCriteria(HrBenefitChangeReason.class).eq(HrBenefitChangeReason.TYPE, (short) 4).first().getHrBenefitChangeReasonId();


//		DelimitedFileWriter loginMap=new DelimitedFileWriter(data.getLoginFileName());

//		loginMap.writeField("Name");
//		loginMap.writeField("Login ID");
//		loginMap.writeField("Password");
//		loginMap.endRecord();


		if (data.skipFirst())
			data.nextRecord(); //skip first line
		while (data.nextRecord()) {

			ArahantSession.resetAI();

			String ssn = data.getSSN().trim();

			//if a person with this ssn is already in the system...skip them
			if (!StringUtils.isEmpty(ssn) && hsu.createCriteriaNoCompanyFilter(Person.class).eq(Person.SSN, ssn).exists()) {
				logger.error("The ssn " + ssn + " is already in the system!");
				continue;
			}

			BEmployee bemp = createEmployee(data);
			if (bemp == null)
				continue;

			num++;

			assignOrgGroup(data, bemp);
//			System.out.println("Assigned Org Group for " + bemp.getNameLFM());


//			loginMap.writeField(bemp.getNameLFM());
//			loginMap.writeField(user);
//			loginMap.writeField(password);
//			loginMap.endRecord();


			createEmployeeStatus(data, bemp);
//			System.out.println("Created employee status for " + bemp.getNameLFM());

			createEmployeeWageAndPosition(data, bemp);
//			System.out.println("Created employee position and wage information for " + bemp.getNameLFM());

			createBenefitJoin(data, bemp);
//			System.out.println("Created benefit join 1 for " + bemp.getNameLFM());

			createBenefitJoin2(data, bemp, changeReasonId);
//			System.out.println("Created benefit join 2 for " + bemp.getNameLFM());


			//System.out.println(bemp.getSsn());
			//System.out.println("Vacation time "+bemp.getTimeOffCurrentPeriod(data.getVacationBenefitId()));
			//System.out.println("Vacation time current "+data.getCurrentVacationTotal());
			//System.out.println("Vacation time offset "+(bemp.getTimeOffCurrentPeriod(data.getVacationBenefitId())-data.getCurrentVacationTotal()));

			createAccruedTimeOff(data, bemp);
//			System.out.println("Created accrued time off 1 for " + bemp.getNameLFM());


			//System.out.println("Sick time "+bemp.getTimeOffCurrentPeriod(data.getSickBenefitId()));
			//System.out.println("Sick time current "+data.getCurrentSickTotal());
			//System.out.println("Sick time offset "+(bemp.getTimeOffCurrentPeriod(data.getSickBenefitId())-data.getCurrentSickTotal()));

			createAccruedTimeOff2(data, bemp);
//			System.out.println("Created accrued time off 2 for " + bemp.getNameLFM());

			//  I don't think the following method does anything because I don't think this dataset has dependents
			//  I leave it here since I don't have time to throughly determine the facts
//			createDependents(data, bemp, changeReasonId);
//			System.out.println("Created dependents for " + bemp.getNameLFM());

			System.out.println("Importing... " + bemp.getNameLFM());
			hsu.commitTransaction();
			hsu.clear();
			hsu.beginTransaction();
		}
		System.out.println("Done.  Imported " + num);
	}

	private static BEmployee createEmployee(ImportMap data) {
		try {
			BEmployee bemp = new BEmployee();
			bemp.create();
			//System.out.println(data.getSSN());
			bemp.setSsn(data.getSSN().trim());
			bemp.setFirstName(data.getFirstName().trim());
			bemp.setMiddleName(data.getMiddleName().trim());
			bemp.setLastName(data.getLastName().trim());
			if (data.getSex().trim().equals(""))
				bemp.setSex("U");
			else
				bemp.setSex(data.getSex().trim());
			bemp.setDob(data.getDob());
			bemp.setHomePhone(data.getHomePhone().trim());
			bemp.setMobilePhone(data.getMobilePhone().trim());
			bemp.setStreet(data.getAddressLine1().trim());
			bemp.setStreet2(data.getAddressLine2().trim());
			bemp.setCity(data.getCity().trim());
			bemp.setState(data.getStateProvince().trim());
			bemp.setZip(data.getZipPostalCode().trim());
			bemp.setCountry(data.getCountry().trim());
			bemp.setJobTitle(data.getJobTitle().trim());
			bemp.setDriversLicenseNumber(data.getDriversLicenseNumber().trim());
			bemp.setExpectedHoursPerPayPeriod((double) data.getExpectedHoursPerPayPeriod());
			bemp.setBenefitClassId(data.getBenefitClass());

			String empId = data.getEmployeeRef();
			if (empId.length() > 11)
				empId = empId.substring(0, 11);
			bemp.setExtRef(empId);

			//Set default user login and password
			bemp.makeLoginDefaults();

			String mstat = data.getMaritalStatus();
			if (!mstat.equals("M") && !mstat.equals("S"))
				mstat = " ";
			bemp.setMaritalStatus(mstat);
			bemp.setFederalExemptions(data.getFederalExemptions());
			bemp.setFederalExtraWithheld(data.getFederalExtraWithhold());
			bemp.setStateExemptions(data.getStateExemptions());
			bemp.setStateExtraWithheld(data.getStateExtraWithhold());
			bemp.setEarnedIncomeCreditStatus(data.getEarnedIncomeCreditStatus().trim());

			//bemp.setUserPassword(password, true);
			bemp.setSecurityGroupId(data.getSecurityGroupId());
			bemp.setScreenGroupId(data.getScreenGroupId());

			//	System.out.println("Doing insert");
			bemp.insert(true);
//			hsu.flush();
			return bemp;
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + data.getSSN() + "; continuing import", e);
			return null;
		}
	}

	private static void assignOrgGroup(ImportMap data, BEmployee bemp) {
		//if there is an org group specified, assign the employee to that group, if not assign them to the top level company group
		try {
			String orgGroupId = data.getOrgGroup();
			if (orgGroupId != null && orgGroupId.length() == 16) {
				BOrgGroup bog = new BOrgGroup(orgGroupId);
				bog.assignPeopleToGroup(new String[]{bemp.getPersonId()});
			} else {
				List<EmployeeOrgGroup> orgGroups = data.getOrgGroups();
				for (EmployeeOrgGroup orgGroup : orgGroups)
					bemp.assignToOrgGroup(orgGroup.getId(), orgGroup.isPrimary());
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}
	}

	private static void createEmployeeStatus(ImportMap data, BEmployee bemp) {
		try {
			List<EmployeeStatus> statuses = data.getStatuses();
			for (EmployeeStatus status : statuses) {
				BHREmplStatusHistory hist = new BHREmplStatusHistory();

				hist.create();
				hist.setEmployee(bemp.getPerson());
				if (status.getEffectiveDate() > 19000101)
					hist.setEffectiveDate(status.getEffectiveDate());
				else
					hist.setEffectiveDate(20100101);
				hist.setStatusId(status.getId());
				hist.setNotes(status.getNote());
				hist.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}
	}

	private static void createEmployeeWageAndPosition(ImportMap data, BEmployee bemp) {
		try {
			List<EmployeeWageAndPosition> wageAndPositions = data.getWageAndPositions();
			for (EmployeeWageAndPosition wageAndPosition : wageAndPositions) {
				BHRWage wage = new BHRWage();
				wage.create();
				if (wageAndPosition.getWageStartDate() > 19000101)
					wage.setEffectiveDate(wageAndPosition.getWageStartDate());
				else
					wage.setEffectiveDate(20100101);
				wage.setWageAmount(wageAndPosition.getWageAmount());
				wage.setEmployeeId(bemp.getPersonId());
				wage.setWageTypeId(wageAndPosition.getWageTypeId());
				wage.setPositionId(wageAndPosition.getPositionId());
				wage.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}
	}

	private static void createBenefitJoin(ImportMap data, BEmployee bemp) {
		try {
			List<EmployeeBenefit> benefits = data.getStandardBenefits();
			for (EmployeeBenefit benefit : benefits) {
				BHRBenefitJoin benefitJoin = new BHRBenefitJoin();
				benefitJoin.create();
				benefitJoin.setChangeReason(benefit.getChangeReasonId());
				benefitJoin.setCoverageStartDate(benefit.getPolicyStartDate());
				benefitJoin.setCoveredPersonId(bemp.getPersonId());
				benefitJoin.setPayingPersonId(bemp.getPersonId());
				benefitJoin.setPolicyStartDate(benefit.getPolicyStartDate());
				benefitJoin.setPolicyEndDate(benefit.getPolicyEndDate());
				benefitJoin.setAmountCovered(benefit.getAmount());
				benefitJoin.setCoverageStartDate(benefit.getCoverageStartDate() != 0 ? benefit.getCoverageStartDate() : benefit.getPolicyStartDate());
				benefitJoin.setCoverageEndDate(benefit.getCoverageEndDate() != 0 ? benefit.getCoverageEndDate() : benefit.getPolicyEndDate());
				//System.out.println(benefit.getConfigId());
				benefitJoin.setBenefitConfigId(benefit.getConfigId());
				benefitJoin.setComments(benefit.getComment());
				benefitJoin.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}
	}

	private static void createBenefitJoin2(ImportMap data, BEmployee bemp, String changeReasonId) {
		try {
			List<EmployeeBenefit> benefits = data.getBenefits();
			for (EmployeeBenefit benefit : benefits) {
				BHRBenefitJoin benefitJoin = new BHRBenefitJoin();
				benefitJoin.create();
				benefitJoin.setChangeReason(benefit.getChangeReasonId());
				benefitJoin.setCoverageStartDate(benefit.getPolicyStartDate());
				benefitJoin.setCoveredPersonId(bemp.getPersonId());
				benefitJoin.setPayingPersonId(bemp.getPersonId());
				benefitJoin.setAmountCovered(benefit.getAmount());
				benefitJoin.setPolicyStartDate(benefit.getPolicyStartDate());
				benefitJoin.setBenefitConfigId(benefit.getConfigId());
				benefitJoin.setComments(benefit.getComment());
				benefitJoin.setChangeReason(changeReasonId);
				benefitJoin.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}

	}

	private static void createAccruedTimeOff(ImportMap data, BEmployee bemp) {
		try {
			if (data.getVacationBenefitId() != null && data.getCurrentVacationTotal() - bemp.getTimeOffCurrentPeriod(data.getVacationBenefitId()) != 0) {
				BHRAccruedTimeOff offset = new BHRAccruedTimeOff();
				offset.create();
				offset.setAccrualDate(DateUtils.now());
				offset.setAccrualHours(data.getCurrentVacationTotal() - bemp.getTimeOffCurrentPeriod(data.getVacationBenefitId()));
				offset.setDescription("Imported Data");
				offset.setResetTotal(false);
				offset.setEmployeeId(bemp.getPersonId());
				offset.setAccrualAccountId(data.getVacationBenefitId());
				offset.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}

	}

	private static void createAccruedTimeOff2(ImportMap data, BEmployee bemp) {
		try {
			if (data.getSickBenefitId() != null && data.getCurrentSickTotal() - bemp.getTimeOffCurrentPeriod(data.getSickBenefitId()) != 0) {
				BHRAccruedTimeOff offset = new BHRAccruedTimeOff();
				offset.create();
				offset.setAccrualDate(DateUtils.now());
				offset.setAccrualHours(data.getCurrentSickTotal() - bemp.getTimeOffCurrentPeriod(data.getSickBenefitId()));
				offset.setDescription("Imported Data");
				offset.setResetTotal(false);
				offset.setEmployeeId(bemp.getPersonId());
				offset.setAccrualAccountId(data.getSickBenefitId());
				offset.insert();
			}
		} catch (Exception e) {
			logger.error("Error while doing import of employee " + bemp.getSsn() + "; continuing import", e);
		}

	}

	private static void createDependents(ImportMap data, BEmployee bemp, String changeReasonId) {
		boolean hadSpouse = false;
		for (Dependent d : data.getDependents()) {
			Person depPerson = ArahantSession.getHSU().createCriteria(Person.class).eq(Person.SSN, d.getSsn()).first();

			BHREmplDependent dep = new BHREmplDependent();

			if (depPerson != null) {
				//System.out.println("Existing dep");

				dep.create(depPerson.getPersonId(), bemp.getPersonId(), d.getRelationship());

				if (d.getRelationship().equals("S") || d.getRelationship().equals("C"))
					dep.setRelationshipType(d.getRelationship());
				else {
					dep.setRelationshipType("O");
					dep.setRelationship(d.getRelationship());
				}

				if (d.getRelationship().equals("S") && hadSpouse) {
					dep.setRelationshipType("O");
					dep.setRelationship("Spouse");
				}

				if (d.getRelationship().equals("S"))
					hadSpouse = true;

				dep.insert();
			} else {

				//System.out.println("New dep");
				dep.create();
				dep.setLastName(d.getLname());
				dep.setFirstName(d.getFname());
				dep.setDob(d.getDob());
				dep.setSex(d.getSex());
				dep.setSsn(d.getSsn());
				dep.setStudent(d.getStudent().equals("Y"));
				if (d.getRelationship().equals("S") || d.getRelationship().equals("C"))
					dep.setRelationshipType(d.getRelationship());
				else {
					dep.setRelationshipType("O");
					dep.setRelationship(d.getRelationship());
				}

				if (d.getRelationship().equals("S") && hadSpouse) {
					dep.setRelationshipType("O");
					dep.setRelationship("Spouse");
				}

				if (d.getRelationship().equals("S"))
					hadSpouse = true;

				dep.setEmployeeId(bemp.getPersonId());
				dep.insert();
			}

			//now handle the benefits
			for (EmployeeBenefit eb : d.getBenefits()) {
				BHRBenefitJoin bj = new BHRBenefitJoin();
				bj.create();
				bj.setPayingPerson(bemp.getPerson());
				bj.setCoveredPerson(dep.getPerson());
				bj.setPolicyStartDate(eb.getPolicyStartDate());
				bj.setAmountCovered(eb.getAmount());
				bj.setPolicyEndDate(eb.getPolicyEndDate());
				bj.setCoverageStartDate(eb.getCoverageStartDate());
				bj.setCoverageEndDate(eb.getCoverageEndDate());
				bj.setBenefitConfigId(eb.getConfigId());
				bj.setChangeReason(eb.getChangeReasonId());
				bj.setComments(eb.getComment());
				bj.setChangeReason(changeReasonId);
				bj.setRelationship(dep.getEmplDependent());
				bj.insert(true);
			}
		}
	}
}
