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
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import jess.JessException;
import jess.Rete;
import org.kissweb.StringUtils;


public class EDI834DeltaWmCo
{
	public EDI834DeltaWmCo(String fileType)
	{
		this.fileType = fileType;
	}

	private String fileType = "F";
	private static final ArahantLogger logger=new ArahantLogger(EDI834DeltaWmCo.class);
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
		private String groupNumber;
		private String subGroupNumber;
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
			AIProperty groupProp=new AIProperty(r,"InsuranceGroupIds",benefitId,payingPerson.getPersonId(), efd);
			groupNumber=groupProp.getValue();
			subGroupNumber=groupProp.getValue2();
		//	groupNumber="";
		//	subGroupNumber="";
			if (groupNumber==null)
			{
				groupNumber="";//	throw new Error("no group number");
				logger.info("No group number for "+payingPerson.getPersonId());
			}
			if (subGroupNumber==null)
				subGroupNumber="";
			
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

			setCoveredPerson(hsu.get(Person.class,bj.getCoveredPersonId()));


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
			
			if (benefitType==HrBenefitCategory.DENTAL && covdate<bj.getPolicyStartDate())
				covdate=bj.getPolicyStartDate();
			
			if (benefitType==HrBenefitCategory.DENTAL && covdate!=0 && covdate < 20080701)
				covdate=20080701;
	
			coverageStartDate=DateUtils.getDateCCYYMMDD(covdate);
			policyStart=bj.getPolicyStartDate();
		
			if (coverageStartDate.equals("")&&bj.getCoverageEndDate()==0)
				logger.info("coverage start date missing");
			
			int covend=bj.getCoverageEndDate();
			if (covend!=0 && benefitType!=HrBenefitCategory.DENTAL)
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
			edi.SF(2, groupNumber); 
			edi.EOS();
		}
		public void REF_subscriber_subgroup_number()
		{
			//Consociates says don't do this for dependents,
			//Delta Dental wants it
			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))||benefitType==HrBenefitCategory.DENTAL)
			{
				edi.SEG("REF");
				edi.SF(1, "17");
				edi.SF(2, subGroupNumber);
				edi.EOS();
			}
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
			if (benefitType==HrBenefitCategory.DENTAL)
			{
				if (employmentStartDate==0)
				{
					edi.SF(8, "34");
					edi.SF(9, stripDashes(depSSN));
				}
				else
				{
					edi.SF(8, "");
					edi.SF(9, "");
				}
			}
			else
			{
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
		 
			}
			edi.EOS();
		}
				
		public void N3_address_information()
		{
			edi.SEG("N3");
			//shift address if messed up
			if (memberAddressLine1==null || memberAddressLine1.equals(""))
			{
				memberAddressLine1=memberAddressLine2;
				memberAddressLine2="";
			}
			edi.SF(1, memberAddressLine1);
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
			
		}
		public void HD_health_coverage()
		{
			edi.SEG("HD");
			//has there been a change?
			
			//"021" addition, "024" cancel or term, "030" no change
			//String changeCode="030";	
			
			if (benefitType==HrBenefitCategory.DENTAL)
				edi.SF(1, maintenanceTypeCode);
			else
				edi.SF(1, "030"); //Consociates just wants a 030
			
			edi.SF(2, "");
			
			
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
			switch (benefitType)
			{
				case HrBenefitCategory.DENTAL : edi.SF(3, "DEN");  //Should be D for dental
												 edi.SF(4, "30"); //30 for Dental
												 break;
				case HrBenefitCategory.HEALTH : edi.SF(3, "HLT");  
												 edi.SF(4, benefitName); 
												 break;
				case HrBenefitCategory.LONG_TERM_CARE : edi.SF(3, "LTC");  
												 edi.SF(4, benefitName); 
												 break;
				case HrBenefitCategory.SHORT_TERM_CARE : edi.SF(3, "STC");  
												 edi.SF(4, benefitName); 
												 break;
				case HrBenefitCategory.VISION : edi.SF(3, "VIS");  
												 edi.SF(4, benefitName); 
												 break;
												 
				default:	edi.SF(3, insuranceLineCode);  
							edi.SF(4, benefitName);
					
			}
			
			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))||benefitType==HrBenefitCategory.DENTAL)
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
		public void DTP_benefit_end_date()
		{
			if (!"".equals(coverageEndDate)&&(maintenanceTypeCode.equals("024")
					|| benefitType!=HrBenefitCategory.DENTAL))
			{
				edi.SEG("DTP");
				edi.SF(1, "349");
				edi.ST(2, "D8");
				edi.SF(3, coverageEndDate);
				edi.EOS();
			}
			
		}
		public void INS_insured_benefit()
		{
			edi.SEG("INS");

			edi.ST(1, stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))?"Y":"N");
			edi.ST(2, relationshipCode);
			if (benefitType==HrBenefitCategory.DENTAL)
				edi.ST(3, maintenanceTypeCode);
			else
				edi.ST(3, "030"); //for consociates
			if (benefitType==HrBenefitCategory.DENTAL)
				edi.ST(4, "AI"); //means not giving reason for change
			else
			{
				//means not giving reason for change
				//TODO: fill in reason code when given mapping
				String reasonCode="AI";
				if (changeReason.trim().equalsIgnoreCase("Divorce"))
					reasonCode="01";
				if (changeReason.trim().equalsIgnoreCase("Birth, Adoption or Legal Custody of a Child"))
					reasonCode="02";
				if (changeReason.trim().equalsIgnoreCase("Death"))
					reasonCode="03";
				if (changeReason.trim().equalsIgnoreCase("Terming Employment"))
					reasonCode="08";
				if (changeReason.trim().equalsIgnoreCase("Elected COBRA"))
					reasonCode="09";
				if (changeReason.trim().equalsIgnoreCase("Open Enrollment"))
					reasonCode="14";
				if (changeReason.trim().equalsIgnoreCase("Spouse Gaining Other Coverage"))
					reasonCode="14";
				if (changeReason.trim().equalsIgnoreCase("New Hire"))
					reasonCode="28";
				if (changeReason.trim().equalsIgnoreCase("Marriage"))
					reasonCode="32";
				if (changeReason.trim().equalsIgnoreCase("LOA - Declined Benefits"))
					reasonCode="38";
                if (changeReason.trim().equalsIgnoreCase("LOA - Termed for Nonpayment"))
                    reasonCode="38";
				if (changeReason.trim().equalsIgnoreCase("Rehired"))
					reasonCode="41";
				if (changeReason.trim().equalsIgnoreCase("LOA - Returned to Work"))
					reasonCode="41";
		//		if (changeReason.trim().equalsIgnoreCase("Marriage"))
		//			reasonCode="32";
				
				
				
			/*	switch (changeReason)
				{
					case 
				}
			 * */
					
				edi.ST(4, reasonCode); 
			}
			edi.ST(5, benefitStatusCode);
			edi.ST(6, "");
			edi.ST(7, "");
			if (benefitType==HrBenefitCategory.DENTAL)
				edi.ST(8, stripDashes(depSSN).equals(stripDashes(subscriberIdentifier))?employmentCode:"");
			else
				edi.ST(8, employmentCode);
			edi.ST(9, (studentStatus==null)?"N":studentStatus);
			edi.ST(10, (handicap==null)?"N":handicap);
			edi.EOS();
		}
		
		public void REF_subscriber_carrier_id()
		{
			
			edi.SEG("REF");
			
			try
			{
				
				switch (benefitType)
				{	
					case HrBenefitCategory.DENTAL :	edi.ST(1, "DX");
														edi.ST(2, "DDPTN");  
														break;
					default:	edi.ST(1, "DX");
								edi.ST(2, subGroupNumber);
				}
			}
			catch (Exception e)
			{
				edi.ST(1, "");
				edi.ST(2, "");
			}
			
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
			
			//logger.info("Ed det 2");
			
			
			//eligibility end date
			DTP_employment_date();
			DTP_employment_term_date();
			NM1_member_name();
			N3_address_information();
			N4_geographic_location();
			DMG_demographic_info();
			
			//logger.info("Ed det 3");
			//Incorrect member name
			HD_health_coverage();
			DTP_benefit_start_date(); //benefit begin
			DTP_benefit_end_date();//benefit end
			
		//TODO: pcp turned off until they can handle it	PCP_provider(); //primary care provider
			
			
			//If I'm not dental and this is an 024
			//see if they had Dental and add cobra
			if (benefitType!=HrBenefitCategory.DENTAL && maintenanceTypeCode.equals("024"))
			{
				HibernateCriteriaUtil<HrBenefitJoin> hcu=ArahantSession.getHSU().createCriteria(HrBenefitJoin.class);
				hcu.gt(HrBenefitJoin.HISTORY_DATE, lastDumpDate)
					.ne(HrBenefitJoin.COVERAGE_END_DATE, 0);
				hcu.joinTo(HrBenefitJoin.COVERED_PERSON).eq(Person.PERSONID, personId);
				hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT)
					.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL);
				
				int covEndDate=0;
				boolean addDentalCobra=false;
				
				HrBenefitJoin bj=hcu.first();
				
				if (bj!=null)
				{
					covEndDate=bj.getCoverageEndDate();
					addDentalCobra=true;
				}
				if (!addDentalCobra) //check history records
				{
					HibernateCriteriaUtil<HrBenefitJoinH> hcuh=ArahantSession.getHSU().createCriteria(HrBenefitJoinH.class);
					hcuh.ge(HrBenefitJoinH.HISTORY_DATE, lastDumpDate)
						.ne(HrBenefitJoinH.COVERAGE_END_DATE, 0);
					hcuh.eq(HrBenefitJoinH.COVERED_PERSON_ID, personId);
					hcuh.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).joinTo(HrBenefitConfig.HR_BENEFIT)
						.joinTo(HrBenefit.BENEFIT_CATEGORY).eq(HrBenefitCategory.TYPE, HrBenefitCategory.DENTAL);
				
					HrBenefitJoinH bjh=hcuh.first();
					if (bjh!=null)
					{
						covEndDate=bjh.getCoverageEndDate();
						addDentalCobra=true;
					}
				}
				
				if (addDentalCobra)
				{
					
					DateUtils.add(covEndDate, 1);
					//HD*024**DEN**EMP~
					
					edi.SEG("HD");
					edi.SF(1, "030");//consociates only takes 030
					edi.SF(2, "");
					edi.SF(3, "DEN");  
					edi.SF(4, "Dental"); 
			
					if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
						edi.SF(5, coverageLevelCode);
					else
						edi.SF(5, "");
					edi.EOS();
			
					edi.SEG("DTP");
					edi.SF(1, "348");
					edi.ST(2, "D8");
					if (bj==null)
						edi.SF(3, DateUtils.getDateCCYYMMDD(covEndDate));
					else
						edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.add(bj.getCoverageStartDate(),1)));
					edi.EOS();
					
					//DTP*349*D8*20080915~
					edi.SEG("DTP");
					edi.SF(1, "349");
					edi.ST(2, "D8");
					edi.SF(3, DateUtils.getDateCCYYMMDD(covEndDate));
					edi.EOS();
				}
				
				
			}
			
			//benefit group number
			//benefit subgroup id			
			//logger.info("Ed det 4");
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

	
	public String [] dumpEDI(CompanyBase vendor,
			BEDITransaction tran, boolean debug)	
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
			.list();
		
		for (HrBenefit bene : benefitsToDo)
		{
			String filename="";
			try {
				filename=new BProperty("LargeReportDir").getValue()+File.separator+"EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn+"_"+DateUtils.now()+"_"+(filenames.size()+1)+".edi.txt";
				edi = new EDI(filename);
			} catch (IOException ex) {
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
			gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			header(transactionSetNumber, bene.getPlanName(), bene.getPayerId(), bene.getPlanId(), sponsorId, gcn);  
			
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
			
			info.setBenefitJoinData(bj, benefitType);

						
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
			if (benefitType==HrBenefitCategory.DENTAL && covdate!=0 && covdate < 20080701)
				covdate=20080701;
	
			
			int covend=bj.getCoverageEndDate();
			if (covend!=0 && benefitType!=HrBenefitCategory.DENTAL)
				covend=DateUtils.addDays(covend, 1);

	
		//	if (p.getLastName().equals("Jenkins")&&p.getFirstName().equals("Robert"))
		//		logger.info("On mike");
			
			if (covdate!=0 && bj.getRecordChangeDate().after(lastDumpDate)) 
			//if (covdate!=0 && bj.getRecordChangeDate().after(DateUtils.getDate(20081223)))
				info.maintenanceTypeCode="021";
			
			if (covend!=0 && bj.getRecordChangeDate().after(lastDumpDate)) 
				info.maintenanceTypeCode="024";
			
			if (bj.getPolicyEndDate()!=0 && bj.getRecordChangeDate().after(lastDumpDate)) 
				info.maintenanceTypeCode="024";


			//replaced these with the TWO below
			//if (info.maintenanceTypeCode.equals("030") &&
			//		covend<DateUtils.now() && covend!=0)
			//	continue;

			//if (info.maintenanceTypeCode.equals("030") &&
			//		bj.getPolicyEndDate()<DateUtils.now() && bj.getPolicyEndDate()!=0)
			//	continue;


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
	
	private void	gs_seg(String gcn, String account, String applicationRecieverId)
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
	
	private  void header(int transactionSetNumber, String payer, String payerId, String sponsorName, String sponsorId, int gcn)
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
