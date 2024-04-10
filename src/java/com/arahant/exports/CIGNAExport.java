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


package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.utils.*;
import org.kissweb.DelimitedFileReader;
import org.kissweb.DelimitedFileWriter;

import java.io.File;

/**
 *
 */
public class CIGNAExport {

	public String export() throws Exception {
		File csvFile = FileSystemUtils.createTempFile("CIGNA", ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath());

		HibernateSessionUtil hsu = ArahantSession.getHSU();

		dfw.writeField("Branch Code");
		dfw.writeField("Effective Date");
		dfw.writeField("Eligible Employee SSN");
		dfw.writeField("Eligible Employee ID");
		dfw.writeField("Member SSN");
		dfw.writeField("Last Name");
		dfw.writeField("First Name");
		dfw.writeField("Birth Date");
		dfw.writeField("Gender");
		dfw.writeField("Relationship Code");
		dfw.writeField("Full Time Student");
		dfw.writeField("Street Address 1");
		dfw.writeField("Street Address 2");
		dfw.writeField("City");
		dfw.writeField("State");
		dfw.writeField("Zip Code");
		dfw.writeField("Date of Hire");
		dfw.writeField("Salary");
		dfw.writeField("Frequency");
		dfw.writeField("Member Home Phone");
		dfw.writeField("Member Work Phone");
		dfw.writeField("Medical Benefit Option");
		dfw.writeField("PCP Number");
		dfw.writeField("Existing Patient");
		dfw.writeField("PCL End Date");
		dfw.writeField("Dental Benefit Option");
		dfw.writeField("Dental Office Code");
		dfw.writeField("Dental Late Entrant Applies");
		dfw.writeField("Dental Waiting Period Applies");
		dfw.writeField("Dental Orig Eff Date");
		dfw.writeField("Vision Benefit Option");
		dfw.writeField("FSA-Health Benefit Option");
		dfw.writeField("FSA HC Employer Goal Amount");
		dfw.writeField("FSA HC Employee Goal Amount");
		dfw.writeField("Medical Auto Claim Forward");
		dfw.writeField("Dental Auto Claim Forward");
		dfw.writeField("Pharmacy Auto Claim Forward");
		dfw.writeField("FSA-Dep Benefit Option");
		dfw.writeField("FSA DC Employer Goal Amount");
		dfw.writeField("FSA DC Employee Goal Amount");
		dfw.writeField("COBRA Effective Date");
		dfw.writeField("Other Medical Insurance Coverage");
		dfw.writeField("Carrier	Policy Number");
		dfw.writeField("Other Ins Effective Date");
		dfw.writeField("Medicare Type");
		dfw.writeField("HIC Number");
		dfw.writeField("Primary Code");
		dfw.writeField("Financial Responsibility");
		dfw.writeField("Cancellation Date");
		dfw.writeField("Current Benefit Option");
		dfw.writeField("Future Benefit Option");
		dfw.writeField("Change Date");
		dfw.endRecord();


		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.SSN).scroll();

		while (scr.next()) {
			BEmployee bemp = new BEmployee(scr.get());

			//Account Details
			/*
			 * String branchCode=hsu.createCriteria(OrgGroup.class)
			 * .addReturnField(OrgGroup.EXTERNAL_REF)
			 * .joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
			 * .eq(OrgGroupAssociation.PERSON, bemp.getEmployee())
			 * .first().toString();
			 */

			String branchCode = getMedicalDivision(bemp.getState(), bemp.getCity(), bemp.getExtRef());
			int effectiveDate = bemp.getHireDate();  //replace with most recent policy change or coverage change date?
			String eligibleEmpSsn = bemp.getSsn();
			String eligibleEmpId = bemp.getExtRef();
			String memberSsn = "";

			//String clientId=""; //figure out where to get this
			//String accountNumber=""; // figure out where to get this

			//Member Information
			//String ssn=bemp.getSsn();
			//String employeeId=bemp.getExtRef();

			String lname = bemp.getLastName();
			String fname = bemp.getFirstName();
			int dob = bemp.getDob();
			String gender = bemp.getSex();
			String relationship = "EE";
			String fullTimeStudent = "N";
			String street1 = bemp.getStreet();
			String street2 = bemp.getStreet2();
			String city = bemp.getCity();
			String state = bemp.getState();
			String zip = bemp.getZip();
			int dateOfHire = bemp.getHireDate();

			String salary = ""; //TODO
			String frequency = ""; //TODO

			String phoneHome = bemp.getHomePhone();
			String phoneWork = bemp.getWorkPhone();

			//medical
			String medicalCode = getBenefitCode(bemp, HrBenefitCategory.HEALTH);
			String pcpCode = getPCPCode(bemp, HrBenefitCategory.HEALTH);
			String existingPatient = "Y"; //TODO: need this
			int pclEndDate = 0; //TODO: what is this?

			//dental
			String dentalCode = getBenefitCode(bemp, HrBenefitCategory.DENTAL);
			String dentalOfficeCode = getPCPCode(bemp, HrBenefitCategory.DENTAL);
			String dentalLateEntrantApplies = "N"; //TODO need this
			String dentalWaitPeriodApplies = "N"; //TODO need this
			int dentalOriginalEffectiveDate = getCoverageStartDate(bemp, HrBenefitCategory.DENTAL);

			//vision
			String visionCode = getBenefitCode(bemp, HrBenefitCategory.VISION);

			//FSA Health
			String flexHealthCode = getBenefitCode(bemp, HrBenefitCategory.FLEX_TYPE); //this won't really work, how to know it's health flex?
			double employerGoalAmount = 0; //TODO
			double employeeGoalAmount = 0; //TODO
			double medicalAutoClaimForward = 0; //TODO
			double dentalAutoClaimForward = 0; //TODO
			double pharmacyAutoClaimForward = 0; //TODO

			//FSA Dependent Care
			String flexDependentCode = ""; //TODO
			double depEmployerGoalAmount = 0; //TODO
			double depEmployeeGoalAmount = 0; //TODO

			//COBRA
			int cobraEffectiveDate = 0; //TODO

			//Other Medical
			String hasOtherMedical = ""; //TODO
			String otherMedCarrier = ""; //TODO
			int otherMedEffectiveDate = 0; //TODO

			String medicareType = ""; //TODO
			//HIC
			String otherMedPolicyNumber = ""; //TODO
			String primaryCode = ""; //TODO
			String financialResponsibility = ""; //TODO

			//Terminate EE
			int cancellationDate = 0; //TODO

			//Change benefit options
			String currentBenefitOption = ""; //TODO
			String futureBenefitOption = ""; //TODO
			int changeDate = 0; //TODO

			//call method to write this line out

			writeRecord(dfw, branchCode, effectiveDate, eligibleEmpSsn, eligibleEmpId, memberSsn, lname, fname, dob, gender, relationship,
					fullTimeStudent, street1, street2, city, state, zip, dateOfHire, salary, frequency, phoneHome, phoneWork, medicalCode,
					pcpCode, existingPatient, pclEndDate, dentalCode, dentalOfficeCode, dentalLateEntrantApplies, dentalWaitPeriodApplies,
					dentalOriginalEffectiveDate, visionCode, flexHealthCode, employerGoalAmount, employeeGoalAmount, medicalAutoClaimForward,
					dentalAutoClaimForward, pharmacyAutoClaimForward, flexDependentCode, depEmployerGoalAmount, depEmployeeGoalAmount,
					cobraEffectiveDate, hasOtherMedical, otherMedCarrier, otherMedEffectiveDate, medicareType, otherMedPolicyNumber,
					primaryCode, financialResponsibility, cancellationDate, currentBenefitOption, futureBenefitOption, changeDate);

			//for every dependent
			for (BHREmplDependent dep : bemp.getDependents()) {

				//replace fields that change on deps
				BPerson bp = new BPerson(dep.getPerson());
				lname = bp.getLastName();
				fname = bp.getFirstName();
				dob = bp.getDob();
				relationship = dep.getRelationship() + ""; //TODO: translate into their code
				fullTimeStudent = dep.getStudent() ? "Y" : "N";
				medicalCode = getBenefitCode(bemp, HrBenefitCategory.HEALTH, bp);
				pcpCode = getPCPCode(bemp, HrBenefitCategory.HEALTH, bp);
				dentalCode = getBenefitCode(bemp, HrBenefitCategory.DENTAL, bp);
				dentalOfficeCode = getPCPCode(bemp, HrBenefitCategory.DENTAL, bp);
				dentalOriginalEffectiveDate = getCoverageStartDate(bemp, HrBenefitCategory.DENTAL, bp);
				visionCode = getBenefitCode(bemp, HrBenefitCategory.VISION, bp);
				flexHealthCode = getBenefitCode(bemp, HrBenefitCategory.FLEX_TYPE, bp);



				//call method to write this line out
				writeRecord(dfw, branchCode, effectiveDate, eligibleEmpSsn, eligibleEmpId, memberSsn, lname, fname, dob, gender, relationship,
						fullTimeStudent, street1, street2, city, state, zip, dateOfHire, salary, frequency, phoneHome, phoneWork, medicalCode,
						pcpCode, existingPatient, pclEndDate, dentalCode, dentalOfficeCode, dentalLateEntrantApplies, dentalWaitPeriodApplies,
						dentalOriginalEffectiveDate, visionCode, flexHealthCode, employerGoalAmount, employeeGoalAmount, medicalAutoClaimForward,
						dentalAutoClaimForward, pharmacyAutoClaimForward, flexDependentCode, depEmployerGoalAmount, depEmployeeGoalAmount,
						cobraEffectiveDate, hasOtherMedical, otherMedCarrier, otherMedEffectiveDate, medicareType, otherMedPolicyNumber,
						primaryCode, financialResponsibility, cancellationDate, currentBenefitOption, futureBenefitOption, changeDate);
			}
		}

		scr.close();
		dfw.close();

		return FileSystemUtils.getHTTPPath(csvFile);

	}

	private int getCoverageStartDate(BEmployee bemp, short type) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).
				joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee());

		HrBenefitJoin bj = hcu.first();

		if (bj == null)
			return 0;

		return bj.getCoverageStartDate();

	}

	private int getCoverageStartDate(BEmployee bemp, short type, BPerson bp) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).
				joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson());

		HrBenefitJoin bj = hcu.first();

		if (bj == null)
			return 0;

		return bj.getCoverageStartDate();

	}

	private String getPCPCode(BEmployee bemp, short type) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).
				joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee());

		HrBenefitJoin bj = hcu.first();

		if (bj == null)
			return "";

		return bj.getComments();

	}

	private String getPCPCode(BEmployee bemp, short type, BPerson bp) {
		HibernateCriteriaUtil<HrBenefitJoin> hcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).
				joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee()).eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson());

		HrBenefitJoin bj = hcu.first();

		if (bj == null)
			return "";

		return bj.getComments();

	}

	private String getBenefitCode(BEmployee bemp, short type) {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);
		hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee());

		HrBenefit b = hcu.first();

		if (b == null)
			return "";
		return b.getInsuranceCode();

	}

	private String getBenefitCode(BEmployee bemp, short type, BPerson bp) {
		HibernateCriteriaUtil<HrBenefit> hcu = ArahantSession.getHSU().createCriteria(HrBenefit.class);
		hcu.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, type);
		hcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS).joinTo(HrBenefitConfig.HR_BENEFIT_JOINS).eq(HrBenefitJoin.COVERED_PERSON, bp.getPerson()).eq(HrBenefitJoin.PAYING_PERSON, bemp.getEmployee());

		HrBenefit b = hcu.first();

		if (b == null)
			return "";
		return b.getInsuranceCode();

	}

	private void writeRecord(DelimitedFileWriter dfw, String branchCode, int effectiveDate, String eligibleEmpSsn, String eligibleEmpId, String memberSsn, String lname, String fname, int dob, String gender, String relationship, String fullTimeStudent, String street1, String street2, String city, String state, String zip, int dateOfHire, String salary, String frequency, String phoneHome, String phoneWork, String medicalCode, String pcpCode, String existingPatient, int pclEndDate, String dentalCode, String dentalOfficeCode, String dentalLateEntrantApplies, String dentalWaitPeriodApplies, int dentalOriginalEffectiveDate, String visionCode, String flexHealthCode, double employerGoalAmount, double employeeGoalAmount, double medicalAutoClaimForward, double dentalAutoClaimForward, double pharmacyAutoClaimForward, String flexDependentCode, double depEmployerGoalAmount, double depEmployeeGoalAmount, int cobraEffectiveDate, String hasOtherMedical, String otherMedCarrier, int otherMedEffectiveDate, String medicareType, String otherMedPolicyNumber, String primaryCode, String financialResponsibility, int cancellationDate, String currentBenefitOption, String futureBenefitOption, int changeDate) throws Exception {

		if (medicalCode.trim().equals(""))
			return;
		dfw.writeField(branchCode);
		dfw.writeField(effectiveDate);
		dfw.writeField(eligibleEmpSsn);
		dfw.writeField(eligibleEmpId);
		dfw.writeField(memberSsn);
		dfw.writeField(lname);
		dfw.writeField(fname);
		dfw.writeField(dob);
		dfw.writeField(gender);
		dfw.writeField(relationship);
		dfw.writeField(fullTimeStudent);
		dfw.writeField(street1);
		dfw.writeField(street2);
		dfw.writeField(city);
		dfw.writeField(state);
		dfw.writeField(zip);
		dfw.writeField(dateOfHire);
		dfw.writeField(salary);
		dfw.writeField(frequency);
		dfw.writeField(phoneHome);
		dfw.writeField(phoneWork);
		dfw.writeField(medicalCode);
		dfw.writeField(pcpCode);
		dfw.writeField(existingPatient);
		dfw.writeField(pclEndDate);
		dfw.writeField(dentalCode);
		dfw.writeField(dentalOfficeCode);
		dfw.writeField(dentalLateEntrantApplies);
		dfw.writeField(dentalWaitPeriodApplies);
		dfw.writeField(dentalOriginalEffectiveDate);
		dfw.writeField(visionCode);
		dfw.writeField(flexHealthCode);
		dfw.writeField(employerGoalAmount);
		dfw.writeField(employeeGoalAmount);
		dfw.writeField(medicalAutoClaimForward);
		dfw.writeField(dentalAutoClaimForward);
		dfw.writeField(pharmacyAutoClaimForward);
		dfw.writeField(flexDependentCode);
		dfw.writeField(depEmployerGoalAmount);
		dfw.writeField(depEmployeeGoalAmount);
		dfw.writeField(cobraEffectiveDate);
		dfw.writeField(hasOtherMedical);
		dfw.writeField(otherMedCarrier);
		dfw.writeField(otherMedEffectiveDate);
		dfw.writeField(medicareType);
		dfw.writeField(otherMedPolicyNumber);
		dfw.writeField(primaryCode);
		dfw.writeField(financialResponsibility);
		dfw.writeField(cancellationDate);
		dfw.writeField(currentBenefitOption);
		dfw.writeField(futureBenefitOption);
		dfw.writeField(changeDate);
		dfw.endRecord();

	}

	public static void main(String args[]) throws Exception {
		ArahantSession.getHSU().setCurrentPersonToArahant();

		CIGNAExport x = new CIGNAExport();
		x.export();
	}

	/*
	 *
	 * 001- NY ACTIVES 002- SOUTH HAMPTON, NY EMPLOYEES 003- PHILADELPHIA, PA
	 * EMPLOYEES 004- WASHINGTON, DC EMPLOYEES 005- MIAMI, FL EMPLOYEES 006-
	 * PALM BEACH, FL EMPLOYEES 007- HOUSTON, TX EMPLOYEES 008- PORTLAND, OR
	 * EMPLOYEES 009- SAN FANCISCO, CA EMPLOYEES 010 - SEATTLE, WA EMPLOYEES
	 */
	private String getMedicalDivision(String state, String city, String empid) throws Exception {

		state = state.toUpperCase();
		if (state.equals("NY") || city.trim().equalsIgnoreCase("Brooklyn")) {

			if (empid.trim().equals("983920")) //wiltshire john
				return "002";

			if (empid.trim().equals("111002")) //mott kimbelry
				return "002";

			return "001";

		}
		if (state.equals("NJ"))
			return "001";
		if (state.equals("RI"))
			return "001";
		if (state.equals("CT"))
			return "001";
		if (state.equals("DE"))
			return "003";
		if (state.equals("PA"))
			return "003";
		if (state.equals("DC") || state.equals("VA"))
			return "004";
		if (state.equals("MD"))
			return "004";
		if (state.equals("SC"))
			return "004";
		if (state.equals("FL") || city.equals("Miami") || city.equals("Coral Springs"))
			//look for
//fd4  miami
			if (getPayrollCode(empid).equals("fd4"))
				return "005";
			else
				return "006"; //rms  palm beach



		if (state.equals("TX") || city.equals("Missouri City"))
			return "007";
		if (state.equals("CA") || city.equals("San Mateo") || city.equals("Alameda"))
			return "009";
		if (state.equals("OR"))
			return "008";
		if (state.equals("WA"))
			return "010";

		System.out.println("didn't find " + state + " " + city);
		return "001";
	}

	public String getPayrollCode(String empid) throws Exception {
		if (empid.trim().equals(""))
			return "";
		if (empid.startsWith("\""))
			empid = empid.substring(1);
		if (empid.endsWith("\""))
			empid = empid.substring(0, empid.length() - 1);

		DelimitedFileReader dfr = new DelimitedFileReader("/Users/Arahant/FrankCrystal/payrollcompanyimport3.csv", '\t', '\"');

		String ret = "";
		while (dfr.nextLine()) {
			String id = dfr.getString(0).substring(1).trim();

			String v = "";

			for (int loop = 0; loop < id.length(); loop++)
				if (id.charAt(loop) >= '0' && id.charAt(loop) <= '9')
					v += id.charAt(loop);

			id = v;
			//	System.out.println(id.length());
			int i1 = Integer.parseInt(id);
			int i2 = Integer.parseInt(empid.trim());

			if (i1 == i2) {
				ret = stripWackyStuff(dfr.getString(1).trim());
				break;
			}
		}

		if ("".equals(ret))
			System.out.println("did not find " + empid.trim());
		return ret;
	}

	public String stripWackyStuff(String x) {
		String v = "";
		for (int loop = 0; loop < x.length(); loop++)
			if (x.charAt(loop) != '\"') {
				v += x.charAt(loop);
				v = v.trim();
			}

		return v;
	}
}

/*
 * 	Branch Code	Effective Date	Eligible Employee SSN	Eligible Employee ID	Member SSN	Last Name	First Name	Birth Date	Gender	Relationship Code	Full Time Student	Street Address 1	Street Address 2	City	State	Zip Code	Date of Hire	Salary	Frequency	Member Home Phone	Member Work Phone	Medical Benefit Option	PCP Number	Existing Patient	PCL End Date	Dental Benefit Option	Dental Office Code	Dental Late Entrant Applies	Dental Waiting Period Applies	Dental Orig Eff Date	Vision Benefit Option	FSA-Health Benefit Option	FSA HC Employer Goal Amount	FSA HC Employee Goal Amount	Medical Auto Claim Forward	Dental Auto Claim Forward	Pharmacy Auto Claim Forward	FSA-Dep Benefit Option	FSA DC Employer Goal Amount	FSA DC Employee Goal Amount	COBRA Effective Date	Other Medical Insurance Coverage	Carrier	Policy Number	Other Ins Effective Date	Medicare Type	HIC Number	Primacy Code	Financial Responsibility	Cancellation Date	Current Benefit Option	Future Benefit Option	Change Date						Branch Code	Effective Date	Eligible Employee SSN	Eligible Employee ID	Member SSN	Last Name	First Name	Birth Date	Gender	Relationship Code	Full Time Student	Street Address 1	Street Address 2	City	State	Zip Code	Date of Hire	Salary	Frequency	Member Home Phone	Member Work Phone	Medical Benefit Option	PCP Number	Existing Patient	PCL End Date	Dental Benefit Option	Dental Office Code	Dental Late Entrant Applies	Dental Waiting Period Applies	Dental Orig Eff Date	Vision Benefit Option	FSA-Health Benefit Option	FSA HC Employer Goal Amount	FSA HC Employee Goal Amount	Medical Auto Claim Forward	Dental Auto Claim Forward	Pharmacy Auto Claim Forward	FSA-Dep Benefit Option	FSA DC Employer Goal Amount	FSA DC Employee Goal Amount	COBRA Effective Date	Other Medical Insurance Coverage	Carrier	Policy Number	Other Ins Effective Date	Medicare Type	HIC Number	Primacy Code	Financial Responsibility	Cancellation Date	Current Benefit Option	Future Benefit Option	Change Date
 */
