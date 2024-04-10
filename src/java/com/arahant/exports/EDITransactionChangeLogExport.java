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

import com.arahant.beans.CompanyBase;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.business.BCompanyBase;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BHRBenefitJoinH;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileWriter;
import com.arahant.utils.FileSystemUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class EDITransactionChangeLogExport {
    public String build(String[] changeTypes, String[] vendorIds, int startDate, int endDate) throws Exception {
		HibernateSessionUtil hsu = ArahantSession.getHSU();
        File csvFile = new File(FileSystemUtils.getWorkingDirectory(), "Transaction Change Log Export " + DateUtils.now() + ".csv");
        DelimitedFileWriter dfw = new DelimitedFileWriter(csvFile.getAbsolutePath(), false);
		List<CompanyBase> vendors = new ArrayList<CompanyBase>();
		List<String> vendIds = new ArrayList<String>();
		char[] recordChangeTypes = new char[changeTypes.length];

		if(vendorIds != null && vendorIds.length > 0)
			for(String v : vendorIds) {
				vendors.add(BCompanyBase.get(v).getBean());
				vendIds.add(v);
			}
		if(changeTypes != null && changeTypes.length > 0)
			for(int i = 0; i < changeTypes.length; i++)
				recordChangeTypes[i] = changeTypes[i].charAt(0);
		
        try {
            //Writing title
            dfw.writeField("Transaction Change Log");
            dfw.endRecord();

            //Writing column headers
            dfw.writeField("Carrier");
            dfw.writeField("Request Type");
            dfw.writeField("Date Entered");
            dfw.writeField("Group Name");
            dfw.writeField("F Name");
            dfw.writeField("L Name");
            dfw.writeField("Effective Date");
            dfw.writeField("SSN");
            dfw.endRecord();

			List<HrBenefit> benes = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFIT_PROVIDER, vendors).list();

			for(HrBenefit bene : benes) {
				HibernateCriteriaUtil<HrBenefitJoinH> hcuI = hsu.createCriteria(HrBenefitJoinH.class)
																.in(HrBenefitJoinH.RECORD_CHANGE_TYPE, recordChangeTypes)
																.ne(HrBenefitJoinH.POLICY_END_DATE, 0)
																.between(HrBenefitJoinH.RECORD_CHANGE_DATE, DateUtils.getDate(startDate), DateUtils.getDate(endDate))
																.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0)
																.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
				
				HibernateScrollUtil<HrBenefitJoinH> scr = hcuI.scroll();

				while(scr.next()) {
					HrBenefitJoinH jh = new BHRBenefitJoinH(scr.get()).getBean();

					dfw.writeField(jh.getHrBenefitConfig().getHrBenefit().getProvider().getName());
					dfw.writeField(jh.getRecordChangeType() + "");
					dfw.writeField(DateUtils.getDateFormatted(jh.getRecordChangeDate()));
					dfw.writeField(jh.getPayingPerson().getOrgGroupAssociations().iterator().next().getOrgGroup().getName());
					dfw.writeField(jh.getCoveredPerson().getFname());
					dfw.writeField(jh.getCoveredPerson().getLname());
					dfw.writeField(DateUtils.getDateFormatted(jh.getCoverageStartDate()));
					dfw.writeField(jh.getCoveredPerson().getUnencryptedSsn());
					dfw.endRecord();
				}

				HibernateCriteriaUtil<HrBenefitJoin> hcuA = hsu.createCriteria(HrBenefitJoin.class)
															   .eq(HrBenefitJoin.BENEFIT_DECLINED, 'N')
															   .eq(HrBenefitJoin.APPROVED, 'Y')
															   .in(HrBenefitJoin.RECORD_CHANGE_TYPE, recordChangeTypes)
															   .between(HrBenefitJoin.RECORD_CHANGE_DATE, DateUtils.getDate(startDate), DateUtils.getDate(endDate))
															   .ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
															   .joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);

				HibernateScrollUtil<HrBenefitJoin> scr2 = hcuA.scroll();

				while(scr2.next()) {
					HrBenefitJoin j = new BHRBenefitJoin(scr2.get()).getBean();

					dfw.writeField(j.getHrBenefitConfig().getHrBenefit().getProvider().getName());
					dfw.writeField(j.getRecordChangeType() + "");
					dfw.writeField(DateUtils.getDateFormatted(j.getRecordChangeDate()));
					dfw.writeField(j.getPayingPerson().getCompanyBase().getName());
					dfw.writeField(j.getCoveredPerson().getFname());
					dfw.writeField(j.getCoveredPerson().getLname());
					dfw.writeField(DateUtils.getDateFormatted(j.getCoverageStartDate()));
					dfw.writeField(j.getCoveredPerson().getUnencryptedSsn());
					dfw.endRecord();
				}
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


            new EDITransactionChangeLogExport().build(new String[]{"N", "M", "D"}, new String[]{"00001-0000072635"}, 20110516, date);
//            adpTime.build(startDate, endDate, orgGroupID);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
