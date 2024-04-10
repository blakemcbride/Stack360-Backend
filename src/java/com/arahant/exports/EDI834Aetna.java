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
import org.kissweb.DelimitedFileReader;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.ListUtils;

public class EDI834Aetna extends AEDI834 {

	public EDI834Aetna(String fileType)
	{
		super();
		this.fileType = fileType;
		ediLog = new EDILog("Aetna_EDI");
		vendorCode = "Aetna";
		logger = new ArahantLogger(EDI834Aetna.class);
	}

    public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) throws CloneNotSupportedException {
        List<String> filenames = new LinkedList<String>();
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
		List<String> benefitIds = new ArrayList<String>();
		for(HrBenefit b : benefitsToDo)
			benefitIds.add(b.getBenefitId());
		List<HrBenefit> emptyActiveBenefits = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefitIds)
																				 .joinTo(HrBenefit.HR_BENEFIT_CONFIGS).sizeEq(HrBenefitConfig.HR_BENEFIT_JOINS, 0)
																				 .list();
		List<HrBenefit> emptyInactiveBenefits = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefitIds)
																				   .joinTo(HrBenefit.HR_BENEFIT_CONFIGS).sizeEq(HrBenefitConfig.HR_BENEFIT_JOINS_H, 0)
																				   .list();
		List<HrBenefit> emptyBenefits = ListUtils.intersection(emptyActiveBenefits, emptyInactiveBenefits);

		benefitsToDo.removeAll(emptyBenefits);

		account = bv.getAccountNumber();
		int benefitCount = 0;
		String filename = "";
		try {
			filename = new BProperty("LargeReportDir").getValue() + File.separator + "EDI" + vendor.getInterchangeReceiverId().replace('-', '_') + "_" + icn + "_" + DateUtils.now() + "_" + (filenames.size() + 1);
			edi = new EDI(filename);
		} catch (IOException ex) {
			throw new ArahantException("Can't create EDI file");
		}
		edi.X12_setup('*', '~', '\n');
		isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId());

        for (HrBenefit bene : benefitsToDo) {
            gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());

            header(transactionSetNumber, vendorCode, vendor.getInterchangeSenderId(), cd.getName(), vendor.getInterchangeReceiverId(), gcn); //cd.getName() cd.getFederalEmployerId()

			try {
				doInactives(bene); //deal with history records - policies that have ended
				doActives(bene);
			}
			catch(IOException ioe) {
				System.out.println("Could not write to logfile.");
			}

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
		}
		catch (IOException ex) {
			//Logger.getLogger(EDI834Humana.class.getName()).log(Level.SEVERE, null, ex);
		}
        tran.setTransactionSetNumber(transactionSetNumber - 1);
        tran.setGCN(gcn - 1);
        tran.setICN(icn);

        mils = new Date().getTime() - mils;

        logger.info("took " + (mils / 1000) + " seconds");

		if(lp != null)
			lp.packageDone();
        return filenames.toArray(new String[filenames.size()]);
    }

    private void doInactives(HrBenefit bene) throws CloneNotSupportedException, IOException {
        int count = 0;
        benefitName = bene.getName();
		String currentSubscriber = "";

		HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
        hcu.joinTo(HrBenefitJoinH.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
        hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
		hcu.gtJoinedField(HrBenefitJoinH.POLICY_END_DATE, HrBenefitJoinH.POLICY_START_DATE);
        hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
        hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);
//		hcu.setMaxResults(1);

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();

        HibernateScrollUtil<HrBenefitJoinH> hscr = hcu.scroll();
		List<HrBenefitJoinH> depJoins = new ArrayList<HrBenefitJoinH>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);
			
            HrBenefitJoinH bj = hscr.get();

			if(!(bj.getPayingPerson() instanceof Employee)) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			maintenanceTypeCode = "024";

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				currentSubscriber = bj.getPayingPersonId();

				Collections.sort(depJoins, comparator);
				setCoverageLevel(depJoins);
				for(HrBenefitJoinH h : depJoins) {
					try {
						if(h.getCoverageEndDate() == 0)
							h.setCoverageEndDate(h.getPolicyEndDate());
						setBenefitJoinData(h, benefitType, true);
						ediMemberLevelDetail(true);
					}
					catch(BranchCodeException b) {
						System.out.println(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
						ediLog.write(new BPerson(h.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
						continue;
					}
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
        }

        hscr.close();
    }

    private List<String> doActives(HrBenefit bene) throws CloneNotSupportedException, IOException {
        int count = 0;
        benefitName = bene.getName();
		String currentSubscriber = "";

		HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
        hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);
		hcu.gtOrEq(HrBenefitJoin.COVERAGE_END_DATE, DateUtils.now(), 0);
//		hcu.setMaxResults(1);

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE)
																 .joinTo(HrBenefitCategory.HRBENEFIT)
																 .eq(HrBenefit.BENEFITID, bene.getBenefitId())
																 .intValue();
        HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();
		List<HrBenefitJoin> depJoins = new ArrayList<HrBenefitJoin>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);

//			if(count > 6100) {
            HrBenefitJoin bj = hscr.get();

			int covdate = bj.getCoverageStartDate();
            int covend = bj.getCoverageEndDate();
            if (covdate != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "021";
			else if (covend != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "024";
			else if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().after(lastDumpDate))
                maintenanceTypeCode = "024";
			else if (bj.getRecordChangeDate().after(lastDumpDate))
				maintenanceTypeCode = "001";
			else if (covend != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

            if (bj.getPolicyEndDate() != 0 && bj.getRecordChangeDate().before(lastDumpDate))
                continue;

			if(!(bj.getPayingPerson() instanceof Employee)) {
				logger.info(bj.getPayingPerson().getNameFML() + " was not transmitted because they are paying for a policy, but is not an employee.");
				continue;
			}

			if(!bj.getPayingPersonId().equals(currentSubscriber)) {
				currentSubscriber = bj.getPayingPersonId();
				Collections.sort(depJoins, comparator);
				setCoverageLevel(depJoins);
				for(HrBenefitJoin j : depJoins) {
					try {
						if(j.getCoverageEndDate() == 0)
							j.setCoverageEndDate(j.getPolicyEndDate());
						setBenefitJoinData(j, benefitType, true);
						ediMemberLevelDetail(true);
					}
					catch(BranchCodeException b) {
						System.out.println(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
						ediLog.write(new BPerson(j.getCoveredPersonId()).getNameFML() + " was not sent due to missing requisite data.");
						continue;
					}
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
//			}
		}
        hscr.close();

        return payingPersons;
    }

	protected void ediMemberLevelDetail(boolean fake) throws BranchCodeException {
		INS_insured_benefit();
		REF_SSN_subscriber();
//		REF_HIC_number();
		//REF_subscriber_group_number();
		//REF_subscriber_class();
		//REF_subscriber_subgroup_number();
		//IDC_identification_card();
//		DTP_employment_date();
//		DTP_employment_term_date();
//		DTP_retirement_date();
		NM1_member_name();
		PER_contact_info();
		N3_address_information();
		N4_geographic_location();
		DMG_demographic_info();
		HD_health_coverage();
		DTP_benefit_start_date();
		DTP_benefit_end_date();
		if(lp != null)
			REF_1L_structure();
	}

	protected void setBenefitJoinData(IHrBenefitJoin bj, int benefitType, boolean fake) throws BranchCodeException {
		BPerson p = new BPerson(bj.getPayingPersonId());
		BEmployee emp = null;
		if (p.isEmployee())
			emp = new BEmployee(bj.getPayingPersonId());
		BPerson d = new BPerson(bj.getCoveredPersonId());
		String benefitId = bj.getHrBenefitConfig().getHrBenefit().getBenefitId();
		BHRBenefit bene = new BHRBenefit(benefitId);

		setLocation(bj);
		setRelationshipCode(bj);
//		setPCP(bj);
		setEmploymentCode(bj, emp.getPersonId());
		setPayingPerson(bj.getPayingPersonId(), benefitId);
		setCoveredPerson(bj.getCoveredPersonId());
		setAddress(p, d);
		setMaritalStatus(emp.getPersonId());
		if(lp != null)
			setBranchCode(emp.getPersonId(), bj);
		setDates(bj);

		HrBenefitConfig hbc = hsu.get(HrBenefitConfig.class, bj.getHrBenefitConfigId());
		insuranceLineCode = hbc.getHrBenefit().getInsuranceCode();
		benefitStatusCode = (bj.getUsingCOBRA() == 'Y') ? "C" : "A";
		changeReason = bj.getChangeDescription();
		benefitName = bene.getGroupId() + bene.getGroupAccountId();
	}

	@Override
	protected void setPayingPerson(String payingPersonId, String benefitId) {
		Person payingPerson = new BPerson(payingPersonId).getPerson();
//		System.out.println(payingPerson.getNameFML());
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
        benefitClassId = bhr.getCoveredUnderCOBRA() ? "C001" : "A001";

		BEmployee be = new BEmployee(payingPerson.getPersonId());
		medicareType = be.getMedicare() + "";
		if(medicareType.equals("2"))
			medicareType = "C";
		else if(medicareType.equals("U"))
			medicareType = "D";
		else if(medicareType.equals("N"))
			medicareType = "E";
		hicNumber = isEmpty(payingPerson.getHicNumber()) ? "" : payingPerson.getHicNumber();

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

		payingAge = new BPerson(payingPerson).getAgeAsOf(DateUtils.now());
	}

	protected <T extends IHrBenefitJoin> void setCoverageLevel(List<T> bj) {
		if(!bj.isEmpty()) {
			if(bj.size() == 1)
				coverageLevelCode = "EMP";
			else if(bj.size() > 2) {
				List<HrEmplDependent> spouse = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.EMPLOYEE, bj.get(1).getPayingPerson())
																						.eq(HrEmplDependent.RELATIONSHIP_TYPE, 'S')
																						.list();
				if(spouse == null || spouse.isEmpty())
					coverageLevelCode = "ECH";
				else
					coverageLevelCode = "FAM";
			}
			else {
				try {
				HrEmplDependent dep = hsu.createCriteria(HrEmplDependent.class).eq(HrEmplDependent.PERSON, bj.get(1).getCoveredPerson())
																			   .eq(HrEmplDependent.EMPLOYEE, bj.get(1).getPayingPerson()).first();
				char relationship = dep.getRelationshipType();
				if(relationship == 'C')
					coverageLevelCode = "ECH";
				else
					coverageLevelCode = "ESP";
				}
				catch (Exception ex) {
					for(int i = 0; i < bj.size(); i++)
						System.out.println("    " + bj.get(i).getPayingPerson().getNameFML() + ", " + (bj.get(i).getCoveredPerson().getNameFML()));
				}
			}
		}
	}

	@Override
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

	protected void setBranchCode(String empId, IHrBenefitJoin bj) throws ArahantException, BranchCodeException {
		BEmployee emp = new BEmployee(empId);
		BOrgGroup bog = new BOrgGroup(emp.getOrgGroupAssociations().iterator().next().getOrgGroup());
		String empStatusId = "";

		if(bj instanceof HrBenefitJoinH)
			empStatusId = emp.getLastActiveStatusHistory().getStatusId();
		else
			empStatusId = emp.getEmployeeStatusId();


		String branch = lp.executeLispReturnString("GET-DIVISION-CODE", bj.getHrBenefitConfig().getHrBenefit().getBenefitId(), bog.getOrgGroupId(), empStatusId);
		while (isEmpty(branch)) {
			bog = bog.getParent();
			if (bog.getParent() == null)
				break;
			branch = lp.executeLispReturnString("GET-DIVISION-CODE", bj.getHrBenefitConfig().getHrBenefit().getBenefitId(), bog.getOrgGroupId(), empStatusId);
		}
//		if(isEmpty(branch))
//			throw new BranchCodeException();
		this.branch = branch;
	}

	@Override
	protected void setMaritalStatus(String empId) {
		BEmployee emp = new BEmployee(empId);
		maritalStatusCode = "I";

		if (emp != null)
			if (emp.getActiveSpouse(DateUtils.now()) != null)
				maritalStatusCode = "M";
			else if (emp.getActiveSpouse(0) != null)
				maritalStatusCode = "U";
	}

	@Override
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
        edi.ST(12, "00501");
        edi.ST(13, icn);
        edi.CH(14, '1');
        edi.CH(15, test ? 'T' : 'P');
        edi.ST(16, ">");
        edi.EOS();
    }

	@Override
	protected void gs_seg(String gcn, String applicationSenderId, String applicationRecieverId) {
        edi.SEG("GS");
        edi.ST(1, "BE");
        edi.ST(2, stripDashes(applicationSenderId));  //applicationSenderId "382479423"
        edi.ST(3, stripDashes(applicationRecieverId));  //applicationRecieverId "382069753"
        edi.D8(4);
        edi.CT(5);
        edi.ST(6, removeLeadingZeros(gcn));
        edi.CH(7, 'X');
        edi.ST(8, "005010X220A1");
        edi.EOS();
    }

	@Override
	protected void header(int transactionSetNumber, String vendor, String vendorId, String sponsorName, String sponsorId, int gcn) {
        edi.SEG("ST");
        edi.ST(1, "834");
        edi.ST(2, EDI.makecn(transactionSetNumber));
        edi.ST(3, "005010X220A1");
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

//		String customerNumber = groupAccountId + /*HMOCompanyCode + */groupNumber;
//        edi.SEG("REF");
//        edi.ST(1, "38");
//        edi.ST(2, customerNumber);
//        edi.EOS();

//        edi.SEG("DTP");
//        edi.SF(1, "007");
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

	protected void REF_subscriber_group_account() {
		edi.SEG("REF");
		edi.SF(1, "1L");
		edi.SF(2, groupAccountId);
		edi.EOS();
	}

	protected void REF_subscriber_group_vendor() {
		edi.SEG("REF");
		edi.SF(1, "DX");
		edi.SF(2, groupVendorId);
		edi.EOS();
	}

	protected void REF_insurance_code() {
		edi.SEG("REF");
		edi.SF(1, "17");
		edi.SF(2, insuranceLineCode);
		edi.EOS();
	}
	
	protected void REF_1L_structure() throws BranchCodeException {
		if(isEmpty(branch)) {
			System.out.println("NO BRANCH CODE GENERATED: " + memberFirstName + " " + memberLastName + ": " + branch + "/" + plan + " " + payingAge);
			throw new BranchCodeException("NO BRANCH CODE GENERATED: " + memberFirstName + " " + memberLastName + ": " + branch + "/" + plan + " " + payingAge);
		}
		String code = "";
		String planKey = branch.substring(0,2);
		if(planKey.equals("10") || planKey.equals("40")) {
			if(payingAge > 64)
				if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
					plan += "C";
				else
					plan += "R";
		}
		if(memberAddressState.equals("NC") || memberAddressState.equals("CA"))
			if(Character.isDigit(plan.charAt(plan.length() - 1)))
				plan = plan.replaceAll(plan.substring(plan.length() - 1), "C" + plan.substring(plan.length() - 1));

		code += ((account + "       ").substring(0, 7));
		code += ((branch + "      ").substring(0, 6));
		code += ((plan + "     ").substring(0, 5));

		edi.SEG("REF");
		edi.SF(1, "1L");
		edi.ST(2, code); //account number (7), branch(6, fill spaces), benefit option(5, fill spaces), network code(5 , optional)
		edi.EOS();
	}

	@Override
	protected void DTP_benefit_start_date() {
		int modifiedCoverageStartDate = 0;
		if(Integer.valueOf(coverageStartDate) < 20110701) {
			if(20110701 < policyStart)
				modifiedCoverageStartDate = policyStart;
			else
				modifiedCoverageStartDate = 20110701;
		}
		else
			modifiedCoverageStartDate = Integer.valueOf(coverageStartDate);

		edi.SEG("DTP");
		edi.SF(1, "348");
		edi.ST(2, "D8");
		if ("".equals(coverageStartDate))
			logger.info("Coverage start date missing");
		edi.SF(3, String.valueOf(modifiedCoverageStartDate));
		edi.EOS();
	}

	class BranchCodeException extends Exception {
		public BranchCodeException() {}

		public BranchCodeException(String msg) {
			super(msg);
		}
	}


    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
		/****
		 * Test code for LISP functionality
		 ****/
//		ABCL.init();
//		LispPackage lp = new LispPackage("WmCo Insurance Division Codes", "com/arahant/lisp/WmCo-Insurance-Division-Codes");
//		String val = lp.executeLispReturnString("GET-DIVISION-CODE", "00001-0000000023", "00001-0000000010", "00001-0000000002");
//		lp.packageDone();
//		System.out.println(val);

		/****
		 * Main method to import in HIC humbers
		 * from WMCO-supplied spreadsheet (5 - 10 minutes)
		 ****/
		HibernateSessionUtil hsu = ArahantSession.getHSU();
		hsu.setCurrentCompany(new BCompany("00001-0000000000").getBean());
		List<Employee> emps = hsu.createCriteria(Employee.class).list();
		HashMap<String, Employee> empHash = new HashMap<String, Employee>();
		for(Employee e : emps)
			empHash.put(e.getUnencryptedSsn(), e);

		DelimitedFileReader fr = new DelimitedFileReader("/home/xichen/Downloads/HIC Number file.csv");
		Set<String> ssns = empHash.keySet();

		hsu.beginTransaction();
		fr.nextLine();
		while(fr.nextLine()) {
			if(ssns.contains(fr.getString(2))) {
				BEmployee be = new BEmployee(empHash.get(fr.getString(2)));
//				be.setHicNumber(stripDashes(fr.getString(3)));
				be.setMedicare(fr.getString(4).charAt(0));
				be.update();
			}
		}
		hsu.commitTransaction();
		hsu.close();
    }
}
