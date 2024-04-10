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
import com.arahant.beans.Person;
import com.arahant.utils.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class AuditFlexExport {
	private static final ArahantLogger logger=new ArahantLogger(AuditFlexExport.class);
	
	public static void main(String args[])
	{

		HashSet <String> sent=new HashSet<String>();
		BufferedReader fr = null;
		try {
			ArahantSession.getHSU().setCurrentPersonToArahant();
			fr = new BufferedReader(new FileReader("Audit.txt"));

			String line = fr.readLine();
			while (line != null) {
				//get ssn
				String ssn=line.substring(123, 135);
			//	logger.info(ssn);
				
				try {
					sent.add(Crypto.encryptTripleDES(Person.encKey(), ssn.trim()));
				} catch (Exception e) {
					logger.error("Error encrypting a SSN", e);
				}
				
				fr.readLine();
				line = fr.readLine();
			}
			
			HibernateSessionUtil hsu=ArahantSession.getHSU();
			
			
			HibernateScrollUtil<Person> scr=hsu.createCriteria(Person.class)
				.notIn(Person.SSN, sent)
//				.orderBy(Person.SSN)
				.joinTo(Person.HR_BENEFIT_JOINS_WHERE_PAYING)
				.ge(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(20081130))
				.ge(HrBenefitJoin.POLICY_START_DATE, 20090101)
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE)
				.scroll();
				
			int count=0;
			while (scr.next())
			{
				count++;
				logger.info(scr.get().getUnencryptedSsn());
				
		/*		
				logger.info(hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.PAYING_PERSON, scr.get())
				.ge(HrBenefitJoin.HISTORY_DATE, DateUtils.getDate(20081130))
				.ge(HrBenefitJoin.POLICY_START_DATE, 20090101)
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE)
				.first().getRecordChangeDate());
			*/		
			}
			logger.info(count);
			scr.close();
		} catch (Exception ex) {
			Logger.getLogger(AuditFlexExport.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				fr.close();
			} catch (IOException ex) {
				Logger.getLogger(AuditFlexExport.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

}
