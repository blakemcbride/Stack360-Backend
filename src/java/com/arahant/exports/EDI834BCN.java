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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EDI834BCN {

	public EDI834BCN(String fileType)
	{
		this.fileType = fileType;
	}

	private String fileType = "F";
    private static final ArahantLogger logger = new ArahantLogger(EDI834BCN.class);

    //File logFile = new File(FileSystemUtils.getWorkingDirectory() + File.separator + "Logfiles/BCN_EDI_LOG.txt");  //TODO: put this somewhere for clients
    EDILog ediLog = new EDILog("BCN_EDI");
    private static String vendorCode = "Blue Care Network";
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
        private String groupNumber;
        private String subGroupNumber;
        private String benefitClassId;
        private boolean hasSpouse;
        private boolean hasMedicare;
        private char smoker;
        private boolean subscriber;

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
            benefitClassId = bhr.getInsuranceCode();

            if (groupNumber == null)
                groupNumber = "";

            if (subGroupNumber == null)
                subGroupNumber = "";

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
            edi.ST(9, (studentStatus == null) ? "N" : studentStatus);
            edi.ST(10, (handicap == null) ? "N" : handicap);
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
            edi.SF(2, groupNumber);
            edi.EOS();
        }

        public void REF_subscriber_subgroup_number() {
            edi.SEG("REF");
            edi.SF(1, "DX");
            edi.SF(2, subGroupNumber);
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
            if (!isEmpty(memberMiddleName))
                edi.SF(5, memberMiddleName);
            else
                edi.SF(5, "");
            edi.SF(6, "");
            edi.SF(7, "");

            if (!"000000000".equals(stripDashes(depSSN))) {
                edi.SF(8, "34");
                edi.SF(9, stripDashes(depSSN));
            } else {
                edi.SF(8, "");
                edi.SF(9, "");
            }

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
            if (memberAddressLine2 != null && !memberAddressLine2.equals(""))
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
            if (subscriber)
                edi.ST(4, maritalStatusCode);
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
                case HrBenefitCategory.DENTAL:
                    edi.SF(3, "DEN");  //Should be D for dental
                    edi.SF(4, benefitName); //30 for Dental
                    break;
                case HrBenefitCategory.HEALTH:
                    edi.SF(3, "HMO");
                    edi.SF(4, benefitName);
                    break;
                case HrBenefitCategory.LONG_TERM_CARE:
                    edi.SF(3, "LTC");
                    edi.SF(4, benefitName);
                    break;
                case HrBenefitCategory.SHORT_TERM_CARE:
                    edi.SF(3, "STC");
                    edi.SF(4, benefitName);
                    break;
                case HrBenefitCategory.VISION:
                    edi.SF(3, "VIS");
                    edi.SF(4, benefitName);
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

        public void DTP_benefit_start_date() {

			//on change files, dont send start date for terminations
			if(fileType.equals("C") && "024".equals(maintenanceTypeCode))
				return;

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

        public void ediMemberLevelDetail() {
            //Check for missing data here and write to log if there is
            if (allDataValid())
            {
                INS_insured_benefit();
                REF_SSN_subscriber();
                REF_subscriber_group_number();
                REF_subscriber_class();
                REF_subscriber_subgroup_number();
                DTP_employment_date();
                DTP_employment_term_date();
                NM1_member_name();
                PER_contact_info();
                N3_address_information();
                N4_geographic_location();
                DMG_demographic_info();
                HD_health_coverage();
                DTP_benefit_start_date();
                DTP_benefit_end_date();

                //TODO: may not need these
                //            INS_maintenance_reason_code();
                //            REF_medicare_HIC_number();
                //            HLH_health_information();
                //            DSB_disability_info();
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

        for (HrBenefit bene : benefitsToDo) {
            String filename = "";
            try {
                filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (filenames.size() + 1) + ".edi.txt";
                edi = new EDI(filename);
            } catch (IOException ex) {
                throw new ArahantException("Can't create EDI file");
            }
            edi.X12_setup('*', '~', '\n');
            isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());
            gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

            header(transactionSetNumber, vendorCode, vendor.getInterchangeSenderId(), cd.getName(), vendor.getInterchangeReceiverId(), gcn); //cd.getFederalEmployerId()

			if(fileType.equals("F"))
			{
				doInactives(hsu, bene); //deal with history records - policies that have ended
				doActives(hsu, bene);
			}
			else //change file!, handle very differently
			{
				doChanges(hsu, bene);
			}

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
        tran.setTransactionSetNumber(transactionSetNumber - 1);
        tran.setGCN(gcn - 1);
        tran.setICN(icn);

        mils = new Date().getTime() - mils;

        logger.info("took " + (mils / 1000) + " seconds");

        return filenames.toArray(new String[filenames.size()]);
    }

    private void doInactives(HibernateSessionUtil hsu, HrBenefit bene) {
        String benefitName = bene.getName();

        int count = 0;

        HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
		hcu.orderByDesc(HrBenefitJoinH.RECORD_CHANGE_DATE);
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

			if(this.fileType.equals("F"))
			{
				if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					info.maintenanceTypeCode = "021";

				if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					info.maintenanceTypeCode = "024";

				if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
					info.maintenanceTypeCode = "024";
			}
			else
			{
				if(bj.getRecordChangeType() == 'N')
					info.maintenanceTypeCode = "021";
				if(bj.getRecordChangeType() == 'M')
					info.maintenanceTypeCode = "001";
				if(bj.getRecordChangeType() == 'D')
					info.maintenanceTypeCode = "024";
			}

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


	private void doChanges(HibernateSessionUtil hsu, HrBenefit bene) {
		HashMap<String,List<IHrBenefitJoin>> coveredPersonMap = new HashMap<String, List<IHrBenefitJoin>>();
		String benefitName = bene.getName();
		int benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();



		//get actives in order of most recent first
        HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class);
		hcu.orderByDesc(HrBenefitJoin.HISTORY_DATE);
		hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
		hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.gt(HrBenefitJoin.HISTORY_DATE, lastDumpDate);

		HibernateScrollUtil<HrBenefitJoin> scr = hcu.scroll();

		//get history records most recent first
		HibernateCriteriaUtil<HrBenefitJoinH> hcu2 = hsu.createCriteria(HrBenefitJoinH.class);
		hcu2.orderByDesc(HrBenefitJoinH.RECORD_CHANGE_DATE);
		hcu2.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
		hcu2.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu2.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
		hcu2.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
		hcu2.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

		HibernateScrollUtil<HrBenefitJoinH> hscr = hcu2.scroll();

		//We need to go through all actives and inactives and group them together by covered person
		while(scr.next()) //go thru actives
		{
			HrBenefitJoin bj = scr.get();
			if(coveredPersonMap.containsKey(bj.getCoveredPersonId()))
			{
				List<IHrBenefitJoin> tempList = coveredPersonMap.get(bj.getCoveredPersonId());
				tempList.add(bj);
				coveredPersonMap.put(bj.getCoveredPersonId(), tempList);
			}
			else
			{
				List<IHrBenefitJoin> tempList = new ArrayList<IHrBenefitJoin>();
				tempList.add(bj);
				coveredPersonMap.put(bj.getCoveredPersonId(), tempList);
			}
		}

		while(hscr.next()) //did we finish actives first?  Finish out inactives
		{
			HrBenefitJoinH hbj = hscr.get();
			if(coveredPersonMap.containsKey(hbj.getCoveredPersonId()))
			{
				List<IHrBenefitJoin> tempList = coveredPersonMap.get(hbj.getCoveredPersonId());
				tempList.add(hbj);
				coveredPersonMap.put(hbj.getCoveredPersonId(), tempList);
			}
			else
			{
				List<IHrBenefitJoin> tempList = new ArrayList<IHrBenefitJoin>();
				tempList.add(hbj);
				coveredPersonMap.put(hbj.getCoveredPersonId(), tempList);
			}
		}

		int count = 0;
		Iterator i = coveredPersonMap.entrySet().iterator();
	    while (i.hasNext())
		{
			if (++count % 50 == 0) {
                logger.info(count);
            }

			Map.Entry entry = (Map.Entry)i.next();

			List<IHrBenefitJoin> joins = (List<IHrBenefitJoin>)entry.getValue();
			//do they have multiple joins for a single covered person?
			if(joins.size() > 1)
			{
				System.out.println(joins.get(0).getCoveredPerson().getNameFL() + "-" + joins.get(0).getCoveredPersonId() + "- has multiple joins.  Filtering to the most recent.");
				Date latestDate = null;
				char latestIbjType = ' ';
				IHrBenefitJoin latestJoin = null;
				boolean hasAdd = false;
				boolean hasEdit = false;
				boolean hasDelete = false;
				//for each join for a single covered person
				for(IHrBenefitJoin ibj : joins)
				{
					//save the latest in the group, because thats what we write the fields with
					if(latestDate == null)
					{
						latestDate = ibj.getRecordChangeDate();
						latestIbjType = ibj.getRecordChangeType();
						latestJoin = ibj;
					}
					else if(latestDate.before(ibj.getRecordChangeDate()))
					{
						latestDate = ibj.getRecordChangeDate();
						latestIbjType = ibj.getRecordChangeType();
						latestJoin = ibj;
					}
					//but we have to determine the
					if(ibj.getRecordChangeType() == 'N')
						hasAdd = true;
					if(ibj.getRecordChangeType() == 'M')
						hasEdit = true;
					if(ibj.getRecordChangeType() == 'D')
						hasDelete = true;
				}
				EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);
				info.benefitName = benefitName;
				info.setBenefitJoinData(latestJoin, benefitType);
				if(hasAdd && !hasEdit && !hasDelete)
				{
					info.maintenanceTypeCode = "021";
				}
				else if((hasAdd && hasEdit && hasDelete) || (hasAdd && !hasEdit && hasDelete) || (!hasAdd && hasEdit && hasDelete && latestIbjType == 'M'))
				{
					info.maintenanceTypeCode = "001";
				}
				else if(!hasAdd && hasEdit && hasDelete && latestIbjType == 'D')
				{
					info.maintenanceTypeCode = "024";
				}
				else
				{
					info.maintenanceTypeCode = "001";
				}
				info.ediMemberLevelDetail();
			}
			else if(joins.size() == 1)
			{
				IHrBenefitJoin ibj = joins.get(0);
				EDIDumpPersonalInfo info = new EDIDumpPersonalInfo(edi);
				info.benefitName = benefitName;
				info.setBenefitJoinData(ibj, benefitType);
				if(ibj.getRecordChangeType() == 'N')
					info.maintenanceTypeCode = "021";
				if(ibj.getRecordChangeType() == 'M')
					info.maintenanceTypeCode = "001";
				if(ibj.getRecordChangeType() == 'D')
					info.maintenanceTypeCode = "024";
				info.ediMemberLevelDetail();
			}

	    }
	}

    private void isa_seg(String icn, boolean test, String interchangeSenderId, String interchangeReceiverId) {
        edi.SEG("ISA");
        edi.ST(1, "00");
        edi.SW(2, "", 10);
        edi.ST(3, "00");
        edi.SW(4, "", 10);
        edi.ST(5, "ZZ");
        edi.SW(6, interchangeSenderId, 15); //"382479423" interchangeSenderId
        edi.ST(7, "ZZ");
        edi.SW(8, interchangeReceiverId, 15); //"382069753" interchangeReceiverId
        edi.CD(9);
        edi.CT(10);
        edi.CH(11, 'U');
        edi.ST(12, "00401");
        edi.ST(13, icn);
        edi.CH(14, '0');
        edi.CH(15, test ? 'T' : 'P');
        edi.ST(16, ":");
        edi.EOS();
    }

    private void gs_seg(String gcn, String applicationSenderId, String applicationRecieverId) {
        edi.SEG("GS");
        edi.ST(1, "BE");
        edi.ST(2, stripDashes(applicationSenderId));  //applicationSenderId "382479423"
        edi.ST(3, stripDashes(applicationRecieverId));  //applicationRecieverId "382069753"
        edi.D8(4);
        edi.CT(5);
        edi.ST(6, removeLeadingZeros(gcn));
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

    private void header(int transactionSetNumber, String vendor, String vendorId, String sponsorName, String sponsorId, int gcn) {
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
        edi.ST(8, "2"); // BCN says put a 2 here even if full file, 2 is changes 4 is full file
        edi.EOS();

        edi.SEG("REF");
        edi.ST(1, "38");
        edi.ST(2, "HMO");
        edi.EOS();

        edi.SEG("DTP");
        edi.SF(1, "303");
        edi.ST(2, "D8");
//        int policyStart = 0;
//        if (0==policyStart)  //TODO: find out what to use instead of policyStart
//            logger.info("Policy start date missing");
        edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.now()));
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "P5");
        edi.ST(2, sponsorName);  //was "Detroit Regional Chamber"
        edi.ST(3, "ZZ");  //was FI
        edi.ST(4, stripDashes(sponsorId));    //was "38-2479423"
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "IN");
        edi.ST(2, vendor);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(vendorId));  //was "382069753"
        edi.EOS();

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
        boolean testingEDI = false;

        if (testingEDI) {
            logger.info(new Date().getTime() / 1000);

            //String vendorId = "00001-0000072600";  //Blue Care Network DRC
            String vendorId = "00001-0000072577";  //BCN New Passages
            HibernateSessionUtil hsu = ArahantSession.getHSU();

//            hsu.beginTransaction();
//            BProperty bp = new BProperty("BCNID");
//            bp.setValue(vendorId);
//            bp.update();
//            hsu.commitTransaction();


            //ArahantSession.getHSU().setCurrentPerson(ArahantSession.getHSU().get(Person.class, personId));
            //hsu.setCurrentPersonToArahant();
            //hsu.setCurrentCompany(new BCompany("00000-0000000005").getBean());  //DRC

            hsu.setCurrentPerson(new BPerson("00001-0000389211").getPerson());
            hsu.setCurrentCompany(new BCompany("00001-0000072555").getBean());  //New Passages

            final BEDITransaction x = new BEDITransaction();
            x.create();

            x.setReceiver(vendorId);
            x.setStatus(0);
            x.setStatusDesc("Started");

            hsu.beginTransaction();

            x.sendExport();

            hsu.commitTransaction();

            hsu.close();
        }

        File logFile = new File("BCN_EDI_LOG.txt");

        try {
            boolean fileCreated = logFile.createNewFile();

            if (fileCreated) {
                BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
                out.write("New File Test" + "\n");
                out.flush();
                out.close();
            } else {
                BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
                out.write("Append Test" + "\n");
                out.flush();
                out.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(EDI834BCN.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
