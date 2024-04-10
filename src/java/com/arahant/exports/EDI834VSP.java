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
import com.arahant.utils.ArahantLogger;
import com.arahant.utils.ArahantSession;
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import org.kissweb.StringUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


public class EDI834VSP
{
	public EDI834VSP(String fileType)
	{
		this.fileType = fileType;
	}

	private String fileType = "F";
	private static final ArahantLogger logger=new ArahantLogger(EDI834VSP.class);
    EDILog ediLog = new EDILog("VSP_EDI");
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
		private String email;
		private String medicareCode;

		private String subGroupNumber;

		public EDIDumpPersonalInfo(EDI edi)
		{
			this.edi=edi;
		}
		public void setCoveredPerson(Person coveredPerson)
		{
			personId=coveredPerson.getPersonId();
			memberLastName=coveredPerson.getLname();
			memberFirstName=coveredPerson.getFname();
			memberMiddleName=StringUtils.isEmpty(coveredPerson.getMname())?"": coveredPerson.getMname().charAt(0)+"";
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
			BHRBenefit bb = new BHRBenefit(benefitId);
			subGroupNumber = bb.getSubGroupId();
			subscriberIdentifier=payingPerson.getUnencryptedSsn();
		//	r.watchAll();
			if ("999-99-9999".equals(subscriberIdentifier))
				subscriberIdentifier="000-00-0000";
			int efd=99999999;
			if (policyStart>efd)
				efd=policyStart;
		}

		public void setEmail(Person coveredPerson)
		{
			email=coveredPerson.getPersonalEmail();
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
		
		public String removeChar(String s)
		{
			return s.replace("#", "");
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
				memberAddressLine2=removeChar(dep.getStreet2());

				memberAddressCity=dep.getCity();
				memberAddressState=dep.getState();
				memberAddressZip=dep.getZip();
			}
			else
			{
				memberAddressLine1=p.getStreet();
				memberAddressLine2=removeChar(p.getStreet2());

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

			BEmployee emp=null;
			if (p.isEmployee())
				emp=new BEmployee(bj.getPayingPersonId());


			setEmploymentCode(bj, emp);

			HibernateSessionUtil hsu=ArahantSession.getHSU();


			setPayingPerson(hsu.get(Person.class,bj.getPayingPersonId()),hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId()).getHrBenefit().getBenefitId());

			setCoveredPerson(hsu.get(Person.class,bj.getCoveredPersonId()));

			setEmail(hsu.get(Person.class,bj.getCoveredPersonId()));

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

			coverageEndDate=DateUtils.getDateCCYYMMDD(covend);

			changeReason=bj.getChangeDescription();
		}

		private void setCoverageLevel(IHrBenefitJoin bj) {
				coverageLevelCode="IND";

			if (bj.getHrBenefitConfig().getSpouseEmployee()=='Y' ||
				bj.getHrBenefitConfig().getSpouseNonEmployee()=='Y')
				coverageLevelCode="ESP";

			if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren()=='Y') ||
				(bj.getHrBenefitConfig().getSpouseEmpOrChildren()=='Y'))
				coverageLevelCode="FAM";

			if (bj.getHrBenefitConfig().getChildren()=='Y')
				coverageLevelCode="ECH";
				
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
				if (x.getStatusName().contains("Full")) //TODO: move to AI engine property
					employmentCode="FT";
				if (x.getStatusName().contains("Part")) //TODO: move to AI engine property
					employmentCode="PT";
//				if (x.getStatusName().contains("Inactive")) //TODO: move to AI engine property
//					employmentCode="L1";
				if (x.getStatusName().contains("HR Administrator")) //TODO: move to AI engine property
					employmentCode="FT";

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

		public void REF_subscriber_number()
		{

			edi.SEG("REF");

			edi.ST(1, "0F");
			edi.ST(2, stripDashes(subscriberIdentifier));

			edi.EOS();
		}

		public void REF_department()
		{

			edi.SEG("REF");

			edi.ST(1, "DX");
			edi.ST(2, subGroupNumber); //TODO!  0001 for Marlette, find somewhere to pull from system

			edi.EOS();
		}

		public void NM1_member_name()
		{
			edi.SEG("NM1");
			edi.SF(1, "IL");
			edi.SF(2, "1");
			edi.SF(3, memberLastName.replaceAll(" ", ""));
			edi.SF(4, memberFirstName.replaceAll(" ", ""));
			if (!isEmpty(memberMiddleName.replaceAll(" ", "")))
				edi.SF(5, memberMiddleName.replaceAll(" ", ""));
			//edi.SF(6, "");
			//edi.SF(7, "");
			if (!"000000000".equals(stripDashes(depSSN)))
			{
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			}
//			else
//			{
//				edi.SF(8, "");
//				edi.SF(9, "");
//			}
			edi.EOS();
		}

		public void NM1_mailing_address()
		{
			edi.SEG("NM1");
			edi.SF(1, "31");
			edi.SF(2, "1");
			edi.EOS();
		}

		public void N3_residence_address()
		{
			edi.SEG("N3");
			//shift address if messed up
			if (memberAddressLine1==null || memberAddressLine1.equals(""))
			{
				memberAddressLine1=memberAddressLine2;
				memberAddressLine2="";
			}
			edi.SF(1, memberAddressLine1);
			if (!memberAddressLine2.equals(""))
				edi.SF(2, memberAddressLine2);
			edi.EOS();
		}

		public void N3_mailing_address()
		{
			edi.SEG("N3");
			//shift address if messed up
			if (memberAddressLine1==null || memberAddressLine1.equals(""))
			{
				memberAddressLine1=memberAddressLine2;
				memberAddressLine2="";
			}
			edi.SF(1, memberAddressLine1);
			if (!memberAddressLine2.equals(""))
				edi.SF(2, memberAddressLine2);
			edi.EOS();
		}

		public void N4_residence_address()
		{
			edi.SEG("N4");
			edi.SF(1, memberAddressCity);
			edi.SF(2, memberAddressState);
			edi.SF(3, stripDashes(memberAddressZip).trim());
			//edi.SF(4, "");
			edi.EOS();
		}

		public void N4_mailing_address()
		{
			edi.SEG("N4");
			edi.SF(1, memberAddressCity);
			edi.SF(2, memberAddressState);
			edi.SF(3, stripDashes(memberAddressZip).trim());
			//edi.SF(4, "");
			edi.EOS();
		}

		public void DMG_demographic_info()
		{
			boolean emp = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));

			edi.SEG("DMG");
			if (memberBirthDate!=0)
			{
				edi.SF(1, "D8");
				edi.DC(2, memberBirthDate);
			}
			else
			{
				edi.SF(1, "");
				edi.SF(2, "");
			}
			
			edi.SF(3, genderCode);
			if (emp)
				edi.SF(4, maritalStatusCode);
			edi.SF(5, raceOrEthnicity);
			edi.SF(6, citizenStatusCode);
			edi.EOS();
		}
		
		public void HD_health_coverage()
		{
			edi.SEG("HD");
			edi.SF(1, "030");
			edi.SF(2, "");
			edi.SF(3, "VIS");
			edi.SF(4, "");

			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
				edi.SF(5, coverageLevelCode);
			else
				edi.SF(5, "");
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

		public void ICM_member_income()
		{
			edi.SEG("ICM");
			edi.SF(1,"7");
			edi.SF(2, "0.00");
			edi.SF(3, "40");
			edi.EOS();
		}

		public void INS_insured_benefit()
		{
			boolean emp = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));

			edi.SEG("INS");
			edi.ST(1, emp?"Y":"N");
			edi.ST(2, relationshipCode); 
			edi.ST(3, "030");
			edi.ST(4, "");
			edi.ST(5, benefitStatusCode);
			edi.ST(6, "E");
			edi.ST(7, "");
			if (emp)
			{
				edi.ST(8, employmentCode);
				edi.ST(9, "N");
			}
			else
			{
				edi.ST(8, "");				
				edi.ST(9, "N" /*studentStatus*/); //Hardcoded for CAS, TODO: add conditional check so VSP works for non-CAS
			}
			edi.ST(10, (handicap==null)?"N":handicap);
			edi.EOS();
		}

		public void PER_contact_info()
		{
			edi.SEG("PER");
			edi.ST(1, "IP");
			//edi.ST(2, "");
			edi.ST(3, "HP");
			edi.ST(4, homePhone);
			edi.ST(5, "EM");
			edi.ST(6, email);
			//edi.ST(7, "");
			//edi.ST(8, "");
			edi.EOS();
		}

		public void LUI_language()
		{
			boolean emp = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));

			edi.SEG("LUI");
			edi.ST(1, "LD");
			if (emp)
				edi.ST(2, "ENG");
			//edi.ST(3, "");
			edi.ST(4, "8");
			edi.EOS();
		}

		public void ediMemberLevelDetail()
		{
			//if(allDataValid())
			{
				INS_insured_benefit();
				REF_subscriber_number();
				REF_department();
				NM1_member_name();
				//PER_contact_info();
				N3_residence_address();
				N4_residence_address();
				DMG_demographic_info();
				//LUI_language();
				NM1_mailing_address();
				N3_mailing_address();
				N4_mailing_address();
				HD_health_coverage();
				DTP_benefit_start_date();
			}
		}


        private boolean allDataValid()
        {
            boolean passed = true;

            boolean ssn = false;
            boolean dob = false;
            boolean phone = false;
            boolean address = false;
            boolean state = false;
            boolean zip = false;
            boolean city = false;

            //social security number
            if (isEmpty(subscriberIdentifier)) {
                passed = false;
                ssn = true;
            }

            //birthday
            if (memberBirthDate == 0) {
                passed = false;
                dob = true;
            }

            //phone
            if (isEmpty(homePhone)) {
                passed = false;
                phone = true;
            }

            //address
            if (isEmpty(memberAddressLine1)) {
                passed = false;
                address = true;
            }

            //city
            if (isEmpty(memberAddressCity)) {
                passed = false;
                city = true;
            }

            //zip
            if (isEmpty(memberAddressZip)) {
                passed = false;
                zip = true;
            }

            //state
            if (isEmpty(memberAddressState)) {
                passed = false;
                state = true;
            }

            if (!passed)
                try {

                    ediLog.write(memberFirstName + " " + memberLastName + " is missing the following: " + "\n");

                    if (ssn)
                        ediLog.write("\tA valid Social Security Number" + "\n");
                    if (dob)
                        ediLog.write("\tA valid Birthdate" + "\n");
                    if (phone)
                        ediLog.write("\tA valid Home Phone Number" + "\n");
                    if (address)
                        ediLog.write("\tA valid Street Address" + "\n");
                    if (city)
                        ediLog.write("\tA valid City" + "\n");
                    if (state)
                        ediLog.write("\tA valid State" + "\n");
                    if (zip)
                        ediLog.write("\tA valid Zip Code" + "\n");

                    ediLog.flush();
                } catch (IOException e) {
                }

            return passed;
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
		//loadEngineWithOrgHierarchies();

		long mils=new Date().getTime();

		AIProperty p=new AIProperty("SponsorId", vendor.getOrgGroupId());
		String sponsorId=p.getValue();

		//WORK AROUND -- WHY IS CURRENT COMPANY NOT WHO WE THINK IT SHOULD BE??? 
		BVendorCompany bv = new BVendorCompany(vendor.getOrgGroupId());
        CompanyDetail cd = bv.getAssociatedCompany();
        ArahantSession.getHSU().setCurrentCompany(cd);

		List <HrBenefit> benefitsToDo=hsu.createCriteria(HrBenefit.class)
			.eq(HrBenefit.BENEFIT_PROVIDER, vendor)
			.list();

		for (HrBenefit bene : benefitsToDo)
		{
			String filename="";

			String accountNumber = bv.getAccountNumber();
			if(StringUtils.isEmpty(accountNumber))
			{
				//logger.info("Account number not set up for VSP!  This is critical for transmitting their files.  Account number represents the Client ID for them.");
				//accountNumber = "9999999";
				throw new ArahantException("Account number not set up for VSP!  This is critical for transmitting their files.  Account number represents the Client ID for them.");
			}
			try {
				filename=new BProperty("LargeReportDir").getValue()+File.separator+(debug?"t":"a") + accountNumber; //CAS 0023514  //Marlett  4844210 //TODO
				edi = new EDI(filename);
			} catch (IOException ex) {
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
			gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			//header(transactionSetNumber, bene.getPlanName(), bene.getPayerId(), bene.getPlanId(), sponsorId, gcn);
			
			header(transactionSetNumber, vendor.getName(), vendor.getInterchangeSenderId(), bv.getAssociatedCompany().getName(), vendor.getInterchangeSenderId(), gcn, accountNumber);

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

		try {
			ediLog.close();
		} catch (IOException ex) {
			//Logger.getLogger(EDI834Humana.class.getName()).log(Level.SEVERE, null, ex);
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

			info.setBenefitJoinData(bj, benefitType);


			info.maintenanceTypeCode="024";

			if (!info.memberFirstName.toLowerCase().contains("test") && !info.memberLastName.toLowerCase().contains("test"))
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
			if (covend!=0 && benefitType!=HrBenefitCategory.DENTAL)
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

			if (!info.memberFirstName.toLowerCase().contains("test") && !info.memberLastName.toLowerCase().contains("test"))
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
		edi.ST(5, "30");
		edi.SW(6, account, 15);
		edi.ST(7, "30");
		edi.SW(8, interchangeReceiverId, 15);
		edi.CD(9);
		edi.CT(10);
		edi.CH(11, 'U');
		edi.ST(12, "00401");
		edi.ST(13, icn);
		edi.CH(14, '0');
		edi.CH(15, 'P'); //T vs P is based off of the file name for them
		edi.ST(16,">");
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
		edi.ST(6, removeLeadingZeros(gcn));
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
		edi.ST(2, removeLeadingZeros(transactionSetNumber));
		edi.EOS();
	}

	private  void functionInterchangeTrailer(String icn)
	{
		edi.SEG("IEA");
		edi.NL(1, 1);
		edi.ST(2, icn);
		edi.EOS();
	}

	private  void header(int transactionSetNumber, String payer, String payerId, String sponsorName, String sponsorId, int gcn, String accountNumber)
	{
		edi.SEG("ST");
		edi.ST(1, "834");
		edi.ST(2, EDI.makecn(transactionSetNumber));
		edi.EOS();

		edi.SEG("BGN");
		edi.ST(1, "00");
		edi.ST(2, String.format("%06d", gcn)); //reference number
		edi.D8(3);
		edi.CT(4);
		edi.ST(5, "");
		edi.ST(6, "");
		edi.ST(7, "");
		edi.ST(8, "4");
		edi.EOS();

		edi.SEG("REF");
		edi.ST(1, "38");
		edi.ST(2, accountNumber); //Hardcoded for CAS -- very important to VSP.  This will change on a per client basis
		edi.EOS();

		//Sponsor
		edi.SEG("N1");
		edi.ST(1, "P5");
		edi.ST(2, sponsorName); //Client name
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, sponsorId);//Client tax id 
		edi.EOS();

		//Payer
		edi.SEG("N1");
		edi.ST(1, "TV");
		edi.ST(2, "Detroit Regional Chamber"); //DRC name
		edi.ST(3, "FI");//Identifier code  FI= fed id, ZZ= mutually defined
		edi.ST(4, "38-0477570"); //DRC tax ID
		edi.EOS();

	}

	public static String removeLeadingZeros(String s)
	{
		int index = s.lastIndexOf("0");
		return s.substring(index + 1, s.length());
	}

	public static void main(String args[])
	{
		//public String [] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug)
//		final BEDITransaction x=new BEDITransaction();
//		x.create();
//
//		x.setReceiver(ArahantSession.getHSU().getFirst(VendorCompany.class).getOrgGroupId());
//		x.setStatus(0);
//		x.setStatusDesc("Started");
//
//		x.insert();
//
//		ArahantSession.getHSU().commitTransaction();
//		ArahantSession.getHSU().beginTransaction();
//
//		EDI834VSP vsp = new EDI834VSP();
//
//		for (String filename : vsp.dumpEDI(ArahantSession.getHSU().get(CompanyBase.class,"00001-0000000167"), x, true))
//			System.out.print("*" + filename);

		//logger.info(new Date().getTime()/1000);
	/*	ArahantSession.getHSU().setCurrentPersonToArahant();



			final BEDITransaction x=new BEDITransaction();
			x.create();

			x.setReceiver(ArahantSession.getHSU().getFirst(VendorCompany.class).getOrgGroupId());
			x.setStatus(0);
			x.setStatusDesc("Started");

			x.insert();

			ArahantSession.getHSU().commitTransaction();
			ArahantSession.getHSU().beginTransaction();


			x.send834Insurance();
	*/

		//ed.dumpEDI("00001-0000000003","Delta Dental Plan of Michigan","381791480");
				//"62-6000913","62-6000913","62-6000913","62-6000913",
				//"076334622      ","076334622 ");

	}
}	
