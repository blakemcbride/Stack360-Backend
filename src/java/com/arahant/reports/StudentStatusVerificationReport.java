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


/**
 * Created on Sep 26, 2008
 * 
 * @author Frank Costanza
 */
package com.arahant.reports;

import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.beans.StudentVerification;
import com.arahant.business.BPerson;
import com.arahant.business.BStudentVerification;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;


public class StudentStatusVerificationReport extends ReportBase {
	
	public StudentStatusVerificationReport() throws ArahantException {
		super("StuStaVerRep", "Student Status Verifications");
	}
	
	public String build(String termType, String term, int year, boolean received, boolean excludeHandicap) throws Exception {
        try {
            PdfPTable table = makeTable(new int[] {32, 15, 4, 32, 17});
            HibernateScrollUtil<Person> dependentScrollUtil = this.getDependents(termType, term, (short)year, received, excludeHandicap);
            boolean alternateRow = false;
            String termName;

            if (term.equals("F")) {
                termName = "Fall";
            } else if (term.equals("I")) {
                termName = "Winter";
            } else if (term.equals("S")) {
                termName = "Spring";
            } else {
                termName = "Summer";
            }

            blankLine();

            writeHeaderLine("Verification Status", received ? "Received" : "Missing");
            writeHeaderLine("Verification Term", termName + " " + year);

            addHeaderLine();

            writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Employee SSN", Element.ALIGN_RIGHT);
            writeColHeader(table, " ");
            writeColHeader(table, "Dependent Name", Element.ALIGN_LEFT);
            writeColHeader(table, "Dependent SSN", Element.ALIGN_RIGHT);

			HashSet<String> personIds=new HashSet<String>();

            while (dependentScrollUtil.next()) {
                BPerson person = new BPerson(dependentScrollUtil.get());

				if (personIds.contains(person.getPersonId()))
					continue;

				personIds.add(person.getPersonId());

				Employee emp=hsu.createCriteria(Employee.class)
					.orderBy(Employee.LNAME)
					.orderBy(Employee.FNAME)
					.joinTo(Employee.HREMPLDEPENDENTS)
					.geOrEq(HrEmplDependent.DATE_INACTIVE,DateUtils.now(),0)
					.eq(HrEmplDependent.PERSON, person.getPerson())
					.first();


                String sponsoringEmployeeName = emp.getNameLFM();
                String sponsoringEmployeeSsn = emp.getUnencryptedSsn();
                String dependentName = person.getNameLFM();
                String dependentSsn = person.getSsn();

                write(table, sponsoringEmployeeName, alternateRow);
                writeRight(table, sponsoringEmployeeSsn, alternateRow);
                write(table, " ", alternateRow);
                write(table, dependentName, alternateRow);
                writeRight(table, dependentSsn, alternateRow);

                alternateRow = !alternateRow;

                
            }

			addTable(table);

            dependentScrollUtil.close();
        } finally {
            close();
        }
        
        return getFilename();
	}

    private HibernateScrollUtil<Person> getDependents(String termType, String term, short year, boolean received, boolean excludeHandicap) {
        // david - get all dependents who are still active students and are on the match term type
        //                and are either missing or have recieved verificiations for the specified term,
        //                dependening on the received flag .... order by sponsoring employee name


		if (isEmpty(termType))
			termType=" ";

		Calendar nineteenYearsAgo=DateUtils.getNow();
		nineteenYearsAgo.set(Calendar.YEAR,nineteenYearsAgo.get(Calendar.YEAR)-19);

		int dobCheck=DateUtils.getDate(nineteenYearsAgo);

        HibernateCriteriaUtil<Person> criteriaUtil = hsu.createCriteria(Person.class);
			

		if (received)
		{
			criteriaUtil.joinTo(Person.STUDENT_VERIFICATIONS)
				.eq(StudentVerification.YEAR, year)
				.eq(StudentVerification.TERM, BStudentVerification.getTerm(term));
		}
		else
		{
			List<String> notIns=(List)hsu.createCriteria(StudentVerification.class)
				.selectFields(StudentVerification.PERSON_ID)
				.eq(StudentVerification.YEAR, year)
				.eq(StudentVerification.TERM, BStudentVerification.getTerm(term))
				.list();

			criteriaUtil.notIn(Person.PERSONID, notIns)
				.eq(Person.STUDENT_CALENDAR_TYPE, termType.charAt(0))
				.le(Person.DOB, dobCheck)
				.eq(Person.STUDENT,'Y');

			if (excludeHandicap)
				criteriaUtil.eq(Person.HANDICAP, 'N');
		}

		criteriaUtil.joinTo(Person.HR_BENEFIT_JOINS_WHERE_COVERED)
			.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now());

        criteriaUtil.joinTo(Person.DEP_JOINS_AS_DEPENDENT)
			.geOrEq(HrEmplDependent.DATE_INACTIVE,DateUtils.now(),0)
			.joinTo(HrEmplDependent.EMPLOYEE)
			.orderBy(Employee.LNAME)
			.orderBy(Employee.FNAME);

        return criteriaUtil.scroll();
    }
	
	public static void main(String[] args) {
		try	{
			ArahantSession.getHSU().setCurrentPerson(ArahantSession.getHSU().get(Person.class, "00001-0000000004"));
			new StudentStatusVerificationReport().build("Q", "F", 2008, false,true);
		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}
}

	
