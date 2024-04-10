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

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BenefitAuditFileExport {
    public String build(String[] catIds, String[] beneIds, String[] configIds, String status, int date) throws Exception {
        File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "Audit File Export " + DateUtils.now() + ".csv");
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);
		List<String> beneCatIdList = new ArrayList<String>();
		List<String> beneIdList = new ArrayList<String>();
		List<String> beneConfigIdList = new ArrayList<String>();

		if(catIds != null && catIds.length > 0)
			for(String c : catIds)
				beneCatIdList.add(c);
		if(beneIds != null && beneIds.length > 0)
			for(String b : beneIds)
				beneIdList.add(b);
		if(configIds != null && configIds.length > 0)
			for(String c : configIds)
				beneConfigIdList.add(c);

        try {
            //Writing title
            dfw.writeField("Audit File Export");
            dfw.endRecord();

            //Writing column headers
            dfw.writeField("Benefit Category");
            dfw.writeField("Benefit Name");
            dfw.writeField("Benefit Config");
            dfw.writeField("SSN");
            dfw.writeField("F Name");
            dfw.writeField("L Name");
            dfw.writeField("Status");
            dfw.writeField("F Name");
            dfw.writeField("L Name");
            dfw.writeField("Effective Date");
            dfw.writeField("SSN");
            dfw.endRecord();

			HibernateCriteriaUtil<HrBenefitJoin> hcuCat = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																				 .gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, date, 0)
																				 .eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
																				 .eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
			hcuCat.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				  .joinTo(HrBenefitConfig.HR_BENEFIT)
				  .joinTo(HrBenefit.BENEFIT_CATEGORY).orderBy(HrBenefitCategory.DESCRIPTION)
				  .in(HrBenefitCategory.BENEFIT_CATEGORY_ID, beneCatIdList);
			if(status.equals("I"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).inactiveEmployee();
			else if(status.equals("A"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).activeEmployee();

//			HibernateCriterionUtil configCrit = hcuCat.makeCriteria();
//
//			HibernateCriteriaUtil hcuConfig = hcuCat.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG, "configTable");
//			HibernateCriterionUtil beneCrit = hcuConfig.makeCriteria();
//
//			HibernateCriteriaUtil hcuBene = hcuConfig.joinTo(HrBenefitConfig.HR_BENEFIT, "beneTable");
//			HibernateCriterionUtil catCrit = hcuBene.makeCriteria();
//
//			HibernateCriterionUtil orCrit = hcuCat.makeCriteria();
//			orCrit.or(configCrit.in(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, beneConfigIdList),
//					  beneCrit.in("configTable." + HrBenefitConfig.HR_BENEFIT_ID, beneIdList),
//					  catCrit.in("beneTable." + HrBenefit.BENEFIT_CATEGORY_ID, beneCatIdList));
//			orCrit.add();

			HibernateScrollUtil<HrBenefitJoin> scr = hcuCat.scroll();

			while(scr.next()) {
				BHRBenefitJoin bj = new BHRBenefitJoin(scr.get());

				dfw.writeField(bj.getBenefitCategoryName());
				dfw.writeField(bj.getBenefitName());
				dfw.writeField(bj.getBenefitConfigName());
				dfw.writeField(bj.getPayingPerson().getSsn());
				dfw.writeField(bj.getPayingPerson().getFirstName());
				dfw.writeField(bj.getPayingPerson().getLastName());
				dfw.writeField(bj.getPayingPerson().getStatus());
				dfw.writeField(bj.getCoveredPerson().getFirstName());
				dfw.writeField(bj.getCoveredPerson().getLastName());
				dfw.writeField(date);
				dfw.writeField(bj.getCoveredPerson().getSsn());
				dfw.endRecord();
			}

			HibernateCriteriaUtil<HrBenefitJoin> hcuBene = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																				  .gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, date, 0)
																				  .eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
																				  .eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
			hcuBene.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				   .joinTo(HrBenefitConfig.HR_BENEFIT).orderBy(HrBenefit.NAME)
				   .in(HrBenefit.BENEFITID, beneIdList);
			if(status.equals("I"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).inactiveEmployee();
			else if(status.equals("A"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).activeEmployee();

			scr = hcuBene.scroll();

			while(scr.next()) {
				BHRBenefitJoin bj = new BHRBenefitJoin(scr.get());

				dfw.writeField(bj.getBenefitCategoryName());
				dfw.writeField(bj.getBenefitName());
				dfw.writeField(bj.getBenefitConfigName());
				dfw.writeField(bj.getPayingPerson().getSsn());
				dfw.writeField(bj.getPayingPerson().getFirstName());
				dfw.writeField(bj.getPayingPerson().getLastName());
				dfw.writeField(bj.getPayingPerson().getStatus());
				dfw.writeField(bj.getCoveredPerson().getFirstName());
				dfw.writeField(bj.getCoveredPerson().getLastName());
				dfw.writeField(date);
				dfw.writeField(bj.getCoveredPerson().getSsn());
				dfw.endRecord();
			}

			HibernateCriteriaUtil<HrBenefitJoin> hcuConfig = ArahantSession.getHSU().createCriteria(HrBenefitJoin.class)
																					.gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, date, 0)
																					.eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
																					.eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
			hcuConfig.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).orderBy(HrBenefitConfig.NAME)
				     .in(HrBenefitConfig.BENEFIT_CONFIG_ID, beneConfigIdList);
			if(status.equals("I"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).inactiveEmployee();
			else if(status.equals("A"))
				hcuCat.joinTo(HrBenefitJoin.PAYING_PERSON).activeEmployee();

			scr = hcuConfig.scroll();

			while(scr.next()) {
				BHRBenefitJoin bj = new BHRBenefitJoin(scr.get());

				dfw.writeField(bj.getBenefitCategoryName());
				dfw.writeField(bj.getBenefitName());
				dfw.writeField(bj.getBenefitConfigName());
				dfw.writeField(bj.getPayingPerson().getSsn());
				dfw.writeField(bj.getPayingPerson().getFirstName());
				dfw.writeField(bj.getPayingPerson().getLastName());
				dfw.writeField(bj.getPayingPerson().getStatus());
				dfw.writeField(bj.getCoveredPerson().getFirstName());
				dfw.writeField(bj.getCoveredPerson().getLastName());
				dfw.writeField(date);
				dfw.writeField(bj.getCoveredPerson().getSsn());
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
            int date = DateUtils.now();


            new BenefitAuditFileExport().build(null, new String[]{"00001-0000142783"}, null, "A", date);
//            adpTime.build(startDate, endDate, orgGroupID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
