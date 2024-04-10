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

package com.arahant.exports;

import com.arahant.beans.Address;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrEmplDependent;
import com.arahant.beans.Person;
import com.arahant.beans.SpousalVerification;
import com.arahant.business.BEmployee;
import com.arahant.business.BPerson;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class SpousalMailingExport {
	public String build(short year, String reportType) throws Exception{
		return build(year, reportType, "");
	}
	
	public String build(short year, String reportType, String noticeType) throws Exception {
		File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "Spousal Verification Mailing List " + DateUtils.now() + " " + reportType + ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			HibernateSessionUtil hsu = ArahantSession.getHSU();
			hsu.dontAIIntegrate();

			//Writing column headers
            dfw.writeField("Employee Last Name");
            dfw.writeField("Employee First Name");
            dfw.writeField("Spouse Last Name");
            dfw.writeField("Spouse First Name");
            dfw.writeField("Street");
			dfw.writeField("Street 2");
            dfw.writeField("City");
            dfw.writeField("State");
            dfw.writeField("Zip Code");
            dfw.writeField("Country");
            dfw.endRecord();

			List<String> spouseVerifs = (List)hsu.createCriteria(SpousalVerification.class)
												 .selectFields(SpousalVerification.PERSON_ID)
												 .eq(SpousalVerification.YEAR, year)
												 .list();

			//Criteria for all Employees, sorted by last name, then first
			HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class)
																		 .orderBy(Person.LNAME)
																		 .orderBy(Person.FNAME);
			
			//Criteria to filter for employees who have a spouse
			HibernateCriteriaUtil perHcu = hcu.joinTo(Employee.HREMPLDEPENDENTS)
									     	  .eq(HrEmplDependent.RELATIONSHIP_TYPE, HrEmplDependent.TYPE_SPOUSE)
											  .geOrEq(HrEmplDependent.DATE_INACTIVE, DateUtils.now(), 0)
										      .joinTo(HrEmplDependent.PERSON);

			//Checks which report type to perform
			if(reportType.equals("M") || (reportType.equals("N") && noticeType.equals("R")))
				perHcu.notIn(Person.PERSONID, spouseVerifs);
			else if (reportType.equals("R"))
				perHcu.in(Person.PERSONID, spouseVerifs);

			//Criteria to further filter for employees who have currently active benefit coverage
			perHcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_COVERED)
				  .dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now())
			      .ge(HrBenefitJoin.COVERAGE_START_DATE,20070102)
			      .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			      .joinTo(HrBenefitConfig.HR_BENEFIT)
			      .in(HrBenefit.BENEFITID, new String[]{"00001-0000000023","00001-0000000019"});

			HibernateScrollUtil<Employee> scr = hcu.scroll();

			while(scr.next()) {
				BEmployee be = new BEmployee(scr.get());
				String lastID = "";
				BPerson spouse = be.getCurrentSpouse();
				Address currentAddr = new Address();
				Set<Address> addr = new HashSet<Address>();
				Iterator<Address> addrIt;

				//Checks that the spouse is covered under a health category benefit
				if(spouse != null) {
					HrBenefitJoin hrbj = hsu.createCriteria(HrBenefitJoin.class)
					.eq(HrBenefitJoin.PAYING_PERSON, be.getEmployee())
					.eq(HrBenefitJoin.COVERED_PERSON, spouse.getPerson())
					.dateInside(HrBenefitJoin.COVERAGE_START_DATE, HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now())
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.joinTo(HrBenefitConfig.HR_BENEFIT)
					.joinTo(HrBenefit.BENEFIT_CATEGORY)
					.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
					.first();

					if(hrbj == null)
						continue;
				}


				if(reportType.equals("N")) {
					if(!hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, be.getEmployee())
															   .ne(HrBenefitJoin.COVERED_PERSON, be.getEmployee())
															   .exists())
						continue;
					if(be.getPersonId().equals(lastID))
						continue;
					else
						lastID = be.getPersonId();
				}
				else {
					if(spouse == null || spouse.isEmployee())
						continue;
				}

				//Write out employee's last name
				dfw.writeField(be.getLastName());

				//Write out employee's first name
				dfw.writeField(be.getFirstName());

				if(spouse != null) {
					//Write out spouse's last name
					dfw.writeField(spouse.getLastName());

					//Write out spouse's first name
					dfw.writeField(spouse.getFirstName());
				}
				else {
					dfw.writeField("");
					dfw.writeField("");
				}

				//Get all addresses associated with this person
				addr = be.getAddresses();
				if(addr != null)
					addrIt = addr.iterator();
				else
					addrIt = null;
				while(addrIt.hasNext()) {
					Address a = addrIt.next();

					//Get the first valid home address
					if(a.getAddressType() == 2 && a.getRecordType() == 'R') {
						currentAddr = a;
						break;
					}
				}

				//Write out street
				dfw.writeField(currentAddr.getStreet());

				//Write out second street line if applicable
				if(!currentAddr.getStreet2().equals(null))
					dfw.writeField(currentAddr.getStreet2());

				//Write out city
				dfw.writeField(currentAddr.getCity());

				//Write out state
				dfw.writeField(currentAddr.getState());

				//Write out zip code
				dfw.writeField(currentAddr.getZip());

				//Write out country
				dfw.writeField(currentAddr.getCountry());

				//End line
				dfw.endRecord();
			}
		}
		finally {
			dfw.close();
		}

		return csvFile.getName();
	}

	public static void main(String args[]) {
        try {
            ArahantSession.getHSU().dontAIIntegrate();
            new SpousalMailingExport().build((short)2011, "M");//N", "R");
        }
		catch (Exception e) {
            e.printStackTrace();
        }
    }
}
