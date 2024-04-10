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
package com.arahant.reports;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;

import com.arahant.beans.*;
import com.arahant.business.BEmployee;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.Set;


public class EmployeeEnrollmentReport extends ReportBase {

    public EmployeeEnrollmentReport() throws ArahantException {
        super("EmpEnrRpt", "Employee Enrollment Report");
    }
    private final HibernateSessionUtil hsu = ArahantSession.getHSU();

    public String build(final String benefitCategoryId, final String benefitId, final String statusId, final String orgGroupId, final int enrolledAsOf) throws FileNotFoundException, DocumentException, ArahantException {

        try {
            final PdfPTable table = makeTable(new int[]{1});
            writeHeader(table, "Enrolled in Benefit Category: " + hsu.get(HrBenefitCategory.class, benefitCategoryId).getDescription());
            writeHeader(table, "Enrolled in Benefit: " + (isEmpty(benefitId) ? "(any)" : hsu.get(HrBenefit.class, benefitId).getName()));
            writeHeader(table, "Employee Status: " + (isEmpty(statusId) ? "(any)" : hsu.get(HrEmployeeStatus.class, statusId).getName()));
            writeHeader(table, "Organizational Group: " + (isEmpty(orgGroupId) ? "(any)" : hsu.get(OrgGroup.class, orgGroupId).getName()));
            writeHeader(table, "Enrolled as of: " + DateUtils.getDateFormatted(enrolledAsOf));
            addTable(table);

            addHeaderLine();
            writeEmployees(benefitCategoryId, benefitId, statusId, orgGroupId, enrolledAsOf);
        } finally {
            close();

        }

        return getFilename();
    }

    protected void writeEmployees(final String benefitCategoryId, final String benefitId, final String statusId, final String orgGroupId, int enrolledAsOf) throws DocumentException, ArahantException {
        PdfPTable table;

        // THIS NEEDS TO BE BROKEN DOWN BY ORG GROUP ... multi-dimensional or something
        // WITHIN THE BREAK DOWN OF EACH ORG GROUP IT SHOULD BE ORDERED BY SAME PLAN
        // FOR CHOSEN BENEFIT CATEGORY CATEGORY ... SO IF MEDICAL BENEFIT CATEGORY CATEGORY
        // THEN GET THE CHOSEN BENEFIT CATEGORY THAT IS A MEDICAL BENEFIT CATEGORY CATEGORY

        List<OrgGroup> ogList;
		Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
		Set<OrgGroup> ogs = new HashSet<OrgGroup>();
		for(OrgGroupAssociation o : oga)
			ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
		List<String> orgIds = new ArrayList<String>();
		for(OrgGroup og : ogs)
			orgIds.add(og.getOrgGroupId());

        if (isEmpty(orgGroupId)) {
            HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, ArahantConstants.COMPANY_TYPE);
			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) 
				hcu.in(OrgGroup.ORGGROUPID, orgIds);

			ogList = hcu.list();
        } else {
            ogList = new ArrayList<OrgGroup>(1);
            ogList.add(hsu.get(OrgGroup.class, orgGroupId));
        }

        List<String> empIds = new ArrayList<String>();

        for (final OrgGroup og : ogList)
        {
            boolean alternateRow = true;
            final String currentOrgGroup = og.getName();
            int currentOrgGroupTotal = 0;

            if (isEmpty(statusId))
            {
                table = makeTable(new int[]{16, 24, 15, 15, 35});
            }
            else
            {
                table = makeTable(new int[]{16, 24, 15, 45});
            }

            writeColHeader(table, "Employee SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
            if (isEmpty(statusId)) {
                writeColHeader(table, "Status", Element.ALIGN_LEFT);
            }
            writeColHeader(table, "Status Date ", Element.ALIGN_LEFT);
            writeColHeader(table, "Benefit Coverage Configuration", Element.ALIGN_LEFT);
            table.setHeaderRows(1);

            ScrollableResults escroll;

            String status = "";

            if (!isEmpty(statusId))
            {
                if (isEmpty(benefitId))
                {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga " +
                            "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins " +
                            " where hist.hrEmployeeStatus = :stat and hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n" +
                            " and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            "and beneJoins.hrBenefitConfig.hrBenefit.hrBenefitCategory = :benefitCategory " +
                            "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") " +
                            "and beneJoins.benefitApproved='Y' " +
                            " order by emp.lname, emp.fname, hist.effectiveDate ");

                    q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
                    q.setEntity("og", og);
                    q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
                    status = hsu.get(HrEmployeeStatus.class, statusId).getName();
                    escroll = q.scroll();
                }
                else
                {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga " +
                            "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.hrEmployeeStatus = :stat and hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            "and beneJoins.hrBenefitConfig.hrBenefit = :benefit " +
                            "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") " +
                            "and beneJoins.benefitApproved='Y' " +
                            " order by emp.lname, emp.fname, hist.effectiveDate ");

                    q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
                    q.setEntity("og", og);
                    q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
                    status = hsu.get(HrEmployeeStatus.class, statusId).getName();
                    escroll = q.scroll();
                }

            }
            else
            {
                if (isEmpty(benefitId))
                {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga" +
                            " join hist.hrEmployeeStatus stat " +
                            "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            "and beneJoins.hrBenefitConfig.hrBenefit.hrBenefitCategory = :benefitCategory " +
                            "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") " +
                            "and beneJoins.benefitApproved='Y' " +
                            " order by emp.lname, emp.fname, hist.effectiveDate ");
                    q.setEntity("og", og);
                    q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
                    escroll = q.scroll();
                }
                else
                {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefitConfig.name, hist.effectiveDate, emp.personId from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga" +
                            " join hist.hrEmployeeStatus stat " +
                            "join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins where hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + enrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            "and beneJoins.hrBenefitConfig.hrBenefit = :benefit " +
                            "and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + enrolledAsOf + ") and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + enrolledAsOf + ") " +
                            "and beneJoins.benefitApproved='Y' " +
                            " order by emp.lname, emp.fname, hist.effectiveDate ");
                    q.setEntity("og", og);
                    q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
                    escroll = q.scroll();
                }
            }

            while (escroll.next()) {

                final String ssn = escroll.getString(0);
                final String lname = escroll.getString(1);
                final String fname = escroll.getString(2);

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, ssn, alternateRow);
                write(table, lname + ", " + fname, alternateRow);
                if (isEmpty(status)) {
                    write(table, escroll.getString(3), alternateRow);
                    write(table, DateUtils.getDateFormatted(escroll.getInteger(5)), alternateRow);
                    write(table, escroll.getString(4), alternateRow);
                    empIds.add(escroll.getString(6));
                } else {
                    write(table, DateUtils.getDateFormatted(escroll.getInteger(4)), alternateRow);
                    write(table, escroll.getString(3), alternateRow);
                     empIds.add(escroll.getString(5));
                }

                currentOrgGroupTotal++;
            }

            escroll.close();

            this.writeOrgGroupTotal(table, currentOrgGroup, currentOrgGroupTotal);

            addTable(table);
        }


        //System.out.println(empIds.size());
        //Now print out the people not in org group associations
        if (isEmpty(orgGroupId))  //They selected all org_groups
        {
            if (isEmpty(statusId))
            {
                table = makeTable(new int[]{16, 24, 15, 15, 35});
            }
            else
            {
                table = makeTable(new int[]{16, 24, 15, 45});
            }

            //List of employees for everyone in company that does not have orgGroup associations
            HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class).orderBy(Employee.LNAME).orderBy(Employee.FNAME)
                    //.eq(Employee.COMPANYBASE, ArahantSession.getHSU().getCurrentCompany())
                    .notIn(Employee.PERSONID, empIds);
			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
				hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORG_GROUP_ID, orgIds);

            //check status for each person if status was passed
            if (!isEmpty(statusId))
            {
                hcu.employeeCurrentStatus(statusId);
            }

            HibernateScrollUtil<Employee> scr = hcu.scroll();

            int unknowns = 0;

            boolean alternateRow = true;

            while(scr.next())
            {
                BEmployee be = new BEmployee(scr.get());

                HibernateCriteriaUtil<HrBenefitJoin> benefitJoins = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
                        .eq(HrBenefitJoin.PAYING_PERSON, be.getEmployee())
                        .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT);

                if (!isEmpty(benefitId))
                {
                    benefitJoins.eq(HrBenefit.BENEFITID, benefitId);
                }
                else
                {
                    benefitJoins.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, benefitCategoryId);
                }

                HrBenefitJoin bj = benefitJoins.first();

                if (bj == null)
                    continue;

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, be.getSsn(), alternateRow);
                write(table, be.getLastName() + ", " + be.getFirstName(), alternateRow);
                if (isEmpty(statusId))
                {
                    write(table, be.getStatus(), alternateRow);
                    write(table, DateUtils.getDateFormatted(be.getEmployeeStatusDate()), alternateRow);
                    write(table, bj.getHrBenefitConfig().getName(), alternateRow);
                }
                else
                {
                    write(table, DateUtils.getDateFormatted(be.getEmployeeStatusDate()), alternateRow);
                    write(table, bj.getHrBenefitConfig().getName(), alternateRow);
                }

                unknowns++;
            }

            if (unknowns != 0)
                this.writeOrgGroupTotal(table, "Unspecified", unknowns);

            addTable(table);

        }

    }

    protected void writeOrgGroupTotal(final PdfPTable table, final String orgGroup, final int total) {
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("", this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Total for Organizational Group " + orgGroup + ": " + total, this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell);
    }

    /*
    public static void main(String[] args) {
    try	{
    new HREnrollmentListReport().build("Medical", "HMO", "Active", "LF (WCG)");
    } catch (Exception e) {
    logger.info(e.getMessage());
    e.printStackTrace();
    }
    }
     */
}
