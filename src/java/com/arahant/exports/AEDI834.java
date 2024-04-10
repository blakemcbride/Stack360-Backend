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
import com.arahant.edi.EDI;
import com.arahant.lisp.LispPackage;
import com.arahant.utils.*;
import org.kissweb.StringUtils;

import java.io.IOException;
import java.util.*;

public abstract class AEDI834 {
	protected HibernateSessionUtil hsu = ArahantSession.getHSU();
	protected EDI edi;
	protected FixedLengthFileWriter fw;
	protected String fileType = "F";
	protected String coverageEndDate;
	protected String frequencyCode = "U";
	protected String raceOrEthnicity = "7";
	protected int memberBirthDate;
	protected String memberAddressZip;
	protected String memberAddressState;
	protected String memberAddressLine1;
	protected String workPhone;
	protected String homePhone;
	protected String cellPhone;
	protected String memberMiddleName;
	protected String memberFirstName;
	protected String memberLastName;
	protected String memberAddressCity;
	protected String maritalStatusCode;
	protected String coverageStartDate;
	protected String citizenStatusCode = "1";
	protected String entityIdentifierCode = "I";
	protected String insuranceLineCode;
	protected String coverageLevelCode;
	protected String genderCode;
	protected String memberAddressLine2;
	protected String statusInformationEffectiveDate;
	protected String subscriberIdentifier;
	protected String hicNumber;
	protected String dateTimeQualifier;
	protected String handicap;
	protected String studentStatus;
	protected String relationshipCode;
	protected String benefitStatusCode;
	protected String employmentCode;
	protected int employmentStartDate;
	protected int employmentEndDate;
	protected int retirementDate;
	protected String depSSN;
	protected String maintenanceTypeCode = "030";
	protected int benefitType;
	protected String benefitName;
	protected String personId;
	protected String primaryCarePhysician;
	protected String changeReason;
	protected int policyStart;
	protected int policyEnd;
	protected String benefitClassId;
	protected boolean hasSpouse;
	protected boolean hasMedicare;
	protected String medicareType = "D";
	protected char smoker;
	protected boolean subscriber = true;
	protected int coveredAge;
	protected int payingAge;
	protected String ti;
	protected String groupAccountId;
	protected String groupVendorId;
	protected String groupNumber;
	protected String subGroupNumber;
	protected String plan = "";
	protected String planName;
	protected String payerId;
	protected String ediFieldValue1;
	protected String ediFieldValue2;
	protected String ediFieldValue3;
	protected String ediFieldValue4;
	protected String ediFieldValue5;
	protected String branch = "";
	protected String vendorCode = "";
	protected LispPackage lp = null;
	protected EDILog ediLog;
	protected boolean newLog;
	protected static ArahantLogger logger = new ArahantLogger(AEDI834.class);
	protected List<String> payingPersons = new LinkedList<String>();
	protected Date lastDumpDate;
	protected String memberExtRef = "";
	protected double salaryAmount;
	protected int salaryEffectiveDate;
	protected String salaryPeriod = "A";
	protected String account = "";
	protected DependentComparator comparator = new DependentComparator();
	protected int eligibilityStartDate;
	protected boolean employeeCovered = true;

	protected void flushData() {
		coverageEndDate = "";
		frequencyCode = "U";
		raceOrEthnicity = "7";
		memberBirthDate = 0;
		memberAddressZip = "";
		memberAddressState = "";
		memberAddressLine1 = "";
		workPhone = "";
		homePhone = "";
		cellPhone = "";
		memberMiddleName = "";
		memberFirstName = "";
		memberLastName = "";
		memberAddressCity = "";
		maritalStatusCode = "";
		coverageStartDate = "";
		coverageLevelCode = "";
		citizenStatusCode = "1";
		insuranceLineCode = "";
		genderCode = "";
		memberAddressLine2 = "";
		statusInformationEffectiveDate = "";
		subscriberIdentifier = "";
		hicNumber = "";
		dateTimeQualifier = "";
		handicap = "";
		studentStatus = "";
		relationshipCode = "";
		benefitStatusCode = "";
		employmentCode = "";
		employmentStartDate = 0;
		employmentEndDate = 0;
		retirementDate = 0;
		depSSN = "";
		maintenanceTypeCode = "030";
		personId = "";
		primaryCarePhysician = "";
		medicareType = "D";
		smoker = 'N';
		subscriber = false;
		coveredAge = 0;
		memberExtRef = "";
		salaryAmount = 0;
		salaryPeriod = "A";
		salaryEffectiveDate = 0;
		eligibilityStartDate = 0;
		benefitClassId = "";
		employeeCovered = true;
	}

	protected void ediMemberLevelDetail() {
		INS_insured_benefit();
		REF_SSN_subscriber();
		REF_HIC_number();
		REF_subscriber_group_number();
		REF_subscriber_class();
		REF_subscriber_subgroup_number();
		IDC_identification_card();
		DTP_employment_date();
		DTP_employment_term_date();
		DTP_retirement_date();
		NM1_member_name();
		PER_contact_info();
		N3_address_information();
		N4_geographic_location();
		DMG_demographic_info();
		HD_health_coverage();
		DTP_benefit_start_date();
		DTP_benefit_end_date();
	}

	protected void setBenefitJoinData(IHrBenefitJoin bj, int benefitType) {
		BPerson p = new BPerson(bj.getPayingPersonId());
		BEmployee emp = null;
		if (p.isEmployee())
			emp = new BEmployee(bj.getPayingPersonId());
		BPerson d = new BPerson(bj.getCoveredPersonId());
		String benefitId = bj.getHrBenefitConfig().getHrBenefit().getBenefitId();

		setLocation(bj);
		setRelationshipCode(bj);
		setPCP(bj);
		setEmploymentCode(bj, emp.getPersonId());
		setCoverageLevel(bj);
		setPayingPerson(bj.getPayingPersonId(), benefitId);
		setCoveredPerson(bj.getCoveredPersonId());
		setAddress(p, d);
		setMaritalStatus(emp.getPersonId());
		setDates(bj);

		HrBenefitConfig hbc = hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId());
		insuranceLineCode = hbc.getHrBenefit().getInsuranceCode();
		benefitStatusCode = (bj.getUsingCOBRA() == 'Y') ? "C" : "A";
		changeReason = bj.getChangeDescription();
		benefitName = hbc.getHrBenefit().getName();
		this.benefitType = benefitType;
	}

	protected void setLocation(IHrBenefitJoin bj) {
		Person p = new BPerson(bj.getPayingPersonId()).getPerson();

		if (bj.getUsingCOBRA() == 'Y') {
			//if this is a dependent, fake the location
			if (p.getOrgGroupAssociations().isEmpty()) {
				HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, bj.getPayingPerson()).first();
				if (dep != null) {
					String ogid = dep.getEmployee().getOrgGroupAssociations().iterator().next().getOrgGroupId();
					ArahantSession.AICmd("(assert (org_group_association (person_id \"" + bj.getPayingPersonId() + "\")(org_group_id \"" + ogid + "\")))");

				}
			}
		}
	}

	protected void setRelationshipCode(IHrBenefitJoin bj) {
		relationshipCode = "18";
		if (!bj.getPayingPersonId().equals(bj.getCoveredPersonId())) {
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

		if(employeeCovered)
			subscriber = bj.getPayingPersonId().equals(bj.getCoveredPersonId());
		else
			subscriber = true;
	}

	protected void setPCP(IHrBenefitJoin bj) {
		if (bj.getComments() == null || "".equals(bj.getComments().trim()))
			primaryCarePhysician = null;
		else
			primaryCarePhysician = bj.getComments().trim();
	}

	protected void setEmploymentCode(IHrBenefitJoin bj, String empId) {
		BEmployee emp = new BEmployee(empId);
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
				else if (s.indexOf("retire") != -1)
					employmentCode = "RT";

				employmentStartDate = emp.getEmploymentDate();
			}
		}
		if (employmentCode.equals("TE")) {
//			maintenanceTypeCode = "024";

			try {
				if(emp.getLastInactiveStatusHistory() == null)
					employmentStartDate = emp.getFirstActiveStatusHistory().getEffectiveDate();
				else {
					int lastTerm = emp.getLastInactiveStatusHistory().getEffectiveDate();
					employmentStartDate = hsu.createCriteria(HrEmplStatusHistory.class)
											 .eq(HrEmplStatusHistory.EMPLOYEE, emp.getEmployee())
											 .gt(HrEmplStatusHistory.EFFECTIVEDATE, lastTerm)
											 .orderBy(HrEmplStatusHistory.EFFECTIVEDATE)
											 .joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS)
											 .eq(HrEmployeeStatus.ACTIVE, 'Y')
											 .first()
											 .getEffectiveDate();
				}
			}
			catch (Exception e) {
				try {
					employmentStartDate = emp.getEmploymentDate();
				}
				catch (Exception x) {}
			}

			try {
				employmentEndDate = emp.getLastStatusDate();
			}
			catch (Exception e) {}
		}
		if(employmentCode.equals("RT")) {
			try {
					if(emp.getLastInactiveStatusHistory() == null)
						employmentStartDate = emp.getFirstActiveStatusHistory().getEffectiveDate();
					else {
						int lastTerm = emp.getLastInactiveStatusHistory().getEffectiveDate();
						employmentStartDate = hsu.createCriteria(HrEmplStatusHistory.class)
												 .eq(HrEmplStatusHistory.EMPLOYEE, emp.getEmployee())
												 .gt(HrEmplStatusHistory.EFFECTIVEDATE, lastTerm)
												 .orderBy(HrEmplStatusHistory.EFFECTIVEDATE)
												 .joinTo(HrEmplStatusHistory.HREMPLOYEESTATUS)
												 .eq(HrEmployeeStatus.ACTIVE, 'Y')
												 .first()
												 .getEffectiveDate();
					}
				}
				catch (Exception e) {
					try {
						employmentStartDate = emp.getEmploymentDate();
					}
					catch (Exception x) {}
				}

				try {
					retirementDate = emp.getLastStatusDate();
				}
				catch (Exception e) {}
		}
	}

	protected void setPayingPerson(String payingPersonId, String benefitId) {
		Person payingPerson = new BPerson(payingPersonId).getPerson();
		subscriberIdentifier = payingPerson.getUnencryptedSsn();

		if ("999-99-9999".equals(subscriberIdentifier))
			subscriberIdentifier = "000-00-0000";

		BHRBenefit bhr = new BHRBenefit(benefitId);

		groupNumber = bhr.getGroupId();
		subGroupNumber = bhr.getSubGroupId();
		groupAccountId = bhr.getGroupAccountId();
		groupAccountId = groupAccountId == null? "" : groupAccountId;
        benefitClassId = bhr.getCoveredUnderCOBRA() ? "C001" : "A001";

		BEmployee be = new BEmployee(payingPerson.getPersonId());
		medicareType = be.getMedicare() + "";
		if(medicareType.equals("2"))
			medicareType = "C";
		else if(medicareType.equals("U"))
			medicareType = "D";
		else if(medicareType.equals("N"))
			medicareType = "E";
		if(employeeCovered)
			hicNumber = isEmpty(payingPerson.getHicNumber()) ? "" : payingPerson.getHicNumber();

//		benefitClassId = bhr.getInsuranceCode();
		plan = bhr.getPlan();
		planName = bhr.getPlanName();
		payerId = bhr.getPayerId();
		ediFieldValue1 = bhr.getEdiFieldValue1();
		ediFieldValue2 = bhr.getEdiFieldValue2();
		ediFieldValue3 = bhr.getEdiFieldValue3();
		ediFieldValue4 = bhr.getEdiFieldValue4();
		ediFieldValue5 = bhr.getEdiFieldValue5();

		Set<OrgGroupAssociation> ogaSet = payingPerson.getOrgGroupAssociations();
		if(ogaSet != null && ogaSet.size() > 0) {
			BOrgGroup bog = new BOrgGroup();
			if(ogaSet != null)
				bog = new BOrgGroup(ogaSet.iterator().next().getOrgGroupId());
			
			VendorGroup vg = ArahantSession.getHSU().createCriteria(VendorGroup.class)
													.eq(VendorGroup.VENDOR, bhr.getBean().getProvider())
													.eq(VendorGroup.ORG_GROUP, bog.getOrgGroup())
													.first();
			if(vg != null)
				groupVendorId = vg.getGroupVendorId();
			else {
				groupVendorId = groupAccountId;
				try {
					ediLog.write(payingPerson.getNameFML() + " does not have a group vendor ID.");
				}
				catch (IOException ex) {
					logger.info("Was not able to write error to EDI log.");
				}
			}
		}
		else
		{
			groupVendorId = groupAccountId;
			try {
				ediLog.write(payingPerson.getNameFML() + " does not have a group vendor ID.");
				ediLog.write(payingPerson.getNameFL() + " (" + subscriberIdentifier + ") has no org group association." + "\n");
			} catch (IOException ex) {
				logger.info("Was not able to write error to EDI log.");
			}
		}

		if (groupNumber == null)
			groupNumber = "";

//		if(benefitClassId == null) {
		if(new BPerson(payingPerson).isEmployee()) {
			memberExtRef = new BPerson(payingPerson).getBEmployee().getExtRef();
			salaryAmount = new BPerson(payingPerson).getWageAmount();
			if(!isEmpty(new BPerson(payingPerson).getWageTypeId()))
				if(new BWageType(new BPerson(payingPerson).getWageTypeId()).getPeriodType() == 1)
					salaryPeriod = "H";
			if(new BEmployee(new BPerson(payingPerson)).getCurrentWageBean() != null)
				salaryEffectiveDate = new BHRWage(new BEmployee(new BPerson(payingPerson)).getCurrentWageBean()).getEffectiveDate();
//			}
		}

		payingAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
	}

	protected void setCoveredPerson(String coveredPersonId) {
		Person coveredPerson = hsu.createCriteria(Person.class).eq(Person.PERSONID, coveredPersonId).first();

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
		coveredAge = new BPerson(coveredPerson).getAgeAsOf(DateUtils.now());

		if(!employeeCovered) {
			subscriberIdentifier = coveredPerson.getUnencryptedSsn();
			hicNumber = isEmpty(coveredPerson.getHicNumber()) ? "" : coveredPerson.getHicNumber();
		}
	}

	protected void setAddress(BPerson p, BPerson dep) {
		if (!isEmpty(dep.getMobilePhone()))
			cellPhone = dep.getMobilePhone();
		else
			cellPhone = p.getMobilePhone();

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

	protected void setMaritalStatus(String empId) {
		BEmployee emp = new BEmployee(empId);
		maritalStatusCode = "S";

		if (emp != null)
			if (emp.getActiveSpouse(DateUtils.now()) != null)
				maritalStatusCode = "M";
			else if (emp.getActiveSpouse(0) != null)
				maritalStatusCode = "U";
	}

	protected void setCoverageLevel(IHrBenefitJoin bj) {
		coverageLevelCode = "EMP";

		HrBenefitConfig config = bj.getHrBenefitConfig();

		if((config.getSpouseEmployee() == 'Y' ||
		   config.getSpouseNonEmployee() == 'Y') &&
		   config.getMaxChildren() == 1)
			coverageLevelCode = "ESP";
		if(((config.getSpouseNonEmpOrChildren() == 'Y') ||
			(config.getSpouseEmpOrChildren() == 'Y')))
			if(config.getMaxChildren() > 1 || config.getMaxChildren() == 0)
				coverageLevelCode = "FAM";
			else if(config.getMaxChildren() == 1)
				coverageLevelCode = "ESP";
		if(config.getChildren() == 'Y' && config.getMaxChildren() == 1)
				coverageLevelCode = "ECH";
	}

//	protected void setCoverageLevel(IHrBenefitJoin bj) {
//		coverageLevelCode = "EMP";
//
//		if (bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' ||
//				bj.getHrBenefitConfig().getSpouseNonEmployee() == 'Y')
//			coverageLevelCode = "ESP";
//
//		if ((bj.getHrBenefitConfig().getSpouseNonEmpOrChildren() == 'Y') ||
//				(bj.getHrBenefitConfig().getSpouseEmpOrChildren() == 'Y') ||
//				(bj.getHrBenefitConfig().getChildren() == 'Y'))
//			coverageLevelCode = "FAM";
//
//		if (bj.getHrBenefitConfig().getMaxChildren() == 1)
//			coverageLevelCode = "ESP";
//
//		if(bj.getCoveredPerson() != bj.getPayingPerson()) {
//			BHREmplDependent dep = new BHREmplDependent(bj.getPayingPersonId(), bj.getCoveredPersonId());
//			if (bj.getHrBenefitConfig().getChildren() == 'Y' && bj.getHrBenefitConfig().getMaxChildren() == 1 &&
//				bj.getHrBenefitConfig().getSpouseEmployee() == 'Y' )
//			{
//				if(dep.getRelationshipType().equals("C"))
//					coverageLevelCode = "ECH";
//				else if(dep.getRelationshipType().equals("S"))
//					coverageLevelCode = "ESP";
//				else
//					coverageLevelCode = "FAM";
//			}
//		}
//	}

	protected void setDates(IHrBenefitJoin bj) {
		dateTimeQualifier = "300";
		if (bj.getUsingCOBRA() == 'Y')
			dateTimeQualifier = "340";
		statusInformationEffectiveDate = DateUtils.getDateCCYYMMDD(bj.getPolicyStartDate());
		int covdate = bj.getCoverageStartDate();
		coverageStartDate = DateUtils.getDateCCYYMMDD(covdate);
		policyStart = bj.getPolicyStartDate();
		policyEnd = bj.getPolicyEndDate();
		if (coverageStartDate.equals("") && bj.getCoverageEndDate() == 0)
			logger.info("coverage start date missing");
		int covend = bj.getCoverageEndDate();
		if (covend != 0)
			covend = DateUtils.addDays(covend, 1);
		coverageEndDate = DateUtils.getDateCCYYMMDD(covend);
		HrBenefit bene = new BHRBenefitConfig(bj.getHrBenefitConfigId()).getBenefit();

		switch(bene.getEligibilityType()) {
			case 1: eligibilityStartDate = employmentStartDate; break;
			case 2: eligibilityStartDate = DateUtils.addDays(DateUtils.endOfMonth(DateUtils.addDays(employmentStartDate, bene.getEligibilityPeriod())),1); break;
			case 3: eligibilityStartDate = DateUtils.addDays(DateUtils.endOfMonth(employmentStartDate),1); break;
			default: break;
		}
	}

	protected boolean isEmpty(String s) {
		return s == null || s.trim().equals("");
	}

	@Override
	public String toString() {
		return memberLastName + ", " + memberFirstName + "\n";
	}

	protected boolean allDataValid() {
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

	protected void isa_seg(String icn, boolean test, String interchangeSenderId, String interchangeReceiverId) {
        edi.SEG("ISA");
        edi.ST(1, "00");
        edi.SW(2, "", 10);
        edi.ST(3, "00");
        edi.SW(4, "", 10);
        edi.ST(5, "ZZ");
        edi.SW(6, interchangeSenderId, 15); //"382479423" interchangeSenderId
        edi.ST(7, "20");
        edi.SW(8, interchangeReceiverId, 15); //"382069753" interchangeReceiverId
        edi.CD(9);
        edi.CT(10);
        edi.CH(11, 'U');
        edi.ST(12, "00401");
        edi.ST(13, icn);
        edi.CH(14, '1');
        edi.CH(15, test ? 'T' : 'P');
        edi.ST(16, ">");
        edi.EOS();
    }

    protected void gs_seg(String gcn, String applicationSenderId, String applicationRecieverId) {
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

    protected void header(int transactionSetNumber, String vendor, String vendorId, String sponsorName, String sponsorId, int gcn) {
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
        edi.ST(8, "4"); //2 change file, 4 full file
        edi.EOS();

//        edi.SEG("REF");
//        edi.ST(1, "38");
//        edi.ST(2, groupNumber);
//        edi.EOS();

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
        edi.ST(2, sponsorName);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(sponsorId));
        edi.EOS();

        edi.SEG("N1");
        edi.ST(1, "IN");
        edi.ST(2, vendor);
        edi.ST(3, "FI");
        edi.ST(4, stripDashes(vendorId));
        edi.EOS();

//		edi.SEG("N1");
//        edi.ST(1, "TV");
//        edi.ST(2, vendor);
//        edi.ST(3, "FI");
//        edi.ST(4, stripDashes(vendorId));
//        edi.EOS();
    }

	protected void INS_insured_benefit() {
		if(employeeCovered)
			subscriber = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));
		else
			subscriber = true;

		edi.SEG("INS");

		edi.ST(1, subscriber ? "Y" : "N");
		edi.ST(2, relationshipCode);

		edi.ST(3, maintenanceTypeCode); //for principal life

		edi.ST(4, "XN"); //means not giving reason for change

		edi.ST(5, benefitStatusCode);
		edi.ST(6, medicareType);
		if (benefitStatusCode.equals("C")) {
			String code = "1";
//                if (changeReason.trim().startsWith("Dependent Ineligible"))
//                    code = "7";
//                if (changeReason.trim().equalsIgnoreCase("Divorce"))
//                    code = "5";
//                if (changeReason.trim().equalsIgnoreCase("Part Time"))
//                    code = "2";
//                if (changeReason.trim().startsWith("Terming"))
//                    code = "1";
//                if (changeReason.trim().equalsIgnoreCase("Death"))
//                    code = "4";
			edi.ST(7, code);
		} else
			edi.ST(7, "");
		edi.ST(8, employmentCode);
		edi.ST(9, (studentStatus == null) ? "N" : studentStatus);
		edi.ST(10, (handicap == null) ? "N" : handicap);
		edi.EOS();
	}

	protected void DTP_employment_date() {
		if (employmentStartDate != 0) {
			edi.SEG("DTP");
			edi.SF(1, "336");
			edi.ST(2, "D8");
			edi.DC(3, employmentStartDate);
			edi.EOS();
		}
	}

	protected void DTP_employment_term_date() {
		if (employmentEndDate != 0) {
			edi.SEG("DTP");
			edi.SF(1, "337");
			edi.ST(2, "D8");
			edi.DC(3, employmentEndDate);
			edi.EOS();
		}
	}

	protected void DTP_eligibility_date() {
		if (employmentStartDate != 0) {
			edi.SEG("DTP");
			edi.SF(1, "356");
			edi.ST(2, "D8");
			edi.DC(3, policyStart);
			edi.EOS();
		}
	}

	protected void DTP_retirement_date() {
		if (retirementDate != 0) {
			edi.SEG("DTP");
			edi.SF(1, "286");
			edi.ST(2, "D8");
			edi.DC(3, retirementDate);
			edi.EOS();
		}
	}

	//TODO: determine if spouse if enrolled in benefit
	protected void INS_maintenance_reason_code() {
		if (hasSpouse) {
			edi.SEG("INS");
			edi.ST(4, "11");
			edi.ST(5, "S");
			edi.EOS();
		}
	}

	protected void REF_SSN_subscriber() {
		edi.SEG("REF");
		edi.SF(1, "0F");//Subscriber Number Qualifier
		edi.SF(2, stripDashes(subscriberIdentifier));
		edi.EOS();
	}

	protected void REF_HIC_number() {
		if(/*(coveredAge > 64 || handicap.equals("Y")) &&*/ !isEmpty(hicNumber)) //if HIC number has been entered, send it on feed
		{
			edi.SEG("REF");
			edi.SF(1, "F6");
			edi.SF(2, stripDashes(hicNumber));
			edi.EOS();
		}
	}

	protected void REF_subscriber_group_number() {
		edi.SEG("REF");
		edi.SF(1, "1L");
		edi.SF(2, groupNumber);
		edi.EOS();
	}

	protected void REF_subscriber_subgroup_number() {
		edi.SEG("REF");
		edi.SF(1, "DX");
		edi.SF(2, subGroupNumber);
		edi.EOS();
	}

	protected void REF_subscriber_class() {
		edi.SEG("REF");
		edi.SF(1, "17");
		edi.SF(2, benefitClassId);
		edi.EOS();
	}

	protected void NM1_member_name() {
		edi.SEG("NM1");
		edi.SF(1, "IL");
		edi.SF(2, "1");
		edi.SF(3, memberLastName);
		edi.SF(4, memberFirstName);
		if (!isEmpty(memberMiddleName)) {
			edi.SF(5, memberMiddleName);
			if(!"000000000".equals(stripDashes(depSSN)) && !isEmpty(depSSN)) {
				edi.SF(6, "");
				edi.SF(7, "");
				edi.SF(8, "34");
				edi.SF(9, stripDashes(depSSN));
			}
		}
		else if(!"000000000".equals(stripDashes(depSSN)) && !isEmpty(depSSN)) {
			edi.SF(5, "");
			edi.SF(6, "");
			edi.SF(7, "");
			edi.SF(8, "34");
			edi.SF(9, stripDashes(depSSN));
		}

		edi.EOS();
	}

	protected void PER_contact_info() {
		String home, cell;
		home = fixPhoneNumber(homePhone);
		cell = fixPhoneNumber(cellPhone);

		if(!StringUtils.isEmpty(home) || !StringUtils.isEmpty(cell)) {
			edi.SEG("PER");
			edi.ST(1, "IP");
			edi.ST(2, "");
			edi.ST(3, "HP");
			if(!StringUtils.isEmpty(home))
				edi.ST(4, home.replaceAll(" ", "").replaceAll("\\*",""));
			else if(!StringUtils.isEmpty(cell))
				edi.ST(4, cell.replaceAll(" ", "").replaceAll("\\*",""));
			edi.EOS();
		}
	}

	protected void N3_address_information() {
		//shift address if messed up
		if (memberAddressLine1 == null || memberAddressLine1.equals("")) {
			memberAddressLine1 = memberAddressLine2;
			memberAddressLine2 = "";
		}

//		if (memberAddressLine1.length() > 24) {
//			String temp = memberAddressLine1.trim() + " " + memberAddressLine2.trim();
//
//			StringTokenizer st = new StringTokenizer(temp, " ");
//			memberAddressLine1 = "";
//			memberAddressLine2 = "";
//
//			String token = "";
//			while (st.hasMoreTokens()) {
//				token = st.nextToken();
//
//				if ((memberAddressLine1.length() + token.length()) < 24)
//					memberAddressLine1 += token;
//				else
//					break;
//			}
//
//			while (st.hasMoreTokens())
//				memberAddressLine2 += st.nextToken();
//		}

		if(!isEmpty(memberAddressLine1) || !isEmpty(memberAddressLine2)) {
			edi.SEG("N3");
			edi.SF(1, memberAddressLine1);
			if (!isEmpty(memberAddressLine2))
				edi.SF(2, memberAddressLine2);
			edi.EOS();
		}
	}

	protected void N4_geographic_location() {
		if(!isEmpty(memberAddressCity) || !isEmpty(memberAddressState) || !isEmpty(memberAddressZip)) {
			edi.SEG("N4");
			edi.SF(1, memberAddressCity);
			if(!isEmpty(memberAddressState) || !isEmpty(memberAddressZip)) {
				edi.SF(2, memberAddressState);
				if(!isEmpty(memberAddressZip))
				edi.SF(3, stripDashes(memberAddressZip).trim());
			}
			edi.EOS();
		}
	}

	protected void DMG_demographic_info() {
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

	protected void HLH_health_information() {
		edi.SEG("HLH");
		edi.ST(1, smoker == 'Y' ? "T" : "N");
		edi.EOS();
	}

	protected void DSB_disability_info() {
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
	protected void HD_health_coverage() {
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
				edi.SF(3, "HLT"); //major medical for EBC
				edi.SF(4, benefitName);
				break;
			case HrBenefitCategory.PRESCRIPTION:
				edi.SF(3, "PDG"); //major medical for EBC
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
		edi.EOS();
	}

	protected void HD_RX_other(String plan) {

		edi.SEG("HD");
		edi.SF(1, "001");
		edi.ST(2, "");
		edi.ST(3, "PDG");
		edi.SF(4, plan);
		if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
			edi.SF(5, coverageLevelCode);
		edi.EOS();
	}

	protected void HD_other(String code) {
		edi.SEG("HD");
		edi.SF(1, "001");
		edi.ST(2, "");
		edi.ST(3, code);
		edi.SF(4, "HP");
		edi.EOS();
	}

	protected void HD_other(String code, String code2) {
		edi.SEG("HD");
		edi.SF(1, "001");
		edi.ST(2, "");
		edi.ST(3, code);
		edi.SF(4, "HP" + "=" + code2 + "00");
		edi.EOS();
	}

	protected void DTP_benefit_start_date() {

		edi.SEG("DTP");
		edi.SF(1, "348");
		edi.ST(2, "D8");
		if ("".equals(coverageStartDate))
			logger.info("Coverage start date missing");

		edi.SF(3, coverageStartDate);
		edi.EOS();
	}

	protected void DTP_benefit_end_date()
	{
		if ("024".equals(maintenanceTypeCode))
		{
			edi.SEG("DTP");
			edi.SF(1, "349");
			edi.ST(2, "D8");
			if ("".equals(coverageEndDate))
				if("".equals(String.valueOf(policyEnd)))
					logger.info("Coverage end date missing");
				else
					edi.SF(3, String.valueOf(policyEnd));
			else
				edi.SF(3, coverageEndDate);
			edi.EOS();
		}
	}

	protected void IDC_identification_card()
	{
		edi.SEG("IDC");
		edi.SF(1, "YYYA");
		edi.SF(2, "H");
		edi.EOS();
	}

    protected void transactionSetTrailer(int transactionSetNumber) {
        edi.SEG("SE");
        edi.NL(1, edi.X12_numb_segments);
        edi.ST(2, EDI.makecn(transactionSetNumber));
        edi.EOS();
    }

    protected void functionalGroupTrailer(String transactionSetNumber) {
        edi.SEG("GE");
        edi.NL(1, edi.X12_numb_st_segments);
        edi.ST(2, removeLeadingZeros(transactionSetNumber));
        edi.EOS();
    }

    protected void functionInterchangeTrailer(String icn) {
        edi.SEG("IEA");
        edi.NL(1, edi.X12_numb_gs_segments);
        edi.ST(2, icn);
        edi.EOS();
    }

	protected void functionInterchangeTrailer(String icn, int benefitCount) {
        edi.SEG("IEA");
        edi.NL(1, benefitCount);
        edi.ST(2, icn);
        edi.EOS();
    }

    protected String removeLeadingZeros(String s) {
		int index = 0;
		for(int i = 0; i < s.length(); i++)
			if(s.charAt(i) != '0') {
				index = i;
				break;
			}

        if (index >= s.length())
            return "0";

        return s.substring(index, s.length());
	}

    protected String stripDashes(String s) {
		s = s.trim().replaceAll("-", "");

        return s;
	}

    protected String fixPhoneNumber(String s) {
		if(!isEmpty(s)) {
			char[] number = s.toCharArray();
			List<Character> fixed = new ArrayList<Character>();
			s = "";
			for(int i = 0; i < number.length; i++)
				if(Character.isDigit(number[i]))
					fixed.add(number[i]);
			for(Character c : fixed)
				s += (char)c;
			return s;
		}
		
		return s;
	}

	protected boolean isValidSSN(String s) {
		for(char c : s.toCharArray())
			if(!Character.isDigit(c))
				return false;
		if(s.length() != 9)
			return false;
		else if(s.equals("999999999"))
			return false;
		else if(s.equals("000000000"))
			return false;
		else
			return true;
	}

	protected static String stripParatheses(String s) {
        String ret = "";

        if (s == null)
            return "";

        StringTokenizer toks = new StringTokenizer(s, "(");
        while (toks.hasMoreTokens())
            ret += toks.nextToken();

		if(ret == null)
			return "";
		StringTokenizer toks2 = new StringTokenizer(ret, ")");
		ret = "";
        while (toks2.hasMoreTokens())
            ret += toks2.nextToken();

        return ret;
    }

	protected class DependentComparator implements Comparator {
		@Override
		public int compare(Object j1, Object j2) {
			String depId1;
			String depId2;

			if(j1 instanceof HrBenefitJoinH) {
				HrBenefitJoinH h1 = (HrBenefitJoinH)j1;
				HrBenefitJoinH h2 = (HrBenefitJoinH)j2;
				depId1 = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.BENEFIT_JOIN_ID, h1.getBenefitJoinId())
																 .eq(HrBenefitJoinH.PAYING_PERSON_ID, h1.getPayingPersonId())
																 .selectFields(HrBenefitJoinH.RELATIONSHIP_ID)
																 .stringVal();
				depId2 = hsu.createCriteria(HrBenefitJoinH.class).eq(HrBenefitJoinH.BENEFIT_JOIN_ID, h2.getBenefitJoinId())
																 .eq(HrBenefitJoinH.PAYING_PERSON_ID, h2.getPayingPersonId())
																 .selectFields(HrBenefitJoinH.RELATIONSHIP_ID)
																 .stringVal();
			}
			else {
				HrBenefitJoin h1 = (HrBenefitJoin)j1;
				HrBenefitJoin h2 = (HrBenefitJoin)j2;
				depId1 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_JOIN_ID, h1.getBenefitJoinId())
																.joinTo(HrBenefitJoin.RELATIONSHIP)
																.selectFields(HrEmplDependent.RELATIONSHIP_ID)
																.stringVal();
				depId2 = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_JOIN_ID, h2.getBenefitJoinId())
																.joinTo(HrBenefitJoin.RELATIONSHIP)
																.selectFields(HrEmplDependent.RELATIONSHIP_ID)
																.stringVal();
			}
			
			if(isEmpty(depId1))
				if(isEmpty(depId2))
					return 0;
				else
					return -1;
			else if(isEmpty(depId2))
				return 1;

			BHREmplDependent dep1 = new BHREmplDependent(depId1);
			BHREmplDependent dep2 = new BHREmplDependent(depId2);
			if(dep1.getRelationshipType().equals("S"))
				return -1;
			else if(dep2.getRelationshipType().equals("S"))
				return 1;
			else {
				IHrBenefitJoin i1 = (IHrBenefitJoin)j1;
				IHrBenefitJoin i2 = (IHrBenefitJoin)j2;

				if(i1.getPolicyStartDate() > i2.getPolicyStartDate())
					return -1;
				else if(i1.getPolicyStartDate() < i2.getPolicyStartDate())
					return 1;
				else
					return i1.getCoveredPersonId().compareTo(i2.getCoveredPersonId());
			}
		}
	}


}
