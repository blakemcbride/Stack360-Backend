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

import com.arahant.beans.Employee;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BEmployee;
import com.arahant.utils.*;
import java.io.File;
import java.text.SimpleDateFormat;

import org.kissweb.DelimitedFileWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class CIGNARequestsExportMoelis {

	HibernateSessionUtil hsu;

	public String export() throws Exception {
		File csvFile = FileSystemUtils.createTempFile("CIGNARequests", ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath());

		hsu = ArahantSession.getHSU();

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


		HibernateScrollUtil<Employee> scr = hsu.createCriteria(Employee.class).orderBy(Employee.SSN).joinTo(Employee.CHANGE_REQUESTS).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_CIGNA).scroll();

		String pid = "";
		while (scr.next()) {
			BEmployee bemp = new BEmployee(scr.get());

			if (pid.equals(bemp.getPersonId()))
				continue;

			pid = bemp.getPersonId();

			PersonChangeRequest pcr = hsu.createCriteria(PersonChangeRequest.class).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_CIGNA).eq(PersonChangeRequest.PERSON, bemp.getEmployee()).orderByDesc(PersonChangeRequest.REQUEST_DATE).first();

			if (pcr == null)
				continue;

			Document doc = DOMUtils.createDocument(pcr.getRequestData());
			Node n = DOMUtils.getNode(doc, "CIGNADATA");

			//Account Details
			/*
			 * String branchCode=hsu.createCriteria(OrgGroup.class)
			 * .addReturnField(OrgGroup.EXTERNAL_REF)
			 * .joinTo(OrgGroup.ORGGROUPASSOCIATIONS)
			 * .eq(OrgGroupAssociation.PERSON, bemp.getEmployee())
			 * .first().toString();
			 */

			String branchCode = bemp.getOrgGroupName();
			int effectiveDate = bemp.getHireDate();  //replace with most recent policy change or coverage change date?
			String eligibleEmpSsn = DOMUtils.getString(n, "employeeSSN");
			String eligibleEmpId = bemp.getExtRef();
			String memberSsn = "";

			//String clientId=""; //figure out where to get this
			//String accountNumber=""; // figure out where to get this

			//Member Information
			//String ssn=bemp.getSsn();
			//String employeeId=bemp.getExtRef();


			String lname = DOMUtils.getString(n, "employeeLastName");
			String fname = DOMUtils.getString(n, "employeeFirstName");
			String dob = DateUtils.getDateFormatted(DOMUtils.getInt(n, "employeeDOB"));

			bemp.setDob(DOMUtils.getInt(n, "employeeDOB"));
			bemp.update();

			String gender = bemp.getSex();
			String relationship = "EE";
			String fullTimeStudent = "N";
			String street1 = DOMUtils.getString(n, "employeeAddress");
			String street2 = "";
			String city = DOMUtils.getString(n, "employeeCity");
			String state = DOMUtils.getString(n, "employeeState");
			String zip = DOMUtils.getString(n, "employeeZip");
			String dateOfHire = "";

			String salary = ""; //MoneyUtils.formatMoney(bemp.getCurrentSalary());
			String frequency = ""; //TODO

			String phoneHome = DOMUtils.getString(n, "employeePhone");
			String phoneWork = DOMUtils.getString(n, "employeeWorkPhone");

			//medical


			String medicalCode = "";

			/*
			 * <openAccessCore>FALSE</openAccessCore>
			 * <openAccessPremium>TRUE</openAccessPremium>
			 *
			 */
			//if open access core, set medical code
			if ("TRUE".equals(DOMUtils.getString(n, "openAccessCore")))
				medicalCode = "OpenAccessCore";
			//if open access premium set medical code
			if ("TRUE".equals(DOMUtils.getString(n, "openAccessPremium")))
				medicalCode = "openAccessPremium";

			String pcpCode = "";
			String existingPatient = ""; //TODO: need this
			int pclEndDate = 0; //TODO: what is this?

			//dental
			String dentalCode = "";
			String dentalOfficeCode = "";
			String dentalLateEntrantApplies = ""; //TODO need this
			String dentalWaitPeriodApplies = ""; //TODO need this
			String dentalOriginalEffectiveDate = "";

			//vision
			String visionCode = "";

			//FSA Health
			String flexHealthCode = ""; //this won't really work, how to know it's health flex?
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


			NodeList depNodes = DOMUtils.getNodes(doc, "//dependent");
			//for every dependent
			for (int loop = 0; loop < depNodes.getLength(); loop++) {

				Node depNode = depNodes.item(loop);

				//get all the selected ids

				memberSsn = DOMUtils.getString(depNode, "personSSN");

				//replace fields that change on deps
				lname = DOMUtils.getString(depNode, "personLastName");
				fname = DOMUtils.getString(depNode, "personFirstName");
				dob = DateUtils.getDateFormatted(getDate(DOMUtils.getString(depNode, "personDOB")));
				relationship = ""; //TODO: translate into their code
				fullTimeStudent = "";
				pcpCode = "";//getPCPCode(pcr, HrBenefitCategory.HEALTH, bp);
				dentalOriginalEffectiveDate = "";
				flexHealthCode = "";
				gender = DOMUtils.getString(depNode, "personGender");

				hasOtherMedical = "";
				otherMedCarrier = "";
				medicareType = "";

				//do I have the same person in other medical with other
				NodeList otherNodes = DOMUtils.getNodes(doc, "//other");
				for (int oloop = 0; oloop < otherNodes.getLength(); oloop++) {
					Node other = otherNodes.item(oloop);

					if (DOMUtils.getString(other, "personLastName").equals(lname)
							&& DOMUtils.getString(other, "personFirstName").equals(fname)
							&& DOMUtils.getString(other, "personSSN").equals(memberSsn))
						if (DOMUtils.getString(other, "personOther").equals("TRUE")) {
							hasOtherMedical = "TRUE";

							if (DOMUtils.getString(other, "personMedicareA").equals("TRUE")) {
								otherMedCarrier = "Medicare";
								medicareType = "A";
							}

							if (DOMUtils.getString(other, "personMedicareB").equals("TRUE")) {
								otherMedCarrier = "Medicare";
								medicareType = "B";
							}

							if (DOMUtils.getString(other, "personMedicaid").equals("TRUE")) {
								otherMedCarrier = "Medicaid";
								medicareType = "";
							}
						}
				}


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

	private boolean isEmpty(String str) {
		return str == null || "".equals(str.trim());
	}

	private void writeRecord(DelimitedFileWriter dfw, String branchCode, int effectiveDate, String eligibleEmpSsn, String eligibleEmpId, String memberSsn, String lname, String fname, String dob, String gender, String relationship, String fullTimeStudent, String street1, String street2, String city, String state, String zip, String dateOfHire, String salary, String frequency, String phoneHome, String phoneWork, String medicalCode, String pcpCode, String existingPatient, int pclEndDate, String dentalCode, String dentalOfficeCode, String dentalLateEntrantApplies, String dentalWaitPeriodApplies, String dentalOriginalEffectiveDate, String visionCode, String flexHealthCode, double employerGoalAmount, double employeeGoalAmount, double medicalAutoClaimForward, double dentalAutoClaimForward, double pharmacyAutoClaimForward, String flexDependentCode, double depEmployerGoalAmount, double depEmployeeGoalAmount, int cobraEffectiveDate, String hasOtherMedical, String otherMedCarrier, int otherMedEffectiveDate, String medicareType, String otherMedPolicyNumber, String primaryCode, String financialResponsibility, int cancellationDate, String currentBenefitOption, String futureBenefitOption, int changeDate) throws Exception {

		phoneHome = phoneHome.replaceAll("\\(", "").replaceAll("\\.", "").replaceAll("\\)", "").replaceAll("-", "");
		phoneWork = phoneWork.replaceAll("\\(", "").replaceAll("\\.", "").replaceAll("\\)", "").replaceAll("-", "");


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
	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

	private int getDate(String dobStr) throws Exception {
		if (dobStr.equals("NaN"))
			return 0;
		try {
			return DateUtils.getDate(sdf.parse(dobStr.replaceAll("GMT-0400", "").replaceAll("GMT-0500", "").replaceAll("GMT-0600", "").replaceAll("GMT-0700", "").replaceAll("GMT-0800", "")));
		} catch (Exception e) {
			return 0;
		}
	}

	public static void main(String args[]) throws Exception {
		ArahantSession.getHSU().setCurrentPersonToArahant();

		ArahantSession.getHSU().beginTransaction();
		CIGNARequestsExportMoelis x = new CIGNARequestsExportMoelis();
		x.export();
		ArahantSession.getHSU().commitTransaction();
	}
}

/*
 * 	Branch Code	Effective Date	Eligible Employee SSN	Eligible Employee ID	Member SSN	Last Name	First Name	Birth Date	Gender	Relationship Code	Full Time Student	Street Address 1	Street Address 2	City	State	Zip Code	Date of Hire	Salary	Frequency	Member Home Phone	Member Work Phone	Medical Benefit Option	PCP Number	Existing Patient	PCL End Date	Dental Benefit Option	Dental Office Code	Dental Late Entrant Applies	Dental Waiting Period Applies	Dental Orig Eff Date	Vision Benefit Option	FSA-Health Benefit Option	FSA HC Employer Goal Amount	FSA HC Employee Goal Amount	Medical Auto Claim Forward	Dental Auto Claim Forward	Pharmacy Auto Claim Forward	FSA-Dep Benefit Option	FSA DC Employer Goal Amount	FSA DC Employee Goal Amount	COBRA Effective Date	Other Medical Insurance Coverage	Carrier	Policy Number	Other Ins Effective Date	Medicare Type	HIC Number	Primacy Code	Financial Responsibility	Cancellation Date	Current Benefit Option	Future Benefit Option	Change Date						Branch Code	Effective Date	Eligible Employee SSN	Eligible Employee ID	Member SSN	Last Name	First Name	Birth Date	Gender	Relationship Code	Full Time Student	Street Address 1	Street Address 2	City	State	Zip Code	Date of Hire	Salary	Frequency	Member Home Phone	Member Work Phone	Medical Benefit Option	PCP Number	Existing Patient	PCL End Date	Dental Benefit Option	Dental Office Code	Dental Late Entrant Applies	Dental Waiting Period Applies	Dental Orig Eff Date	Vision Benefit Option	FSA-Health Benefit Option	FSA HC Employer Goal Amount	FSA HC Employee Goal Amount	Medical Auto Claim Forward	Dental Auto Claim Forward	Pharmacy Auto Claim Forward	FSA-Dep Benefit Option	FSA DC Employer Goal Amount	FSA DC Employee Goal Amount	COBRA Effective Date	Other Medical Insurance Coverage	Carrier	Policy Number	Other Ins Effective Date	Medicare Type	HIC Number	Primacy Code	Financial Responsibility	Cancellation Date	Current Benefit Option	Future Benefit Option	Change Date
 */
