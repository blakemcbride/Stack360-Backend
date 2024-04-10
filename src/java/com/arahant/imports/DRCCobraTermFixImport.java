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
import com.arahant.business.BCompany;
import com.arahant.business.BHRBenefit;
import com.arahant.business.BHRBenefitChangeReason;
import com.arahant.business.BHRBenefitConfig;
import com.arahant.utils.ArahantSession;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Arahant
 */
public class DRCCobraTermFixImport {
	HibernateSessionUtil hsu = ArahantSession.getHSU();

	public void importRecords(String filename)
	{
		BHRBenefitChangeReason volTerm = new BHRBenefitChangeReason("00001-0000000271");
		BHRBenefitChangeReason involTerm = new BHRBenefitChangeReason("00001-0000000272");
		List<HrBenefit> cobraBenefits = hsu.createCriteria(HrBenefit.class)
											.eq(HrBenefit.COVERED_UNDER_COBRA, 'Y')
											.list();

		try{
			DelimitedFileReader fr = new DelimitedFileReader(filename);
			fr.nextLine();
			int count = 0;
			while (fr.nextLine()) {
				String lname = fr.nextString();
				String fname = fr.nextString();
				String statusName = fr.nextString();
				String statusDate = fr.nextString();
				String reason = fr.nextString();
				
				if(!statusName.equalsIgnoreCase("Terminated"))
				{
					continue;
				}
				if(++count % 25 == 0)
				{
					hsu.commitTransaction();
					hsu.beginTransaction();
				}

				Person p = hsu.createCriteria(Person.class)
						.eq(Person.FNAME, fname).eq(Person.LNAME, lname)
						.eq(Person.RECORD_TYPE, 'R')
						.first();
				if(p == null)
				{
					System.out.println("No person record found for: " + fname + " " + lname);
					continue;
				}
				else
				{
					List<HrBenefitJoinH> historyJoins = hsu.createCriteria(HrBenefitJoinH.class)
							.eq(HrBenefitJoinH.PAYING_PERSON_ID, p.getPersonId())
							.ne(HrBenefitJoinH.COVERAGE_END_DATE, 0)
							.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
							.in(HrBenefitConfig.HR_BENEFIT, cobraBenefits)
							.list();

					if(historyJoins.size() == 0)
					{
						System.out.println("No history joins found for: " + fname + " " + lname );
						continue;
					}

					for(HrBenefitJoinH historyJoin : historyJoins)
					{
						if(reason.equalsIgnoreCase("Voluntary Termination"))
						{
							historyJoin.setBenefitChangeReason(volTerm.getBean());
							historyJoin.setChangeDescription(volTerm.getDescription());
						}
						else if(reason.equalsIgnoreCase("Involuntary Termination"))
						{
							historyJoin.setBenefitChangeReason(involTerm.getBean());
							historyJoin.setChangeDescription(involTerm.getDescription());
						}
						else
						{
							System.out.println("History for " + fname + " " + lname + " doesn't match change reason: " + reason + " (" + historyJoin.getHistory_id() + ")");
							continue;
						}
						BHRBenefit b = new BHRBenefit(new BHRBenefitConfig(historyJoin.getHrBenefitConfigId()).getBenefitId());
						historyJoin.setRecordChangeDate(new Date());
						hsu.update(historyJoin);
						hsu.flush();
						System.out.println("Updated " + fname + " " + lname + " " + b.getName() + " to reason: " + reason + " (" + historyJoin.getHistory_id() + ")");
					}
				}

			}
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
		new DRCCobraTermFixImport().importRecords("/Users/arahant/Desktop/New Passages CobraGuard.csv");
	}
}
