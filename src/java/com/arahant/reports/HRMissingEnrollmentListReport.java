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
 * Created on Jul 12, 2007
 * 
 */
package com.arahant.reports;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;

import com.arahant.beans.*;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantConstants;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateSessionUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.HashSet;
import java.util.Set;


/**
 * 
 *
 * Created on Jul 12, 2007
 *
 */
public class HRMissingEnrollmentListReport extends ReportBase {

    public HRMissingEnrollmentListReport() throws ArahantException {
        super("HRMissEnrListRpt", "Missing Enrollment List");
    }
    private final HibernateSessionUtil hsu = ArahantSession.getHSU();

    public String build(final String benefitCategoryId, final String benefitId, final String statusId, final String orgGroupId, boolean includeDeclines, int notEnrolledAsOf) throws FileNotFoundException, DocumentException, ArahantException {

        try {
            final PdfPTable table = makeTable(new int[]{1});
            writeHeader(table, "Not Enrolled in Benefit Category: " + hsu.get(HrBenefitCategory.class, benefitCategoryId).getDescription());
            writeHeader(table, "Not Enrolled in Benefit: " + (isEmpty(benefitId) ? "(any)" : hsu.get(HrBenefit.class, benefitId).getName()));
            writeHeader(table, "Employee Status: " + (isEmpty(statusId) ? "(any)" : hsu.get(HrEmployeeStatus.class, statusId).getName()));
            writeHeader(table, "Organizational Group: " + (isEmpty(orgGroupId) ? "(any)" : hsu.get(OrgGroup.class, orgGroupId).getName()));
            writeHeader(table, "Include Declined Benefits as not enrolled: " + (includeDeclines ? "Yes" : "No"));
            writeHeader(table, "Not Enrolled as of: " + DateUtils.getDateFormatted(notEnrolledAsOf));
            addTable(table);

            addHeaderLine();
            writeEmployees(benefitCategoryId, benefitId, statusId, orgGroupId, includeDeclines, notEnrolledAsOf);
        } finally {
            close();

        }

        return getFilename();
    }

    @SuppressWarnings("unchecked")
    protected void writeEmployees(final String benefitCategoryId, final String benefitId, final String statusId, final String orgGroupId, boolean includeDeclines, int notEnrolledAsOf) throws DocumentException, ArahantException {
        PdfPTable table;

        // THIS NEEDS TO BE BROKEN DOWN BY ORG GROUP ... multi-dimensional or something
        // WITHIN THE BREAK DOWN OF EACH ORG GROUP IT SHOULD BE ORDERED BY SAME PLAN
        // FOR CHOSEN BENEFIT CATEGORY CATEGORY ... SO IF MEDICAL BENEFIT CATEGORY CATEGORY
        // THEN GET THE CHOSEN BENEFIT CATEGORY THAT IS A MEDICAL BENEFIT CATEGORY CATEGORY

        List<OrgGroup> ogList;

        if (isEmpty(orgGroupId)) {
            HibernateCriteriaUtil<OrgGroup> hcu = hsu.createCriteria(OrgGroup.class).orderBy(OrgGroup.NAME).eq(OrgGroup.ORGGROUPTYPE, ArahantConstants.COMPANY_TYPE);
			if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE) {
				Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
				Set<OrgGroup> ogs = new HashSet<OrgGroup>();
				for(OrgGroupAssociation o : oga)
					ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
				List<String> orgIds = new ArrayList<String>();
				for(OrgGroup og : ogs)
					orgIds.add(og.getOrgGroupId());

				hcu.in(OrgGroup.ORGGROUPID, orgIds);
			}

			ogList = hcu.list();
        } else {
            ogList = new ArrayList<OrgGroup>(1);
            ogList.add(hsu.get(OrgGroup.class, orgGroupId));
        }

        for (final OrgGroup og : ogList) {
            boolean alternateRow = true;
            final String currentOrgGroup = og.getName();
            int currentOrgGroupTotal = 0;

            if (isEmpty(statusId) && includeDeclines) {
                table = makeTable(new int[]{18, 30, 20, 15, 15});
            } else if (!isEmpty(statusId) && includeDeclines) {
                table = makeTable(new int[]{18, 47, 20, 15});
            } else if (isEmpty(statusId) && !includeDeclines) {
                table = makeTable(new int[]{18, 30, 20, 15});
            } else {
                table = makeTable(new int[]{18, 62, 20});
            }

            writeColHeader(table, "Employee SSN", Element.ALIGN_LEFT);
            writeColHeader(table, "Employee Name", Element.ALIGN_LEFT);
            if (isEmpty(statusId)) {
                writeColHeader(table, "Employee Status", Element.ALIGN_LEFT);
            }
            writeColHeader(table, "Status Date", Element.ALIGN_RIGHT);
            if (includeDeclines) {
                writeColHeader(table, "Declined", Element.ALIGN_LEFT);
            }

            table.setHeaderRows(1);




            // !!!! THE BELOW VALUE DEPENDS ON THE REPORT PARAMS - GET PLAN FOR REQUESTED 
            // BENEFIT CATEGORY CATEGORY (e.g. if MEDICAL CATEGORY GET THE MEDICAL BENEFIT CHOSEN)
            // USE NONE IF NOT SET
            // THIS IS THE SAME FIELD REFERENCED FOR HOW IT IS SORTED WITHIN EACH ORG GROUP AS STATED ABOVE


            ScrollableResults escroll;

            String status = "";

            String subquery =
                    " and (" +
                    "        (" +
                    "	        (bene.hrBenefitCategory = :benefitCategory or cat = :benefitCategory)" +
                    "	        and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + notEnrolledAsOf + ") " +
                    "           and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + notEnrolledAsOf + ")" +
                    "        )" +
                    //or they have no configs that match up
                    "        or emp.personId not in " +
                    "           (select emp2.personId from Employee emp2 join emp2." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins2 " +
                    "            where beneJoins2.hrBenefitConfig.hrBenefit.hrBenefitCategory = :benefitCategory" +
                    "            and (beneJoins2.policyEndDate=0 or beneJoins2.policyEndDate>=" + notEnrolledAsOf + ") " +
                    "            and (beneJoins2.policyStartDate!=0 and beneJoins2.policyStartDate<=" + notEnrolledAsOf + ")" +
                    "           )" +
                    "     )";

            String subquery2 =
                    " and (" +
                    "        (" +
                    "           (bene = :benefit)" +
                    "           and (beneJoins.policyEndDate=0 or beneJoins.policyEndDate>=" + notEnrolledAsOf + ") " +
                    "           and (beneJoins.policyStartDate!=0 and beneJoins.policyStartDate<=" + notEnrolledAsOf + ")" +
                    "        )" +
                    //or they have no configs that match up
                    "        or emp.personId not in " +
                    "           (select emp2.personId from Employee emp2 join emp2." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins2" +
                    "            where beneJoins2.hrBenefitConfig.hrBenefit = :benefit" +
                    "            and (beneJoins2.policyEndDate=0 or beneJoins2.policyEndDate>=" + notEnrolledAsOf + ") " +
                    "            and (beneJoins2.policyStartDate!=0 and beneJoins2.policyStartDate<=" + notEnrolledAsOf + ")" +
                    "           )" +
                    "     )" +
                    ")";

            if (!isEmpty(statusId)) {	  //TODO: someday change this from HQL
                if (isEmpty(benefitId)) {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate " +
                            "from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga " +
                            "left join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins left join beneJoins.hrBenefit bene left join beneJoins.hrBenefitCategory cat " +
                            "where hist.hrEmployeeStatus = :stat and hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + notEnrolledAsOf + ") \n" +
                            " and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            subquery +
                            " order by emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate ");

                    q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
                    q.setEntity("og", og);
                    q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
                    status = hsu.get(HrEmployeeStatus.class, statusId).getName();
                    //  empList=q.list();
                    escroll = q.scroll();
                } else {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate " +
                            " from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga " +
                            "left join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins left join beneJoins.hrBenefit bene left join beneJoins.hrBenefitCategory cat" +
                            " where hist.hrEmployeeStatus = :stat and hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + notEnrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            subquery2 +
                            " order by emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate ");

                    q.setEntity("stat", hsu.get(HrEmployeeStatus.class, statusId));
                    q.setEntity("og", og);
                    q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
                    status = hsu.get(HrEmployeeStatus.class, statusId).getName();
                    //  empList=q.list();
                    escroll = q.scroll();
                }

            } else {
                if (isEmpty(benefitId)) {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate, hist.effectiveDate " +
                            " from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga" +
                            " join hist.hrEmployeeStatus stat " +
                            "left join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins left join beneJoins.hrBenefit bene left join beneJoins.hrBenefitCategory cat" +
                            " where hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + notEnrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            subquery +
                            " order by emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate, hist.effectiveDate ");
                    q.setEntity("og", og);
                    q.setEntity("benefitCategory", hsu.get(HrBenefitCategory.class, benefitCategoryId));
                    //  empList=q.list();
                    escroll = q.scroll();
                } else {
                    final Query q = hsu.createQuery("select distinct emp.ssn, emp.lname, emp.fname, stat.name, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate " +
                            " from Employee emp join emp.hrEmplStatusHistories hist join emp.orgGroupAssociations oga" +
                            " join hist.hrEmployeeStatus stat " +
                            "left join emp." + Person.HR_BENEFIT_JOINS_WHERE_PAYING + " beneJoins left join beneJoins.hrBenefit bene left join beneJoins.hrBenefitCategory cat" +
                            " where hist.effectiveDate = " +
                            "(select max(effectiveDate) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate<=" + notEnrolledAsOf + ") \n and oga.orgGroup = :og " +
                            " and ((select count(*) from HrEmplStatusHistory hist2 where hist2.employee=emp and hist2.effectiveDate=hist.effectiveDate)=1 " +
                            " or hist.hrEmployeeStatus.dateType='S' ) " +
                            subquery2 +
                            " order by emp.lname, emp.fname, beneJoins.hrBenefit.benefitId, beneJoins.hrBenefitCategory.benefitCatId, hist.effectiveDate ");
                    q.setEntity("og", og);
                    q.setEntity("benefit", hsu.get(HrBenefit.class, benefitId));
                    //  empList=q.list();
                    escroll = q.scroll();

                }



            //	empList=hcu.list();
            //	escroll=hcu.scroll().internalScroll();
            }

            String prev = "";

            while (escroll.next()) {
                final String ssn = escroll.getString(0);
                final String lname = escroll.getString(1);
                final String fname = escroll.getString(2);

                if (ssn.equals(prev)) {
                    continue;
                }

                prev = ssn;

                if (isEmpty(status)) {
                    if (!includeDeclines && (!isEmpty(escroll.getString(4)) || !isEmpty(escroll.getString(5)))) {
                        if (escroll.next()) {
                            continue;
                        } else {
                            break;
                        }
                    }
                } else {
                    if (!includeDeclines && (!isEmpty(escroll.getString(3)) || !isEmpty(escroll.getString(4)))) {
                        if (escroll.next()) {
                            continue;
                        } else {
                            break;
                        }
                    }
                }

                currentOrgGroupTotal++;

                // toggle the alternate row
                alternateRow = !alternateRow;

                write(table, ssn, alternateRow);
                write(table, lname + ", " + fname, alternateRow);

                if (isEmpty(status)) {
                    write(table, escroll.getString(3), alternateRow);
                    writeRight(table, DateUtils.getDateFormatted(escroll.getInteger(6)), alternateRow);
                    if (includeDeclines) {
                        if (isEmpty(escroll.getString(4)) && isEmpty(escroll.getString(5))) {
                            write(table, "No", alternateRow);
                        } else {
                            write(table, "Yes", alternateRow);

                            if (escroll.next()) {
                                continue;
                            } else {
                                break;
                            }
                        }
                    }
                } else {
                    writeRight(table, DateUtils.getDateFormatted(escroll.getInteger(5)), alternateRow);
                    if (includeDeclines) {
                        if (isEmpty(escroll.getString(3)) && isEmpty(escroll.getString(4))) {
                            write(table, "No", alternateRow);
                        } else {
                            write(table, "Yes", alternateRow);

                            if (escroll.next()) {
                                continue;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            escroll.close();
            this.writeOrgGroupTotal(table, currentOrgGroup, currentOrgGroupTotal);

            addTable(table);
        }
       
    }

    protected void writeEnrolledInCategoryTotal(final PdfPTable table, final String benefitCategoryname, final int total) {
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("", this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Total for Benefit " + benefitCategoryname + ": " + total, this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("", this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        table.addCell(cell);
    }

    protected void writeOrgGroupTotal(final PdfPTable table, final String orgGroup, final int total) {
        PdfPCell cell;

        cell = new PdfPCell(new Paragraph("", this.baseFont));
        cell.disableBorderSide(1 | 2 | 4 | 8);
        cell.setColspan(4);
        table.addCell(cell);

        cell = new PdfPCell(new Paragraph("Total for Location " + orgGroup + ": " + total, this.baseFont));
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

	
