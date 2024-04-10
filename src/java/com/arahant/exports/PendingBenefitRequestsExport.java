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
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 */
public class PendingBenefitRequestsExport {
    public String build(String sortBy) throws Exception {
		File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "Pending Benefit Request " + DateUtils.now() + ".csv");
		DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);

		List<Character> benefitWizardStatuses = new ArrayList<Character>();
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_FINALIZED);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_NO_CHANGE);
		benefitWizardStatuses.add(Employee.BENEFIT_WIZARD_STATUS_PROCESSED);
		Set<OrgGroupAssociation> oga = ArahantSession.getHSU().getCurrentPerson().getOrgGroupAssociations();
		Set<OrgGroup> ogs = new HashSet<OrgGroup>();
		for(OrgGroupAssociation o : oga)
			ogs.addAll(new BOrgGroup(o.getOrgGroup()).getAllOrgGroupsInHierarchy2());
		
		try {
			dfw.writeField("Benefit Category");
			dfw.writeField("Benefit Name");
			dfw.writeField("Benefit Config");
			dfw.writeField("Last Name");
			dfw.writeField("First Name");
			dfw.writeField("Middle Name");
			dfw.writeField("Coverage Start");
			dfw.endRecord();

			if(sortBy.equals("B")) {
				HibernateCriteriaUtil<HrBenefitCategory> hcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
																					  .orderBy(HrBenefitCategory.DESCRIPTION)
																					  .joinTo(HrBenefitCategory.HRBENEFIT)
																					  .eq(HrBenefit.TIMERELATED, 'N')
																					  .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
																					  .joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
																					  .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
																					  .joinTo(HrBenefitJoin.PAYING_PERSON)
																					  .in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses);
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);
				
				for (HrBenefitCategory cat : hcu.list()) {
					List<HrBenefit> sortedBenefits = ArahantSession.getHSU().createCriteria(HrBenefit.class)
																			.eq(HrBenefit.TIMERELATED, 'N')
																			.orderBy(HrBenefit.NAME)
																			.joinTo(HrBenefit.BENEFIT_CATEGORY)
																			.eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, cat.getBenefitCatId())
																			.list();
					for(HrBenefit bene : sortedBenefits) {
						HibernateCriteriaUtil<HrBenefitJoin> joinHcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																							  .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
																							  .eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON);
						joinHcu.joinTo(HrBenefitJoin.PAYING_PERSON)
							   .in(Employee.BENEFIT_WIZARD_STATUS, benefitWizardStatuses)
							   .orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME);
						List<HrBenefitJoin> sortedJoins = joinHcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
																 .orderBy(HrBenefitConfig.NAME)
																 .eq(HrBenefitConfig.HR_BENEFIT, bene)
																 .list();

						for(HrBenefitJoin h : sortedJoins) {
							BHRBenefitJoin bh = new BHRBenefitJoin(h);
							if(bh.getPayingPersonId().equals(bh.getCoveredPersonId())) {
								dfw.writeField(cat.getDescription());
								dfw.writeField(bene.getName());
								dfw.writeField(bh.getBenefitConfigName());
								dfw.writeField(bh.getPayingPerson().getLastName());
								dfw.writeField(bh.getPayingPerson().getFirstName());
								dfw.writeField(bh.getPayingPerson().getMiddleName());
								dfw.writeField(DateUtils.getDateFormatted(bh.getCoverageStartDate()));
								dfw.writeField(bh.getBenefitJoinId());
								dfw.endRecord();
							}
						}
					}
				}
			}
			else {
				HibernateCriteriaUtil<Employee> hcu = ArahantSession.getHSU().createCriteria(Employee.class)
																			 .orderBy(Person.LNAME).orderBy(Person.FNAME).orderBy(Person.MNAME);
				hcu.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
					.eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
					.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
					.joinTo(HrBenefitConfig.HR_BENEFIT)
					.eq(HrBenefit.TIMERELATED, 'N');
				if(BRight.checkRight("SeeAllOrgGroups") != BRight.ACCESS_LEVEL_WRITE)
					hcu.joinTo(Employee.ORGGROUPASSOCIATIONS).in(OrgGroupAssociation.ORGGROUP, ogs);

				for (Employee e : hcu.list()) {
					HibernateCriteriaUtil<HrBenefitCategory> catHcu = ArahantSession.getHSU().createCriteria(HrBenefitCategory.class)
																							 .orderBy(HrBenefitCategory.DESCRIPTION)
																							 .joinTo(HrBenefitCategory.HRBENEFIT)
																							 .eq(HrBenefit.TIMERELATED, 'N')
																							 .joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
																							 .joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
																							 .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N')
																							 .eq(HrBenefitJoin.PAYING_PERSON, e);

					for (HrBenefitCategory cat : catHcu.list()) {
						HibernateCriteriaUtil<HrBenefit> beneHcu = ArahantSession.getHSU().createCriteria(HrBenefit.class)
																						  .orderBy(HrBenefit.NAME)
																						  .eq(HrBenefit.TIMERELATED, 'N');
						beneHcu.joinTo(HrBenefit.BENEFIT_CATEGORY)
							   .eq(HrBenefitCategory.BENEFIT_CATEGORY_ID, cat.getBenefitCatId());
						List<HrBenefit> sortedBenefits = beneHcu.joinTo(HrBenefit.HR_BENEFIT_CONFIGS)
																.joinTo(HrBenefitConfig.HR_BENEFIT_JOINS)
																.eq(HrBenefitJoin.PAYING_PERSON, e)
																.eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
																.list();
						for(HrBenefit bene : sortedBenefits) {
							HibernateCriteriaUtil<HrBenefitJoin> joinHcu = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																								  .eq(HrBenefitJoin.BENEFIT_APPROVED, 'N');
							joinHcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
								   .orderBy(HrBenefitConfig.NAME)
								   .eq(HrBenefitConfig.HR_BENEFIT, bene)
								   .list();
							List<HrBenefitJoin> sortedJoins = joinHcu.eq(HrBenefitJoin.PAYING_PERSON, e)
																	 .eqJoinedField(HrBenefitJoin.PAYING_PERSON, HrBenefitJoin.COVERED_PERSON)
																	 .list();

							for(HrBenefitJoin h : sortedJoins) {
								BHRBenefitJoin bh = new BHRBenefitJoin(h);
								if(bh.getPayingPersonId().equals(bh.getCoveredPersonId())) {
									dfw.writeField(e.getLname());
									dfw.writeField(e.getFname());
									dfw.writeField(e.getMname());
									dfw.writeField(cat.getDescription());
									dfw.writeField(bene.getName());
									dfw.writeField(bh.getBenefitConfigName());
									dfw.writeField(DateUtils.getDateFormatted(bh.getCoverageStartDate()));
									dfw.writeField(bh.getBenefitJoinId());
									dfw.endRecord();
								}
							}
						}
					}
				}
			}
        } finally {
			dfw.close();
		}

        return csvFile.getName();
    }

    public static void main(String args[]) {
        try {
//			ArahantSession.multipleCompanySupport = true;
			ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().get(CompanyDetail.class,"00000-0000072568"));
            new PendingBenefitRequestsExport().build("B");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
