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

import com.arahant.beans.CompanyDetail;
import com.arahant.beans.Employee;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.OrgGroup;
import com.arahant.beans.OrgGroupAssociation;
import com.arahant.beans.Person;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BOrgGroup;
import com.arahant.business.BRight;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 */
public class PendingBenefitRequests extends ReportBase {

	public PendingBenefitRequests() throws ArahantException {
        super("PendingBeneRqts", "Pending Benefit Requests");
    }

    public String build(String sortBy) throws DocumentException {
        try {
            PdfPTable table;
            addHeaderLine();
			List<Character> benefitWizardStatuses = new ArrayList<Character>();
			benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_FINALIZED);
			benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_NO_CHANGE);
			benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_PROCESSED);
			Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
			Set<OrgGroup> ogs = new HashSet<OrgGroup>();
			for(OrgGroupAssociation o : oga)
				ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());

			if(sortBy.equals("B")) {
				HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
																					  .orderBy(HrBenefitCategory.DESCRIPTION)
																					  .joinTo(HrBenefitCategory.HRBENEFIT)
																					  .eq(HrBenefit.TIMERELATED, 'N')
																					  .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
																					  .joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
																					  .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
																					  .eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
																					  .joinTo(HrBenefitJoin.PAYING_PERSON)
																					  .in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses);
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
				table = makeTable(new int[]{5, 15, 80});

				OrderComparator comparator = new OrderComparator();

				for (HrBenefitCategory bCat : hcu.list()) {
					writeColHeader(table, bCat.getDescription(), Element.ALIGN_LEFT, 3);
					for(HrBenefit bene : bCat.getBenefits()) {
						writeColHeader(table, "", Element.ALIGN_LEFT, 1, true);
						writeColHeader(table, bene.getName(), Element.ALIGN_LEFT, 2, true);
						for(HrBenefitConfig c : bene.getBenefitConfigs()) {
							List<HrBenefitJoin> sortedJoins = makeList(c.getHrBenefitJoins());
							Collections.sort(sortedJoins, comparator);

							for(HrBenefitJoin h : sortedJoins) {
								BHRBenefitJoin bh = new BHRBenefitJoin(h);
								if(bh.getPayingPersonId().equals(bh.getCoveredPersonId())) {
									writeAlign(table, "", Element.ALIGN_LEFT, false);
									writeAlign(table, "", Element.ALIGN_LEFT, false);
									writeAlign(table, bh.getPayingPerson().getNameLFM() + " - " + 
													  bh.getBenefitConfigName() + " - " +
													  DateUtils.getDateFormatted(bh.getCoverageStartDate()), Element.ALIGN_LEFT, false);
								}
							}
						}
					}

					addTable(table);
					table.flushContent();
				}
			}
			else if(sortBy.equals("E")) {
				HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class)
																			 .orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME)
																			 .in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses);
				hcu.joinTo(Employee.HR_BENEFIT_JOINS_WHERE_PAYING)
				   .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
				   .eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON);
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);

				table = makeTable(new int[]{20, 80});

				List<Employee> empList = hcu.list();

				for (Employee e : empList) {
					writeColHeader(table, e.getNameLFM(), Element.ALIGN_LEFT, 2, true);
					for(HrBenefitJoin h : e.getHrBenefitJoinsWherePaying()) {
						BHRBenefitJoin bh = new BHRBenefitJoin(h);
						if(!bh.isBenefitDecline() && 
							bh.getBenefitConfig().getBenefit().getTimeRelated() == 'N' &&
							bh.getPayingPersonId().equals(bh.getCoveredPersonId())) {
							writeAlign(table, "", Element.ALIGN_LEFT, false);
							writeAlign(table, bh.getBenefitName() + " - " + bh.getBenefitConfigName(), Element.ALIGN_LEFT, false);
						}
					}

					addTable(table);
					table.flushContent();
				}
			}

			table = makeTable(new int[]{100});
			addTable(table);

        } finally {
            close();
        }

        return getFilename();
    }

	private List makeList(Set set) {
		List list = new ArrayList();
		Iterator i = set.iterator();
		while(i.hasNext())
			list.add(i.next());
		return list;
	}

	private class OrderComparator implements Comparator {
		@Override
		public int compare(Object emp1, Object emp2) {
			if(emp1 == null)
				return emp2 == null ? 0 : -1;
			else if(emp2 == null)
				return 1;
			else {
				if(emp1 instanceof HrBenefit) {
					if(((HrBenefit)emp1).getName() == null)
						return ((HrBenefit)emp2).getName() == null ? 0 : -1;
					else if(((HrBenefit)emp2).getName() == null)
						return 1;
					else
						return ((HrBenefit)emp1).getName().toLowerCase().compareTo(((HrBenefit)emp2).getName().toLowerCase());
				}
				else if(emp1 instanceof HrBenefitJoin) {
					if(((HrBenefitJoin)emp1).getPayingPerson().getLname() == null)
						return ((HrBenefitJoin)emp2).getPayingPerson().getLname() == null ? 0 : -1;
					else if(((HrBenefitJoin)emp2).getPayingPerson().getLname() == null)
						return 1;
					else
						return ((HrBenefitJoin)emp1).getPayingPerson().getLname().toLowerCase().compareTo(((HrBenefitJoin)emp2).getPayingPerson().getLname().toLowerCase());
				}
				else if(emp1 instanceof HrBenefitConfig) {
					if(((HrBenefitConfig)emp1).getName() == null)
						return ((HrBenefitConfig)emp2).getName() == null ? 0 : -1;
					else if(((HrBenefitConfig)emp2).getName() == null)
						return 1;
					else
						return ((HrBenefitConfig)emp1).getName().toLowerCase().compareTo(((HrBenefitConfig)emp2).getName().toLowerCase());
				}
				else
					return 0;
			}
		}
	}

    public static void main(String args[]) {
        try {
//			ArahantSession.multipleCompanySupport = true;
			ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class,"00000-0000072568"));
            new PendingBenefitRequests().build("B");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
