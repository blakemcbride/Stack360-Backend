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
package com.arahant.reports;

import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.beans.PersonChangeRequest;
import com.arahant.business.BPerson;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.DOMUtils;
import com.arahant.utils.DateUtils;
import com.itextpdf.text.pdf.PdfPTable;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class PersonChangeRequestReport extends ReportBase {

	public PersonChangeRequestReport() throws ArahantException {
		super("changes", "");
	}
	int tableSizes[] = new int[]{20, 80};
	private boolean showSSN = false;
	boolean didAddress = false;

	public String build(String personId, boolean showSSN) throws Exception {

		try {

			this.showSSN = showSSN;
			PdfPTable table;

			//     addHeaderLine();



			table = makeTable(tableSizes);

			BPerson bp = new BPerson(personId);

			table = makeTable(new int[]{100});
			write(table, bp.getNameLFM());
			addTable(table);
			table = makeTable(tableSizes);

			addTable(table);

			table = makeTable(new int[]{100});
			seperatorLine(table);
			addTable(table);
			table = makeTable(tableSizes);

			PersonChangeRequest pcr = hsu.createCriteria(PersonChangeRequest.class).eq(PersonChangeRequest.PERSON, bp.getPerson()).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_CIGNA).orderByDesc(PersonChangeRequest.REQUEST_DATE).first();

			if (pcr != null) {
				addTable(table);
				writeLine("CIGNA (Submitted " + DateUtils.getDateTimeFormatted(pcr.getRequestDate()) + ")");
				table = makeTable(tableSizes);
				table = parseCigna(table, bp, pcr);
				addTable(table);
				table = makeTable(tableSizes);
			}

			pcr = hsu.createCriteria(PersonChangeRequest.class).eq(PersonChangeRequest.PERSON, bp.getPerson()).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_METLIFE).orderByDesc(PersonChangeRequest.REQUEST_DATE).first();
			if (pcr != null) {
				table = makeTable(new int[]{100});
				seperatorLine(table);
				addTable(table);
				table = makeTable(tableSizes);
				addTable(table);
				writeLine("MetLife (Submitted " + DateUtils.getDateTimeFormatted(pcr.getRequestDate()) + ")");
				table = makeTable(tableSizes);
				parseMetLife(table, bp, pcr);
				addTable(table);
				table = makeTable(tableSizes);
			}

			pcr = hsu.createCriteria(PersonChangeRequest.class).eq(PersonChangeRequest.PERSON, bp.getPerson()).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_MUTUAL_OF_OMAHA).orderByDesc(PersonChangeRequest.REQUEST_DATE).first();
			if (pcr != null) {
				table = makeTable(new int[]{100});
				seperatorLine(table);
				addTable(table);
				table = makeTable(tableSizes);
				addTable(table);
				writeLine("Mutual of Omaha (Submitted " + DateUtils.getDateTimeFormatted(pcr.getRequestDate()) + ")");
				table = makeTable(tableSizes);
				parseMutualOfOmaha(table, bp, pcr);
				addTable(table);
				table = makeTable(tableSizes);
			}
			else
			{

				table = makeTable(new int[]{100});
				seperatorLine(table);
				addTable(table);
				table = makeTable(tableSizes);
				addTable(table);
				writeLine("Mutual of Omaha");
				table = makeTable(tableSizes);

				//STD
				//LTD
				//Basic Life AD&D Yes
				//AD&D
				writeField(table, "STD", null, "Yes");
				writeField(table, "LTD", null, "Yes");
				writeField(table, "Basic Life AD&D Employee", null, "Yes");
				addTable(table);
				table = makeTable(tableSizes);

			}
			pcr = hsu.createCriteria(PersonChangeRequest.class).eq(PersonChangeRequest.PERSON, bp.getPerson()).eq(PersonChangeRequest.REQUEST_TYPE, PersonChangeRequest.TYPE_VSP).orderByDesc(PersonChangeRequest.REQUEST_DATE).first();
			if (pcr != null) {
				table = makeTable(new int[]{100});
				seperatorLine(table);
				addTable(table);
				table = makeTable(tableSizes);

				addTable(table);
				writeLine("VSP Vision (Submitted " + DateUtils.getDateTimeFormatted(pcr.getRequestDate()) + ")");
				table = makeTable(tableSizes);
				parseVSP(table, bp, pcr);
				addTable(table);
				table = makeTable(tableSizes);
			}



			addTable(table);


		} finally {
			close();

		}
		if (showSSN) {
			return getFilename();
		} else {
			return absolutePath;
		}
	}

	public static void main(String args[]) {
		try {
			new PersonChangeRequestReport().build(null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getDate(String dobStr) throws Exception {
		if (dobStr.equals("NaN")) {
			return 0;
		}
		try {
			return DateUtils.getDate(sdf.parse(dobStr.replaceAll("GMT-0400", "").replaceAll("GMT-0500", "").replaceAll("GMT-0600", "").replaceAll("GMT-0700", "").replaceAll("GMT-0800", "")));
		} catch (Exception e) {
			return 0;
		}
	}

	private HashSet<String> didDOBPersons=new HashSet<String>();
	private PdfPTable parseCigna(PdfPTable table, BPerson bp, PersonChangeRequest pcr) throws Exception {
		Document doc = DOMUtils.createDocument(pcr.getRequestData());
		Node top = DOMUtils.getNode(doc, "CIGNADATA");

		if (bp.getLastName().equals("Mann"))
			System.out.println("here");
		writeField(table, "Last Name", bp.getLastName(), DOMUtils.getString(top, "employeeLastName"));
		writeField(table, "First Name", bp.getFirstName(), DOMUtils.getString(top, "employeeFirstName"));
		//writeField(table, "Middle Name", bp.getMiddleName(), DOMUtils.getString(top, "employeeMiddleName"));
		writeField(table, "SSN", bp.getSsn(), DOMUtils.getString(top, "employeeSSN"));
		if (!didDOBPersons.contains(bp.getPersonId()))
		{
			writeField(table, "DOB", DateUtils.getDateFormatted(bp.getDob()), DateUtils.getDateFormatted(DOMUtils.getInt(top, "employeeDOB")));
			didDOBPersons.add(bp.getPersonId());
		}
		//writeField(table, "Gender", null, DOMUtils.getString(top, "employeeSex"));
		if (!isEmpty(DOMUtils.getString(top, "employeeAddress")) && !didAddress) {
			writeField(table, "Street", null, DOMUtils.getString(top, "employeeAddress"));
			writeField(table, "City", null, DOMUtils.getString(top, "employeeCity"));
			writeField(table, "State", null, DOMUtils.getString(top, "employeeState"));
			writeField(table, "Zip", null, DOMUtils.getString(top, "employeeZip"));
			didAddress = true;
		}

	//	writeField(table, "Email", null, DOMUtils.getString(top, "employeeEmail"));
	//	writeField(table, "Home Phone", null, DOMUtils.getString(top, "employeePhone"));
	//	writeField(table, "Work Phone", null, DOMUtils.getString(top, "employeeWorkPhone"));

		writeField(table, "Employee Coverage Effective Date", null, DateUtils.getDateFormatted(20100101));


		writeField(table, "Point of Service", null, DOMUtils.getString(top, "pointOfService"));
		writeField(table, "HMO", null, DOMUtils.getString(top, "HMO"));
		writeField(table, "Network", null, DOMUtils.getString(top, "network"));
		writeField(table, "Point of Service Open", null, DOMUtils.getString(top, "pointOfServiceOpen"));
		writeField(table, "HMO Open", null, DOMUtils.getString(top, "HMOOpen"));
		writeField(table, "Network Open", null, DOMUtils.getString(top, "networkOpen"));
		writeField(table, "Open Access Plus", null, DOMUtils.getString(top, "openAccessPlus"));
		writeField(table, "Open Access Plus In Network", null, DOMUtils.getString(top, "openAccessPlusInNetwork"));
		writeField(table, "PPO", null, DOMUtils.getString(top, "PPO"));
		writeField(table, "Network PPO EPO", null, DOMUtils.getString(top, "networkPPOEPO"));
		writeField(table, "PPA", null, DOMUtils.getString(top, "PPA"));
		writeField(table, "Medical Indemnity", null, DOMUtils.getString(top, "medicalIndemnity"));
		writeField(table, "Write In", null, DOMUtils.getString(top, "writeIn"));
		writeField(table, "Write In Text", null, DOMUtils.getString(top, "writeInText"));
		writeField(table, "HRA", null, DOMUtils.getString(top, "HRA"));
		writeField(table, "HSA", null, DOMUtils.getString(top, "HSA"));
		writeField(table, "Pharmacy HRA", null, DOMUtils.getString(top, "pharmacyHRA"));
		writeField(table, "Dental HRA", null, DOMUtils.getString(top, "dentalHRA"));
		writeField(table, "With Open Access Plus", null, DOMUtils.getString(top, "withOpenAccessPlus"));
		writeField(table, "With Open Access Plus Network", null, DOMUtils.getString(top, "withOpenAccessPlusNetwork"));
		writeField(table, "With EPO", null, DOMUtils.getString(top, "withEPO"));
		writeField(table, "With Indemnity", null, DOMUtils.getString(top, "withIndemnity"));
		writeField(table, "CIGNA Care Network", null, DOMUtils.getString(top, "CIGNACareNetwork"));
		writeField(table, "Applicable Option", null, DOMUtils.getString(top, "applicableOption"));
		writeField(table, "Flexible Spending Health Care", null, DOMUtils.getString(top, "flexibleSpendingHealthCare"));
		writeField(table, "Flexible Spending Dependent Day Care", null, DOMUtils.getString(top, "flexibleSpendingDependentDayCare"));
		writeField(table, "Flexible Spending Decline", null, DOMUtils.getString(top, "flexibleSpendingDeclineCoverage"));
		writeField(table, "CIGNA Dental Care", null, DOMUtils.getString(top, "CIGNADentalCare"));
		writeField(table, "Dental PPO", null, DOMUtils.getString(top, "dentalPPO"));
		writeField(table, "Dental EPO", null, DOMUtils.getString(top, "dentalEPO"));
		writeField(table, "Dental Indemnity", null, DOMUtils.getString(top, "dentalIndemnity"));
		writeField(table, "Dental Coverage", null, DOMUtils.getString(top, "dentalCoverage"));
		writeField(table, "Other Insurance", null, DOMUtils.getString(top, "otherInsurance"));
		writeField(table, "Life", null, DOMUtils.getString(top, "life"));
		writeField(table, "Life Value", null, DOMUtils.getString(top, "lifeValue"));
		writeField(table, "Additional Life", null, DOMUtils.getString(top, "additionalLife"));
		writeField(table, "Additional Life Value", null, DOMUtils.getString(top, "additionalLifeValue"));
		writeField(table, "Dependent Life Spouse", null, DOMUtils.getString(top, "dependentLifeSpouse"));
		writeField(table, "Dependent Life Spouse Value", null, DOMUtils.getString(top, "dependentLifeSpouseValue"));
		writeField(table, "Dependent Life Child", null, DOMUtils.getString(top, "dependendLifeChild"));
		writeField(table, "Dependent Life Child Value", null, DOMUtils.getString(top, "dependendLifeChildValue"));
		writeField(table, "AD&D", null, DOMUtils.getString(top, "adnd"));
		writeField(table, "AD&D Value", null, DOMUtils.getString(top, "adndValue"));
		writeField(table, "Additional AD&D", null, DOMUtils.getString(top, "additionalAdnd"));
		writeField(table, "Additional AD&D Value", null, DOMUtils.getString(top, "additionalAdndValue"));
		writeField(table, "STD", null, DOMUtils.getString(top, "std"));
		writeField(table, "STD Value", null, DOMUtils.getString(top, "stdValue"));
		writeField(table, "LTD", null, DOMUtils.getString(top, "ltd"));
		writeField(table, "LTD Value", null, DOMUtils.getString(top, "ltdValue"));
		writeField(table, "Decline Life", null, DOMUtils.getString(top, "declineLife"));
		writeField(table, "Decline AD&D", null, DOMUtils.getString(top, "declineAdnd"));
		writeField(table, "Decline STD", null, DOMUtils.getString(top, "declineStd"));
		writeField(table, "Decline LTD", null, DOMUtils.getString(top, "declineLtd"));
		writeField(table, "Open Access Core", null, DOMUtils.getString(top, "openAccessCore"));
		writeField(table, "Open Access Premium", null, DOMUtils.getString(top, "openAccessPremium"));
		writeField(table, "Decline Coverage", null, DOMUtils.getString(top, "declineCoverage"));
		writeField(table, "Decline Coverage", null, DOMUtils.getString(top, "declineCoverage"));
		NodeList nodes = DOMUtils.getNodes(top, "dependents/dependent");

		HashSet<String> depids = new HashSet<String>();

		for (int loop = 0; loop < nodes.getLength(); loop++) {
			Node n = nodes.item(loop);

			String dobStr = DOMUtils.getString(n, "personDOB");
			String fname = DOMUtils.getString(n, "personFirstName");
			String lname = DOMUtils.getString(n, "personLastName");

			if (fname.equals("") && lname.equals("")) {
				continue;
			}

			if (lname.equals("Andrews"))
				continue;

			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent " + lname + ", " + fname);
			table = makeTable(tableSizes);


			int dob = getDate(dobStr);

			Person dep = hsu.createCriteria(Person.class).eq(Person.FNAME, fname).eq(Person.DOB, dob).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).first();

			if (dep == null) {
				writeField(table, "First Name", null, fname);
				writeField(table, "Middle Name", null, DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", null, lname);

				writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));

				writeField(table, "Gender", null, DOMUtils.getString(n, "personGender"));
				writeField(table, "SSN", null, DOMUtils.getString(n, "personSSN"));

			} else {
				depids.add(dep.getPersonId());
				writeField(table, "First Name", dep.getFname(), fname);
				writeField(table, "Middle Name", dep.getMname(), DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", dep.getLname(), lname);
				if (!didDOBPersons.contains(dep.getPersonId()))
				{
					writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));
					didDOBPersons.add(dep.getPersonId());
				}
				writeField(table, "Gender", dep.getSex() + "", DOMUtils.getString(n, "personGender"));
				writeField(table, "SSN", dep.getUnencryptedSsn(), DOMUtils.getString(n, "personSSN"));

			}
		}

		for (Person dep : hsu.createCriteria(Person.class).notIn(Person.PERSONID, depids).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).list()) {
			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent Not Submitted " + dep.getLname() + ", " + dep.getFname());
			table = makeTable(tableSizes);

			writeField(table, "First Name", dep.getFname(), null);
			writeField(table, "Middle Name", dep.getMname(), null);
			writeField(table, "Last Name", dep.getLname(), null);
			if (!didDOBPersons.contains(dep.getPersonId()))
			{
				writeField(table, "DOB", null, DateUtils.getDateFormatted(dep.getDob()));
				didDOBPersons.add(dep.getPersonId());
			}
			writeField(table, "Gender", dep.getSex() + "", null);
			writeField(table, "SSN", dep.getUnencryptedSsn(), null);

		}




		///////////////// OTHER COVERAGE //////////////////////////
		nodes = DOMUtils.getNodes(top, "otherCoverage/other");

		depids = new HashSet<String>();

		for (int loop = 0; loop < nodes.getLength(); loop++) {
			Node n = nodes.item(loop);

			String fname = DOMUtils.getString(n, "personFirstName");
			String lname = DOMUtils.getString(n, "personLastName");


			if (fname.equals("") && lname.equals("")) {
				continue;
			}

			if (lname.equals("Andrews"))
				continue;

			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Other Coverage " + lname + ", " + fname);
			table = makeTable(tableSizes);

			Person dep = hsu.createCriteria(Person.class).eq(Person.FNAME, fname).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).first();

			if (dep == null) {
				writeField(table, "First Name", null, fname);
				writeField(table, "Middle Name", null, DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", null, lname);
				writeField(table, "SSN", null, DOMUtils.getString(n, "personSSN"));
				writeField(table, "Effective Date", null, DateUtils.getDateFormatted((int) DOMUtils.getFloat(n, "personEffDate")));
				writeField(table, "Medicare A", null, DOMUtils.getString(n, "personMedicareA"));
				writeField(table, "Medicare B", null, DOMUtils.getString(n, "personMedicareB"));
				writeField(table, "Medicaid", null, DOMUtils.getString(n, "personMedicaid"));
				writeField(table, "Other", null, DOMUtils.getString(n, "personOther"));
			} else {
				depids.add(dep.getPersonId());
				writeField(table, "First Name", dep.getFname(), fname);
				writeField(table, "Middle Name", dep.getMname(), DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", dep.getLname(), lname);
				writeField(table, "SSN", dep.getUnencryptedSsn(), DOMUtils.getString(n, "personSSN"));
				writeField(table, "Effective Date", null, DateUtils.getDateFormatted((int) DOMUtils.getFloat(n, "personEffDate")));
				writeField(table, "Medicare A", null, DOMUtils.getString(n, "personMedicareA"));
				writeField(table, "Medicare B", null, DOMUtils.getString(n, "personMedicareB"));
				writeField(table, "Medicaid", null, DOMUtils.getString(n, "personMedicaid"));
				writeField(table, "Other", null, DOMUtils.getString(n, "personOther"));

			}
		}

		return table;
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy");

	private void parseMetLife(PdfPTable table, BPerson bp, PersonChangeRequest pcr) throws Exception {
		//	System.out.println(pcr.getRequestData());
		String data = pcr.getRequestData();
		/*data=data.replace('<', '@');
		data.replace('>', '^');
		data=DOMUtils.escapeText(data);
		data.replace('@', "<");
		data.
		 * */
		data = data.replaceAll("&", "&amp;");
		Document doc = DOMUtils.createDocument(data);
		Node top = DOMUtils.getNode(doc, "METLIFEDATA");

		writeField(table, "Last Name", bp.getLastName(), DOMUtils.getString(top, "employeeLastName"));
		writeField(table, "First Name", bp.getFirstName(), DOMUtils.getString(top, "employeeFirstName"));
		//writeField(table, "Middle Name", bp.getMiddleName(), DOMUtils.getString(top, "employeeMiddleName"));
		writeField(table, "SSN", bp.getSsn(), DOMUtils.getString(top, "employeeSSN"));
		if (!didDOBPersons.contains(bp.getPersonId()))
		{
			writeField(table, "DOB", DateUtils.getDateFormatted(bp.getDob()), DateUtils.getDateFormatted(DOMUtils.getInt(top, "employeeDOB")));
			didDOBPersons.add(bp.getPersonId());
		}
		//writeField(table, "Gender", null, DOMUtils.getString(top, "employeeSex"));
		if (!isEmpty(DOMUtils.getString(top, "employeeAddress")) && !didAddress) {
			writeField(table, "Street", null, DOMUtils.getString(top, "employeeAddress"));
			writeField(table, "City", null, DOMUtils.getString(top, "employeeCity"));
			writeField(table, "State", null, DOMUtils.getString(top, "employeeState"));
			writeField(table, "Zip", null, DOMUtils.getString(top, "employeeZip"));
			didAddress = true;
		}

		String married = DOMUtils.getString(top, "employeeMaritalStatus");
		if (married.equals("M")) {
			married = "Married";
		}
		if (married.equals("D")) {
			married = "Divorced";
		}
		if (married.equals("W")) {
			married = "Widowed";
		}

		//writeField(table, "Marital Status", null, married);
		//writeField(table, "Email", null, DOMUtils.getString(top, "employeeEmail"));
		//writeField(table, "Home Phone", null, DOMUtils.getString(top, "employeePhone"));
		//writeField(table, "Work Phone", null, DOMUtils.getString(top, "employeeWorkPhone"));
		//writeField(table,"Employee Job Type",null,DOMUtils.getString(top,"employeeJobType").equals("F")?"Full Time":"Part Time");
		//writeField(table, "Employee Occupation", null, DOMUtils.getString(top, "employeeOccupation"));
	//	writeField(table, "Employee Coverage Effective Date", null, DateUtils.getDateFormatted(DOMUtils.getInt(top, "employeeCoverageEffectiveDate")));

		String status = DOMUtils.getString(top, "employeeWorkStatus");
		if (status.equals("A")) {
			status = "Active";
		}
		if (status.equals("N")) {
			status = "New Hire";
		}
		if (status.equals("H")) {
			status = "Rehire";
		}
		if (status.equals("L")) {
			status = "Layoff/Leave of Absence";
		}
		if (status.equals("R")) {
			status = "Retired";
		}
		if (status.equals("D")) {
			status = "Disabled";
		}


	//	writeField(table, "Employee Work Status", null, status);
		writeField(table, "Employee Weekly Hours Worked", null, DOMUtils.getString(top, "employeeWeeklyHoursWorked"));
		writeField(table, "Employee Salary Type", null, DOMUtils.getString(top, "employeeSalaryType"));
		writeField(table, "Employee Salary", null, DOMUtils.getString(top, "employeeSalary"));
		writeField(table, "Employee COBRA Effective Date", null, DOMUtils.getString(top, "employeeCOBRAEffDate"));
		writeField(table, "Enrollment Reason", null, DOMUtils.getString(top, "enrollmentReason"));
		writeField(table, "Enrollment Reason Date", null, DOMUtils.getString(top, "enrollmentReasonDate"));
		writeField(table, "Coverage Dental", null, DOMUtils.getString(top, "coverageDental"));
		writeField(table, "Coverage Dental Dual", null, DOMUtils.getString(top, "coverageDentalDual"));
		String dentalOption = DOMUtils.getString(top, "coverageDentalDualOption");
		if (dentalOption.equals("C")) {
			dentalOption = "Core";
		}
		if (dentalOption.equals("P")) {
			dentalOption = "Enhanced";
		}
		writeField(table, "Coverage Dental Dual Option", null, dentalOption);
		writeField(table, "Coverage Spouse", null, DOMUtils.getString(top, "coverageSpouse"));
		writeField(table, "Coverage Child", null, DOMUtils.getString(top, "coverageChild"));
		writeField(table, "Coverage Decline", null, DOMUtils.getString(top, "coverageDecline"));
		writeField(table, "Coverage Decline Reason", null, DOMUtils.getString(top, "coverageDeclineReason"));
		writeField(table, "Voluntary Dental", null, DOMUtils.getString(top, "voluntaryDental"));

		NodeList nodes = DOMUtils.getNodes(top, "dependents/dependent");

		HashSet<String> depids = new HashSet<String>();

		for (int loop = 0; loop < nodes.getLength(); loop++) {
			Node n = nodes.item(loop);

			String dobStr = DOMUtils.getString(n, "personDOB");
			String fname = DOMUtils.getString(n, "personFirstName");
			String lname = DOMUtils.getString(n, "personLastName");

			if (lname.equals("Andrews"))
				continue;

			if (fname.equals("") && lname.equals("")) {
				continue;
			}

			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent " + lname + ", " + fname);
			table = makeTable(tableSizes);


			int dob = getDate(dobStr);

			Person dep = hsu.createCriteria(Person.class).eq(Person.FNAME, fname).eq(Person.DOB, dob).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).first();

			if (dep == null) {
				writeField(table, "First Name", null, fname);
				writeField(table, "Middle Name", null, DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", null, lname);

				writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));

				writeField(table, "Gender", null, DOMUtils.getString(n, "personGender"));
				writeField(table, "Student", null, DOMUtils.getString(n, "personStudent"));

			} else {
				depids.add(dep.getPersonId());
				writeField(table, "First Name", dep.getFname(), fname);
				writeField(table, "Middle Name", dep.getMname(), DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", dep.getLname(), lname);
				if (!didDOBPersons.contains(dep.getPersonId()))
				{
					writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));
					didDOBPersons.add(dep.getPersonId());
				}
				writeField(table, "Gender", dep.getSex() + "", DOMUtils.getString(n, "personGender"));
				writeField(table, "Student", dep.getStudent() + "", DOMUtils.getString(n, "personStudent"));

			}
		}

		for (Person dep : hsu.createCriteria(Person.class).notIn(Person.PERSONID, depids).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).list()) {
			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent Not Submitted " + dep.getLname() + ", " + dep.getFname());
			table = makeTable(tableSizes);

			writeField(table, "First Name", dep.getFname(), null);
			writeField(table, "Middle Name", dep.getMname(), null);
			writeField(table, "Last Name", dep.getLname(), null);
			if (!didDOBPersons.contains(dep.getPersonId()))
				{
					writeField(table, "DOB", null, DateUtils.getDateFormatted(dep.getDob()));
					didDOBPersons.add(dep.getPersonId());
				}
			writeField(table, "Gender", dep.getSex() + "", null);
			writeField(table, "Student", dep.getStudent() + "", null);

		}




	}

	private PdfPTable parseMutualOfOmaha(PdfPTable table, BPerson bp, PersonChangeRequest pcr) throws Exception {

		Document doc = DOMUtils.createDocument(pcr.getRequestData());
		Node top = DOMUtils.getNode(doc, "MUTUALDATA");

		writeField(table, "Last Name", bp.getLastName(), DOMUtils.getString(top, "employeeLastName"));
		writeField(table, "First Name", bp.getFirstName(), DOMUtils.getString(top, "employeeFirstName"));
		//writeField(table, "Middle Name", bp.getMiddleName(), DOMUtils.getString(top, "employeeMiddleName"));
		writeField(table, "SSN", bp.getSsn(), DOMUtils.getString(top, "employeeSSN"));
		if (!didDOBPersons.contains(bp.getPersonId()))
		{
			writeField(table, "DOB", DateUtils.getDateFormatted(bp.getDob()), DateUtils.getDateFormatted(DOMUtils.getInt(top, "employeeDOB")));
			didDOBPersons.add(bp.getPersonId());
		}
	//	writeField(table, "Gender", null, DOMUtils.getString(top, "employeeSex"));

		if (!isEmpty(DOMUtils.getString(top, "employeeAddress")) && !didAddress) {
			writeField(table, "Street", null, DOMUtils.getString(top, "employeeAddress"));
			writeField(table, "City", null, DOMUtils.getString(top, "employeeCity"));
			writeField(table, "State", null, DOMUtils.getString(top, "employeeState"));
			writeField(table, "Zip", null, DOMUtils.getString(top, "employeeZip"));
			didAddress = true;
		}


		writeField(table, "Salary", null, DOMUtils.getString(top, "employeeSalary"));
		writeField(table, "Life AD&D Employee", null, DOMUtils.getString(top, "lifeAdndEmployee"));
		writeField(table, "STD", null, DOMUtils.getString(top, "std"));
		writeField(table, "LTD", null, DOMUtils.getString(top, "ltd"));
		writeField(table, "Basic Life AD&D Employee", null, DOMUtils.getString(top, "basicLifeAdndEmployee"));
		writeField(table, "Life AD&D Employee Benefit", null, DOMUtils.getString(top, "lifeAdndEmployeeBen"));
		//	writeField(table, "Life AD&D Employee Premium", null, DOMUtils.getString(top, "lifeAdndEmployeePrem"));

		writeField(table, "Life AD&D Spouse", null, DOMUtils.getString(top, "lifeAdndSpouse"));
		writeField(table, "Life AD&D Spouse Benefit", null, DOMUtils.getString(top, "lifeAdndSpouseBen"));
		//	writeField(table, "Life AD&D Spouse Premium", null, DOMUtils.getString(top, "lifeAdndSpousePrem"));
		writeField(table, "Life AD&D Child", null, DOMUtils.getString(top, "lifeAdndChild"));
		writeField(table, "Life AD&D Child Benefit", null, DOMUtils.getString(top, "lifeAdndChildBen"));
		//	writeField(table, "Life AD&D Child Premium", null, DOMUtils.getString(top, "lifeAdndChildPrem"));


		NodeList nodes = DOMUtils.getNodes(top, "dependents/dependent");

		HashSet<String> depids = new HashSet<String>();

		for (int loop = 0; loop < nodes.getLength(); loop++) {
			Node n = nodes.item(loop);

			String dobStr = DOMUtils.getString(n, "personDOB");
			String fname = DOMUtils.getString(n, "personFirstName");
			String lname = DOMUtils.getString(n, "personLastName");


			if (fname.equals("") && lname.equals("")) {
				continue;
			}

			if (lname.equals("Andrews"))
				continue;

			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent " + lname + ", " + fname);
			table = makeTable(tableSizes);


			int dob = getDate(dobStr);

			Person dep = hsu.createCriteria(Person.class).eq(Person.FNAME, fname).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).first();

			if (dep == null) {
				writeField(table, "First Name", null, fname);
				writeField(table, "Middle Name", null, DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", null, lname);
				writeField(table, "Relationship", null, DOMUtils.getString(n, "personRelationship"));

				writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));

				writeField(table, "Percentage", null, DOMUtils.getString(n, "personBenPerc"));

			} else {
				depids.add(dep.getPersonId());
				writeField(table, "First Name", dep.getFname(), fname);
				writeField(table, "Middle Name", dep.getMname(), DOMUtils.getString(n, "personMiddleName"));
				writeField(table, "Last Name", dep.getLname(), lname);
				if (!didDOBPersons.contains(dep.getPersonId()))
				{
					writeField(table, "DOB", null, DateUtils.getDateFormatted(dob));
					didDOBPersons.add(dep.getPersonId());
				}
				writeField(table, "Relationship", null, DOMUtils.getString(n, "personRelationship"));
				writeField(table, "Percentage", null, DOMUtils.getString(n, "personBenPerc"));

			}
		}

		for (Person dep : hsu.createCriteria(Person.class).notIn(Person.PERSONID, depids).joinTo(Person.DEP_JOINS_AS_DEPENDENT).eq(HrEmplDependent.EMPLOYEE, bp.getPerson()).list()) {
			addTable(table);
			table = makeTable(new int[]{100});
			writeLine("Dependent " + dep.getLname() + ", " + dep.getFname());
			table = makeTable(tableSizes);

			writeField(table, "First Name", dep.getFname(), null);
			writeField(table, "Middle Name", dep.getMname(), null);
			writeField(table, "Last Name", dep.getLname(), null);
			if (!didDOBPersons.contains(dep.getPersonId()))
				{
					writeField(table, "DOB", null, DateUtils.getDateFormatted(dep.getDob()));
					didDOBPersons.add(dep.getPersonId());
				}
			writeField(table, "Relationship", null, null);
			writeField(table, "Percentage", null, null);
		}



		return table;

	}

	private PdfPTable parseVSP(PdfPTable table, BPerson bp, PersonChangeRequest pcr) throws Exception {
		Document doc = DOMUtils.createDocument(pcr.getRequestData());
		Node top = DOMUtils.getNode(doc, "VSPDATA");

		writeField(table, "Last Name", bp.getLastName(), DOMUtils.getString(top, "employeeLastName"));
		writeField(table, "First Name", bp.getFirstName(), DOMUtils.getString(top, "employeeFirstName"));
	//	writeField(table, "Middle Name", bp.getMiddleName(), DOMUtils.getString(top, "employeeMiddleName"));
		writeField(table, "SSN", bp.getSsn(), DOMUtils.getString(top, "employeeSSN"));
		if (!didDOBPersons.contains(bp.getPersonId()))
		{
			writeField(table, "DOB", DateUtils.getDateFormatted(bp.getDob()), DateUtils.getDateFormatted(DOMUtils.getInt(top, "employeeDOB")));
			didDOBPersons.add(bp.getPersonId());
		}
		String option = DOMUtils.getString(top, "coverageOption");
		if (option.equals("C")) {
			option = "Employee + Children";
		}
		if (option.equals("E")) {
			option = "Employee";
		}
		if (option.equals("W")) {
			option = "Waive Coverage";
		}
		if (option.equals("D")) {
			option = "Employee + 1 Dependent";
		}
		if (option.equals("F")) {
			option = "Employee + Family";
		}
		writeField(table, "Coverage Option", bp.getSsn(), option);
		return table;

	}

	private void writeField(PdfPTable table, String field, String oldValue, String newValue) {

		if (newValue == null && oldValue == null) {
			return;
		}

		if (newValue != null && newValue.equals(oldValue)) {
			return;
		}

		if (newValue != null && newValue.equals("FALSE")) {
			return;
		}

		if (newValue != null && newValue.equals("NaN")) {
			return;
		}

		if (newValue != null && newValue.equals("")) {
			return;
		}

		if (newValue != null && newValue.equals("0")) {
			return;
		}

		if (newValue != null && newValue.equals("0.0")) {
			return;
		}

		if (!showSSN && field.equals("SSN")) {
			return;
		}

		if ("TRUE".equals(newValue)) {
			newValue = "Yes";
		}

		boolean showOldNew = oldValue != null && newValue != null;

//		if (oldValue != null) {
//			write(table, (showOldNew ? "Old " : "") + field, false);
//			write(table, oldValue, false);
//		}
		/*
		else
		{write(table,field,false);
		write(table,"",false);
		write(table,"",false);
		}
		 */
		if (newValue != null) {
			write(table, field, false);
			write(table, newValue, false);
		}
	/*
	else
	{
	write(table,field,false);
	write(table,"",false);
	write(table,"",false);
	}
	 * */

	}
}
