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

import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.Person;
import com.arahant.business.BHRBenefitJoin;
import com.arahant.business.BMessage;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateSessionUtil;
import com.arahant.utils.MoneyUtils;

/**
 *
 */
public class PricingUpdateImport {
	private static final ArahantLogger logger=new ArahantLogger(PricingUpdateImport.class);
	public void updatePricing(String configId, String changeDataStr, String file, String ssnColumnStr, String priceColumnStr)
	{
		String message="";
		try {
			int changeDate = Integer.parseInt(changeDataStr);
			int ssnColumn = Integer.parseInt(ssnColumnStr)-1;
			int priceColumn = Integer.parseInt(priceColumnStr)-1;
					
			DelimitedFileReader dfr = new DelimitedFileReader(file);
		//	dfr.nextLine();
			
			int missingCount=0;
			while (dfr.nextLine()) {

				String ssn = dfr.getString(ssnColumn);
				String priceStr = dfr.getString(priceColumn);

				if (ssn==null || ssn.equals(""))
					continue;
				
				// calculate price
				float price=MoneyUtils.parseMoney(priceStr);


				
				HibernateSessionUtil hsu = ArahantSession.getHSU();
				
				//if (hsu.getCurrentCompany().getName().startsWith("McIntosh")) //todo temporary thing
				//	price*=24;

					Person p=hsu.createCriteriaNoCompanyFilter(Person.class)
						.eq(Person.SSN, ssn)
						.first();

				if (p==null)
				{
					missingCount++;
					System.out.println("SSN "+ssn+" not found");
					message+="SSN "+ssn+" not found\n";
					continue;
				}
				//System.out.println(p.getNameLFM()+" "+ssn);


				HrBenefitJoin bj=hsu.createCriteria(HrBenefitJoin.class)
					.geOrEq(HrBenefitJoin.POLICY_END_DATE, changeDate, 0)
					.eq(HrBenefitJoin.HR_BENEFIT_CONFIG_ID, configId)
					.joinTo(HrBenefitJoin.PAYING_PERSON)
					.eq(Person.SSN, ssn)
					.first();

				HrBenefitConfig conf=hsu.get(HrBenefitConfig.class, configId);

				conf.getHrBenefit().setEmployeeIsProvider('Y');
				hsu.saveOrUpdate(conf);
				
				if (bj==null)
				{
					//logger.info("Benefit not found for "+ssn);
					
					//continue;
					//add it
					
					if (p==null)
						continue;

					//System.out.println(p.getNameLFM()+"<-"+conf.getName());
					BHRBenefitJoin bbj=new BHRBenefitJoin();
					bbj.create();
					bbj.setPayingPerson(p);
					bbj.setCoveredPerson(p);
					bbj.setAmountPaid(price);
					bbj.setPolicyStartDate(changeDate);
					bbj.setCoverageStartDate(changeDate);
					bbj.setChangeDescription("Pricing upload");
					bbj.setAmountPaidType("F");
					bbj.setUsingCOBRA(false);
					bbj.setHrBenefitConfig(conf);
					bbj.setBenefitApproved(true);
					bbj.setUseAmountOverride(true);
					bbj.insert();
//for some reason didn't do it on first try
					bbj.setAmountPaid(price);

					bbj.setChangeDescription("Pricing upload");
					bbj.setAmountPaidType("F");
					bbj.setUseAmountOverride(true);

					bbj.update();

				}
				else
				{
				//logger.info(ssn+" "+price);
				
				//move everything over to a new one and set term on old one
				//	System.out.println(bj.getPayingPerson().getNameLFM()+"<="+bj.getHrBenefitConfig().getName());

					BHRBenefitJoin bbj=new BHRBenefitJoin(bj);

					bbj.setAmountPaid(price);

					bbj.setChangeDescription("Pricing upload");
					bbj.setAmountPaidType("F");
					bbj.setUseAmountOverride(true);
				
					bbj.update(true);

					
				}
	 
			}
			logger.info(missingCount+" missing.");
			message+="PRICE IMPORT DONE!\n";
			BMessage.send(ArahantSession.getCurrentPerson(), ArahantSession.getCurrentPerson(), "Price import done", message);
		} catch (Exception e) {
			ArahantSession.getHSU().rollbackTransaction();
			ArahantSession.getHSU().beginTransaction();
			BMessage.send(ArahantSession.getCurrentPerson(), ArahantSession.getCurrentPerson(), "Price import failed - please check server log", message);
			ArahantSession.getHSU().commitTransaction();
			throw new ArahantException(e);
		}
	}
	
	public static void main (String args[])
	{
		PricingUpdateImport imp=new PricingUpdateImport();
		ArahantSession.getHSU().beginTransaction();
		ArahantSession.getHSU().setCurrentPersonToArahant();
		
		imp.updatePricing("00001-0000000041", "20090101", "/Users/ImportFiles/LTD2009County.csv", "3", "12");
		imp.updatePricing("00001-0000000035", "20090101", "/Users/ImportFiles/SupplementalLifeRateChange.csv", "3", "12");
		imp.updatePricing("00001-0000000035", "20090101", "/Users/ImportFiles/SupplementalLifeRateChangeBOE.csv", "3", "12");
		imp.updatePricing("00001-0000000041", "20090101", "/Users/ImportFiles/LTD2009BOE.csv", "3", "12");
		ArahantSession.getHSU().commitTransaction();
		
	}

}
