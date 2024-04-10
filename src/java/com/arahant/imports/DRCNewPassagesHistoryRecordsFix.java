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

package com.arahant.imports;

import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.beans.Person;
import com.arahant.beans.VendorCompany;
import com.arahant.business.BCompany;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class DRCNewPassagesHistoryRecordsFix {
HibernateSessionUtil hsu = ArahantSession.getHSU();

	public void importRecords(String filename)
	{
		try{
			DelimitedFileReader fr = new DelimitedFileReader(filename);
			fr.nextLine();
			int count = 0;
			String query = "";
			while (fr.nextLine()) {
				String lname = fr.nextString();
				String fname = fr.nextString();
				String carrierName = fr.nextString();
				String statusDate = fr.nextString();
				String[] date = statusDate.split("/");
				String year = date[2];
				String month = date[0];
				String day = date[1];

				if(month.length() == 1)
				{
					month = "0" + month;
				}
				if(Integer.parseInt(month) > 3)
				{
					year = "2010";
				}


				if(++count % 25 == 0)
				{
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				Person p = hsu.createCriteria(Person.class)
						.eq(Person.FNAME, fname)
						.eq(Person.LNAME, lname)
						.eq(Person.RECORD_TYPE, 'R')
						.first();

				VendorCompany v = hsu.createCriteria(VendorCompany.class)
									.eq(VendorCompany.NAME, carrierName)
									.eq(VendorCompany.ASSOCIATED_COMPANY, ArahantSession.getHSU().getCurrentCompany())
									.first();
				if(p == null)
				{
					System.out.println("No person record found for: " + fname + " " + lname);
					continue;
				}
				if(v == null)
				{
					System.out.println("Vendor not found: " + carrierName + " (" + fname + " " + lname + ")");
					continue;
				}

				{
					List<HrBenefitJoinH> historyJoins = hsu.createCriteria(HrBenefitJoinH.class)
							.eq(HrBenefitJoinH.PAYING_PERSON_ID, p.getPersonId())
							.eqJoinedField(HrBenefitJoinH.PAYING_PERSON, HrBenefitJoinH.COVERED_PERSON)
							.ne(HrBenefitJoinH.COVERAGE_END_DATE, 0)
							.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D')
							.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
							.joinTo(HrBenefitConfig.HR_BENEFIT)
							.eq(HrBenefit.BENEFIT_PROVIDER, v)
							.list();

					if(historyJoins.size() == 0)
					{
						System.out.println("No history joins found for: " + fname + " " + lname + " (" + carrierName + ")" );
						continue;
					}

					for(HrBenefitJoinH historyJoin : historyJoins)
					{
						BHRBenefit b = new BHRBenefit(new BHRBenefitConfig(historyJoin.getHrBenefitConfigId()).getBenefitId());
						historyJoin.setRecordChangeDate(new Date());

						Calendar changeDate = DateUtils.getCalendar(DateUtils.getDate(statusDate));
						if(changeDate.get(Calendar.MONTH) > Calendar.getInstance().get(Calendar.MONTH))
							changeDate.add(Calendar.YEAR, -1);

						historyJoin.setCoverageEndDate(DateUtils.getDate(changeDate));
						historyJoin.setPolicyEndDate(DateUtils.getDate(changeDate));

						hsu.update(historyJoin);
						hsu.flush();
						System.out.println("Updated " + fname + " " + lname + " " + b.getName() + " to date: " + DateUtils.getDateFormatted(DateUtils.getDate(changeDate)) + " (" + historyJoin.getHistory_id() + ")");
						query += "update hr_benefit_join_h set policy_end_date = " +
								year + month + day + "," +
								"coverage_end_date = " +
								year + month + day + "," +
								"record_change_date = '" +
								"2011" + "-" + "03" + "-" + "17" + " " + "01:00:00.000' " +
								"where history_id = '" + historyJoin.getHistory_id() + "';\n";
					}
				}

			}
			System.out.println(query);
		} catch (IOException ex) {
            Logger.getLogger(DRCCobraTermFixImport.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DRCCobraTermFixImport.class.getName()).log(Level.SEVERE, null, ex);

        }
		hsu.commitTransaction();
	}

	public static void main(String[] args) {
		ArahantSession.getHSU().setCurrentCompany(new BCompany("00001-0000072555").getBean());
		ArahantSession.getHSU().setCurrentPersonToArahant();
		new DRCNewPassagesHistoryRecordsFix().importRecords("/Users/arahant/Desktop/New Passages Discrepancies.csv");
	}
}
