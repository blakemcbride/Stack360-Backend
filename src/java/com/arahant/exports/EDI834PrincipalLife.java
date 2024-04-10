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


package com.arahant.exports;


import com.arahant.beans.*;
import com.arahant.business.*;
import com.arahant.beans.CompanyBase;

import com.arahant.business.BEDITransaction;
import com.arahant.edi.EDI;
import com.arahant.exceptions.ArahantException;
import com.arahant.utils.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jess.JessException;
import jess.Rete;
import org.kissweb.StringUtils;


public class EDI834PrincipalLife
{
	private static final ArahantLogger logger=new ArahantLogger(EDI834PrincipalLife.class);
	private Rete r=new Rete();
	private class EDIDumpPersonalInfo implements Comparable<EDIDumpPersonalInfo>
	{
		private EDI edi;
		private String coverageEndDate;
		private String frequencyCode="U";
		private String raceOrEthnicity="7";
		private int memberBirthDate;
		private String memberAddressZip;
		private String memberAddressState;
		private String memberAddressLine1;
		private String workPhone;
		private String homePhone;
		private String memberMiddleName;
		private String memberFirstName;
		private String memberLastName;
		private String memberAddressCity;
		private String maritalStatusCode;
		private String coverageStartDate;
		private String citizenStatusCode="1";
		private String entityIdentifierCode="I";
		private String insuranceLineCode;
		private String coverageLevelCode;
		private String genderCode;
		private String memberAddressLine2;
		private String statusInformationEffectiveDate;
		private String subscriberIdentifier;
		private String dateTimeQualifier;
		private String handicap;
		private String studentStatus;
		private String relationshipCode;
		private String benefitStatusCode;
		private String employmentCode;
		private int employmentStartDate;
		private int employmentEndDate;
		private String depSSN;
		private String maintenanceTypeCode="030";
		private int benefitType;
		private String benefitName;
		private String personId;
		private String primaryCarePhysician;
		private String changeReason;
		private int policyStart;


		public EDIDumpPersonalInfo(EDI edi)
		{
			this.edi=edi;
		}

		public void setCoveredPerson(Person coveredPerson)
		{
			personId=coveredPerson.getPersonId();
			memberLastName=coveredPerson.getLname();
			memberFirstName=coveredPerson.getFname();
			memberMiddleName=coveredPerson.getMname();
			depSSN=coveredPerson.getUnencryptedSsn();
			if ("999-99-9999".equals(depSSN))
				depSSN="000-00-0000";
			memberBirthDate=coveredPerson.getDob();
			studentStatus=(coveredPerson.getStudent()=='Y')?"F":"N";
			handicap=coveredPerson.getHandicap()+"";
			genderCode=coveredPerson.getSex()+"";
		}

		public void setPayingPerson(Person payingPerson, String benefitId)
		{
			subscriberIdentifier=payingPerson.getUnencryptedSsn();
		//	r.watchAll();
			if ("999-99-9999".equals(subscriberIdentifier))
				subscriberIdentifier="000-00-0000";
			int efd=99999999;
			if (policyStart>efd)
				efd=policyStart;
			

		}

		@Override
		public String toString()
		{
			return memberLastName+", "+memberFirstName+"\n";
		}

		private boolean isEmpty(String s)
		{
			return s==null || s.trim().equals("");
		}

		private void setAddress(BPerson p, BPerson dep) {

			if (!isEmpty(dep.getHomePhone()))
				homePhone=dep.getHomePhone();
			else
				homePhone=p.getHomePhone();

			if (!isEmpty(dep.getWorkPhoneNumber()))
				homePhone=dep.getWorkPhoneNumber();
			else
				homePhone=p.getWorkPhoneNumber();


			if (!isEmpty(dep.getStreet()))
			{
				memberAddressLine1=dep.getStreet();
				memberAddressLine2=dep.getStreet2();

				memberAddressCity=dep.getCity();
				memberAddressState=dep.getState();
				memberAddressZip=dep.getZip();
			}
			else
			{
				memberAddressLine1=p.getStreet();
				memberAddressLine2=p.getStreet2();

				memberAddressCity=p.getCity();
				memberAddressState=p.getState();
				memberAddressZip=p.getZip();

			}


		}

		private void setBenefitJoinData(IHrBenefitJoin bj, int benefitType) {
			setLocation(bj);

			setRelationshipCode(bj);

			benefitStatusCode=(bj.getUsingCOBRA()=='Y')?"C":"A";
			this.benefitType=benefitType;

			setPCP(bj);

			BPerson p =new BPerson(bj.getPayingPersonId());

		//	if (p.getLastName().equalsIgnoreCase("KNOTTS"))
		//		logger.info("At knotts");

			BEmployee emp=null;
			if (p.isEmployee())
				emp=new BEmployee(bj.getPayingPersonId());


			setEmploymentCode(bj, emp);

			HibernateSessionUtil hsu=ArahantSession.getHSU();


			setPayingPerson(hsu.get(Person.class,bj.getPayingPersonId()),hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId()).getHrBenefit().getBenefitId());

			setCoveredPerson(bj.getCoveredPerson());


			dateTimeQualifier="300";

			if (bj.getUsingCOBRA()=='Y')
				dateTimeQualifier="340";

			statusInformationEffectiveDate=DateUtils.getDateCCYYMMDD(bj.getPolicyStartDate());


			BPerson d=new BPerson(bj.getCoveredPersonId());

			setAddress(p,d);

			setMaritalStatus(emp);

			HrBenefitConfig hbc=ArahantSession.getHSU().get(HrBenefitConfig.class, bj.getHrBenefitConfigId());

			insuranceLineCode=hbc.getHrBenefit().getInsuranceCode();

			setCoverageLevel(bj);

			int covdate=bj.getCoverageStartDate();

			coverageStartDate=DateUtils.getDateCCYYMMDD(covdate);
			policyStart=bj.getPolicyStartDate();

			if (coverageStartDate.equals("")&&bj.getCoverageEndDate()==0)
				logger.info("coverage start date missing");

			int covend=bj.getCoverageEndDate();
			if (covend!=0)
				covend=DateUtils.addDays(covend, 1);

			coverageEndDate=DateUtils.getDateCCYYMMDD(covend);

			changeReason=bj.getChangeDescription();

		}

		private void setCoverageLevel(IHrBenefitJoin bj) {
				coverageLevelCode="EMP";

			if (bj.getHrBenefitConfig().getSpouseEmployee()=='Y' ||
				bj.getHrBenefitConfig().getSpouseNonEmployee()=='Y')
				coverageLevelCode="ESP";

			if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren()=='Y') ||
				(bj.getHrBenefitConfig().getSpouseEmpOrChildren()=='Y') ||
				(bj.getHrBenefitConfig().getChildren()=='Y') )
				coverageLevelCode="FAM";

			if (bj.getHrBenefitConfig().getMaxChildren()==1)
				coverageLevelCode="ESP";

			if (bj.getHrBenefitConfig().getChildren()=='Y' && bj.getHrBenefitConfig().getMaxChildren()==1 &&
					bj.getHrBenefitConfig().getSpouseEmployee()=='Y')
				coverageLevelCode="FAM";

		}

		private void setEmploymentCode(IHrBenefitJoin bj, BEmployee emp) {
			employmentCode="TE";

			if (emp!=null)
			{
				BHREmplStatusHistory x=emp.getLastStatusHistory();
				if (x.getStatusId().equals("00001-0000000001")) //TODO: move to AI engine property
					employmentCode="FT";
				if (x.getStatusId().equals("00001-0000000000")) //TODO: move to AI engine property
					employmentCode="TE";
				if (x.getStatusId().equals("00001-0000000003")) //TODO: move to AI engine property
					employmentCode="L1";
				if (x.getStatusId().equals("00001-0000000002")) //TODO: move to AI engine property
					employmentCode="RT";
				if (x.getStatusId().equals("00001-0000000004")) //TODO: move to AI engine property
					employmentCode="PT";

				employmentStartDate=emp.getEmploymentDate();
			}
			if (employmentCode.equals("TE"))
			{
				try
				{
					employmentStartDate=emp.getLastActiveStatusHistory().getEffectiveDate();
				}
				catch (Exception e)
				{
					try{employmentStartDate=emp.getEmploymentDate();}catch (Exception x){}
				}
				try
				{
					employmentEndDate=emp.getLastStatusDate();
				}
				catch (Exception e)
				{

				}
			}
		}

		private void setLocation(IHrBenefitJoin bj) {

			if (bj.getUsingCOBRA()=='Y')
			{
				//if this is a dependent, fake the location
				if (bj.getPayingPerson().getOrgGroupAssociations().size()==0)
				{
					HrEmplDependent dep=ArahantSession.getHSU().createCriteria(HrEmplDependent.class)
						.eq(HrEmplDependent.PERSON, bj.getPayingPerson())
						.first();
					if (dep!=null)
					{
						String ogid=dep.getEmployee().getOrgGroupAssociations().iterator().next().getOrgGroupId();
						ArahantSession.AICmd("(assert (org_group_association (person_id \""+bj.getPayingPersonId()+"\")(org_group_id \""+ogid+"\")))");

					}
				}
			}

		}

		private void setMaritalStatus(BEmployee emp) {
			maritalStatusCode="I";

			if (emp!=null)
			{
				if (emp.getActiveSpouse(DateUtils.now())!=null)
					maritalStatusCode="M";
				else
					if (emp.getActiveSpouse(0)!=null)
						maritalStatusCode="U";
			}
		}

		private void setPCP(IHrBenefitJoin bj) {
			if (bj.getComments()==null || "".equals(bj.getComments().trim()))
				primaryCarePhysician=null;
			else
				primaryCarePhysician=bj.getComments().trim();
		}

		private void setRelationshipCode(IHrBenefitJoin bj) {
			relationshipCode="18";
			if (!bj.getPayingPersonId().equals(bj.getCoveredPersonId()))
			{
				try
				{
					BHREmplDependent dep=new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());

					switch (dep.getRelationship())
					{
						case 'S' : relationshipCode="01";
								break;
						case 'C' : relationshipCode="19";
								break;
						default :
								//TODO: not a legal relationship - we need them to fix the others
							//	logger.info("Skipping relationship "+dep.getRelationship());
							relationshipCode="19";
							//	continue;
					}
				}
				catch (Exception e)
				{
					//relationship was not set up correctly - use 19
					relationshipCode="19";
				}
			}

		}


		private String stripDashes(String s)
		{
			String ret="";

			if (s==null)
				return "";

			StringTokenizer toks=new StringTokenizer(s, "-");
			while (toks.hasMoreTokens())
				ret+=toks.nextToken();

			return ret;
		}
		public void REF_SSN_subscriber()
		{
			edi.SEG("REF");
			edi.SF(1, "0F");//Subscriber Number Qualifier
			edi.SF(2, stripDashes(subscriberIdentifier));
			edi.EOS();
		}
		public void REF_subscriber_group_number()
		{
			edi.SEG("REF");
			edi.SF(1, "1L");
			edi.SF(2, "H60317");
			edi.EOS();
		}
		public void REF_subscriber_subgroup_number()
		{
			//Principal Life says don't do this
			/*
			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))||benefitType==HrBenefitCategory.DENTAL)
			{
				edi.SEG("REF");
				edi.SF(1, "17");
				edi.SF(2, subGroupNumber);
				edi.EOS();
			}
			 * */
		}
		public void REF_accounting_type()
		{
			edi.SEG("REF");
			edi.SF(1, "ZZ");
			edi.SF(2, "1");
			edi.EOS();
		}

		public void DTP_employment_date()
		{
			if (employmentStartDate!=0)
			{
				edi.SEG("DTP");
				edi.SF(1, "336");
				edi.ST (2, "D8");
				edi.DC(3, employmentStartDate);
				edi.EOS();
			}
		}

		public void DTP_employment_term_date()
		{
			if (employmentEndDate!=0)
			{
				edi.SEG("DTP");
				edi.SF(1, "357");
				edi.ST (2, "D8");
				edi.DC(3, employmentEndDate);
				edi.EOS();
			}
		}
		public void NM1_member_name()
		{
			edi.SEG("NM1");
			edi.SF(1, "IL");
			edi.SF(2, "1");
			edi.SF(3, memberLastName);
			edi.SF(4, memberFirstName);
			edi.SF(5, memberMiddleName);
			edi.SF(6, "");
			edi.SF(7, "");

			if (!"000000000".equals(stripDashes(depSSN)))
			{
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			}
			else
			{
				edi.SF(8, "");
				edi.SF(9, "");
			}

			edi.EOS();
		}

		public void N3_address_information()
		{
			//shift address if messed up
			if (memberAddressLine1==null || memberAddressLine1.equals(""))
			{
				memberAddressLine1=memberAddressLine2;
				memberAddressLine2="";
			}

			if (memberAddressLine1.length() > 24)
			{
				String temp = memberAddressLine1.trim() + " " + memberAddressLine2.trim();

				StringTokenizer st = new StringTokenizer(temp, " ");
				memberAddressLine1 = "";
				memberAddressLine2 = "";

				String token = "";
				while (st.hasMoreTokens())
				{
					token = st.nextToken();
					
					if ((memberAddressLine1.length() + token.length()) < 24)
						memberAddressLine1 += token;
					else
						break;
				}

				while (st.hasMoreTokens())
				{
					memberAddressLine2 += st.nextToken();
				}
			}

			edi.SEG("N3");
			edi.SF(1, memberAddressLine1);
			if (memberAddressLine2!=null && !memberAddressLine2.equals(""))
				edi.SF(2, memberAddressLine2);
			edi.EOS();
		}

		public void N4_geographic_location()
		{
			edi.SEG("N4");
			edi.SF(1, memberAddressCity);
			edi.SF(2, memberAddressState);
			edi.SF(3, stripDashes(memberAddressZip).trim());
			edi.EOS();
		}

		public void DMG_demographic_info()
		{

			edi.SEG("DMG");
			if (memberBirthDate!=0)
				edi.SF(1, "D8");
			else
				edi.SF(1, "");
			edi.DC(2, memberBirthDate);
			edi.SF(3, genderCode);
			edi.EOS();
		}
		public void PCP_provider()
		{
			/*
			if (primaryCarePhysician!=null && benefitType!=HrBenefitCategory.DENTAL && !maintenanceTypeCode.equals("024") )
			{
				edi.SEG("LX");
				edi.SF(1, "1");
				edi.EOS();
				edi.SEG("NM1");
				edi.SF(1, "P3");
				edi.SF(2, "2");
				edi.SF(3, primaryCarePhysician);
				edi.SF(4, "");
				edi.SF(5, "");
				edi.SF(6, "");
				edi.SF(7, "");
				edi.SF(8, "");
				edi.SF(9, "");
				edi.SF(10, "72");
				edi.SF(11, "");
				edi.EOS();
			}
			*/
		}
		public void HD_health_coverage()
		{
			edi.SEG("HD");
			//has there been a change?

			//"021" addition, "024" cancel or term, "030" no change
			//String changeCode="030";


			edi.SF(1, "030"); //Principal Life just wants a 030

			edi.SF(2, "XN");


			/*
			 *AG - Preventive Care/Wellness
AH - 24 Hour Care
AJ - Medicare Risk
AK - Mental Health
DCP - Dental Capitation
DEN - Dental
EPO - Exclusive Provider Organization
FAC - Facility
HE - Hearing
HLT - Health
HMO - Health Maintenance Organization
LTC - Long-Term Care
LTD - Long-Term Disability
MM - Major Medical
MOD - Mail Order Drug
PDG - Prescription Drug
POS - Point of Service
PPO - Preferred Provider Organization
PRA - Practitioners
STD - Short-Term Disability
UR - Utilization Review
VIS - Vision

*/
			edi.SF(3, "VIS");
			edi.SF(4, "001");

				edi.SF(5, coverageLevelCode);
			edi.EOS();
		}

		public void DTP_benefit_start_date()
		{

			edi.SEG("DTP");
			edi.SF(1, "348");
			edi.ST(2, "D8");
			if ("".equals(coverageStartDate))
				logger.info("Coverage start date missing");

			edi.SF(3, coverageStartDate);
			edi.EOS();
		}

		public void DTP_sign_elect_date()
		{
/*
			edi.SEG("DTP");
			edi.SF(1, "300");
			edi.ST(2, "D8");
			if (0==policyStart)
				logger.info("Policy start date missing");

			edi.SF(3, DateUtils.getDateCCYYMMDD(policyStart));
			edi.EOS();
*/
		}

		public void ICM_member_income()
		{
			edi.SEG("ICM");
			edi.SF(1,"7");
			edi.SF(2, "0.00");
			edi.SF(3, "40");
			edi.EOS();
		}

		public void DTP_maintenance_effective_date()
		{
/*
			edi.SEG("DTP");
			edi.SF(1, "303");
			edi.ST(2, "D8");
			if (0==policyStart)
				logger.info("Policy start date missing");

			edi.SF(3, DateUtils.getDateCCYYMMDD(policyStart));
			edi.EOS();
*/
		}


		final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		public void DTP_benefit_end_date()
		{
			if (!"".equals(coverageEndDate)&&maintenanceTypeCode.equals("024"))
			{
				edi.SEG("DTP");
				edi.SF(1, "349");
				edi.ST(2, "D8");
				try {
					edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.endOfMonth(DateUtils.getDate(sdf.parse(coverageEndDate)))));
				} catch (ParseException ex) {
					Logger.getLogger(EDI834PrincipalLife.class.getName()).log(Level.SEVERE, null, ex);
				}
				edi.EOS();
			}

		}
		public void INS_insured_benefit()
		{
			edi.SEG("INS");

			edi.ST(1, stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))?"Y":"N");
			edi.ST(2, relationshipCode);

			edi.ST(3, "030"); //for principal life

			edi.ST(4, "XN"); //means not giving reason for change
			

			//edi.ST(4, "");
			edi.ST(5, benefitStatusCode);
			edi.ST(6, "");
			if (benefitStatusCode.equals("C"))
			{
				String code="1";
				if (changeReason.trim().startsWith("Dependent Ineligible"))
					code="7";
				if (changeReason.trim().equalsIgnoreCase("Divorce"))
					code="5";
				if (changeReason.trim().equalsIgnoreCase("Part Time"))
					code="2";
				if (changeReason.trim().startsWith("Terming"))
					code="1";
				if (changeReason.trim().equalsIgnoreCase("Death"))
					code="4";
				edi.ST(7, code);
			}
			else
				edi.ST(7, "");
				edi.ST(8, employmentCode);
			edi.ST(9, (studentStatus==null)?"N":studentStatus);
			edi.ST(10, (handicap==null)?"N":handicap);
			edi.EOS();
		}

		public void REF_subscriber_carrier_id()
		{

			edi.SEG("REF");

			edi.ST(1, "DX");
			edi.ST(2, "00001");

			edi.EOS();
		}

		public void ediMemberLevelDetail()
		{
			//logger.info("Ed det 1");
			INS_insured_benefit();
			REF_SSN_subscriber();
			REF_subscriber_group_number();
			REF_subscriber_subgroup_number();
			REF_subscriber_carrier_id();//member carrier id
			REF_accounting_type();

			//logger.info("Ed det 2");


			//eligibility end date
			DTP_employment_date();
			DTP_employment_term_date();
	//Sign/elect date
			DTP_sign_elect_date();
	//Maintenance Date
			DTP_maintenance_effective_date();
			NM1_member_name();
			N3_address_information();
			N4_geographic_location();
			DMG_demographic_info();
			ICM_member_income();

			//logger.info("Ed det 3");
			//Incorrect member name
			HD_health_coverage();
			DTP_benefit_start_date(); //benefit begin
			DTP_benefit_end_date();//benefit end

		//TODO: pcp turned off until they can handle it	PCP_provider(); //primary care provider

		}

		@Override
		public int compareTo(EDIDumpPersonalInfo o) {

			if (relationshipCode.equals("18")) //payer
				return -1;

			if (o.relationshipCode.equals("18"))
				return 1;

			if (relationshipCode.equals("01")) //spouse
				return -1;

			if (o.relationshipCode.equals("01"))
				return 1;

			return 0; //19 - child

		}
	}

	EDI edi;

	private void loadEngineWithOrgHierarchies()
	{
		try {

			ArahantSession.loadMinimumRules(r);
			ArahantSession.setAI(r);

			JessBean jb=new JessBean();

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

	private Date lastDumpDate;

	public String [] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug)
	{
		List <String> filenames=new LinkedList<String>();
		HibernateSessionUtil hsu=ArahantSession.getHSU();
		hsu.dontAIIntegrate();

		int icn=tran.getStartingICN();
		int gcn=tran.getStartingGCN();
		int transactionSetNumber=tran.getStartingTscn();


		lastDumpDate=tran.getLastExportDate();


//		lastDumpDate=DateUtils.getDate(20081130);

		loadEngineWithOrgHierarchies();

		long mils=new Date().getTime();


		AIProperty p=new AIProperty("SponsorId", vendor.getOrgGroupId());
		String sponsorId=p.getValue();


		List <HrBenefit> benefitsToDo=hsu.createCriteria(HrBenefit.class)
			.eq(HrBenefit.BENEFIT_PROVIDER, vendor)
			.joinTo(HrBenefit.BENEFIT_CATEGORY)
			.eq(HrBenefitCategory.TYPE, HrBenefitCategory.VISION)
			.list();

		for (HrBenefit bene : benefitsToDo)
		{
			String filename="";
			try {
				filename=new BProperty("LargeReportDir").getValue()+File.separator+"ANSIH60317" + (debug?"TEST":"PROD") +DateUtils.now()/*+".txt"*/;
				edi = new EDI(filename);
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
			gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			//header(transactionSetNumber, vendor.getName()/*bene.getPlanName()*/, bene.getPayerId(), bene.getPlanId(), sponsorId, gcn);
			header(transactionSetNumber, vendor.getName(), vendor.getInterchangeSenderId(), bene.getPlanId(), vendor.getInterchangeReceiverId(), gcn);

			doInactives(hsu, bene); //deal with history records - policies that have ended
			doActives(hsu,bene);


			transactionSetTrailer(transactionSetNumber);
			transactionSetNumber++;
			functionalGroupTrailer(EDI.makecn(gcn));
			functionInterchangeTrailer(EDI.makecn(icn));
			edi.close();
			gcn++;
			filenames.add(filename);
		}


		tran.setTransactionSetNumber(transactionSetNumber-1);
		tran.setGCN(gcn-1);
		tran.setICN(icn);

		mils=new Date().getTime()-mils;

		logger.info("took "+(mils/1000)+" seconds");

		return filenames.toArray(new String[filenames.size()]);
	}

	private void doInactives(HibernateSessionUtil hsu, HrBenefit bene)
	{
		String benefitName=bene.getName();

		int count=0;

		HibernateCriteriaUtil <HrBenefitJoinH>hcu= hsu.createCriteria(HrBenefitJoinH.class);
	//	hcu.notIn(HrBenefitJoinH.COVERED_PERSON_ID, payingPersons);
		hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
		hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
		hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT,bene);
		hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

		int benefitType=hsu.createCriteria(HrBenefitCategory.class)
				.selectFields(HrBenefitCategory.TYPE)
				.joinTo(HrBenefitCategory.HRBENEFIT)
				.eq(HrBenefit.BENEFITID,bene.getBenefitId())
				.intValue();

		HibernateScrollUtil <HrBenefitJoinH>hscr=hcu.scroll();

		List<EDIDumpPersonalInfo> batch=new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson="";


		while (hscr.next())
		{


			if (++count%50==0)
			{
				logger.info(count);
				//break;
			}

			HrBenefitJoinH bj=hscr.get();

			if (!bj.getPayingPersonId().equals(currentPayingPerson))
			{
				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();

				batch.clear();
				currentPayingPerson=bj.getPayingPersonId();
			}

			EDIDumpPersonalInfo info=new EDIDumpPersonalInfo(edi);

			info.benefitName=benefitName;

			if (bj.getCoverageEndDate()==0)
				bj.setCoverageEndDate(bj.getPolicyEndDate());

			try
			{
				info.setBenefitJoinData(bj, benefitType);
			}
			catch (Throwable t)
			{
				//Since this is a history record, it's possible that needed data could have been deleted
				//if so, have to skip the record
				continue;
			}

			info.maintenanceTypeCode="024";


			batch.add(info);

		}

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();
	}

	List<String> payingPersons=new LinkedList<String>();

	private List<String> doActives(HibernateSessionUtil hsu, HrBenefit bene)
	{

		int count=0;
		String benefitName=bene.getName();

		HibernateCriteriaUtil <HrBenefitJoin>hcu= hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.APPROVED, 'Y').eq(HrBenefitJoin.BENEFIT_DECLINED, 'N');
		hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
		hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG)
			.eq(HrBenefitConfig.HR_BENEFIT, bene);
		hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);


	//PETERSON TEST	hcu.eq(HrBenefitJoin.PAYING_PERSON_ID, "00001-0000016362");

		///TEST CODE
		//hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		//END TEST CODE

		int benefitType=hsu.createCriteria(HrBenefitCategory.class)
				.selectFields(HrBenefitCategory.TYPE)
				.joinTo(HrBenefitCategory.HRBENEFIT)
				.eq(HrBenefit.BENEFITID,bene.getBenefitId())
				.intValue();

		HibernateScrollUtil <HrBenefitJoin>hscr=hcu.scroll();


		List<EDIDumpPersonalInfo> batch=new LinkedList<EDIDumpPersonalInfo>();

		String currentPayingPerson="";

		while (hscr.next())
		{

			if (++count%50==0)
			{
				logger.info(count);
				//break;
			}


			//get relationship code
			HrBenefitJoin bj=hscr.get();

			if (!bj.getPayingPersonId().equals(currentPayingPerson))
			{
				Collections.sort(batch);
				for (EDIDumpPersonalInfo i : batch)
					i.ediMemberLevelDetail();

				batch.clear();
				currentPayingPerson=bj.getPayingPersonId();
			}

			EDIDumpPersonalInfo info=new EDIDumpPersonalInfo(edi);

			info.benefitName=benefitName;

			info.setBenefitJoinData(bj, benefitType);

			int covdate=bj.getCoverageStartDate();

			int covend=bj.getCoverageEndDate();
			if (covend!=0)
				covend=DateUtils.addDays(covend, 1);

			if (covdate!=0 && bj.getRecordChangeDate().after(lastDumpDate))
				info.maintenanceTypeCode="021";

			if (covend!=0 && bj.getRecordChangeDate().after(lastDumpDate))
				info.maintenanceTypeCode="024";

			if (bj.getPolicyEndDate()!=0 && bj.getRecordChangeDate().after(lastDumpDate))
				info.maintenanceTypeCode="024";

			if (covend!=0 && bj.getRecordChangeDate().before(lastDumpDate))  //already sent this one
				continue;

			if (bj.getPolicyEndDate()!=0 && bj.getRecordChangeDate().before(lastDumpDate))  //already sent this one
				continue;

		//	logger.info(info);

			//info.ediMemberLevelDetail();

			batch.add(info);

			payingPersons.add(bj.getPayingPersonId()+bj.getCoveredPersonId());

		}

		Collections.sort(batch);
		for (EDIDumpPersonalInfo i : batch)
			i.ediMemberLevelDetail();

		hscr.close();

		return payingPersons;
	}

	private void isa_seg(String icn, boolean test, String account, String interchangeReceiverId)
	{
		edi.SEG("ISA");
		edi.ST(1, "00");
		edi.SW(2, "", 10);
		edi.ST(3, "00");
		edi.SW(4, "", 10);
		edi.ST(5, "ZZ");
		edi.SW(6, account, 15);
		edi.ST(7, "ZZ");
		edi.SW(8, interchangeReceiverId, 15);
		edi.CD(9);
		edi.CT(10);
		edi.CH(11, 'U');
		edi.ST(12, "00401");
		edi.ST(13, icn);
		edi.CH(14, '0');
		edi.CH(15, test ? 'T' : 'P');

		String delim=AIProperty.getValue("Delim"+interchangeReceiverId);

	//	if (delim==null || "".equals(delim))
	//		edi.NV(16); //need the delimiter there
	//	else
			edi.ST(16,":");

		edi.EOS();
	}

	private void gs_seg(String gcn, String account, String applicationRecieverId)
	{
		edi.SEG("GS");
		edi.ST(1, "BE");
		edi.ST(2, StringUtils.rightStrip(account));
		edi.ST(3, applicationRecieverId);
		edi.D8(4);
		edi.CT(5);
		edi.ST(6, gcn);
		edi.CH(7, 'X');
		edi.ST(8, "004010X095A1");
		edi.EOS();
	}

	private void transactionSetTrailer(int transactionSetNumber)
	{
		edi.SEG("SE");
		edi.NL(1, edi.X12_numb_segments);
		edi.ST(2, EDI.makecn(transactionSetNumber));
		edi.EOS();
	}

	private void functionalGroupTrailer(String transactionSetNumber)
	{
		edi.SEG("GE");
		edi.NL(1, edi.X12_numb_st_segments);
		edi.ST(2, transactionSetNumber);
		edi.EOS();
	}

	private  void functionInterchangeTrailer(String icn)
	{
		edi.SEG("IEA");
		edi.NL(1, 1);
		edi.ST(2, icn);
		edi.EOS();
	}

	private void header(int transactionSetNumber, String payer, String payerId, String sponsorName, String sponsorId, int gcn)
	{
		edi.SEG("ST");
		edi.ST(1, "834");
		edi.ST(2, EDI.makecn(transactionSetNumber));
		edi.EOS();

		edi.SEG("BGN");
		edi.ST(1, "00");
		edi.ST(2, String.format("%06d", gcn));//reference number
		edi.D8(3);
		edi.CT(4);
		edi.ST(5, "");
		edi.ST(6, "");
		edi.ST(7, "");
		edi.ST(8, "4");
		edi.EOS();

		//Sponsor
		edi.SEG("N1");
		edi.ST(1, "P5");
		edi.ST(2, sponsorName);//Plan name
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, sponsorId);
		edi.EOS();

		//Payer
		edi.SEG("N1");
		edi.ST(1, "IN");
		edi.ST(2, payer);
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, payerId);
		edi.EOS();

		/* May not need this
		//Broker
		edi.SEG("N1");
		edi.ST(1, "IN"); //BO - broker, TV - third party admin
		edi.ST(2, "BROKER NAME"); //TODO: get broker name
		edi.ST(3, "F1");//Identifier code  F1= fed id, ZZ= mutually defined, 94 org code
		edi.ST(4, "0000000000"); //TODO: organization id
		edi.EOS();
		*/

	}

	public static void main(String args[])
	{

		logger.info(new Date().getTime()/1000);
		ArahantSession.getHSU().setCurrentPersonToArahant();



			final BEDITransaction x=new BEDITransaction();
			x.create();

			x.setReceiver("00001-0000000644");  //org group id for principal life
			x.setStatus(0);
			x.setStatusDesc("Started");

			x.insert();

			
			ArahantSession.getHSU().beginTransaction();


			x.sendExport();
	
			ArahantSession.getHSU().commitTransaction();

	}
}	
