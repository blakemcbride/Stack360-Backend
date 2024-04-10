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

import com.arahant.beans.AIProperty;
import com.arahant.beans.IHrBenefitJoin;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.business.BEmployee;
import com.arahant.business.BProperty;
import com.arahant.utils.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.JessException;
import jess.Rete;

/**
 *
 */
public class FlexExport  {
	private static final ArahantLogger logger=new ArahantLogger(FlexExport.class);
	private FileWriter fw;
	HibernateSessionUtil hsu=ArahantSession.getHSU();
	
	
	private HashSet<String> addedList=new HashSet<String>();
	
	public String export(Date lastDate) throws Exception
	{
		loadEngineWithOrgHierarchies();
		String filename= new BProperty("LargeReportDir").getValue()+File.separator +"flexport_"+DateUtils.now()+"_"+DateUtils.nowTime()+".txt";
	//	String filename="flexport_20081221.txt";
		fw=new FileWriter(filename);
		
		Calendar endOfYear=DateUtils.getNow();
		endOfYear.set(Calendar.MONTH, Calendar.DECEMBER);
		endOfYear.set(Calendar.DAY_OF_MONTH,31);

		endYear=DateUtils.getDate(endOfYear);
		
		   
		//get all benefit flex joins since the last time
		
		HibernateCriteriaUtil<HrBenefitJoin> hcu=hsu.createCriteria(HrBenefitJoin.class)
			.ge(HrBenefitJoin.HISTORY_DATE, lastDate)
			.eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
			.orderBy(HrBenefitJoin.PAYING_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);
		
		
		HashSet<String> doneIds=new HashSet<String>();
		
		HibernateScrollUtil<HrBenefitJoin> scr=hcu.scroll();
		
		int count=0;
		
		while (scr.next())
		{
			
			if (++count%50==0)
				logger.info(count);
			
			HrBenefitJoin bj=scr.get();
			
			
			if (bj.getCoverageStartDate()==0)
				continue;

			
			
		//	int covEnd=DateUtils.add(bj.getCoverageEndDate(), 1);
		//	int polEnd=DateUtils.add(bj.getPolicyEndDate(), 1);
			
			//if the coverage endYear date is endYear of year date, then skip it
			if (bj.getCoverageEndDate()==endYear)
				continue;
			if (bj.getPolicyEndDate()==endYear)
				continue;
			
			//if this is for a prior year, skip it
			if (DateUtils.year(bj.getCoverageStartDate())<DateUtils.year(DateUtils.now()))
				continue;
			if (bj.getCoverageEndDate()!=0 && DateUtils.year(bj.getCoverageEndDate())<DateUtils.year(DateUtils.now()))
				continue;
			
			
			String actionType="AD"; //AD/CH/TE
			
			// if the change type is M and there is not a New in history since last date,
			// the change type is ch
			if (bj.getRecordChangeType()=='M')
			{
				if (!hsu.createCriteria(HrBenefitJoinH.class)
					.eq(HrBenefitJoinH.BENEFIT_JOIN_ID, bj.getBenefitJoinId())
					.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE,'N')
					.ge(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDate)
					.exists())
					actionType="CH";
					
					
			}
			
			
			//do they already have a medical?
			if (addedList.contains(bj.getPayingPersonId()) || hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.joinTo(HrBenefit.BENEFIT_CATEGORY)
				.eq(HrBenefitCategory.TYPE, HrBenefitCategory.HEALTH)
				.exists())
				writeREFIND(actionType,bj);	
			else
				writeADDIND(actionType, bj);
					
			
			addedList.add(bj.getPayingPersonId());
			
			writeRecord(actionType,bj);
			
			doneIds.add(bj.getBenefitJoinId());
			
		}
		
		scr.close();
		
		
		HibernateCriteriaUtil<HrBenefitJoinH> hcu2=hsu.createCriteria(HrBenefitJoinH.class)
			.gt(HrBenefitJoinH.HISTORY_DATE, lastDate)
			.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D')
			.notIn(HrBenefitJoinH.BENEFIT_JOIN_ID, doneIds)
			.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.FLEX_TYPE);
	
			
		HibernateScrollUtil<HrBenefitJoinH> scr2=hcu2.scroll();
		
		while (scr2.next())
		{
			if (++count%50==0)
				logger.info(count);
			
			HrBenefitJoinH bj=scr2.get();
			
			if (bj.getCoverageStartDate()==0)
				continue;
			
			if (bj.getCoverageEndDate()==0)
				continue;
			
			
			int covEnd=DateUtils.add(bj.getCoverageEndDate(), 1);
			int polEnd=DateUtils.add(bj.getPolicyEndDate(), 1);
			
			//if the coverage endYear date is endYear of year date, then skip it
			if ((DateUtils.month(covEnd)==1 && DateUtils.day(covEnd)==1))
				continue;
			if ((DateUtils.month(polEnd)==1 && DateUtils.day(polEnd)==1))
				continue;
			
			//if this is for a prior year, skip it
			if (DateUtils.year(bj.getCoverageStartDate())<DateUtils.year(DateUtils.now()))
				continue;
			if (DateUtils.year(bj.getCoverageEndDate())<DateUtils.year(DateUtils.now()))
				continue;
			
			
			
			String actionType="TE"; //AD/CH/TE
			
			writeREFIND(actionType,bj);
			
			writeRecord(actionType,bj);
			
			doneIds.add(bj.getBenefitJoinId());
			
		}
		
		scr2.close();
		fw.flush();
		fw.close();
		
		return filename;
	}

	protected void writeREFIND(String actionType, IHrBenefitJoin bj) throws IOException {


		//write a refind record
		write("RF", 2);
		write("IND", 3);
		write(bj.getCoverageStartDate() + "", 8); //action date
		String benefitId = "00001-0000000019";

		//does this person have a copay
		if (hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y').eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId()).dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, bj.getCoverageStartDate()).joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, "00001-0000000023").exists()) {
			benefitId = "00001-0000000023";
		}
		else
		{
			//if this is a term, did they have copay?
			if (actionType.equals("TE"))
				if (hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, bj.getPayingPersonId()).dateInside(HrBenefitJoinH.POLICY_START_DATE, HrBenefitJoinH.POLICY_END_DATE, bj.getCoverageStartDate()).joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, "00001-0000000023").exists()) 
					benefitId = "00001-0000000023";
		}
	
		AIProperty groupProp = new AIProperty(r, "InsuranceGroupIds", benefitId, bj.getPayingPersonId(), 99999999);
		String groupNumber = groupProp.getValue();
		String subGroupNumber = groupProp.getValue2();
  

		write(groupNumber, 7);

		write("", 4); //CERT number
	
		write("", 1);


		write("", 15);
		write("", 15);
		write("", 8); //effective date
		write("", 1);
		write("", 1);
		write("", 2);
		write("", 8); //term date
		write("", 8); //last trans date
		write("", 10); //phonetic code
		write("", 30); //message
		write(bj.getPayingPerson().getUnencryptedSsn(), 11);
		write("", 1); //variant
		write(subGroupNumber, 3);
		write("", 8); //first bill month
		write("", 23); //filler
		write("", 8);
		write("", 8); //spouse bday
		write("", 8); //employment date
		write("", 1224 - 194); //rest of record is spaces
		fw.write("\r\n");
	}
	
	protected void writeADDIND(String actionType, IHrBenefitJoin bj) throws IOException {


		//write a refind record
		write("AD", 2);
		write("IND", 3);
		write(bj.getCoverageStartDate() + "", 8); //action date
		String benefitId = "00001-0000000019";


		AIProperty groupProp = new AIProperty(r, "InsuranceGroupIds", benefitId, bj.getPayingPersonId(), 99999999);
		String groupNumber = groupProp.getValue();
		String subGroupNumber = groupProp.getValue2();
  

		write(groupNumber, 7);

		write("", 4); //CERT number
	
		write("A", 1);


		write(bj.getPayingPerson().getLname(), 15);
		write(bj.getPayingPerson().getFname(), 15);
		write(bj.getCoverageStartDate()+"", 8); //effective date
		write(bj.getPayingPerson().getSex()+"", 1);
		write("N", 1);
		write("", 2);
		write("", 8); //term date
		write("", 8); //last trans date
		write("", 10); //phonetic code
		write("", 30); //message
		write(bj.getPayingPerson().getUnencryptedSsn(), 11);
		write("", 1); //variant
		write(subGroupNumber, 3);
		write("", 8); //first bill month
		write("", 23); //filler
		write(bj.getPayingPerson().getDob()+"", 8);
		write("", 8); //spouse bday
		BEmployee bemp=new BEmployee(bj.getPayingPersonId());
		write(bemp.getEmploymentDate()+"", 8); //employment date
		write("", 3878 - 194); //rest of record is spaces
		
		write(bemp.getStreet(),30);
		write(bemp.getStreet2(),30);
		write(bemp.getCity(),15);
		write(bemp.getState(),2);
		write(bemp.getZip(),10);
		
		write ("",1224-465);
		
		fw.write("\r\n");
	}
	


	int endYear;
	
	private void writeRecord (String actionType, IHrBenefitJoin bj) throws IOException
	{
			write(actionType,2);
	
			write("SIB",3);

			if (actionType.equals("TE"))
				write(DateUtils.add(bj.getCoverageEndDate(), 1)+"",8);//action date
			else
				write(bj.getCoverageStartDate()+"",8);//action date
			
			String benefitId="00001-0000000019";
			
			//does this person have a copay
			if (hsu.createCriteria(HrBenefitJoin.class)
				.eq(HrBenefitJoin.BENEFIT_APPROVED, 'Y')
				.eq(HrBenefitJoin.PAYING_PERSON_ID, bj.getPayingPersonId())	
				.dateInside(HrBenefitJoin.POLICY_START_DATE, HrBenefitJoin.POLICY_END_DATE, bj.getCoverageStartDate())
				.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
				.joinTo(HrBenefitConfig.HR_BENEFIT)
				.eq(HrBenefit.BENEFITID,"00001-0000000023")
				.exists())
				benefitId="00001-0000000023";
			else
			{
				//if this is a term, did they have copay?
				if (actionType.equals("TE"))
					if (hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.PAYING_PERSON_ID, bj.getPayingPersonId()).dateInside(HrBenefitJoinH.POLICY_START_DATE, HrBenefitJoinH.POLICY_END_DATE, bj.getCoverageStartDate()).joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT).eq(HrBenefit.BENEFITID, "00001-0000000023").exists()) 
						benefitId = "00001-0000000023";
			}
				
					
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",benefitId,bj.getPayingPersonId(), 99999999);
			String groupNumber=groupProp.getValue();
			//String subGroupNumber=groupProp.getValue2();
			
			write(groupNumber,7); // case number
			
			
			/*
			 *HS77023
ADOP = Bi weekly (26 pay periods)
DAYB = Bi weekly (26 pay periods)
DY20 = 20 pay periods
MEDR = Bi weekly ( 26 pay periods)
MR20 = 20 pay periods
PREM = Bi weekly (26 pay periods)
PRMS = 20 pay periods
 
WC77770
ADOP = Bi weekly (26 pay periods)
DAYB = Bi weekly (26 pay periods)
DY20 = 20 pay periods
MEDR = Bi weekly (26 pay periods)
MR20 = 20 pay periods
PREM = Bi weekly (26 pay periods
			 * */

			if (groupNumber==null)
				groupNumber="WC77770";
			
			String flexBenefitId=bj.getHrBenefitConfig().getHrBenefit().getBenefitId();
			String benefitCode=bj.getHrBenefitConfig().getHrBenefit().getInsuranceCode();
	//		if (groupNumber.equals("HS77023")||groupNumber.equals("WC77770"))
			{	
				if (flexBenefitId.equals("00001-0000000046"))
					benefitCode="ADOP";
				
				if (AIProperty.getBoolean("CheckIsCounty", bj.getPayingPersonId()))
				{		
					if (flexBenefitId.equals("00001-0000000045"))
						benefitCode="DAYB";
					if (flexBenefitId.equals("00001-0000000044"))
						benefitCode="MEDR";
					if (flexBenefitId.equals("00001-0000000052"))
						benefitCode="PREM";
				}
				else
				{
					if (flexBenefitId.equals("00001-0000000045"))
						benefitCode="DY20";
					if (flexBenefitId.equals("00001-0000000044"))
						benefitCode="MR20";
					if (flexBenefitId.equals("00001-0000000052"))
						benefitCode="PRMS";
				}
			}
			/*  TODO: update for these
			ADOP  26 pay cycles
			DAYB  26 pay cycles
			DAYC  24 pay cycles
			DY20  20 pay cycles
			DY22  22 pay cycles
			MEDR  26 pay cycles
			MR24  24 pay cycles
			MR22  22 pay cycles
			MR20  20 pay cycles
			PREM  26 pay cycles
			PRMS  20 pay cycles
					*/
			write(benefitCode,4); //benefit code
			
			int covEnd=DateUtils.add(bj.getCoverageEndDate(), 1);
			
			if (bj.getCoverageEndDate()==0 || (DateUtils.month(covEnd)==1 && DateUtils.day(covEnd)==1) || actionType.equals("AD"))
				write("",8);
			else
				write(covEnd+"",8);//term date

			if (!actionType.equals("TE"))
			{
			
				write("",11);//TIN
				write("",3); //sub tin

				write(MoneyUtils.parseMoney(bj.getCalculatedCost())+"",12); //contribution

				write("",12);//Payment amount

				write("",12);//YTD contribution

				write("",12);//YTD payment

				write(bj.getAmountCovered()+"",12);//Annual election

				write("",12);//deductible type

			}
			fw.write("\r\n");
		
	}
	


	private void write(String str, int len) throws IOException
	{
		if (str==null)
			str="";
		if (str.length()>len)
			str=str.substring(len);
		
		while (str.length()<len)
			str+=" ";
		
		fw.write(str);
	}
	
	private Rete r;
	
	private void loadEngineWithOrgHierarchies()
	{
		try {		
			
			JessBean jb=new JessBean();
			r=jb.getAIEngine();
			
			ArahantSession.loadMinimumRules(r);
			
			jb.loadTableFacts("org_group_hierarchy");
			jb.loadTableFacts("org_group_association");
			jb.loadTableFacts("hr_empl_status_history"," where status_hist_id not in (select h1.status_hist_id " +
					" from hr_empl_status_history h1, hr_empl_status_history h2 where h1.employee_id=h2.employee_id " +
					" and h1.effective_date < h2.effective_date)");
			jb.loadTableFacts("hr_employee_status");

			//r.watchAll();
			jb.getAIEngine().batch("WmCoEDIRules.jess");
		} catch (JessException ex) {
			JessUtils.reportError(ex, logger);
		}
	}
	
	public static void main(String args[])
	{
		try {
			ArahantSession.getHSU().setCurrentPersonToArahant();
			FlexExport f = new FlexExport();
			f.export(DateUtils.getDate(20090105));
		} catch (Exception ex) {
			Logger.getLogger(FlexExport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
}
