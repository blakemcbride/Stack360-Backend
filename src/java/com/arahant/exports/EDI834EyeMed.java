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
import java.util.logging.Level;
import java.util.logging.Logger;

public class EDI834EyeMed {

    private static final ArahantLogger logger = new ArahantLogger(EDI834EyeMed.class);

    //File logFile = new File(FileSystemUtils.getWorkingDirectory() + File.separator + "EyeMed_EDI_LOG.txt");  //TODO: put this somewhere for clients
    EDILog ediLog = new EDILog("EyeMed_EDI");
    private static String vendorCode = "EyeMed Vision Care";
    boolean newLog = true;

    private class EDIDumpPersonalInfo implements Comparable<EDIDumpPersonalInfo> {

        private EDI edi;
        private String coverageEndDate;
        private String frequencyCode = "U";
        private String raceOrEthnicity = "7";
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
        private String citizenStatusCode = "1";
        private String entityIdentifierCode = "I";
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
        private String maintenanceTypeCode = "030";
        private int benefitType;
        private String benefitName;
        private String personId;
        private String primaryCarePhysician;
        private String changeReason;
        private int policyStart;
        private String benefitClassId;
        private boolean hasSpouse;
        private boolean hasMedicare;
        private char smoker;
        private boolean subscriber;

        private String groupAccountId;
        private String groupVendorId;

        private String groupNumber;
        private String subGroupNumber;
        private String plan;
        private String planName;
        private String payerId;

		private String ediFieldValue1;
		private String ediFieldValue2;
		private String ediFieldValue3;
		private String ediFieldValue4;
		private String ediFieldValue5;

        public EDIDumpPersonalInfo(EDI edi) {
            this.edi = edi;
        }

        public void setCoveredPerson(Person coveredPerson) {
            personId = coveredPerson.getPersonId();
            memberLastName = coveredPerson.getLname();
            memberFirstName = coveredPerson.getFname();
            memberMiddleName = coveredPerson.getMname();
            depSSN = coveredPerson.getUnencryptedSsn();
            if ("999-99-9999".equals(depSSN))
                depSSN = "000-00-0000";
            memberBirthDate = coveredPerson.getDob();
            studentStatus = (coveredPerson.getStudent() == 'Y') ? "F" : "N";
            handicap = coveredPerson.getHandicap() + "";
            genderCode = coveredPerson.getSex() + "";

            smoker = coveredPerson.getSmoker();
			
        }

        public void setPayingPerson(Person payingPerson, String benefitId) {
            subscriberIdentifier = payingPerson.getUnencryptedSsn();

            if ("999-99-9999".equals(subscriberIdentifier))
                subscriberIdentifier = "000-00-0000";
            int efd = 99999999;

            if (policyStart > efd)
                efd = policyStart;

            BHRBenefit bhr = new BHRBenefit(benefitId);

            groupNumber = bhr.getGroupId();
			subGroupNumber = bhr.getSubGroupId();
			groupAccountId = bhr.getGroupAccountId();
			if(new BPerson(payingPerson).getOrgGroupAssociations().size() > 0)
			{
				BOrgGroup bog = new BOrgGroup(new BPerson(payingPerson).getOrgGroupAssociations().iterator().next().getOrgGroup());
				VendorGroup vg = ArahantSession.getHSU().createCriteria(VendorGroup.class)
						.eq(VendorGroup.VENDOR, bhr.getBean().getProvider())
						.eq(VendorGroup.ORG_GROUP, bog.getOrgGroup())
						.first();
				if(vg != null)
				{
					groupVendorId = vg.getGroupVendorId();
					groupVendorId = groupVendorId.replace(groupAccountId, "");
				}
				else
				{
					groupVendorId = groupAccountId;
					try {
						ediLog.write(payingPerson.getNameFL() + "'s (" + subscriberIdentifier + ") org group association has no group vendor ID (" + bog.getName() + ").\n");
					} catch (IOException ex) {
						Logger.getLogger(EDI834EyeMed.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
				
			}
			else
			{
				groupVendorId = groupAccountId;
				try {
					ediLog.write(payingPerson.getNameFL() + " (" + subscriberIdentifier + ") has no org group association." + "\n");
				} catch (IOException ex) {
					Logger.getLogger(EDI834EyeMed.class.getName()).log(Level.SEVERE, null, ex);
				}
			}

            benefitClassId = bhr.getInsuranceCode();
			plan = bhr.getPlan();
			planName = bhr.getPlanName();
			payerId = bhr.getPayerId();
			ediFieldValue1 = bhr.getEdiFieldValue1();
			ediFieldValue2 = bhr.getEdiFieldValue2();
			ediFieldValue3 = bhr.getEdiFieldValue3();
			ediFieldValue4 = bhr.getEdiFieldValue4();
			ediFieldValue5 = bhr.getEdiFieldValue5();

            if (groupNumber == null)
                groupNumber = "";

            if (groupVendorId == null)
                groupVendorId = "";

            if (benefitClassId == null)
                benefitClassId = "";

//            BEmployee be = new BEmployee(payingPerson.getPersonId());
//            hasSpouse = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, be.getEmployee()).eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S').exists();
//            hasMedicare = ArahantSession.getHSU().createCriteria(Employee.class).notIn(Employee.MEDICARE, new String[]{"", "N"}).exists();

        }

        @Override
        public String toString() {
            return memberLastName + ", " + memberFirstName + "\n";
        }

        private boolean isEmpty(String s) {
            return s == null || s.trim().equals("");
        }

        private void setAddress(BPerson p, BPerson dep) {

            if (!isEmpty(dep.getHomePhone()))
                homePhone = dep.getHomePhone();
            else
                homePhone = p.getHomePhone();

            if (!isEmpty(dep.getWorkPhoneNumber()))
                workPhone = dep.getWorkPhoneNumber();
            else
                workPhone = p.getWorkPhoneNumber();


            if (!isEmpty(dep.getStreet())) {
                memberAddressLine1 = dep.getStreet();
                memberAddressLine2 = dep.getStreet2();

                memberAddressCity = dep.getCity();
                memberAddressState = dep.getState();
                memberAddressZip = dep.getZip();
            } else {
                memberAddressLine1 = p.getStreet();
                memberAddressLine2 = p.getStreet2();

                memberAddressCity = p.getCity();
                memberAddressState = p.getState();
                memberAddressZip = p.getZip();

            }
        }

        private void setBenefitJoinData(IHrBenefitJoin bj, int benefitType) {
            setLocation(bj);

            setRelationshipCode(bj);

            benefitStatusCode = (bj.getUsingCOBRA() == 'Y') ? "C" : "A";
            this.benefitType = benefitType;

            setPCP(bj);

            BPerson p = new BPerson(bj.getPayingPersonId());

            //	if (p.getLastName().equalsIgnoreCase("KNOTTS"))
            //		logger.info("At knotts");

            BEmployee emp = null;
            if (p.isEmployee())
                emp = new BEmployee(bj.getPayingPersonId());


            setEmploymentCode(bj, emp);

            HibernateSessionUtil hsu = ArahantSession.getHSU();


            setPayingPerson(hsu.get(Person.class, bj.getPayingPersonId()), hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId()).getHrBenefit().getBenefitId());

            setCoveredPerson(bj.getCoveredPerson());


            dateTimeQualifier = "300";

            if (bj.getUsingCOBRA() == 'Y')
                dateTimeQualifier = "340";

            statusInformationEffectiveDate = DateUtils.getDateCCYYMMDD(bj.getPolicyStartDate());


            BPerson d = new BPerson(bj.getCoveredPersonId());

            setAddress(p, d);

            setMaritalStatus(emp);

            HrBenefitConfig hbc = ArahantSession.getHSU().get(HrBenefitConfig.class, bj.getHrBenefitConfigId());

            insuranceLineCode = hbc.getHrBenefit().getInsuranceCode();

            setCoverageLevel(bj);

            int covdate = bj.getCoverageStartDate();

            coverageStartDate = DateUtils.getDateCCYYMMDD(covdate);
            policyStart = bj.getPolicyStartDate();

            if (coverageStartDate.equals("") && bj.getCoverageEndDate() == 0)
                logger.info("coverage start date missing");

            int covend = bj.getCoverageEndDate();
            if (covend != 0)
                covend = DateUtils.addDays(covend, 1);

            coverageEndDate = DateUtils.getDateCCYYMMDD(covend);

            changeReason = bj.getChangeDescription();

        }

        private void setCoverageLevel(IHrBenefitJoin bj) {
            coverageLevelCode = "EMP";

            if (bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' ||
                    bj.getHrBenefitConfig().getSpouseNonEmployee() == 'Y')
                coverageLevelCode = "ESP";

            if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren() == 'Y') ||
                    (bj.getHrBenefitConfig().getSpouseEmpOrChildren() == 'Y') ||
                    (bj.getHrBenefitConfig().getChildren() == 'Y'))
                coverageLevelCode = "FAM";

            if (bj.getHrBenefitConfig().getMaxChildren() == 1)
                coverageLevelCode = "ESP";

            if (bj.getHrBenefitConfig().getChildren() == 'Y' && bj.getHrBenefitConfig().getMaxChildren() == 1 &&
                    bj.getHrBenefitConfig().getSpouseEmployee() == 'Y')
                coverageLevelCode = "FAM";

        }

        private void setEmploymentCode(IHrBenefitJoin bj, BEmployee emp) {
            employmentCode = "";

            if (emp != null) {
                BHREmplStatusHistory x = emp.getLastStatusHistory();

                if (x != null) //TODO: log this person and do not send
                {
                    String s = "";

                    if (!isEmpty(x.getStatusName()))
                        s = x.getStatusName().replaceAll("-", " ").toLowerCase();

                    if ((s.indexOf("full time") != -1) || (s.indexOf("active") != -1))
                        employmentCode = "FT";
                    else if ((s.indexOf("part time") != -1) || (s.indexOf("seasonal") != -1))
                        employmentCode = "PT";
                    else if ((s.indexOf("terminated") != -1) || (s.indexOf("inactive") != -1))
                        employmentCode = "TE";

                    employmentStartDate = emp.getEmploymentDate();
                }
            }
            if (employmentCode.equals("TE")) {
                //maintenanceTypeCode = "024";

                try {
                    employmentStartDate = emp.getLastActiveStatusHistory().getEffectiveDate();
                } catch (Exception e) {
                    try {
                        employmentStartDate = emp.getEmploymentDate();
                    } catch (Exception x) {
                    }
                }
                try {
                    employmentEndDate = emp.getLastStatusDate();
                } catch (Exception e) {
                }
            }
        }

        private void setLocation(IHrBenefitJoin bj) {

            if (bj.getUsingCOBRA() == 'Y')
                //if this is a dependent, fake the location
                if (bj.getPayingPerson().getOrgGroupAssociations().size() == 0) {
                    HrEmplDependent dep = ArahantSession.getHSU().createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, bj.getPayingPerson()).first();
                    if (dep != null) {
                        String ogid = dep.getEmployee().getOrgGroupAssociations().iterator().next().getOrgGroupId();
                        ArahantSession.AICmd("(assert (org_group_association (person_id \"" + bj.getPayingPersonId() + "\")(org_group_id \"" + ogid + "\")))");

                    }
                }
        }

        private void setMaritalStatus(BEmployee emp) {
            maritalStatusCode = "I";

            if (emp != null)
                if (emp.getActiveSpouse(DateUtils.now()) != null)
                    maritalStatusCode = "M";
                else if (emp.getActiveSpouse(0) != null)
                    maritalStatusCode = "U";
        }

        private void setPCP(IHrBenefitJoin bj) {
            if (bj.getComments() == null || "".equals(bj.getComments().trim()))
                primaryCarePhysician = null;
            else
                primaryCarePhysician = bj.getComments().trim();
        }

        private void setRelationshipCode(IHrBenefitJoin bj) {
            relationshipCode = "18";
            if (!bj.getPayingPersonId().equals(bj.getCoveredPersonId()))
                try {
                    BHREmplDependent dep = new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());

                    switch (dep.getRelationship()) {
                        case 'S':
                            relationshipCode = "01";
                            break;
                        case 'C':
                            relationshipCode = "19";
                            break;
                        default:
                            //TODO: not a legal relationship - we need them to fix the others
                            //	logger.info("Skipping relationship "+dep.getRelationship());
                            relationshipCode = "19";
                        //	continue;
                    }
                } catch (Exception e) {
                    //relationship was not set up correctly - use 19
                    relationshipCode = "19";
                }

        }

        public void INS_insured_benefit() {
            subscriber = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));

            edi.SEG("INS");

            edi.ST(1, subscriber ? "Y" : "N");
            edi.ST(2, relationshipCode);

            edi.ST(3, maintenanceTypeCode); //for principal life

            edi.ST(4, "XN"); //means not giving reason for change


            //edi.ST(4, "");
            edi.ST(5, benefitStatusCode);
            edi.ST(6, "");
            if (benefitStatusCode.equals("C")) {
                String code = "1";
                if (changeReason.trim().startsWith("Dependent Ineligible"))
                    code = "7";
                if (changeReason.trim().equalsIgnoreCase("Divorce"))
                    code = "5";
                if (changeReason.trim().equalsIgnoreCase("Part Time"))
                    code = "2";
                if (changeReason.trim().startsWith("Terming"))
                    code = "1";
                if (changeReason.trim().equalsIgnoreCase("Death"))
                    code = "4";
                edi.ST(7, code);
            } else
                edi.ST(7, "");
            edi.ST(8, employmentCode);
//            edi.ST(9, ""); // Was (studentStatus == null) ? "N" : studentStatus
//            edi.ST(10, ""); // Was (handicap == null) ? "N" : handicap
//            edi.ST(11, "");
//            edi.ST(12, "");
            edi.EOS();
        }

        public void DTP_employment_date() {
            if (employmentStartDate != 0) {
                edi.SEG("DTP");
                edi.SF(1, "336");
                edi.ST(2, "D8");
                edi.DC(3, employmentStartDate);
                edi.EOS();
            }
        }

        public void DTP_employment_term_date() {
            if (employmentEndDate != 0) {
                edi.SEG("DTP");
                edi.SF(1, "357");
                edi.ST(2, "D8");
                edi.DC(3, employmentEndDate);
                edi.EOS();
            }
        }

        //TODO: determine if spouse if enrolled in benefit
        public void INS_maintenance_reason_code() {
            if (hasSpouse) {
                edi.SEG("INS");
                edi.ST(4, "11");
                edi.ST(5, "S");
                edi.EOS();
            }
        }

        public void REF_SSN_subscriber() {
            edi.SEG("REF");
            edi.SF(1, "0F");//Subscriber Number Qualifier
            edi.SF(2, stripDashes(subscriberIdentifier));
            edi.EOS();
        }

        public void REF_subscriber_group_number() {
            edi.SEG("REF");
            edi.SF(1, "1L");
            edi.SF(2, groupNumber);  // Should be '9744954' for CAS Res -. EyeMed
            edi.EOS();
        }

        public void REF_subscriber_subgroup_number() {
            edi.SEG("REF");
            edi.SF(1, "DX");
            edi.SF(2, groupVendorId);
            edi.EOS();
        }

        public void REF_subscriber_class() {
            edi.SEG("REF");
            edi.SF(1, "17");
            edi.SF(2, benefitClassId);
            edi.EOS();
        }

        public void REF_medicare_HIC_number() {
            if (hasMedicare) {
                edi.SEG("REF");
                edi.ST(1, "F6");
                edi.ST(2, "");  //TODO: get HIC number
            }
        }

        public void NM1_member_name() {
            edi.SEG("NM1");
            edi.SF(1, "IL");
            edi.SF(2, "1");
            edi.SF(3, memberLastName);
            edi.SF(4, memberFirstName);
            if(!isEmpty(memberMiddleName)) {
				if(memberMiddleName.length() == 1)
					edi.SF(5, memberMiddleName);
				else
					edi.SF(5, String.valueOf(memberMiddleName.charAt(0)));

				if (!"000000000".equals(stripDashes(depSSN)) && checkValidSSN(depSSN)) {
					edi.SF(8, "34");
					edi.SF(9, stripDashes(depSSN));
				}
			}
			else if (!"000000000".equals(stripDashes(depSSN)) && checkValidSSN(depSSN)) {
				edi.SF(5, "");
				edi.SF(6, "");
				edi.SF(7, "");
                edi.SF(8, "34");
                edi.SF(9, stripDashes(depSSN));
            }

//            else
//                edi.SF(5, "");
//            edi.SF(6, "");
//            edi.SF(7, "");
//
//            if (!"000000000".equals(stripDashes(depSSN))) {
//                edi.SF(8, "34");
//                edi.SF(9, stripDashes(depSSN));
//            } else {
//                edi.SF(8, "");
//                edi.SF(9, "");
//            }

            edi.EOS();
        }

        public void PER_contact_info() {
            edi.SEG("PER");
            edi.ST(1, "IP");
            edi.ST(2, "");
            edi.ST(3, "HP");
            edi.ST(4, stripDashes(homePhone));
            edi.EOS();
        }

        public void N3_address_information() {
            //shift address if messed up
            if (memberAddressLine1 == null || memberAddressLine1.equals("")) {
                memberAddressLine1 = memberAddressLine2;
                memberAddressLine2 = "";
            }

            if (memberAddressLine1.length() > 24) {
                String temp = memberAddressLine1.trim() + " " + memberAddressLine2.trim();

                StringTokenizer st = new StringTokenizer(temp, " ");
                memberAddressLine1 = "";
                memberAddressLine2 = "";

                String token = "";
                while (st.hasMoreTokens()) {
                    token = st.nextToken();

                    if ((memberAddressLine1.length() + token.length()) < 24)
                        memberAddressLine1 += token;
                    else
                        break;
                }

                while (st.hasMoreTokens())
                    memberAddressLine2 += st.nextToken();
            }

            edi.SEG("N3");
            edi.SF(1, memberAddressLine1);
//            if (memberAddressLine2 != null && !memberAddressLine2.equals(""))
			if(!isEmpty(memberAddressLine2))
				edi.SF(2, memberAddressLine2);
            edi.EOS();
        }

        public void N4_geographic_location() {
            edi.SEG("N4");
            edi.SF(1, memberAddressCity);
            edi.SF(2, memberAddressState);
            edi.SF(3, stripDashes(memberAddressZip).trim());
            edi.EOS();
        }

        public void DMG_demographic_info() {
            edi.SEG("DMG");
            if (memberBirthDate != 0)
                edi.SF(1, "D8");
            else
                edi.SF(1, "");
            edi.DC(2, memberBirthDate);
            edi.SF(3, genderCode);
//            if (subscriber)
//                edi.ST(4, maritalStatusCode);
            edi.EOS();
        }

        public void HLH_health_information() {
            edi.SEG("HLH");
            edi.ST(1, smoker == 'Y' ? "T" : "N");
            edi.EOS();
        }

        public void DSB_disability_info() {
            //TODO: find out what goes here
        }

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
        public void HD_health_coverage() {
            edi.SEG("HD");
            //has there been a change?

            //"021" addition, "024" cancel or term, "030" no change
            //String changeCode="030";

            edi.SF(1, maintenanceTypeCode);

            edi.SF(2, "");

            switch (benefitType) {
//                case HrBenefitCategory.DENTAL:
//                    edi.SF(3, "DEN");  //Should be D for dental
//                    edi.SF(4, benefitName); //30 for Dental
//                    break;
//                case HrBenefitCategory.HEALTH:
//                    edi.SF(3, "MM"); //major medical for EBC
//                    edi.SF(4, benefitName);
//                    break;
//                case HrBenefitCategory.LONG_TERM_CARE:
//                    edi.SF(3, "LTC");
//                    edi.SF(4, benefitName);
//                    break;
//                case HrBenefitCategory.SHORT_TERM_CARE:
//                    edi.SF(3, "STC");
//                    edi.SF(4, benefitName);
//                    break;
                case HrBenefitCategory.VISION:
                    edi.SF(3, "VIS");
                    edi.SF(4, "LEV1");  // Was benefitName
                    break;

                default:
                    edi.SF(3, insuranceLineCode);
                    edi.SF(4, benefitName);

            }

            if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
                edi.SF(5, coverageLevelCode);
//			else
//				edi.SF(5, "");
            edi.EOS();
        }

        public void HD_RX_other(String plan) {

            edi.SEG("HD");
            edi.SF(1, "001");
            edi.ST(2, "");
            edi.ST(3, "PDG");
            edi.SF(4, plan);
			if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
                edi.SF(5, coverageLevelCode);
            edi.EOS();
        }

        public void HD_other(String code) {

            edi.SEG("HD");
            edi.SF(1, "001");
            edi.ST(2, "");
            edi.ST(3, code);
            edi.SF(4, "HP");
            edi.EOS();
        }

		public void REF_1L_other(String code)
		{
			edi.SEG("REF");
            edi.SF(1, "1L");
            edi.ST(2, code);
			edi.EOS();
		}

        public void DTP_benefit_start_date() {

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
            if ("024".equals(maintenanceTypeCode))
			{
                edi.SEG("DTP");
                edi.SF(1, "349");
                edi.ST(2, "D8");
                if ("".equals(coverageEndDate))
                    logger.info("Coverage end date missing");

                edi.SF(3, coverageEndDate);
                edi.EOS();
            }
        }

		public void IDC_identification_card()
		{
			edi.SEG("IDC");
			edi.SF(1, "YYYA");
			edi.SF(2, "H");
			edi.EOS();
		}

		public void Additional_Products()
		{
			edi.SEG("HD");
			edi.SF(1, "021");
			edi.SF(3, "HLT");
			edi.SF(4, "OI-001382CA");
			edi.EOS();

			edi.SEG("HD");
			edi.SF(1, "021");
			edi.SF(3, "HLT");
			edi.SF(4, "OI-001382MA");
			edi.EOS();

			edi.SEG("HD");
			edi.SF(1, "021");
			edi.SF(3, "HLT");
			edi.SF(4, "OI-001382YY");
			edi.EOS();

			edi.SEG("HD");
			edi.SF(1, "021");
			edi.SF(3, "HLT");
			edi.SF(4, "OI-001382YA");
			edi.EOS();

			edi.SEG("HD");
			edi.SF(1, "021");
			edi.SF(3, "HLT");
			edi.SF(4, "OI-001382PR");
			edi.EOS();

		}

		public boolean checkValidSSN(String ssn) {
			String strippedSSN = stripDashes(ssn).trim();

			if(strippedSSN.length() == 9) {
				for(int i = 0; i < 9; i++) {
					if(!Character.isDigit(strippedSSN.charAt(i)))
						return false;
				}
			}
			else
				return false;
			return true;
		}

        public void ediMemberLevelDetail() {
            //Check for missing data here and write to log if there is
//            if (allDataValid())
//            {
                INS_insured_benefit();
                REF_SSN_subscriber();
                REF_subscriber_group_number();
                //REF_subscriber_class();
                //REF_subscriber_subgroup_number();
				//IDC_identification_card();
                //DTP_employment_date();
                //DTP_employment_term_date();
                NM1_member_name();
//                PER_contact_info();
                N3_address_information();
                N4_geographic_location();
                DMG_demographic_info();

                HD_health_coverage();
                DTP_benefit_start_date();
                DTP_benefit_end_date();
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
            if (!checkValidSSN(subscriberIdentifier)) {
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

                    //create the file is it needs to be created
                   
                    
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

            if (relationshipCode.equals("18"))
                return -1;

            if (o.relationshipCode.equals("18"))
                return 1;

            if (relationshipCode.equals("01"))
                return -1;

            if (o.relationshipCode.equals("01"))
                return 1;

            return 0; //19 - child

        }
    }
    private EDI edi;
    private Date lastDumpDate;
    List<String> payingPersons = new LinkedList<String>();

    public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) {
        List<String> filenames = new LinkedList<String>();
        HibernateSessionUtil hsu = ArahantSession.getHSU();
        hsu.dontAIIntegrate();

        int icn = tran.getStartingICN();
        int gcn = tran.getStartingGCN();
        int transactionSetNumber = tran.getStartingTscn();

        lastDumpDate = tran.getLastExportDate();

        long mils = new Date().getTime();

        //Work around to get the correct company.  Need to find out why the App is not filtering by company.
        BVendorCompany bv = new BVendorCompany(vendor.getOrgGroupId());
        CompanyDetail cd = bv.getAssociatedCompany();
        ArahantSession.getHSU().setCurrentCompany(cd);

        List<HrBenefit> benefitsToDo = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor).list();

		String filename = "";
			try {
				filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (debug ? "TEST" : "PROD") + "_" + (filenames.size() + 1) + ".edi";  //.txt
				edi = new EDI(filename);
			} catch (IOException ex) {
				throw new ArahantException("Can't create EDI file");
			}
			edi.X12_setup('*', '~', '\n');
			isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());

		int benefitCount = 0;
        for (HrBenefit bene : benefitsToDo) {
           
            gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

			String accountNumber = (StringUtils.isEmpty(bene.getGroupAccountId()) ? bv.getAccountNumber() : bene.getGroupAccountId());
			
            header(transactionSetNumber, vendorCode, vendor.getInterchangeReceiverId(), cd.getName(), vendor.getInterchangeSenderId(), gcn, accountNumber); //cd.getName() cd.getFederalEmployerId()

            doInactives(hsu, bene); //deal with history records - policies that have ended
            doActives(hsu, bene);

            transactionSetTrailer(transactionSetNumber);
            transactionSetNumber++;
            functionalGroupTrailer(EDI.makecn(gcn));
            gcn++;

			benefitCount++;
        }
        filenames.add(filename);
		functionInterchangeTrailer(EDI.makecn(icn));
        edi.close();

		try {
			ediLog.close();
		} catch (IOException ex) {
			//Logger.getLogger(EDI834Humana.class.getName()).log(Level.SEVERE, null, ex);
		}
        tran.setTransactionSetNumber(transactionSetNumber - 1);
        tran.setGCN(gcn - 1);
        tran.setICN(icn);

        mils = new Date().getTime() - mils;

        logger.info("took " + (mils / 1000) + " seconds");

        return filenames.toArray(new String[filenames.size()]);
    }

    private void doInactives(HibernateSessionUtil hsu, HrBenefit bene) {
        String benefitName = bene.getPlanName(); //bene.getName();

        int count = 0;

        HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
        hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
        hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
        hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
        hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
        hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

        int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();

        HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();

        List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

        String currentPayingPerson = "";


        while (hscr.next()) {
            if (++count % 50 == 0) {
                logger.info(count);
                //break;
            }

            HrBenefitJoinH bj = hscr.get();

            if (!bj.getPayingPersonId().equals(currentPayingPerson)) {
                Collections.sort(batch);
                for (EDIDumpPersonalInfo i : batch)
                    i.ediMemberLevelDetail();

                batch.clear();
                currentPayingPerson = bj.getPayingPersonId();
            }

            EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);

            info.benefitName = benefitName;

            if (bj.getCoverageEndDate() == 0)
                bj.setCoverageEndDate(bj.getPolicyEndDate());

            try {
                info.setBenefitJoinData(bj, benefitType);
            } catch (Throwable t) {
                //Since this is a history record, it's possible that needed data could have been deleted
                //if so, have to skip the record
                continue;
            }

            info.maintenanceTypeCode = "024";


            batch.add(info);

        }

        Collections.sort(batch);
        for (EDIDumpPersonalInfo i : batch)
            i.ediMemberLevelDetail();

        hscr.close();
    }

    private List<String> doActives(HibernateSessionUtil hsu, HrBenefit bene) {

        int count = 0;
        String benefitName = bene.getName();

        HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
        hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);

        int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();


        HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();


        List<EDIDumpPersonalInfo> batch = new LinkedList<EDIDumpPersonalInfo>();

        String currentPayingPerson = "";

        while (hscr.next()) {

            if (++count % 50 == 0) {
                logger.info(count);
                //break;
            }

            HrBenefitJoin bj = hscr.get();

            if (!bj.getPayingPersonId().equals(currentPayingPerson)) {
                Collections.sort(batch);
                for (EDIDumpPersonalInfo i : batch)
                    i.ediMemberLevelDetail();

                batch.clear();
                currentPayingPerson = bj.getPayingPersonId();
            }

            EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);

            info.benefitName = benefitName;

            info.setBenefitJoinData(bj, benefitType);

            int covdate = bj.getCoverageStartDate();

            int covend = bj.getCoverageEndDate();

            if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                info.maintenanceTypeCode = "021";

            if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                info.maintenanceTypeCode = "024";

            if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                info.maintenanceTypeCode = "024";

            if (covend != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

            if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

            batch.add(info);

            payingPersons.add(bj.getPayingPersonId() + bj.getCoveredPersonId());

        }

        Collections.sort(batch);
        for (EDIDumpPersonalInfo i : batch)
            i.ediMemberLevelDetail();

        hscr.close();

        return payingPersons;
    }

    private void isa_seg(String icn, boolean test, String interchangeSenderId, String interchangeReceiverId) {
        edi.SEG("ISA");
        edi.ST(1, "00");
        edi.SW(2, "", 10);
        edi.ST(3, "00");
        edi.SW(4, "", 10);
        edi.ST(5, "30");
        edi.SW(6, interchangeSenderId, 15); 
        edi.ST(7, "30");
        edi.SW(8, interchangeReceiverId, 15); //"311656473" for EyeMed
        edi.CD(9);
        edi.CT(10);
        edi.CH(11, 'U');
        edi.ST(12, "00401");
        edi.ST(13, icn); //Should be "000000001" for CAS Res -> EyeMed
        edi.CH(14, '1');
        edi.CH(15, test ? 'T' : 'P');
        edi.ST(16, ":");
        edi.EOS();
    }

    private void gs_seg(String gcn, String applicationSenderId, String applicationRecieverId) {
        edi.SEG("GS");
        edi.ST(1, "BE");
        edi.ST(2, stripDashes(applicationSenderId));
        edi.ST(3, stripDashes(applicationRecieverId));
        edi.D8(4);
        edi.CT(5);
        edi.ST(6, removeLeadingZeros(gcn));  //Should be 1 for Cas Res -> EyeMed
        edi.CH(7, 'X');
        edi.ST(8, "004010X095A1");
        edi.EOS();
    }

    private void transactionSetTrailer(int transactionSetNumber) {
        edi.SEG("SE");
        edi.NL(1, edi.X12_numb_segments);
        edi.ST(2, EDI.makecn(transactionSetNumber));
        edi.EOS();
    }

    private void functionalGroupTrailer(String transactionSetNumber) {
        edi.SEG("GE");
        edi.NL(1, edi.X12_numb_st_segments);
        edi.ST(2, removeLeadingZeros(transactionSetNumber));
        edi.EOS();
    }

    private void functionInterchangeTrailer(String icn) {
        edi.SEG("IEA");
        edi.NL(1, 1);
        edi.ST(2, icn);
        edi.EOS();
    }

    private void header(int transactionSetNumber, String vendor, String vendorId, String sponsorName, String sponsorId, int gcn, String accountNo) {
        edi.SEG("ST");
        edi.ST(1, "834");
        edi.ST(2, EDI.makecn(transactionSetNumber));  //Should be "0001" for CAS Res - EyeMed
        edi.EOS();

        edi.SEG("BGN");
        edi.ST(1, "00");
        edi.ST(2, String.format("%06d", gcn));//reference number
        edi.D8(3);
        edi.CT(4);
        edi.ST(5, "");
        edi.ST(6, "");
        edi.ST(7, "");
        edi.ST(8, "4"); // 2 is changes 4 is full file
        edi.EOS();

		if(!StringUtils.isEmpty(accountNo)) {
			edi.SEG("REF");
			edi.ST(1, "38");
			edi.ST(2, accountNo);
			edi.EOS();
		}

//        edi.SEG("DTP");
//        edi.SF(1, "303");
//        edi.ST(2, "D8");
//        int policyStart = 0;
//        if (0==policyStart)  //TODO: find out what to use instead of policyStart
//            logger.info("Policy start date missing");
//        edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.now()));
//        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "P5");
        edi.ST(2, sponsorName);  //was "Detroit Regional Chamber"
        edi.ST(3, "FI");  //was ZZ
        edi.ST(4, stripDashes(sponsorId));    //was "38-2479423"
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "IN");
        edi.ST(2, vendor);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(vendorId));  
        edi.EOS();

		edi.SEG("N1");
        edi.ST(1, "TV");
        edi.ST(2, vendor);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(vendorId));  
        edi.EOS();

//		edi.SEG("ACT");      // Does not seem to be needed by EyeMed
//		edi.ST(1, stripDashes(vendorId));
//		edi.EOS();

        //May not need this
//        edi.SEG("N1");
//        edi.ST(3, "94");
//        edi.ST(4, "");    //TODO: BCBSM agent id
//        edi.EOS();


    }

    public static String removeLeadingZeros(String s) {
        int index = s.lastIndexOf("0");

        if (index >= s.length() - 1)
            return "0";

        return s.substring(index + 1, s.length());
    }

    public static String stripDashes(String s) {
        String ret = "";

        if (s == null)
            return "";

        StringTokenizer toks = new StringTokenizer(s, "-");
        while (toks.hasMoreTokens())
            ret += toks.nextToken();

        return ret;
    }

    public static void main(String[] args) {

		//THIS IS FOR MOVING OVER ALL THE EXTERNAL IDS INTO THE NEW VENDOR GROUP TABLE
//		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().createCriteria(CompanyDetail.class).like(CompanyDetail.NAME, "CAS Resources%").first());
//		ArahantSession.getHSU().beginTransaction();
//		VendorCompany ebc = ArahantSession.getHSU().createCriteria(VendorCompany.class).eq(VendorCompany.NAME, "Employee Benefit Concepts").first();
//		List<OrgGroup> orgGroups = ArahantSession.getHSU().createCriteria(OrgGroup.class)
//									.eq(OrgGroup.OWNINGCOMPANY, ArahantSession.getHSU().getCurrentCompany())
//									.isNotEmpty(OrgGroup.EXTERNAL_REF)
//									.orderBy(OrgGroup.NAME)
//									.list();
//
//		for(OrgGroup orgGroup : orgGroups)
//		{
//			BVendorGroup bvg = new BVendorGroup();
//			bvg.create();
//			bvg.setOrgGroup(orgGroup);
//			bvg.setGroupVendorId(orgGroup.getExternalId());
//			bvg.setVendor(ebc);
//			bvg.insert();
//			System.out.println(orgGroup.getName() + " -- " + orgGroup.getExternalId());
//		}
//
//		ArahantSession.getHSU().commitTransaction();
//		ArahantSession.getHSU().beginTransaction();

		//THIS IS FOR INITIALIZING THE BENEFIT GROUP ACCOUNT IDS QUICKLY
//		List<HrBenefit> benefits =  ArahantSession.getHSU().createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, ebc).list();
//		List<HrBenefit> updates = new ArrayList<HrBenefit>();
//		for(HrBenefit benefit : benefits)
//		{
//			if(benefit.getGroupId().length() == 10)
//			{
//				benefit.setGroupAccountId(benefit.getGroupId().substring(3, benefit.getGroupId().length() - 1));
//				updates.add(benefit);
//				System.out.println(benefit.getName() + " set to " + benefit.getGroupId().substring(3, benefit.getGroupId().length() - 1));
//			}
//		}
//		ArahantSession.getHSU().update(updates);
//		ArahantSession.getHSU().commitTransaction();


//		ArahantSession.getHSU().setCurrentCompany(ArahantSession.getHSU().createCriteria(CompanyDetail.class).like(CompanyDetail.NAME, "CAS Resources%").first());
//
//		ArahantSession.getHSU().beginTransaction();
//		for(HrBenefit bene : ArahantSession.getHSU().createCriteria(HrBenefit.class).like(HrBenefit.NAME, "Blue%").list()) {
//			BHRBenefit bbene = new BHRBenefit(bene);
//			String groupId = bbene.getGroupId();
//			String plan, planName, payerId;
//
//			if(!groupId.isEmpty()) {
//				plan = groupId.replaceAll("MM", "OI").substring(0, groupId.length() - 1).concat("PR");
//				planName = groupId.replaceAll("MM", "OI").substring(0, groupId.length() - 1).concat("YY");
//				payerId = groupId.replaceAll("MM", "OI").substring(0, groupId.length() - 1).concat("CA");
//				System.out.println(plan + " " + planName + " " + payerId);
//
//				bbene.setPlan(plan);
//				bbene.setPlanName(planName);
//				bbene.setPayerId(payerId);
//
//				bbene.update();
//			}
//		}
//		ArahantSession.getHSU().commitTransaction();




//        boolean testingEDI = false;
//
//        logger.info(new Date().getTime()/1000);
//			ArahantSession.getHSU().setCurrentPersonToArahant();
//
//			final BEDITransaction x=new BEDITransaction();
//			x.create();
//
//			x.setReceiver("00001-0000072670");
//			x.setStatus(0);
//			x.setStatusDesc("Started");
//
//			//x.insert();
//
//			ArahantSession.getHSU().commitTransaction();
//			ArahantSession.getHSU().beginTransaction();
//			ArahantSession.multipleCompanySupport = true;
//
//
//			x.send834Insurance();




		

		//EDI834EyeMed ed = new EDI834EyeMed();
		//ed.dumpEDI(new BVendorCompany("00001-0000000167").getBean(),fds,"381791480");
				//"62-6000913","62-6000913","62-6000913","62-6000913",
				//"076334622      ","076334622 ");


//        File logFile = new File("BCN_EDI_LOG.txt");
//
//        try {
//            boolean fileCreated = logFile.createNewFile();
//
//            if (fileCreated) {
//                BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
//                out.write("New File Test" + "\n");
//                out.flush();
//                out.close();
//            } else {
//                BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
//                out.write("Append Test" + "\n");
//                out.flush();
//                out.close();
//            }
//
//        } catch (IOException ex) {
//            Logger.getLogger(EDI834BCN.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
}
