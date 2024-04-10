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
import com.arahant.beans.CompanyBase;
import com.arahant.beans.HrBenefit;
import com.arahant.beans.HrBenefitCategory;
import com.arahant.beans.HrBenefitConfig;
import com.arahant.beans.HrBenefitJoin;
import com.arahant.beans.HrBenefitJoinH;
import com.arahant.business.BHREmplDependent;
import com.arahant.business.BPerson;
import com.arahant.business.BProperty;
import com.arahant.utils.*;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.JessException;
import jess.Rete;
import org.kissweb.DelimitedFileWriter;

/**
 *
 */
public class AuditMedicalBenefits {
	private static final ArahantLogger logger=new ArahantLogger(AuditMedicalBenefits.class);
	/*
	 * First Name
Last Name
Date of Birth
SSN of Employee.  Mike asked if the SSN could be filled in for all dependents with the SSN of the employee? This will help him. 
Dependent SSN
Date of coverage
Termination of coverage (maybe supply us with this information for the last year?)
Type of coverage (medical  family, single, etc, dental, family, single, etc)
Division number
Address line 1
Address line 2
City
State
Zip
 */
	
	private Rete r=new Rete();

	public String doAuditHealthCurrent() throws Exception
	{
		loadEngineWithOrgHierarchies();
				
		String filename= new BProperty("LargeReportDir").getValue()+File.separator +"WmCoHealthCurrent_"+DateUtils.now()+".csv";
		
		DelimitedFileWriter fw=new DelimitedFileWriter(filename);
		
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		
		int count=0;
		
		
		HibernateScrollUtil<HrBenefitJoin> scr=hsu.createCriteria(HrBenefitJoin.class)
			.ne(HrBenefitJoin.COVERAGE_START_DATE, 0)
			.orderBy(HrBenefitJoin.PAYING_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.ne(HrBenefit.BENEFITID, "00001-0000000026") //TODO: wmco specific exclusions
			.ne(HrBenefit.BENEFITID, "00001-0000000042")
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE,HrBenefitCategory.HEALTH)
			
			.scroll();
		
		fw.writeField("fname");
		fw.writeField("lname");
		fw.writeField("payerSSN");
		fw.writeField("coveredSSN");
		fw.writeField("coveredStart");
		fw.writeField("coveredEnd");
		fw.writeField("benefitName");
		fw.writeField("groupNumber");
		fw.writeField("subGroup");
		fw.writeField("street");
		fw.writeField("street2");
		fw.writeField("city");
		fw.writeField("state");
		fw.writeField("zip");
		fw.writeField("policyStart");
		fw.writeField("policyEnd");
		fw.writeField("COBRA");
		//Relationship to EE, gender, date of birth, PCP Name if on the Copay plan
		fw.writeField("relationship");
		fw.writeField("gender");
		fw.writeField("dob");
		fw.writeField("PCP Name");
		fw.writeField("Effective Start");
		fw.writeField("Effective End");
		fw.endRecord();
		
		
		while (scr.next())
		{
			if (++count%50==0)
				logger.info(count);
			
			HrBenefitJoin bj=scr.get();
			fw.writeField(bj.getCoveredPerson().getFname());
			fw.writeField(bj.getCoveredPerson().getLname());
			fw.writeField(bj.getPayingPerson().getUnencryptedSsn());
			fw.writeField(bj.getCoveredPerson().getUnencryptedSsn());
			fw.writeDate(bj.getCoverageStartDate());
			fw.writeDate(bj.getCoverageEndDate());
			fw.writeField(bj.getHrBenefitConfig().getName());
	//		fw.writeField(x);
			
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",bj.getHrBenefitConfig().getHrBenefit().getBenefitId(),bj.getPayingPersonId(),99999999);
			String groupNumber=groupProp.getValue();
			String subGroupNumber=groupProp.getValue2();
			
			fw.writeField(groupNumber);
			fw.writeField(subGroupNumber);
			
			BPerson p=new BPerson(bj.getPayingPerson());
			fw.writeField(p.getStreet());
			fw.writeField(p.getStreet2());
			fw.writeField(p.getCity());
			fw.writeField(p.getState());
			fw.writeField(p.getZip());
			
			
			fw.writeDate(bj.getPolicyStartDate());
			fw.writeDate(bj.getPolicyEndDate());
			fw.writeField(bj.getUsingCOBRA()+"");
			
			//Relationship to EE, gender, date of birth, PCP Name if on the Copay plan
			if (bj.getPayingPerson().getPersonId().equals(bj.getCoveredPerson().getPersonId()))
				fw.writeField("self");
			else
			{
				//get relationship
				BHREmplDependent dep=new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());
				fw.writeField(dep.getTextRelationship());
			}
			
			BPerson covered=new BPerson(bj.getCoveredPerson());
			fw.writeField(covered.getSex());
			fw.writeDate(covered.getDob());
			fw.writeField(getPCP(bj));
			
			if (bj.getPolicyStartDate()>bj.getCoverageStartDate())
				fw.writeDate(bj.getPolicyStartDate());
			else
				fw.writeDate(bj.getCoverageStartDate());
			
			if (bj.getPolicyEndDate()<bj.getCoverageEndDate() && bj.getPolicyEndDate()!=0)
				fw.writeDate(bj.getPolicyEndDate());
			else
				if (bj.getCoverageEndDate()!=0)
					fw.writeDate(bj.getCoverageEndDate());
				else
					fw.writeDate(bj.getPolicyEndDate());
			
			fw.endRecord();
		}
		
		scr.close();
		
		hsu.rollbackTransaction();
		hsu.beginTransaction();
		
		fw.close();
		
		encryptAndSend(filename);
		return filename;
	}
	
	private String getPCP(HrBenefitJoin bj)
	{
		if (bj.getComments().equals("Marriage"))
			return "";
		if (bj.getComments().equals("Birth"))
			return "";
		if (bj.getComments().equals("New Born"))
			return "";
		if (bj.getComments().indexOf("spousal")!=-1)
			return "";
		if (bj.getComments().indexOf("Spousal")!=-1)
			return "";
		if (bj.getComments().indexOf("coverage")!=-1)
			return "";
		if (bj.getComments().indexOf("prophet")!=-1)
			return "";
		if (bj.getComments().indexOf("student")!=-1)
			return "";
		if (bj.getComments().indexOf("disclaimer")!=-1)
			return "";
		if (bj.getComments().indexOf("boe ee eff")!=-1)
			return "";
		if (bj.getComments().indexOf("benefits")!=-1)
			return "";
		
		return bj.getComments();
	}

	public String doAuditHealthHistory() throws Exception
	{
		String filename="HealthHistoryAudit.csv";
		
		DelimitedFileWriter fw=new DelimitedFileWriter(filename);
		
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		
		int count=0;
		fw.writeField("fname");
		fw.writeField("lname");
		fw.writeField("payerSSN");
		fw.writeField("coveredSSN");
		fw.writeField("coveredStart");
		fw.writeField("coveredEnd");
		fw.writeField("benefitName");
		fw.writeField("groupNumber");
		fw.writeField("subGroup");
		fw.writeField("street");
		fw.writeField("street2");
		fw.writeField("city");
		fw.writeField("state");
		fw.writeField("zip");
		fw.writeField("policyStart");
		fw.writeField("policyEnd");
		fw.writeField("COBRA");

		fw.endRecord();
	
		HibernateScrollUtil<HrBenefitJoinH> hscr=hsu.createCriteria(HrBenefitJoinH.class)
			.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D')
			.ge(HrBenefitJoinH.HISTORY_DATE, DateUtils.getDate(20080101))
			.ne(HrBenefitJoinH.POLICY_END_DATE, 0)
			.orderBy(HrBenefitJoinH.PAYING_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.ne(HrBenefit.BENEFITID, "00001-0000000026")
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE,HrBenefitCategory.HEALTH)
			.scroll();
		
		while (hscr.next())  {
			if (++count%50==0)
				logger.info(count);
			HrBenefitJoinH bj=hscr.get();
			if (bj.getCoveredPerson()==null)
				continue;
			fw.writeField(bj.getCoveredPerson().getFname());
			fw.writeField(bj.getCoveredPerson().getLname());
			fw.writeField(bj.getPayingPerson().getUnencryptedSsn());
			fw.writeField(bj.getCoveredPerson().getUnencryptedSsn());
			fw.writeDate(bj.getCoverageStartDate());
			fw.writeDate(bj.getCoverageEndDate());
			fw.writeField(bj.getHrBenefitConfig().getName());
	//		fw.writeField(x);
			
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",bj.getHrBenefitConfig().getHrBenefit().getBenefitId(),bj.getPayingPersonId(), 99999999);
			String groupNumber=groupProp.getValue();
			String subGroupNumber=groupProp.getValue2();
			
			fw.writeField(groupNumber);
			fw.writeField(subGroupNumber);
			
			BPerson p=new BPerson(bj.getPayingPerson());
			fw.writeField(p.getStreet());
			fw.writeField(p.getStreet2());
			fw.writeField(p.getCity());
			fw.writeField(p.getState());
			fw.writeField(p.getZip());
			
			fw.writeDate(bj.getPolicyStartDate());
			fw.writeDate(bj.getPolicyEndDate());
			fw.writeField(bj.getUsingCOBRA()+"");
			
			fw.endRecord();
		}
			
		hscr.close();
		
		hsu.rollbackTransaction();
		hsu.beginTransaction();
		
		fw.close();
		
		encryptAndSend(filename);
		return filename;
	}
	
	
	public String doAuditDentalCobra() throws Exception
	{
		String filename="DentalCobraCurrentAudit.csv";
		
		DelimitedFileWriter fw=new DelimitedFileWriter(filename);
		
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		
		int count = 0;
		
		fw.writeField("fname");
		fw.writeField("lname");
		fw.writeField("payerSSN");
		fw.writeField("coveredSSN");
		fw.writeField("coveredStart");
		fw.writeField("coveredEnd");
		fw.writeField("benefitName");
		fw.writeField("groupNumber");
		fw.writeField("subGroup");
		fw.writeField("street");
		fw.writeField("street2");
		fw.writeField("city");
		fw.writeField("state");
		fw.writeField("zip");
		fw.writeField("policyStart");
		fw.writeField("policyEnd");
		fw.writeField("COBRA");
		fw.endRecord();
		
		HibernateScrollUtil<HrBenefitJoin> dscr=hsu.createCriteria(HrBenefitJoin.class)
			.eq(HrBenefitJoin.USING_COBRA,'Y')
			.orderBy(HrBenefitJoin.PAYING_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE,HrBenefitCategory.DENTAL)
			.scroll();
		
		while (dscr.next())  {
			if (++count%50 == 0)
				logger.info(count);
			HrBenefitJoin bj=dscr.get();
			fw.writeField(bj.getCoveredPerson().getFname());
			fw.writeField(bj.getCoveredPerson().getLname());
			fw.writeField(bj.getPayingPerson().getUnencryptedSsn());
			fw.writeField(bj.getCoveredPerson().getUnencryptedSsn());
			fw.writeDate(bj.getCoverageStartDate());
			fw.writeDate(bj.getCoverageEndDate());
			fw.writeField(bj.getHrBenefitConfig().getName());
	//		fw.writeField(x);
			
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",bj.getHrBenefitConfig().getHrBenefit().getBenefitId(),bj.getPayingPersonId(), 99999999);
			String groupNumber=groupProp.getValue();
			String subGroupNumber=groupProp.getValue2();
			
			fw.writeField(groupNumber);
			fw.writeField(subGroupNumber);
			
			BPerson p=new BPerson(bj.getPayingPerson());
			fw.writeField(p.getStreet());
			fw.writeField(p.getStreet2());
			fw.writeField(p.getCity());
			fw.writeField(p.getState());
			fw.writeField(p.getZip());
			
			fw.writeDate(bj.getPolicyStartDate());
			fw.writeDate(bj.getPolicyEndDate());
			fw.writeField(bj.getUsingCOBRA()+"");
			
			fw.endRecord();
		}
		
		dscr.close();
		
		hsu.rollbackTransaction();
		hsu.beginTransaction();
		
		fw.close();
		
		encryptAndSend(filename);
		return filename;
	}
	
	public String doAuditDentalHistory() throws Exception
	{
		String filename="DentalCobraHistoryAudit.csv";
		
		DelimitedFileWriter fw=new DelimitedFileWriter(filename);
		
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();
		
		int count=0;
		
		fw.writeField("fname");
		fw.writeField("lname");
		fw.writeField("payerSSN");
		fw.writeField("coveredSSN");
		fw.writeField("coveredStart");
		fw.writeField("coveredEnd");
		fw.writeField("benefitName");
		fw.writeField("groupNumber");
		fw.writeField("subGroup");
		fw.writeField("street");
		fw.writeField("street2");
		fw.writeField("city");
		fw.writeField("state");
		fw.writeField("zip");
		fw.writeField("policyStart");
		fw.writeField("policyEnd");
		fw.writeField("COBRA");
		fw.endRecord();
		
		HibernateScrollUtil<HrBenefitJoinH> hdscr=hsu.createCriteria(HrBenefitJoinH.class)
			.eq(HrBenefitJoin.USING_COBRA,'Y')
			.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D')
			.ge(HrBenefitJoinH.HISTORY_DATE, DateUtils.getDate(20080101))
			.ne(HrBenefitJoin.POLICY_END_DATE, 0)
			.orderBy(HrBenefitJoinH.PAYING_PERSON_ID)
			.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.joinTo(HrBenefitConfig.HR_BENEFIT)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE,HrBenefitCategory.DENTAL)
			.scroll();
		
		while (hdscr.next())
		{
			if (++count%50==0)
				logger.info(count);
			HrBenefitJoinH bj=hdscr.get();
			if (bj.getCoveredPerson()==null)
				continue;
			fw.writeField(bj.getCoveredPerson().getFname());
			fw.writeField(bj.getCoveredPerson().getLname());
			fw.writeField(bj.getPayingPerson().getUnencryptedSsn());
			fw.writeField(bj.getCoveredPerson().getUnencryptedSsn());
			fw.writeDate(bj.getCoverageStartDate());
			fw.writeDate(bj.getCoverageEndDate());
			fw.writeField(bj.getHrBenefitConfig().getName());
	//		fw.writeField(x);
			
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",bj.getHrBenefitConfig().getHrBenefit().getBenefitId(),bj.getPayingPersonId(), 99999999);
			String groupNumber=groupProp.getValue();
			String subGroupNumber=groupProp.getValue2();
			
			fw.writeField(groupNumber);
			fw.writeField(subGroupNumber);
			
			BPerson p=new BPerson(bj.getPayingPerson());
			fw.writeField(p.getStreet());
			fw.writeField(p.getStreet2());
			fw.writeField(p.getCity());
			fw.writeField(p.getState());
			fw.writeField(p.getZip());
			
			fw.writeDate(bj.getPolicyStartDate());
			fw.writeDate(bj.getPolicyEndDate());
			fw.writeField(bj.getUsingCOBRA()+"");
			
			fw.endRecord();
		}
			
		hdscr.close();
		
		hsu.rollbackTransaction();
		hsu.beginTransaction();
		
		fw.close();
		
		encryptAndSend(filename);
		return filename;
	}
	
	
	public void encryptAndSend(String filename) throws Exception
	{
		if (!BProperty.getBoolean("EDIProduction"))
				return;
		
		CompanyBase co=ArahantSession.getHSU().get(CompanyBase.class,"00001-0000000211");

		//encrypt
		File decryptedFile=new File(filename);
		File encryptedFile=new File(filename+".pgp");

		Crypto.encryptPGP(decryptedFile, encryptedFile, co.getPublicEncryptionKey(), co.getEncryptionKeyId());

		//ftp
		new FTP().send(co.getComUrl(), co.getComPassword(), co.getComDirectory(), encryptedFile, true);
	}
	
	private void loadEngineWithOrgHierarchies()
	{
		try {
			
			ArahantSession.loadMinimumRules(r);
			ArahantSession.setAI(r);
			
			JessBean jb = new JessBean();
			
			jb.loadTableFacts("org_group_hierarchy");
			jb.loadTableFacts("org_group_association");
			jb.loadTableFacts("hr_empl_status_history"," where status_hist_id not in (select h1.status_hist_id " +
					" from hr_empl_status_history h1, hr_empl_status_history h2 where h1.employee_id=h2.employee_id " +
					" and h1.effective_date < h2.effective_date)");
			jb.loadTableFacts("hr_employee_status");


			jb.getAIEngine().batch("WmCoEDIRules.jess");
		} catch (JessException ex) {
			JessUtils.reportError(ex, logger);
		}
	}
	
	public static void main(String args[])
	{
		try {
			ArahantSession.getHSU().setCurrentPersonToArahant();
			AuditMedicalBenefits x=new AuditMedicalBenefits();
			x.doAuditHealthCurrent();
		//	x.doAuditHealthHistory();
		//	x.doAuditDentalCobra();
		//	x.doAuditDentalHistory();
			
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
		
}
