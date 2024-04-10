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
 *
 *
 */
package com.arahant.exports;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;
import com.itextpdf.text.DocumentException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.kissweb.DelimitedFileWriter;
import org.kissweb.StringUtils;

public class EmployeeEnrollmentExport {

	private final HibernateSessionUtil hsu = ArahantSession.getHSU();

	public String build(final String benefitCategoryId, final String benefitId, final String statusId, final String orgGroupId, final int enrolledAsOf) throws FileNotFoundException, DocumentException, ArahantException, Exception {

		File csvFile = FileSystemUtils.createTempFile("EmployeeEnrollment" + ((Double) (Math.random() * 10000)).toString().replace(".", ""), ".csv");
		DelimitedFileWriter writer = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		try {
			List<OrgGroup> ogList;
			writer.writeField("Employee SSN");
			writer.writeField("Employee First Name");
			writer.writeField("Employee Last Name");
			if (StringUtils.isEmpty(statusId))
				writer.writeField("Status");
			writer.writeField("Status Date ");
			writer.writeField("Benefit Coverage Configuration");
			writer.writeField("Org Group");
			writer.writeField("Org Group Total");
			writer.endRecord();

			if (StringUtils.isEmpty(orgGroupId))
				ogList = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, ArahantConstants.COMPANY_TYPE).list();
			else {
				ogList = new ArrayList<OrgGroup>(1);
				ogList.add(hsu.get(OrgGroup.class, orgGroupId));
			}

			List<String> empIds = new ArrayList();

			for (final OrgGroup og : ogList) {
				final String currentOrgGroup = og.getName();
				int currentOrgGroupTotal = 0;

				ScrollableResults escroll;

				String status = "";

				if (!StringUtils.isEmpty(statusId))
					if (StringUtils.isEmpty(benefitId)) {
						final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga "
								+ "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins "
								+ " where hist.hrEmployeeStatus = :stat and hist.effectiveDate = "
								+ "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n"
								+ " and oga.orgGroup = :og "
								+ " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 "
								+ " or hist.hrEmployeeStatus.dateType='S' ) "
								+ "and beneJoins.hrBenefitConfig.hrBenefit.hrBenefitCategory = :benefitCategory "
								+ "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") "
								+ "and beneJoins.benefitApproved='Y' "
								+ " order by emp.lname, emp.fname, hist.effectiveDate ");

						q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
						q.setEntity("og", og);
						q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
						status = hsu.get(HrEmployeeStatus.class, statusId).getName();
						escroll = q.scroll();
					} else {
						final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga "
								+ "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.hrEmployeeStatus = :stat and hist.effectiveDate = "
								+ "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og "
								+ " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 "
								+ " or hist.hrEmployeeStatus.dateType='S' ) "
								+ "and beneJoins.hrBenefitConfig.hrBenefit = :benefit "
								+ "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") "
								+ "and beneJoins.benefitApproved='Y' "
								+ " order by emp.lname, emp.fname, hist.effectiveDate ");

						q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
						q.setEntity("og", og);
						q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
						status = hsu.get(HrEmployeeStatus.class, statusId).getName();
						escroll = q.scroll();
					}
				else
					if (StringUtils.isEmpty(benefitId)) {
						final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga"
								+ " join hist.hrEmployeeStatus stat "
								+ "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.effectiveDate = "
								+ "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og "
								+ " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 "
								+ " or hist.hrEmployeeStatus.dateType='S' ) "
								+ "and beneJoins.hrBenefitConfig.hrBenefit.hrBenefitCategory = :benefitCategory "
								+ "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") "
								+ "and beneJoins.benefitApproved='Y' "
								+ " order by emp.lname, emp.fname, hist.effectiveDate ");
						q.setEntity("og", og);
						q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
						escroll = q.scroll();
					} else {
						final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga"
								+ " join hist.hrEmployeeStatus stat "
								+ "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.effectiveDate = "
								+ "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og "
								+ " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 "
								+ " or hist.hrEmployeeStatus.dateType='S' ) "
								+ "and beneJoins.hrBenefitConfig.hrBenefit = :benefit "
								+ "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") "
								+ "and beneJoins.benefitApproved='Y' "
								+ " order by emp.lname, emp.fname, hist.effectiveDate ");
						q.setEntity("og", og);
						q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
						escroll = q.scroll();
					}

				while (escroll.next()) {

					final String ssn = escroll.getString(0);
					final String lname = escroll.getString(1);
					final String fname = escroll.getString(2);

					writer.writeField(ssn);
					writer.writeField(fname);
					writer.writeField(lname);
					if (StringUtils.isEmpty(status)) {
						writer.writeField(escroll.getString(3));
						writer.writeField(DateUtils.getDateFormatted(escroll.getInteger(5)));
						writer.writeField(escroll.getString(4));
						empIds.add(escroll.getString(6));
					} else {
						writer.writeField(DateUtils.getDateFormatted(escroll.getInteger(4)));
						writer.writeField(escroll.getString(3));
						empIds.add(escroll.getString(5));
					}

					currentOrgGroupTotal++;
					writer.endRecord();
				}

				escroll.close();

				if (currentOrgGroupTotal > 0) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
					if (StringUtils.isEmpty(statusId))
						writer.writeField("");
					writer.writeField("");
					writer.writeField("");
					writer.writeField(currentOrgGroup);
					writer.writeField(currentOrgGroupTotal);
					writer.endRecord();
				}

			}


			//System.out.println(empIds.size());
			//Now print out the people not in org group associations
			if (StringUtils.isEmpty(orgGroupId)) //They selected all org_groups
			{
				//List of employees for everyone in company that does not have orgGroup associations
				HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME) //.eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
						.notIn(Employee.PERSONID, empIds);

				//check status for each person if status was passed
				if (!StringUtils.isEmpty(statusId))
					hcu.employeeCurrentStatus(statusId);

				HibernateScrollUtil<Employee> scr = hcu.scroll();

				int unknowns = 0;

				boolean alternateRow = true;

				while (scr.next()) {
					BEmployee be = new BEmployee(scr.get());

					HibernateCriteriaUtil<HrBenefitJoin> benefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.PAYING_PERSON, be.getEmployee()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT);

					if (!StringUtils.isEmpty(benefitId))
						benefitJoins.eq(HrBenefit.BENEFITID, benefitId);
					else
						benefitJoins.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, benefitCategoryId);

					HrBenefitJoin bj = benefitJoins.first();

					if (bj == null)
						continue;

					// toggle the alternate row
					alternateRow = !alternateRow;

					writer.writeField(be.getSsn());
					writer.writeField(be.getFirstName());
					writer.writeField(be.getLastName());
					if (StringUtils.isEmpty(statusId)) {
						writer.writeField(be.getStatus());
						writer.writeField(DateUtils.getDateFormatted(be.getEmployeeStatusDate()));
						writer.writeField(bj.getHrBenefitConfig().getName());
					} else {
						writer.writeField(DateUtils.getDateFormatted(be.getEmployeeStatusDate()));
						writer.writeField(bj.getHrBenefitConfig().getName());
					}

					unknowns++;
					writer.endRecord();
				}

				if (unknowns != 0) {
					writer.writeField("");
					writer.writeField("");
					writer.writeField("");
					if (StringUtils.isEmpty(statusId))
						writer.writeField("");
					writer.writeField("");
					writer.writeField("");
					writer.writeField("Unknowns");
					writer.writeField(unknowns);
					writer.endRecord();
				}

			}
		} finally {
			writer.close();
		}
		return FileSystemUtils.getHTTPPath(csvFile);
	}

	public static void main(String[] args) {
		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class, "00001-0000072555"));
		ArahantSession.getHSU().setCurrentPersonToArahant();
		try {
			new EmployeeEnrollmentExport().build("00001-0000095216", "00001-0000142655", "", "", 20110411);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(EmployeeEnrollmentExport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (DocumentException ex) {
			Logger.getLogger(EmployeeEnrollmentExport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ArahantException ex) {
			Logger.getLogger(EmployeeEnrollmentExport.class.getName()).log(Level.SEVERE, null, ex);
		} catch (Exception ex) {
			Logger.getLogger(EmployeeEnrollmentExport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
