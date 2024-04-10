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
import com.arahant.utils.DateUtils;
import com.arahant.utils.HibernateCriteriaUtil;
import com.arahant.utils.HibernateScrollUtil;
import com.arahant.utils.HibernateSessionUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.ListUtils;

public class EDI834BCBS extends AEDI834 {

	public EDI834BCBS(String fileType)
	{
		super();
		this.fileType = fileType;
		ediLog = new EDILog("BCBS_EDI");
		vendorCode = "BCBS OF MI";
		logger = new ArahantLogger(EDI834BCBS.class);
	}

	public String[] dumpEDI(CompanyBase vendor, BEDITransaction tran, boolean debug) {
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
        hsu.setCurrentCompany(cd);

        List<HrBenefit> benefitsToDo = hsu.createCriteria(HrBenefit.class).eq(HrBenefit.BENEFIT_PROVIDER, vendor).list();
		List<String> benefitIds = new ArrayList<String>();
		for(HrBenefit b : benefitsToDo)
			benefitIds.add(b.getBenefitId());
		List<HrBenefit> emptyActiveBenefits = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefitIds)
																				 .joinTo(HrBenefit.HR_BENEFIT_CONFIGS).isNull(HrBenefitConfig.HR_BENEFIT_JOINS)
																				 .list();
		List<HrBenefit> emptyInactiveBenefits = hsu.createCriteria(HrBenefit.class).in(HrBenefit.BENEFITID, benefitIds)
																				   .joinTo(HrBenefit.HR_BENEFIT_CONFIGS).isNull(HrBenefitConfig.HR_BENEFIT_JOINS_H)
																				   .list();
		List<HrBenefit> emptyBenefits = ListUtils.intersection(emptyActiveBenefits, emptyInactiveBenefits);
		benefitsToDo.removeAll(emptyBenefits);

		String filename = "";

		try {
			filename = new BProperty("LargeReportDir").getValue() + File.separator + bv.getAccountNumber() + ".edi.txt";
			edi = new EDI(filename);
		} catch (IOException ex) {
			throw new ArahantException("Can't create EDI file");
		}
		edi.X12_setup('*', '~', '\n');
		boolean companyIsSender = cd.getFederalEmployerId().equals(bv.getFederalEmployerId());
		isa_seg(EDI.makecn(icn), debug, vendor.getInterchangeSenderId(), vendor.getInterchangeReceiverId(), companyIsSender);

        for (HrBenefit bene : benefitsToDo) {
            gs_seg(EDI.makecn(gcn), vendor.getApplicationSenderId(), vendor.getApplicationReceiverId());
            header(transactionSetNumber, vendorCode, vendor.getInterchangeSenderId(), cd.getName(), vendor.getInterchangeReceiverId(), gcn);

            doInactives(hsu, bene); //deal with history records - policies that have ended
            doActives(hsu, bene);

            transactionSetTrailer(transactionSetNumber);
            transactionSetNumber++;
            functionalGroupTrailer(EDI.makecn(gcn));
            gcn++;
        }
		functionInterchangeTrailer(EDI.makecn(icn));
		edi.close();
        filenames.add(filename);

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
        benefitName = bene.getName();
		String currentSubscriber = "";
        int count = 0;

        HibernateCriteriaUtil<HrBenefitJoinH> hcu = hsu.createCriteria(HrBenefitJoinH.class);
        hcu.orderBy(HrBenefitJoinH.PAYING_PERSON_ID);
        hcu.eq(HrBenefitJoinH.RECORD_CHANGE_TYPE, 'D');
        hcu.ne(HrBenefitJoinH.POLICY_END_DATE, 0);
        hcu.gt(HrBenefitJoinH.RECORD_CHANGE_DATE, lastDumpDate);
        hcu.joinTo(HrBenefitJoinH.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoinH.COVERAGE_START_DATE, 0);

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
					if(h.getCoverageEndDate() == 0)
						h.setCoverageEndDate(h.getPolicyEndDate());
					try {
						setBenefitJoinData(h, benefitType);
					}
					catch (Throwable t) {
						continue;
					}
					ediMemberLevelDetail();
				}
				depJoins.clear();
				flushData();
			}

			depJoins.add(bj);
        }

        hscr.close();
    }

    private List<String> doActives(HibernateSessionUtil hsu, HrBenefit bene) {
        int count = 0;
        benefitName = bene.getName();
		String currentSubscriber = "";

        HibernateCriteriaUtil<HrBenefitJoin> hcu = hsu.createCriteria(HrBenefitJoin.class).eq(HrBenefitJoin.BENEFIT_DECLINED, 'N').eq(HrBenefitJoin.APPROVED, 'Y');
        hcu.orderBy(HrBenefitJoin.POLICY_START_DATE);
        hcu.joinTo(HrBenefitJoin.PAYING_PERSON).orderBy(Person.PERSONID);
        hcu.joinTo(HrBenefitJoin.HR_BENEFIT_CONFIG).eq(HrBenefitConfig.HR_BENEFIT, bene);
        hcu.ne(HrBenefitJoin.COVERAGE_START_DATE, 0);

        benefitType = hsu.createCriteria(HrBenefitCategory.class).selectFields(HrBenefitCategory.TYPE).joinTo(HrBenefitCategory.HRBENEFIT).eq(HrBenefit.BENEFITID, bene.getBenefitId()).intValue();

        HibernateScrollUtil<HrBenefitJoin> hscr = hcu.scroll();
		List<HrBenefitJoin> depJoins = new ArrayList<HrBenefitJoin>();

        while (hscr.next()) {
            if (++count % 50 == 0)
                logger.info(count);

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
				flushData();
				currentSubscriber = bj.getPayingPersonId();
				Collections.sort(depJoins, comparator);
				for(HrBenefitJoin j : depJoins) {
					if(j.getCoverageEndDate() == 0)
						j.setCoverageEndDate(j.getPolicyEndDate());
					setBenefitJoinData(j, benefitType);
					ediMemberLevelDetail();
				}
				depJoins.clear();
			}
			depJoins.add(bj);
			continue;
        }
        hscr.close();

        return payingPersons;
    }

	@Override
	public void ediMemberLevelDetail() {
		//Check for missing data here and write to log if there is
//		if (allDataValid())
//		{
			boolean COBRA = INS_insured_benefit_return();
			REF_SSN_subscriber();
			REF_subscriber_group_number();
			DTP_employment_date();//COBRA);
			DTP_eligibility_start_date();//COBRA);
			DTP_employment_term_date();//COBRA);
			NM1_member_name();
			//PER_contact_info();  BCBS says not to send this
			N3_address_information();
			N4_geographic_location();
			DMG_demographic_info();
			HD_health_coverage();
			DTP_benefit_start_date();
			//REF_subscriber_group_number();

			//TODO: may not need these
//            INS_maintenance_reason_code();
//            REF_medicare_HIC_number();
//            HLH_health_information();
//            DSB_disability_info();
//            DTP_benefit_end_date();
//		}
	}

	protected <T extends IHrBenefitJoin> void setCoverageLevel(List<T> bj) {
		if(!bj.isEmpty()) {
			if(bj.size() == 1)
				coverageLevelCode = "EMP";
			else if(bj.size() > 2)
				coverageLevelCode = "FAM";
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

	private void isa_seg(String icn, boolean test, String interchangeSenderId, String interchangeReceiverId, boolean companyIsSender) {
        edi.SEG("ISA");
        edi.ST(1, "00");
        edi.SW(2, "", 10);
        edi.ST(3, "00");
        edi.SW(4, "", 10);
        edi.ST(5, (companyIsSender ? "30" : "ZZ"));
        edi.SW(6, stripDashes(interchangeSenderId), 15);
        edi.ST(7, (companyIsSender ? "30" : "ZZ"));
        edi.SW(8, stripDashes(interchangeReceiverId), 15);
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

	@Override
	protected void header(int transactionSetNumber, String vendor, String payerId, String sponsorName, String sponsorId, int gcn) {
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
		edi.ST(8, fileType.equals("F") ? "4" : "2"); //2 is changes  4 is full file
		edi.EOS();

		edi.SEG("REF");
		edi.ST(1, "38");
		edi.ST(2, "MOS"); // was PPO
		edi.EOS();

		edi.SEG("DTP");
		edi.SF(1, "007");  //was 303
		edi.ST(2, "D8");
		edi.SF(3, DateUtils.getDateCCYYMMDD(DateUtils.now()));
		edi.EOS();

		edi.SEG("N1");
		edi.ST(1, "P5");
		edi.ST(2, sponsorName);
		edi.ST(3, "ZZ");  // the validator testing online says to use ZZ but the contact says to use FI
		edi.ST(4, payerId);
		edi.EOS();

		edi.SEG("N1");
		edi.ST(1, "IN");
		edi.ST(2, vendor);
		edi.ST(3, "FI");
		edi.ST(4, sponsorId);
		edi.EOS();
	}

	public boolean INS_insured_benefit_return() {
		subscriber = stripDashes(depSSN).equals(stripDashes(subscriberIdentifier));
		boolean COBRA = false;

		edi.SEG("INS");

		edi.ST(1, subscriber ? "Y" : "N");
		edi.ST(2, relationshipCode);

		edi.ST(3, "030"); //was maintenanceTypeCode, sending 030 because it is an audit file

		edi.ST(4, "XN"); //means not giving reason for change

		//edi.ST(4, "");
		edi.ST(5, benefitStatusCode);
		edi.ST(6, "");
		if (benefitStatusCode.equals("C")) {
			String code = "1";
			COBRA = true;
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

		return COBRA;
	}

	@Override
	public void REF_subscriber_group_number() {
		edi.SEG("REF");
		edi.SF(1, "1L");

		if (!isEmpty(groupNumber))
		{
			if (!isEmpty(subGroupNumber))
				edi.SF(2, groupNumber + " " + subGroupNumber);
			else
				edi.SF(2, groupNumber);
		}
		else
		{
			edi.SF(2, "");
//			logger.info("No Group Number for " + personId);
		}
		//was 53770000
		edi.EOS();
	}

	public void DTP_eligibility_start_date() {
		if (eligibilityStartDate != 0) {
			edi.SEG("DTP");
			edi.SF(1, "356");
			edi.ST(2, "D8");
			edi.DC(3, Integer.valueOf(eligibilityStartDate));
			edi.EOS();
		}
	}
	
	@Override
	protected void HD_health_coverage() {
		edi.SEG("HD");
		edi.SF(1, maintenanceTypeCode);
		edi.SF(2, "");

		switch (benefitType) {
			case HrBenefitCategory.DENTAL:
				edi.SF(3, "DEN");  
				edi.SF(4, groupAccountId);
				break;
			case HrBenefitCategory.HEALTH:
				edi.SF(3, "PPO"); 
				edi.SF(4, groupAccountId);
				break;
			case HrBenefitCategory.LONG_TERM_CARE:
				edi.SF(3, "LTC");
				edi.SF(4, groupAccountId);
				break;
			case HrBenefitCategory.SHORT_TERM_CARE:
				edi.SF(3, "STC");
				edi.SF(4, groupAccountId);
				break;
			case HrBenefitCategory.VISION:
				edi.SF(3, "VIS");
				edi.SF(4, groupAccountId);
				break;
			default:
				edi.SF(3, insuranceLineCode);
				edi.SF(4, groupAccountId);
		}

		if (stripDashes(depSSN).equals(stripDashes(subscriberIdentifier)))
			edi.SF(5, coverageLevelCode);
		edi.EOS();
	}
}
